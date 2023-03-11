package me.truemb.soundmuffler.gui;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.comphenix.protocol.wrappers.EnumWrappers.SoundCategory;

import me.truemb.soundmuffler.main.Main;

public class SettingsGUI {

	public static Inventory getGUI(Main instance, int id) {
		
		String title = ChatColor.translateAlternateColorCodes('&', instance.manageFile().getString("GUI.Settings.displayName"));
		int size = 36;

		Inventory inv = Bukkit.createInventory(null, size, title);
		int slot = 10;
		
		for(SoundCategory sounds : SoundCategory.values()) {
			String soundAsString = StringUtils.capitalize(sounds.toString().toLowerCase());
			
    		boolean value = instance.getDataFileManager().getConfig().getBoolean("SoundMufflers." + String.valueOf(id) + ".Categories." + soundAsString);
    		
			String itemPath = "GUI.Settings.items." + (value ? "deny" : "allow") + soundAsString + "Sounds";
			
			if(!instance.manageFile().isSet(itemPath))
				continue;
			
			String name = ChatColor.translateAlternateColorCodes('&', instance.manageFile().getString(itemPath + ".name"));
			boolean enchAnim = instance.manageFile().getBoolean(itemPath + ".enchantmentAnimation");
			List<String> lore = instance.manageFile().getStringList(itemPath + ".lore");

			Material type = Material.valueOf(instance.manageFile().getString(itemPath + ".type"));
			ItemStack item = new ItemStack(type);
			ItemMeta meta = item.getItemMeta();
			
			meta.setDisplayName(name);
			
			if(enchAnim) {
				meta.addEnchant(Enchantment.DURABILITY, 1, false);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			
			if(lore == null || lore.size() <= 0) 
				lore = Collections.emptyList();
			
			for(int i = 0; i < lore.size(); i++)
				lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));

			meta.setLore(lore);
			
			meta.getPersistentDataContainer().set(instance.guiItemKey, PersistentDataType.STRING, "true");
			meta.getPersistentDataContainer().set(instance.sm_IDKey, PersistentDataType.INTEGER, id);
			meta.getPersistentDataContainer().set(instance.guiSoundCategory, PersistentDataType.STRING, sounds.toString());
			
			item.setItemMeta(meta);
			
			inv.setItem(slot, item);
			
			slot++;
			if((slot + 1) % 9 == 0)
				slot += 2;
		}
		
		for(int s = 0; s < inv.getSize(); s++) {
			if(inv.getItem(s) == null || inv.getItem(s).getType() == Material.AIR) {
				String name = ChatColor.translateAlternateColorCodes('&', instance.manageFile().getString("GUI.Settings.items.spaceItem.name"));
				boolean enchAnim = instance.manageFile().getBoolean("GUI.Settings.items.spaceItem.enchantmentAnimation");
				List<String> lore = instance.manageFile().getStringList("GUI.Settings.items.spaceItem.lore");
				
				Material type = Material.valueOf(instance.manageFile().getString("GUI.Settings.items.spaceItem.type"));
				ItemStack item = new ItemStack(type);
				ItemMeta meta = item.getItemMeta();
				
				meta.setDisplayName(name);
				
				if(enchAnim) {
					meta.addEnchant(Enchantment.DURABILITY, 1, false);
					meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				}
				
				if(lore == null || lore.size() <= 0) 
					lore = Collections.emptyList();
				
				for(int i = 0; i < lore.size(); i++)
					lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));

				meta.setLore(lore);
				
				meta.getPersistentDataContainer().set(instance.guiItemKey, PersistentDataType.STRING, "true");
				meta.getPersistentDataContainer().set(instance.sm_IDKey, PersistentDataType.INTEGER, id);
				
				item.setItemMeta(meta);
				
				inv.setItem(s, item);
			}
				
		}
		
		return inv;
	}
}
