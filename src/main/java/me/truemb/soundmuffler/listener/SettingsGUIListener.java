package me.truemb.soundmuffler.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.comphenix.protocol.wrappers.EnumWrappers.SoundCategory;

import me.truemb.soundmuffler.gui.SettingsGUI;
import me.truemb.soundmuffler.main.Main;

public class SettingsGUIListener implements Listener {

	private Main instance;
	
	public SettingsGUIListener(Main plugin) {
		this.instance = plugin;
		this.instance.getServer().getPluginManager().registerEvents(this, this.instance);
	}

	@EventHandler
    public void onPlace(BlockPlaceEvent e) {
		
		if(!e.canBuild())
			return;
		
		ItemStack item = e.getItemInHand();
		Block b = e.getBlock();
		
		if(item == null)
			return;
		
		ItemMeta meta = item.getItemMeta();
		
		if(!meta.getPersistentDataContainer().has(this.instance.soundMufflerKey, PersistentDataType.STRING))
			return;

		if(b.getType() != Material.PLAYER_HEAD && b.getType() != Material.PLAYER_WALL_HEAD)
			return;

		if(!(b.getState() instanceof TileState))
			return;

		TileState state = (TileState) b.getState();
		
		int id;
		if(meta.getPersistentDataContainer().has(this.instance.sm_IDKey, PersistentDataType.INTEGER))
			id = meta.getPersistentDataContainer().get(this.instance.sm_IDKey, PersistentDataType.INTEGER);
		else
			id = this.instance.getDataFileManager().findNextId();
		
		this.instance.getDataFileManager().setLocation(id, b.getLocation());

		state.getPersistentDataContainer().set(this.instance.sm_IDKey, PersistentDataType.INTEGER, id);
		state.getPersistentDataContainer().set(this.instance.soundMufflerKey, PersistentDataType.STRING, "true");
		state.update();
		
	}

	@EventHandler
    public void onBreak(BlockBreakEvent e) {
		
		if(e.isCancelled())
			return;
		
		Block b = e.getBlock();

		if(b.getType() != Material.PLAYER_HEAD && b.getType() != Material.PLAYER_WALL_HEAD)
			return;

		if(!(b.getState() instanceof TileState))
			return;

		TileState state = (TileState) b.getState();

		if(!state.getPersistentDataContainer().has(this.instance.soundMufflerKey, PersistentDataType.STRING))
			return;

		int id = state.getPersistentDataContainer().get(this.instance.sm_IDKey, PersistentDataType.INTEGER);
		this.instance.getDataFileManager().setLocation(id, null);
		
		e.setCancelled(true);
		b.setType(Material.AIR);
		b.getDrops().clear();
		
		b.getWorld().dropItem(b.getLocation(), this.instance.getItem(id));
	}

	@EventHandler
    public void onInteract(PlayerInteractEvent e) {
		
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		
		if(e.getHand() != EquipmentSlot.HAND)
			return;
		
		if(e.getClickedBlock() == null)
			return;
		
		Block b = e.getClickedBlock();
		if(b.getType() != Material.PLAYER_HEAD)
			return;
		
		if(!(b.getState() instanceof TileState))
			return;
		
		TileState state = (TileState) b.getState();

		if(!state.getPersistentDataContainer().has(this.instance.soundMufflerKey, PersistentDataType.STRING))
			return;
		
		e.setCancelled(true);
		
		Player p = e.getPlayer();

		int id = state.getPersistentDataContainer().get(this.instance.sm_IDKey, PersistentDataType.INTEGER);
		p.openInventory(SettingsGUI.getGUI(this.instance, id));
	}
	
	@EventHandler
    public void onSettingsChange(InventoryClickEvent e) {
		
        if(e.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', this.instance.manageFile().getString("GUI.Settings.displayName")))) {
          
	        e.setCancelled(true);
	        
	        if(e.getClickedInventory() == null)
	        	return;
	        
	        if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
	        	return;

	        ItemStack item = e.getCurrentItem();
			ItemMeta meta = item.getItemMeta();
			
			if(!meta.getPersistentDataContainer().has(this.instance.guiItemKey, PersistentDataType.STRING))
				return;
			
			if(meta.getPersistentDataContainer().has(this.instance.guiSoundCategory, PersistentDataType.STRING)) {
				int id = meta.getPersistentDataContainer().get(this.instance.sm_IDKey, PersistentDataType.INTEGER);
				SoundCategory soundCategory = SoundCategory.valueOf(meta.getPersistentDataContainer().get(this.instance.guiSoundCategory, PersistentDataType.STRING).toUpperCase());
				this.instance.getDataFileManager().setSoundCategory(id, soundCategory, !this.instance.getDataFileManager().getSoundCategory(id, soundCategory));
				
				//Update Inventory
				e.getView().getTopInventory().setContents(SettingsGUI.getGUI(this.instance, id).getContents());
			}
			
        }
    }
}
