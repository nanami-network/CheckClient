package xyz.n7mn.dev.checkclient.packet;

import com.lunarclient.bukkitapi.nethandler.client.LCPacketModSettings;
import com.lunarclient.bukkitapi.nethandler.client.obj.ModSettings;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.*;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.handshaking.setprotocol.WrappedPacketHandshakingInSetProtocol;
import io.github.retrooper.packetevents.packetwrappers.login.in.custompayload.WrappedPacketLoginInCustomPayload;
import io.github.retrooper.packetevents.packetwrappers.login.out.custompayload.WrappedPacketLoginOutCustomPayload;
import io.github.retrooper.packetevents.packetwrappers.play.in.custompayload.WrappedPacketInCustomPayload;
import io.github.retrooper.packetevents.packetwrappers.play.out.custompayload.WrappedPacketOutCustomPayload;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.Bukkit;
import xyz.n7mn.dev.checkclient.CheckClientInstance;
import xyz.n7mn.dev.checkclient.check.CheckMCBrandAbstract;
import xyz.n7mn.dev.checkclient.data.PlayerData;
import xyz.n7mn.dev.checkclient.data.PlayerDataUtil;
import xyz.n7mn.dev.checkclient.lunar.LunarClient;
import xyz.n7mn.dev.checkclient.type.ClientType;
import xyz.n7mn.dev.checkclient.util.FeatherUtils;
import xyz.n7mn.dev.checkclient.util.ForgeUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Map<Object, ClientVersion> hashMap = new HashMap<>();

    @Override
    public void onPacketLoginReceive(PacketLoginReceiveEvent event) {
        try {
            if (event.getPacketId() == PacketType.Login.Client.CUSTOM_PAYLOAD) {

                WrappedPacketLoginInCustomPayload wrapper = new WrappedPacketLoginInCustomPayload(event.getNMSPacket());

                if (wrapper.getMessageId() == 1212) {
                    Bukkit.getLogger().info("wrapper:" + new String(wrapper.getData(), StandardCharsets.UTF_8));
                }

                Bukkit.getLogger().info("packet:" + "incoming!");

            } else if (event.getPacketId() == PacketType.Login.Client.START) {
                ClientVersion version = hashMap.get(event.getChannel());

                if (version.isNewerThanOrEquals(ClientVersion.v_1_13) && PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_13)) {
                    if (version.isOlderThan(ClientVersion.v_1_17)) {
                        PacketEvents.get().getPlayerUtils().sendPacket(event.getChannel(), new WrappedPacketLoginOutCustomPayload(1212, "fml:handshake", new byte[]{1, 0, 0, 0}));
                    } else {
                        //PacketEvents.get().getPlayerUtils().sendPacket(event.getChannel(), new WrappedPacketLoginOutCustomPayload(1212, "fml:handshake", new byte[]{2, 0, 0, 0}));
                        //todo: v1_17 handshake
                    }
                }

                hashMap.remove(event.getChannel());
            }
        } catch (Exception ex) {
            //memory leak fix
            hashMap.remove(event.getChannel());
        }
    }

    @Override
    public void onPacketHandshakeReceive(PacketHandshakeReceiveEvent event) {
        WrappedPacketHandshakingInSetProtocol wrapper = new WrappedPacketHandshakingInSetProtocol(event.getNMSPacket());

        final ClientVersion version = ClientVersion.getClientVersion(wrapper.getProtocolVersion());

        hashMap.put(event.getChannel(), version);
    }

    @Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {
        if (event.getPacketId() == PacketType.Login.Server.CUSTOM_PAYLOAD) {
            WrappedPacketLoginOutCustomPayload wrapper = new WrappedPacketLoginOutCustomPayload(event.getNMSPacket());

            Bukkit.getLogger().info("wrapper:" + Arrays.toString(wrapper.getData()));
        }
    }

    @Override
    public void onPostPacketPlayReceive(PostPacketPlayReceiveEvent event) {
        if (event.getPacketId() == PacketType.Login.Server.CUSTOM_PAYLOAD) {
            WrappedPacketLoginOutCustomPayload wrapper = new WrappedPacketLoginOutCustomPayload(event.getNMSPacket());

            Bukkit.getLogger().info("wrapper:" + Arrays.toString(wrapper.getData()));
        }
    }

    @Override
    public void onPacketLoginSend(PacketLoginSendEvent event) {
        if (event.getPacketId() == PacketType.Login.Server.DISCONNECT) {
            hashMap.remove(event.getChannel());
        }
    }

    public void readyAlertModerator(PlayerData data) {

    }
}