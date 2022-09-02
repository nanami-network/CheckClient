package xyz.n7mn.dev.checkclient.data;

import com.github.retrooper.packetevents.protocol.player.User;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataUtil {

    private static final Map<User, PlayerData> data = new HashMap<>();

    public static PlayerData getPlayerData(User user) {
        PlayerData playerData = data.get(user);

        return playerData == null ? createPlayerData(user) : playerData;
    }

    public static PlayerData createPlayerData(User user) {
        return data.put(user, new PlayerData(user));
    }


    public static void removePlayerData(User user) {
        data.remove(user);
    }
}
