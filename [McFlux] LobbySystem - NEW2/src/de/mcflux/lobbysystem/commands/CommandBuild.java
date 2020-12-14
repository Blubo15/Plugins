package de.mcflux.lobbysystem.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.mcflux.lobbysystem.api.PlayerAPI;
import de.mcflux.lobbysystem.data.BuildTypes;
import de.mcflux.lobbysystem.items.ItemsJoin;

public class CommandBuild implements CommandExecutor {

	public static ArrayList<Player> builders = new ArrayList<Player>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			PlayerAPI player = new PlayerAPI((Player) sender);
			if (player.hasPermission("lobbysystem.build")) {
				if (args.length == 0) {
					if (builders.contains(player.getPlayer())) {
						changeBuildMode(player, player, BuildTypes.CAN_NOT_BUILD);
					} else {
						changeBuildMode(player, player, BuildTypes.CAN_BUILD);
					}
				} else if (args.length == 1) {
					String playerName = args[0];
					PlayerAPI target = new PlayerAPI(Bukkit.getPlayerExact(playerName));
					if (target.isOnline()) {
						if (builders.contains(target.getPlayer())) {
							changeBuildMode(player, target, BuildTypes.CAN_NOT_BUILD);
						} else {
							changeBuildMode(player, target, BuildTypes.CAN_BUILD);
						}
					} else {
						player.sendMessage("Der §eSpieler §7ist §cnicht §7online.");
					}
				} else {
					player.sendMessage("Verwende: §a/build <Spieler>");
				}
			} else {
				player.sendMessage("Du hast keine §c§lBerechtigung§7!");
			}
		} else {
			sender.sendMessage("Du bist eine Konsole");
		}
		return false;
	}

	private static void changeBuildMode(PlayerAPI arg0, PlayerAPI arg1, BuildTypes buildMode) {
		if (buildMode == BuildTypes.CAN_BUILD) {
			if (!builders.contains(arg1.getPlayer())) {
				builders.add(arg1.getPlayer());
				arg1.clearInventory();
				arg0.sendMessage("Bau-Modus für §e" + arg1.getName() + " §7auf §aaktiviert §7gesetzt.");
			} else {
				arg0.sendMessage("Der §eSpieler §7kann bereits bauen.");
			}
		} else if (buildMode == BuildTypes.CAN_NOT_BUILD) {
			if (builders.contains(arg1.getPlayer())) {
				builders.remove(arg1.getPlayer());
				arg1.clearInventory();
				ItemsJoin.setLobbyItems(arg1);
				arg0.sendMessage("Bau-Modus für §e" + arg1.getName() + " §7auf §cdeaktiviert §7gesetzt.");
			} else {
				arg0.sendMessage("Der §eSpieler §7kann bereits §cnicht §7bauen.");
			}
		} else {
			arg0.sendMessage("Not found");
		}
	}

}
