package me.truemb.soundmuffler.commands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.truemb.soundmuffler.main.Main;

public class SoundMufflerCOMMAND implements CommandExecutor, TabCompleter{

	private Main instance;
	
	public SoundMufflerCOMMAND(Main plugin) {
		this.instance = plugin;
		this.instance.getCommand("soundmuffler").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(this.instance.getMessage("console"));
			return true;
		}
		
		Player p = (Player) sender;
		
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return new ArrayList<>();
	}
	
}
