/**
 * *****************************************************************************
 * Copyright (c) 2013 DHBW. This source is subject to the DHBW Permissive
 * License. Please see the License.txt file for more information. All other
 * rights reserved.
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND,
 * EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A PARTICULAR PURPOSE.
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * Project: Zombiz Package: com.dhbw.zombiz
 *******************************************************************************
 */
package com.dhbw.Zombiz.gameEngine.logic;


import com.dhbw.Zombiz.gameEngine.logic.Runtime;

import com.dhbw.Zombiz.gameEngine.parser.XmlParser;
import com.dhbw.Zombiz.output.audio.SoundPlayer;
import com.dhbw.Zombiz.output.display.DialogOutput;
import com.dhbw.Zombiz.output.display.Help;
import com.dhbw.Zombiz.output.display.InGameMenue;
import com.dhbw.Zombiz.output.display.ItemInspect;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Main function of the game. Create rooms with all objects in it, which are
 * mentioned in the XMLfile.
 *
 * @author Jan Brodhaecker
 * @author Kira Schomber
 * @author Jannik Pachal
 * @author Nadir Yuldashev
 */
public class BuildRoom {

    List<Item> items;
    List<Item> roomObjects;
    List<Actor> actors;
    static BufferedImage backgroundImage;
    String roomImagePath;
    Room room;
    JFrame frame;
    Inventory inventory;
    static XmlParser parser;
    Runtime runtime;
    int roomId;
    int side = 0;
    static char option;
    static int convStatic;

    // different states to trigger, if a menu is open
    int menueIsOpenFlag = 0;
    int inventoryIsOpenFlag = 0;
    int inGameMenueIsOpenFlag = 0;
    int helpIsOpenFlag = 0;
    boolean dialogOutputOpen = false;
    
    static int cnt = 0;
    Item roomObj;
    boolean wantToCombineRoomObjWithItem;

    private final int Z_ENERGY_CAN_ID = 2;
    private final String ARROW = "Arrow";
    
    public BuildRoom(){
    }
    
    // Constructor
    public void init(boolean game, int roomId, JFrame frame, Runtime runtime) {
        this.runtime = runtime;
        this.inventory = new Inventory(frame, runtime);
        frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        deleteExistingKeyListeners(frame);
        menueIsOpenFlag = 0;

        XmlParser p = null;

        // choose, which XML file for which chapter should be loaded
        if (roomId < 100) {
            p = new XmlParser("src/main/resources/XML/chapter1.xml");
        } else if (roomId >= 100 && roomId < 200) {
            p = new XmlParser("src/main/resources/XML/chapter2.xml");
        }else{
            throw new IllegalArgumentException("No parser found for room: " + roomId);
        }

        setParser(p);
        if(p.getRoomById(roomId) == null){
            return;
        }
        setRoomId(roomId);
        this.runtime.setCurrRoomId(roomId);
        setRoom(p.getRoomById(roomId));
        setItems(p.getAllItemsByRoomId(roomId));
        setRoomObjects(p.getAllRoomObjectsByRoomId(roomId));

        if ((this.runtime.getGameState() == 0 && this.runtime.helpFlag) || this.runtime.getRemItems().isEmpty()) {
            System.out.println("Es wird initialisiert.");
            this.runtime.helpFlag = false;
            this.runtime.setRemItems(p.getRemItemList());
            this.runtime.addItemToInventory(p.getPickableItemById(Z_ENERGY_CAN_ID));
        }
        
        validateItemsInRoom();
        setActors(p.getAllNpcsByRoomId(roomId));
        setRoomImagePath(trimmPicPath(room.getPicturePath()));
        deleteFrame(frame);
        frame.repaint();
        
        setFrame(frame);
        JLabel label = setBackgroundImage(frame);
        drawRoomObjects(frame, true);
        drawObjects(frame, true);
        this.inventory.drawInventoryBag();
        drawActors(frame);
        frame.add(label);

        if (this.runtime.checkTrigger(roomId)) {
            automaticTrigger(roomId);
        }

        addKeyListeners(frame);
        frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        this.runtime.setFrame(frame);
    }
    
    public int getConvStatic() {
        return convStatic;
    }

    public boolean isWantToCombineRoomObjWithItem() {
        return wantToCombineRoomObjWithItem;
    }

    public void setWantToCombineRoomObjWithItem(
            boolean wantToCombineRoomObjWithItem) {
        this.wantToCombineRoomObjWithItem = wantToCombineRoomObjWithItem;
    }

    public Item getRoomObj() {
        return roomObj;
    }

    public void setRoomObj(Item roomObj) {
        this.roomObj = roomObj;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomImagePath() {
        return roomImagePath;
    }

    public void setRoomImagePath(String roomImagePath) {
        this.roomImagePath = roomImagePath;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public BufferedImage getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(BufferedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Item> getRoomObjects() {
        return roomObjects;
    }

    public void setRoomObjects(List<Item> roomObjects) {
        this.roomObjects = roomObjects;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public void setParser(XmlParser p) {
        this.parser = p;
    }

    public static XmlParser getParser() {
        return parser;
    }

    public void automaticTrigger(int roomId) {
        System.out.println("I was triggered.");
        int convId = getConvStatic();
        int autoItem = this.runtime.checkAutoItem(convId, 'c', 'c');
        if (autoItem != 0) {
            this.runtime.addItemToInventory(this.inventory.getItemById(getItems(), autoItem));
        }
        // BuildRoom br = new BuildRoom(roomId, frame);
        this.dialogOutputOpen = true;
        DialogOutput dout = new DialogOutput(frame, getParser().getConversationById(convId), getBackgroundImage(), getParser().getListOfActors(), roomId, this.runtime);
        /*
         * if (option=='c'){ DialogOutput dout = new DialogOutput(frame,
         * getParser().getConversationById(convId), getBackgroundImage(),
         * getParser().getListOfActors(), getRoomId()); }
         */
    }

    // remove all  existing keylisteners
    private void deleteExistingKeyListeners(JFrame frame) {
        System.out.println("Size KeyL: " + frame.getKeyListeners().length);
        for (int cnt = 0; cnt < frame.getKeyListeners().length; cnt++) {
            frame.removeKeyListener(frame.getKeyListeners()[cnt]);
        }
    }

    // add keylisteners to the game for different functions
    public void addKeyListeners(final JFrame frame) {
        KeyAdapter keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_I) {
                    if (inventoryIsOpenFlag == 0) {
                        BuildRoom.this.inventory.drawInventory();
                        inventoryIsOpenFlag = 1;
                    } else {
                        inventoryIsOpenFlag = 0;
                        refreshFrame(frame);
                    }
                }

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE && inventoryIsOpenFlag == 1) {
                    SoundPlayer.soundClick();
                    refreshFrame(frame);
                    inventoryIsOpenFlag = 0;
                }

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE && inventoryIsOpenFlag == 0 && menueIsOpenFlag == 0 && !dialogOutputOpen) {
                    drawInGameMenue(frame);
                    System.out.println("there here");
                }

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE && menueIsOpenFlag == 1) {
                    refreshFrame(frame);
                    SoundPlayer.soundClick();
                    System.out.println("here there");
                    menueIsOpenFlag = 0;
                }
                  
                if (e.getKeyCode() == KeyEvent.VK_H) {
                    if (helpIsOpenFlag == 0 && !dialogOutputOpen && menueIsOpenFlag == 0 && inGameMenueIsOpenFlag == 0 && inventoryIsOpenFlag == 0) {
                        Help.showHelpText(frame, getBackgroundImage(), BuildRoom.this.runtime);
                        helpIsOpenFlag = 1;
                    } else {
                        if (helpIsOpenFlag == 1) {
                            refreshFrame(frame);
                            helpIsOpenFlag = 0;
                        }
                    }
                }
            }
        };

        frame.addKeyListener(keyListener);
    }

    // draw all actors 
    public void drawActors(JFrame frame) {
        List<Actor> actors = getActors();
        for (int cnt = 0; cnt < actors.size(); cnt++) {
            Actor actor = actors.get(cnt);
            String actorPicPath = trimmPicPath(actor.getPicturePath());
            if (this.runtime.checkStep(actor.id, 'a', 'd')) {
                BufferedImage foregroundImage = null;
                try {
                    foregroundImage = ImageIO.read(new File(actorPicPath));
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        foregroundImage = ImageIO.read(new File("src/main/resources/Picture/Items/itemid_002.png"));
                    } catch (IOException f) {
                        f.printStackTrace();
                    }
                }
                backgroundImage.getGraphics().drawImage(foregroundImage, (int) actor.getNpcLocX(), (int) actor.getNpcLocY(), null);
                System.out.println("You draw NPC " + actor.getId());
                addClickableFunction((int) actor.getNpcLocX(), (int) actor.getNpcLocY(), 100, 300, actor.getId(), frame, "actor");
            }
        }
    }

    // when a item was taken by the user, it shouldn´t be drawn in the room anymore
    public void validateItemsInRoom() {
        List<Item> remItems = this.runtime.getRemItems();
        List<Item> items = getItems();

        for (int cnt = 0; cnt < getItems().size(); cnt++) {
            for (int count = 0; (count < remItems.size()) && (!getItems().isEmpty() && cnt < getItems().size()); count++) {
                if (getItems().get(cnt).getId() == remItems.get(count).getId()) {
                    int id = this.inventory.getItemIDById(items, getItems().get(cnt).getId());
                    items.remove(id);
                }
            }
            setItems(items);
        }
    }

    // draw all items 
    public void drawObjects(JFrame frame, boolean addClickableFct) {
        List<Item> itemsInDrawFunction = getItems();

        for (int cntItemPic = 0; cntItemPic < itemsInDrawFunction.size(); cntItemPic++) {
            if (this.runtime.checkStep(itemsInDrawFunction.get(cntItemPic).getId(), 'i', 'd')) {
                System.out.println("I want to draw item" + itemsInDrawFunction.get(cntItemPic).getId());
                String itemPicPath = trimmPicPath(itemsInDrawFunction.get(cntItemPic).getPicturePath());
                float xLoc = itemsInDrawFunction.get(cntItemPic).getItemLocY();
                float yLoc = itemsInDrawFunction.get(cntItemPic).getItemLocX();

                BufferedImage foregroundImage = null;
                try {
                    foregroundImage = ImageIO.read(new File(itemPicPath));
                } catch (IOException e) {                                      
                    e.printStackTrace();
                    try {
                        foregroundImage = ImageIO.read(new File("src/main/resources/Picture/Items/itemid_002.png"));
                    } catch (IOException f) {
                        f.printStackTrace();
                    }
                }
                backgroundImage.getGraphics().drawImage(foregroundImage,
                        (int) yLoc, (int) xLoc, null);
                if (addClickableFct) {
                    addClickableFunction((int) yLoc, (int) xLoc, foregroundImage.getWidth(), foregroundImage.getHeight(), itemsInDrawFunction.get(cntItemPic).getId(), frame, "item");
                }
            }
        }
    }

    // draw all RoomObjects
    public void drawRoomObjects(JFrame frame, boolean addClickableFct) {
        List<Item> roomObjectsInDrawFunction = getRoomObjects();

        for (int cntItemPic = 0; cntItemPic < roomObjectsInDrawFunction.size(); cntItemPic++) {
            if (this.runtime.checkStep(roomObjectsInDrawFunction.get(cntItemPic).getId(), 'o', 'd')) {
                System.out.println("I want do draw RoomObj " + roomObjectsInDrawFunction.get(cntItemPic).name);
                String itemPicPath = trimmPicPath(roomObjectsInDrawFunction.get(cntItemPic).getPicturePath());
                float xLoc = roomObjectsInDrawFunction.get(cntItemPic).getItemLocY();
                float yLoc = roomObjectsInDrawFunction.get(cntItemPic).getItemLocX();

                BufferedImage foregroundImage = null;
                try {
                    foregroundImage = ImageIO.read(new File(itemPicPath));
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        foregroundImage = ImageIO.read(new File("src/main/resources/Picture/Items/itemid_002.png"));
                    } catch (IOException f) {
                        f.printStackTrace();
                    }
                }
                backgroundImage.getGraphics().drawImage(foregroundImage, (int) yLoc, (int) xLoc, null);
                if (addClickableFct) {
                    addClickableFunction((int) yLoc, (int) xLoc, foregroundImage.getWidth(), foregroundImage.getHeight(), roomObjectsInDrawFunction.get(cntItemPic).getId(), frame, "roomObjects");
                    /*
                     * if(Runtime.checkStep(roomObjectsInDrawFunction.get(cntItemPic
                     * ).getId(), 'o')){
                     * 
                     * } else { System.out.println("You cannot click on item "+
                     * roomObjectsInDrawFunction.get(cntItemPic).getId()); }
                     */
                }
            } else {
                System.out.println("You didn't draw item " + roomObjectsInDrawFunction.get(cntItemPic).getId());
            }
        }
    }

    // set the backgroundimage of the room
    public JLabel setBackgroundImage(JFrame frame) {
        try {
            setBackgroundImage(ImageIO.read(new File(getRoomImagePath())));
        } catch (IOException e1) {
            e1.printStackTrace();
            try {
                setBackgroundImage(ImageIO.read(new File("src/main/resources/Picture/Backgrounds/locationid_002.png")));
            } catch (IOException f) {
                f.printStackTrace();
            }
        }

        BufferedImage backgroundImagetmp = getBackgroundImage();
        JLabel label = new JLabel(new ImageIcon(backgroundImagetmp));
        return label;
    }

    // add clickable layers for the items to interactions
    public void addClickableFunction(final int xLoc, final int yLoc, int width, int height, final int itemId, final JFrame frame, final String type) {
        JLabel label = new JLabel();
        if(type.equalsIgnoreCase("item") || type.equalsIgnoreCase("inventory:next") || type.equalsIgnoreCase("inventory:back") || type.equalsIgnoreCase("inventory:click") || isArrow(itemId)){
            label.setBounds(xLoc, yLoc-15, width, height);
        }else{
            label.setBounds(xLoc, yLoc, width, height);
        }
        
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (!(menueIsOpenFlag == 1)) {
                    if (type.equalsIgnoreCase("inventory")) {
                        System.out.println("You pressed the Inventory");
                        BuildRoom.this.inventory.drawInventory();
                        inventoryIsOpenFlag = 1;
                        frame.repaint();
                    }
                    if (type.equalsIgnoreCase("inventory:close")) {
                        System.out.println("You closed the inventory !!!");
                        SoundPlayer.soundClick();
                        refreshFrame(frame);
                        inventoryIsOpenFlag = 0;
                        BuildRoom.this.inventory.itemsFocussedInInventory = 0;
                    }
                    if (type.equalsIgnoreCase("inventory:next")) {
                        System.out.println("You turned the page!!!");
                        SoundPlayer.soundClick();
                        side = 15;
                        refreshFrame(frame);
                        BuildRoom.this.inventory.itemsFocussedInInventory = 0;
                        BuildRoom.this.inventory.drawInventory();
                        frame.repaint();

                    }
                    if (type.equalsIgnoreCase("inventory:back")) {
                        System.out.println("You turned the page!!!");
                        SoundPlayer.soundClick();
                        side = 0;
                        refreshFrame(frame);
                        BuildRoom.this.inventory.itemsFocussedInInventory = 0;
                        BuildRoom.this.inventory.drawInventory();
                        frame.repaint();

                    }
                    if (type.equalsIgnoreCase("item")) {
                        SoundPlayer.soundClick();
                        if (menueIsOpenFlag == 1) {
                            refreshFrame(frame);
                            frame.repaint();
                            menueIsOpenFlag = 0;
                        } else {
                            System.out.println("You pressed Item " + itemId);
                            drawItemMenue(frame, xLoc, yLoc, itemId, 'i');
                        }
                    }
                    if (type.equalsIgnoreCase("roomObjects")) {
                        if (menueIsOpenFlag == 1) {
                            refreshFrame(frame);
                            frame.repaint();
                            menueIsOpenFlag = 0;
                        } else {
                            System.out.println("You pressed RoomObject " + itemId);
                            drawItemMenue(frame, xLoc, yLoc, itemId, 'r');
                        }
                    }

                    if (type.equalsIgnoreCase("leaveRoom")) {
                        System.out.println("You want to leave ... ? :(");
                        SoundPlayer.soundUseDoor();
                        deleteFrame(frame);
			// Runtime.changeRoom(getRoom().getLocationPointer(),
                        // frame);
                        // TODO: Delete the help variable once the whole game is
                        // finished
                        // Checking whether there is simply a pointer missing in
                        // the XML
                        int help = getRoom().getLocationPointer();
                        if (help == 0) {
                            System.out.println("There is no Locationpointer specified.");
                        } else {
                            if (BuildRoom.this.runtime.checkStep(getRoom().getLocationPointer(), 'r', 'd')) {
                                BuildRoom.this.runtime.initNewRoom(false, getRoom().getLocationPointer(), frame);
                                if (BuildRoom.this.runtime.checkTrigger(help)) {
                                    automaticTrigger(getRoom().getLocationPointer());
                                }
                            }
                        }
                    }

                    if (type.equalsIgnoreCase("inGameMenue")) {
                        System.out.println("InGameMenue");
                        drawInGameMenue(frame);
                    }

                    if (type.equalsIgnoreCase("actor")) {
                        int conv = BuildRoom.this.runtime.chooseConv(actors.get(cnt).getId(), roomId);
                        System.out.println("You NPC: " + actors.get(cnt).getId() + " Your Room: " + roomId + " Your Conversation: " + conv + " Your State: " + BuildRoom.this.runtime.getGameState());
                        dialogOutputOpen = true;
                        DialogOutput dout = new DialogOutput(frame, getParser().getConversationById(conv), getBackgroundImage(), getParser().getListOfActors(), getRoomId(), BuildRoom.this.runtime);
                        int autoItem = BuildRoom.this.runtime.checkAutoItem(conv, 'c', 'c');
                        System.out.println("I want to add item " + autoItem);
                        if (autoItem != 0) {
                            BuildRoom.this.runtime.addItemToInventory(BuildRoom.this.inventory.getItemById(getItems(), autoItem));
                        }
                    }

                    if (type.equalsIgnoreCase("inventory:click")) {
                        SoundPlayer.soundClick();
                        Item itemInInventory = null;
                        itemInInventory = BuildRoom.this.inventory.getItemById(BuildRoom.this.runtime.getInventory(), itemId);
                        if (isWantToCombineRoomObjWithItem()) {
                            BuildRoom.this.inventory.itemAndRoomObjInteraction(itemInInventory);
                            BuildRoom.this.runtime.checkInteraction(roomObj.id, itemInInventory.id);
                        }
                        BuildRoom.this.inventory.focusItemInInventory(itemInInventory);
                    }
                } // check if MenuIsOpenFlag
                // Options for Items
                if (type.equalsIgnoreCase("pickup:itemmenue")) {
                    SoundPlayer.soundClick();
                    // Once the key is picked up, the game will change the state
                    BuildRoom.this.runtime.checkStep(itemId, 'i', 'p');
                    BuildRoom.this.runtime.addItemToInventory(BuildRoom.this.inventory.getItemById(getItems(), itemId));
                    refreshFrame(frame);
                    if (BuildRoom.this.runtime.checkTrigger(roomId)) {
                        automaticTrigger(roomId);
                    }
                    if (itemId == 7) {
                        BuildRoom.this.runtime.addItemToInventory(BuildRoom.this.inventory.getItemById(getItems(), 20));
                        BuildRoom.this.inventory.deleteItem(20);
                    }
                    System.out.println("item not deleted");
                    menueIsOpenFlag = 0;
                }
                if (type.equalsIgnoreCase("inspect:itemmenue")) {
                    SoundPlayer.soundClick();
                    int autoItem = BuildRoom.this.runtime.checkAutoItem(itemId, 'i', 'i');
                    menueIsOpenFlag = 0;
                    refreshFrame(frame);
                    ItemInspect itins = new ItemInspect(frame, backgroundImage, BuildRoom.this.inventory.getItemById(getItems(), itemId), getRoomId(), BuildRoom.this.runtime);

                    if (autoItem != 0) {
                        BuildRoom.this.runtime.addItemToInventory(BuildRoom.this.inventory.getItemById(getItems(), autoItem));
                    }
                    System.out.println("inspect item ..." + itemId);
                }
                if (type.equalsIgnoreCase("leave:item")) {
                    refreshFrame(frame);
                    SoundPlayer.soundClick();
                    menueIsOpenFlag = 0;
                }

                // Options for RoomObjects
                if (type.equalsIgnoreCase("use:RoomObjMenue")) {
                    // checks whether a roomobject may be used.
                    Item item = getRoomObjectById(itemId);
                    SoundPlayer.soundClick();
                    String aimLoc = item.getLocationPointer();
                    aimLoc = aimLoc.substring(11, 14);
                    int aimLocId = Integer.parseInt(aimLoc);
                    System.out.println(aimLocId);
                    menueIsOpenFlag = 0;

                    if (runtime.checkStep(itemId, 'o', 'u')) {
                        if (aimLocId == 0) {
                            System.out.println("There is no Locationpointer specified.");
                        } else {
                            runtime.initNewRoom(false, aimLocId, frame);
                            if (runtime.checkTrigger(aimLocId)) {
                                automaticTrigger(aimLocId);
                            }
                        }
                    } // in case the roomobject may not be used.
                    /**
                     * TODO: Has to be replaced by calling a dialog which states
                     * something about better not passing the door/ going that
                     * way.
                     *
                     */
                    else {
                        System.out.println("You shall not pass.");
                    }
                }

                if (type.equalsIgnoreCase("inspect:RoomObjMenue")) {
                    SoundPlayer.soundClick();
                    int autoItem = BuildRoom.this.runtime.checkAutoItem(itemId, 'r', 'i');
                    menueIsOpenFlag = 0;
                    refreshFrame(frame);
                    ItemInspect itins = new ItemInspect(frame, backgroundImage, BuildRoom.this.inventory.getItemById(getRoomObjects(), itemId), getRoomId(), BuildRoom.this.runtime);
                    if (autoItem != 0) {
                        BuildRoom.this.runtime.addItemToInventory(BuildRoom.this.inventory.getItemById(getItems(), autoItem));
                    }
                    System.out.println("inspect roomObject ..." + itemId);
                    refreshFrame(frame);
                    ItemInspect inspect = new ItemInspect(frame, getBackgroundImage(), BuildRoom.this.inventory.getItemById(getRoomObjects(), itemId), getRoomId(), BuildRoom.this.runtime);
                }

                if (type.equalsIgnoreCase("item:RoomObjMenue")) {
                    SoundPlayer.soundClick();
                    Item roomObj = getRoomObjectById(itemId);
                    setRoomObj(roomObj);
                    BuildRoom.this.inventory.drawInventory();
                    setWantToCombineRoomObjWithItem(true);
                    menueIsOpenFlag = 0;
                }
            }
        });
        frame.add(label);
    }

    public void addRefreshListener(final JFrame frame) {
        JLabel label = new JLabel();
        label.setBounds(0, 0, 800, 600);

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (menueIsOpenFlag == 1) {
                    deleteFrame(frame);
                    JLabel label = setBackgroundImage(frame);

                    menueIsOpenFlag = 0;
                    drawObjects(frame, true);
                    drawRoomObjects(frame, true);
                    BuildRoom.this.inventory.drawInventoryBag();
                    frame.add(label);
                    frame.repaint();
                }
            }
        });
        frame.add(label);
    }

    public static int myRandom(int low, int high) {
        return (int) (Math.random() * (high - low) + low);
    }

    public String trimmPicPath(String picPath) {
        picPath = picPath.trim();
        picPath = picPath.substring(1, picPath.length() - 1);
        return picPath;
    }

    // draw ItemMenue
    public void drawItemMenue(JFrame frame, int xLoc, int yLoc, int itemId, char option) {

        deleteFrame(frame);
        JLabel label = setBackgroundImage(frame);

        drawObjects(frame, false);
        drawRoomObjects(frame, false);
        drawActors(frame);
        this.inventory.drawInventoryBag();

        menueIsOpenFlag = 1;
        if (option == 'i') {
            BufferedImage btnTakeItem = null;
            BufferedImage btnLeaveItem = null;
            BufferedImage btnInspectItem = null;
            try {
                btnTakeItem = ImageIO.read(new File("src/main/resources/Picture/Menue/Itemmenue/btnTakeItem.png"));
                btnLeaveItem = ImageIO.read(new File("src/main/resources/Picture/Menue/Itemmenue/btnLeaveItem.png"));
                btnInspectItem = ImageIO.read(new File("src/main/resources/Picture/Menue/Itemmenue/btnInspectItem.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            backgroundImage.getGraphics().drawImage(btnTakeItem, xLoc - 60, yLoc - 40, 180, 30, null);
            addClickableFunction(xLoc - 60, yLoc - 50, 180, 30, itemId, frame, "pickup:itemmenue");

            backgroundImage.getGraphics().drawImage(btnLeaveItem, xLoc - 60, yLoc + 40, 180, 30, null);
            addClickableFunction(xLoc - 60, yLoc + 40, 180, 30, itemId, frame, "leave:item");

            if (xLoc + 180 < 800) {
                backgroundImage.getGraphics().drawImage(btnInspectItem, xLoc + 80, yLoc, 180, 30, null);
                addClickableFunction(xLoc + 80, yLoc, 180, 30, itemId, frame, "inspect:itemmenue");
            } else {
                backgroundImage.getGraphics().drawImage(btnInspectItem, xLoc - 210, yLoc, 180, 30, null);
                addClickableFunction(xLoc - 210, yLoc, 180, 30, itemId, frame, "inspect:itemmenue");
            }
            addRefreshListener(frame);
            frame.add(label);
            frame.repaint();
        }

        if (option == 'r') {

            BufferedImage btnUseRoomObj = null;
            BufferedImage btnInspectRoomObj = null;
            BufferedImage btnNothingRoomObj = null;
            BufferedImage btnUseItemOnRoomObj = null;
            try {
                btnUseRoomObj = ImageIO.read(new File("src/main/resources/Picture/Menue/Itemmenue/btnUseRoomObj.png"));
                btnInspectRoomObj = ImageIO.read(new File("src/main/resources/Picture/Menue/Itemmenue/btnInspectRoomObj.png"));
                btnNothingRoomObj = ImageIO.read(new File("src/main/resources/Picture/Menue/Itemmenue/btnNothingRoomObj.png"));
                btnUseItemOnRoomObj = ImageIO.read(new File("src/main/resources/Picture/Menue/Itemmenue/btnUseItemOnRoomObj.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Liste f?r RoomObjects, bei denen der Men?unkt : Untersuchen nicht erscheinen soll !
            // Sets correction values to ensure that the button is not outside the frame
            int upsideCorrection = 0;
            int rightsideCorrection = 0;

            if (yLoc + 110 > 600) {
                upsideCorrection = 60;
            }

            if (xLoc + 180 > 800) {
                rightsideCorrection = xLoc + 180 - 800;
            }

            if (this.runtime.checkItemMenue(itemId, 'r', 'i')) {
                backgroundImage.getGraphics().drawImage(btnInspectRoomObj, xLoc - rightsideCorrection, yLoc + 90 - upsideCorrection, 180, 30, null);
                addClickableFunction(xLoc - rightsideCorrection, yLoc + 75 - upsideCorrection, 180, 30, itemId, frame, "inspect:RoomObjMenue");
            }
            if (this.runtime.checkItemMenue(itemId, 'r', 'u')) {
                backgroundImage.getGraphics().drawImage(btnUseRoomObj, xLoc - rightsideCorrection, yLoc - upsideCorrection, 180, 30, null);
                addClickableFunction(xLoc - rightsideCorrection, yLoc - 15 - upsideCorrection, 180, 30, itemId, frame, "use:RoomObjMenue");
            }

            backgroundImage.getGraphics().drawImage(btnNothingRoomObj, xLoc - rightsideCorrection, yLoc + 30 - upsideCorrection, 180, 30, null);
            addClickableFunction(xLoc - rightsideCorrection, yLoc + 15 - upsideCorrection, 180, 30, itemId, frame, "leave:item");

            if (this.runtime.checkItemMenue(itemId, 'r', 'c')) {
                backgroundImage.getGraphics().drawImage(btnUseItemOnRoomObj, xLoc - rightsideCorrection, yLoc + 60 - upsideCorrection, 180, 30, null);
                addClickableFunction(xLoc - rightsideCorrection, yLoc + 45 - upsideCorrection, 180, 30, itemId, frame, "item:RoomObjMenue");
            }
            addRefreshListener(frame);
            frame.add(label);
            frame.repaint();
        }
    }

    // is used to delete and refresh the frame
    public static void deleteFrame(JFrame frame) {
        JPanel contentPane = (JPanel) frame.getContentPane();
        contentPane.removeAll();
        contentPane.revalidate();
        contentPane.repaint();
    }

    // draw InGameMenue 
    public void drawInGameMenue(JFrame frame) {
        SoundPlayer.soundClick();
        deleteFrame(frame);
        JLabel label = setBackgroundImage(frame);

        drawObjects(frame, false);
        drawRoomObjects(frame, false);

        InGameMenue igm = new InGameMenue(frame, backgroundImage, getRoomId(), this.runtime);
        inGameMenueIsOpenFlag = 1;
    }

    public void refreshFrame(JFrame frame) {

        deleteFrame(frame);
        JLabel label = setBackgroundImage(frame);
        validateItemsInRoom();
        drawObjects(frame, true);
        drawRoomObjects(frame, true);
        drawActors(frame);
        this.inventory.drawInventoryBag();
        frame.add(label);
        frame.repaint();
        System.out.println("Your roomObj is " + roomId);
        if (this.runtime.checkTrigger(roomId)) {
            automaticTrigger(roomId);
        }
    }

    public Item getRoomObjectById(int id) {
        Item item = null;

        for (int cnt = 0; cnt < getRoomObjects().size(); cnt++) {
            if (getRoomObjects().get(cnt).getId() == id) {
                item = getRoomObjects().get(cnt);
            }
        }
        return item;
    }
    
    private boolean isArrow(int roomObjectID){
        Item item = getRoomObjectById(roomObjectID);
        if(item != null){
            return item.getName().contains(ARROW);
        }
        return false;
    }
}