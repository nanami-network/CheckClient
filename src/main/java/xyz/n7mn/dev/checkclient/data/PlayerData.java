package xyz.n7mn.dev.checkclient.data;

import org.bukkit.entity.Player;
import xyz.n7mn.dev.checkclient.data.impl.ClientData;
import xyz.n7mn.dev.checkclient.data.impl.ModData;
import xyz.n7mn.dev.checkclient.type.ClientType;

public class PlayerData {

    private Player player;
    private final ClientData clientData = new ClientData(this);
    private final ModData modData = new ModData(this);

    public PlayerData(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public ClientData getClientData() {
        return clientData;
    }

    public ModData getModData() {
        return modData;
    }
}
