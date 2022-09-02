package xyz.n7mn.dev.checkclient.packet;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.login.client.WrapperLoginClientLoginStart;
import com.github.retrooper.packetevents.wrapper.login.server.WrapperLoginServerPluginRequest;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPluginMessage;
import io.netty.buffer.Unpooled;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.n7mn.dev.checkclient.CheckClient;
import xyz.n7mn.dev.checkclient.data.PlayerData;
import xyz.n7mn.dev.checkclient.data.PlayerDataUtil;
import xyz.n7mn.dev.checkclient.type.ClientType;
import xyz.n7mn.dev.checkclient.util.bytebuf.MinecraftByteBuf;
import xyz.n7mn.dev.checkclient.util.pair.Pair;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class PacketEvent implements PacketListener {

    //try {
    //
    //                WrappedPacketInCustomPayload wrapper = new WrappedPacketInCustomPayload(event.getNMSPacket());
    //
    //                String channel = wrapper.getChannelName();
    //
    //                String client = new String(wrapper.getData(), StandardCharsets.UTF_8).replaceAll("[^A-z: ]", "");
    //
    //                if (data.getClientData().getType() == ClientType.UNKNOWN
    //                        && !data.getClientData().isReceivedMCBrand()
    //                        && (channel.equalsIgnoreCase("minecraft:brand") || channel.equalsIgnoreCase("MC|Brand"))) {
    //
    //
    //                    for (CheckMCBrandAbstract check : CheckClientInstance.INSTANCE.getMCBrands()) {
    //                        ClientType clientType = check.run(data, client);
    //
    //                        if (clientType != ClientType.UNKNOWN) {
    //                            data.getClientData().getClientTypes().add(clientType);
    //
    //                            break;
    //                        }
    //                    }
    //
    //                    data.getClientData().setMCBrandResult(data.getClientData().getType());
    //                    data.getClientData().setReceivedMCBrand(true);
    //                }
    //
    //                if (channel.equalsIgnoreCase("FML|HS")) {
    //                    if (data.getClientData().isSendedHandShake()) {
    //                        data.getModData().setMod(ForgeUtils.readModBytes(wrapper.getData()));
    //
    //                        data.getClientData().setReceivedHandShake(true);
    //                    }
    //                } else if (channel.equalsIgnoreCase("feather:client")) {
    //                    FeatherUtils.readFeatherMod(data, wrapper.getData());
    //
    //                    data.getModData().setReceivedFeatherModData(true);
    //                } else if (channel.equalsIgnoreCase("labymod3:main")
    //                        && data.getClientData().getType() == ClientType.VANILLA) {
    //                    data.getClientData().setType(ClientType.LABY_MOD);
    //
    //                    //LabyUtils.readLabyMod(data, wrapper.getData());
    //                } else if (channel.equalsIgnoreCase("REGISTER")) {
    //                    if (client.equalsIgnoreCase("lunarclient:pm")) {
    //                        data.getClientData().setReceivedLunarChannel(true);
    //                    } else if (client.equalsIgnoreCase("thezigmod:zig_set")
    //                            && data.getClientData().getType() == ClientType.VANILLA) {
    //                        data.getClientData().setType(ClientType.ZIG_MOD);
    //                    }
    //                }
    //
    //                Bukkit.getLogger().info("Client:" + client);
    //
    //                Bukkit.getLogger().info("Client:" + data.getClientData().getClientTypes());
    //            } catch (Exception ex) {
    //                data.getClientData().setType(ClientType.MODIFIED_CLIENT);
    //            }
    //
    //            //new WrappedPacketOutMapChunk()


    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.PLUGIN_MESSAGE) {
            WrapperPlayClientPluginMessage wrapper = new WrapperPlayClientPluginMessage(event);

            onPluginMessageReceived(PlayerDataUtil.getPlayerData(event.getUser()), event, wrapper, new String(wrapper.getData(), StandardCharsets.UTF_8));
        } else if (event.getPacketType() == PacketType.Login.Client.LOGIN_START) {
            WrapperLoginClientLoginStart wrapper = new WrapperLoginClientLoginStart(event);

            onLoginStartEvent(PlayerDataUtil.getPlayerData(event.getUser()), event, wrapper);
            Bukkit.getLogger().info(event.getUser().getClientVersion().toString());
        }
    }

    public void onPluginMessageReceived(PlayerData data, PacketReceiveEvent event, WrapperPlayClientPluginMessage wrapper, String client) {
        if (wrapper.getChannelName().equalsIgnoreCase("MC|Brand")) {
            if (!data.getClientData().isReceivedMCBrand()) {
                ConfigurationSection yaml = CheckClient.INSTANCE.getConfig().getConfigurationSection("clientSettings");

                for (String key : yaml.getKeys(false)) {
                    List<String> typeOf = yaml.getStringList(key + ".name");

                    if (typeOf.stream().anyMatch(c -> c.equalsIgnoreCase(client))) {
                        data.getClientData().getClientTypes().add(key);
                    }
                }
                data.getClientData().setReceivedMCBrand(true);
            } else {
                //illegal action report to server owner
            }
        }
        Bukkit.getLogger().info(data.getClientData().getClientTypes().toString());
        Bukkit.getLogger().info(wrapper.getChannelName());
    }

    public void onLoginStartEvent(PlayerData data, PacketReceiveEvent event, WrapperLoginClientLoginStart wrapper) {
        if (event.getUser().getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13) && event.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_13)) {
            if (event.getUser().getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_17)) {
                event.getUser().sendPacket(new WrapperLoginServerPluginRequest(401, "fml:handshake", PacketEvents.getAPI().getNettyManager().getByteBufOperator().array(emulateFMLv3(emulateFMLv3(new MinecraftByteBuf(Unpooled.buffer()))))));
            } else {
                event.getUser().sendPacket(new WrapperLoginServerPluginRequest(402, "fml:handshake", new byte[]{1, 0, 0, 0}));
            }
        } else if (event.getUser().getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_12)) {
            event.getUser().sendPacket(new WrapperPlayServerPluginMessage("FML|HS", new byte[]{-2, 0}));
            event.getUser().sendPacket(new WrapperPlayServerPluginMessage("FML|HS", new byte[]{0, 2, 0, 0, 0, 0}));
            event.getUser().sendPacket(new WrapperPlayServerPluginMessage("FML|HS", new byte[]{2, 0, 0, 0, 0}));
        }
        data.getClientData().setSendedHandShake(true);
    }

    @Override
    public void onUserDisconnect(UserDisconnectEvent event) {
        PlayerDataUtil.removePlayerData(event.getUser());
    }

    @Override
    public void onUserConnect(UserConnectEvent event) {

    }

    @Override
    public void onUserLogin(UserLoginEvent event) {

    }

    /*private Map<Object, ClientVersion> hashMap = new HashMap<>();*/

    /*@Override
    public void onPacketLoginReceive(PacketLoginReceiveEvent event) {
        try {
            if (event.getPacketId() == PacketType.Login.Client.CUSTOM_PAYLOAD) {
                event.setCancelled(true);

                WrappedPacketLoginInCustomPayload wrapper = new WrappedPacketLoginInCustomPayload(event.getNMSPacket());

                if (wrapper.getMessageId() == 113 || wrapper.getMessageId() == 117) {
                    MinecraftByteBuf buf = new MinecraftByteBuf(Unpooled.wrappedBuffer(wrapper.getData()));

                    List<String> mods = readModList(buf);
                    mods.forEach(r -> System.out.println("mods: " + r));
                }
            } else if (event.getPacketId() == PacketType.Login.Client.START) {
                ClientVersion version = hashMap.get(event.getChannel());

                if (version.isOlderThan(ClientVersion.v_1_17)) {
                    PacketEvents.get().getPlayerUtils().sendPacket(event.getChannel(), new WrappedPacketLoginOutCustomPayload(1_13, "fml:handshake", new byte[]{1, 0, 0, 0}));
                } else {
                    MinecraftByteBuf buf = emulateFMLv3(new MinecraftByteBuf(Unpooled.buffer()));
                    byte[] bytes = PacketEvents.get().getByteBufUtil().getBytes(buf.getBuf());

                    WrappedPacketLoginOutCustomPayload wrapper = new WrappedPacketLoginOutCustomPayload(1_17, "fml:handshake", bytes);
                    PacketEvents.get().getPlayerUtils().sendPacket(event.getChannel(), wrapper);
                }

                hashMap.remove(event.getChannel());
            }
        } catch (Exception ex) {
            //memory leak fix
            hashMap.remove(event.getChannel());
        }
    }*/

    public static Map<String, String> channels = new HashMap<>();

    public static List<String> dataPackRegistries = new ArrayList<>();

    public static Map<String, Pair<String, String>> mods = new HashMap<>();

    static {
        channels.put("fml:loginwrapper", "FML3");
        channels.put("forge:tier_sorting", "1.0");
        channels.put("fml:handshake", "FML3");
        channels.put("minecraft:unregister", "FML3");
        channels.put("fml:play", "FML3");
        channels.put("minecraft:register", "FML3");
        channels.put("forge:split", "1.1");

        dataPackRegistries.add("minecraft:block");
        dataPackRegistries.add("minecraft:fluid");
        dataPackRegistries.add("minecraft:item");
        dataPackRegistries.add("minecraft:mob_effect");
        dataPackRegistries.add("minecraft:sound_event");
        dataPackRegistries.add("minecraft:potion");
        dataPackRegistries.add("minecraft:enchantment");
        dataPackRegistries.add("minecraft:entity_type");
        dataPackRegistries.add("minecraft:block_entity_type");
        dataPackRegistries.add("minecraft:particle_type");
        dataPackRegistries.add("minecraft:menu");
        dataPackRegistries.add("minecraft:painting_variant");
        dataPackRegistries.add("minecraft:recipe_serializer");
        dataPackRegistries.add("minecraft:stat_type");
        dataPackRegistries.add("minecraft:villager_profession");
        dataPackRegistries.add("forge:data_serializers");
        dataPackRegistries.add("forge:fluid_type");

        //randoms
        mods.put("minecraft", Pair.of("Minecraft", "1.19"));
        mods.put("forge", Pair.of("Forge", "41.0.45"));
    }

    /*
     * Todo: emulate Mod Data? I think impossibly!
     */
    public MinecraftByteBuf emulateModData(MinecraftByteBuf buf) {
        //I don't know what is this
        //maybe packet id.
        buf.writeVarInt(5);

        //normal forge mods!
        buf.writeMap(mods, (o, s) -> o.writeUtf(s, 0x100), (o, p) -> {
            o.writeUtf(p.getLeft(), 0x100);
            o.writeUtf(p.getRight(), 0x100);
        });

        return buf;
    }

    public MinecraftByteBuf emulateFMLv3(MinecraftByteBuf buf) {
        //I don't know what is this
        //maybe packet id.
        buf.writeVarInt(1);

        //mods
        buf.writeVarInt(2);

        //normal forge mods!
        buf.writeUtf("minecraft", 0x100);
        buf.writeUtf("forge", 0x100);

        //channels
        buf.writeVarInt(channels.size());
        channels.forEach((k, v) -> {
            buf.writeUtf(k);
            buf.writeUtf(v, 0x100);
        });

        //datapacks
        buf.writeVarInt(dataPackRegistries.size());
        dataPackRegistries.forEach(buf::writeUtf);

        return buf;
    }

    public List<String> readModList(MinecraftByteBuf buf) {
        List<String> mods = new ArrayList<>();

        //mod count
        buf.readVarInt();

        final int len = buf.readVarInt();
        for (int i = 0; i < len; i++) {
            final String mod = buf.readUtf(0x100);

            if (i != 0) {
                mods.add(mod);
            }
        }

        return mods;
    }

    public Map<String, Pair<String, String>> readModData(MinecraftByteBuf buf) {
        return buf.readMap(o -> o.readUtf(0x100), o -> Pair.of(o.readUtf(0x100), o.readUtf(0x100)));
    }

    public void readyAlertModerator(PlayerData data) {

    }
}