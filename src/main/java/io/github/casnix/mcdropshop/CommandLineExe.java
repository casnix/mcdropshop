/*File CommandLineExe.java - Part of mcDropShop
 * Version 0.1.0
 * Copyright (c) Matt Rienzo, 2016
 * <http://github.com/casnix/>
 * Licensed under GPLv2.  See license in src/doc/LICENSE or src/doc/LICENSE.txt
 */

package io.github.casnix.mcdropshop;

// bukkit imports
import org.bukkit.command.CommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

// internal imports
import io.github.casnix.mcdropshop.Main;
import io.github.casnix.mcdropshop.Versioning;
import io.github.casnix.mcdropshop.util.configsys.Shops;
import io.github.casnix.mcdropshop.util.ConfigSys;
import io.github.casnix.mcdropshop.util.Screen;

public class CommandLineExe implements CommandExecutor {
	private final Main plugin;
	
	// CommandLineExe(mcDropShop plugin)
	public CommandLineExe(Main plugin2){
		this.plugin = plugin2;
	}
	// boolean onCommand(etc)
	// Command handler for plugin
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		boolean isPlayerEntity = false;
		
		// Need to implement these commands:
		//  /mcdropshop help
		//  /mcdropshop about
		//  A way to add a shop via coordinates over console...?
		
		// Must make sure that it is a player calling us.
		// The only thing that the console can use
		//   is /mcdropshop list
		if(sender instanceof Player){
			isPlayerEntity = true;
		}else{
			sender.sendMessage("Only mcDropShop command available to non-players is /mcdropshop list!");
			isPlayerEntity = false; // Don't need this but whatevs
		}
		
		if(args.length < 1){
			sender.sendMessage("\u00a76Usage: /mcdropshop <command> <options>");
			sender.sendMessage("\u00a76Try /mcdropshop help");
			return false;
		}
		
		Shops mcDropShops = new Shops(this.plugin);
		Holograms myHolo = new Holograms(this.plugin);
		
		if(sender.hasPermission(ConfigSys.variables.get("$(interact)")) && isPlayerEntity){
			if(args[0].equals("list")){
				if(!sender.hasPermission(ConfigSys.variables.get("$(list)"))){
					sender.sendMessage("\u00a74You do not have permission to do that!");
					return false;
				}
				String formattedShopList = mcDropShops.getFormattedShopList().toString();
				Screen.sendMultilineMessageToPlayer((Player) sender, formattedShopList);
			}else if(args[0].equals("add")){
				if(!sender.hasPermission(ConfigSys.variables.get("$(buildShops)"))){
					sender.sendMessage("\u00a74You do not have permission to do that!");
					return false;
				}
				// /mcdropshop add <name>  - Creates an mcDropShop at your location
				if(args.length < 2){
					sender.sendMessage("\u00a7aCommand format:");
					sender.sendMessage("\u00a7a  /mcdropshop add <shopname>");
				}else{
				
					String worldName = ((Player) sender).getWorld().getName();
					double x = ((Player) sender).getLocation().getX();
					double y = ((Player) sender).getLocation().getY() + 2.5;
					double z = ((Player) sender).getLocation().getZ();
					
					mcDropShops.addShop(args[1], worldName, Double.toString(x), Double.toString(y), Double.toString(z), (Player) sender);
					
					myHolo.refreshShops();
					
					if(myHolo.NullListException){
						sender.sendMessage("\u00a74INTERNAL ERROR");
						return false;
					}
				}
			}else if(args[0].equals("del")){
				if(!sender.hasPermission(ConfigSys.variables.get("$(buildShops)"))){
					sender.sendMessage("\u00a74You do not have permission to do that!");
					return false;
				}
				// /mcdropshop del <name>  - Deletes the named mcDropShop 
				if(args.length < 2){
					sender.sendMessage("\u00a7aCommand format:");
					sender.sendMessage("\u00a7a  /mcdropshop del <shopname>");
				}else{
					mcDropShops.delShop(args[1], (Player) sender);
					
					myHolo.refreshShops();
					
					if(myHolo.NullListException){
						sender.sendMessage("\u00a74INTERNAL ERROR");
						return false;
					}
					
					sender.sendMessage("\u00a7aShop deleted!");
				}
			}else if(args[0].equals("addsell")){
				if(!sender.hasPermission(ConfigSys.variables.get("$(manageShops)"))){
					sender.sendMessage("\u00a74You do not have permission to do that!");
					return false;
				}
				// /mcdropshop addsell <shop> <cost>
				if(args.length < 3){
					sender.sendMessage("\u00a7aCommand format:");
					sender.sendMessage("\u00a7a  /mcdropshop addsell <shop> <cost>");
				}else{
					mcDropShops.addSell(args[1], args[2], (Player) sender);
					
					myHolo.refreshShops();
					
					if(myHolo.NullListException){
						sender.sendMessage("\u00a74INTERNAL ERROR");
						return false;
					}
				}
			}else if(args[0].equals("addbuy")){
				if(!sender.hasPermission(ConfigSys.variables.get("$(manageShops)"))){
					sender.sendMessage("\u00a74You do not have permission to do that!");
					return false;
				}
				
				// /mcdropshop addbuy <shop> <cost>
				if(args.length < 3){
					sender.sendMessage("\u00a7aCommand format:");
					sender.sendMessage("\u00a7a  /mcdropshop addsell <shop> <cost>");
				}else{
					mcDropShops.addBuy(args[1], args[2], (Player) sender);
					
					myHolo.refreshShops();
					
					if(myHolo.NullListException){
						sender.sendMessage("\u00a74INTERNAL ERROR");
						return false;
					}
				}
			}else if(args[0].equals("move")){ 
				if(!sender.hasPermission(ConfigSys.variables.get("$(buildShops)"))){
					sender.sendMessage("\u00a74You do not have permission to do that!");
					return false;
				}
				if(args.length < 2){
					sender.sendMessage("\u00a7aCommand format:");
					sender.sendMessage("\u00a7a  /mcdropshop move <shopname>");
				}else{
					double x = ((Player) sender).getLocation().getX();
					double y = ((Player) sender).getLocation().getY() + 2.5;
					double z = ((Player) sender).getLocation().getZ();
					
					mcDropShops.moveShop(args[1], Double.toString(x), Double.toString(y), Double.toString(z), (Player) sender);
					
					myHolo.refreshShops();
					
					if(myHolo.NullListException){
						sender.sendMessage("\u00a74INTERNAL ERROR");
						return false;
					}
				}
			}else if(args[0].equals("version")){
				sender.sendMessage("mcDropShop version "+Versioning.CurrentVersion());
			}else if(args[0].equals("about")){
				sender.sendMessage("\u00a7amcDropShop version "+Versioning.CurrentVersion());
				sender.sendMessage("\u00a7a-- Copyright (c) Matt Rienzo, 2016");
				sender.sendMessage("\u00a7a-- GPLv2 open source license");
				sender.sendMessage("\u00a7a-- < http://github.com/casnix/mcdropshop/blob/master/LICENSE >");
			}else if(args[0].equals("help")){
				// If they have $(interact) perm
				sender.sendMessage("\u00a76help - Show this help");
				sender.sendMessage("\u00a76about - Show license and copyright info");
				sender.sendMessage("\u00a76version - Show version");
				
				if(sender.hasPermission(ConfigSys.variables.get("$(list)")))
					sender.sendMessage("\u00a76list - List shops in world");
				
				if(sender.hasPermission(ConfigSys.variables.get("$(tpPermPlayers)"))
						|| sender.hasPermission(ConfigSys.variables.get("$(tpPermStaff)")))
					sender.sendMessage("\u00a76tp <shop name> - Teleport to a shop"); // Only if tp=true in the config file
				
				if(sender.hasPermission(ConfigSys.variables.get("$(manageShops)"))){
					sender.sendMessage("\u00a76addsell <name> <cost> - Add currently held item to shop to sell for <cost>");
					sender.sendMessage("\u00a76addbuy <name> <cost> - Add currently held item to shop to buy for <cost>");
					sender.sendMessage("\u00a76refresh - internal function");
					sender.sendMessage("\u00a76repsell <name> <cost> <index> - Replace the item at index in shop NAME with held item to sell for cost");
					sender.sendMessage("\u00a76repbuy <name> <cost> <index> - Replace the item at index in shop NAME with held item to buy for cost");
					sender.sendMessage("\u00a76delindex <name> <index> - Delete the item at index in shop NAME");	
				}
				
				if(sender.hasPermission(ConfigSys.variables.get("$(buildShops)"))){
					sender.sendMessage("\u00a76add <name> - Add a shop at your location");
					sender.sendMessage("\u00a76del <name> - Remove a shop");
					sender.sendMessage("\u00a76move <name> - Move a shop to your location");	
				}
				
				if(sender.hasPermission(ConfigSys.variables.get("$(adminPerm)"))){
					sender.sendMessage("\u00a76give <playername> <amount> - Give <playername> $<amount> of money");
					sender.sendMessage("\u00a76reload - reload the configuration file for mcDropShop");
				}
				
			}else if(args[0].equals("refresh")){
				if(!sender.hasPermission(ConfigSys.variables.get("$(manageShops)"))){
					sender.sendMessage("\u00a74You do not have permission to do that!");
					return false;
				}
				
				myHolo.refreshShops();
				
				if(myHolo.NullListException){
					sender.sendMessage("\u00a74INTERNAL ERROR");
					return false;
				}
				
				sender.sendMessage("\u00a7aShops refreshed!");
			}else if(args[0].equals("repsell")){
				if(!sender.hasPermission(ConfigSys.variables.get("$(manageShops)"))){
					sender.sendMessage("\u00a74You do not have permission to do that!");
					return false;
				}
				
				if(args.length != 4){
					sender.sendMessage("\u00a7aCommand format:");
					sender.sendMessage("\u00a7a  /mcdropshop repsell <shopname> <cost> <index>");
					
					return false;
				}
				
				mcDropShops.repSell(args[1], args[2], args[3], (Player) sender);
				
				myHolo.refreshShops();
				
				if(myHolo.NullListException){
					sender.sendMessage("\u00a74INTERNAL ERROR");
					return false;
				}
			}else if(args[0].equals("repbuy")){
				if(!sender.hasPermission(ConfigSys.variables.get("$(manageShops)"))){
					sender.sendMessage("\u00a74You do not have permission to do that!");
					return false;
				}
				
				if(args.length != 4){
					sender.sendMessage("\u00a7aCommand format:");
					sender.sendMessage("\u00a7a  /mcdropshop repbuy <shopname> <cost> <index>");
					
					return false;
					
				}
				
				mcDropShops.repBuy(args[1], args[2], args[3], (Player) sender);
				
				myHolo.refreshShops();
				
				if(myHolo.NullListException){
					sender.sendMessage("\u00a74INTERNAL ERROR");
					return false;
				}
			}else if(args[0].equals("delindex")){
				if(!sender.hasPermission(ConfigSys.variables.get("$(manageShops)"))){
					sender.sendMessage("\u00a74You do not have permission to do that!");
					return false;
				}
				
				if(args.length != 3){
					sender.sendMessage("\u00a7aCommand format:");
					sender.sendMessage("\u00a7a  /mcdropshop delindex <shopname> <index>");
					return false;
				}
				
				mcDropShops.delIndex(args[1], args[2], (Player)sender);
				
				myHolo.refreshShops();
				
				if(myHolo.NullListException){
					sender.sendMessage("\u00a74INTERNAL ERROR");
					return false;
				}
			}else if(args[0].equals("tp")){
				if(!ConfigSys.variables.get("$(allowTeleport)").equals("true")){
					sender.sendMessage("\u00a7cTeleportation disabled for mcDropShop");
					
					return false;
				}
				
				if(!ConfigSys.variables.get("$(playerTeleport)").equals("true")
						&& sender.hasPermission(ConfigSys.variables.get("$(tpPermPlayers)"))){
					sender.sendMessage("\u00a7cTeleportation disabled for mcDropShop");
					
					return false;
				}
				
				if(!sender.hasPermission(ConfigSys.variables.get("$(tpPermStaff)"))
						|| !sender.hasPermission(ConfigSys.variables.get("$(tpPermPlayers)"))){
					sender.sendMessage("\u00a74You do not have permission to do that!");
					return false;
				}
				
				if(args.length != 2){
					sender.sendMessage("\u00a7aCommand format:");
					sender.sendMessage("\u00a7a  /mcdropshop tp <shopname>");
					return false;
				}
				
				mcDropShops.tpPlayer(args[1], (Player) sender);
				
				if(myHolo.NullListException){
					sender.sendMessage("\u00a74INTERNAL ERROR");
					return false;
				}
			}else if(args[0].equals("give")){
				if(!sender.hasPermission(ConfigSys.variables.get("$(adminPerm)"))){
					sender.sendMessage("\u00a74You do not have permission to do that!");
					return false;
				}
				
				if(args.length != 3){
					sender.sendMessage("\u00a7aCommand format:");
					sender.sendMessage("\u00a7a  /mcdropshop give <playername> <amount>");
					return false;
				}
				
				Player target = Bukkit.getPlayer(args[1]);
				
				if(target == null){
					sender.sendMessage("Player "+args[1]+" isn't online!");
					
					return false;
				}
				try{
					Holograms.econ.depositPlayer(target, Double.parseDouble(args[2]));
				}catch(Exception e){
					sender.sendMessage("Amount has to be a number");
					
					return false;
				}
			}else if(args[0].equals("reload")){
				if(!sender.hasPermission(ConfigSys.variables.get("$(adminPerm)"))){
					sender.sendMessage("\u00a74You do not have permission to do that!");
					return false;
				}
				
				ConfigSys.loadConfig();
				myHolo.refreshShops();
				
				if(myHolo.NullListException){
					sender.sendMessage("\u00a74INTERNAL ERROR");
					return false;
				}
				
				sender.sendMessage("\u00a7amcDropShop config reloaded!");
			}
			
			else{
				return false;
			}
			
			return true; //Only here until functions work 
		}else if(!sender.hasPermission(ConfigSys.variables.get("$(interact)")) && isPlayerEntity){
			sender.sendMessage("\u00a74You do not have permission to do that!");
			return false;
		}
		else{
			if(args[0].equals("list")){
				String formattedShopList = mcDropShops.getFormattedShopList().toString();
				Screen.sendMultilineMessageToPlayer((Player) sender, formattedShopList);
			}else{
				return false;
			}
			
			return true;
		}
		
		//return false;
	}
	

}
