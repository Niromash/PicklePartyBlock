package fr.niromash.partyblock.bonus;

import fr.niromash.partyblock.PartyBlock;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public enum Bonus {
    SPEED("Bonus de rapidité", true, player -> {
        player.setWalkSpeed(.6F);

        new BukkitRunnable() {

            @Override
            public void run() {
                player.setWalkSpeed(.4F);
            }
        }.runTaskLater(PartyBlock.get(), 10*20);
    });

    private final String name;
    private final boolean isBonus;
    private final Consumer<Player> playerConsumer;

    Bonus(String name, boolean isBonus, Consumer<Player> player) {
        this.name = name;
        this.isBonus = isBonus;
        playerConsumer = player;
    }

    public String getName(){
        return name;
    }

    public boolean isBonus() {
        return isBonus;
    }

    public void run(Player player){
        this.playerConsumer.accept(player);
    }

    public static Bonus getRandom(){
        return Arrays.asList(values()).get(new Random().nextInt(values().length));
    }
}
