package ua.Ldoin.HologramList;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class Listeners implements Listener {

    @EventHandler
    public void unloadChunk(ChunkUnloadEvent event) {

        for (Hologram hologram : Hologram.holograms.values())
            if (hologram.getLocation().getChunk().equals(event.getChunk()))
                event.setCancelled(true);

    }
}