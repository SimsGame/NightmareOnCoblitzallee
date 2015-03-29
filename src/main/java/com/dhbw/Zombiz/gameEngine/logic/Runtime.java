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

import com.dhbw.Zombiz.output.audio.Sound;
import com.dhbw.Zombiz.output.audio.SoundPlayer;
import com.dhbw.Zombiz.output.display.DialogOutput;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.dhbw.Zombiz.output.display.Menu;

/**
 * This Class contains all Runtime Variables, the Inventory, is responsible for
 * saveing and loading the game and starts a new Game.
 *
 * It also decides which gamestate is the current one, decides which
 * conversation should be loaded and which interactions are valid and which one
 * aren't.
 *
 *
 * @author Kira Schomber
 * @author Christoph Schabert
 * @author Jan Brodhaecker
 *
 */
public class Runtime implements Serializable {

    private String savegame = "src/main/resources/savegame.sav";    //path of the Savegame File

	//Runtime Variables
    public static final int START_ROOM_ID = 10;
    public JFrame frame;
    private List<Item> inventory = new ArrayList<Item>();           //List of Items the play have
    private List<Item> remItems = new ArrayList<Item>();
    private int enterdRoomCounter = 0;                              //Counter of how many Rooms have been entered
    private int gameState = 0;                                      //Monitores the flowcontrol
    public int currRoomId = 5;                                      //Gives the current Room ID, mainuse for save and starting a new Game
    private boolean firstConv = true;
    public int conditionCounter = 0;
    public boolean dontDraw = false;
    public boolean receiveYes = true;                               //used as the flag for edward conversation
    public boolean profNotMet = false;                              //Needed if chemestry prof was not met
    public int chosenRoom = 0;
    public boolean helpFlag = true;
    public boolean tutorialFlag = false;
    public boolean toggleFlag = true;
    public int currentChapter = 1;                                  //DEFAULT: 1 - Takes the flow controll according to the current chapter
    public int riddleFlag = 0;                                      //for Dau's riddle 1 for given, 2 for solved, 3 for Dau convinved
    public int helpCounter = 0;
    public boolean burntBook = false;
    public BuildRoom br;

    /**
     * Constructor for a new Game
     *
     * @param newGame 1 for a new game; 0 for load game
     * @param frame	the game Frame
     */
    public Runtime(boolean newGame, JFrame frame) {

        //Hier kommt der Prolog hin ... 
        setCurrRoomId(START_ROOM_ID);               //Deafult 5
        gameState = 2;                              //Default 0 //Use 2 for cheat
        setFrame(frame);
        currRoomId = START_ROOM_ID;
        
        br = new BuildRoom();
        //BuildRoom br = new BuildRoom(currRoomId, frame);
        tutorialFlag = false;                       //DEFAULT: true
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    /**
     * @param id The id of the "thing" to be checked. What that thing is, is
     * determined by the type.
     * @param type To what does the id belong? If further types will be needed
     * (perhaps Conversation or dialog) they can be easily implemented by
     * assigning a char to them
     * @values for type o - RoomObject; i - item; a - NPC; r - Room/Location;
     * @param origin From where was the function called. With this variable it
     * is possible to distinguish what sort of step is being checked
     * @values for origin u - 'Use' [For example if you still want to make
     * interaction with an object possible, but you're not allowed to use it
     * anymore or use it yet] d - 'Draw' [Determines whether a thing should be
     * drawn or not. Helpful for example in the room with Meier, where the
     * Pulley should only be drawn when you're in one certain state.]
     * @return returns whether the step will be granted or denied
     */
    public void resetParams() {
        firstConv = true;
        conditionCounter = 0;
        dontDraw = false;
        receiveYes = true;
        helpFlag = true;
        tutorialFlag = false;
        toggleFlag = true;
        riddleFlag = 0;
        helpCounter = 0;
    }

    public void checkDialog(int dialogId, int convId) {
        if (currentChapter == 1) {
            System.out.println("I checked the Dialogentry/Conversation.");
            switch (convId) {
                case 9:
                case 10:
                    if (dialogId == 16) {
                        chosenRoom = 21;
                    } else if (dialogId == 11) {
                        chosenRoom = 23;
                    }
                    break;
                case 18:
                    if (dialogId == 4) {
                        tutorialFlag = true;
                    } else if (dialogId == 5) {
                        tutorialFlag = false;
                    }
                    break;
                //               case 21: if (dialogId==1)System.out.println("I want to change the profNotMet"); profNotMet=true; break;
                default:
                    break;
            }
        } else if (currentChapter == 2) {
            switch (convId) {
                case 6:
                    if (dialogId == 2) {
                        conditionCounter += 1;
                        if (conditionCounter == 3) {
                            gameState = 3;
                        }
                    }
                    break;
                case 28:
                    if (dialogId == 7) {
                        gameState = 9;
                    } //schlechtes Ende
                    else if (dialogId == 8) {
                        if (!profNotMet && chosenRoom == 23) {
                            gameState = 11; //sehr gutes Ende
                        } else {
                            gameState = 10; //semi-gutes Ende
                        }
                    }
                default:
                    break;
            }
        }
    }

    public void checkInteraction(int roomObjId, int itemId) {
        if (currentChapter == 1) {
            //using key on door
            if (roomObjId == 1 && itemId == 1) {
                gameState = 2;
                firstConv = true;
                helpFlag = true;
                receiveYes = true;
                profNotMet = true;
                toggleFlag = true;
                conditionCounter = 0;
            } //using sponge on beamer
            else if (roomObjId == 8 && itemId == 9) {
                conditionCounter += 1;
                dontDraw = true;
                System.out.println("You combined the sponge and the Beamer. Your Counter is " + conditionCounter);
            } //all conditions for repairing the elevator
            else if ((roomObjId == 11 && itemId == 8) || (roomObjId == 34 && itemId == 14) || (roomObjId == 14 && itemId == 10)) {
                conditionCounter += 1;
                if (roomObjId == 11) {
                    toggleFlag = false;
                }
            }
            //if you get the last note by a combination you change the game state
            if (gameState == 6 && conditionCounter == 3) {
                gameState = 7;
            } //changing the state if the elevator is repaired
            else if (gameState == 8 && conditionCounter == 3) {
                gameState = 9;
                firstConv = true;
            }
        } else if (currentChapter == 2) {
            if (roomObjId == 120 && itemId == 19) {
                // When Henry burns the mathbook. Flag change to trigger dialog!                    
            }
        }
    }

    public int checkItemCombination(int firstItemId, int secondItemId) {
        if(firstItemId == secondItemId) {
            if(br != null && br.inventory != null){
                return Integer.parseInt(br.inventory.getItemFromInventoryById(firstItemId).getContains());
            }
            return 0; 
        }else{
            if(Integer.parseInt(br.inventory.getItemFromInventoryById(firstItemId).getCombinesWith()) == secondItemId){
                switch (firstItemId) {
                    case 120:
                        if (secondItemId == 104) {
                            return 105;
                        }
                        break;
                    case 104:
                        if (secondItemId == 120) {
                            return 105;
                        }
                        break;
                }
            }
        }
        return 0;
    }

    /**
     * @param id The id of the room you want to draw
     * @return It returns a true, when a leave symbol should be drawn in a room
     * and a false, when it shouldn't be drawn.
     */
    public boolean checkLeave(int id) {
        if (currentChapter == 1) {
            if (gameState < 15 && (id == 1 || id == 2 || id == 3 || id == 5 || id == 14 || id == 13 || id == 16 || id == 17 || id == 33)) {
                return false;
            } else if (gameState == 14 && id == 7) {
                return false;
            }
        }
        if (currentChapter == 2) {
            //The two corridors and the not possibility to leave the cafeteria if you haven't convinced all three people yet
            if (id == 109 || id == 110 || (gameState <= 3 && (id == 101 || id == 102 || id == 103))) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param id depends on method and type
     * @param method 'i' = inspect, 'u'= use, 'c'=combine with an item
     * @param type 'i' = item, 'r' = roomObject
     * @return If you return a true, the field of the item menue will be drawn.
     * Meaning if you don't want a roomobject to be usable, you must return
     * false for it's id, origin='u' und type='r'
     */
    public boolean checkItemMenue(int id, char type, char method) {
        if (currentChapter == 1) {
            if (type == 'r') {
                //These are RoomObjects which can't be used.
                if (method == 'u' && (id == 10 || id == 8 || id == 9 || id == 14 || id == 4 || id == 12)) {
                    return false;
                } // 28 to 32 are the arrows used to navigate the floors; they don't need to be inspected because they're not part of Henry's virtual reality.
                else if (method == 'i' && (id >= 28 && id <= 32)) {
                    System.out.println("I decided to not show the option inspect for roomobject " + id);
                    return false;
                } // 28 to 32 are the arrows used to navigate the floors; no item may be used on them.
                else if (method == 'c' && (id >= 28 && id <= 32)) {
                    return false;
                }
            }
        }
        if (currentChapter == 2) {
            if (type == 'r') {
                //These are RoomObjects which can't be used.
                if (method == 'u' && (id == 101 || id == 102 || id == 103 || id == 105 || id == 106 || id == 113 || id == 120)) {
                    return false;
                } // These are the arrows used to navigate the floors; they don't need to be inspected because they're not part of Henry's virtual reality.
                else if (method == 'i' && (id >= 114 && id <= 119 || id == 122 || id == 123)) {
                    System.out.println("I decided to not show the option inspect for roomobject " + id);
                    return false;
                } // These are the arrows used to navigate the floors; no item may be used on them.
                else if (method == 'c' && (id >= 114 && id <= 119 || id == 122 || id == 123)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     *
     * @param id Depends on type and origin
     * @param type What kind of object was checked. o - RoomObject, i - Item, r
     * - Room, a - NPCs (actors)
     * @param origin What sort of action was executed and evoked the checkStep
     * function. i - Inspect, u - Use, d - draw, p - pickup
     * @return If it returns true, the step is allowed. What such a step is
     * depends on the parameters.
     *
     */
    public boolean checkStep(int id, char type, char origin) {
        if (currentChapter == 1) {
            if ((type == 'o' && origin == 'u') || (type == 'r' && origin == 'd')) {
                if (gameState >= 2 && gameState <= 6) {
                    enterdRoomCounter += 1;
                    System.out.println("You entered this number of rooms " + enterdRoomCounter);
                    if (enterdRoomCounter == 20) {
                        gameState = 14;
                    }
                }
            }
                //This is the section where the id of the roomobjects will be checked 
            // Origins: i=Inspect; u=Use; no Origin checked=draw;
            if (type == 'o') {
                if (origin == 'u' && (id == 1 || (id >= 15 && id <= 23) || id == 25 || id == 26 || id == 27 || id == 33)) {
                    SoundPlayer.soundUseDoor(br.muted);
                }
                if (id == 8 && dontDraw) {
                    return false;
                }
                //the prohibitions for the gameStates 2-5 are the same, so it would only result in code duplication to write 4 cases.
                if (gameState == 3 || gameState == 4 || gameState == 5) {
                    if (id == 3 || id == 1 || id == 7 || id == 9) {
                        return false;
                    }
                } else {
                    switch (gameState) {
                        case 0:
                            if (id == 33) {
                                return false;
                            }
                            break;
                        case 1:
                            if (id == 28 || id == 16 || (id == 1 && origin == 'u')) {
                                return false;
                            }
                            if (!helpFlag && toggleFlag) {
                                profNotMet = true;
                                toggleFlag = false;
                            }
                            break;
                        case 2:
                            if (id == 3 || id == 1 || id == 7 || id == 9 | id == 10) {
                                return false;
                            }
                            break;
                        //else if (id==10&&origin=='i') return false; break;
                        case 6:
                            if (id == 3 || id == 1 || id == 7) {
                                return false;
                            }
                            if (id == 10 && origin == 'i' && !toggleFlag) {
                                return false;
                            }
                            ;
                            break;
                        case 7:
                            if (id == 3 || id == 1 || id == 7) {
                                return false;
                            }
                            if (id == 10 && origin == 'i' && !toggleFlag) {
                                return false;
                            }
                            ;
                            break;
                        case 8:
                            if (id == 1 || id == 7 || id == 35 || id == 19) {
                                return false;
                            }
                            if (id == 26 && profNotMet) {
                                return false;
                            }
                            if (!toggleFlag && id == 11) {
                                return false;
                            }
                            if (origin == 'u') {
                                if (id == 26) {
                                    firstConv = true;
                                }
                                if (id == 16 || id == 15 || id == 17) {
                                    return false;
                                }
                                if (chosenRoom == 0 && id == 21) {
                                    chosenRoom = 21;
                                    System.out.println("You chose room " + chosenRoom);
                                } else if (chosenRoom == 0 && id == 23) {
                                    chosenRoom = 23;
                                    System.out.println("You chose room " + chosenRoom);
                                }
                                if (chosenRoom != 0 && (id == 21 || id == 23)) {
                                    if (id != chosenRoom) {
                                        return false;
                                    }
                                }
                            }
                            break;
                        case 9:
                            if ((id >= 20 && id <= 24) || id == 32) {
                                return false;
                            }
                            if (id == 27 && origin == 'u') {
                                System.out.println("You left Chapter 1");
                                currentChapter = 2;
                                resetParams(); //All necessary parameters such as flags will be set to the starting value                                    
                                gameState = 0;
                                currRoomId = 101;
                            }
                            break;
                        case 14:
                            if (origin == 'u' && !(id == 29 || id == 16 || id == 7)) {
                                return false;
                            }
                            if (id == 14) {
                                firstConv = true;
                            }
                            break;
                        default:
                            return true;
                    }
                }
                if ((id == 1 || id == 2 || id == 3 || (id >= 15 && id <= 35)) && origin == 'i') {
                    return false;
                }
            } //This is the section where the id of the items will be checked
            // 'p'=pickup; 'd'=draw             
            else if (type == 'i') {
                if (id == 1 && origin == 'p') {
                    gameState = 1;
                    helpFlag = true;
                    firstConv = true;
                } //all the items that are contained in a room so that they can be added have to be added here!
                else if (origin == 'd' && (id == 16 || id == 2 || id == 3 || id == 4 || id == 8 || id == 10 || id == 11 || id == 17 || id == 19)) {
                    return false;
                }
            } //This is the section where the id of the actors is checked
            else if (type == 'a') {
                System.out.println("You npc is " + id + " and this is the first conv " + firstConv);
                if (id == 6 && !firstConv || id == 14) {
                    return false;
                }
            } //This is the section where the id of the rooms will be checked                
            else if (type == 'r') {
                switch (gameState) {
                    case 8:
                        if (id == 12) {
                            chosenRoom = 21;
                            System.out.println("You chose room " + chosenRoom);
                        } else if (id == 11) {
                            chosenRoom = 23;
                            System.out.println("You chose room " + chosenRoom);
                        }
                        break;
                    default:
                        return true;

                }
            }
        }
        if (currentChapter == 2) {
                //This is the section where the id of the roomobjects will be checked 
            // Origins: i=Inspect; u=Use; no Origin checked=draw;
            if (type == 'o') {
                if ((id == 1 || id == 2 || id == 3 || (id >= 15 && id <= 35)) && origin == 'i') {
                    return false;
                }
                switch (gameState) {
                    case 0:
                        if (id == 33) {
                            return false;
                        }
                    case 7:
                        if (id != 109 && id != 110) {
                            return false;
                        }
                    default:
                        return true;
                }
            } //This is the section where the id of the items will be checked
            // 'p'=pickup; 'd'= draw
            /**
             * TODO: Add the contained Items
             */
            else if (type == 'i') {
                //all the items that are contained in a room so that they can be added have to be added here!
                if (origin == 'd' && (id >= 101 && id <= 103 || id == 105 || id == 107 || id >= 110 && id <= 112 || id == 114 || id >= 116 && id <= 121)) {
                    return false;
                }
            } //This is the section where the id of the actors is checked
            else if (type == 'a') {
                if (id == 14) {
                    return false;
                }
                if (id == 15 && profNotMet) {
                    return false;
                }
                if (gameState != 7 && (id >= 19 && id <= 25)) {
                    return false;
                }
            } //This is the section where the id of the rooms will be checked
            //l - means the 'leave' symbol on the hud
            //
            else if (type == 'r') {
                //if (gameState==14&&id==7) return false;
                switch (gameState) {
                    case 6:
                        if (id == 109) {
                            gameState = 7;
                        }
                    default:
                        return true;

                }
            }

        }
        return true;
    }

    /**
     * @param id The id of the room in which the trigger is checked.
     * @return Whether a dialog should be displayed automatically (return true)
     * or not (return false).
     */
    public boolean checkTrigger(int id) {
        if (currentChapter == 1) {
            System.out.println("There was a checking of the trigger with the gameState " + gameState + " and the id " + id);
            switch (gameState) {
                case 0:
                    if (firstConv && tutorialFlag) {
                        System.out.println("This is the first automatical dialog.");
                        BuildRoom.convStatic = 18;
                        firstConv = false;
                        tutorialFlag = false;
                        return true;
                    }
                    ;
                    break;
                case 1:
                    System.out.println("Gamestate 1 with helpFlag " + helpFlag + " and firstConv " + firstConv + " and tutorialFlag " + tutorialFlag + " and receiveYes " + receiveYes + " and ProfessorNotMet " + profNotMet);
                    if (helpFlag && tutorialFlag) {
                        System.out.println("Now I was called.");
                        BuildRoom.convStatic = 21;
                        helpFlag = false;
                        return true;
                    } else if (tutorialFlag && profNotMet && id == 5) {
                        BuildRoom.convStatic = 19;
                        profNotMet = false;
                        return true;
                    } else if (id == 1 && receiveYes) {
                        BuildRoom.convStatic = 22;
                        firstConv = true;
                        receiveYes = false;
                        return true;
                    } else if (id == 1 && tutorialFlag && firstConv) {
                        System.out.println("Tutorial part 2");
                        BuildRoom.convStatic = 20;
                        firstConv = false;
                        return true;
                    }
                    ;
                    break;
                case 2:
                    if (id == 1 && firstConv) {
                        System.out.println("Yout want to automatically display the Henry monoloque.");
                        BuildRoom.convStatic = 1;
                        firstConv = false;
                        return true;
                    }
                    ;
                    break;
                case 3:
                    if (id == 1 && firstConv) {
                        System.out.println("I changed the trigger. ");
                        BuildRoom.convStatic = 4;
                        gameState = 4;
                        return true;
                    }
                    break;
                case 9:
                    if (firstConv) {
                        System.out.println("I want to display the elevator dialog.");
                        BuildRoom.convStatic = 16;
                        firstConv = false;
                        return true;
                    }
                    break;
                case 14:
                    if (id == 7 && helpFlag) {
                        System.out.println("I wanted to automatically display meier conv.");
                        BuildRoom.convStatic = 17;
                        helpFlag = false;
                        return true;
                    } else if (id == 10) {
                        System.out.println("I wanted to automatically display janitor conv.");
                        BuildRoom.convStatic = 9;
                        firstConv = false;
                        gameState = 8;
                        toggleFlag = true;
                        helpFlag = true;
                        return true;
                    } else if (firstConv) {
                        BuildRoom.convStatic = 15;
                        firstConv = false;
                        return true;
                    }
                    break;
                default:
                    return false;
            }
        }
        if (currentChapter == 2) {
            if (id == 101 && burntBook) {
                BuildRoom.convStatic = 4;
                burntBook = false;
                gameState = 2;
                helpFlag = false;
            };
            switch (gameState) {
                case 0:
                    if (id == 101) {
                        BuildRoom.convStatic = 1;
                        gameState = 1;
                        conditionCounter = 0;
                        return true;
                    } //Anfangsdialog getriggert
                case 4:
                    if (helpCounter == 2) {
                        BuildRoom.convStatic = 27;
                        helpCounter = 0;
                        return true;
                    } // Display the desoldering dialog as soon as you have all radioparts                                                                
                case 6:
                    if (conditionCounter == 10) {
                        BuildRoom.convStatic = 26;
                        if (!profNotMet) {
                            gameState = 8;
                        }
                        conditionCounter = 0;
                    }
                case 7:
                    if (conditionCounter == 0) {
                        BuildRoom.convStatic = 22;
                        conditionCounter = 0;
                    }
                    break;
                case 8:
                    BuildRoom.convStatic = 23;
                    gameState = 6;
            }
        }
        System.out.println("I have this state " + gameState + " and I am the firstConv " + firstConv);
        return false;
    }

    //possibility for 'you shall not pass' replacement: actorId 0 stands for automatically called dialog, ergo a monologue displayed when doing something
    public int chooseConv(int actorId, int roomId) {
        int conv = 14;
        System.out.println("I am the first Conversation " + firstConv);
        if (currentChapter == 1) {
            switch (roomId) {
                case 2:
                    if (receiveYes) {
                        conv = 12;
                    } else {
                        conv = 13;
                    }
                    break;
                case 7:
                    if (gameState == 2) {
                        conv = 2;
                        gameState = 3;
                        firstConv = true;
                    } else if (gameState == 3 || gameState == 4) {
                        conv = 3;
                    } else if (gameState == 5) {
                        conv = 5;
                        gameState = 6;
                        remItemFromInventory(11);
                    } else if (gameState == 7) {
                        conv = 7;
                        gameState = 8;
                        toggleFlag = true;
                        conditionCounter = 0;
                        firstConv = true;
                        remItemFromInventory(3);
                        remItemFromInventory(15);
                        remItemFromInventory(16);
                        remItemFromInventory(17);
                    }
                    ;
                    break;
                case 14:
                    if (firstConv) {
                        conv = 8;
                        profNotMet = false;
                        firstConv = false;
                    }
                    ;
                    break;
                case 10:
                    if (firstConv && gameState == 8) {
                        conv = 10;
                        firstConv = false;
                    } else {
                        conv = 11;
                    }
                    break;
            }
        } else if (currentChapter == 2) {
            if (gameState != 6) {
                switch (actorId) {
                    case 8:
                        if (gameState < 3) {
                            conv = 2;
                        } else if (gameState == 3) {
                            conv = 19;
                        } else if (gameState == 4) {
                            conv = 20;
                        } else if (gameState == 5) {
                            conv = 21;
                            gameState = 6;
                            conditionCounter = 10;
                        }
                        break;     //Getting the list from Sonnenberg, and giving it back to him once everythings collected
                    case 9:
                        if (toggleFlag) {
                            conv = 6;
                            toggleFlag = false;
                        }
                        break;      // Conversation with Stefanie Schneider                        
                    case 10:
                        if (profNotMet && !dontDraw) {
                            conv = 10;
                            dontDraw = true;
                            break;
                        } else if (!dontDraw) {
                            conv = 11;
                            conditionCounter += 1;
                            if (conditionCounter == 3) {
                                gameState = 3;
                            }
                            dontDraw = true;
                            break;
                        }
                        break;      //The Conversation with Heinemann, depending on whether you have met Dr. Schwartz or haven't                        
                    case 12:
                        if (firstConv) {
                            conv = 3;
                            firstConv = false;
                        } else if (!helpFlag && riddleFlag == 1) {
                            conv = 5;
                            riddleFlag = 2;
                            conditionCounter += 1;
                            if (conditionCounter == 3) {
                                gameState = 3;
                            }
                            helpFlag = true;
                        }
                        break; // Jonas Conversations, excluding automatically triggered burn book conversation
                    case 16:
                        conv = 25;
                        break;    // Hint to wake the secretary up
                    case 17:
                        if (!tutorialFlag) {
                            conv = 12;
                            tutorialFlag = true;
                        }
                        break;    //Conversation with Julian Bashir
                    case 18:
                        if (receiveYes) {
                            conv = 18;
                            receiveYes = false;
                        }
                        break;    // Getting the cardgame from the secretary
                    case 11:
                        if (riddleFlag == 0) {
                            conv = 7;
                            riddleFlag = 1;
                        } //The different stages of Dau's riddle
                        else if (riddleFlag == 1) {
                            conv = 8;
                        } else if (riddleFlag == 2) {
                            conv = 9;
                            conditionCounter += 1;
                            if (conditionCounter == 3) {
                                gameState = 3;
                            }
                            riddleFlag = 3;
                        }
                        break;
                    case 19:
                        if (gameState == 7) {
                            conv = 28;
                        }
                        break;
                    default:
                        conv = 14;
                        break;
                }
            }
        }
        return conv;
    }

    public int checkAutoItem(int id, char type, char origin) {
        int itemId = 0;
            //System.out.println("In der RemListe ist: "+);
        //origin C=Conversation; r=Combination; m=multiple; i=Inspection; in this case the id of the roomObject is contained in id
        // type c=conversation; i=item; r=roomObject
        //during a conversation you receive an item
        if (currentChapter == 1) {
            if (origin == 'c') {
                switch (id) {
                    case 2:
                        itemId = 16;
                        break;
                    case 5:
                        itemId = 10;
                        break;
                    case 12:
                        if (receiveYes) {
                            itemId = 8;
                            receiveYes = false;
                        }
                        break;
                    case 17:
                        itemId = 10;
                        break;
                }
            } // for adding an item as a result of a combination
            else if (origin == 'r') {
                switch (id) {
                    case 8:
                        itemId = 3;
                        conditionCounter += 1;
                        if (conditionCounter == 3) {
                            gameState = 7;
                        }
                        break;
                }
            } //when you inspect an RoomObject and find an Item in it
            else if (origin == 'i') {
                //you inspect a roomObj
                if (type == 'r') {
                    switch (id) {
                        case 4:
                            if (!inRemItems(4)) {
                                itemId = 4;
                            }
                            break;
                        case 9:
                            if (!inRemItems(17)) {
                                itemId = 17;
                                conditionCounter += 1;
                                if (conditionCounter == 3) {
                                    gameState = 7;
                                }
                            }
                            break;
                        case 10:
                            if (!inRemItems(11)) {
                                itemId = 11;
                                gameState = 5;
                                toggleFlag = false;
                            }
                            break;
                        case 12:
                            if (!inRemItems(19)) {
                                itemId = 19;
                            }
                            break;
                        default:
                            break;
                    }
                } else if (type == 'i') {
                    System.out.println("You want to find something and inspected item " + id);
                }
            }
        } else if (currentChapter == 2) {
            System.out.println("Checking under the second chapter.");
            //this is the section for items received during a conversation; id stands for Conversation ID in this case
            if (origin == 'c') {
                switch (id) {
                    case 4:
                        itemId = 120;
                        break;
                    case 7:
                        itemId = 101;
                        break;
                    case 19:
                        itemId = 102;
                        break;
                    case 18:
                        itemId = 107;
                        break;
                    case 23:
                        itemId = 118;
                        break;
                    case 27:
                        itemId = 121;
                        break;
                }
            } //this is the section about inspection
            else if (origin == 'i') {
                //inspecting a roomObj
                System.out.println("Es wurde ein roomobj inspected");
                if (type == 'r') {
                    switch (id) {
                        case 101:
                            itemId = -1;
                            break;
                        case 105:
                            System.out.println("I recognized that you want to search desoldering tools");
                            if (!inRemItems(111)) {
                                System.out.println("Ich will die platine hinzuf?gen.");
                            }
                            itemId = 111;
                            helpCounter += 1;
                            break;
                        default:
                            break;
                    }
                }
            } //this is the section where the combination is processed; it always is about the roomObjid
            else if (origin == 'r') {
                switch (id) {
                    case 103:
                        itemId = 114;
                        break;            // When combining the empty mug and the baisin you receive the full mug.                                                                 
                    case 102:
                        itemId = 119;
                        break;            // When using the printer with empty paper, you get a random copy.
                    case 106:
                        itemId = 112;
                        conditionCounter += 1;
                        break;            // Combining the radioparts and the desoldering tool
                    case 120:
                        burntBook = true;
                        break;
                    default:
                        break;
                }
            } //this is the section where more than one item is added to the inventory
            else if (origin == 'm') {
                if (type == 'r') {
                    if (id == 101 && helpCounter == 2) {
                        itemId = 117;
                        helpCounter--;
                    } else if (id == 101 && helpCounter == 1) {
                        itemId = 103;
                        helpCounter--;
                    }
                }
            }
        }
        return itemId;
    }

    public int checkNumber(int roomObjId) {
        if (roomObjId == 101) {
            helpCounter = 2;
            return 2;
        }
        return 1;
    }

    public boolean inRemItems(int itemId) {
        boolean contained = false;
        for (int cnt = 0; cnt < remItems.size(); cnt++) {
            if (remItems.get(cnt).getId() == itemId) {
                contained = true;
            }
        }
        return contained;
    }

    /**
     * saves all Runtime Variables and the current Room the player is in into
     * the savefile
     *
     */
    public void saveGame() {

        try {
            FileOutputStream saveFile = new FileOutputStream(savegame);
            ObjectOutputStream save = new ObjectOutputStream(saveFile);

            //save the Runtime Data			
            save.writeObject(remItems);
            save.writeObject(gameState);
            save.writeObject(inventory);
            save.writeObject(currRoomId);
            save.writeObject(tutorialFlag);
            save.writeObject(helpFlag);
            save.writeObject(receiveYes);
            save.writeObject(firstConv);
            save.writeObject(enterdRoomCounter);
            save.writeObject(conditionCounter);
            save.writeObject(dontDraw);
            save.writeObject(profNotMet);
            save.writeObject(chosenRoom);
            save.writeObject(toggleFlag);
            save.writeObject(currentChapter);
            save.writeObject(br.muted);
            // Close the file.
            save.close();
        } catch (Exception exc) {
            System.out.println("Unable to Save game");
            exc.printStackTrace(); // If there was an error, print the info.
        }
    }

    /**
     * loads the game, all saved Runtime variables are restored and the last
     * room is returned.
     *
     * if there is no Save File or the Game is unable to load it a new Game is
     * started.
     *
     * @return return the last Room the player was in
     */
    public void loadGame() {
        System.out.println("Load Game?");
        try {
            FileInputStream saveFile = new FileInputStream(savegame);

            ObjectInputStream save = new ObjectInputStream(saveFile);

			// Load the Objects into the Runtime
            remItems = (List<Item>) save.readObject();
            gameState = (Integer) save.readObject();
            inventory = (List<Item>) save.readObject();
            currRoomId = (Integer) save.readObject();
            tutorialFlag = (Boolean) save.readObject();
            helpFlag = (Boolean) save.readObject();
            receiveYes = (Boolean) save.readObject();
            firstConv = (Boolean) save.readObject();
            enterdRoomCounter = (Integer) save.readObject();
            conditionCounter = (Integer) save.readObject();
            dontDraw = (Boolean) save.readObject();
            profNotMet = (Boolean) save.readObject();
            chosenRoom = (Integer) save.readObject();
            toggleFlag = (Boolean) save.readObject();
            currentChapter = (Integer) save.readObject();
            br.muted = (Boolean) save.readObject();

            // Close the file.
            save.close();
        } catch (Exception exc) {
            System.out.println("Unable to load Savegame");
            exc.printStackTrace();

        }
        setCurrRoomId(currRoomId);
        initNewRoom(false, currRoomId, frame);
    }

    /**
     * Adds a item into the Inventory
     *
     * @param item the item to add into the Inventory
     */
    public void addItemToInventory(Item item) {
        inventory.add(item);
        remItems.add(item);
        System.out.println("I added to inventory Item " + item.getName());
    }

    public void addToRemItems(Item item) {
        remItems.add(item);
        System.out.println("I added to remlist " + item.getName());
    }

    /**
     * removes a item from the Inventory
     *
     * @param item the item to remove
     */
    public void remItemFromInventory(int item) {
        int remIndex = 0;
        for (int cnt = 0; cnt < inventory.size(); cnt++) {
            int indexInInventory = inventory.get(cnt).getId();
            if (item == indexInInventory) {
                remIndex = cnt;
            }
        }
        inventory.remove(remIndex);
    }

    /**
     * returns the current Inventory List
     *
     * @return a List<Item>
     */
    public List<Item> getInventory() {
        return inventory;
    }

    /**
     * replace the Current inventory
     *
     * @param inventory the new Inventory
     */
    public void setInventory(List<Item> inventory) {
        inventory = inventory;
    }

    public List<Item> getRemItems() {
        return remItems;
    }

    public void setRemItems(List<Item> remItems) {
        this.remItems = remItems;
    }

    /**
     * Returns the current Room ID
     *
     * @return current Room ID
     */
    public int getCurrRoomId() {
        return currRoomId;
    }

    /**
     * set a new current Room ID
     *
     * @param currRoomId new current Room ID
     */
    public void setCurrRoomId(int currRoomId) {
        this.currRoomId = currRoomId;
    }

    /**
     * returns the Game State
     *
     * @return Game state
     */
    public int getGameState() {
        return gameState;
    }

    /**
     * set a new Game State
     *
     * @param gameState the new Game State
     */
    public void setGameState(int gameState) {
        this.gameState = gameState;
    }

    public boolean isFirstConv() {
        return firstConv;
    }

    public void setFirstConv(boolean firstConv) {
        this.firstConv = firstConv;
    }

    public void initNewRoom(boolean isNewGame, int roomId, JFrame frame){
        br.init(isNewGame, roomId, frame, this);
    }
}
