package xyz.n7mn.dev.checkclient;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.github.retrooper.packetevents.PacketEventsPlugin;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.n7mn.dev.checkclient.event.EventListener;
import xyz.n7mn.dev.checkclient.packet.PacketEvent;
import xyz.n7mn.dev.checkclient.placeholder.PlaceHolderTests;

import java.util.ArrayList;
import java.util.List;

public final class CheckClient extends JavaPlugin {

    List<PlaceholderExpansion> placeholderExpansions = new ArrayList<>();

    public static CheckClient INSTANCE;

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        INSTANCE = this;

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeholderExpansions.add(new PlaceHolderTests());

            //todo:
            placeholderExpansions.forEach(PlaceholderExpansion::register);
        }

        saveDefaultConfig();


        PacketEvents.getAPI()
                .getEventManager()
                .registerListener(new PacketEvent(), PacketListenerPriority.LOWEST);

        PacketEvents.getAPI().init();

        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
        // Plugin shutdown logic
    }
}
