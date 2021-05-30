package fr.niromash.partyblock.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherChangeListener implements Listener {

    @EventHandler
    public void weatherChange(WeatherChangeEvent e) {
        if(e.toWeatherState()) e.setCancelled(true);
    }
}
