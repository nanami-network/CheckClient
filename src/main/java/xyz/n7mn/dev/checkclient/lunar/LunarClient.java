package xyz.n7mn.dev.checkclient.lunar;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPluginMessage;
import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.nethandler.LCPacket;
import com.lunarclient.bukkitapi.nethandler.client.LCPacketModSettings;

import org.bukkit.entity.Player;
import xyz.n7mn.dev.checkclient.data.PlayerDataUtil;
import xyz.n7mn.dev.checkclient.type.ClientType;

public class LunarClient {

    /*public static void sendLCPacketModSettingsPacket(Player player, LCPacketModSettings packet) {
        if (LunarClientAPI.getInstance() != null
                && PlayerDataUtil.getPlayerData(player).getClientData().getType() == ClientType.LUNAR) {
            PacketEvents.getAPI().getPlayerManager().sendPacket(player, new WrapperPlayServerPluginMessage("lunarclient:pm"
                    , LCPacket.getPacketData(packet)));
        }
    }*/
}
