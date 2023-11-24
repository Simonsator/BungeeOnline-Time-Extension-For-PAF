package de.simonsator.partyandfriends.extensions.bungeeonlinetime;

import de.simonsator.partyandfriends.api.PAFExtension;
import de.simonsator.partyandfriends.api.events.command.FriendshipCommandEvent;
import de.simonsator.partyandfriends.api.events.command.party.InviteEvent;
import de.simonsator.partyandfriends.extensions.bungeeonlinetime.configuration.BOTConfig;
import de.simonsator.partyandfriends.friends.commands.Friends;
import de.simonsator.partyandfriends.friends.subcommands.Accept;
import de.simonsator.partyandfriends.friends.subcommands.Add;
import de.simonsator.partyandfriends.party.command.PartyCommand;
import de.simonsator.partyandfriends.utilities.ConfigurationCreator;
import lu.r3flexi0n.bungeeonlinetime.BungeeOnlineTimePlugin;
import lu.r3flexi0n.bungeeonlinetime.objects.OnlineTime;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BOTPlugin extends PAFExtension implements Listener {
	private ConfigurationCreator config;
	private BungeeOnlineTimePlugin bungeeOnlineTimePlugin;

	@Override
	public void onEnable() {
		try {
			config = new BOTConfig(new File(getConfigFolder(), "config.yml"), this);
			getAdapter().registerListener(this, this);
			bungeeOnlineTimePlugin = (BungeeOnlineTimePlugin) getProxy().getPluginManager().getPlugin("BungeeOnlineTime");
			registerAsExtension();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ConfigurationCreator getConfig() {
		return config;
	}

	@EventHandler
	public void onPartyInvite(InviteEvent pEvent) {
		try {
			if (getConfig().getLong("Party.Invite.MinOnlineTime") > 0) {
				if (getTime(bungeeOnlineTimePlugin.database.getOnlineTime(pEvent.getExecutor().getName())) < getConfig().getLong("Party.Invite.MinOnlineTime")) {
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
				if (getTime(bungeeOnlineTimePlugin.database.getOnlineTime(pEvent.getExecutor().getName())) < getConfig().getLong("Friends.Add.MinOnlineTime")) {
					pEvent.getExecutor().sendMessage(Friends.getInstance().getPrefix() + getConfig().getString("Friends.Add.NotOnlineLongEnough"));
					pEvent.setCancelled(true);
				}
			}
			if (pEvent.getCaller().getClass().equals(Accept.class) && getConfig().getLong("Friends.Accept.MinOnlineTime") > 0) {
				if (getTime(bungeeOnlineTimePlugin.database.getOnlineTime(pEvent.getExecutor().getName())) < getConfig().getLong("Friends.Accept.MinOnlineTime")) {
					pEvent.getExecutor().sendMessage(Friends.getInstance().getPrefix() + getConfig().getString("Friends.Accept.NotOnlineLongEnough"));
					pEvent.setCancelled(true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private long getTime(List<OnlineTime> onlineTime) {
		long time = 0;
		for (OnlineTime onlineTime1 : onlineTime)
			time += onlineTime1.getTime();
		return time;
	}

}
