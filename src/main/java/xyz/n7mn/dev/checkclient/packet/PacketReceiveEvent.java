package xyz.n7mn.dev.checkclient.packet;

import com.lunarclient.bukkitapi.nethandler.client.LCPacketModSettings;
import com.lunarclient.bukkitapi.nethandler.client.obj.ModSettings;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.custompayload.WrappedPacketInCustomPayload;
import io.github.retrooper.packetevents.packetwrappers.play.out.custompayload.WrappedPacketOutCustomPayload;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import xyz.n7mn.dev.checkclient.data.PlayerData;
import xyz.n7mn.dev.checkclient.data.PlayerDataUtil;
import xyz.n7mn.dev.checkclient.lunar.LunarClient;
import xyz.n7mn.dev.checkclient.type.ClientType;
import xyz.n7mn.dev.checkclient.util.FeatherUtils;
import xyz.n7mn.dev.checkclient.util.ForgeUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class PacketReceiveEvent extends PacketListenerAbstract {

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        if (event.getPacketId() == PacketType.Play.Client.CUSTOM_PAYLOAD) {
            PlayerData data = PlayerDataUtil.getPlayerData(event.getPlayer());

            WrappedPacketInCustomPayload wrapper = new WrappedPacketInCustomPayload(event.getNMSPacket());

            String channel = wrapper.getChannelName();

            String client = new String(wrapper.getData(), StandardCharsets.UTF_8).replaceAll("[^A-z: ]", "");

            if (data.getClientData().getType() == ClientType.UNKNOWN &&
                    (channel.equalsIgnoreCase("minecraft:brand") || channel.equalsIgnoreCase("MC|Brand"))) {

                if (client.equalsIgnoreCase("forge")) {
                    data.getClientData().setType(ClientType.FORGE_1_13_ABOVE);

                    if (!data.getClientData().isSendedHandShake() && PacketEvents.get().getServerUtils().getVersion().isNewerThan(ServerVersion.v_1_12_2)) {
                        //todo: fake handshake
                    }

                    data.getClientData().setSendedHandShake(true);
                } else if (client.equalsIgnoreCase("fml:forge")) {
                    data.getClientData().setType(ClientType.FORGE_1_13_BELOW);

                    if (!data.getClientData().isSendedHandShake()) {
                        PacketEvents.get().getPlayerUtils().sendPacket(event.getPlayer(), new WrappedPacketOutCustomPayload("FML|HS", new byte[]{-2, 0}));
                        PacketEvents.get().getPlayerUtils().sendPacket(event.getPlayer(), new WrappedPacketOutCustomPayload("FML|HS", new byte[]{0, 2, 0, 0, 0, 0}));
                        PacketEvents.get().getPlayerUtils().sendPacket(event.getPlayer(), new WrappedPacketOutCustomPayload("FML|HS", new byte[]{2, 0, 0, 0, 0}));

                        data.getClientData().setSendedHandShake(true);
                    }
                } else if (client.equalsIgnoreCase("fabric")) {
                    data.getClientData().setType(ClientType.FABRIC);
                } else if (client.startsWith("lunar")) {
                    data.getClientData().setType(ClientType.LUNAR);

                    ModSettings.ModSetting disabled = new ModSettings.ModSetting(false, new HashMap<>());

                    LunarClient.sendLCPacketModSettingsPacket(event.getPlayer(), new LCPacketModSettings(new ModSettings()
                            .addModSetting("freelook", disabled)));

                } else if (client.startsWith("Feather Forge")) {
                    data.getClientData().setType(ClientType.FEATHER_FORGE);
                } else if (client.startsWith("Feather Fabric")) {
                    data.getClientData().setType(ClientType.FEATHER_FABRIC);
                } else if (client.equalsIgnoreCase("plc")) {
                    data.getClientData().setType(ClientType.PVP_LOUNGE);
                } else if (client.equalsIgnoreCase("blueberry")) {
                    data.getClientData().setType(ClientType.BLUEBERRY);
                } else if (client.equalsIgnoreCase("vanilla")) {
                    data.getClientData().setType(ClientType.VANILLA);
                }

                data.getClientData().setReceivedMCBrand(true);
            }

            if (channel.equalsIgnoreCase("FML|HS")) {
                if (data.getClientData().isSendedHandShake()) {
                    data.getModData().setMod(ForgeUtils.readModBytes(wrapper.getData()));
                }
            } else if (channel.equalsIgnoreCase("feather:client")) {
                FeatherUtils.readFeatherMod(data, wrapper.getData());

                data.getModData().setReceivedFeatherModData(true);
            } else if (channel.equalsIgnoreCase("labymod3:main")
                    && data.getClientData().getType() == ClientType.VANILLA) {
                data.getClientData().setType(ClientType.LABY_MOD);

                //LabyUtils.readLabyMod(data, wrapper.getData());
            } else if (channel.equalsIgnoreCase("REGISTER")) {
                if (client.equalsIgnoreCase("lunarclient:pm")) {
                    data.getClientData().setReceivedLunarChannel(true);
                } else if (client.equalsIgnoreCase("thezigmod:zig_set")
                    && data.getClientData().getType() == ClientType.VANILLA) {
                    data.getClientData().setType(ClientType.ZIG_MOD);
                }
            }
        }
    }

    public void readyAlertModerator(PlayerData data) {

    }
}
