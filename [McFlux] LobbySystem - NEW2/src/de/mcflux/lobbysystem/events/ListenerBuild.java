package de.mcflux.lobbysystem.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import de.mcflux.lobbysystem.commands.CommandBuild;

public class ListenerBuild implements Listener {

	@EventHandler
	public void onBuild(BlockBreakEvent e) {
		try {
			if (CommandBuild.builders.contains(e.getPlayer())) {
				e.setCancelled(false);
			} else {
				e.setCancelled(true);
			}
		} catch (Exception ex) {

		}
	}

	@EventHandler
	public void onBuild(BlockPlaceEvent e) {
		try {
			if (CommandBuild.builders.contains(e.getPlayer())) {
				e.setCancelled(false);
			} else {
				e.setCancelled(true);
			}
		} catch (Exception ex) {

		}
	}

	@EventHandler
	public void onBuild(PlayerDropItemEvent e) {
		try {
			if (CommandBuild.builders.contains(e.getPlayer())) {
				e.setCancelled(false);
			} else {
				e.setCancelled(true);
			}
		} catch (Exception ex) {

		}
	}

	@EventHandler
	public void onBuild(PlayerPickupItemEvent e) {
		try {
			if (CommandBuild.builders.contains(e.getPlayer())) {
				e.setCancelled(false);
			} else {
				e.setCancelled(true);
			}
		} catch (Exception ex) {

		}
	}

	@EventHandler
	public void onBuild(InventoryClickEvent e) {
		try {
			if (CommandBuild.builders.contains((Player) e.getWhoClicked())) {
				e.setCancelled(false);
			} else {
				e.setCancelled(true);

			}
		} catch (Exception ex) {

		}
	}

	@EventHandler
	public void onBuild(EntityDamageEvent e) {
		try {
			if (e.getCause() == DamageCause.FALL) {
				e.setCancelled(true);
			}
			e.setCancelled(true);
		} catch (Exception ex) {

		}
	}

}
