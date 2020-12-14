package de.mcflux.lobbysystem.api;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.player.OfflinePlayer;
import de.dytanic.cloudnet.lib.player.permission.PermissionPool;
import de.mcflux.lobbysystem.manager.ScoreboardManager;

public class RangAPI {
	  public static String getRang(String name)
	  {
	    OfflinePlayer p = CloudAPI.getInstance().getOfflinePlayer(name);
	    return CloudAPI.getInstance().getOnlinePlayer(p.getUniqueId()).getPermissionEntity().getHighestPermissionGroup(ScoreboardManager.getPermissionPool()).getName();
	  }
	  
	  public static void setRang(String name, String rang, String time) {}
	  
	  public static String getColorOfRank(String rang)
	  {
	    if (rang.equalsIgnoreCase("Owner")) {
	      return "§4§l";
	    }
	    if (rang.equalsIgnoreCase("Admin")) {
	      return "§4§l";
	    }
	    if (rang.equalsIgnoreCase("srdev")) {
	      return "§b§l";
	    }
	    if (rang.equalsIgnoreCase("SrModerator")) {
	      return "§c§l";
	    }
	    if (rang.equalsIgnoreCase("Developer")) {
	      return "§b";
	    }
	    if (rang.equalsIgnoreCase("Moderator")) {
	      return "§c";
	    }
	    if (rang.equalsIgnoreCase("jrdev")) {
	      return "§b";
	    }
	    if (rang.equalsIgnoreCase("Supporter")) {
	      return "§9";
	    }
	    if (rang.equalsIgnoreCase("Builder")) {
	      return "§a";
	    }
	    if (rang.equalsIgnoreCase("Azubi")) {
	      return "§3";
	    }
	    if (rang.equalsIgnoreCase("YouTuber")) {
	      return "§5";
	    }
	    if (rang.equalsIgnoreCase("pplus")) {
	      return "§e";
	    }
	    if (rang.equalsIgnoreCase("Ultra")) {
	      return "§d";
	    }
	    if (rang.equalsIgnoreCase("Premium")) {
	      return "§6";
	    }
	    return "§7";
	  }
	  
	  public static boolean inGroup(String name, String rang)
	  {
	    OfflinePlayer player = CloudAPI.getInstance().getOfflinePlayer(name);
	    PermissionPool pp = CloudAPI.getInstance().getPermissionPool();
	    String group = CloudAPI.getInstance().getOnlinePlayer(player.getUniqueId()).getPermissionEntity().getHighestPermissionGroup(pp).getName();
	    return group.equalsIgnoreCase(rang);
	  }
}
