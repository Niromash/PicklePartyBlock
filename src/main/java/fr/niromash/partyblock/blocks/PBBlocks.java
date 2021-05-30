package fr.niromash.partyblock.blocks;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public enum PBBlocks {
    WHITE(0, "Béton blanc", ChatColor.WHITE, "#ffffff"),
    ORANGE(1, "Béton orange", ChatColor.GOLD, "#f3bb12"),
    MAGENTA(2, "Béton magenta", ChatColor.DARK_PURPLE, "#ba2bd9"),
    LIGHT_BLUE(3, "Béton bleu clair", ChatColor.AQUA, "#5fc2f6"),
    YELLOW(4, "Béton jaune", ChatColor.YELLOW, "#f3ec30"),
    LIME(5, "Béton vert clair", ChatColor.GREEN, "#80ea40"),
    PINK(6, "Béton rose", ChatColor.LIGHT_PURPLE, "#dd8cec"),
    GRAY(7, "Béton gris", ChatColor.DARK_GRAY, "#525252"),
    LIGHT_GRAY(8, "Béton gris clair", ChatColor.GRAY, "#c6c6c6"),
    CYAN(9, "Béton cyan", ChatColor.DARK_AQUA, "#1da0b0"),
    PURPLE(10, "Béton violet", ChatColor.LIGHT_PURPLE, "#573b9f"),
    BLUE(11, "Béton bleu", ChatColor.DARK_BLUE, "#2564cc"),
    BROWN(12, "Béton marron", ChatColor.BLACK, "#6e4d29"),
    GREEN(13, "Béton vert", ChatColor.DARK_GREEN, "#2d752b"),
    RED(14, "Béton rouge", ChatColor.RED, "#c92e2a"),
    BLACK(15, "Béton noir", ChatColor.BLACK, "#000000");

    private final int id;
    private final String name;
    private final ChatColor color;
    private final String hexaColor;

    PBBlocks(int id, String name, ChatColor color, String hexaColor) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.hexaColor = hexaColor;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getHexaColor() {
        return hexaColor;
    }

    public static PBBlocks getFromId(int id) {
        return Arrays.stream(values()).filter(blocks -> blocks.id == id).findAny().orElse(null);
    }
}
