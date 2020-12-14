package de.mcflux.lobbysystem.events;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

public class ListenerJumpPads implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJumppad(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		if ((player.getLocation().getBlock().getType() == Material.GOLD_PLATE)
				&& ((player.getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.REDSTONE_LAMP_OFF)
						|| (player.getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock()
								.getType() == Material.REDSTONE_LAMP_ON))) {
			Vector v = player.getLocation().getDirection().multiply(3.0D).setY(1);
			player.setVelocity(v);
			player.playEffect(player.getLocation(), Effect.MAGIC_CRIT, 1);
			player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 1.0F, 1.0F);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDoublejump(PlayerToggleFlightEvent e) {
		Player player = e.getPlayer();
		if (player.getGameMode() == GameMode.SURVIVAL) {
			e.setCancelled(true);
			player.setAllowFlight(false);
			player.setFlying(false);
			Vector v = player.getLocation().getDirection().multiply(3.0D).setY(1);
			player.setVelocity(v);
			player.playEffect(player.getLocation(), Effect.MAGIC_CRIT, 1);
			player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 1.0F, 1.0F);
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		if ((player.getGameMode() == GameMode.SURVIVAL)
				&& (player.getLocation().add(0.0D, -1.0D, 0.0D).getBlock().getType() != Material.AIR)) {
			player.setAllowFlight(true);
			player.setFlying(false);
		}
	}

}
