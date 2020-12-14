package me.blubo.development.Listener;

import java.util.List;

import de.dytanic.cloudnet.api.CloudAPI;
import me.blubo.development.Strings;
import me.blubo.development.mysql.ClanAPI;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinListener implements Listener {

	private ClanAPI api = new ClanAPI();

	private Strings strings = new Strings(); 
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(PostLoginEvent e) {
		if (!api.playerExists(e.getPlayer().getUniqueId().toString())) {
			api.registerPlayer(CloudAPI.getInstance().getOnlinePlayer(e.getPlayer().getUniqueId()));
		}
		if(!api.getInvites(e.getPlayer().getUniqueId().toString()).isEmpty()) {
			List<String> list = api.getInvites(e.getPlayer().getUniqueId().toString()); 
			e.getPlayer().sendMessage(strings.getPrefix()+"Du hast §eeinladungen §7zu folgenden §eClans §7bekommen:");
			for(String string : list) {
				e.getPlayer().sendMessage(strings.getPrefix()+"§7- "+string.split(":")[1]);
			}
 		}
	}

}
