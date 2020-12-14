package de.mcflux.lobbysystem.manager;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import de.dytanic.cloudnet.lib.player.CloudPlayer;
import de.mcflux.lobbysystem.LobbySystem;

public class CoinAPI {

	public CoinAPI() {

	}
	
	@SuppressWarnings("deprecation")
	private DBCollection coll = LobbySystem.getMongo().getDB("lobby").getCollection("lobby"); 

	public void register(CloudPlayer player) {
		BasicDBObject obj = new BasicDBObject("_id", player.getUniqueId().toString()).append("name", player.getName())
				.append(CoinTypes.CITYBUILD.toString(), Float.valueOf("0"))
				.append(CoinTypes.FREEBUILD.toString(), Float.valueOf("0"))
				.append(CoinTypes.LOBBY.toString(), Float.valueOf("0"));
		coll.insert(obj); 
		
	}

	public boolean isRegistered(String uuid) {
		return coll.find(new BasicDBObject("_id", uuid)).hasNext(); 
	}

	public void addCoins(String uuid, float coins, CoinTypes coinType) {
		coll.update(new BasicDBObject("_id", uuid), new BasicDBObject("$set", new BasicDBObject(coinType.toString(), getCoins(uuid, coinType)+coins))); 
	}

	public void removeCoins(String uuid, float coins, CoinTypes coinType) {
		coll.update(new BasicDBObject("_id", uuid), new BasicDBObject("$set", new BasicDBObject(coinType.toString(), getCoins(uuid, coinType)-coins))); 
	}

	public void setCoins(String uuid, float coins, CoinTypes coinType) {
		coll.update(new BasicDBObject("_id", uuid), new BasicDBObject("$set", new BasicDBObject(coinType.toString(), coins))); 
	}

	public Float getCoins(String uuid, CoinTypes coinType) {
		return (Float) coll.find(new BasicDBObject("_id", uuid), new BasicDBObject(coinType.toString(), 1)).one().get(coinType.toString()); 
	}
}
