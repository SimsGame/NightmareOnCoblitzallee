/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dhbw.Zombiz.gameEngine.logic;

import static com.dhbw.Zombiz.gameEngine.logic.BuildRoom.backgroundImage;
import static com.dhbw.Zombiz.gameEngine.logic.BuildRoom.cnt;
import static com.dhbw.Zombiz.gameEngine.logic.BuildRoom.deleteFrame;
import com.dhbw.Zombiz.output.audio.SoundPlayer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Stazzer
 */
public class Inventory {
    
    private final JFrame frame;
    private final Runtime runtime;
    private Item firstFocussedItem;
    private Item secondFocussedItem;
    public int itemsFocussedInInventory = 0;
    
    public Inventory(JFrame frame, Runtime runtime){
        this.frame = frame;
        this.runtime = runtime;
    }
    
    public void itemAndRoomObjInteraction(Item item) {
        Item roomObj = runtime.br.getRoomObj();
        Item itemToCombine = item;

        System.out.println("Item " + itemToCombine.getName());

        if (Integer.parseInt(itemToCombine.getCombinesWith()) == roomObj.getId()) {
            // Deletes item from Inventory if it works
            System.out.println("is ready to combine ...");
            List<Item> inventory = runtime.getInventory();
            if (inventory.contains(itemToCombine)) {
                inventory.remove(itemToCombine);
                runtime.setInventory(inventory);
                drawInventory();
                frame.repaint();
            }
            int autoItem = runtime.checkAutoItem(roomObj.id, 'r', 'r');
            System.out.println("There was a combination " + roomObj.id
                    + " and I want to add item " + autoItem);
            if (autoItem != 0) {
                if (autoItem == -1) {
                    int n = runtime.checkNumber(roomObj.id);
                    while (n > 0) {
                        runtime.addItemToInventory(getItemById(runtime.br.getItems(), runtime.checkAutoItem(roomObj.id, 'm', 'r')));
                    }
                } else {
                    runtime.addItemToInventory(getItemById(runtime.br.getItems(), autoItem));
                }
            }
        } else {
            // Do somethin if it not works ...
            System.out.println("RoomObj:  " + roomObj.getName());
            System.out.println("Item :" + item.getName() + " , "
                    + item.getCombinesWith());
        }
    }
    
    public void checkIfCombineable(Item item1, Item item2) {
        if (Integer.parseInt(item1.getCombinesWith()) == item2.getId()
                || Integer.parseInt(item2.getCombinesWith()) == item1.getId()) {
            System.out.println("You can combine !!!");
        } else {
            System.out.println("Item 1 :" + item1.getName() + " is not combineable with " + item2.getName());
        }
    }
    
    private void checkIfItemsCombineable() {
        Item firstItem = this.firstFocussedItem;
        Item secondItem = this.secondFocussedItem;

        this.firstFocussedItem = null;
        this.secondFocussedItem = null;

        this.itemsFocussedInInventory = 0;
        System.out.println("Your first Item combines with " + Integer.parseInt(firstItem.getCombinesWith()) + " and second item is " + secondItem.id);
        if (Integer.parseInt(firstItem.getCombinesWith()) == secondItem.getId() || firstItem.getId() == Integer.parseInt(secondItem.getCombinesWith())) {
            System.out.println("fit");
            int addedItem = this.runtime.checkItemCombination(firstItem.getId(), secondItem.getId());
            if (addedItem != 0) {
                System.out.println(getItemById(this.runtime.getRemItems(), addedItem).getName());
                this.runtime.addItemToInventory(getItemById(this.runtime.getRemItems(), addedItem));
                this.runtime.remItemFromInventory(firstItem.getId());
                if (firstItem.getId() != secondItem.getId()) {
                    this.runtime.remItemFromInventory(secondItem.getId());
                }
                drawInventory();
                frame.repaint();
            }
        } else {
            System.out.println("Doesn't combine");
        }
    }
    
    public void focusItemInInventory(Item item) {

        itemsFocussedInInventory++;
        List<Item> inventory = this.runtime.getInventory();
        int itemId = item.getId();
        int pos = 0;

        System.out.println("Items focussed = " + itemsFocussedInInventory);

        for (int cnt = 0; cnt < inventory.size(); cnt++) {
            if (itemId == inventory.get(cnt).getId()) {
                pos = cnt;
                System.out.println("cnt " + cnt);
            }
        }

        //Sets the black borders arround the focussed inventory items
        System.out.println("Pos " + pos);
        if (pos == 0) {
            frame.getContentPane().getGraphics().drawRect(110, 148, 90, 84);
        }
        if (pos > 0 && pos <= 3) {
            frame.getContentPane().getGraphics().drawRect(110 + (pos * 120), 148, 90, 84);
        }
        if (pos == 4) {
            frame.getContentPane().getGraphics().drawRect(101 + (pos * 120), 148, 90, 84);
        }
        if (pos == 5) {
            frame.getContentPane().getGraphics().drawRect(110, 250, 90, 84);
        }
        if (pos >= 6 && pos <= 8) {
            frame.getContentPane().getGraphics().drawRect(110 + (pos%5 * 120), 250, 90, 84);
        }
        if (pos == 9) {
            frame.getContentPane().getGraphics().drawRect(101 + (pos%5 * 120), 250, 90, 84);
        }
        if (pos == 10) {
            frame.getContentPane().getGraphics().drawRect(110, 351, 90, 84);
        }
        if (pos >= 11 && pos <= 13) {
            frame.getContentPane().getGraphics().drawRect(110 + (pos%5 * 120), 351, 90, 84);
        }
        if (pos == 14) {
            frame.getContentPane().getGraphics().drawRect(101 + (pos%5 * 120), 351, 90, 84);
        }

        if (itemsFocussedInInventory == 1) {
            firstFocussedItem = item;
        }
        if (itemsFocussedInInventory == 2) {
            secondFocussedItem = item;
            checkIfItemsCombineable();
        }
    }
    
    public int getItemIDById(List<Item> items, int id) {
        int cnt = 0;
        for (cnt = 0; cnt < items.size(); cnt++) {
            int itemId = items.get(cnt).getId();
            if (itemId == id) {
                return cnt;
            }
        }
        return cnt;
    }

    public Item getItemById(List<Item> items, int id) {
        Item item = null;
        for (Item item1 : items) {
            int itemId = item1.getId();
            if (itemId == id) {
                return item1;
            }
        }
        return item;
    }

    public Item getItemFromInventoryById(int id) {
        Item item = null;
        List<Item> inventory = this.runtime.getInventory();

        for (int cnt = 0; cnt < inventory.size(); cnt++) {
            if (id == inventory.get(cnt).getId()) {
                item = inventory.get(cnt);
            }
        }
        return item;
    }

    public void deleteItem(int notToDrawItemId) {
        deleteFrame(frame);
        List<Item> items = this.runtime.br.getItems();

        int removeId = getItemIDById(items, notToDrawItemId);
        items.remove(removeId);
        this.runtime.br.setItems(items);

        JLabel label = this.runtime.br.setBackgroundImage(frame);
        this.runtime.br.drawObjects(frame, true);
        this.runtime.br.drawRoomObjects(frame, true);
        drawInventoryBag();
        frame.add(label);
    }

    // draws the menu on game screen 
    public void drawInventoryBag() {
        // if room is floor draw another HUD
        boolean drawLeaveSymbol = this.runtime.checkLeave(this.runtime.br.roomId);
        if (drawLeaveSymbol) {
            BufferedImage inventoryBag = null;
            try {
                inventoryBag = ImageIO.read(new File("src/main/resources/Picture/hud.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            backgroundImage.getGraphics().drawImage(inventoryBag, 0, 0, 800, 590, null);
            // special ItemID
            this.runtime.br.addClickableFunction(670, 0, 130, 115, 999, frame, "inventory");
            this.runtime.br.addClickableFunction(710, 510, 90, 90, 999, frame, "leaveRoom");
            this.runtime.br.addClickableFunction(0, 0, 100, 90, 999, frame, "inGameMenue");
        } else {
            BufferedImage inventoryBag = null;
            try {
                inventoryBag = ImageIO.read(new File("src/main/resources/Picture/hudFloor.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            backgroundImage.getGraphics().drawImage(inventoryBag, 0, 0, 800, 590, null);
            // special ItemID
            this.runtime.br.addClickableFunction(670, 0, 130, 115, 999, frame, "inventory");
            this.runtime.br.addClickableFunction(0, 0, 100, 90, 999, frame, "inGameMenue");
        }
    }
    
    // draw Inventory
    public void drawInventory() {
        SoundPlayer.soundClick(runtime.br.muted);
        deleteFrame(frame);
        JLabel label = runtime.br.setBackgroundImage(frame);

        runtime.br.drawObjects(frame, false);
        runtime.br.drawRoomObjects(frame, false);

        BufferedImage inventoryBackground = null;
        BufferedImage btnCloseInventory = null;
        BufferedImage btninventoryNext = null;
        BufferedImage btninventoryBack = null;
        try {
            inventoryBackground = ImageIO.read(new File("src/main/resources/Picture/Inventory/inventoryBackground.png"));
            btnCloseInventory = ImageIO.read(new File("src/main/resources/Picture/Inventory/btnCloseInventory.png"));
            btninventoryNext = ImageIO.read(new File("src/main/resources/Picture/Inventory/btninventoryNext.png"));
            btninventoryBack = ImageIO.read(new File("src/main/resources/Picture/Inventory/btninventoryBack.png"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        backgroundImage.getGraphics().drawImage(inventoryBackground, 90, 80, 600, 400, null);
        backgroundImage.getGraphics().drawImage(btnCloseInventory, 630, 85, 60, 50, null);
        if (runtime.br.side == 0) {
            backgroundImage.getGraphics().drawImage(btninventoryNext, 560, 450, 60, 25, null);
        } else {
            backgroundImage.getGraphics().drawImage(btninventoryBack, 160, 450, 25, 25, null);
        }
        runtime.br.addClickableFunction(630, 85, 50, 50, 999, frame, "inventory:close");
        runtime.br.addClickableFunction(560, 450, 60, 25, 999, frame, "inventory:next");
        runtime.br.addClickableFunction(160, 450, 60, 25, 999, frame, "inventory:back");
        List<Item> inventory = runtime.getInventory();

        int rowid = 0;
        int xLoc = 110;
        int yLoc = 165;

        int xLocClick = 110;
        int yLocClick = 154;

        for (int cntItemPic = 0 + runtime.br.side; cntItemPic < inventory.size(); cntItemPic++) {

            String itemPicPath = runtime.br.trimmPicPath(inventory.get(cntItemPic).getPicturePath());
            itemPicPath = itemPicPath.replace(".png", "");
            itemPicPath = itemPicPath + "_inventory.png";

            BufferedImage foregroundImage = null;
            try {
                foregroundImage = ImageIO.read(new File(itemPicPath));
                System.out.println("Your Image is " + foregroundImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            //Fixes the inventory item pics in the last row
            if(cntItemPic == 10){
                yLoc -= 12;
            }
            
            backgroundImage.getGraphics().drawImage(foregroundImage, xLoc, yLoc, null);
            System.out.println("I added a clickable function to " + inventory.get(cntItemPic).getName() + " at xLocClick " + xLocClick + " and yLocClick " + yLocClick);
            runtime.br.addClickableFunction(xLocClick, yLocClick, 90, 84, inventory.get(cntItemPic).getId(), frame, "inventory:click");

            if (rowid == 0) {
                yLocClick = 154;
                xLoc = xLoc + 120;
                xLocClick = xLocClick + 120;
                if (cntItemPic >= 4 + runtime.br.side) {
                    rowid = 1;
                    xLoc = -10;
                    xLocClick = -10;
                    yLoc = 267;
                    yLocClick = 267;
                }
            }
            if (rowid == 1) {
                System.out.println("I switched into the second row.");
                xLoc = xLoc + 120;
                xLocClick = xLocClick + 120;
                if (cntItemPic >= 9 + runtime.br.side) {
                    rowid = 2;
                    xLoc = -10;
                    xLocClick = -10;
                    yLoc = 380;
                    yLocClick = 380;
                }
            }
            /**
             * TODO: Put items > 13 to the next page (or hide them on the first one)
             */
            if (rowid == 2) {
                xLoc = xLoc + 120;
                xLocClick = xLocClick + 120;
                if (cntItemPic > 13 + runtime.br.side) {
                    rowid = -1;
                    xLoc = -10;
                    xLocClick = -10;
                    yLoc = 493;
                    yLocClick = 493;
                }
            }

            /*
             * if(cntItemPic > 6){ xLoc = 110; xLocClick = 110;
             * 
             * yLoc = yLoc+103; yLocClick = yLocClick+103; } if(cntItemPic >
             * 11){ xLoc = 110; xLocClick = 110;
             * 
             * yLoc = yLoc+103; yLocClick = yLocClick+103; }
             */
            System.out.println(cntItemPic);
        }
        frame.add(label);
        frame.repaint();
    }
}
