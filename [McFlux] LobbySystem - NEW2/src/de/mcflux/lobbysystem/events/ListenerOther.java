package de.mcflux.lobbysystem.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class ListenerOther implements Listener{
	
	@EventHandler
	public void onWeather(WeatherChangeEvent e) {
		e.getWorld().setTime(0L);
	    e.setCancelled(true);
	}

	@EventHandler
	public void onHunger(FoodLevelChangeEvent e) {
		e.setFoodLevel(20);
	    e.setCancelled(true);
	}

	@EventHandler
	public void onHealth(PlayerInteractEntityEvent e) {
		if (e.getRightClicked() instanceof Player) {
			e.setCancelled(true);
		}
	}
}
