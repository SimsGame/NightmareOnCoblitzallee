/*******************************************************************************
 * Copyright (c) 2013 DHBW.
 * This source is subject to the DHBW Permissive License.
 * Please see the License.txt file for more information.
 * All other rights reserved.
 * 
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY 
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
 * PARTICULAR PURPOSE.
 * 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 *Project: Zombiz
 *Package: com.dhbw.zombiz
 ********************************************************************************/
package com.dhbw.Zombiz.output.audio;
/**
 * Provides all functions for playing sound in game.
 * 
 * @author Jan Brodhaecker 
 */
public class SoundPlayer {
	
	public static Thread ts;
        private static Sound backgroundSound; 
	
	public static void soundUseDoor(boolean muted){
                if(muted) return;
		Sound doorSound = new Sound("door");
		Thread tdoorSound = new Thread(doorSound,"doorSound"); 
		tdoorSound.start();
	}
	
	public static void soundNextDialog(boolean muted){
            if(muted) return;
		Sound con_flip = new Sound("con-flip");
		Thread tcon_flip = new Thread(con_flip,"con-flip");
		tcon_flip.start();
	}
	
	public static void soundStartConv(boolean muted){
            if(muted) return;
		Sound con_start = new Sound("con-start");
		Thread tcon_start = new Thread(con_start,"con-start");
		tcon_start.start();
	}
	
	public static void soundItemPickUp(boolean muted){
            if(muted) return;
		Sound itempickup = new Sound("itempickup");
		Thread titempickup = new Thread(itempickup,"itempickup");
		titempickup.start();
	}
	
	public static void soundClick(boolean muted){
            if(muted) return;
		Sound clickSound = new Sound("click1");
		Thread tclickSound = new Thread(clickSound,"clickSound");
		tclickSound.start();
	}
	
	public static void createBackgroundSound(){
		backgroundSound = new Sound("Background");
		backgroundSound.setRepeat(true);
		ts = new Thread(backgroundSound,"Background Sound");
	}
	
	public static void stopBackgroundSound(){
		backgroundSound.stop();
	}
        
        public static void startBackgroundSound(){
                ts.start();
	}
}
