package ua.Ldoin.HologramList;

import java.lang.reflect.Field;
import java.util.*;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class Hologram {

    private Hologram(String name, int maxLines, String head, boolean rotable, List<String> lines, Location loc) {

        this.name = name;

        this.maxLines = maxLines;
        this.head = head;
        this.rotatable = rotable;

        this.lines = lines;
        this.location = loc;

        spawnedLines = new ArrayList<>();

    }

    public static Map<String, Hologram> holograms = new HashMap<>();

    private final String name;

    private int maxLines;

    private String head;
    private boolean rotatable;

    private List<String> lines;
    private Location location;

    private final List<ArmorStand> spawnedLines;

    public List<String> getLines() {

        return lines;

    }

    public Location getLocation() {

        return location;

    }

    public void addLine(String line) {

        if (lines.size() + 1 > maxLines)
            return;

        lines.add(line);
        UpdateLines();

    }

    public void addLine(String line, int index) {

        if (index > maxLines)
            throw new ArrayIndexOutOfBoundsException("Index > MaxLines of Hologram " + name);

        if (index > lines.size()) {

            addLine(line);
            return;

        }

        List<String> cloneLines = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++)
            if (i > maxLines)
                break;
            else if (i + 1 == index) {

                cloneLines.add(line);
                cloneLines.add(lines.get(i));

            } else
                cloneLines.add(lines.get(i));

        lines = cloneLines;

        UpdateLines();

    }

    public void removeLine(String line) {

        lines.remove(line);
        UpdateLines();

    }

    public void setMaxLines(int maxLines) {

        this.maxLines = maxLines;
        UpdateLines();

    }

    public void setHead(String head) {

        this.head = head;
        UpdateLines();

    }

    public void setRotatable(boolean rotatable) {

        this.rotatable = rotatable;
        UpdateLines();

    }

    public void setLocation(Location location) {

        this.location = location;
        UpdateLines();

    }

    public ItemStack getHead() {

        if (head.equals(""))
            return null;

        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", Main.config.getString("holograms." + name + ".head.texture")));

        try {

            Field profileField = headMeta.getClass().getDeclaredField("profile");

            profileField.setAccessible(true);
            profileField.set(headMeta, profile);

        } catch (Exception ex) {

            ex.printStackTrace();

        }

        head.setItemMeta(headMeta);
        return head;

    }

    public void SpawnLine(Location loc, String text) {

        ArmorStand stand = loc.getWorld().spawn(loc, ArmorStand.class);

        stand.setCustomName(text);
        stand.setCustomNameVisible(true);

        stand.setVisible(false);
        stand.setGravity(false);
        stand.setBasePlate(false);

        stand.setNoDamageTicks(Integer.MAX_VALUE);

        spawnedLines.add(stand);

    }

    public void SpawnHologram() {

        for (int i = 0; i < lines.size(); i++) {

            if (i < maxLines)
                SpawnLine(location.clone().subtract(0.0D, 0.25D * i, 0.0D), lines.get(i));

            if (i == lines.size() - 1)
                if (getHead() != null) {

                    ArmorStand stand = location.getWorld().spawn(location.clone().subtract(0, 0.25D * i - 0.75, 0), ArmorStand.class);

                    stand.setVisible(false);
                    stand.setGravity(false);
                    stand.setBasePlate(false);

                    stand.setSmall(true);

                    stand.setHelmet(getHead());

                    stand.setNoDamageTicks(Integer.MAX_VALUE);

                    spawnedLines.add(stand);

                    if (rotatable)
                        new BukkitRunnable() {

                            public void run() {

                                Location location = stand.getLocation();
                                location.setYaw(location.getYaw() + Main.config.getInt("options.head.degrees"));

                                stand.teleport(location);

                            }
                        }.runTaskTimer(Main.plugin, 0L, Main.config.getInt("options.head.time"));
                }
        }
    }

    private void ClearLines() {

        for (ArmorStand stand : spawnedLines)
            stand.remove();

    }

    public void UpdateLines() {

        ClearLines();
        SpawnHologram();

    }

    public void save(boolean clear) {

        if (Main.config.contains("holograms." + name)) {

            ConfigurationSection section = Main.config.getConfigurationSection("holograms." + name);

            section.set("location", LocationUtil.getLocation(location));
            section.set("max_lines", maxLines);

            if (!head.equals(""))
                section.set("head.texture", head);

            section.set("head.rotatable", rotatable);

            section.set("lines", lines);

        } else {

            ConfigurationSection section = Main.config.createSection("holograms." + name);

            section.set("location", LocationUtil.getLocation(location));
            section.set("max_lines", maxLines);

            if (!head.equals(""))
                section.set("head.texture", head);

            section.set("head.rotatable", rotatable);

            section.set("lines", lines);

        }

        Main.plugin.saveConfig();

        if (clear)
            ClearLines();

    }

    public void delete() {

        if (Main.config.contains("holograms." + name))
            Main.config.set("holograms." + name, null);

        Main.plugin.saveConfig();

        ClearLines();
        holograms.remove(name);

    }

    public static void loadHolograms() {

        for (String name : Main.config.getConfigurationSection("holograms").getKeys(false)) {

            List<String> lines = new ArrayList<>();

            for (String linesPiece : Main.config.getStringList("holograms." + name + ".lines"))
                lines.add(linesPiece.replace("&", "§"));

            createHologram(name, Main.config.getInt("holograms." + name + ".max_lines"),
                    Main.config.contains("holograms." + name + ".head.texture") ? Main.config.getString("holograms." + name + ".head.texture") : "",
                    Main.config.getBoolean("holograms." + name + ".head.rotatable"),
                    LocationUtil.getLocation(Main.config.getString("holograms." + name + ".location")), lines);

        }

        Main.sendMessage(Bukkit.getConsoleSender(), "&eHologramList &f>> Loaded &e" + holograms.size() + " &fholograms!");

    }

    public static void createHologram(String name, int maxLines, String head, boolean rotatable, Location location, List<String> lines) {

        Hologram holo = new Hologram(name, maxLines, head, rotatable, lines, location);

        new BukkitRunnable() {

            public void run() {

                if (location.getChunk().isLoaded()) {

                    holo.SpawnHologram();
                    cancel();

                }
            }
        }.runTaskTimer(Main.plugin, 0L, 20L);

        holograms.put(name, holo);

    }

    public static void saveHolograms() {

        for (Hologram holo : Hologram.holograms.values())
            holo.save(true);

    }
}