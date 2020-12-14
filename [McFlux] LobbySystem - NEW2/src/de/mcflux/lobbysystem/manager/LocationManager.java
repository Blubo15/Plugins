package de.mcflux.lobbysystem.manager;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.mcflux.lobbysystem.LobbySystem;

public class LocationManager {

	private static File locations = new File(LobbySystem.getLobbySystem().getDataFolder(), "locations.yml");
	private static FileConfiguration cfg = YamlConfiguration.loadConfiguration(locations);

	public LocationManager() {

	}

	public static void setLocation(String warpname, Player target) {
		double x = target.getLocation().getX();
		double y = target.getLocation().getY();
		double z = target.getLocation().getZ();
		double yaw = target.getLocation().getYaw();
		double pitch = target.getLocation().getPitch();
		String world = target.getLocation().getWorld().getName();

		cfg.set(warpname+".X", x);
		cfg.set(warpname+".Y", y);
		cfg.set(warpname+".Z", z);
		cfg.set(warpname+".Yaw", yaw);
		cfg.set(warpname+".Pitch", pitch);
		cfg.set(warpname+".world", world.toLowerCase());

		try {
			cfg.save(locations);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static Location getLocation(String warpname) {
		double x = cfg.getDouble(warpname+".X"); 
		double y = cfg.getDouble(warpname+".Y"); 
		double z = cfg.getDouble(warpname+".Z"); 
		double yaw = cfg.getDouble(warpname+".Yaw"); 
		double pitch = cfg.getDouble(warpname+".Pitch"); 
		String world = cfg.getString(warpname+".world"); 
		
		Location loc = new Location(Bukkit.getWorld(world), x, y, z); 
		loc.setYaw((float)yaw);
		loc.setPitch((float)pitch);
		
		return loc; 
		
	}
}
