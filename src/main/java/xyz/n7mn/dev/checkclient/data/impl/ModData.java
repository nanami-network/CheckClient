package xyz.n7mn.dev.checkclient.data.impl;

import xyz.n7mn.dev.checkclient.data.PlayerData;
import xyz.n7mn.dev.checkclient.type.ClientType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModData {

    private PlayerData data;

    private Map<String, String> mod = new HashMap<>();

    private final List<String> featherMod = new ArrayList<>();
    private final Map<String, String> labyAddons = new HashMap<>();

    private boolean isReceivedFeatherModData;

    public ModData(PlayerData data) {
        this.data = data;
    }

    /**
     * @see ClientData#isSendedHandShake()
     */
    public Map<String, String> getMod() {
        return mod;
    }

    public void setMod(Map<String, String> mod) {
        this.mod = mod;
    }

    public List<String> getFeatherMod() {
        return featherMod;
    }

    public boolean isReceivedFeatherModData() {
        return isReceivedFeatherModData;
    }

    public void setReceivedFeatherModData(boolean receivedFeatherModData) {
        isReceivedFeatherModData = receivedFeatherModData;
    }

    public Map<String, String> getLabyAddons() {
        return labyAddons;
    }
}
