package fr.niromash.partyblock.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void playerInteract(PlayerInteractEvent e) {
        e.setCancelled(true);
    }
}
