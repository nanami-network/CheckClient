package xyz.n7mn.dev.checkclient.util;

import io.github.retrooper.packetevents.utils.player.ClientVersion;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ForgeUtils {
    public static String getHandShake(ClientVersion version) {
        return version.isNewerThanOrEquals(ClientVersion.v_1_13) ? "fml:handshake" : "FML|HS";
    }

    /**
     * https://www.spigotmc.org/threads/forge-mod-list-packet-reading.308465/
     */
    public static Map<String, String> readModBytes(byte[] data) {
        Map<String, String> mods = new HashMap<>();

        boolean store = false;
        String modName = null;

        for (int i = 2; i < data.length; store = !store) {
            int end = i + data[i] + 1;
            byte[] range = Arrays.copyOfRange(data, i + 1, end);

            String string = new String(range, StandardCharsets.UTF_8);

            if (store) {
                mods.put(modName, string);
            } else {
                modName = string;
            }

            i = end;
        }

        return mods;
    }
}