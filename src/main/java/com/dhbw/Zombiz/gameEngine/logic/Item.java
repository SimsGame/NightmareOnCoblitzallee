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
package com.dhbw.Zombiz.gameEngine.logic;


/**
 * Class for Items in the Game. 
 * 
 * Differences between roomObjects (can not picked up) and normal items (which can be picked up).
 * 
 * Gets stuff from the XML parser, which creates these objects for the game
 * 
 * @author Jan Brodhaecker 
 */
public class Item extends AGameElement {


	String name;
	String picturePath;
	String description;
	String primaryLocation; 
	String secondaryLocation;
	String associatedWith;
	String combinesWith;
	String contains;
	boolean isCollectible;
	boolean isUseable;
	boolean isRoomObject; 
	String condition;
	String locationPointer;
	String audioFile;
	
	float itemLocX;
	float itemLocY; 
	
	
	
	

	public String getAudioFile() {
		return audioFile;
	}



	public void setAudioFile(String audioFile) {
		this.audioFile = audioFile;
	}



	public String getLocationPointer() {
		return locationPointer;
	}



	public void setLocationPointer(String locationPointer) {
		this.locationPointer = locationPointer;
	}



	public float getItemLocX() {
		return itemLocX;
	}



	public void setItemLocX(float itemLocX) {
		this.itemLocX = itemLocX;
	}



	public float getItemLocY() {
		return itemLocY;
	}



	public void setItemLocY(float itemLocY) {
		this.itemLocY = itemLocY;
	}



	public Item(int id) {
		super(id); 
		// TODO Auto-generated constructor stub
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getPicturePath() {
		return picturePath;
	}



	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public String getPrimaryLocation() {
		return primaryLocation;
	}



	public void setPrimaryLocation(String primaryLocation) {
		this.primaryLocation = primaryLocation;
	}



	public String getSecondaryLocation() {
		return secondaryLocation;
	}



	public void setSecondaryLocation(String secondaryLocation) {
		this.secondaryLocation = secondaryLocation;
	}



	public String getAssociatedWith() {
		return associatedWith;
	}



	public void setAssociatedWith(String associatedWith) {
		this.associatedWith = associatedWith;
	}



	public String getCombinesWith() {
		return combinesWith;
	}



	public void setCombinesWith(String combinesWith, String defaultValue) {
             if(!combinesWith.isEmpty()){
                    this.combinesWith = combinesWith;
                }else{
                    this.combinesWith = defaultValue;
                }
	}



	public String getContains() {
		return contains;
	}



	public void setContains(String contains, String defaultValue) {
                if(!contains.isEmpty()){
                    this.contains = contains;
                }else{
                    this.contains = defaultValue;
                }
	}



	public boolean isCollectible() {
		return isCollectible;
	}



	public void setCollectible(boolean isCollectible) {
		this.isCollectible = isCollectible;
	}



	public boolean isUseable() {
		return isUseable;
	}



	public void setUseable(boolean isUseable) {
		this.isUseable = isUseable;
	}


	public boolean isRoomObject() {
		return isRoomObject;
	}



	public void setRoomObject(boolean isRoomObject) {
		this.isRoomObject = isRoomObject;
	}


	public String getCondition() {
		return condition;
	}



	public void setCondition(String condition) {
		this.condition = condition;
	}



	
	

	

}
