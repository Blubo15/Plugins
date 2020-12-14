package de.mcflux.lobbysystem.manager;

import java.text.DecimalFormat;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import de.dytanic.cloudnet.lib.player.permission.PermissionPool;
import de.mcflux.lobbysystem.LobbySystem;
import de.mcflux.lobbysystem.mongodb.ClanAPI;
import de.mcflux.lobbysystem.mongodb.FriendManager;

public class ScoreboardManager {
	public static boolean inGroup(Player player, String group) {
		PermissionPool pp = CloudAPI.getInstance().getPermissionPool();
		String grou = CloudAPI.getInstance().getOnlinePlayer(player.getUniqueId()).getPermissionEntity()
				.getHighestPermissionGroup(pp).getName();
		return grou.equalsIgnoreCase(group);
	}

	public static Team getTeamForPlayer(Scoreboard board, Player forWhom) {
		if (inGroup(forWhom, "Owner"))
			return board.getTeam("a");
		if (inGroup(forWhom, "Admin"))
			return board.getTeam("b");
		if (inGroup(forWhom, "srdev"))
			return board.getTeam("c");
		if (inGroup(forWhom, "SrModerator"))
			return board.getTeam("d");
		if (inGroup(forWhom, "Developer"))
			return board.getTeam("e");
		if (inGroup(forWhom, "Moderator"))
			return board.getTeam("f");
		if (inGroup(forWhom, "jrdev"))
			return board.getTeam("g");
		if (inGroup(forWhom, "Supporter"))
			return board.getTeam("h");
		if (inGroup(forWhom, "Azubi"))
			return board.getTeam("i");
		if (inGroup(forWhom, "YouTuber"))
			return board.getTeam("j");
		if (inGroup(forWhom, "pplus"))
			return board.getTeam("k");
		if (inGroup(forWhom, "Ultra"))
			return board.getTeam("l");
		if (inGroup(forWhom, "Premium"))
			return board.getTeam("m");
		return board.getTeam("n");
	}

	public static Team searchTeamsForEntry(Player forWhom, String entry) {
		if (forWhom.getScoreboard() == null)
			setScoreboard(forWhom);
		Scoreboard board = forWhom.getScoreboard();
		for (Team team : board.getTeams()) {
			if (team.hasEntry(entry))
				return team;
		}
		return null;
	}

	public static void setScoreboard(Player p) {
		ClanAPI clanAPI = LobbySystem.getClanAPI();
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		Team owner = board.registerNewTeam("a");
		Team admin = board.registerNewTeam("b");
		Team srdev = board.registerNewTeam("c");
		Team srmod = board.registerNewTeam("d");
		Team developer = board.registerNewTeam("e");
		Team moderator = board.registerNewTeam("f");
		Team jrdev = board.registerNewTeam("g");
		Team supporter = board.registerNewTeam("h");
		Team azubi = board.registerNewTeam("i");
		Team youtuber = board.registerNewTeam("j");
		Team pplus = board.registerNewTeam("k");
		Team ultra = board.registerNewTeam("l");
		Team premium = board.registerNewTeam("m");
		Team spieler = board.registerNewTeam("n");
		owner.setPrefix(format("§4", "O"));
		admin.setPrefix(format("§4", "A"));
		srdev.setPrefix(format("§b", "SD"));
		srmod.setPrefix(format("§c", "SM"));
		developer.setPrefix(format("§b", "D"));
		moderator.setPrefix(format("§c", "M"));
		jrdev.setPrefix(format("§b", "D"));
		supporter.setPrefix(format("§9", "S"));
		azubi.setPrefix(format("§3", "A"));
		youtuber.setPrefix(format("§5", "Y"));
		pplus.setPrefix(format("§e", "P+"));
		ultra.setPrefix(format("§d", "U"));
		premium.setPrefix(format("§6", "P"));
		spieler.setPrefix(format("§7", "S"));
		for (Player player : Bukkit.getOnlinePlayers()) {
			Team playerTeam = getTeamForPlayer(board, player);
			if (playerTeam.hasEntry(player.getName())) {
				playerTeam.removeEntry(player.getName());
			}

			if (clanAPI.isInClan(player.getUniqueId().toString())) {
				playerTeam.setSuffix(" §8[§e" + clanAPI.getClan(player.getUniqueId().toString()) + "§8]");
			}

			playerTeam.addEntry(player.getName());
		}
		Team server = board.registerNewTeam("server");
		server.addEntry("§b§5");
		Team clan = board.registerNewTeam("clanPlayers");
		clan.addEntry("§c§a");
		Team friends = board.registerNewTeam("friends");
		friends.addEntry("§k§l");
		Team coins = board.registerNewTeam("coins");
		coins.addEntry("§l§k");

		if (clanAPI.isInClan(p.getUniqueId().toString())) {
			clan.setPrefix(getClanString(p).split("Ñ")[0]);
			clan.setSuffix(getClanString(p).split("Ñ")[1]);
		} else {
			clan.setPrefix(getClanString(p));
		}
		server.setPrefix("§8» §f" + CloudAPI.getInstance().getServerId().split("-")[0]);
		coins.setPrefix(getCoins(p));
		friends.setPrefix(getFriendString(p).split("ó")[0]);
		friends.setSuffix(getFriendString(p).split("ó")[1]);

		Objective ob = (board.getObjective("aaa") != null) ? board.getObjective("aaa")
				: board.registerNewObjective("aaa", "bbb");
		ob.setDisplaySlot(DisplaySlot.SIDEBAR);
		ob.setDisplayName("§6§lMCFLUX.DE");
		ob.getScore("§1").setScore(12);
		ob.getScore("§7  » §6§lServer").setScore(11);
		ob.getScore("§b§5").setScore(10);
		ob.getScore("§2").setScore(9);
		ob.getScore("§7  » §6§lClan").setScore(8);
		ob.getScore("§c§a").setScore(7);
		ob.getScore("§3§l").setScore(6);
		ob.getScore("§7  » §6§lFriends").setScore(5);
		ob.getScore("§k§l").setScore(4);
		ob.getScore("§3").setScore(3);
		ob.getScore("§7  » §6§lCoins").setScore(2);
		ob.getScore("§l§k").setScore(1);
		ob.getScore("§4").setScore(0);
		p.setScoreboard(board);
	}

	public static void updateBoard() {
		try {
			for (Player all : Bukkit.getOnlinePlayers()) {

				all.getScoreboard().getTeam("online").setPrefix("§8» §f" + Bukkit.getOnlinePlayers().size());

			}
		} catch (NullPointerException nullPointerException) {
		}
	}

	public static String format(String color, String rang) {
		if (rang.isEmpty()) {
			return color;
		} else {
			return color + rang + " §8▏ " + color;
		}
	}

	public static PermissionPool getPermissionPool() {
		return CloudAPI.getInstance().getPermissionPool();
	}

	public static String getCoins(Player player) {
		try {
			DecimalFormat df = new DecimalFormat("#.##");
			CoinAPI coinAPI = new CoinAPI();
			double coins = coinAPI.getCoins(player.getUniqueId().toString(), CoinTypes.FREEBUILD);
			if (coins >= 1.0E7D) {
				return "§8» §cZU VIEL";
			}
			return "§8» §f" + df.format(+coinAPI.getCoins(player.getUniqueId().toString(), CoinTypes.FREEBUILD));

		} catch (NullPointerException localNullPointerException) {
		}
		return "0";
	}

	public static String getClanString(Player player) {
		ClanAPI clanAPI = LobbySystem.getClanAPI();

		if (!clanAPI.isInClan(player.getUniqueId().toString())) {
			return "§8» §fKein Clan";
		} else {
			String clanName = clanAPI.getClan(player.getUniqueId().toString());
			List<CloudPlayer> online = clanAPI.getOnlinePlayers(clanName);
			return "§8» §f" + clanAPI.getTag(clanName) + " §8[Ñ§a" + online.size() + "§8/§c"
					+ clanAPI.getClanMembers(clanName).size() + "§8]";
		}
	}

	public static String getFriendString(Player player) {
		List<String> friends = FriendManager.getFriendList(player.getUniqueId().toString());
		List<String> onlineF = FriendManager.getList(player.getUniqueId().toString()).get("online");

		return "§8» §8§a" + onlineF.size() + "§8/ó§c" + friends.size() + "§8";
	}
}
