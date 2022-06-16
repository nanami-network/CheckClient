package xyz.n7mn.dev.checkclient;

import com.lunarclient.bukkitapi.LunarClientAPI;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.n7mn.dev.checkclient.event.EventListener;
import xyz.n7mn.dev.checkclient.packet.PacketReceiveEvent;
import xyz.n7mn.dev.checkclient.placeholder.PlaceHolderTests;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class CheckClient extends JavaPlugin {

    List<PlaceholderExpansion> placeholderExpansions = new ArrayList<>();

    @Override
    public void onLoad() {
        PacketEvents.create(this);
    }

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeholderExpansions.add(new PlaceHolderTests());

            //todo:
            placeholderExpansions.forEach(PlaceholderExpansion::register);
        }

        saveDefaultConfig();

        PacketEvents.get().registerListener(new PacketReceiveEvent());
        PacketEvents.get().init();

        Bukkit.getPluginManager().registerEvents(new EventListener(), this);

        Bukkit.getMessenger().registerIncomingPluginChannel(this, "fml:handshake", new EventListener());
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "fml:handshake");

        Bukkit.getMessenger().registerIncomingPluginChannel(this, "fml:handshake", new EventListener());
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "fml:handshake");
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}