package me.truemb.soundmuffler.listener;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.BlockVector;

import com.comphenix.protocol.wrappers.EnumWrappers.SoundCategory;

import me.truemb.soundmuffler.main.Main;

public class SoundMuteListener implements Listener {

	private Main instance;
	
	public SoundMuteListener(Main plugin) {
		this.instance = plugin;
		this.instance.getServer().getPluginManager().registerEvents(this, this.instance);
	}

	@EventHandler
    public void onPlace(BlockPlaceEvent e) {
		
		Player p = e.getPlayer();
		Block b = e.getBlock();
		Location loc = b.getLocation();
		
		BlockVector vec = new BlockVector(loc.getX(), loc.getY(), loc.getZ());
		Sound sound = b.getBlockData().getSoundGroup().getPlaceSound();
		
        ConfigurationSection sec = this.instance.getDataFileManager().getConfig().getConfigurationSection("SoundMufflers");
        if(sec == null)
        	return;
        
        for(String ids : sec.getKeys(false)) {
        	if(!this.instance.getDataFileManager().getConfig().isSet("SoundMufflers." + ids + ".Location"))
        		continue;
        	
        	String worldname = this.instance.getDataFileManager().getConfig().getString("SoundMufflers." + ids + ".Location.World");
        	if(worldname == null || !worldname.equalsIgnoreCase(loc.getWorld().getName()))
        		continue;
        	
        	double headX = this.instance.getDataFileManager().getConfig().getDouble("SoundMufflers." + ids + ".Location.X");
        	double headY = this.instance.getDataFileManager().getConfig().getDouble("SoundMufflers." + ids + ".Location.Y");
        	double headZ = this.instance.getDataFileManager().getConfig().getDouble("SoundMufflers." + ids + ".Location.Z");
        	
        	BlockVector headVec = new BlockVector(headX, headY, headZ);
        	
        	double distance = headVec.distance(vec);
        	
        	if(distance <= this.instance.manageFile().getDouble("Options.distance")) {
        		
        		//HEAD IS IN AFFECTED AREA
        		boolean value = this.instance.getDataFileManager().getConfig().getBoolean("SoundMufflers." + String.valueOf(ids) + ".Categories." + StringUtils.capitalize(SoundCategory.BLOCKS.toString().toLowerCase()));
        		if(value) {
        			// System.out.println("x: " + x + "; y: " + y + "; z: " + z);
        			p.stopSound(sound, org.bukkit.SoundCategory.BLOCKS);
        			return;
        		}
        	}
        	
        }
		
	}

	@EventHandler
    public void onBreak(BlockBreakEvent e) {
		
		Player p = e.getPlayer();
		Block b = e.getBlock();
		Location loc = b.getLocation();
		
		BlockVector vec = new BlockVector(loc.getX(), loc.getY(), loc.getZ());
		Sound sound = b.getBlockData().getSoundGroup().getBreakSound();
		
        ConfigurationSection sec = this.instance.getDataFileManager().getConfig().getConfigurationSection("SoundMufflers");
        if(sec == null)
        	return;
        
        for(String ids : sec.getKeys(false)) {
        	if(!this.instance.getDataFileManager().getConfig().isSet("SoundMufflers." + ids + ".Location"))
        		continue;
        	
        	String worldname = this.instance.getDataFileManager().getConfig().getString("SoundMufflers." + ids + ".Location.World");
        	if(worldname == null || !worldname.equalsIgnoreCase(loc.getWorld().getName()))
        		continue;
        	
        	double headX = this.instance.getDataFileManager().getConfig().getDouble("SoundMufflers." + ids + ".Location.X");
        	double headY = this.instance.getDataFileManager().getConfig().getDouble("SoundMufflers." + ids + ".Location.Y");
        	double headZ = this.instance.getDataFileManager().getConfig().getDouble("SoundMufflers." + ids + ".Location.Z");
        	
        	BlockVector headVec = new BlockVector(headX, headY, headZ);
        	
        	double distance = headVec.distance(vec);
        	
        	if(distance <= this.instance.manageFile().getDouble("Options.distance")) {
        		
        		//HEAD IS IN AFFECTED AREA
        		boolean value = this.instance.getDataFileManager().getConfig().getBoolean("SoundMufflers." + String.valueOf(ids) + ".Categories." + StringUtils.capitalize(SoundCategory.BLOCKS.toString().toLowerCase()));
        		if(value) {
        			// System.out.println("x: " + x + "; y: " + y + "; z: " + z);
        			p.stopSound(sound, org.bukkit.SoundCategory.BLOCKS);
        			return;
        		}
        	}
        	
        }
	}
}
