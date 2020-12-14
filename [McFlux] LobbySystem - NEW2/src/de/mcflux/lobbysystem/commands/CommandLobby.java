package de.mcflux.lobbysystem.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.mcflux.lobbysystem.api.PlayerAPI;
import de.mcflux.lobbysystem.manager.LocationManager;

public class CommandLobby implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			PlayerAPI player = new PlayerAPI((Player) sender);
			if (args.length == 0) {
				try {
					player.teleport(LocationManager.getLocation("Spawn"));
				} catch (NullPointerException ex) {
					player.sendMessage("§cDer Spawn wurde noch nicht gesetzt! Bitte melde dies einem Administrator!");
				}
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("setspawn")) {
					if (player.hasPermission("lobbysystem.setspawn")) {
						LocationManager.setLocation("Spawn", player.getPlayer());
						player.sendMessage("Der Spawn wurde erfolgreich gesetzt!");
					}
				}
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("setwarp")) {
					if (player.hasPermission("lobbysystem.setwarp")) {
						String warpName = args[1];
						LocationManager.setLocation(warpName, player.getPlayer());
						player.sendMessage("Location §e" + warpName + " §7gesetzt.");
					}
				}
			} else if (args.length == 4) {

			} else {
				player.sendUsage("lobby <setwarp, setspawn> <Item> <Slot> <Name>");
			}

		} else {

			Bukkit.getConsoleSender().sendMessage("§cDu bist eine Konsole!");

		}
		return true;
	}

}
