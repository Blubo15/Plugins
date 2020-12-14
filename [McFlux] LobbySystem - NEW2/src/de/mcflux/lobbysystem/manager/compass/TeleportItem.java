package de.mcflux.lobbysystem.manager.compass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.mcflux.lobbysystem.items.ItemsJoin;

public class TeleportItem {
	
	private boolean enabled;
	private Material material;
	private int amount;
	private short durability;
	private String name;
	private List<String> lores;
	private int slot;
	private String invName;
	private int size;
	
	public TeleportItem(){}
	
	public void load(){
		CompassConfig config = new CompassConfig();
		material = Material.COMPASS;
		
		slot = 5;
		if((slot < 0) || (slot > 8)){
			throw new IllegalArgumentException("The slot length is < 0 or > 8");
		}
		amount = config.getCompassConfig().getInt("Compass.amount");
		if(amount < 1){
			throw new IllegalArgumentException("The Compass item amount is < 1");
		}
		int i = config.getCompassConfig().getInt("Compass.itemdamage");
		if(i > 32767)
		{
			throw new ArrayIndexOutOfBoundsException("The itemdamage is to high!" +i);
		}
		
		durability = (short)i;
		name = ItemsJoin.getItemName("Navigator");
		
		if(this.name.length() > 32){
			this.name = this.name.substring(0, 32);
		}
		
		lores = new ArrayList<String>();
		for(String str : config.getCompassConfig().getStringList("Compass.lores")){
			lores.add(str);
		}
		invName = "§9Compass";
		enabled = config.getCompassConfig().getBoolean("Compass.enabled");
		size = config.getCompassConfig().getInt("Compass.inventorysize");
	}
	
	public void giveToAllPlayers(){
		for(Player player : Bukkit.getOnlinePlayers()){
			this.giveItem(player);
		}
	}
	
	public void giveItem(Player player){
		CompassConfig config = new CompassConfig();
		
		if(!config.getCompassConfig().getBoolean("Compass.enabled")) 
			return;
		player.getInventory().setItem(slot, getTpItem());
	}
	
	public ItemStack getTpItem(){
		if(this.material == null)
			{return null;}
		ItemStack item = new ItemStack(this.material, this.amount);
		item.setDurability(this.durability);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(this.name);
		meta.setLore(this.lores);
		item.setItemMeta(meta);
		return item;
	}
	public void openInventory(Player player){
		if(this.enabled){
			
			Inventory inv = Bukkit.createInventory(player, this.size, this.invName);
			
			for(Map.Entry<Integer, TeleportItems> i : TeleportItems.items.entrySet()){
				inv.setItem(i.getValue().getSlot(), i.getValue().getItem());
			}
			player.openInventory(inv);
		} else {
			player.sendMessage("");
		}
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @return the material
	 */
	public Material getMaterial() {
		return material;
	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @return the durability
	 */
	public short getDurability() {
		return durability;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the lores
	 */
	public List<String> getLores() {
		return lores;
	}

	/**
	 * @return the slot
	 */
	public int getSlot() {
		return slot;
	}

	/**
	 * @return the invName
	 */
	public String getInvName() {
		return invName;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}
}
