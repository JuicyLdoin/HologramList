package ua.Ldoin.HologramList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Commands implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {

        if (command.equals("hologramlist") || command.equals("hl")) {

            if (!sender.hasPermission("hologramlist.manage")) {

                Main.sendMessage(sender, Main.config.getString("message.no_permissions"));
                return false;

            }

            if (args.length == 1 && args[0].equals("save")) {

                Hologram.saveHolograms();
                Main.sendMessage(sender, Main.config.getString("message.messages.hologram.all_saved"));

                return true;

            }

            if (args.length == 2) {

                if (args[0].equals("info")) {

                    Hologram holo = Hologram.holograms.get(args[1]);

                    if (holo == null) {

                        Main.sendMessage(sender, Main.config.getString("message.messages.hologram.is_null").replace("%hologram%", args[1]));
                        return false;

                    }

                    Main.sendMessage(sender, Main.config.getString("message.messages.hologram.info").replace("%hologram%", args[1]));

                    for (String line : holo.getLines())
                        Main.sendMessage(sender, line);

                    return true;

                }

                if (args[0].equals("create")) {

                    if (!(sender instanceof Player)) {

                        Main.sendMessage(sender, Main.config.getString("message.for_players"));
                        return false;

                    }

                    Player player = (Player) sender;

                    Hologram holo = Hologram.holograms.get(args[1]);

                    if (holo != null) {

                        Main.sendMessage(player, Main.config.getString("message.messages.hologram.is_not_null").replace("%hologram%", args[1]));
                        return false;

                    }

                    Hologram.createHologram(args[1], 10, "", false, player.getLocation(), new ArrayList<>());
                    Main.sendMessage(player, Main.config.getString("message.messages.hologram.created").replace("%hologram%", args[1]));

                    return true;

                }

                if (args[0].equals("delete")) {

                    Hologram holo = Hologram.holograms.get(args[1]);

                    if (holo == null) {

                        Main.sendMessage(sender, Main.config.getString("message.messages.hologram.is_null").replace("%hologram%", args[1]));
                        return false;

                    }

                    holo.delete();
                    Main.sendMessage(sender, Main.config.getString("message.messages.hologram.deleted").replace("%hologram%", args[1]));

                    return true;

                }

                if (args[0].equals("save")) {

                    Hologram holo = Hologram.holograms.get(args[1]);

                    if (holo == null) {

                        Main.sendMessage(sender, Main.config.getString("message.messages.hologram.is_null").replace("%hologram%", args[1]));
                        return false;

                    }

                    holo.save(false);
                    Main.sendMessage(sender, Main.config.getString("message.messages.hologram.saved").replace("%hologram%", args[1]));

                    return true;

                }

                if (args[0].equals("move")) {

                    if (!(sender instanceof Player)) {

                        Main.sendMessage(sender, Main.config.getString("message.for_players"));
                        return false;

                    }

                    Player player = (Player) sender;

                    Hologram holo = Hologram.holograms.get(args[1]);

                    if (holo == null) {

                        Main.sendMessage(sender, Main.config.getString("message.messages.hologram.is_null").replace("%hologram%", args[1]));
                        return false;

                    }

                    holo.setLocation(player.getLocation());
                    Main.sendMessage(sender, Main.config.getString("message.messages.hologram.moved").replace("%hologram%", args[1]));

                    return true;

                }
            }

            if (args.length == 3) {

                if (args[0].equals("addline")) {

                    Hologram holo = Hologram.holograms.get(args[1]);

                    if (holo == null) {

                        Main.sendMessage(sender, Main.config.getString("message.messages.hologram.is_null").replace("%hologram%", args[1]));
                        return false;

                    }

                    String line = args[2].replace("&", "§").replace(Main.config.getString("options.replacement"), " ");

                    holo.addLine(line);
                    Main.sendMessage(sender, Main.config.getString("message.messages.hologram.add_line")
                            .replace("%hologram%", args[1])
                            .replace("%line%", line));

                    return true;

                }

                if (args[0].equals("removeline")) {

                    Hologram holo = Hologram.holograms.get(args[1]);

                    if (holo == null) {

                        Main.sendMessage(sender, Main.config.getString("message.messages.hologram.is_null").replace("%hologram%", args[1]));
                        return false;

                    }

                    String line = args[2].replace("&", "§");

                    if (!holo.getLines().contains(line)) {

                        Main.sendMessage(sender, Main.config.getString("message.messages.not_contains_line")
                                .replace("%hologram%", args[1])
                                .replace("%line%", line));
                        return false;

                    }

                    holo.removeLine(line);
                    Main.sendMessage(sender, Main.config.getString("message.messages.hologram.remove_line")
                            .replace("%hologram%", args[1])
                            .replace("%line%", args[2].replace("&", "§")));

                    return true;

                }

                if (args[0].equals("maxlines")) {

                    Hologram holo = Hologram.holograms.get(args[1]);

                    if (holo == null) {

                        Main.sendMessage(sender, Main.config.getString("message.messages.hologram.is_null").replace("%hologram%", args[1]));
                        return false;

                    }

                    if (!args[2].matches("^-?\\d+$")) {

                        Main.sendMessage(sender, Main.config.getString("message.messages.not_an_integer"));
                        return false;

                    }

                    holo.setMaxLines(Integer.parseInt(args[2]));
                    Main.sendMessage(sender, Main.config.getString("message.messages.hologram.max_lines")
                            .replace("%hologram%", args[1])
                            .replace("%lines%", args[2]));

                    return true;

                }

                if (args[0].equals("head")) {

                    Hologram holo = Hologram.holograms.get(args[1]);

                    if (holo == null) {

                        Main.sendMessage(sender, Main.config.getString("message.messages.hologram.is_null").replace("%hologram%", args[1]));
                        return false;

                    }

                    holo.setHead(args[2]);
                    Main.sendMessage(sender, Main.config.getString("message.messages.hologram.head").replace("%hologram%", args[1]));

                    return true;

                }

                if (args[0].equals("rotatable")) {

                    Hologram holo = Hologram.holograms.get(args[1]);

                    if (holo == null) {

                        Main.sendMessage(sender, Main.config.getString("message.messages.hologram.is_null").replace("%hologram%", args[1]));
                        return false;

                    }

                    holo.setRotatable(Boolean.parseBoolean(args[2]));
                    Main.sendMessage(sender, Main.config.getString("message.messages.hologram.rotatable")
                            .replace("%hologram%", args[1])
                            .replace("%rotatable%", args[2]));

                    return true;

                }
            }

            if (args.length == 4 && args[0].equals("addline")) {

                Hologram holo = Hologram.holograms.get(args[1]);

                if (holo == null) {

                    Main.sendMessage(sender, Main.config.getString("message.messages.hologram.is_null").replace("%hologram%", args[1]));
                    return false;

                }

                if (!args[3].matches("^-?\\d+$")) {

                    Main.sendMessage(sender, Main.config.getString("message.messages.not_an_integer"));
                    return false;

                }

                String line = args[2].replace("&", "§").replaceAll(Main.config.getString("options.replacement"), " ");

                holo.addLine(line, Integer.parseInt(args[3]));
                Main.sendMessage(sender, Main.config.getString("message.messages.hologram.add_line")
                        .replace("%hologram%", args[1])
                        .replace("%line%", line));

                return true;

            }

            Main.sendMessage(sender, "&a/hologramlist info [hologram] - get info of hologram [hologram]");
            Main.sendMessage(sender, "&a/hologramlist create [hologram] - create hologram [hologram]");
            Main.sendMessage(sender, "&a/hologramlist delete [hologram] - delete hologram [hologram]");
            Main.sendMessage(sender, "&a/hologramlist addline [hologram] [line] - add line [line] to hologram [hologram] " +
                    "(if your line has spaces, replace them with '" + Main.config.getString("options.replacement") + "')");
            Main.sendMessage(sender, "&a/hologramlist addline [hologram] [line] [index] - add line [line] to hologram [hologram] " +
                    "(if your line has spaces, replace them with '" + Main.config.getString("options.replacement") + "') with index [index]");
            Main.sendMessage(sender, "&a/hologramlist removeline [hologram] [line] - remove line [line] from hologram [hologram]");
            Main.sendMessage(sender, "&a/hologramlist maxlines [hologram] [lines] - set maxlines to [lines] in hologram [hologram]");
            Main.sendMessage(sender, "&a/hologramlist move [hologram] - move hologram [hologram] to your location");
            Main.sendMessage(sender, "&a/hologramlist head [hologram] [texture] - set maxlines to [lines] in hologram [hologram]");
            Main.sendMessage(sender, "&a/hologramlist rotatable [hologram] [rotatable] - set hologram [hologram] is rotatable to [rotatable]");
            Main.sendMessage(sender, "&a/hologramlist save [hologram] - save hologram [hologram] to config");
            Main.sendMessage(sender, "&a/hologramlist save - save holograms to config");

        }

        return false;

    }
}