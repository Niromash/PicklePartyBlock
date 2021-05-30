package fr.niromash.partyblock.task;

import fr.niromash.partyblock.PartyBlock;
import fr.niromash.partyblock.game.GameManager;
import fr.niromash.partyblock.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

public class LobbyTask extends BukkitRunnable {

    int timeBeforeStart = 10;
    int timer = timeBeforeStart;

    @Override
    public void run() {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        GameManager gameManager = PartyBlock.get().getGameManager();

        for (Player player : players) {
            player.setExp((float) timer/ timeBeforeStart);
            player.setLevel(timer);
        }

        if(players.size() < gameManager.requiredPlayersToStart) {
            Bukkit.broadcastMessage(ChatColor.RED + "Un joueur a quitté, la partie a été annulé !");
            gameManager.setState(GameState.WAITING);
            cancel();
            return;
        }

        if(timer != 0 && (timer < 5 || timer % 5 == 0)) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "La partie commence dans " + ChatColor.AQUA + timer + "s");
        }

        if(timer == 0){
            Bukkit.broadcastMessage(ChatColor.GOLD + "La partie va commencer !");
            gameManager.start();
            cancel();
            return;
        }

        timer--;
    }
}
