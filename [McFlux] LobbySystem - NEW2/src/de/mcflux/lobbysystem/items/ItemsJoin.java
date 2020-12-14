package de.mcflux.lobbysystem.items;

import org.bukkit.Material;

import de.mcflux.lobbysystem.api.ItemAPI;
import de.mcflux.lobbysystem.api.PlayerAPI;
import de.mcflux.lobbysystem.manager.LobbyAPI;

public class ItemsJoin {

	public static void setLobbyItems(PlayerAPI player) {
		player.clearInventory();
		player.setItem(0, new ItemAPI(Material.COMPASS).setDisplayName(getItemName("Navigator")).build());
		getHider(player);
		getNick(player);
		player.setItem(4, new ItemAPI(Material.BARRIER).setDisplayName(getItemName("Kein Gadget ausgewählt")).build());
		getSilentLobby(player);
		player.setItem(7, new ItemAPI(Material.CHEST).setDisplayName(getItemName("Extras")).build());
		player.setItem(8, new ItemAPI(Material.SKULL_ITEM).setData((short)3).setSkullOwner(player.getName())
			      .setDisplayName(getItemName("Freunde")).build());
	}

	public static void getNick(PlayerAPI player) {
		if (player.hasPermission("nick.use")) {
			if (LobbyAPI.getSetting(player.getPlayer().getUniqueId().toString(), "isNicked")) {
				player.setItem(3,
						new ItemAPI(Material.NAME_TAG).setDisplayName("§8● §eNick §8| §cCOMING SOON").setGlow().build());
			} else {
				player.setItem(3, new ItemAPI(Material.NAME_TAG).setDisplayName(getItemName("Nick §8| §cCOMING SOON")).build());
			}
		}
	}

	public static void getHider(PlayerAPI player) {
		if (LobbyAPI.getHiderAll().contains(player.getPlayer())) {
			player.setItem(1,
					new ItemAPI(Material.STICK).setDisplayName(getItemName("Spieler anzeigen")).build());
		} else {
			player.setItem(1,
					new ItemAPI(Material.BLAZE_ROD).setDisplayName(getItemName("Spieler verstecken")).build());
		}
	}

	public static void getSilentLobby(PlayerAPI player) {
		if (player.hasPermission("silentlobby.use")) {
			if (LobbyAPI.getSetting(player.getPlayer().getUniqueId().toString(), "silentlobby")) {
				player.setItem(5, new ItemAPI(Material.TNT).setDisplayName(getItemName("Silent-Lobby §8| §aOn"))
						.setGlow().build());
			} else {
				player.setItem(5,
						new ItemAPI(Material.TNT).setDisplayName(getItemName("Silent-Lobby §8| §cOff")).build());
			}
		}
	}

	public static String getItemName(String arg0) {
		return "§8● §e" + arg0 + " §8» §7Rechtsklick";

	}

}
