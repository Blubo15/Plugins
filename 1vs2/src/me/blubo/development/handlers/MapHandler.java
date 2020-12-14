package me.blubo.development.handlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.blubo.development.Main;

public class MapHandler {

	public static File file = new File("plugins" + File.separator + "1vs1", "maps.yml");
	public static FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

	@SuppressWarnings("unchecked")
	public static void setArena(int oneortwo, Player target, String name) {
		if (oneortwo != 1 && oneortwo != 2) {
			target.sendMessage(Main.prefix + "Bitte gib an 1 oder 2.");
		}
		double x = target.getLocation().getX();
		double y = target.getLocation().getY();
		double z = target.getLocation().getZ();
		double yaw = target.getLocation().getYaw();
		double pitch = target.getLocation().getPitch();
		String world = target.getLocation().getWorld().getName().toLowerCase();

		List<String> test = null;
		try {
			test = (List<String>) cfg.getList("Arenas.");
			test.add(name.toLowerCase());
		} catch (Exception ex) {
			test = new ArrayList<String>();
			test.add("test");
		}
		cfg.set("Arenas", test);
		cfg.set("Maps." + name.toLowerCase() + "." + oneortwo, name);
		cfg.set("Maps." + name.toLowerCase() + "." + oneortwo + ".x", x);
		cfg.set("Maps." + name.toLowerCase() + "." + oneortwo + ".y", y);
		cfg.set("Maps." + name.toLowerCase() + "." + oneortwo + ".z", z);
		cfg.set("Maps." + name.toLowerCase() + "." + oneortwo + ".yaw", yaw);
		cfg.set("Maps." + name.toLowerCase() + "." + oneortwo + ".pitch", pitch);
		cfg.set("Maps." + name.toLowerCase() + "." + oneortwo + ".world", world.toLowerCase());

		target.sendMessage(Main.prefix + "Du hast erfolgreich den Spawn-Punkt des " + oneortwo
				+ " Spielers für die Arena " + name + " ersetzt bzw. gesetzt!");
		try {
			cfg.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static ArrayList<Location> getArenaLocations(String name) {
		ArrayList<Location> map = new ArrayList<Location>();

		for (int i = 1; i <= 2; i++) {
			double x = cfg.getDouble("Maps." + name.toLowerCase() + "." + i + ".x");
			double y = cfg.getDouble("Maps." + name.toLowerCase() + "." + i + ".y");
			double z = cfg.getDouble("Maps." + name.toLowerCase() + "." + i + ".z");
			double yaw = cfg.getDouble("Maps." + name.toLowerCase() + "." + i + ".yaw");
			double pitch = cfg.getDouble("Maps." + name.toLowerCase() + "." + i + ".pitch");
			String world = cfg.getString("Maps." + name.toLowerCase() + "." + i + ".world");
			Location loc = new Location(Bukkit.getWorld(world.toLowerCase()), x, y, z);
			loc.setYaw((float) yaw);
			loc.setPitch((float) pitch);
			map.add(loc);
		}

		return map;

	}

	public static void setLobby(Player target) {
		double x = target.getLocation().getX();
		double y = target.getLocation().getX();
		double z = target.getLocation().getX();
		double yaw = target.getLocation().getX();
		double pitch = target.getLocation().getX();
		String world = target.getLocation().getWorld().getName().toLowerCase();
		cfg.set("Lobby.x", x);
		cfg.set("Lobby.y", y);
		cfg.set("Lobby.z", z);
		cfg.set("Lobby.yaw", yaw);
		cfg.set("Lobby.pitch", pitch);
		cfg.set("Lobby.world", world.toLowerCase());

		try {
			cfg.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Location getLobbyLocation() {
		double x = cfg.getDouble("Lobby.x");
		double y = cfg.getDouble("Lobby.y");
		double z = cfg.getDouble("Lobby.z");
		double yaw = cfg.getDouble("Lobby.yaw");
		double pitch = cfg.getDouble("Lobby.pitch");
		String world = cfg.getString("Lobby.world");

		Location loc = new Location(Bukkit.getWorld(world), x, y, z);
		loc.setYaw((float) yaw);
		loc.setPitch((float) pitch);

		return loc;

	}

}
