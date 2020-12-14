package de.mcflux.lobbysystem.manager;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import de.mcflux.lobbysystem.LobbySystem;
import de.mcflux.lobbysystem.api.ItemAPI;
import de.mcflux.lobbysystem.api.RangAPI;

public class LobbyAPI {

	private static ArrayList<Player> hiderAll;

	static {
		LobbyAPI.hiderAll = new ArrayList<Player>();
	}

	@SuppressWarnings("deprecation")
	private static DBCollection coll = LobbySystem.getMongo().getDB("lobby").getCollection("lobby");

	public static boolean playerExist(String uuid) {
		return coll.find(new BasicDBObject("_id", uuid)).hasNext();
	}

	public static void createPlayer(CloudPlayer player) {
		String name = player.getName();
		String uuid = player.getUniqueId().toString();
		if (!playerExist(uuid)) {
			DBObject obj = new BasicDBObject("_id", uuid).append("name", name).append("nick", "")
					.append("isNicked", false).append("silentlobby", false);
			coll.insert(obj); 
		} else {
			coll.update(new BasicDBObject("_id", uuid), new BasicDBObject("$set", new BasicDBObject("name", name)));

		}
	}

	public static boolean getSetting(String uuid, String setting) {
		if (playerExist(uuid))
			return (boolean) coll.find(new BasicDBObject("_id", uuid), new BasicDBObject(setting, 1)).one()
					.get(setting);
		CloudAPI.getInstance().getLogger().log(new LogRecord(Level.SEVERE, "Cloud not find Player " + uuid + " / " + CloudAPI.getInstance().getPlayerName(UUID.fromString(uuid))));
		return false; 
	}

	public static void setSetting(String uuid, String setting) {
		if (playerExist(uuid)) {
			boolean value; 
			if (getSetting(uuid, setting)) {
				value = true;
			} else {
				value = false;
			}
			coll.update(coll.find(new BasicDBObject("_id", uuid), new BasicDBObject(setting, 1)).one(), new BasicDBObject("$set", new BasicDBObject(setting, value))); 
		} else {
			CloudAPI.getInstance().getLogger().log(new LogRecord(Level.SEVERE, "Cloud not find Player " + uuid + " / " + CloudAPI.getInstance().getPlayerName(UUID.fromString(uuid))));
		}
	}

	public static void setNicked(String uuid) {
		setSetting(uuid, "isNicked"); 
	}

	public static void setSilentLobby(String uuid) {
		setSetting(uuid, "silentlobby");
	}

	public static void setHider(Player player) {
		if (LobbyAPI.hiderAll.contains(player)) {
			LobbyAPI.hiderAll.remove(player);
			Bukkit.getOnlinePlayers().forEach(all -> player.showPlayer(all));
		} else {
			LobbyAPI.hiderAll.add(player);
			Bukkit.getOnlinePlayers().forEach(all -> player.hidePlayer(all));
		}
	}

	public static ArrayList<Player> getHiderAll() {
		return LobbyAPI.hiderAll;
	}

	public static Inventory createInventory(Player player, String inventoryName, int size) {
		Inventory inventory = Bukkit.createInventory((InventoryHolder) player, size, inventoryName);
		for (int i = 0; i < size; ++i) {
			inventory.setItem(i, new ItemAPI(Material.STAINED_GLASS_PANE).setData((short) 7).setNoName().build());
		}
		return inventory;
	}

	public static void openNavigator(Player player) {
		Inventory inventory = createInventory(player, "§9Navigator", 27);
		inventory.setItem(3, new ItemAPI(Material.STICK).setDisplayName("§eKnockIT").build());
		inventory.setItem(5, new ItemAPI(Material.TNT).setDisplayName("§eTNTRun").build());
		inventory.setItem(11, new ItemAPI(Material.GRASS).setDisplayName("§eFreebuild").build());
		inventory.setItem(13, new ItemAPI(Material.EXP_BOTTLE).setDisplayName("§eSpawn").build());
		inventory.setItem(15, new ItemAPI(Material.GRASS).setDisplayName("§eCitybuild").build());
		inventory.setItem(21, new ItemAPI(Material.IRON_SWORD).setDisplayName("§eSkyWars").build());
		inventory.setItem(23, new ItemAPI(Material.SANDSTONE).setDisplayName("§eBuildFFA").build());
		inventory.setItem(26, new ItemAPI(Material.DIAMOND).setDisplayName("§eEvent").build());
		if (player.hasPermission("cloud.server.build")) {
			inventory.setItem(18, new ItemAPI(Material.IRON_PICKAXE).setDisplayName("§eBauserver").build());
		}
		player.openInventory(inventory);
	}

	public static void openExtras(Player player) {
		Inventory inventory = createInventory(player, "§9Extras", 27);
		inventory.setItem(10,
				new ItemAPI(Material.LEATHER_BOOTS).setColor(Color.RED).setDisplayName("§eBoots").build());
		inventory.setItem(12, new ItemAPI(Material.ENDER_PEARL).setDisplayName("§eGadgets").build());
		inventory.setItem(14, new ItemAPI(Material.PAPER).setDisplayName("§eStats-Reset Tokens").build());
		inventory.setItem(16, new ItemAPI(Material.SKULL_ITEM).setData((short) 3).setSkullOwner("MHF_Question")
				.setDisplayName("§cSpoilerschutz").build());
		player.openInventory(inventory);
	}

	public static void openProfile(Player player) {
		Inventory inventory = createInventory(player, "§9Profil", 27);
		inventory.setItem(12,
				new ItemAPI(Material.SKULL_ITEM).setData((short) 3).setSkullOwner(player.getName())
						.setDisplayName("§a" + player.getName())
						.addLoreLine("§7Rang §8- " + RangAPI.getColorOfRank(RangAPI.getRang(player.getName()))
								+ RangAPI.getRang(player.getName()))
						.addLoreLine("§7Coins §8- §eFeature not available").addLoreLine("").build());
		inventory.setItem(14, new ItemAPI(Material.PAPER).setDisplayName("§eStats").build());
		player.openInventory(inventory);
	}
}
