package fr.niromash.partyblock.listeners;

import fr.niromash.partyblock.PartyBlock;
import fr.niromash.partyblock.player.PBPlayer;
import fr.niromash.partyblock.player.PlayerState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void playerDeath(PlayerDeathEvent e) {
        e.setDeathMessage(null);
        Player player = e.getEntity();
        PartyBlock instance = PartyBlock.get();
        PBPlayer pbPlayer = instance.getPlayerManager().getPlayer(player);
        player.spigot().respawn();
        pbPlayer.die();
        if (instance.getPlayerManager().getPlayers().values().stream().filter(pbPlayer1 -> pbPlayer1.getState().equals(PlayerState.ALIVE)).count() <= 1) {
            instance.getGameManager().stop();
        }
    }
}
