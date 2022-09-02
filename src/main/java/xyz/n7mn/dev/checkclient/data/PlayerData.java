package xyz.n7mn.dev.checkclient.data;

import com.github.retrooper.packetevents.protocol.player.User;
import org.bukkit.entity.Player;
import xyz.n7mn.dev.checkclient.data.impl.ClientData;
import xyz.n7mn.dev.checkclient.data.impl.ModData;
import xyz.n7mn.dev.checkclient.type.ClientType;

public class PlayerData {

    private User user;
    private final ClientData clientData = new ClientData(this);
    private final ModData modData = new ModData(this);

    public PlayerData(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ClientData getClientData() {
        return clientData;
    }

    public ModData getModData() {
        return modData;
    }
}
