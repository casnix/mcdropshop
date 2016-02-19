/*File CommandLineExe.java - Part of mcDropShop
 * Version 0.1.0
 * Copyright (c) Matt Rienzo, 2016
 * <http://github.com/casnix/>
 * Licensed under GPLv2.  See license in src/doc/LICENSE or src/doc/LICENSE.txt
 */
package io.github.casnix.mcdropshop;

// internal
import io.github.casnix.mcdropshop.Main;
import io.github.casnix.mcdropshop.util.configsys.Shops;

// bukkit
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

// system
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// HolographicDisplays
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import com.gmail.filoghost.holographicdisplays.api.line.TouchableLine;

// Vault
import net.milkbowl.vault.economy.*;

public class Holograms {
	// Exception lines:
	public boolean NullListException = false;
	
	// Private variables
	private int itemIndex;
	private int leftOffset;
	private JSONArray itemList;
	private JSONObject shopStub;
	private Hologram hologram;
	private TextLine textLine3;
	private TextLine textLine4;
	private ItemLine itemLine1;
	public static Economy econ = Main.economy;
	// Our handler
	public final Main plugin;
		
	public Holograms(Main plugin2){
		this.plugin = plugin2;
	}
	
	public void loadShop(JSONObject shopObj, JSONObject jsonObj){
		//System.out.println(shopObj);
		String shopName = (String) shopObj.get("shopName");
		//Sytem.out.println(shopName);
		String world = (String) shopObj.get("world");
		//System.out.println(world);
		String x = (String) shopObj.get("x");
//		System.out.println(x);
		String y = (String) shopObj.get("y");
//		System.out.println(y);
		String z = (String) shopObj.get("z");
	//	System.out.println(z);
				
		JSONObject shopStub = (JSONObject) jsonObj.get(shopName);
		JSONArray itemList = (JSONArray) shopStub.get("ShopItems");
				
		Plugin plugin = this.plugin;
		World worldInstance = Bukkit.getServer().getWorld(world);
		Location holoLoc = new Location(worldInstance, Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
			
		Hologram hologram = HologramsAPI.createHologram(plugin, holoLoc);
			
		// Making actual hologram
		// Formatting
		int itemIndex = itemList.size();
		int leftOffset = itemIndex;
				
		// Store values in memory for use in touch handlers
		this.itemIndex = itemIndex;
		this.leftOffset = leftOffset;
		this.itemList = itemList;
		this.shopStub = shopStub;
				
		String itemToDisplay = (String) itemList.get(itemIndex - leftOffset);
		String[] iTD = itemToDisplay.split("[:]+");
		JSONObject itemData = (JSONObject) shopStub.get(itemToDisplay);
		String amount = (String) itemData.get("amount");
		String price = (String) itemData.get("price");
		String buyOrSell = (String) itemData.get("type");
				
				
		TextLine scrollLeft = hologram.appendTextLine("\u00a7c-----\u00a7e<<\u00a7c-----"); // Maybe put these on bottom so they're easy to touch?
		// On touch, subtract hologram index by one
				
		TextLine textLine3 = hologram.appendTextLine(amount + "x - $" + price);
				
		TextLine scrollRight = hologram.appendTextLine("\u00a7c-----\u00a7e>>\u00a7c-----");
				
		ItemLine itemLine1 = hologram.appendItemLine(new ItemStack(Material.getMaterial(iTD[0])));
				
		TextLine textLine4 = hologram.appendTextLine("[" + buyOrSell + "]");
				
		this.textLine3 = textLine3;
		this.itemLine1 = itemLine1;
		this.textLine4 = textLine4;
		this.hologram = hologram;
			
		// Touch handlers
		scrollLeft.setTouchHandler(new TouchHandler() {			
			public void onTouch(Player player){
				//Bukkit.broadcastMessage("[mcDropShop] scroll left");
				
				Holograms holoClass = getThis();
				holoClass.moveOffsetLeft();
						
				String itemToDisplay = (String) holoClass.itemList.get(holoClass.itemIndex - holoClass.leftOffset);
				String[] iTD = itemToDisplay.split("[:]+");
				JSONObject itemData = (JSONObject) holoClass.shopStub.get(itemToDisplay);
				String amount = (String) itemData.get("amount");
				String price = (String) itemData.get("price");
				String buyOrSell = (String) itemData.get("type");
						
				holoClass.textLine3.setText(amount + "x - $" + price);
				holoClass.textLine4.setText("[" + buyOrSell + "]");
				holoClass.itemLine1.setItemStack(new ItemStack(Material.getMaterial(iTD[0])));
			}
		});
				
		scrollRight.setTouchHandler(new TouchHandler() {
			public void onTouch(Player player){
				//Bukkit.broadcastMessage("[mcDropShop] scroll right");
				
				Holograms holoClass = getThis();
				holoClass.moveOffsetRight();
						
				String itemToDisplay = (String) holoClass.itemList.get(holoClass.itemIndex - holoClass.leftOffset);
				String[] iTD = itemToDisplay.split("[:]+");
				JSONObject itemData = (JSONObject) holoClass.shopStub.get(itemToDisplay);
				String amount = (String) itemData.get("amount");
				String price = (String) itemData.get("price");
				String buyOrSell = (String) itemData.get("type");
						
				holoClass.textLine3.setText(amount + "x - $" + price);
				holoClass.textLine4.setText("[" + buyOrSell + "]");
				holoClass.itemLine1.setItemStack(new ItemStack(Material.getMaterial(iTD[0])));
			}
		});
				
		textLine4.setTouchHandler(new TouchHandler() {
			public void onTouch(Player player){
				//Bukkit.broadcastMessage("[mcDropShop] buy/sell");
						
				// Get data from offset
				// Check if this item is buy or sell
						
				// If buy:
				// Check if player has enough money
				// Check if there's room in player's inventory
				// If true, then take the player's money and give items
				
				// If sell:
				// Check if player has the items
				// If so, then take the items and pay the player
				
				Holograms holoClass = getThis();
				
				String itemToDisplay = (String) holoClass.itemList.get(holoClass.itemIndex - holoClass.leftOffset);
				String[] iTD = itemToDisplay.split("[:]+");
				JSONObject itemData = (JSONObject) holoClass.shopStub.get(itemToDisplay);
				String amount = (String) itemData.get("amount");
				String price = (String) itemData.get("price");
				String buyOrSell = (String) itemData.get("type");
				
				if(buyOrSell.equals("buy")){
					double playerBalance = econ.getBalance(player);
					
					if(playerBalance < Double.parseDouble(price)){
						player.sendMessage("\u00a7cYou don't have enough money to do that!");
						return;
					}
					
					if(player.getInventory().firstEmpty() == -1){
						player.sendMessage("\u00a7cYou don't have enough space in your inventory!");
						return;
					}
					
					// There's both enough money and room, take their money and run!
					econ.withdrawPlayer(player, Double.parseDouble(price));
					player.sendMessage("\u00a7a$" + price + " has been taken from your account.");
					
					ItemStack boughtItems = new ItemStack(Material.getMaterial(iTD[0]), Integer.parseInt(amount));
					
					player.getInventory().addItem(boughtItems);
				}else{
					// Player must be holding item in hand
					String itemInHand = player.getItemInHand().getType().name();
					int amountInHand = player.getItemInHand().getAmount();
					
					if(itemInHand.equals(iTD[0]) && (Integer.parseInt(amount) == amountInHand)){
						player.getInventory().removeItem(player.getInventory().getItemInHand());
						econ.depositPlayer(player, Double.parseDouble(price));
						
						player.sendMessage("\u00a7a$" + price + " has been deposited in your account.");
					}else{
						player.sendMessage("\u00a7cYou aren't holding " + amount + " of " + iTD[0] + "!");
					}
				}
			}
		});
	}

	private void moveOffsetLeft(){
		this.leftOffset += 1;
		
		if((this.itemIndex - this.leftOffset) < 0){
			this.leftOffset = 1;
		}
	}
	
	private void moveOffsetRight(){
		this.leftOffset -= 1;
		
		if((this.itemIndex - this.leftOffset) >= this.itemIndex){
			this.leftOffset = this.itemIndex;
		}
	}
	
	public Holograms getThis(){
		return this;
	}
	
	public void closeShops(){
		// Get list of holograms
		Collection<Hologram> holos = HologramsAPI.getHolograms(this.plugin);
		
		for(Hologram holo : holos){
			holo.delete();
		}
	}

	public void refreshShops(){
		this.closeShops();
		Shops myShops = new Shops(this.plugin);
		myShops.loadShops();
		
		if(myShops.NullListException){
			this.NullListException = true;
		}
	}
}
