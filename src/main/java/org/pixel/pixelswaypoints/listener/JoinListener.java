package org.pixel.pixelswaypoints.listener;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import org.pixel.pixelswaypoints.Main;
import org.pixel.pixelswaypoints.config.ConfigHandler;

public class JoinListener implements Listener {
    private final Main main;
    public JoinListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Instancing Classes
        final ConfigHandler ch = new ConfigHandler(this.main);

        // Getting waypoints
        YamlConfiguration data = ch.getData();

        Player p = event.getPlayer();
        String owner = p.getUniqueId().toString();

        if (!data.isConfigurationSection("waypoints." + owner)) {
            data.set("waypoints." + owner, null);
            ch.saveData();
        }
    }

}
