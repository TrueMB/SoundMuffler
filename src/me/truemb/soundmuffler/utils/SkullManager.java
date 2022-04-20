package me.truemb.soundmuffler.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

public class SkullManager {

	/**
	 * Only works on Minecraft 1.18.2
	 * 
	 * @param displayName
	 * @param minecraftNetHash
	 * @return
	 */
	public static ItemStack getSkullOfTexture(String displayName, String minecraftNetHash) {

		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());

		try {
			profile.getTextures().setSkin(new URL("http://textures.minecraft.net/texture/" + minecraftNetHash));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		meta.setOwnerProfile(profile);

		if (displayName != null)
			meta.setDisplayName(displayName);

		skull.setItemMeta(meta);
		return skull;
	}
}
