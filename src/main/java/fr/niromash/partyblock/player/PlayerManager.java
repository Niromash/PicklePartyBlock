package fr.niromash.partyblock.player;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerManager {

    private final HashMap<UUID, PBPlayer> players = new HashMap<>();

    public void addPlayer(Player player) {
        players.put(player.getUniqueId(), new PBPlayer(player));
    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
    }

    public PBPlayer getPlayer(Player searchedPlayer){
        return players.values().stream().filter(player -> player.getPlayer().getUniqueId() == searchedPlayer.getUniqueId()).findAny().orElse(null);
    }

    public HashMap<UUID, PBPlayer> getPlayers() {
        return players;
    }

    public List<PBPlayer> getAlivePlayers(){
        return players.values().stream().filter(pbPlayer -> pbPlayer.getState().equals(PlayerState.ALIVE)).collect(Collectors.toList());
    }
}
