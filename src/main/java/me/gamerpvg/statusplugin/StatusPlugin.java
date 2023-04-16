package me.gamerpvg.statusplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class StatusPlugin extends JavaPlugin implements Listener {
    private Map<UUID, Boolean> playerStatusMap;
    private Map<UUID, BukkitRunnable> playerRunnableMap;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        playerStatusMap = new HashMap<>();
        playerRunnableMap = new HashMap<>();


        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        playerStatusMap.put(playerId, true);
        updateTabList(player, true);

    }
    @EventHandler
    public void onPlayerMove (PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        boolean status = playerStatusMap.get(playerId);
        playerStatusMap.put(playerId, true);
        updateTabList(player, status);
        BukkitRunnable runnable = playerRunnableMap.get(playerId);
        if (runnable != null) {
            runnable.cancel();
        }
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                playerStatusMap.put(playerId, false);
                updateTabList(player, false);

            }
        };
        playerRunnableMap.put(playerId, runnable);
        runnable.runTaskLater(this, 1200);
    }
    private void updateTabList(Player player, boolean status) {
        String playerName = player.getName();
        String statusString = status ? ChatColor.GREEN + "● " : ChatColor.YELLOW + "● ";
        player.setPlayerListName(statusString + ChatColor.RESET + playerName);
    }
}
