package me.blubo.development.Commands;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.blubo.development.Main;
import me.blubo.development.handlers.MapHandler;

public class setLocation implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Main.prefix + "§cDieser Befehl ist nur für Spieler gedacht.");
		}
		if (args.length == 2) {
			String arena = args[0];
			int pos = 0;
			try {
				pos = Integer.valueOf(args[1]);
			} catch (NumberFormatException ex) {
				sender.sendMessage(Main.prefix + "§cDas erste Argument muss eine Zahl sein!");
			}
			MapHandler.setArena(pos, (Player) sender, arena);
		} else if (args.length == 1) {
			try {
				if (args[0].equalsIgnoreCase("lobby")) {
					MapHandler.setLobby((Player) sender);
					sender.sendMessage(Main.prefix + "§7Du hast erfolgreich den §4§lLobby-Punkt§7 gesetzt.");
				} else {
					ArrayList<Location> locs = MapHandler.getArenaLocations(args[0]);
					boolean point1 = true;
					boolean point2 = true;
					try {
						Location loc1 = locs.get(1);
					} catch (NullPointerException ex) {
						point1 = false;
					}
					try {
						Location loc2 = locs.get(0);
					} catch (NullPointerException ex) {
						point2 = false;
					}
					sender.sendMessage(Main.prefix + "----ARENA INFO (" + args[0] + ")----");
					if (point1) {
						sender.sendMessage("Location 1: §aEingerichtet");
					} else {
						sender.sendMessage("Location 1: §cNicht eingerichtet");
					}

					if (point2) {
						sender.sendMessage("Location 2: §aEingerichtet");
					} else {
						sender.sendMessage("Location 2: §cNicht eingerichtet");
					}
					sender.sendMessage(Main.prefix + "----ARENA INFO (" + args[0] + ")----");
				}
			} catch (Exception ex) {
				sender.sendMessage(Main.prefix + "§cDiese Arena existiert noch nicht!");
			}
		} else {
			sender.sendMessage(Main.prefix + "§cBitte nutze /setloc <NameOfArena> | <NameOfArena> <1|2>!");
		}
		return true;
	}

}
