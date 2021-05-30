package fr.niromash.partyblock.listeners;

import fr.niromash.partyblock.PartyBlock;
import fr.niromash.partyblock.game.GameManager;
import fr.niromash.partyblock.game.GameState;
import fr.niromash.partyblock.player.PlayerState;
import fr.niromash.partyblock.sounds.Sounds;
import fr.niromash.partyblock.task.LobbyTask;
import net.picklemc.api.PickleAPI;
import net.picklemc.api.server.ServerState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class JoinQuitListener implements Listener {

    @EventHandler
    public void joinListener(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        Player player = e.getPlayer();
        GameManager gameManager = PartyBlock.get().getGameManager();

        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.getInventory().clear();
        player.teleport(gameManager.getHubLocation());

        int onlinePlayersCount = Bukkit.getOnlinePlayers().size();
        Bukkit.broadcastMessage(ChatColor.GRAY + "(" + ChatColor.GOLD + onlinePlayersCount + "/" + gameManager.maxPlayers + ChatColor.GRAY + ") " + player.getDisplayName() + " a rejoint la partie !");
        PartyBlock.get().getPlayerManager().addPlayer(player);

        if (!gameManager.getState().equals(GameState.STARTING) && onlinePlayersCount >= gameManager.requiredPlayersToStart) {
            gameManager.setState(GameState.STARTING);
            PickleAPI.get().getPickleServer().updateState(ServerState.STARTING);
            new LobbyTask().runTaskTimer(PartyBlock.get(), 20, 20);
        }
    }

    @EventHandler
    public void quitListener(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        Player player = e.getPlayer();
        PartyBlock instance = PartyBlock.get();
        GameManager gameManager = instance.getGameManager();

        player.setWalkSpeed(.1F);

        if (gameManager.getState().equals(GameState.IN_GAME)) {
            instance.getPlayerManager().getPlayer(player).setState(PlayerState.DEAD);
            if (instance.getPlayerManager().getPlayers().values().stream().filter(pbPlayer1 -> pbPlayer1.getState().equals(PlayerState.ALIVE)).count() <= 1) {
                gameManager.stop();
                Bukkit.broadcastMessage(ChatColor.RED + "Un joueur a quitté, il reste plus que vous, la partie est annulée !");
                return;
            }
        }
        if (gameManager.getState().equals(GameState.WAITING)) {
            instance.getPlayerManager().removePlayer(player);
        }
    }
}
