/*File Main.java - Part of mcDropShop
 * Version 0.0.1-SNAPSHOT
 * Copyright (c) Matt Rienzo, 2016
 * Licensed under GPLv2.  See license in src/doc/LICENSE or src/doc/LICENSE.txt
 */

package io.github.casnix.mcdropshop;

// java/system imports
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

// bukkit/spigot imports
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Server;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;

// vault economy
import net.milkbowl.vault.economy.Economy;

// internal imports
import io.github.casnix.mcdropshop.util.ConfigSys;


// Main class, holds our startpoint
public final class Main extends JavaPlugin{
	
	// void onEnable(void)
	// -- is called by Bukkit
	// -- We use this as a jumping off point to
	//      set up
	@Override
	public void onEnable(){
		
	}
	
	// void onDisable(void)
	// -- Let's clean up our mess
	// -- Called by Bukkit when we're disabled.
	@Override
	public void onDisable(){
		
	}
	
	// 
}
