package org.pixel.pixelswaypoints;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.pixel.pixelswaypoints.config.ConfigHandler;
import org.pixel.pixelswaypoints.listener.JoinListener;

public class Main extends JavaPlugin {

    // Instancing Classes
    private final ConfigHandler ch = new ConfigHandler(this);
    private final JoinListener jl = new JoinListener(this);

    // Getting config + waypoints
    private YamlConfiguration config = ch.getConfig();
    private YamlConfiguration data = ch.getData();

    @Override
    public void onEnable() {
        // Setting lang defaults
        if(!config.contains("error_onlyplayers")) {config.set("error_onlyplayers", "Dieser Befehl kann nur von Spielern ausgeführt werden!");}
        if(!config.contains("error_no_subcommand")) {config.set("error_no_subcommand", "Dieser Befehl existiert nicht!");}
        if(!config.contains("error_no_waypoint")) {config.set("error_no_waypoint", "Dieser Waypoint existiert nicht!");}
        if(!config.contains("delete_success")) {config.set("delete_success", "Waypoint löschen erfolgreich!");}
        if(!config.contains("create_success")) {config.set("create_success", "Waypoint erstellen erfolgreich!");}
        if(!config.contains("world")) {config.set("world", "Welt");}
        if(!config.contains("existing_waypoints")) {config.set("existing_waypoints", "Bestehende Waypoints");}
        if(!config.contains("empty_waypoints")) {config.set("empty_waypoints", "Du hast keine Waypoints!");}

        // Setting data defaults
        if(!data.contains("waypoints")) {data.set("waypoints", null);}

        // Saving config
        ch.saveConfig();
        ch.saveData();

        // Registering Events
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(jl, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + config.getString("error_onlyplayers"));
            return false;
        }

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

        String owner = p.getUniqueId().toString();

        switch (args[0]) {
            case "get" -> {
                if (!data.isConfigurationSection("waypoints." + owner)) {
                    data.set("waypoints." + owner, null);
                }
                if (args.length != 2) {
                    p.sendMessage(ChatColor.RED + "Syntax: /waypoint <set|setPos|delete|get|list> <name>");
                }
                if (data.contains("waypoints." + owner + "." + args[1])) {
                    p.sendMessage(ChatColor.BLUE + "Waypoint " + args[1] + ":");
                    p.sendMessage("X: " + data.getInt("waypoints." + owner + "." + args[1] + ".x") + " Y: " + data.getInt("waypoints." + owner + "." + args[1] + ".y") + " Z: " + data.getInt("waypoints." + owner + "." + args[1] + ".z"));
                    p.sendMessage(config.getString("world") + ": " + data.getString("waypoints." + owner + "." + args[1] + ".world"));
                } else {
                    p.sendMessage(ChatColor.RED + config.getString("error_no_waypoint"));
                }
            }
            case "set" -> {
                if (!data.isConfigurationSection("waypoints." + owner)) {
                    data.set("waypoints." + owner, null);
                }
                if (args.length != 2) {
                    p.sendMessage(ChatColor.RED + "Syntax: /waypoint <set|setPos|delete|get|list> <name>");
                }
                data.set("waypoints." + owner + "." + args[1] + ".x", x);
                data.set("waypoints." + owner + "." + args[1] + ".y", y);
                data.set("waypoints." + owner + "." + args[1] + ".z", z);
                data.set("waypoints." + owner + "." + args[1] + ".world", world);
                ch.saveData();
                p.sendMessage(ChatColor.GREEN + config.getString("create_success"));
            }
            case "setPos" -> {
                if (!data.isConfigurationSection("waypoints." + owner)) {
                    data.set("waypoints." + owner, null);
                }
                if (args.length != 5) {
                    p.sendMessage(ChatColor.RED + "Syntax: /waypoint setPos <x> <y> <z> <name>");
                }
                data.set("waypoints." + owner + "." + args[1] + ".x", args[1]);
                data.set("waypoints." + owner + "." + args[1] + ".y", args[2]);
                data.set("waypoints." + owner + "." + args[1] + ".z", args[3]);
                data.set("waypoints." + owner + "." + args[1] + ".world", world);
                ch.saveData();
                p.sendMessage(ChatColor.GREEN + config.getString("create_success"));
            }
            case "delete" -> {
                if (!data.isConfigurationSection("waypoints." + owner)) {
                    data.set("waypoints." + owner, null);
                }
                if (data.contains("waypoints." + owner + "." + args[1])) {
                    data.set("waypoints." + owner + "." + args[1], null);
                    ch.saveData();
                    p.sendMessage(ChatColor.GREEN + config.getString("delete_success"));
                } else {
                    p.sendMessage(ChatColor.RED + config.getString("error_no_waypoint"));
                }
            }
            case "list" -> {
                if (!data.isConfigurationSection("waypoints." + owner)) {
                    data.set("waypoints." + owner, null);
                }
                int i = 0;
                for (String key : data.getConfigurationSection("waypoints." + owner).getKeys(false)) {
                    i = i + 1;
                }
                if (i != 0) {
                    p.sendMessage(ChatColor.BLUE + config.getString("existing_waypoints") + ": ");
                    for (String key : data.getConfigurationSection("waypoints." + owner).getKeys(false)) {
                        p.sendMessage("- " + key);
                    }
                } else {
                    p.sendMessage(ChatColor.LIGHT_PURPLE + config.getString("empty_waypoints"));
                }
            }
            case "help" -> p.sendMessage(ChatColor.BLUE +
                    "Syntax: /waypoint <set|setPos|delete|get|list> <name>\n" +
                    ChatColor.GREEN +
                    "/waypoint set <name>\n" +
                    "/waypoint setPos <x> <y> <z> <name>\n" +
                    "/waypoint delete <name>\n" +
                    "/waypoint get <name>\n" +
                    "/waypoint list\n" +
                    "/waypoint help ");
            default -> {
                p.sendMessage(ChatColor.RED + config.getString("error_no_subcommand"));
                p.sendMessage(ChatColor.RED + "Syntax: /waypoint <set|setPos|delete|get|list> <name>");
                return false;
            }
        }
        return true;
        }

}



