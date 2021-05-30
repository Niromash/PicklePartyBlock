package fr.niromash.partyblock.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceBreakListener implements Listener {

    @EventHandler
    public void placeListener(BlockPlaceEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void breakListener(BlockBreakEvent e) {
        e.setCancelled(true);
    }
}
