package de.mcflux.lobbysystem.events;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.dytanic.cloudnet.api.CloudAPI;
import de.mcflux.lobbysystem.LobbySystem;
import de.mcflux.lobbysystem.api.PlayerAPI;
import de.mcflux.lobbysystem.data.Data;
import de.mcflux.lobbysystem.items.ItemsJoin;
import de.mcflux.lobbysystem.manager.LobbyAPI;
import de.mcflux.lobbysystem.manager.LocationManager;
import de.mcflux.lobbysystem.manager.ScoreboardManager;
import de.mcflux.lobbysystem.mongodb.FriendManager;

public class ListenerJoin implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		String uuid = e.getPlayer().getUniqueId().toString(); 
		PlayerAPI player = new PlayerAPI(e.getPlayer());
		LobbyAPI.createPlayer(CloudAPI.getInstance().getOnlinePlayer(UUID.fromString(uuid)));
		e.setJoinMessage(null);

		ItemsJoin.setLobbyItems(player);
		player.sendTitle("§8● §6§lFluxLobby §8§ §7", "§aWillkommen, " + player.getName());
		for (Player all : Bukkit.getOnlinePlayers()) {
			ScoreboardManager.setScoreboard(e.getPlayer());
			ScoreboardManager.setScoreboard(all);
		}
		if (LobbyAPI.getSetting(uuid, "silentlobby")) {
			for (Player players : Bukkit.getOnlinePlayers()) {
				player.getPlayer().hidePlayer(players);
			}
		}
		Bukkit.getScheduler().runTaskLater(LobbySystem.getLobbySystem(), new Runnable() {
			public void run() {
				player.teleport(LocationManager.getLocation("Spawn"));
			}
		}, 2L);
		
		HashMap<String, List<String>> list = FriendManager.getList(uuid); 
		List<String> onlineF = list.get("online"); 

		if (onlineF.size() == 0) {
			e.getPlayer().sendMessage(Data.getPrefix() + "Aktuell sind §e" + "keine" + " §7Freunde online!");
		} else if (onlineF.size() == 1) {
			e.getPlayer().sendMessage(Data.getPrefix() + "Aktuell ist §e" + "1" + " §7Freund online!");
		} else {
			e.getPlayer().sendMessage(Data.getPrefix() + "Aktuell sind §e" + onlineF.size() + " §7Freunde online!");
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		e.setQuitMessage(null);
	}

}
