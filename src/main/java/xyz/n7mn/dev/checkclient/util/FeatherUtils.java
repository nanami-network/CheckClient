package xyz.n7mn.dev.checkclient.util;

import org.json.JSONArray;
import org.json.JSONObject;
import xyz.n7mn.dev.checkclient.data.PlayerData;

import java.nio.charset.StandardCharsets;

public class FeatherUtils {

    public static void safeReadFeatherMod(PlayerData data, byte[] bytes) {
        try {
            JSONObject object = new JSONObject(new String(bytes, StandardCharsets.UTF_8));

            if (object.has("packetType") && object.optString("packetType").equalsIgnoreCase("CLIENT_HELLO")) {
                if (object.has("payload")) {

                    JSONObject payload = object.optJSONObject("payload");

                    if (payload != null) {
                        JSONArray featherMods = payload.optJSONArray("featherMods");

                        if (featherMods != null) {
                            for (Object ob : featherMods) {
                                if (ob instanceof String) data.getModData().getFeatherMod().add((String) ob);
                            }
                        }

                        JSONArray mods = payload.optJSONArray("mods");

                        if (mods != null) {
                            for (Object ob : mods) {
                                if (ob instanceof JSONObject) {
                                    JSONObject mod = (JSONObject) ob;

                                    String name = mod.optString("name");
                                    String version = mod.optString("version");

                                    data.getModData().getMod().put(name, version);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            //ignored
        }
    }

    public static void readFeatherMod(PlayerData data, byte[] bytes) {
        try {
            String json = new String(bytes, StandardCharsets.UTF_8);

            JSONObject object = new JSONObject(json);

            JSONObject payload = object.getJSONObject("payload");

            for (Object ob : payload.getJSONArray("featherMods")) {
                if (ob instanceof String) data.getModData().getFeatherMod().add((String) ob);
            }

            JSONArray mods = payload.getJSONArray("mods");

            for (int i = 1; i < mods.length(); i++) {
                JSONObject mod = mods.getJSONObject(i);

                String name = mod.getString("name");
                String version = mod.getString("version");

                data.getModData().getMod().put(name, version);
            }
        } catch (Exception ex) {
            //ignored
        }
    }
}
