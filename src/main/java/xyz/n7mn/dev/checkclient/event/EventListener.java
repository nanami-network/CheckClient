package xyz.n7mn.dev.checkclient.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;
import xyz.n7mn.dev.checkclient.data.PlayerData;
import xyz.n7mn.dev.checkclient.data.PlayerDataUtil;

public class EventListener implements Listener, PluginMessageListener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        PlayerData data = PlayerDataUtil.getPlayerData(e.getPlayer());
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        PlayerDataUtil.removePlayerData(e.getPlayer());
    }

    @EventHandler
    public void onP(PlayerRegisterChannelEvent event) {

    }

    @Override
    public void onPluginMessageReceived(@NotNull String s, @NotNull Player player, @NotNull byte[] bytes) {
        Bukkit.broadcastMessage("incoming:" + new String(bytes));
    }
}
