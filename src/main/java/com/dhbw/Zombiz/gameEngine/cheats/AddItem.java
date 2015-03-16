/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhbw.Zombiz.gameEngine.cheats;

import com.dhbw.Zombiz.gameEngine.logic.BuildRoom;
import com.dhbw.Zombiz.gameEngine.logic.Item;
import com.dhbw.Zombiz.gameEngine.logic.Runtime;

/**
 *
 * @author Stazzer
 */
public class AddItem extends BasicCheat{
    
    private final String COMMAND_NAME = "addItem";

    public AddItem() {
    }
    
    @Override
    public void performAction(String cheatNameString, String[] params, Runtime runtime){
        super.performAction(COMMAND_NAME, params, runtime);
        if(params.length < 2){
            help();
            return;
        }
         
        Item item = BuildRoom.getParser().getPickableItemById(Integer.parseInt(params[1]));
        if(item != null){
            runtime.addItemToInventory(item);
            System.out.println("Item to add: " + item.getName());
        }else{
            System.out.println("There is no item with id: " + params[1]);
        }
    }
    
    private void help(){
        System.out.println("HELP: /addItem <itemID>");
    }
}
