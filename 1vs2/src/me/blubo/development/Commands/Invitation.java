package me.blubo.development.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.blubo.development.Main;
import me.blubo.development.events.InteractEvent;
import me.blubo.development.handlers.QueueHandler;

public class Invitation implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		Player origsender = null;
		if (InteractEvent.awaitingconfirmation.containsValue((Player) sender)) {
			for (Player k : InteractEvent.awaitingconfirmation.keySet()) {
				if (InteractEvent.awaitingconfirmation.get(k).equals((Player) sender)) {
					origsender = k;
				}
			}
		}

		if (origsender != null) {
			if (args[0].equalsIgnoreCase("deny")) {
				origsender.sendMessage(Main.prefix + "§7Der Spieler " + sender.getName() + " hat deine Einladung §cabgelehnt§7.");
				sender.sendMessage(Main.prefix + "§7Du hast erfolgreich die Einladung des Spielers " + origsender.getName() + " §cabgelehnt§7.");
				InteractEvent.awaitingconfirmation.remove(origsender);
			} else if (args[0].equalsIgnoreCase("accept")) {
				origsender.sendMessage(Main.prefix + "Der Spieler " + sender.getName() + " hat deine Einladung §aangenommen.");
				sender.sendMessage(Main.prefix + "§7Du hast erfolgreich die Einladung des Spielers " + origsender.getName() + " §aangenommen§7.");
				QueueHandler.queue_for_battles.put(origsender, (Player) sender);
				QueueHandler.startGameHandling_Private();
				InteractEvent.awaitingconfirmation.remove(origsender);
			}
		} else if (origsender == null) {
			sender.sendMessage(Main.prefix + "Du hast noch keine Einladung erhalten.");
		}
		return true;
	}

}
