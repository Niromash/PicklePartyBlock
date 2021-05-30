package fr.niromash.partyblock.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageListener implements Listener {

    @EventHandler
    public void entityDamageByEntity(EntityDamageByEntityEvent e) {
        e.setCancelled(true);
    }
}
