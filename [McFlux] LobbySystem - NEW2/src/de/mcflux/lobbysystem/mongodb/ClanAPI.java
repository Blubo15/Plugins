package de.mcflux.lobbysystem.mongodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import de.mcflux.lobbysystem.LobbySystem;

@SuppressWarnings({ "unchecked", "deprecation" })
public class ClanAPI {

	private DBCollection coll;
	private DBCollection collPlayers;
	private DBCollection collInvites;

	public ClanAPI() {
		coll = LobbySystem.getMongo().getDB("clans").getCollection("clans");
		collPlayers = LobbySystem.getMongo().getDB("clans").getCollection("players");
		collInvites = LobbySystem.getMongo().getDB("clans").getCollection("invites");
	}

	public void createClan(String clan, CloudPlayer player, String tag) {
		List<String> list = new ArrayList<String>();
		list.add(player.getUniqueId().toString() + ":" + ClanRoles.LEADER);
		collPlayers.update(collPlayers.find(new BasicDBObject("_id", player.getUniqueId().toString())).one(),
				new BasicDBObject("$set", new BasicDBObject("clan", clan.toLowerCase())));
		coll.insert(new BasicDBObject("_id", clan.toLowerCase()).append("players", list).append("tag", tag));
	}

	public void deleteClan(String clan) {
		coll.findAndRemove(new BasicDBObject("_id", clan.toLowerCase()));
		for (String s : getClanMembers(clan.toLowerCase())) {
			removePlayerFromClan(clan, s);
		}
	}

	public void addPlayerToClan(String clan, String player) {
		DBObject db = coll.find(new BasicDBObject("_id", clan.toLowerCase())).one();
		List<String> list = (List<String>) db.get("players");
		list.add(player + ":" + ClanRoles.MEMBER);
		coll.update(db, new BasicDBObject("$set", new BasicDBObject("players", list)));
		DBObject obj2 = collPlayers.find(new BasicDBObject("_id", player)).one();
		coll.update(obj2, new BasicDBObject("$set", new BasicDBObject("clan", clan.toLowerCase())));
	}

	public void removePlayerFromClan(String clan, String player) {
		DBObject db = coll.find(new BasicDBObject("_id", clan.toLowerCase())).one();
		List<String> list = (List<String>) db.get("players");
		list.remove(player + ":");
		coll.update(db, new BasicDBObject("$set", new BasicDBObject("players", list)));
		DBObject obj2 = collPlayers.find(new BasicDBObject("_id", player)).one();
		coll.update(obj2, new BasicDBObject("$set", new BasicDBObject("clan", "")));
	}

	public void promotePlayer(String clan, String player) {
		DBObject obj = coll.find(new BasicDBObject("_id", clan.toLowerCase()), new BasicDBObject("players", 1)).one();
		List<String> list = (List<String>) obj.get("players");
		for (int i = 0; i < list.size(); i++) {
			String s = list.get(i);
			if (s.split(":")[0].equalsIgnoreCase(player)) {
				list.remove(i);
				list.add(player + ":" + ClanRoles.getNextRole(getRole(clan.toLowerCase(), player)));
				coll.update(obj, new BasicDBObject("$set", new BasicDBObject("players", list)));
				return;
			}
		}

	}

	public void demotePlayer(String clan, String player) {
		DBObject obj = coll.find(new BasicDBObject("_id", clan.toLowerCase()), new BasicDBObject("players", 1)).one();
		List<String> list = (List<String>) obj.get("players");
		for (int i = 0; i < list.size(); i++) {
			String s = list.get(i);
			if (s.split(":")[0].equalsIgnoreCase(player)) {
				list.remove(i);
				list.add(player + ":" + ClanRoles.getPreviousRole(getRole(clan.toLowerCase(), player)));
				coll.update(obj, new BasicDBObject("$set", new BasicDBObject("players", list)));
				return;
			}
		}
	}

	public void invitePlayer(String clan, String invited) {
		if (!collInvites.find(new BasicDBObject("_id", invited)).hasNext()) {
			List<String> invites = new ArrayList<String>();
			invites.add(invited + ":" + clan.toLowerCase());
			BasicDBObject obj = new BasicDBObject("_id", invited).append("invites", invites);
			collInvites.insert(obj);
		} else {
			DBObject obj = collInvites.find(new BasicDBObject("_id", invited)).one();
			List<String> invites = (List<String>) obj.get("invites");
			invites.add(invited + ":" + clan.toLowerCase());
			collInvites.update(obj, new BasicDBObject("$set", new BasicDBObject("invites", invites)));
		}
	}

	public void removeInvite(String clan, String invited) {
		DBObject obj = collInvites.find(new BasicDBObject("_id", invited)).one();
		List<String> invites = (List<String>) obj.get("invites");
		invites.remove(invited + ":" + clan.toLowerCase());
		collInvites.update(obj, new BasicDBObject("$set", new BasicDBObject("invites", invites)));
	}

	public boolean isInvited(String clan, String invited) {
		DBObject obj = collInvites.find(new BasicDBObject("_id", invited)).one();
		List<String> invites = (List<String>) obj.get("invites");
		for (int i = 0; i < invites.size(); i++) {
			if (invites.get(i).contains(clan.toLowerCase())) {
				return true;
			}
		}

		return false;
	}

	public void setRole(String player, String clan, ClanRoles role) {
		DBObject obj = coll.find(new BasicDBObject("_id", clan.toLowerCase()), new BasicDBObject("players", 1)).one();
		List<String> players = (List<String>) obj.get("players");
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).equalsIgnoreCase(player + ":" + getRole(clan.toLowerCase(), player))) {
				players.remove(player + ":" + getRole(clan.toLowerCase(), player));
				players.add(player + ":" + role.toString());
			}
		}

		coll.update(obj, new BasicDBObject("$set", new BasicDBObject("players", players)));
	}

	public ClanRoles getRole(String clan, String player) {
		DBObject obj = coll.find(new BasicDBObject("_id", clan.toLowerCase()), new BasicDBObject("players", 1)).one();
		List<String> players = (List<String>) obj.get("players");
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).contains(player)) {
				return ClanRoles.valueOf(players.get(i).split(":")[1]);
			}
		}

		return null;
	}

	public List<String> getClanMembers(String clan) {
		return (List<String>) coll.find(new BasicDBObject("_id", clan.toLowerCase()), new BasicDBObject("players", 1))
				.one().get("players");
	}

	public ArrayList<CloudPlayer> getOnlinePlayers(String clan) {
		ArrayList<CloudPlayer> players = new ArrayList<CloudPlayer>();
		for (String s : getClanMembers(clan)) {
			if (CloudAPI.getInstance().getOnlinePlayer(UUID.fromString(s.split(":")[0])) != null) {
				players.add(CloudAPI.getInstance().getOnlinePlayer(UUID.fromString(s.split(":")[0])));
			}
		}
		return players;
	}

	public String getClan(String uuid) {
		return (String) collPlayers.find(new BasicDBObject("_id", uuid), new BasicDBObject("clan", 1)).one()
				.get("clan");
	}

	public boolean isInClan(String uuid) {
		return !(collPlayers.find(new BasicDBObject("_id", uuid), new BasicDBObject("clan", 1)).one().get("clan")
				.equals(""));
	}

	public String getTag(String clan) {
		return (String) coll.find(new BasicDBObject("_id", clan), new BasicDBObject("tag", 1)).one().get("tag");
	}

	public boolean playerExists(String uuid) {
		return collPlayers.find(new BasicDBObject("_id", uuid)).hasNext();
	}

	public void registerPlayer(CloudPlayer player) {
		BasicDBObject obj = new BasicDBObject("_id", player.getUniqueId().toString()).append("clan", "");
		collPlayers.insert(obj);
	}

	public boolean playerExists(String uuid, String clan) {
		return ((List<String>) coll.find(new BasicDBObject("_id", uuid), new BasicDBObject("players", 1)).one()
				.get("players")).contains(uuid);
	}

	public boolean clanExists(String clan) {
		return coll.find(new BasicDBObject("_id", clan.toLowerCase())).hasNext();
	}

	public boolean hasRole(CloudPlayer player, String clan, ClanRoles role) {
		return getClanMembers(clan).contains(player + ":" + role);
	}

	public List<String> getInvites(String uuid) {
		try {
			return (List<String>) collInvites.find(new BasicDBObject("_id", uuid)).one().get("invites");
		} catch (NullPointerException ex) {
			return new ArrayList<String>();
		}
	}

	public HashMap<ClanRoles, List<String>> getRolesWithPlayers(String clan) {
		List<String> players = getClanMembers(clan.toLowerCase());
		List<String> leader = new ArrayList<String>();
		List<String> coleader = new ArrayList<String>();
		List<String> moderator = new ArrayList<String>();
		List<String> member = new ArrayList<String>();
		for (String s : players) {
			String player = s.split(":")[0];
			String rank = s.split(":")[1];
			if (rank.equals(ClanRoles.LEADER.toString())) {
				leader.add(player);
			} else if (rank.equals(ClanRoles.COLEADER.toString())) {
				coleader.add(player);
			} else if (rank.equals(ClanRoles.MODERATOR.toString())) {
				moderator.add(player);
			} else {
				member.add(player);
			}
		}

		HashMap<ClanRoles, List<String>> output = new HashMap<ClanRoles, List<String>>();
		output.put(ClanRoles.LEADER, leader);
		output.put(ClanRoles.COLEADER, coleader);
		output.put(ClanRoles.MODERATOR, moderator);
		output.put(ClanRoles.MEMBER, member);
		return output;
	}
}
