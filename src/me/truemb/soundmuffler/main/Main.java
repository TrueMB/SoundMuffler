package me.truemb.soundmuffler.main;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.SoundCategory;
import com.jeff_media.updatechecker.UpdateCheckSource;
import com.jeff_media.updatechecker.UpdateChecker;
import com.jeff_media.updatechecker.UserAgentBuilder;
import me.truemb.soundmuffler.commands.SoundMufflerCOMMAND;
import me.truemb.soundmuffler.utils.ConfigUpdater;
import me.truemb.soundmuffler.utils.UTF8YamlConfiguration;

public class Main extends JavaPlugin {

	private UTF8YamlConfiguration config;

	private ProtocolManager protocolManager;

	// NAMESPACES
	public NamespacedKey guiItem = new NamespacedKey(this, "guiItem");

	private static final int configVersion = 1;
	private static final String SPIGOT_RESOURCE_ID = ""; // TODO
	private static final int BSTATS_PLUGIN_ID = 14928;
	
	//TODO EDIT SOUND VOLUME?
	
	@Override
	public void onEnable() {
		this.manageFile();

		// COMMANDS
		new SoundMufflerCOMMAND(this);

		// METRICS ANALYTICS
		if (this.manageFile().getBoolean("Options.useMetrics"))
			new Metrics(this, BSTATS_PLUGIN_ID);

		// UPDATE CHECKER
		//TODO this.checkForUpdate();

		ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
		protocolManager.addPacketListener(
			new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.NAMED_SOUND_EFFECT) {
				
				@Override
				public void onPacketSending(PacketEvent e) {
					
					if (e.getPacketType() == PacketType.Play.Server.NAMED_SOUND_EFFECT) {
						e.setCancelled(true);
						
			            PacketContainer packet = e.getPacket();
			            
			            SoundCategory soundCategory = packet.getSoundCategories().read(0);
			            Sound sound = packet.getSoundEffects().read(0);
			            
			            float volume = packet.getFloat().read(0);
			            float pitch = packet.getFloat().read(1);
			            
			            //packet.getSoundCategories().getValues().forEach(soundCat -> System.out.println("SOUND CATEGORY: " + soundCat));
			            //packet.getSoundEffects().getValues().forEach(soundEff -> System.out.println("SOUND EFFECT: " + soundEff));
					}
				}
				
			});
		
		this.protocolManager = protocolManager;
		
	}

	@Override
	public void onDisable() {

	}

	// CONFIG
	public String getMessage(String path) {
		String s = this.manageFile().getString("Messages.prefix") + " "
				+ this.manageFile().getString("Messages." + path);
		return ChatColor.translateAlternateColorCodes('&', this.translateHexColorCodes(s));
	}

	public String translateHexColorCodes(String message) {

		final Pattern hexPattern = Pattern.compile("#[a-fA-F0-9]{6}");
		Matcher matcher = hexPattern.matcher(message);
		while (matcher.find()) {
			String color = message.substring(matcher.start(), matcher.end());
			message = message.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
			matcher = hexPattern.matcher(message);
		}
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public YamlConfiguration manageFile() {
		File configFile = this.getConfigFile();
		if (!configFile.exists())
			saveResource("config.yml", true);

		if (this.config == null) {

			// TO GET THE CONFIG VERSION
			this.config = new UTF8YamlConfiguration(configFile);

			// UPDATE
			if (!this.config.isSet("ConfigVersion") || this.config.getInt("ConfigVersion") < configVersion) {
				this.getLogger().info("Updating Config!");
				try {

					ConfigUpdater.update(this, "config.yml", configFile, Arrays.asList("GUI")); // TODO
					this.reloadConfig();
					this.config = new UTF8YamlConfiguration(configFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		return this.config;
	}

	private File getConfigFile() {
		return new File(this.getDataFolder().getPath(), "config.yml");
	}

	// CHECK FOR UPDATE
	// https://www.spigotmc.org/threads/powerful-update-checker-with-only-one-line-of-code.500010/
	private void checkForUpdate() {
		
		new UpdateChecker(this, UpdateCheckSource.SPIGET, SPIGOT_RESOURCE_ID)
                .setDownloadLink(SPIGOT_RESOURCE_ID) // You can either use a custom URL or the Spigot Resource ID
                .setDonationLink("https://www.paypal.me/truemb")
                .setChangelogLink(SPIGOT_RESOURCE_ID) // Same as for the Download link: URL or Spigot Resource ID
                .setNotifyOpsOnJoin(true) // Notify OPs on Join when a new version is found (default)
                .setNotifyByPermissionOnJoin(this.getDescription().getName() + ".updatechecker") // Also notify people on join with this permission
                .setUserAgent(new UserAgentBuilder().addPluginNameAndVersion())
                .checkEveryXHours(12) // Check every hours
                .checkNow(); // And check right now
        
	}

	public ProtocolManager getProtocolManager() {
		return this.protocolManager;
	}

}
