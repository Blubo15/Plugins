package de.mcflux.lobbysystem.items.event;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.mcflux.lobbysystem.api.PlayerAPI;
import de.mcflux.lobbysystem.items.ItemsJoin;
import de.mcflux.lobbysystem.manager.LobbyAPI;
import de.mcflux.lobbysystem.manager.LocationManager;

public class ItemsEvent implements Listener {

	@EventHandler
	public void onNick(PlayerInteractEvent e) {
		try {
			PlayerAPI player = new PlayerAPI(e.getPlayer());
			if ((e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemsJoin.getItemName("Nick §8| §aOn"))) ||
					(e.getItem().getItemMeta().getDisplayName()
							.equalsIgnoreCase(ItemsJoin.getItemName("Nick §8| §cOff")))) {
				LobbyAPI.setNicked(player.getPlayer().getUniqueId().toString());
				ItemsJoin.getNick(player);
			}
		} catch (Exception localException) {
		}
	}

	@EventHandler
	public void onHide(PlayerInteractEvent e) {
		try {
			PlayerAPI player = new PlayerAPI(e.getPlayer());
			if ((e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemsJoin.getItemName("Spieler anzeigen")))
					||

					(e.getItem().getItemMeta().getDisplayName()
							.equalsIgnoreCase(ItemsJoin.getItemName("Spieler verstecken")))) {
				LobbyAPI.setHider(player.getPlayer());
				ItemsJoin.getHider(player);
			}
		} catch (Exception localException) {
		}
	}

	@EventHandler
	public void onNavigator(PlayerInteractEvent e) {
		try {
			PlayerAPI player = new PlayerAPI(e.getPlayer());
			if (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemsJoin.getItemName("Navigator"))) {
				LobbyAPI.openNavigator(player.getPlayer());
			}
		} catch (Exception localException) {
		}
	}

	@EventHandler
	public void onExtras(PlayerInteractEvent e) {
		try {
			PlayerAPI player = new PlayerAPI(e.getPlayer());
			if (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemsJoin.getItemName("Extras"))) {
				LobbyAPI.openExtras(player.getPlayer());
			}
		} catch (Exception localException) {
		}
	}

	@EventHandler
	public void onProfil(PlayerInteractEvent e) {
		try {
			PlayerAPI player = new PlayerAPI(e.getPlayer());
			if (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemsJoin.getItemName("Profil"))) {
				LobbyAPI.openProfile(player.getPlayer());
			}
		} catch (Exception localException) {
		}
	}

	@EventHandler
	public void onNavigatorClick(InventoryClickEvent e) {
		try {
			PlayerAPI player = new PlayerAPI((Player) e.getWhoClicked());
			if ((e.getInventory().getName().equalsIgnoreCase("§9Navigator"))
					&& (e.getCurrentItem().getType() != Material.STAINED_GLASS_PANE)) {
				String warpName = e.getCurrentItem().getItemMeta().getDisplayName().replace("§e", "");
				if (warpName.equalsIgnoreCase("Bauserver")) {
					player.sendMessage("Du wirst auf den §eBauserver §7weitergeleitet.");
					player.sendToServer("Bauserver-1");
				} else {
					player.teleport(LocationManager.getLocation(warpName));
				}
			}
		} catch (Exception localException) {
		}
	}

	@EventHandler
	public void onSilentLobby(PlayerInteractEvent e) {
		try {
			PlayerAPI player = new PlayerAPI(e.getPlayer());
			if (e.getItem().getItemMeta().getDisplayName()
					.equalsIgnoreCase(ItemsJoin.getItemName("Silent-Lobby §8| §cOff"))) {
				for (Player players : Bukkit.getOnlinePlayers()) {
					player.getPlayer().hidePlayer(players);
				}
				LobbyAPI.setSilentLobby(player.getPlayer().getUniqueId().toString());
				ItemsJoin.getSilentLobby(player);
			} else if (e.getItem().getItemMeta().getDisplayName()
					.equalsIgnoreCase(ItemsJoin.getItemName("Silent-Lobby §8| §aOn"))) {
				for (Player players : Bukkit.getOnlinePlayers()) {
					player.getPlayer().showPlayer(players);
				}
				LobbyAPI.setSilentLobby(player.getPlayer().getUniqueId().toString());
				ItemsJoin.getSilentLobby(player);
			}
		} catch (Exception localException) {
		}
	}

}
