/*File Screen.java - Part of mcDropShop
 * Version 0.1.0
 * Copyright (c) Matt Rienzo, 2016
 * <http://github.com/casnix/>
 * Licensed under GPLv2.  See license in src/doc/LICENSE or src/doc/LICENSE.txt
 */
package io.github.casnix.mcdropshop.util;

import org.bukkit.entity.Player;


public class Screen {
	public static void sendMultilineMessageToPlayer(Player player, String message){
	  if (player != null && message != null && player.isOnline()){
	    String[] s = message.split("\n");
	    for (String m : s){
	      player.sendMessage(m);
	    }
	  }
	}
}
