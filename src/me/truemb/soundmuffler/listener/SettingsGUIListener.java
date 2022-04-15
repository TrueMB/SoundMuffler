package me.truemb.soundmuffler.listener;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import me.truemb.soundmuffler.main.Main;

public class SettingsGUIListener implements Listener {

	private Main instance;
	
	public SettingsGUIListener(Main plugin) {
		this.instance = plugin;
		this.instance.getServer().getPluginManager().registerEvents(this, this.instance);
	}

	@EventHandler
    public void onSettingsChange(InventoryClickEvent e) {
        
        Player p = (Player) e.getWhoClicked();
        UUID uuid = p.getUniqueId();
        
        if(e.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', this.instance.manageFile().getString("GUI.shopConfirmation.displayName")))) {
          
	        e.setCancelled(true);
	        
	        if(e.getClickedInventory() == null)
	        	return;
	        
	        if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
	        	return;

	        ItemStack item = e.getCurrentItem();
			ItemMeta meta = item.getItemMeta();
			
			if(!meta.getPersistentDataContainer().has(this.instance.guiItem, PersistentDataType.STRING) || meta.getPersistentDataContainer().get(this.instance.guiItem, PersistentDataType.STRING).equalsIgnoreCase("false"))
				return;
			
			
        }
    }
}
