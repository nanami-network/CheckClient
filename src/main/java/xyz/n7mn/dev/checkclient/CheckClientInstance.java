package xyz.n7mn.dev.checkclient;

import com.lunarclient.bukkitapi.LunarClientAPI;
import org.bukkit.Bukkit;

import java.util.Objects;

public enum CheckClientInstance {

    INSTANCE;
    private CheckClient plugin = (CheckClient) Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("CheckClient"));

    public CheckClient getPlugin() {
        return plugin;
    }

    public void setPlugin(CheckClient plugin) {
        this.plugin = plugin;
    }

}
