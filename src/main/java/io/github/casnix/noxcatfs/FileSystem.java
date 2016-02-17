/*File FileSystem.java - Part of the NOXCATFS driver (Java port)
 * NOXCATFS/1.0
 * Copyright (c) Matt Rienzo, 2016
 * Licensed under GPLv2.  License should have been distributed with this program
 */
package io.github.casnix.noxcatfs;

// Java/system imports

// JSON imports
import org.json.*;

public class FileSystem {
	// Variables
	private static String fsDev = "";
	private static int isFSDevAlreadySet = 0;
	
	// public int RegisterFSDev(String deviceName)
	// Need to register fsdevice, for file perms
	// -- Pass device name
	// -- e.g. io.github.casnix.noxcatfs.FileSystem.RegisterFSDev("GenericJavaTextEditor_with_unique_ID");
	// -- Returns 0 on success, other on failure
	public int RegisterFSDev(String deviceName){
		// Check if the fsDev ID has already been set
		if(FileSystem.isFSDevAlreadySet == 1){
			// Return -1.  You need to unregister your device ID to register another
			return -1;
		}else if(FileSystem.isFSDevAlreadySet == 0){
			// Set device ID to argument
			FileSystem.fsDev = deviceName;
			return 0;
		}
		
		// Error
		return -2;
	}
	
}
