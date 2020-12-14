package me.blubo.development.events;

import java.util.HashMap;

import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;

public class InteractEvent implements Listener {

	public static HashMap<Player, Player> awaitingconfirmation = new HashMap<Player, Player>();
	
	int i = 0; 
	@EventHandler
	public void onInteract(PlayerInteractEntityEvent e) {
		if(e.getHand() == EquipmentSlot.OFF_HAND) {
			return; 
		}
		Player player = e.getPlayer();
		if (e.getRightClicked() instanceof Player) { 
			Player target = (Player) e.getRightClicked();
			awaitingconfirmation.put(player, target); 
			IChatBaseComponent ichat = ChatSerializer.a("[\"\",{\"text\":\"§7Du hast eine Einladung von §4§l"
					+ player.getName()
					+ "§7 bekommen. §7Drücke \"}, {\"text\":\"§ahier"
					+ "\", \"clickEvent\":{\"action\":\"run_command\", \"value\":\"/invite accept\"}}, {\"text\":\" §7um die Einladung zu §aakzeptieren§7, oder drücke \"}, {\"text\":\"§chier\", \"clickEvent\":{\"action\":\"run_command\", \"value\":\"/invite deny\"}}, {\"text\":\" §7um sie §cabzulehnen§7.\"}]");
			PacketPlayOutChat paket = new PacketPlayOutChat(ichat);
			((CraftPlayer) target).getHandle().playerConnection.sendPacket(paket);
			 i += 1; 
		}
	}

}
