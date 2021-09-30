package org.pixel.pixelswaypoints.listener;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.pixel.pixelswaypoints.Main;

import java.io.File;

public class JoinListener implements Listener {

    // Loading Data
    Main main = new Main();
    File f = new File(main.getDataFolder(), "waypoints.yml");
    YamlConfiguration data = YamlConfiguration.loadConfiguration(f);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        String owner = p.getUniqueId().toString();
        if (!data.isConfigurationSection("waypoints." + owner)) {
            data.set("waypoints." + owner, null);
            try {data.save(f);} catch (Exception e) {e.printStackTrace();}
        }
    }

}
