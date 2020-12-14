package me.blubo.development;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import me.blubo.development.Commands.CommandClan;
import me.blubo.development.Listener.JoinListener;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {

	private static Main instance;
	private static MongoClient mongo;

	public void onEnable() {
		instance = this;

		mongo = new MongoClient(new MongoClientURI("mongodb://test:test123@localhost:27017")); 

		BungeeCord.getInstance().getPluginManager().registerCommand(this, new CommandClan("clan"));
		BungeeCord.getInstance().getPluginManager().registerListener(this, new JoinListener());

		getProxy().registerChannel("clan:channel");

	}

	public static MongoClient getMongo() {
		// TODO Auto-generated method stub
		return mongo;
	}

	public static Main getInstance() {
		return instance;
	}

}
