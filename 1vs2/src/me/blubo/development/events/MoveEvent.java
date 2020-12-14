package me.blubo.development.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.blubo.development.handlers.GameHandler;

public class MoveEvent implements Listener{
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(GameHandler.players_in_countdown.contains(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
}
