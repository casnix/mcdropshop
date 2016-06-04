/*File Shops.java - Part of mcDropShop
 * Version 0.1.0
 * Copyright (c) Matt Rienzo, 2016
 * <http://github.com/casnix/>
 * Licensed under GPLv2.  See license in src/doc/LICENSE or src/doc/LICENSE.txt
 */
package io.github.casnix.mcdropshop.util.configsys;

// Java
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;

// bukkit
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

// json-simple
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// HolographicDisplaysAPI
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;

// internal
import io.github.casnix.mcdropshop.Holograms;
import io.github.casnix.mcdropshop.Main;

public class Shops {
	public boolean NullListException;
	// Our handler
	public final Main plugin;
	
	public Shops(Main plugin2){
		this.plugin = plugin2;
	}
	
	// Variables
	private Object returnStack = "";
	
	public static Map<String, Holograms> shops = new HashMap<String, Holograms>();
	
	// final Shops getFormattedShopList()
	// -- Puts our return value into the this.returnStack
	// -- Return type of Shops
	public final Shops getFormattedShopList(){
		try{
			String configTable = new String(Files.readAllBytes(Paths.get("./plugins/mcDropShop/Shops.json")));
			
			JSONParser parser = new JSONParser();
			
			Object obj = parser.parse(configTable);
			

			JSONObject jsonObj = (JSONObject) obj;
			

			JSONArray shopList  = (JSONArray) jsonObj.get("shopsArray");
			
			String formattedShopList = "List of mcDropShops:\n";
			// Make sure that our array isn't empty
			if(shopList == null){
				formattedShopList = "\u00A7e<\u00A7ainternal error\u00A7e>";
			}
			
			String shopName;
			String world;
			String x;
			String y;
			String z;
			String tmp;
			JSONObject shopObj;
			//System.out.println("[MDS DBG MSG] shopList.size() = "+shopList.size());
			for(int index = 0; index < shopList.size(); index++){
				//System.out.println(index);
				
				shopObj = (JSONObject) (shopList.get(index));
				
				//System.out.println(shopObj);
				shopName = (String) shopObj.get("shopName");
				//System.out.println(shopName);
				world = (String) shopObj.get("world");
				//System.out.println(world);
				x = (String) shopObj.get("x");
				//System.out.println(x);
				y = (String) shopObj.get("y");
				//System.out.println(y);
				z = (String) shopObj.get("z");
				//System.out.println(z);
				
				formattedShopList = formattedShopList + "\u00A7a"+shopName+": \u00A7e"+world+","+x+","+y+","+z+"\n";
				//System.out.println(formattedShopList);
			}
			
			this.returnStack = (Object) formattedShopList;
			return this;
		}catch(ParseException e){
			Bukkit.getLogger().warning("[mcDropShop] Caught ParseException in getFormattedShopList(void)");
			e.printStackTrace();
		}catch(FileNotFoundException e){
			Bukkit.getLogger().warning("[mcDropShop] Could not find ./plugins/mcDropShop/Shops.json");
			e.printStackTrace();
		}catch(IOException e){
			Bukkit.getLogger().warning("[mcDropShop] Caught IOException in getFormattedShopList(void)");
			e.printStackTrace();
		}
		
		return this;
	}
	
	public final String toString(){
		return (String) this.returnStack;
	}

	public final Shops addShop(String shopName, String worldName, String x, String y, String z, Player player) {
		try{
			String configTable = new String(Files.readAllBytes(Paths.get("./plugins/mcDropShop/Shops.json")));
			
			JSONParser parser = new JSONParser();
			
			Object obj = parser.parse(configTable);
			
			JSONObject jsonObj = (JSONObject) obj;
			
			JSONArray shopList  = (JSONArray) jsonObj.get("shopsArray");
			
			// Make sure that our array isn't empty
			if(shopList == null){
				player.sendMessage("\u00A7e<\u00A73mcDropShop : internal error\u00A7e>");
				
				return this;
			}
			
			JSONObject shopObj;
			
			String shopName2;
			// Check if there's already a shop by that name			
			for(int index = 0; index < shopList.size(); index++){
				shopObj = (JSONObject) (shopList.get(index));
				
				shopName2 = (String) shopObj.get("shopName");
				
				if(shopName2.equals(shopName)){
					player.sendMessage("\u00a7aThere is already a shop by that name!");
					return this;
				}

			}
			
			// Set our new shop in the JSON table in memory
			JSONObject newShop = new JSONObject();
			
			newShop.put("shopName", shopName);
			newShop.put("world", worldName);
			newShop.put("x", x);
			newShop.put("y", y);
			newShop.put("z", z);
			
			// Update the array in memory
			shopList.add(newShop);
			
			// Update the entire JSON table in memory
			jsonObj.put("shopsArray", shopList);
			
			// Make the shop stub
			JSONObject shopStub = new JSONObject();
			shopStub.put("amount", "0");
			shopStub.put("price", "0");
			shopStub.put("type", "buy");
			
			JSONArray newShopItemList = new JSONArray();
			
			JSONObject SNL = new JSONObject();
			SNL.put("ShopItems", newShopItemList);
			
			jsonObj.put(shopName, SNL);
			
			// Update Shops.json
			FileWriter shopsJSON = new FileWriter("./plugins/mcDropShop/Shops.json");
			
			shopsJSON.write(jsonObj.toJSONString());
			
			shopsJSON.flush();
			shopsJSON.close();
			
			player.sendMessage("\u00a7aShop added!");

			return this;
		}catch(ParseException e){
			Bukkit.getLogger().warning("[mcDropShop] Caught ParseException in addShop()");
			e.printStackTrace();
		}catch(FileNotFoundException e){
			Bukkit.getLogger().warning("[mcDropShop] Could not find ./plugins/mcDropShop/Shops.json");
			e.printStackTrace();
		}catch(IOException e){
			Bukkit.getLogger().warning("[mcDropShop] Caught IOException in addShop()");
			e.printStackTrace();
		}
		
		return this;
	}
	
	public final Shops delShop(String shopName, Player player) {
		try{
			String configTable = new String(Files.readAllBytes(Paths.get("./plugins/mcDropShop/Shops.json")));
			
			JSONParser parser = new JSONParser();
			
			Object obj = parser.parse(configTable);
			
			JSONObject jsonObj = (JSONObject) obj;
			
			JSONArray shopList  = (JSONArray) jsonObj.get("shopsArray");
			
			// Make sure that our array isn't empty
			if(shopList == null){
				player.sendMessage("\u00A7e<\u00A73mcDropShop : internal error 0x00\u00A7e>");
				
				return this;
			}else if(jsonObj.get(shopName) == null){
				player.sendMessage("\u00A7e<\u00A73mcDropShop : internal error 0x01\u00A7e>");
				
				return this;
			}
			
			JSONObject shopObj;
			
			String shopName2;
			int index;
			// Find our shop in our array			
			for(index = 0; index < shopList.size(); index++){
				shopObj = (JSONObject) (shopList.get(index));
				
				shopName2 = (String) shopObj.get("shopName");
				
				if(shopName2.equals(shopName)){
					break;
				}

			}
			
			// Remove shop from array
			shopList.remove(index);
			
			// Update the entire JSON table in memory
			jsonObj.put("shopsArray", shopList);

			// Now remove the shop data from the root
			jsonObj.remove(shopName);
			
			// Update Shops.json
			FileWriter shopsJSON = new FileWriter("./plugins/mcDropShop/Shops.json");
			
			shopsJSON.write(jsonObj.toJSONString());
			
			shopsJSON.flush();
			shopsJSON.close();
			
			Shops.shops.remove(shopName);
			
			player.sendMessage("\u00a7aShop removed!");

			return this;
		}catch(ParseException e){
			Bukkit.getLogger().warning("[mcDropShop] Caught ParseException in addShop()");
			e.printStackTrace();
		}catch(FileNotFoundException e){
			Bukkit.getLogger().warning("[mcDropShop] Could not find ./plugins/mcDropShop/Shops.json");
			e.printStackTrace();
		}catch(IOException e){
			Bukkit.getLogger().warning("[mcDropShop] Caught IOException in addShop()");
			e.printStackTrace();
		}
		
		return this;
	}

	public final Shops moveShop(String shopName, String x, String y, String z, Player player){
		try{
			String configTable = new String(Files.readAllBytes(Paths.get("./plugins/mcDropShop/Shops.json")));
			
			JSONParser parser = new JSONParser();
			
			Object obj = parser.parse(configTable);
			
			JSONObject jsonObj = (JSONObject) obj;
			
			JSONArray shopList  = (JSONArray) jsonObj.get("shopsArray");
			
			// Make sure that our array isn't empty
			if(shopList == null){
				player.sendMessage("\u00A7e<\u00A73mcDropShop : internal error\u00A7e>");
				
				return this;
			}
			
			JSONObject shopObj = new JSONObject();
			
			String shopName2;
			boolean foundShop = false;
			int index = 0;
			// Make sure the shop exists			
			for(index = 0; index < shopList.size(); index++){
				shopObj = (JSONObject) (shopList.get(index));
				
				shopName2 = (String) shopObj.get("shopName");
				
				if(shopName2.equals(shopName)){
					shopList.remove(index);
					foundShop = true;
					break;
				}
			}
			
			if(!foundShop){
				player.sendMessage("\u00a7aThat shop doesn't exist!");
				return this;
			}
			
			// Update the coordinates in memory
			shopObj.put("x", x);
			shopObj.put("y", y);
			shopObj.put("z", z);
			shopObj.put("world", player.getWorld().getName());
			shopObj.put("shopName", shopName);
			
			shopList.add(shopObj);
			
			// Update the entire JSON table in memory
			jsonObj.put("shopsArray", shopList);
			
			// Update Shops.json
			FileWriter shopsJSON = new FileWriter("./plugins/mcDropShop/Shops.json");
			
			shopsJSON.write(jsonObj.toJSONString());
			
			shopsJSON.flush();
			shopsJSON.close();
			
			player.sendMessage("\u00a7aShop moved!");

			return this;
		}catch(ParseException e){
			Bukkit.getLogger().warning("[mcDropShop] Caught ParseException in addShop()");
			e.printStackTrace();
		}catch(FileNotFoundException e){
			Bukkit.getLogger().warning("[mcDropShop] Could not find ./plugins/mcDropShop/Shops.json");
			e.printStackTrace();
		}catch(IOException e){
			Bukkit.getLogger().warning("[mcDropShop] Caught IOException in addShop()");
			e.printStackTrace();
		}
		
		return this;
	}

	public final void loadShops(){
		try{
			String configTable = new String(Files.readAllBytes(Paths.get("./plugins/mcDropShop/Shops.json")));
			
			JSONParser parser = new JSONParser();
			
			Object obj = parser.parse(configTable);
			

			JSONObject jsonObj = (JSONObject) obj;
			

			JSONArray shopList  = (JSONArray) jsonObj.get("shopsArray");
			
			// Make sure that our array isn't empty
			if(shopList == null){
				Bukkit.getLogger().severe("[mcDropShop] Null shopList!");
				this.NullListException = true;
				return;
			}
			
			JSONObject shopObj;
			//System.out.println("[MDS DBG MSG] lS shopList.size() = "+shopList.size());
			for(int index = 0; index < shopList.size(); index++){
				//System.out.println(index);
				
				shopObj = (JSONObject) (shopList.get(index));
				
				if(shopObj == null)
					continue; // Skip missing indices
				
				Holograms holo = new Holograms(this.plugin);
				holo.loadShop(shopObj, (JSONObject) jsonObj);
				
				Shops.shops.put(holo.shopName, holo);
			}
		}catch(ParseException e){
			Bukkit.getLogger().warning("[mcDropShop] Caught ParseException in loadShops(void)");
			e.printStackTrace();
		}catch(FileNotFoundException e){
			Bukkit.getLogger().warning("[mcDropShop] Could not find ./plugins/mcDropShop/Shops.json");
			e.printStackTrace();
		}catch(IOException e){
			Bukkit.getLogger().warning("[mcDropShop] Caught IOException in loadShops(void)");
			e.printStackTrace();
		}
		
		return;
	}

	public final Shops addBuy(String shopName, String cost, Player player){
		try{
			String configTable = new String(Files.readAllBytes(Paths.get("./plugins/mcDropShop/Shops.json")));
			
			JSONParser parser = new JSONParser();
			
			Object obj = parser.parse(configTable);
			
			JSONObject jsonObj = (JSONObject) obj;

			JSONObject shopObj = (JSONObject) jsonObj.get(shopName);
			
			// Make sure the shop exists
			if(shopObj == null){
				player.sendMessage("\u00a76That shop does not exist!");
				return this;
			}
			
			String newItem = player.getItemInHand().getType().name();
			
			Bukkit.getLogger().info("Player \"" + player.getDisplayName() + "\" added " + newItem + " to shop " + shopName);
			
			JSONArray shopItems = (JSONArray) shopObj.get("ShopItems");
			
			shopItems.add(newItem + ":buy:"+player.getItemInHand().getDurability());
			
			shopObj.put("ShopItems", shopItems);
			
			JSONObject itemStub = new JSONObject();
			itemStub.put("amount", Integer.toString(player.getItemInHand().getAmount()));
			itemStub.put("price", cost);
			itemStub.put("type", "buy");
			itemStub.put("durability", ""+player.getItemInHand().getDurability());
			
			shopObj.put(newItem + ":buy:"+player.getItemInHand().getDurability(), itemStub);
			
			jsonObj.put(shopName, shopObj);
			
			// Update Shops.json
			FileWriter shopsJSON = new FileWriter("./plugins/mcDropShop/Shops.json");
			
			shopsJSON.write(jsonObj.toJSONString());
			
			shopsJSON.flush();
			shopsJSON.close();
			
			player.sendMessage("\u00a7aShop updated!");

			return this;
		}catch(ParseException e){
			Bukkit.getLogger().warning("[mcDropShop] Caught ParseException in addBuy()");
			e.printStackTrace();
		}catch(FileNotFoundException e){
			Bukkit.getLogger().warning("[mcDropShop] Could not find ./plugins/mcDropShop/Shops.json");
			e.printStackTrace();
		}catch(IOException e){
			Bukkit.getLogger().warning("[mcDropShop] Caught IOException in addBuy()");
			e.printStackTrace();
		}
		
		return this;
	}

	public final Shops addSell(String shopName, String cost, Player player){
		try{
			String configTable = new String(Files.readAllBytes(Paths.get("./plugins/mcDropShop/Shops.json")));
			
			JSONParser parser = new JSONParser();
			
			Object obj = parser.parse(configTable);
			
			JSONObject jsonObj = (JSONObject) obj;

			JSONObject shopObj = (JSONObject) jsonObj.get(shopName);
			
			// Make sure the shop exists
			if(shopObj == null){
				player.sendMessage("\u00a76That shop does not exist!");
				return this;
			}
			
			String newItem = player.getItemInHand().getType().name();
			
			Bukkit.getLogger().info("Player \"" + player.getDisplayName() + "\" added " + newItem + " to shop " + shopName);
			
			JSONArray shopItems = (JSONArray) shopObj.get("ShopItems");
			
			shopItems.add(newItem + ":sell:"+player.getItemInHand().getDurability());
			
			shopObj.put("ShopItems", shopItems);
			
			JSONObject itemStub = new JSONObject();
			itemStub.put("amount", Integer.toString(player.getItemInHand().getAmount()));
			itemStub.put("price", cost);
			itemStub.put("type", "sell");
			itemStub.put("durability", ""+player.getItemInHand().getDurability());
			
			shopObj.put(newItem + ":sell:"+player.getItemInHand().getDurability(), itemStub);
			
			jsonObj.put(shopName, shopObj);
			
			// Update Shops.json
			FileWriter shopsJSON = new FileWriter("./plugins/mcDropShop/Shops.json");
			
			shopsJSON.write(jsonObj.toJSONString());
			
			shopsJSON.flush();
			shopsJSON.close();
			
			player.sendMessage("\u00a7aShop updated!");

			return this;
		}catch(ParseException e){
			Bukkit.getLogger().warning("[mcDropShop] Caught ParseException in addSell()");
			e.printStackTrace();
		}catch(FileNotFoundException e){
			Bukkit.getLogger().warning("[mcDropShop] Could not find ./plugins/mcDropShop/Shops.json");
			e.printStackTrace();
		}catch(IOException e){
			Bukkit.getLogger().warning("[mcDropShop] Caught IOException in addSell()");
			e.printStackTrace();
		}
		
		return this;
	}

	public static boolean checkShopsFile(){
		File shopsFile = new File("./plugins/mcDropShop/Shops.json");
		
		if(!shopsFile.exists())
			return false;
		
		try{
			String configTable = new String(Files.readAllBytes(Paths.get("./plugins/mcDropShop/Shops.json")));
			
			JSONParser parser = new JSONParser();
			
			Object obj = parser.parse(configTable);
			
			JSONObject jsonObj = (JSONObject) obj;
			
			String listVersion = (String)jsonObj.get("listVersion");
			
			if(listVersion == null || !listVersion.equals("0.2.0")){
				// Old version
				jsonObj.put("listVersion", "0.2.0");
				
				// Update Shops.json
				FileWriter shopsJSON = new FileWriter("./plugins/mcDropShop/Shops.json");
				
				shopsJSON.write(jsonObj.toJSONString());
				
				shopsJSON.flush();
				shopsJSON.close();
			}
			
			return true;
		}catch(Exception e){
			Bukkit.getLogger().severe("mcDropShop failed on setup check in Shops.json");
			
			e.printStackTrace();
			
			return false;
		}
	}

	public final void tpPlayer(String shopName, Player player){
		if(!shops.containsKey(shopName)){
			player.sendMessage("\u00a7eThat shop does not exist!");
			
			return;
		}
		
		double x = shops.get(shopName).x;
		double y = shops.get(shopName).y;
		double z = shops.get(shopName).z;
		String world = shops.get(shopName).world;
		
		World wurld = Bukkit.getServer().getWorld(world);
		
		player.teleport(new Location(wurld, x, y, z));
	}

	public final Shops delIndex(String shopName, String index, Player player){
		if(!shops.containsKey(shopName)){
			player.sendMessage("\u00a7eThat shop does not exist!");
			
			return this;
		}
		
		try{
			String configTable = new String(Files.readAllBytes(Paths.get("./plugins/mcDropShop/Shops.json")));
			
			JSONParser parser = new JSONParser();
			
			Object obj = parser.parse(configTable);
			
			JSONObject jsonObj = (JSONObject) obj;

			JSONObject shopObj = (JSONObject) jsonObj.get(shopName);
			
			JSONArray shopItems = (JSONArray) shopObj.get("ShopItems");
			
			if(shopItems.size() <= Integer.parseInt(index)){
				player.sendMessage("\u00a7eThat shop doesn't have an item at index ("+index+")!");
				
				return this;
			}
			
			String itemName = (String)shopItems.get(Integer.parseInt(index));
			
			shopItems.remove(Integer.parseInt(index));
			
			shopObj.put("ShopItems", shopItems);
			
			shopObj.remove(itemName);
			
			jsonObj.put(shopName, shopObj);
			
			// Update Shops.json
			FileWriter shopsJSON = new FileWriter("./plugins/mcDropShop/Shops.json");
			
			shopsJSON.write(jsonObj.toJSONString());
			
			shopsJSON.flush();
			shopsJSON.close();
			
			player.sendMessage("\u00a7aShop updated!");

			return this;
		}catch(ParseException e){
			Bukkit.getLogger().warning("[mcDropShop] Caught ParseException in delIndex()");
			e.printStackTrace();
		}catch(FileNotFoundException e){
			Bukkit.getLogger().warning("[mcDropShop] Could not find ./plugins/mcDropShop/Shops.json");
			e.printStackTrace();
		}catch(IOException e){
			Bukkit.getLogger().warning("[mcDropShop] Caught IOException in delIndex()");
			e.printStackTrace();
		}

		return this;
	}

	public final Shops repBuy(String shopName, String cost, String index, Player player){
		if(!shops.containsKey(shopName)){
			player.sendMessage("\u00a7eThat shop does not exist!");
			
			return this;
		}
		
		try{
			String configTable = new String(Files.readAllBytes(Paths.get("./plugins/mcDropShop/Shops.json")));
			
			JSONParser parser = new JSONParser();
			
			Object obj = parser.parse(configTable);
			
			JSONObject jsonObj = (JSONObject) obj;

			JSONObject shopObj = (JSONObject) jsonObj.get(shopName);
			
			JSONArray shopItems = (JSONArray) shopObj.get("ShopItems");
			
			if(shopItems.size() <= Integer.parseInt(index)){
				player.sendMessage("\u00a7eThat shop doesn't have an item at index ("+index+")!");
				
				return this;
			}
			
			String itemName = (String)shopItems.get(Integer.parseInt(index));
			shopObj.remove(itemName);
			shopItems.remove(Integer.parseInt(index));
			
			String newItem = player.getItemInHand().getType().name();
			
			Bukkit.getLogger().info("Player \"" + player.getDisplayName() + "\" added " + newItem + " to shop " + shopName);
			
			shopItems.add(Integer.parseInt(index), newItem + ":buy:"+player.getItemInHand().getDurability());
			
			shopObj.put("ShopItems", shopItems);
			
			JSONObject itemStub = new JSONObject();
			itemStub.put("amount", Integer.toString(player.getItemInHand().getAmount()));
			itemStub.put("price", cost);
			itemStub.put("type", "buy");
			itemStub.put("durability", ""+player.getItemInHand().getDurability());
			
			shopObj.put(newItem + ":buy:"+player.getItemInHand().getDurability(), itemStub);
			
			shopObj.put("ShopItems", shopItems);
			
			jsonObj.put(shopName, shopObj);
			
			// Update Shops.json
			FileWriter shopsJSON = new FileWriter("./plugins/mcDropShop/Shops.json");
			
			shopsJSON.write(jsonObj.toJSONString());
			
			shopsJSON.flush();
			shopsJSON.close();
			
			player.sendMessage("\u00a7aShop updated!");

			return this;
		}catch(ParseException e){
			Bukkit.getLogger().warning("[mcDropShop] Caught ParseException in repBuy()");
			e.printStackTrace();
		}catch(FileNotFoundException e){
			Bukkit.getLogger().warning("[mcDropShop] Could not find ./plugins/mcDropShop/Shops.json");
			e.printStackTrace();
		}catch(IOException e){
			Bukkit.getLogger().warning("[mcDropShop] Caught IOException in repBuy()");
			e.printStackTrace();
		}

		return this;
	}

	public final Shops repSell(String shopName, String cost, String index, Player player){
		if(!shops.containsKey(shopName)){
			player.sendMessage("\u00a7eThat shop does not exist!");
			
			return this;
		}
		
		try{
			String configTable = new String(Files.readAllBytes(Paths.get("./plugins/mcDropShop/Shops.json")));
			
			JSONParser parser = new JSONParser();
			
			Object obj = parser.parse(configTable);
			
			JSONObject jsonObj = (JSONObject) obj;

			JSONObject shopObj = (JSONObject) jsonObj.get(shopName);
			
			JSONArray shopItems = (JSONArray) shopObj.get("ShopItems");
			
			if(shopItems.size() <= Integer.parseInt(index)){
				player.sendMessage("\u00a7eThat shop doesn't have an item at index ("+index+")!");
				
				return this;
			}
			
			String itemName = (String)shopItems.get(Integer.parseInt(index));
			shopObj.remove(itemName);
			shopItems.remove(Integer.parseInt(index));
			
			String newItem = player.getItemInHand().getType().name();
			
			Bukkit.getLogger().info("Player \"" + player.getDisplayName() + "\" added " + newItem + " to shop " + shopName);
			
			shopItems.add(Integer.parseInt(index), newItem + ":sell:"+player.getItemInHand().getDurability());
			
			shopObj.put("ShopItems", shopItems);
			
			JSONObject itemStub = new JSONObject();
			itemStub.put("amount", Integer.toString(player.getItemInHand().getAmount()));
			itemStub.put("price", cost);
			itemStub.put("type", "sell");
			itemStub.put("durability", ""+player.getItemInHand().getDurability());
			
			shopObj.put(newItem + ":sell:"+player.getItemInHand().getDurability(), itemStub);
			
			shopObj.put("ShopItems", shopItems);
			
			jsonObj.put(shopName, shopObj);
			
			// Update Shops.json
			FileWriter shopsJSON = new FileWriter("./plugins/mcDropShop/Shops.json");
			
			shopsJSON.write(jsonObj.toJSONString());
			
			shopsJSON.flush();
			shopsJSON.close();
			
			player.sendMessage("\u00a7aShop updated!");

			return this;
		}catch(ParseException e){
			Bukkit.getLogger().warning("[mcDropShop] Caught ParseException in repBuy()");
			e.printStackTrace();
		}catch(FileNotFoundException e){
			Bukkit.getLogger().warning("[mcDropShop] Could not find ./plugins/mcDropShop/Shops.json");
			e.printStackTrace();
		}catch(IOException e){
			Bukkit.getLogger().warning("[mcDropShop] Caught IOException in repBuy()");
			e.printStackTrace();
		}

		return this;
	}

}
