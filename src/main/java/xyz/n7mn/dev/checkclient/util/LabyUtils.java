package xyz.n7mn.dev.checkclient.util;

import org.json.JSONArray;
import org.json.JSONObject;
import xyz.n7mn.dev.checkclient.data.PlayerData;

import java.nio.charset.StandardCharsets;

public class LabyUtils {
    public static void readLabyMod(PlayerData data, byte[] bytes) {
        try {
            String json = new String(bytes, StandardCharsets.UTF_8);

            if (json.contains("INFO")) {
                JSONObject object = new JSONObject(json.replaceAll("[^(,\\s*\"atr :.|?=})]", ""));

                JSONArray addons = object.getJSONArray("addons");
                JSONArray mods = object.getJSONArray("mods");

                for (int i = 0; i < addons.length(); i++) {
                    JSONObject addonInfo = addons.getJSONObject(i);

                    String uuid = addonInfo.getString("uuid");
                    String name = addonInfo.getString("name");

                    data.getModData().getLabyAddons().put(name, uuid);
                }

                for (int i = 0; i < mods.length(); i++) {
                    JSONObject modInfo = mods.getJSONObject(i);

                    String hash = modInfo.getString("hash");
                    String name = modInfo.getString("name");

                    data.getModData().getMod().put(name, hash);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //ignored
        }
    }
}
