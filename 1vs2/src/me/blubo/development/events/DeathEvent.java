package me.blubo.development.events;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import me.blubo.development.Main;
import me.blubo.development.handlers.GameHandler;
import me.blubo.development.handlers.MapHandler;

public class DeathEvent implements Listener {

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		ArrayList<Player> players = new ArrayList<Player>();
		boolean won = false; 
		players.add(e.getEntity());
		players.add(e.getEntity().getKiller());
		String arena = "";
		int points_for_1 = 0;
		int points_for_2 = 0;
		if (GameHandler.running_games.containsValue(players)) {
			for (String k : GameHandler.running_games.keySet()) {
				if (GameHandler.running_games.get(k).equals(players)) {
					arena = k;
					points_for_1 = GameHandler.points.get(players.get(0));
					points_for_2 = GameHandler.points.get(players.get(1));
				}
			}
		}

		if (e.getEntity().equals(GameHandler.running_games.get(arena).get(0))) {
			System.out.println("test2");
			points_for_2 += 1;
			Bukkit.broadcastMessage(
					Main.prefix + "§7Der Spieler §4§l" + GameHandler.running_games.get(arena).get(1).getName()
							+ "§7 hat nun §c" + points_for_2 + "§7 Punkte.");
			GameHandler.points.replace(GameHandler.running_games.get(arena).get(1), points_for_2);
			if(points_for_2 == 5) {
				won = true; 
			}
		} else { 
			System.out.println("test3");
			points_for_1 += 1;
			Bukkit.broadcastMessage(
					Main.prefix + "§7Der Spieler §4§l" + GameHandler.running_games.get(arena).get(0).getName()
							+ "§7 hat nun §c" + points_for_1 + "§7 Punkte.");
			GameHandler.points.replace(GameHandler.running_games.get(arena).get(0), points_for_1);
			if(points_for_1 == 5) {
				won = true; 
			}
		}
		
		if(!won) {
			e.getEntity().spigot().respawn();
		}

		if (GameHandler.points.get(players.get(0)).equals(5)) {
			Bukkit.broadcastMessage(Main.prefix + "§7Der Spieler §4§l"
					+ GameHandler.running_games.get(arena).get(0).getName() + "§7 hat §cgewonnen§7!");
			GameHandler.running_games.remove(arena); 
			GameHandler.available_games.add(arena); 
			GameHandler.points.remove(e.getEntity()); 
			GameHandler.points.remove(e.getEntity().getKiller()); 
			e.getEntity().teleport(MapHandler.getLobbyLocation()); 
			e.getEntity().getKiller().teleport(MapHandler.getLobbyLocation()); 
			e.getEntity().getKiller().setHealth(20);
			e.getEntity().getKiller().setFoodLevel(20);
		}

		if (GameHandler.points.get(players.get(1)).equals(5)) {
			Bukkit.broadcastMessage(Main.prefix + "§7Der Spieler §4§l"
					+ GameHandler.running_games.get(arena).get(1).getName() + " hat gewonnen!");
			GameHandler.running_games.remove(arena); 
			GameHandler.available_games.add(arena); 
			GameHandler.points.remove(e.getEntity()); 
			GameHandler.points.remove(e.getEntity().getKiller()); 
			e.getEntity().teleport(MapHandler.getLobbyLocation()); 
			e.getEntity().getKiller().teleport(MapHandler.getLobbyLocation()); 
			e.getEntity().getKiller().setHealth(20);
			e.getEntity().getKiller().setFoodLevel(20);
		}
		
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		ArrayList<Player> players = new ArrayList<Player>();
		players.add(e.getPlayer());
		players.add(e.getPlayer().getKiller());
		String arena = "";
		if (GameHandler.running_games.containsValue(players)) {
			for (String k : GameHandler.running_games.keySet()) {
				if (GameHandler.running_games.get(k).equals(players)) {
					arena = k;
				}
			}
		}
		ArrayList<Location> locs = MapHandler.getArenaLocations(arena); 
		Location loc1 = locs.get(0); 
		Location loc2 = locs.get(1); 
		
		e.setRespawnLocation(loc1);
		e.getPlayer().getKiller().teleport(loc2);
		setKits(arena); 
	}
	
	public static void setKits(String arena) {

		ArrayList<Player> players = GameHandler.running_games.get(arena); 
		
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD); 
		ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE); 
		ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS); 
		ItemStack boots = new ItemStack(Material.IRON_BOOTS); 
		ItemStack helmet = new ItemStack(Material.IRON_HELMET); 
		ItemStack bow = new ItemStack(Material.BOW); 
		ItemStack arrows = new ItemStack(Material.ARROW, 32); 
		ItemStack apples = new ItemStack(Material.APPLE, 32); 
		
		items.add(helmet); 
		items.add(chestplate); 
		items.add(leggings);
		items.add(boots); 
		items.add(sword); 
		items.add(bow); 
		items.add(arrows); 
		items.add(apples); 
		
		for(Player player : players) {
			player.getInventory().clear();
			for(ItemStack item : items) { 
				player.getInventory().addItem(item); 
			}
		}
	}
	
}
