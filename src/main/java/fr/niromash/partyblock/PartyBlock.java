package fr.niromash.partyblock;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import fr.niromash.partyblock.game.GameManager;
import fr.niromash.partyblock.listeners.*;
import fr.niromash.partyblock.player.PlayerManager;
import fr.niromash.partyblock.rmq.RabbitManager;
import fr.niromash.partyblock.sounds.SoundManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class PartyBlock extends JavaPlugin {

    private static PartyBlock instance;
    private PlayerManager playerManager;
    private RabbitManager rabbitManager;
    private GameManager gameManager;
    private SoundManager soundManager;
    public static final String WORLDNAME = "partyblock";

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Plugin PartyBlock enabled.");
        playerManager = new PlayerManager();
        gameManager = new GameManager();
        rabbitManager = new RabbitManager(System.getenv("RABBIT_URI"));
        try {
            rabbitManager.connect();
        } catch (URISyntaxException | IOException | NoSuchAlgorithmException | KeyManagementException | TimeoutException e) {
            e.printStackTrace();
        }
        GameManager.loadModule("lobbyModule");
        soundManager = new SoundManager();
        init();
    }

    @Override
    public void onDisable() {
        try {
            rabbitManager.disconnect();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void init(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new JoinQuitListener(), this);
        pm.registerEvents(new PlaceBreakListener(), this);
        pm.registerEvents(new FoodLevelChangeListener(), this);
        pm.registerEvents(new WeatherChangeListener(), this);
        pm.registerEvents(new PlayerDeathListener(), this);
        pm.registerEvents(new EntityDamageListener(), this);
        pm.registerEvents(new PlayerInteractListener(), this);
    }

    public RabbitManager getRabbitManager() {
        return rabbitManager;
    }

    public static PartyBlock get(){
        return instance;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public WorldEditPlugin getWorldEdit() {
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if (p instanceof WorldEditPlugin) return (WorldEditPlugin) p;
        else return null;
    }
}
