/*File ConfigSys.java - Part of mcDropShop
 * Version 0.0.1-SNAPSHOT
 * Copyright (c) Matt Rienzo, 2016
 * Licensed under GPLv2.  See license in src/doc/LICENSE or src/doc/LICENSE.txt
 */
package io.github.casnix.mcdropshop.util;

// java/system imports
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigSys {
	public static Map<String, String> variables = new HashMap<String, String>();
	
	public static void loadConfig(){
		try {
			String configTable = new String(Files.readAllBytes(Paths.get("./plugins/mcDropShop/config.txt")));
			
			String[] lines = configTable.split("\n");
		
			int numVars = 0;
			for(int i = 0; i < lines.length; i++){
				if(lines[i].length() < 2)
					continue;
				
				if(lines[i].substring(0, 1).equals("//"))
					continue;
				
				numVars++;
			}
			
			int quidditch = numVars;
			if(numVars == 0)
				return;
			
			String[] vars = new String[numVars];
			for(int n = 0; n < lines.length; n++){
				if(lines[n].length() < 2)
					continue;
				
				if(lines[n].substring(0, 1).equals("//"))
					continue;
				
				vars[numVars - 1] = lines[n];
				numVars--;
			}
			
			for(int q = 0; q < vars.length; q++){
				String[] parts = vars[q].split("=");
				String val = new String();
				
				if(parts.length == 1)
					variables.put(parts[0], " ");
				
				if(parts.length > 2){
					val = parts[1];
					for(int i = 2; i < parts.length; i++)
						val += "=" + parts[i];
				}
				
				if(parts.length == 2)
					val = parts[1];
				
				variables.put(parts[0], val);
			}
			
		} catch (IOException e) {
			Bukkit.getLogger().severe("mcDropShop failed to load config file");
			e.printStackTrace();
			
		}

		if(!variables.containsKey("$(streamLineColor)"))
			variables.put("$(streamLineColor)", "\u00a7c");
		if(!variables.containsKey("$(arrowColor)"))
			variables.put("$(arrowColor)", "\u00a7e");
		if(!variables.containsKey("$(textColor)"))
			variables.put("$(textColor)", "\u00a7f");
		if(!variables.containsKey("$(streamLineTextLeft)"))
			variables.put("$(streamLineTextLeft)", "-----");
		if(!variables.containsKey("$(streamLineTextRight)"))
			variables.put("$(streamLineTextRight)", "-----");
		if(!variables.containsKey("$(arrowLeft)"))
			variables.put("$(arrowLeft)", "<<");
		if(!variables.containsKey("$(arrowRight)"))
			variables.put("$(arrowRight)", ">>");
	}
	
	public static boolean checkConfFile(){
		File cf = new File("./plugins/mcDropShop/config.txt");
		
		if(!cf.exists())
			return false;
		
		try {
			String configTable = new String(Files.readAllBytes(Paths.get("./plugins/mcDropShop/config.txt")));
			
			String[] lines = configTable.split("\n");
		
			int numVars = 0;
			for(int i = 0; i < lines.length; i++){
				if(lines[i].length() < 2)
					continue;
				
				if(lines[i].substring(0, 1).equals("//"))
					continue;
				
				numVars++;
			}
			
			int quidditch = numVars;
			if(numVars == 0)
				return false;
			
			String[] vars = new String[numVars];
			for(int n = 0; n < lines.length; n++){
				if(lines[n].length() < 2)
					continue;
				
				if(lines[n].substring(0, 1).equals("//"))
					continue;
				
				vars[numVars - 1] = lines[n];
				numVars--;
			}
			
			for(int q = 0; q < vars.length; q++){
				String[] parts = vars[q].split("=");
				String val = new String();
				
				if(parts.length == 1)
					variables.put(parts[0], " ");
				
				if(parts.length > 2){
					val = parts[1];
					for(int i = 2; i < parts.length; i++)
						val += "=" + parts[i];
				}
				
				if(parts.length == 2)
					val = parts[1];
				
				variables.put(parts[0], val);
			}
			
			if(variables.containsKey("$(configVersion)")){
				if(variables.get("$(configVersion)").equals("0.2.0"))
					return true;
				
				// If this code is reached, then it's an old config version
				FileWriter ocf = new FileWriter("./plugins/mcDropShop/oldConfig-"+variables.get("$(configVersion)")+".txt");
				ocf.write(configTable);
				ocf.flush();
				ocf.close();
				
				ConfigSys.upgradeConf();
				return true;
			}
			
			return false;
		} catch (IOException e) {
			Bukkit.getLogger().severe("mcDropShop failed to check config file");
			e.printStackTrace();
			
			return false;
		}
	}
	
	public static void upgradeConf() throws IOException{
		if(!variables.containsKey("$(streamLineColor)"))
			variables.put("$(streamLineColor)", "\u00a7c");
		if(!variables.containsKey("$(arrowColor)"))
			variables.put("$(arrowColor)", "\u00a7e");
		if(!variables.containsKey("$(textColor)"))
			variables.put("$(textColor)", "\u00a7f");
		if(!variables.containsKey("$(streamLineTextLeft)"))
			variables.put("$(streamLineTextLeft)", "-----");
		if(!variables.containsKey("$(streamLineTextRight)"))
			variables.put("$(streamLineTextRight)", "-----");
		if(!variables.containsKey("$(arrowLeft)"))
			variables.put("$(arrowLeft)", "<<");
		if(!variables.containsKey("$(arrowRight)"))
			variables.put("$(arrowRight)", ">>");
		
		String ncf = "// This is the configuration file\n"
+"// for mcDropShop 0.2.0 by majikchicken\n"
+"// <http://github.com/casnix/mcdropshop/>\n\n"

+"// Don't uncomment this!\n"
+"$(configVersion)=0.2.0\n\n"

+"// Stream line color: sets the color for the arrow lines\n"
+"// Can be overridden by $(streamLineTextLeft) and $(streamLineTextRight)\n"
+"// If you comment out this variable, it will default to \u00a7c\n"
+"$(streamLineColor)="+variables.get("$(streamLineColor)")+"\n\n"

+"// Arrow color: sets the color for the arrows\n"
+"// Can be overriden by $(arrowRight) and $(arrowLeft)\n"
+"// If commented out, will default to \u00a7e\n"
+"$(arrowColor)="+variables.get("$(arrowColor)")+"\n\n"

+"// Text color: sets the color of the text\n"
+"// If commented out, will default to \u00a7f\n"
+"$(textColor)="+variables.get("$(textColor)")+"\n\n"

+"//-----------------------------------------------------\n"
+"// Override variables.\n\n"

+"// Sets the text on either side of the nav arrows\n"
+"// When commented out, both default to \"-----\"\n"
+"$(streamLineTextLeft)="+variables.get("$(streamLineTextLeft)")+"\n"
+"$(streamLineTextRight)="+variables.get("$(streamLineTextRight)")+"\n\n"

+"// Sets the nav arrows' text\n"
+"// When commented out, default to << and >> respectively\n"
+"$(arrowLeft)="+variables.get("$(arrowLeft)")+"\n"
+"$(arrowRight)="+variables.get("$(arrowRight)")+"\n\n"

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
	}
}
