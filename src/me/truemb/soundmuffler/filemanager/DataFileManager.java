package me.truemb.soundmuffler.filemanager;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import com.comphenix.protocol.wrappers.EnumWrappers.SoundCategory;

import me.truemb.soundmuffler.main.Main;

public class DataFileManager {
	
	private File file;
	private YamlConfiguration config;
	
	private int startId = -1;
	
	public DataFileManager(Main plugin) {
		this.file = new File(plugin.getDataFolder(), "data.yml");
		this.config = YamlConfiguration.loadConfiguration(this.file);
		
		this.startId = this.findNextId();
	}
	
	public int findNextId() {
		int i = this.startId != -1 ? this.startId : 0;
		while(this.config.isSet("SoundMufflers." + String.valueOf(i))) {
			i++;
		}
		return i;
	}

	public void setSoundCategory(int id, SoundCategory type, boolean value) {
		
		this.config.set("SoundMufflers." + String.valueOf(id) + ".Categories." + StringUtils.capitalize(type.toString().toLowerCase()), value);
		
		try {
			this.config.save(this.file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean getSoundCategory(int id, SoundCategory type) {
		return this.config.getBoolean("SoundMufflers." + String.valueOf(id) + ".Categories." + StringUtils.capitalize(type.toString().toLowerCase()));
	}
	
	public void setLocation(int id, Location loc) {
		
		if(loc != null) {
			this.config.set("SoundMufflers." + String.valueOf(id) + ".Location.X", loc.getX());
			this.config.set("SoundMufflers." + String.valueOf(id) + ".Location.Y", loc.getY());
			this.config.set("SoundMufflers." + String.valueOf(id) + ".Location.Z", loc.getZ());
			this.config.set("SoundMufflers." + String.valueOf(id) + ".Location.World", loc.getWorld().getName());
		}else
			this.config.set("SoundMufflers." + String.valueOf(id) + ".Location", null);
		
		try {
			this.config.save(this.file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public YamlConfiguration getConfig() {
		return this.config;
	}

}
