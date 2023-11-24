package de.simonsator.partyandfriends.extensions.bungeeonlinetime.configuration;

import de.simonsator.partyandfriends.api.PAFExtension;
import de.simonsator.partyandfriends.utilities.ConfigurationCreator;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class BOTConfig extends ConfigurationCreator {
	public BOTConfig(File file, PAFExtension pPlugin) throws IOException {
		super(file, pPlugin, true);
		readFile();
		loadDefaults();
		saveFile();
		process(configuration);
	}

	private void loadDefaults() {
		set("Friends.Add.MinOnlineTime", 60);
		set("Friends.Add.NotOnlineLongEnough", " &7You need to have been at least 1 minute online to use this command.");
		set("Friends.Accept.MinOnlineTime", 60);
		set("Friends.Accept.NotOnlineLongEnough", " &7You need to have been at least 1 minute online to use this command.");
		set("Party.Invite.MinOnlineTime", 60);
		set("Party.Invite.NotOnlineLongEnough", " &7You need to have been at least 1 minute online to use this command.");
	}
}
