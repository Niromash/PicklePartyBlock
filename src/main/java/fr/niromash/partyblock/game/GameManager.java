package fr.niromash.partyblock.game;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import fr.niromash.partyblock.PartyBlock;
import fr.niromash.partyblock.blocks.PBBlocks;
import fr.niromash.partyblock.bonus.Bonus;
import fr.niromash.partyblock.module.Modules;
import fr.niromash.partyblock.player.PBPlayer;
import fr.niromash.partyblock.player.PlayerManager;
import fr.niromash.partyblock.player.PlayerState;
import fr.niromash.partyblock.sounds.SoundManager;
import fr.niromash.partyblock.sounds.Sounds;
import fr.niromash.partyblock.utils.ItemBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static fr.niromash.partyblock.utils.ListUtils.distinctBy;

public class GameManager {

    private GameState state = GameState.WAITING;
    public final int requiredPlayersToStart = 2;
    public final int maxPlayers = 6;
    private final Location hubLocation = new Location(Bukkit.getWorld(PartyBlock.WORLDNAME), -53.5, 21, 103.5, 180, 0);
    private final Location spawnLocation = new Location(Bukkit.getWorld(PartyBlock.WORLDNAME), -53.5, 18, 54.5, 180, 0);
    private long gameStartDate;

    public Location getHubLocation() {
        return hubLocation;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public void start() {
        gameStartDate = System.currentTimeMillis();
        setState(GameState.IN_GAME);
        PlayerManager playerManager = PartyBlock.get().getPlayerManager();
        for (PBPlayer pbPlayer : playerManager.getPlayers().values()) {
            Player player = pbPlayer.getPlayer();
            player.teleport(spawnLocation);
//            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10000000, 3));
            player.setWalkSpeed(.4F);
        }

        initGame(8, Modules.MODULE_LOBBY);
        try {
            PartyBlock.get().getSoundManager().setGameSound(Sounds.getRandomSound()).playSound();
        } catch (IOException e) {
            Bukkit.broadcastMessage(ChatColor.RED + "Une erreur s'est produite durant le lancement de la musique !");
            e.printStackTrace();
        }
    }

    public void stop() {
        for (PBPlayer pbPlayer : PartyBlock.get().getPlayerManager().getPlayers().values()) {
            Player player = pbPlayer.getPlayer();
            player.setGameMode(GameMode.SPECTATOR);
            player.setWalkSpeed(.2F);
//            for (PotionEffect activePotionEffect : player.getActivePotionEffects())
//                player.removePotionEffect(activePotionEffect.getType());
            player.teleport(hubLocation);
            player.getInventory().clear();
        }
        Bukkit.broadcastMessage(ChatColor.GREEN + "La partie est finie !");
        setState(GameState.FINISHED);
        loadModule("lobbyModule");
        try {
            PartyBlock.get().getSoundManager().stop();
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.broadcastMessage(ChatColor.RED + "Une erreur s'est produite durant l'arrêt de la musique !");
        }
    }

    private void initGame(double timeBeforeChange, Modules module) {
        PartyBlock instance = PartyBlock.get();
        GameManager gameManager = instance.getGameManager();
        if (timeBeforeChange <= 0) {
            gameManager.stop();
            return;
        }

        loadModule(module.getName());

        int chance = new Random().nextInt(15);

        if(chance == 5) { // Nombre random < 15
            Bonus bonus = Bonus.getRandom();
            Bukkit.broadcastMessage(ChatColor.AQUA.toString() + (bonus.isBonus() ? ChatColor.GREEN : ChatColor.RED) + bonus.getName() + " a été activé !");
            for (PBPlayer alivePlayer : instance.getPlayerManager().getAlivePlayers()) bonus.run(alivePlayer.getPlayer());
        }

        CuboidRegion region = new CuboidRegion(BlockVector3.at(-85.5, 17, 22.5), BlockVector3.at(-21.5, 17, 86.5));
        Iterator<BlockVector3> iterator = region.iterator();
        List<Block> initialBlocks = new ArrayList<>();

        while (iterator.hasNext()) {
            BlockVector3 bv = iterator.next();
            initialBlocks.add(Bukkit.getWorld(PartyBlock.WORLDNAME).getBlockAt(new Location(Bukkit.getWorld(PartyBlock.WORLDNAME), bv.getX(), bv.getY(), bv.getZ())));
        }
        List<Block> blocks = initialBlocks.stream().filter(block -> !block.getBlockData().getMaterial().name().contains(Material.AIR.name())).filter(distinctBy(Block::getBlockData)).collect(Collectors.toList());

        Block randomBlock = blocks.get(new Random().nextInt(blocks.size()));
        PBBlocks pbBlocks = PBBlocks.getFromId(randomBlock.getData());
        try {
            instance.getSoundManager().changeBlock(pbBlocks);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ItemStack item = new ItemBuilder(randomBlock.getType()).name(pbBlocks.getColor() + pbBlocks.getName()).durability(randomBlock.getData()).build();
        String initialChars = genChars("⬛", (int) Math.floor(timeBeforeChange));

        for (PBPlayer pbPlayer : instance.getPlayerManager().getPlayers().values()) {
            Player player = pbPlayer.getPlayer();
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(pbBlocks.getColor() + initialChars + " " + pbBlocks.getName() + " " + initialChars));
            player.getInventory().setItem(4, item);
        }

        new BukkitRunnable() {
            int timer = 0;
            private boolean isRemoved = false;
            private final SoundManager soundManager = instance.getSoundManager();

            @Override
            public void run() {
                if(!gameManager.getState().equals(GameState.IN_GAME)) {
                    cancel();
                    return;
                }

                for (PBPlayer pbPlayer : instance.getPlayerManager().getPlayers().values()) {
                    Player player = pbPlayer.getPlayer();
                    if (pbPlayer.getState().equals(PlayerState.ALIVE) && player.getLocation().getY() < 15) {
                        pbPlayer.die();
                        if (instance.getPlayerManager().getPlayers().values().stream().filter(pbPlayer1 -> pbPlayer1.getState().equals(PlayerState.ALIVE)).count() <= 1) {
                            gameManager.stop();
                            cancel();
                            return;
                        }
                    }
                }
                if (timeBeforeChange - timer <= -3) {
                    cancel();
                    initGame(timeBeforeChange < 2 ? timeBeforeChange : timeBeforeChange - 0.25, Modules.getRandomUnusedModule());
                    return;
                }
                if (timeBeforeChange - timer <= 0 && !isRemoved) {
                    isRemoved = true;
                    try {
                        soundManager.pause();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    for (Block block : initialBlocks) {
                        if (!block.getBlockData().equals(randomBlock.getBlockData())) {
                            block.setType(Material.AIR);
                        }
                    }
                    for (PBPlayer pbPlayer : PartyBlock.get().getPlayerManager().getPlayers().values()) {
                        pbPlayer.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "✖ " + ChatColor.WHITE + "STOP" + ChatColor.RED + " ✖"));
                    }
                } else {
                    int timeToWait = (int) Math.floor(timeBeforeChange - timer);
                    if(timeToWait >= 0)
                    for (PBPlayer pbPlayer : PartyBlock.get().getPlayerManager().getPlayers().values()) {
                        Player player = pbPlayer.getPlayer();
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 5, (float) ((timer / timeBeforeChange) * 2));
                        String chars = genChars("⬛", timeToWait);
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(pbBlocks.getColor() + chars + " " + pbBlocks.getName() + " " + chars));
                    }
                }

                timer++;
            }
        }.runTaskTimer(PartyBlock.get(), 20, 20);
    }

    public static void loadModule(String moduleName) {
        try {
            File module = new File(PartyBlock.get().getWorldEdit().getDataFolder().getAbsolutePath() + "/schematics/" + moduleName + ".schem");
            ClipboardFormat format = ClipboardFormats.findByFile(module);
            assert format != null;
            ClipboardReader reader = format.getReader(new FileInputStream(module));
            Clipboard clipboard = reader.read();

            com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(Objects.requireNonNull(Bukkit.getWorld(PartyBlock.WORLDNAME)));

            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1);

            Operation operation = new ClipboardHolder(clipboard).createPaste(editSession)
                    .to(BlockVector3.at(-21.5, 18, 22.5)).ignoreAirBlocks(true).build();

            Operations.complete(operation);
            editSession.flushSession();
        } catch (IOException | WorldEditException e) {
            Bukkit.broadcastMessage(ChatColor.RED + "Il y a eu une erreur de génération de module, contactez Niromash");
            e.printStackTrace();
        }
    }

    private String genChars(String string, int charCount) {
        StringBuilder finalString = new StringBuilder();
        for (int i = 0; i < charCount; i++) {
            finalString.append(string);
        }

        return finalString.toString();
    }
}
