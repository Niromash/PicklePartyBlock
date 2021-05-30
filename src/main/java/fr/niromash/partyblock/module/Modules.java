package fr.niromash.partyblock.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public enum Modules {
    MODULE_LOBBY("lobbyModule", false),
    MODULE_1("1Module"),
    MODULE_2("2Module"),
    MODULE_3("3Module"),
    MODULE_4("4Module"),
    MODULE_5("5Module"),
    MODULE_6("6Module"),
    MODULE_7("7Module"),
    MODULE_8("8Module"),
    MODULE_BONUS("BonusModule");

    private static final List<Modules> usedModules = new ArrayList<>();
    private final String name;
    private final boolean inRandom;

    Modules(String name) {
        this.name = name;
        this.inRandom = true;
    }

    Modules(String name, boolean inRandom) {
        this.name = name;
        this.inRandom = inRandom;
    }

    public String getName() {
        return name;
    }

    public static List<Modules> getInRandomModules(){
        return Arrays.stream(values()).filter(modules -> modules.inRandom).collect(Collectors.toList());
    }

    public static Modules getRandomModule(){
        List<Modules> filteredModules = getInRandomModules();
        return filteredModules.get(new Random().nextInt(filteredModules.size()));
    }

    public static Modules getRandomUnusedModule(){
        if(usedModules.size() == getInRandomModules().size()) usedModules.clear();
        List<Modules> filteredModules = Arrays.stream(values()).filter(modules -> modules.inRandom && !usedModules.contains(modules)).collect(Collectors.toList());
        Modules randomModule = filteredModules.get(new Random().nextInt(filteredModules.size()));
        usedModules.add(randomModule);
        return randomModule;
    }
}
