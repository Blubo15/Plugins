package de.mcflux.lobbysystem;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import de.mcflux.lobbysystem.commands.CommandBuild;
import de.mcflux.lobbysystem.commands.CommandLobby;
import de.mcflux.lobbysystem.data.Data;
import de.mcflux.lobbysystem.events.ListenerBuild;
import de.mcflux.lobbysystem.events.ListenerChat;
import de.mcflux.lobbysystem.events.ListenerJoin;
import de.mcflux.lobbysystem.events.ListenerJumpPads;
import de.mcflux.lobbysystem.events.ListenerOther;
import de.mcflux.lobbysystem.items.event.ItemsEvent;
import de.mcflux.lobbysystem.mongodb.ClanAPI;

public class LobbySystem extends JavaPlugin {

	private static LobbySystem lobbySystem;
	private static ClanAPI clanAPI;
	private static MongoClient client;

	public void onEnable() {
		lobbySystem = this;
		loadMongo();
		Data.setPrefix("§8● §6§lFluxLobby §8» §7");
		load();
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		clanAPI = new ClanAPI(); 
		
		File config = new File(getDataFolder(), "config.yml");
		if (!config.exists()) {
			saveDefaultConfig();
		}
	}

	public void loadMongo() {
		client = new MongoClient(new MongoClientURI("mongodb://test:test123@localhost:27017"));
	}

	public void load() {
		PluginManager pluginManager = Bukkit.getPluginManager();
		pluginManager.registerEvents(new ItemsEvent(), this);
		pluginManager.registerEvents(new ListenerJoin(), this);
		pluginManager.registerEvents(new ListenerBuild(), this);
		pluginManager.registerEvents(new ListenerChat(), this);
		pluginManager.registerEvents(new ListenerOther(), this);
		pluginManager.registerEvents(new ListenerJumpPads(), this);

		getCommand("build").setExecutor(new CommandBuild());
		getCommand("slobby").setExecutor(new CommandLobby());
	}

	public void onDisable() {
	}

	public static LobbySystem getLobbySystem() {
		return lobbySystem;
	}

	public static MongoClient getMongo() {
		return client;
	}

	public static ClanAPI getClanAPI() {
		return clanAPI;
	}

}
