package de.mcflux.lobbysystem.manager.compass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.mcflux.lobbysystem.data.Data;

public class TeleportItems {
	
	public static HashMap<Integer, TeleportItems> items = new HashMap<>();
	private int itemNumber;
	private int slot;
	private ItemStack item;
	private Location tpLoc;
	private boolean teleportation;
	private String message;
	
	@SuppressWarnings("deprecation")
	public TeleportItems(int number, String item, int itemdamage, int amount, String displayname, List<String> lores, int slot, boolean teleport, String world, double x, double y, double z, float yaw, float pitch,String message){
		if (amount < 0) {
		      throw new IllegalArgumentException("Amount of the Item (" + itemNumber + ") is < 0");
		    }
		    if (slot < 0) {
		      throw new IllegalArgumentException("Slot of Item (" + itemNumber + ") is < 0");
		    }
		    Material m;
		    try{
		    	m = Material.getMaterial(Integer.valueOf(item));
		    }catch(NumberFormatException ex){
		    	m  = Material.getMaterial(item);
		    }
		    if (m == null) {
		      throw new IllegalArgumentException("The Material of the Item (" + itemNumber + ") was not found! " + item);
		    }
		    if (itemdamage > 32767) {
		      throw new IllegalArgumentException("ItemDamage of the Item (" + itemNumber + ") is to high!");
		    }
		    if (Bukkit.getWorld(world) == null) {
		      throw new IllegalArgumentException("World of Item (" + itemNumber + ") not loaded!");
		    }
		    
		    this.itemNumber = number;
		    this.slot = slot;
		    this.tpLoc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
		    this.teleportation = teleport;
		    this.message = ChatColor.translateAlternateColorCodes('&', message.replace("%prefix%", Data.getPrefix()));
		    this.item = new ItemStack(m);
		    this.item.setAmount(amount);
		    ItemMeta meta = this.item.getItemMeta();
		    try {
		    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayname));
		    }catch(NullPointerException ex) {
		    	meta.setDisplayName(displayname);
		    }
		    List<String> nlore = new ArrayList<String>();
		    for(String str : lores){
		    	nlore.add(ChatColor.translateAlternateColorCodes('&', str));
		    }
		    meta.setLore(nlore);
		    this.item.setItemMeta(meta);
		    this.item.setDurability((short)itemdamage);
		    items.put(Integer.valueOf(slot), this);
	}
	
	public static void load(){
		try {
		CompassConfig config = new CompassConfig();
		
		String str = "Items.";
		for(String string : config.getCompassConfig().getConfigurationSection("Items").getKeys(false)){
			new TeleportItems(Integer.parseInt(string), config.getCompassConfig().getString(str+string+".item").toUpperCase()
					, config.getCompassConfig().getInt(str+string+".itemdamage"), config.getCompassConfig().getInt(str+string+".amount"),
					config.getCompassConfig().getString(str+string+".displayname"), config.getCompassConfig().getStringList(str+string+".lores"),
					config.getCompassConfig().getInt(str+string+".slot")-1, config.getCompassConfig().getBoolean(str+string+".teleport"), 
					config.getCompassConfig().getString(str+string+".world"),
					config.getCompassConfig().getDouble(str+string+".x"),
					config.getCompassConfig().getDouble(str+string+".y"),
					config.getCompassConfig().getDouble(str+string+".z"),
					(float)config.getCompassConfig().getDouble(str+string+".yaw"),
					(float)config.getCompassConfig().getDouble(str+string+".pitch"), 
					config.getCompassConfig().getString(str+string+".message"));
		}
		}catch(NullPointerException ex) {
			
		}
	}
	
	public static void createNewWarp(String item, String displayname, int slot, String world, double x, double y, double z, float yaw, float pitch) {
		int warps = slot; 
		CompassConfig config = new CompassConfig(); 

		String str = "Items."; 
		config.getCompassConfig().set(str+warps+".item", item);
		config.getCompassConfig().set(str+warps+".itemdamage", 0);
		config.getCompassConfig().set(str+warps+".amount", 1);
		config.getCompassConfig().set(str+warps+".displayname", displayname);
		config.getCompassConfig().set(str+warps+".lores", "");
		config.getCompassConfig().set(str+warps+".slot", slot+1);
		config.getCompassConfig().set(str+warps+".teleport", true);
		config.getCompassConfig().set(str+warps+".world", world);
		config.getCompassConfig().set(str+warps+".x", x);
		config.getCompassConfig().set(str+warps+".y", y);
		config.getCompassConfig().set(str+warps+".z", z);
		config.getCompassConfig().set(str+warps+".yaw", yaw);
		config.getCompassConfig().set(str+warps+".pitch", pitch);
		config.getCompassConfig().set(str+warps+".message", "");
		
		try {
			config.getCompassConfig().save(config.getFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		load(); 
		
	}
	
	public void performTeleport(Player player){
		if(this.teleportation){
			player.teleport(this.tpLoc);
		}
		player.sendMessage(this.message);
	}

	/**
	 * @return the itemNumber
	 */
	public int getItemNumber() {
		return itemNumber;
	}

	/**
	 * @return the slot
	 */
	public int getSlot() {
		return slot;
	}

	/**
	 * @return the item
	 */
	public ItemStack getItem() {
		return item;
	}

	/**
	 * @return the tpLoc
	 */
	public Location getTpLoc() {
		return tpLoc;
	}

	/**
	 * @return the teleportation
	 */
	public boolean isTeleportation() {
		return teleportation;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
}
