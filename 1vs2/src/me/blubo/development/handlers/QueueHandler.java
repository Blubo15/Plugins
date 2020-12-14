package me.blubo.development.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.blubo.development.Main;

public class QueueHandler {

	public static HashMap<Player, Player> queue_for_battles = new HashMap<Player, Player>();

	public static void startGameHandling_Private() {
		if (!queue_for_battles.isEmpty()) {
			Player host = null;
			for (Entry<Player, Player> p : queue_for_battles.entrySet()) {
				host = p.getKey();
				System.out.println(host.getName());
			}
			Player challenger = queue_for_battles.get(host);
			try {
				String arena = getMap(host, challenger);
				if (arena != null) {
					GameHandler.startGame(arena, host, challenger);
					queue_for_battles.remove(host);
				}
			} catch (NullPointerException ex) {
				ex.printStackTrace();
			}
		} else {
			Bukkit.broadcastMessage(Main.prefix+"§cEtwas ist schief gelaufen.");
		}
	}

	static int sched2;
	static boolean found = false;
	static String arena = null;

	@SuppressWarnings("unchecked")
	private static String getMap(Player host, Player challenger) {
		List<String> maps = (List<String>) MapHandler.cfg.getList("Arenas");
		int total = new Random().nextInt(maps.size());
		String arenanow = maps.get(total);
		if (!GameHandler.available_games.contains(arenanow)) {
			sched2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
				int checked = 0;

				@Override
				public void run() {
					int total2 = new Random().nextInt(maps.size());
					String arenanow2 = maps.get(total2);
					if (GameHandler.available_games.contains(arenanow2)) {
						host.sendMessage(Main.prefix + "Ein Spiel (" + arenanow2 + " wurde gefunden. ");
						challenger.sendMessage(Main.prefix + "Ein Spiel (" + arenanow2 + " wurde gefunden. ");
						found = true;
						arena = arenanow2;
						Bukkit.getScheduler().cancelTask(sched2);
					}
					checked++;
					if (checked == GameHandler.available_games.size()) {
						host.sendMessage(Main.prefix + "Im moment sind keine Spiele verfügbar. Versuche es später erneut.");
						challenger.sendMessage(Main.prefix + "Im moment sind keine Spiele verfügbar. Versuche es später erneut.");
						Bukkit.getScheduler().cancelTask(sched2);
					}

				}

			}, 0, 10);

			return arena;

		} else {
			host.sendMessage(Main.prefix + "Das Spiel §4§l" + arenanow + " §7 ist frei, starte Spiel...");
			challenger.sendMessage(Main.prefix + "Das Spiel §4§l" + arenanow + " §7 ist frei, starte Spiel...");
			return arenanow;
		}
	}
}
