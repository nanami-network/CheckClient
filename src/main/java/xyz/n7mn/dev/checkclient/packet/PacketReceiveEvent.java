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
import xyz.n7mn.dev.checkclient.CheckClientInstance;
import xyz.n7mn.dev.checkclient.check.CheckMCBrandAbstract;
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
            try {

                WrappedPacketInCustomPayload wrapper = new WrappedPacketInCustomPayload(event.getNMSPacket());

                String channel = wrapper.getChannelName();

                String client = new String(wrapper.getData(), StandardCharsets.UTF_8).replaceAll("[^A-z: ]", "");

                if (data.getClientData().getType() == ClientType.UNKNOWN
                        && !data.getClientData().isReceivedMCBrand()
                        && (channel.equalsIgnoreCase("minecraft:brand") || channel.equalsIgnoreCase("MC|Brand"))) {


                    for (CheckMCBrandAbstract check : CheckClientInstance.INSTANCE.getMCBrands()) {
                        ClientType clientType = check.run(data, client);

                        if (clientType != ClientType.UNKNOWN) {
                            data.getClientData().setType(clientType);

                            break;
                        }
                    }

                    data.getClientData().setMCBrandResult(data.getClientData().getType());
                    data.getClientData().setReceivedMCBrand(true);
                }

                if (channel.equalsIgnoreCase("FML|HS")) {
                    if (data.getClientData().isSendedHandShake()) {
                        data.getModData().setMod(ForgeUtils.readModBytes(wrapper.getData()));

                        data.getClientData().setReceivedHandShake(true);
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
            } catch (Exception ex) {
                data.getClientData().setType(ClientType.MODIFIED_CLIENT);
            }
        }
    }

    public void readyAlertModerator(PlayerData data) {

    }
}