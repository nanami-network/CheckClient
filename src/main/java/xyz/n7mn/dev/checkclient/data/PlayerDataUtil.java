package xyz.n7mn.dev.checkclient.data;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataUtil {

    private static Map<UUID, PlayerData> hashMap = new HashMap<>();

    public static PlayerData getPlayerData(Player player) {
        PlayerData data = hashMap.get(player.getUniqueId());

        return data == null ? createPlayerData(player) : data;
    }

    public static PlayerData createPlayerData(Player player) {
        hashMap.put(player.getUniqueId(), new PlayerData(player));

        return getPlayerData(player);
    }

    public static void removePlayerData(Player player) {
        hashMap.remove(player.getUniqueId());
    }
}
