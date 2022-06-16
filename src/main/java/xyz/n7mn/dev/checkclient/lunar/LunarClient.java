package xyz.n7mn.dev.checkclient.lunar;

import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.nethandler.LCPacket;
import com.lunarclient.bukkitapi.nethandler.client.LCPacketModSettings;
import com.lunarclient.bukkitapi.nethandler.client.obj.ModSettings;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.play.out.custompayload.WrappedPacketOutCustomPayload;
import org.bukkit.entity.Player;
import xyz.n7mn.dev.checkclient.data.PlayerDataUtil;
import xyz.n7mn.dev.checkclient.type.ClientType;

public class LunarClient {

    public static void sendLCPacketModSettingsPacket(Player player, LCPacketModSettings packet) {
        if (LunarClientAPI.getInstance() != null
                && PlayerDataUtil.getPlayerData(player).getClientData().getType() == ClientType.LUNAR) {
            PacketEvents.get().getPlayerUtils().sendPacket(player, new WrappedPacketOutCustomPayload("lunarclient:pm"
                    , LCPacket.getPacketData(packet)));
        }
    }
}
