/*File Main.java - Part of mcDropShop
 * Version 0.1.0
 * Copyright (c) Matt Rienzo, 2016
 * Licensed under GPLv2.  See license in src/doc/LICENSE or src/doc/LICENSE.txt
 */

package io.github.casnix.mcdropshop;

// java/system imports

// bukkit/spigot imports
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

// vault economy
//import net.milkbowl.vault.economy.Economy;

// internal imports
import io.github.casnix.mcdropshop.Versioning;
import io.github.casnix.mcdropshop.CommandLineExe;
import io.github.casnix.mcdropshop.Holograms;
import io.github.casnix.mcdropshop.util.configsys.Shops;

// Server enhancement system imports (for future)
// import io.github.casnix.serveres.ServerES;
// import io.github.casnix.serveres.SesInterface;

/*
 * mcDropShop graphics order:
 * 		NAME
 * 	<	ITEM	>
 * 		PRICE
 * 		Quantity
 * 		[BUY/SELL]
 * 
 */

// Main class, holds our startpoint
public final class Main extends JavaPlugin{
	private String chatPrefix = ChatColor.DARK_RED + "[mcDropShop] ";
	// Needed for interfacing with SES
	private String pluginName;
	
	private final void SetPluginName(String name){
		this.pluginName = name;
	}
	
	// ServerES uses this function for metaData logging
	public Object ourPluginClassName(){
		return (Object) this.pluginName;
	}
	
	// void onEnable(void)
	// -- is called by Bukkit
	// -- We use this as a jumping off point to
	//      set up
	@Override
	public void onEnable(){
		getLogger().info("mcDropShop ["+Versioning.CurrentVersion()+"] Copyright (c) Matt Rienzo, 2016");
		
		// Set up our plugin class name
		this.SetPluginName("io.github.casnix.mcdropshop.Main");
		
		// Now for SES:
		// ServerES sesRef = new ServerES(this);
		
		// Check that HolographicDisplays is enabled.
		if(!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")){
			getLogger().severe("[mcDropShop] HolographicDisplays is not installed or not enabled.");
			getLogger().severe("[mcDropShop] This plugin will be disabled");
			this.setEnabled(false);
			return;
		}
		
		// Command handler (/mcdropshop)
		this.getCommand("mcdropshop").setExecutor(new CommandLineExe(this));
		
		Shops myShops = new Shops(this);
		
		// I kept calling Holograms.refreshShops()
		// -- Couldn't figure out why I kept getting an error lol
		myShops.loadShops();
		
		// Catch proprietary exceptions
		if(myShops.NullListException){
			getLogger().warning("[mcDropShop] onEnable: Caught a NullListException in myShops.loadShops()");
			Bukkit.broadcastMessage(this.chatPrefix + "Internal error on refresh");
		}
	}
	
	// void onDisable(void)
	// -- Let's clean up our mess
	// -- Called by Bukkit when we're disabled.
	@Override
	public void onDisable(){
		getLogger().info("[mcDropShop] Cleaning up");
		
		// Clean up our holograms
		Holograms holo = new Holograms(this);
		
		holo.closeShops();
		
		if(holo.NullListException){
			getLogger().warning("[mcDropShop] onDisable: Caught a NullListException in holo.closeShops()");
		}
	}
	 
}
