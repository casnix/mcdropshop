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
import org.bukkit.plugin.RegisteredServiceProvider;

// vault economy
//import net.milkbowl.vault.economy.Economy;

// internal imports
import io.github.casnix.mcdropshop.Versioning;
import io.github.casnix.mcdropshop.CommandLineExe;
import io.github.casnix.mcdropshop.Holograms;
import io.github.casnix.mcdropshop.util.ConfigSys;
import io.github.casnix.mcdropshop.util.configsys.Shops;

// java imports
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

//Vault imports
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;

/*
 * mcDropShop graphics order:
 * 		NAME (index)
 * 	<	ITEM	>
 * 		PRICE
 * 		Quantity
 * 		[BUY/SELL]
 * 
 */

// Main class, holds our startpoint
public final class Main extends JavaPlugin{
	private String chatPrefix = ChatColor.DARK_RED + "[mcDropShop] ";
	
	public final static String sV = "p0";
	
	// void onEnable(void)
	// -- is called by Bukkit
	// -- We use this as a jumping off point to
	//      set up
	@Override
	public void onEnable(){
		getLogger().info("mcDropShop ["+Versioning.CurrentVersion()+"] Copyright (c) Matt Rienzo, 2016");
		
		// Check that HolographicDisplays is enabled.
		if(!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")){
			getLogger().severe("[mcDropShop] HolographicDisplays is not installed or not enabled.");
			getLogger().severe("[mcDropShop] This plugin will be disabled");
			this.setEnabled(false);
			return;
		}
		
		if(!Bukkit.getPluginManager().isPluginEnabled("Vault")){
			getLogger().severe("[mcDropShop] Vault is not installed or not enabled.");
			getLogger().severe("[mcDropShop] This plugin will be disabled");
			this.setEnabled(false);
			return;
		}
		
		// Vault stuff
		this.setupPermissions();
		this.setupChat();
		this.setupEconomy();
		
		boolean isSetupDone = this.checkSetup();
		
		if(isSetupDone == false){
			getLogger().info("[mcDropShop] This version of mcDropShop isn't set up! Doing so now...");
			
			getLogger().info("Building Shops.json...");
			String currentFile = new String();
		    try{
		    	currentFile = "Shops.json";
		    	File regFile = new File("./plugins/mcDropShop/Shops.json");
		    	try{
		    		regFile.createNewFile();
		    	}catch(IOException e){
		    		getLogger().severe("Couldn't make Shops.json! Enable failed");
		    		this.setEnabled(false);
		    		
		    		e.printStackTrace();
		    		
		    		return;
		    	}
		    	
		    	FileWriter registry = new FileWriter("./plugins/mcDropShop/Shops.json");
		    	
		    	registry.write("{\"listVersion\":\"0.2.0\", \"shopsArray\":[]}");
		    	registry.flush();
		    	registry.close();
		    	
		    	getLogger().info("Building config.txt...");
		    	
		    	currentFile = "config.txt";
		    	String ncf = "// This is the configuration file\n"
		    			+"// for mcDropShop 0.2.0 by majikchicken\n"
		    			+"// <http://github.com/casnix/mcdropshop/>\n\n"

		    			+"// Don't uncomment this!\n"
		    			+"$(configVersion)=0.2.0\n\n"

		    			+"// Stream line color: sets the color for the arrow lines\n"
		    			+"// Can be overridden by $(streamLineTextLeft) and $(streamLineTextRight)\n"
		    			+"// If you comment out this variable, it will default to \u00a7c\n"
		    			+"$(streamLineColor)=\u00a7c\n\n"

		    			+"// Arrow color: sets the color for the arrows\n"
		    			+"// Can be overriden by $(arrowRight) and $(arrowLeft)\n"
		    			+"// If commented out, will default to \u00a7e\n"
		    			+"$(arrowColor)=\u00a7e\n\n"

		    			+"// Text color: sets the color of the text\n"
		    			+"// If commented out, will default to \u00a7f\n"
		    			+"$(textColor)=\u00a7f\n\n"

		    			+"//-----------------------------------------------------\n"
		    			+"// Override variables.\n\n"

		    			+"// Sets the text on either side of the nav arrows\n"
		    			+"// When commented out, both default to \"-----\"\n"
		    			+"$(streamLineTextLeft)=-----\n"
		    			+"$(streamLineTextRight)=-----\n\n"

		    			+"// Sets the nav arrows' text\n"
		    			+"// When commented out, default to << and >> respectively\n"
		    			+"$(arrowLeft)=<<\n"
		    			+"$(arrowRight)=>>\n\n"

		    			+"//------------------------------------------------------\n"
		    			+"// Permissions variables (teleport, etc.)\n"
		    			+"// Do not uncomment these!!!\n\n"
		    			
		    			+"//Allow players to use shops\n"
		    			+"$(interact)=mcDropShop.interact\n\n"

						+"//Let's players do /mcDropShop list\n"
						+"$(list)=mcDropShop.list\n\n"

		    			+"// Sets the shop tp perm for players\n"
		    			+"$(tpPermPlayers)=mcDropShop.player.tp\n\n"

		    			+"// tp perm for staff\n"
		    			+"$(tpPermStaff)=mcDropShop.staff.tp\n\n"

		    			+"// Sets the perms for adding, deleting, and moving shops\n"
		    			+"$(buildShops)=mcDropShop.staff.build\n\n"

		    			+"// Sets the perms for selling, buying, replacing, and deleting items\n"
		    			+"$(manageShops)=mcDropShop.staff.manage\n\n"

		    			+"//-----------------------------------------------------\n"
		    			+"// Master overrides (do not uncomment!)\n\n"

		    			+"// Allow or disallow teleporting to shops\n"
		    			+"$(allowTeleport)=true\n\n"

						+"// TP for players, on or off.  Ignores staff perms\n"
						+"$(playerTeleport)=true\n\n"

						+"// Administrator perms\n"
						+"$(adminPerm)=mcDropShop.admin\n\n"
						
		    			+"//-----------------------------------------------------\n"
		    			+"// Log file path (do not uncomment!)\n"
		    			+"$(logPath)=./plugins/mcDropShop/logs";
		    					
	    		FileWriter ocf = new FileWriter("./plugins/mcDropShop/config.txt");
	    		ocf.write(ncf);
	    		ocf.flush();
	    		ocf.close();

		    }catch(IOException e){
		    	getLogger().severe("Couldn't add data to "+currentFile+"! Enable failed");
		    	this.setEnabled(false);
		    	
		    	e.printStackTrace();
		    	
		    	return;
		    }
		}
		
		ConfigSys.loadConfig();
		
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
	
	
	// Vault!!! Copypasta'd from
	// dev.bukkit.org/bukkit-plugins/vault/
	public static Permission permission = null;
    public static Economy economy = null;
    public static Chat chat = null;

    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    private boolean setupChat()
    {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }

    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
    
    private boolean checkSetup(){
    	boolean checkShops = Shops.checkShopsFile();
    	boolean checkConfig = ConfigSys.checkConfFile();
    	
    	if(checkShops && checkConfig)
    		return true;
    	
    	return false;
    }
}
