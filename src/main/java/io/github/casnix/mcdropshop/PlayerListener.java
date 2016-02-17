/*File PlayerListener.java - Part of mcDropShop
 * Version 0.1.0
 * Copyright (c) Matt Rienzo, 2016
 * Licensed under GPLv2.  See license in src/doc/LICENSE or src/doc/LICENSE.txt
 */
package io.github.casnix.mcdropshop;

// bukkit imports
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerListener implements Listener{
	
	// Loads shops for players when they join
	@EventHandler
	public void onJoin(PlayerJoinEvent player){
		// Add player to each shop
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent player){
		// Remove player from each shop
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent player){
		// Rotational arrows, other
	}
}
