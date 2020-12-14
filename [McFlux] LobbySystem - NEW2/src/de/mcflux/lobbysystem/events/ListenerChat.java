package de.mcflux.lobbysystem.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.player.permission.PermissionPool;

public class ListenerChat implements Listener{
	
	public static boolean inGroup(Player player, String group)
	  {
	    PermissionPool pp = CloudAPI.getInstance().getPermissionPool();
	    String checkingGroup = CloudAPI.getInstance().getOnlinePlayer(player.getUniqueId()).getPermissionEntity().getHighestPermissionGroup(pp).getName();
	    return group.equalsIgnoreCase(checkingGroup);
	  }
	  
	  public static String getTeamForPlayer(Player forWhom)
	  {
	    if (inGroup(forWhom, "Owner")) {
	      return "§4§lOwner §8● §4§l" + forWhom.getName() + " §8» §e";
	    }
	    if (inGroup(forWhom, "Admin")) {
	      return "§4§lAdmin §8● §4§l" + forWhom.getName() + " §8» §e";
	    }
	    if (inGroup(forWhom, "srdev")) {
	      return "§b§lSrDeveloper §8● §b§l" + forWhom.getName() + " §8» §e";
	    }
	    if (inGroup(forWhom, "SrModerator")) {
	      return "§c§lSrModerator §8● §c§l" + forWhom.getName() + " §8» §e";
	    }
	    if (inGroup(forWhom, "Developer")) {
	      return "§bDeveloper §8● §b" + forWhom.getName() + " §8» §e";
	    }
	    if (inGroup(forWhom, "Moderator")) {
	      return "§cModerator §8● §c" + forWhom.getName() + " §8» §e";
	    }
	    if (inGroup(forWhom, "jrdev")) {
	      return "§bJrDeveloper §8● §b" + forWhom.getName() + " §8» §e";
	    }
	    if (inGroup(forWhom, "Supporter")) {
	      return "§9Supporter §8● §9" + forWhom.getName() + " §8» §e";
	    }
	    if (inGroup(forWhom, "Azubi")) {
	      return "§3Azubi §8● §3" + forWhom.getName() + " §8» §e";
	    }
	    if (inGroup(forWhom, "Builder")) {
	      return "§aBuilder §8● §a" + forWhom.getName() + " §8» §e";
	    }
	    if (inGroup(forWhom, "YouTuber")) {
	      return "§5YouTuber §8● §5" + forWhom.getName() + " §8» §e";
	    }
	    if (inGroup(forWhom, "pplus")) {
	      return "§ePremium§b+ §8● §e" + forWhom.getName() + " §8» §f";
	    }
	    if (inGroup(forWhom, "Ultra")) {
	      return "§dUltra §8● §d" + forWhom.getName() + " §8» §f";
	    }
	    if (inGroup(forWhom, "Premium")) {
	      return "§6Premium §8● §6" + forWhom.getName() + " §8» §f";
	    }
	    return "§7Spieler §8● §7" + forWhom.getName() + " §8» §f";
	  }
	  
	  @EventHandler
	  public void onChat(AsyncPlayerChatEvent e)
	  {
	    Player player = e.getPlayer();
	    String message = e.getMessage().replace("%", "Prozent");
	    e.setFormat(getTeamForPlayer(player) + message);
	  }

}
