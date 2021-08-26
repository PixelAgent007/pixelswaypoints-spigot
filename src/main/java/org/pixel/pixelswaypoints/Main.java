package org.pixel.pixelswaypoints;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin start logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Loading config
        File f2 = new File(this.getDataFolder(), "waypoints.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(f2);

        if(sender instanceof Player) {
            // Getting waypoints
            File f = new File(this.getDataFolder(), "waypoints.yml");
            YamlConfiguration data = YamlConfiguration.loadConfiguration(f);

            // Get coords + World
            Player p = (Player) sender;
            Location loc = p.getLocation();
            int x = (int) loc.getX();
            int y = (int) loc.getY();
            int z = (int) loc.getZ();
            String world = p.getWorld().getName();

            if (args.length < 1) {
                p.sendMessage(ChatColor.RED + "Syntax: /waypoint <set|delete|get|list> <name>");
            }

            if (args[0].equals("get")) {
                if (args.length != 2) {
                    p.sendMessage(ChatColor.RED + "Syntax: /waypoint <set|delete|get|list> <name>");
                }
                if (data.contains("waypoints." + args[1])) {
                    p.sendMessage(ChatColor.BLUE + "Waypoint " + args[1] + ":");
                    p.sendMessage("X: " + data.getInt("waypoints." + args[1] + ".x") + " Y: " + data.getInt("waypoints." + args[1] + ".y") + " Z: " + data.getInt("waypoints." + args[1] + ".z"));
                    p.sendMessage(config.getString("world") + ": " + data.getString("waypoints." + args[1] + ".world"));
                    p.sendMessage(config.getString("created_by") + ": " + data.getString("waypoints." + args[1] + ".creator"));
                }else {
                    p.sendMessage(ChatColor.RED + config.getString("error_no_waypoint"));
                }
            } else if (args[0].equals("set")) {
                if (args.length != 2) {
                    p.sendMessage(ChatColor.RED + "Syntax: /waypoint <set|delete|get|list> <name>");
                }
                data.set("waypoints." + args[1] + ".x", x);
                data.set("waypoints." + args[1] + ".y", y);
                data.set("waypoints." + args[1] + ".z", z);
                data.set("waypoints." + args[1] + ".world", world);
                data.set("waypoints." + args[1] + ".creator", p.getName());
                try {
                    data.save(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                p.sendMessage(ChatColor.GREEN + config.getString("create_success"));
            } else if (args[0].equals("delete")) {
                if (data.contains("waypoints." + args[1])) {
                    data.set("waypoints." + args[1], null);
                    try {
                        data.save(f);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    p.sendMessage(ChatColor.GREEN + config.getString("delete_success"));
                }else {
                    p.sendMessage(ChatColor.RED + config.getString("error_no_waypoint"));
                }
            } else if (args[0].equals("list")) {
                p.sendMessage(ChatColor.BLUE + config.getString("existing_waypoints") + ": ");
                for (String key : data.getConfigurationSection("waypoints").getKeys(false)) {
                    p.sendMessage("- " + key);
                }
            } else if(args[0].equals(("help"))) {
                p.sendMessage(ChatColor.GREEN + "Syntax: /waypoint <set|delete|get|list> <name>\n" +
                        "/waypoint set <name>\n" +
                        "/waypoint delete <name>\n" +
                        "/waypoint get <name>\n" +
                        "/waypoint list\n" +
                        "/waypoint help ");
            } else {
                p.sendMessage(ChatColor.RED + config.getString("error_no_subcommand"));
                p.sendMessage(ChatColor.RED + "Syntax: /waypoint <set|delete|get|list> <name>");
            }
        }else {
            System.out.println(config.getString("error_onlyplayers"));
        }
        return true;
    }
}


