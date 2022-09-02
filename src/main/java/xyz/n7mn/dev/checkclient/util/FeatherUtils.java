package xyz.n7mn.dev.checkclient.util;

import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;
import xyz.n7mn.dev.checkclient.data.PlayerData;

import java.nio.charset.StandardCharsets;

public class FeatherUtils {

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
