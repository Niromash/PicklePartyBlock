package fr.niromash.partyblock.sounds;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public enum Sounds {
    APPROACHING,
    APPROACHING_ALBUM,
    LETS_BE_FRIENDS,
    NEW_GAME,
    ECLIPSE,
    ROCKTRONIK,
    SUGAR_RUSH,
    REACH,
    RAZOR_SHARP,
    A_SKILLZ_VS_BEAT_VANDALS,
    CHEER(false),
    GITHUB,
    TAKING_MY_TIME,
    EXIGE,
    MAKE_A_CAKE,
    FREAK_ME_OUT,
    IN_TIME,
    FIRESTARTER,
    THE_EXIT,
    SPLINTER,
    THE_ARCADE_2013,
    THE_STANDOFF,
    PIXEL_PARTY,
    THIRSTY,
    YOU_CAN_PLAY,
    VINYL_STOP(false),
    KILL_ALL_DAY;

    private boolean isMusic = true;

    Sounds() {}

    Sounds(boolean isMusic) {
        this.isMusic = isMusic;
    }

    public static Sounds getRandomSound(){
        List<Sounds> filteredMusics = Arrays.stream(values()).filter(sounds -> sounds.isMusic).collect(Collectors.toList());
        return filteredMusics.get(new Random().nextInt(filteredMusics.size()));
    }
}
