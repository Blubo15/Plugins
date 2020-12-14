package de.mcflux.lobbysystem.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemAPI {
	
	private ItemStack item;
	  
	  private List<String> lore = new ArrayList<>();
	  
	  private ItemMeta meta;
	  
	  public ItemAPI(Material mat, short subid, int amount) {
	    this.item = new ItemStack(mat, amount, subid);
	    this.meta = this.item.getItemMeta();
	  }
	  
	  public ItemAPI(ItemStack item) {
	    this.item = item;
	    this.meta = item.getItemMeta();
	  }
	  
	  public ItemAPI(Material mat, short subid) {
	    this.item = new ItemStack(mat, 1, subid);
	    this.meta = this.item.getItemMeta();
	  }
	  
	  public ItemAPI(Material mat, int amount) {
	    this.item = new ItemStack(mat, amount, (short)0);
	    this.meta = this.item.getItemMeta();
	  }
	  
	  public ItemAPI(Material mat) {
	    this.item = new ItemStack(mat, 1, (short)0);
	    this.meta = this.item.getItemMeta();
	  }
	  
	  public ItemAPI setAmount(int value) {
	    this.item.setAmount(value);
	    return this;
	  }
	  
	  public ItemAPI setNoName() {
	    this.meta.setDisplayName(" ");
	    return this;
	  }
	  
	  public ItemAPI setGlow() {
	    this.meta.addEnchant(Enchantment.DURABILITY, 1, true);
	    this.meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
	    return this;
	  }
	  
	  public ItemAPI setData(short data) {
	    this.item.setDurability(data);
	    return this;
	  }
	  
	  public ItemAPI addLoreLine(String line) {
	    this.lore.add(line);
	    return this;
	  }
	  
	  public ItemAPI addLoreArray(String[] lines) {
	    for (int x = 0; x < lines.length; x++)
	      this.lore.add(lines[x]); 
	    return this;
	  }
	  
	  public ItemAPI addLoreAll(List<String> lines) {
	    this.lore.addAll(lines);
	    return this;
	  }
	  
	  public ItemAPI setDisplayName(String name) {
	    this.meta.setDisplayName(name);
	    return this;
	  }
	  
	  public ItemAPI setSkullOwner(String owner) {
	    ((SkullMeta)this.meta).setOwner(owner);
	    return this;
	  }
	  
	  public ItemAPI setColor(Color c) {
	    ((LeatherArmorMeta)this.meta).setColor(c);
	    return this;
	  }
	  
	  public ItemAPI setBannerColor(DyeColor c) {
	    ((BannerMeta)this.meta).setBaseColor(c);
	    return this;
	  }
	  
	  public ItemAPI setUnbreakable(boolean value) {
	    this.meta.spigot().setUnbreakable(value);
	    return this;
	  }
	  
	  public ItemAPI addEnchantment(Enchantment ench, int lvl) {
	    this.meta.addEnchant(ench, lvl, true);
	    return this;
	  }
	  
	  public ItemAPI addItemFlag(ItemFlag flag) {
	    this.meta.addItemFlags(new ItemFlag[] { flag });
	    return this;
	  }
	  
	  public ItemAPI addLeatherColor(Color color) {
	    ((LeatherArmorMeta)this.meta).setColor(color);
	    return this;
	  }
	  
	  public ItemStack build() {
	    if (!this.lore.isEmpty())
	      this.meta.setLore(this.lore); 
	    this.item.setItemMeta(this.meta);
	    return this.item;
	  }

}
