package de.mcflux.lobbysystem.mongodb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import de.mcflux.lobbysystem.LobbySystem;

@SuppressWarnings("unchecked")
public class FriendManager {

	@SuppressWarnings("deprecation")
	private static DBCollection collection = LobbySystem.getMongo().getDB("friends").getCollection("friends");

	public static void registerPlayer(CloudPlayer player) {
		if (!existPlayer(player.getUniqueId().toString())) {
			List<String> flist = new ArrayList<String>();
			List<String> frequests = new ArrayList<String>();
			DBObject user = new BasicDBObject("_id", player.getUniqueId().toString()).append("name", player.getName())
					.append("flist", flist).append("frequests", frequests).append("frequest", true)
					.append("fjump", true).append("fonline", true).append("pinvite", 0).append("fserver", "Lobby-1");
			collection.insert(user);
		} else {
			collection.update(new BasicDBObject("_id", player.getUniqueId().toString()),
					new BasicDBObject("$set", new BasicDBObject("name", player.getName())));
		}
	}

	/* FREUNDE */
	public static List<String> getFriendList(String uuid) {
		List<String> list = (List<String>) get(uuid, "flist");
		if (list != null) {
			return list;
		}
		List<String> a = new ArrayList<String>();
		return a;
	}

	public static List<String> getRequestList(String uuid) {
		List<String> list = (List<String>) get(uuid, "frequests");
		if (list != null) {
			return list;
		}
		List<String> a = new ArrayList<String>();
		return a;
	}

	public static void addFriend(String uuid, String frienduuid) {
		DBCursor cursor = collection.find(new BasicDBObject("_id", uuid), new BasicDBObject("flist", 1));
		List<String> list = (List<String>) cursor.one().get("flist");
		list.add(frienduuid);
		collection.update(new BasicDBObject("_id", uuid),
				new BasicDBObject("$set", new BasicDBObject("flist", list)));
	}

	public static void addRequest(String uuid, String frienduuid) {
		DBCursor cursor = collection.find(new BasicDBObject("_id", uuid), new BasicDBObject("frequests", 1));
		List<String> list = (List<String>) cursor.one().get("frequests");
		list.add(frienduuid);
		collection.update(new BasicDBObject("_id", uuid),
				new BasicDBObject("$set", new BasicDBObject("frequests", list)));

	}

	public static void removeFriend(String uuid, String frienduuid) {
		DBCursor cursor = collection.find(new BasicDBObject("_id", uuid), new BasicDBObject("flist", 1));
		List<String> list = (List<String>) cursor.one().get("flist");
		list.remove(frienduuid);
		collection.update(new BasicDBObject("_id", uuid),
				new BasicDBObject("$set", new BasicDBObject("flist", list)));
	}

	public static void removeRequest(String uuid, String frienduuid) {
		DBCursor cursor = collection.find(new BasicDBObject("_id", uuid), new BasicDBObject("frequests", 1));
		List<String> list = (List<String>) cursor.one().get("frequests");
		list.remove(frienduuid);
		collection.update(new BasicDBObject("_id", uuid),
				new BasicDBObject("$set", new BasicDBObject("frequests", list)));
	}

	public static HashMap<String, List<String>> getList(String uuid) {
		List<String> friendList = getFriendList(uuid);
		List<String> offline = new ArrayList<String>();
		List<String> online = new ArrayList<String>();
		for (String user : friendList) {
			if (CloudAPI.getInstance().getOnlinePlayer(UUID.fromString(user)) != null) {
				online.add(user);
			} else {
				offline.add(user);
			}
		}

		Collections.sort(online);
		Collections.sort(offline);

		HashMap<String, List<String>> hash = new HashMap<String, List<String>>();
		hash.put("online", online);
		hash.put("offline", offline);

		return hash;
	}

	public static boolean getSettings(String uuid, String type) {
		return (boolean) get(uuid, type);
	}

	public static void setSetting(String uuid, String type, String typeValue) {
		collection.update(new BasicDBObject("_id", uuid),
				new BasicDBObject("$set", new BasicDBObject(type, typeValue)));
	}

	public static void setServer(String uuid, String server) {
		collection.update(new BasicDBObject("_id", uuid),
				new BasicDBObject("$set", new BasicDBObject("fserver", server)));
	}

	public static String getNameByUUID(String playeruuid) {
		DBCursor cursor = collection.find(new BasicDBObject("_id", playeruuid), new BasicDBObject("name", 1));
		return (String) cursor.one().get("name");
	}

	public static boolean existPlayer(String uuid) {
		DBCursor cursor = collection.find(new BasicDBObject("_id", uuid));
		if (cursor.hasNext()) {
			return true;
		}

		return false;
	}

	public static Object get(String uuid, String what) {
		return collection.find(new BasicDBObject("_id", uuid), new BasicDBObject(what, 1)).one().get(what);
	}

}
