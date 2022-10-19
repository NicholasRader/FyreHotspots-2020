package us.fyrepvp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class FyreCore extends JavaPlugin {

	public static Economy econ = null;
	
	Hotspots hotspots;

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	@Override
	public void onEnable() {
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "FyreCore has been enabled!");
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

		if (!setupEconomy()) {
			getLogger().severe(
					String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		hotspots = new Hotspots(this);
		Bukkit.getServer().getPluginManager().registerEvents(hotspots, this);
		hotspots.runTaskTimer(this, 0L, 20L);
	}

	@Override
	public void onDisable() {
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "FyreCore has been disabled!");
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
	}
}