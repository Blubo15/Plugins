package me.blubo.development.Commands;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.api.player.PlayerExecutorBridge;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import me.blubo.development.Main;
import me.blubo.development.Strings;
import me.blubo.development.mysql.ClanAPI;
import me.blubo.development.mysql.ClanRoles;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CommandClan extends Command {

	public CommandClan(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	private Strings strings = new Strings();

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender instanceof ProxiedPlayer) {
			ProxiedPlayer ProxiedPlayer = (ProxiedPlayer) sender;
			String uuid = ProxiedPlayer.getUniqueId().toString();
			ClanAPI clanAPI = new ClanAPI();
			if (args.length == 3) {
				if (args[0].equalsIgnoreCase("create")) {
					String clanName = args[1];
					String clanTag = args[2];
					if (!clanAPI.isInClan(uuid)) {
						if (!clanAPI.clanExists(clanName)) {
							if (clanTag.length() <= 5) {
								clanAPI.createClan(clanName, CloudAPI.getInstance().getOnlinePlayer(UUID.fromString(uuid)), clanTag);
								ProxiedPlayer.sendMessage(strings.getPrefix() + "Du hast einen §eClan §7erstellt.");
								ProxiedPlayer.sendMessage(strings.getPrefix() + "Name: §e" + clanName);
								ProxiedPlayer.sendMessage(strings.getPrefix() + "Tag: §e" + clanTag);
							} else {
								ProxiedPlayer.sendMessage(strings.getPrefix() + "Dein §eTag §7ist zu §elang§7!");
							}
						} else {
							ProxiedPlayer.sendMessage(strings.getPrefix() + "Der §eClan §7existiert bereits!");
						}
					} else {
						ProxiedPlayer.sendMessage(strings.getPrefix() + "Du bist bereits in einem §eClan§7!");
					}
				}
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("invite")) {
					if (clanAPI.isInClan(uuid)) {
						String name = args[1];
						CloudPlayer target = CloudAPI.getInstance().getOnlinePlayer(CloudAPI.getInstance().getPlayerUniqueId(name)); 
						String clanName = clanAPI.getClan(uuid);
						if (target != null) {
							if (ProxiedPlayer != target) {
								if (clanAPI.getRole(clanName, uuid).equals(ClanRoles.MODERATOR)
										|| clanAPI.getRole(clanName, uuid).equals(ClanRoles.LEADER)
										|| clanAPI.getRole(clanName, uuid).equals(ClanRoles.COLEADER)) {
									if (!clanAPI.isInvited(clanName, target.getUniqueId().toString())) {
										if (!clanAPI.playerExists(clanName, target.getUniqueId().toString())) {
											clanAPI.invitePlayer(clanName, target.getUniqueId().toString());
											sendMessage(target,
													strings.getPrefix() + "Du hast eine neue §eClan §7einladung!");
											sendMessage(target, strings.getPrefix() + "Name#Tag: §e" + clanName + "§b#§e"
													+ clanAPI.getTag(clanName));
											ProxiedPlayer.sendMessage(strings.getPrefix() + "Du hast den Spieler §e"
													+ target.getName() + " §7in dein §eClan §7eingeladen.");

										} else {
											ProxiedPlayer.sendMessage(strings.getPrefix()
													+ "Der §eSpieler §7ist bereits in einem §eClan§7!");
										}
									} else {
										ProxiedPlayer.sendMessage(strings.getPrefix()
												+ "Der §eSpieler §7hat bereits eine §eAnfrage §7von diesem §eClan§7!");
									}
								} else {
									ProxiedPlayer.sendMessage(strings.getNoPerms());
								}
							} else {
								ProxiedPlayer
										.sendMessage(strings.getPrefix() + "Du kannst dich nicht selber einladen!");
							}
						} else {
							ProxiedPlayer.sendMessage(strings.getPrefix() + "Der §eSpieler §7ist nicht §aonline§7!");
						}
					} else {
						ProxiedPlayer.sendMessage(strings.getPrefix() + "Du bist in keinem Clan!");
					}
				} else if (args[0].equalsIgnoreCase("accept")) {
					String clanName = args[1];
					if (clanAPI.isInvited(clanName, uuid)) {
						clanAPI.removeInvite(clanName, uuid);
						clanAPI.addPlayerToClan(clanName, uuid);
						ProxiedPlayer.sendMessage(
								strings.getPrefix() + "Du bist dem Clan §e" + clanName + " §7beigetreten!");
						sendMessageToAll(clanName,
								strings.getPrefix() + "§e" + ProxiedPlayer.getName() + " §7hat den §eClan §7betreten.");

					} else {
						ProxiedPlayer.sendMessage(
								strings.getPrefix() + "Du hast keine §eEinladung §7zu diesem §eClan §7erhalten!");
					}

				} else if (args[0].equalsIgnoreCase("deny")) {
					String clanName = args[1];
					if (clanAPI.isInvited(clanName, uuid)) {
						clanAPI.removeInvite(uuid, clanName);
						ProxiedPlayer.sendMessage(strings.getPrefix() + "Du hast die §eAnfrage §7vom Clan §e" + clanName
								+ " §7abgelehnt!");
						sendMessageToAll(clanName, strings.getPrefix() + "§e" + ProxiedPlayer.getName()
								+ " §7hat die Einladung abgelehnt!");
					} else {
						ProxiedPlayer.sendMessage("Du hast keine §eAnfrage §7von diesem §eClan§7!");
					}
				} else if (args[0].equalsIgnoreCase("promote")) {
					if (clanAPI.isInClan(uuid)) {
						String clanName = clanAPI.getClan(uuid);
						String target = CloudAPI.getInstance().getPlayerUniqueId(args[1]).toString();
						if (clanAPI.getRole(clanName, uuid).equals(ClanRoles.LEADER)
								|| clanAPI.getRole(clanName, uuid).equals(ClanRoles.COLEADER)) {
							if (!clanAPI.getRole(clanName, target).equals(ClanRoles.COLEADER)
									&& !clanAPI.getRole(clanName, target).equals(ClanRoles.LEADER)) {
								if (clanAPI.playerExists(target, clanName)) {

									sendMessageToAll(clanName,
											strings.getPrefix() + "§e" + CloudAPI.getInstance().getPlayerName(UUID.fromString(target))
													+ " §7ist nun §e"
													+ ClanRoles.getNextRole(clanAPI.getRole(clanName, target)) + "§7!");

									clanAPI.promotePlayer(clanName, target);
								} else {
									ProxiedPlayer.sendMessage(
											strings.getPrefix() + "Der §eSpieler §7ist nicht im §eClan§7!");
								}
							} else {
								ProxiedPlayer.sendMessage(strings.getPrefix()
										+ "Der Spieler ist bereits §eLeader §7bzw. §eCo-Leader §7und kann §enicht §7weiter Aufgestuft werden!");
								ProxiedPlayer.sendMessage(strings.getPrefix()
										+ "Falls ein anderer Spieler den Rang §4Leader §7erhalten soll, \n"
										+ "kann der Clan-Leader §c/clan transferowner <ProxiedPlayer> §7eingeben um dies zu erreichen.");

							}
						} else {
							ProxiedPlayer.sendMessage(strings.getPrefix()
									+ "Du musst mindestens Co-Leader sein um diesen Befehl auszuführen!");
						}
					} else {
						ProxiedPlayer.sendMessage(strings.getPrefix() + "Du bist in keinem §eClan§7!");
					}

				} else if (args[0].equalsIgnoreCase("kick")) {
					if (clanAPI.isInClan(uuid)) {
						String clanName = clanAPI.getClan(uuid);
						if (clanAPI.getRole(clanName, uuid).equals(ClanRoles.MODERATOR)
								|| clanAPI.getRole(clanName, uuid).equals(ClanRoles.COLEADER)
								|| clanAPI.getRole(clanName, uuid).equals(ClanRoles.LEADER)) {
							String name = args[1];
							String otarget = CloudAPI.getInstance().getPlayerUniqueId(name).toString(); 
							if (clanAPI.playerExists(otarget, clanName)) {
								if (isKickable(clanAPI.getRole(clanName, uuid), clanAPI.getRole(clanName, otarget))) {
									clanAPI.removePlayerFromClan(clanName, otarget);
									if (CloudAPI.getInstance().getOnlinePlayer(UUID.fromString(otarget)) != null) {
										CloudPlayer target = CloudAPI.getInstance().getOnlinePlayer(UUID.fromString(otarget)); 
										sendMessage(target, strings.getPrefix() + "Du wurdest aus dem Clan §e" + clanName
												+ " §7geworfen!");
									}
									ProxiedPlayer.sendMessage(strings.getPrefix() + "Du hast den Spieler §e" + name
											+ " §7aus dem §eClan §7geworfen!");

									sendMessageToAll(clanName,
											strings.getPrefix() + "Der Spieler §e"
													+ CloudAPI.getInstance().getPlayerName(UUID.fromString(otarget)) + " §7wurden von §e"
													+ ProxiedPlayer.getName() + " §7aus dem Clan gekickt!");

								} else {
									ProxiedPlayer
											.sendMessage("Du kannst den Spieler aufgrund deines Ranges nicht kicken!");
								}
							} else {
								ProxiedPlayer.sendMessage(
										strings.getPrefix() + "Der §eSpieler §7ist nicht in deinem §eClan§7!");

							}
						} else {
							ProxiedPlayer.sendMessage(strings.getNoPerms());

						}
					} else {
						ProxiedPlayer.sendMessage(strings.getPrefix() + "Du bist in keinem §eClan§7!");
					}

				} else if (args[0].equalsIgnoreCase("transferowner")) {
					if (clanAPI.isInClan(uuid)) {
						String clanName = clanAPI.getClan(uuid);
						String target = CloudAPI.getInstance().getPlayerUniqueId(args[1]).toString();
						if (clanAPI.playerExists(target, clanName)) {
							if (clanAPI.getRole(clanName, uuid).equals(ClanRoles.LEADER)) {
								clanAPI.setRole(target, clanName, ClanRoles.LEADER);
								clanAPI.setRole(uuid, clanName, ClanRoles.COLEADER);

								sendMessageToAll(clanName,
										strings.getPrefix() + "Der neue §4Leader §7des Clans ist nun §e"
												+ CloudAPI.getInstance().getPlayerName(UUID.fromString(target)) + "§7!");

							} else {
								ProxiedPlayer.sendMessage(strings.getNoPerms());
							}
						} else {
							ProxiedPlayer
									.sendMessage(strings.getPrefix() + "Der §eSpieler §7ist nicht in deinem §eClan§7!");
						}
					} else {
						ProxiedPlayer.sendMessage(strings.getPrefix() + "Du bist in keinem §eClan§7!");
					}
				} else if (args[0].equalsIgnoreCase("demote")) {
					if (clanAPI.isInClan(uuid)) {
						String clanName = clanAPI.getClan(uuid);
						String target = CloudAPI.getInstance().getPlayerUniqueId(args[1]).toString(); 
						if (clanAPI.getRole(clanName, uuid).equals(ClanRoles.LEADER)
								|| clanAPI.getRole(clanName, uuid).equals(ClanRoles.COLEADER)) {
							if (!clanAPI.getRole(clanName, target).equals(ClanRoles.MEMBER)) {
								if (clanAPI.playerExists(target, clanName)) {

									sendMessageToAll(clanName, strings.getPrefix() + "§e"
											+ CloudAPI.getInstance().getPlayerName(UUID.fromString(target)) + " §7ist nun §e"
											+ ClanRoles.getPreviousRole(clanAPI.getRole(clanName, target)) + "§7!");

									clanAPI.demotePlayer(clanName, target);
								} else {
									ProxiedPlayer.sendMessage(
											strings.getPrefix() + "Der §eSpieler §7ist nicht im §eClan§7!");
								}
							} else {
								ProxiedPlayer.sendMessage(strings.getPrefix()
										+ "Der Spieler ist bereits §eMember §7ist, kann er §enicht §7weiter Abgestuft werden!");
							}
						} else {
							ProxiedPlayer.sendMessage(strings.getPrefix()
									+ "Du musst mindestens Co-Leader sein um diesen Befehl auszuführen!");
						}
					} else {
						ProxiedPlayer.sendMessage(strings.getPrefix() + "Du bist in keinem §eClan§7!");
					}
				}
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("delete")) {
					if (clanAPI.isInClan(uuid)) {
						String clanName = clanAPI.getClan(uuid);
						if (clanAPI.playerExists(uuid, clanName)) {
							if (clanAPI.getRole(clanName, uuid).equals(ClanRoles.LEADER)) {
								clanAPI.deleteClan(clanName);
								ProxiedPlayer.sendMessage(strings.getPrefix() + "Du hast deinen §eClan §7gelöscht!");
								sendMessageToAll(clanName,
										strings.getPrefix() + "Der §eClan-§4Owner §7hat den Clan aufgelöst!");
							} else {
								ProxiedPlayer.sendMessage(strings.getNoPerms());
							}
						}
					}
				} else if (args[0].equalsIgnoreCase("info")) {
					try {
						if (clanAPI.isInClan(uuid)) {
							String clanName = clanAPI.getClan(uuid);

							HashMap<ClanRoles, List<String>> list = clanAPI.getRolesWithPlayers(clanName);

							String coleaders = "";
							if (list.get(ClanRoles.COLEADER).size() > 0) {
								for (String coleader : list.get(ClanRoles.COLEADER)) {
									coleaders += CloudAPI.getInstance().getPlayerName(UUID.fromString(coleader)) + ", ";
								}
								coleaders = coleaders.substring(0, coleaders.length() - 2);
							}

							String moderators = "";
							if (list.get(ClanRoles.MODERATOR).size() > 0) {
								for (String moderator : list.get(ClanRoles.MODERATOR)) {
									moderators += CloudAPI.getInstance().getPlayerName(UUID.fromString(moderator)) + ", ";
								}
								moderators = moderators.substring(0, moderators.length() - 2);
							}
							String members = "";
							if (list.get(ClanRoles.MEMBER).size() > 0) {
								for (String member : list.get(ClanRoles.MEMBER)) {
									members += CloudAPI.getInstance().getPlayerName(UUID.fromString(member)) + ", ";
								}
								members = members.substring(0, members.length() - 2);
							}

							String onlineMembers = "";
							for (CloudPlayer online : clanAPI.getOnlinePlayers(clanName)) {
								onlineMembers += online.getName() + ", ";
							}
							onlineMembers = onlineMembers.substring(0, onlineMembers.length() - 2);

							ProxiedPlayer.sendMessage(
									strings.getPrefix() + "§8[]§m§l-----§r§8{ §eCLAN-INFO §8}§m§l-----§r§8[]");
							ProxiedPlayer.sendMessage("");
							ProxiedPlayer.sendMessage(strings.getPrefix() + "Name: §e" + clanName);
							ProxiedPlayer.sendMessage(strings.getPrefix() + "Tag: §e#" + clanAPI.getTag(clanName));
							ProxiedPlayer
									.sendMessage(strings.getPrefix() + "Rolle: §e" + clanAPI.getRole(clanName, uuid));
							ProxiedPlayer.sendMessage(strings.getPrefix());
							ProxiedPlayer.sendMessage(strings.getPrefix() + "Mitglieder: ");
							ProxiedPlayer.sendMessage(strings.getPrefix());
							ProxiedPlayer.sendMessage(strings.getPrefix() + "§4Leader:");
							ProxiedPlayer.sendMessage(strings.getPrefix() + ""
									+ CloudAPI.getInstance().getPlayerName(UUID.fromString(list.get(ClanRoles.LEADER).get(0))));
							ProxiedPlayer.sendMessage(strings.getPrefix());
							ProxiedPlayer.sendMessage(strings.getPrefix() + "§cCo-Leaders:");
							ProxiedPlayer.sendMessage(strings.getPrefix() + "" + coleaders);
							ProxiedPlayer.sendMessage(strings.getPrefix());
							ProxiedPlayer.sendMessage(strings.getPrefix() + "§5Moderatoren:");
							ProxiedPlayer.sendMessage(strings.getPrefix() + "" + moderators);
							ProxiedPlayer.sendMessage(strings.getPrefix());
							ProxiedPlayer.sendMessage(strings.getPrefix() + "Members:");
							ProxiedPlayer.sendMessage(strings.getPrefix() + "" + members);
							ProxiedPlayer.sendMessage(strings.getPrefix() + "");
							ProxiedPlayer.sendMessage(strings.getPrefix() + "Im Clan sind Online:");
							ProxiedPlayer.sendMessage(strings.getPrefix() + "" + onlineMembers);
							ProxiedPlayer.sendMessage(strings.getPrefix() + "");
							ProxiedPlayer.sendMessage(
									strings.getPrefix() + "§8[]§m§l-----§r§8{ §eCLAN-INFO §8}§m§l-----§r§8[]");
						} else {
							ProxiedPlayer.sendMessage(strings.getPrefix() + "Du bist in keinem §eClan§7!");
						}
					} catch (IndexOutOfBoundsException | IllegalArgumentException e) {
						e.printStackTrace();
						ProxiedPlayer.sendMessage(strings.getPrefix() + "Du bist in keinem §eClan§7!");
						return;
					}
				}
//					else if (args[0].equalsIgnoreCase("invites")) {
//						if (!clanAPI.getInvites().isEmpty()) {
//							Data.sendMessage(ProxiedPlayer, "§eClan-Einladungen§8:");
//							for (String invites : clanAPI.getInvites()) {
//								Data.sendMessage(ProxiedPlayer, " §8-> §e" + invites);
//							}
//						} else {
//							Data.sendMessage(ProxiedPlayer, "Du hast keine §eClan-Einladungen§7!");
//						}
//					} 
				else if (args[0].equalsIgnoreCase("leave")) {
					if (clanAPI.isInClan(uuid)) {
						String clanName = clanAPI.getClan(uuid);
						if (!clanAPI.getRole(clanName, uuid).equals(ClanRoles.LEADER)) {
							clanAPI.removePlayerFromClan(clanName, uuid);
							ProxiedPlayer.sendMessage(strings.getPrefix() + "Du hast den §eClan §7verlassen!");
						} else {
							ProxiedPlayer.sendMessage(
									strings.getPrefix() + "Du kannst deinen eigenen §eClan §7nicht verlassen§7!");
						}
					}
				}
			} else {
				ProxiedPlayer.sendMessage(strings.getPrefix() + "§8§m§l------------§r §c§lClans §8§m§l------------");
				ProxiedPlayer.sendMessage(strings.getPrefix() + "§e/clan create [NAME] [TAG]");
				ProxiedPlayer.sendMessage(strings.getPrefix() + "§e/clan accept [NAME]");
				ProxiedPlayer.sendMessage(strings.getPrefix() + "§e/clan deny [NAME]");
				ProxiedPlayer.sendMessage(strings.getPrefix() + "§e/clan promote [NAME]");
				ProxiedPlayer.sendMessage(strings.getPrefix() + "§e/clan kick [NAME]");
				ProxiedPlayer.sendMessage(strings.getPrefix() + "§e/clan transferowner [NAME]");
				ProxiedPlayer.sendMessage(strings.getPrefix() + "§e/clan leave");
				ProxiedPlayer.sendMessage(strings.getPrefix() + "§e/clan info");
				ProxiedPlayer.sendMessage(strings.getPrefix() + "§e/clan delete");
				ProxiedPlayer.sendMessage(strings.getPrefix() + "§8§m§l------------§r §c§lClans §8§m§l------------");
			}
		} else {
			sender.sendMessage(strings.getNoPlayer());
		}
		return;
	}

	public boolean isKickable(ClanRoles roleOfKicker, ClanRoles roleOfKicked) {
		if (roleOfKicker.equals(ClanRoles.LEADER)) {
			return true;
		} else if (roleOfKicker.equals(ClanRoles.COLEADER)) {
			if (roleOfKicked.equals(ClanRoles.LEADER)) {
				return false;
			} else {
				return true;
			}
		} else if (roleOfKicker.equals(ClanRoles.MODERATOR)) {
			if (roleOfKicked.equals(ClanRoles.MEMBER)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	public void sendMessageToAll(String clanName, String msg) {
		ClanAPI api = new ClanAPI();
		BungeeCord.getInstance().getScheduler().schedule(Main.getInstance(), new Runnable() {

			@Override
			public void run() {
				for (CloudPlayer ProxiedPlayers : api.getOnlinePlayers(clanName)) {
					new PlayerExecutorBridge().sendMessage(ProxiedPlayers, msg);

				}

			}

		}, 2, TimeUnit.SECONDS);
	}
	
	public void sendMessage(CloudPlayer player, String msg) {
		new PlayerExecutorBridge().sendMessage(player, msg);
	}
}
