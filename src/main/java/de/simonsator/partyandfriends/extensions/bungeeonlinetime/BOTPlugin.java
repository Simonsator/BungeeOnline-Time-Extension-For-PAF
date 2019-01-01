package de.simonsator.partyandfriends.extensions.bungeeonlinetime;

import de.simonsator.partyandfriends.api.PAFExtension;
import de.simonsator.partyandfriends.api.events.command.FriendshipCommandEvent;
import de.simonsator.partyandfriends.api.events.command.party.InviteEvent;
import de.simonsator.partyandfriends.extensions.bungeeonlinetime.configuration.BOTConfig;
import de.simonsator.partyandfriends.friends.commands.Friends;
import de.simonsator.partyandfriends.friends.subcommands.Accept;
import de.simonsator.partyandfriends.friends.subcommands.Add;
import de.simonsator.partyandfriends.party.command.PartyCommand;
import lu.r3flexi0n.bungeeonlinetime.BungeeOnlineTime;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class BOTPlugin extends PAFExtension implements Listener {
	private Configuration config;

	@Override
	public void onEnable() {
		try {
			config = new BOTConfig(new File(getConfigFolder(), "config.yml"), this).getCreatedConfiguration();
			getProxy().getPluginManager().registerListener(this, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Configuration getConfig() {
		return config;
	}

	@EventHandler
	public void onPartyInvite(InviteEvent pEvent) {
		try {
			if (getConfig().getLong("Party.Invite.MinOnlineTime") > 0) {
				if (BungeeOnlineTime.SQL.getOnlineTime(pEvent.getExecutor().getUniqueId()).getTime() < getConfig().getLong("Party.Invite.MinOnlineTime")) {
					pEvent.getExecutor().sendMessage(PartyCommand.getInstance().getPrefix() + getConfig().getString("Party.Invite.NotOnlineLongEnough"));
					pEvent.setCancelled(true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@EventHandler
	public void onFriendCommand(FriendshipCommandEvent pEvent) {
		try {
			if (pEvent.getCaller().getClass().equals(Add.class) && getConfig().getLong("Friends.Add.MinOnlineTime") > 0) {
				if (BungeeOnlineTime.SQL.getOnlineTime(pEvent.getExecutor().getUniqueId()).getTime() < getConfig().getLong("Friends.Add.MinOnlineTime")) {
					pEvent.getExecutor().sendMessage(Friends.getInstance().getPrefix() + getConfig().getString("Friends.Add.NotOnlineLongEnough"));
					pEvent.setCancelled(true);
				}
			}
			if (pEvent.getCaller().getClass().equals(Accept.class) && getConfig().getLong("Friends.Accept.MinOnlineTime") > 0) {
				if (BungeeOnlineTime.SQL.getOnlineTime(pEvent.getExecutor().getUniqueId()).getTime() < getConfig().getLong("Friends.Accept.MinOnlineTime")) {
					pEvent.getExecutor().sendMessage(Friends.getInstance().getPrefix() + getConfig().getString("Friends.Accept.NotOnlineLongEnough"));
					pEvent.setCancelled(true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
