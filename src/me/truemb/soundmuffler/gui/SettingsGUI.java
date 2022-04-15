package me.truemb.soundmuffler.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import me.truemb.soundmuffler.main.Main;

public class SettingsGUI {

	public static Inventory getGUI(Main instance) {
		
		String title = ChatColor.translateAlternateColorCodes('&', instance.manageFile().getString("GUI.Settings.displayName"));
		int size = instance.manageFile().getInt("GUI.Settings.size");

		Inventory inv = Bukkit.createInventory(null, 0, "");
		
		return inv;
	}
}
