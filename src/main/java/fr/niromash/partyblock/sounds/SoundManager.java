package fr.niromash.partyblock.sounds;

import fr.niromash.partyblock.PartyBlock;
import fr.niromash.partyblock.blocks.PBBlocks;
import fr.niromash.partyblock.rmq.RabbitManager;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class SoundManager {

    private final RabbitManager manager;
    private Sounds gameSound;

    public SoundManager(){
        manager = PartyBlock.get().getRabbitManager();
    }

    public SoundManager setGameSound(Sounds sound) {
        gameSound = sound;
        return this;
    }

    public void playSound() throws IOException {
        playSound(gameSound);
    }

    public void playSound(Sounds sound) throws IOException {
        manager.sendMessage("playSound", Arrays.asList(new HashMap(){{
            put("sound", sound.toString());
        }}));
    }

    public void changeBlock(PBBlocks block) throws IOException {
        manager.sendMessage("changeBlock", Arrays.asList(new HashMap(){{
            put("hexaColor", block.getHexaColor());
        }}));
    }

    public void pause() throws IOException {
        manager.sendMessage("pause");
    }

    public void stop() throws IOException {
        manager.sendMessage("stop");
    }

    public void end() throws IOException {
        manager.sendMessage("end");
    }
}
