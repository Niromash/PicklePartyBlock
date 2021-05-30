package fr.niromash.partyblock.player;

import fr.niromash.partyblock.PartyBlock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.Objects;

public class PBPlayer {

    private final Player player;
    private PlayerState state = PlayerState.ALIVE;

    public PBPlayer(Player player){
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public void die(){
        player.setWalkSpeed(.4F);
        setState(PlayerState.DEAD);
        Bukkit.broadcastMessage(player.getDisplayName() + ChatColor.RED + " est mort, il reste " + ChatColor.AQUA + PartyBlock.get().getPlayerManager().getPlayers().values().stream().filter(pbPlayer -> pbPlayer.getState().equals(PlayerState.ALIVE)).count() + ChatColor.RED + " joueurs");
        Objects.requireNonNull(Bukkit.getWorld(PartyBlock.WORLDNAME)).strikeLightningEffect(player.getLocation());
        player.teleport(PartyBlock.get().getGameManager().getHubLocation());
        player.getInventory().clear();
    }
}
