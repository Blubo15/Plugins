package me.blubo.development;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.blubo.development.Commands.Invitation;
import me.blubo.development.Commands.setLocation;
import me.blubo.development.events.DeathEvent;
import me.blubo.development.events.InteractEvent;
import me.blubo.development.events.MoveEvent;
import me.blubo.development.handlers.GameHandler;
import me.blubo.development.handlers.MapHandler;

public class Main extends JavaPlugin{
	
	public static Main main; 
	
	public static String prefix = "§7[§e1vs1§7] §7"; 
	
	public void onEnable() {
		main = this; 
		setMaps(); 
		Bukkit.getPluginManager().registerEvents(new InteractEvent(), this);
		Bukkit.getPluginManager().registerEvents(new MoveEvent(), this);
		Bukkit.getPluginManager().registerEvents(new DeathEvent(), this);
		Bukkit.getPluginCommand("invite").setExecutor(new Invitation());
		Bukkit.getPluginCommand("setloc").setExecutor(new setLocation());
		
	}
	
	public void onDisable() {
		
	}
	
	public static Main getInstance() {
		return main; 
	}
	
	@SuppressWarnings("unchecked")
	public static void setMaps() {
		ArrayList<String> maps = (ArrayList<String>) MapHandler.cfg.get("Arenas"); 
		GameHandler.available_games = maps; 
	}

}
