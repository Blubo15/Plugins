package de.mcflux.lobbysystem.api;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.messaging.ChannelNotRegisteredException;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.NetworkUtils;
import de.dytanic.cloudnet.lib.player.OfflinePlayer;
import de.dytanic.cloudnet.lib.player.permission.GroupEntityData;
import de.mcflux.lobbysystem.LobbySystem;
import de.mcflux.lobbysystem.data.Data;

public class PlayerAPI {

	private Player player;
	private String name;
	private String displayName;
	private String rang;

	public PlayerAPI(Player player) {
		this.player = player;
		this.name = player.getName();
		this.displayName = player.getDisplayName();
		this.rang = "";
	}

	public String getName() {
		return this.name;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public String getRang() {
		return this.rang;
	}

	public void teleport(Location arg0) {
		this.player.teleport(arg0);
	}

	public void teleport(Player arg0) {
		this.player.teleport(arg0);
	}

	public void teleport(Location arg0, PlayerTeleportEvent.TeleportCause arg1) {
		this.player.teleport(arg0, arg1);
	}

	public void setDisplayName(String arg0) {
		this.player.setDisplayName(arg0);
	}

	public boolean hasPermission(String arg0) {
		return (this.player.hasPermission(arg0)) || (this.player.hasPermission("lobbysystem.*"));
	}

	public void sendMessage(String arg0) {
		this.player.sendMessage(Data.getPrefix() + arg0);
	}

	public void sendUsage(String arg0) {
		this.player.sendMessage(Data.getPrefix() + "Verwende: �a/" + arg0);
	}

	public void setGamemode(GameMode gameMode) {
		this.player.setGameMode(gameMode);
	}

	public void sendToServer(String arg0) {
		try {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(b);
			try {
				out.writeUTF("Connect");
				out.writeUTF(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.player.sendPluginMessage(LobbySystem.getLobbySystem(), "BungeeCord", b.toByteArray());
		} catch (ChannelNotRegisteredException e) {
			Bukkit.getLogger().warning(
					" ERROR - Usage of bungeecord connect effects is not possible. Your server is not having bungeecord support (Bungeecord channel is not registered in your minecraft server)!");
		}
	}

	public void setFoodLevel(int arg0) {
		this.player.setFoodLevel(arg0);
	}

	@SuppressWarnings("deprecation")
	public void sendTitle(String arg0, String arg1) {
		this.player.sendTitle(arg0, arg1);
	}

	public void setRang(String rang, boolean lifetime, int days) {
		OfflinePlayer offlinePlayer = CloudAPI.getInstance().getOfflinePlayer(this.player.getName());
		if (CloudAPI.getInstance().getPermissionPool().getGroups().containsKey(rang)) {
			offlinePlayer.getPermissionEntity().getGroups().clear();
			String d = String.valueOf(lifetime);
			String dd = String.valueOf(days);
			offlinePlayer.getPermissionEntity().getGroups().add(new GroupEntityData(rang,

					NetworkUtils.checkIsNumber(dd) ? calcDays(Integer.parseInt(dd))
							: d.equalsIgnoreCase("true") ? 0L : 0L));
			CloudAPI.getInstance().updatePlayer(offlinePlayer);
		}
	}

	public Location getLocation() {
		return this.player.getLocation();
	}

	public void addItem(ItemStack arg0) {
		this.player.getInventory().addItem(new ItemStack[] { arg0 });
	}

	public void setItem(int arg0, ItemStack arg1) {
		this.player.getInventory().setItem(arg0, arg1);
	}

	public void clearInventory() {
		this.player.getInventory().clear();
		this.player.getInventory().setArmorContents(null);
	}

	public boolean isOnline() {
		if (this.player != null) {
			return true;
		}
		return false;
	}

	public Player getPlayer() {
		return this.player;
	}

	private long calcDays(int value) {
		return System.currentTimeMillis() + TimeUnit.DAYS.toMillis(value);
	}

}
