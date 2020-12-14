package me.blubo.development.handlers;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.blubo.development.Main;
import me.blubo.development.events.DeathEvent;

public class GameHandler {
	
	public static HashMap<String, ArrayList<Player>> running_games = new HashMap<String, ArrayList<Player>>();
	public static HashMap<Player, Integer> points = new HashMap<Player, Integer>(); 
	public static HashMap<String, HashMap<Player, String>> kits = new HashMap<String, HashMap<Player, String>>(); 
	public static ArrayList<String> available_games = new ArrayList<String>(); 
	public static ArrayList<Player> players_in_countdown = new ArrayList<Player>(); 
	
	public static void startGame(String arena, Player host, Player challenger) {
		Location loc1 = MapHandler.getArenaLocations(arena).get(0); 
		Location loc2 = MapHandler.getArenaLocations(arena).get(1);
		
		host.teleport(loc1); 
		challenger.teleport(loc2); 
		
		startCountdownForMatch(host, challenger, arena); 
		
	}
	static int sched1; 
	private static void startCountdownForMatch(Player host, Player challenger, String arena) {
		players_in_countdown.add(host); 
		players_in_countdown.add(challenger); 
		sched1 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
			int i = 10; 
			@Override
			public void run() {
				if(i >= 2) {
					host.sendMessage(Main.prefix+"Der Kampf wird in §4§l"+i+" §7sekunden beginnen.");
					challenger.sendMessage(Main.prefix+"Der Kampf wird in §4§l"+i+" §7sekunden beginnen.");
				} else if(i == 1) {
					host.sendMessage(Main.prefix+"Der Kampf wird in §4§l"+i+" §7sekunde beginnen.");
					challenger.sendMessage(Main.prefix+"Der Kampf wird in §4§l"+i+" §7sekunde beginnen.");
				} else {
					host.sendMessage(Main.prefix+"Der Kampf hat begonnen.");
					challenger.sendMessage(Main.prefix+"Der Kampf hat begonnen.");
					players_in_countdown.remove(host);
					players_in_countdown.remove(challenger); 
					
					ArrayList<Player> players = new ArrayList<Player>(); 
					players.add(host); 
					players.add(challenger); 
					
					running_games.put(arena, players); 
					
					points.put(host, 0); 
					points.put(challenger, 0); 
					
					DeathEvent.setKits(arena);
					
					Bukkit.getScheduler().cancelTask(sched1);
				}
				
				i--; 
				
			}
			
		}, 0, 20);
	}

}
