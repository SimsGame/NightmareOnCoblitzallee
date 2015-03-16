/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhbw.Zombiz.gameEngine.cheats;

import com.dhbw.Zombiz.gameEngine.logic.BuildRoom;
import com.dhbw.Zombiz.gameEngine.logic.Item;

/**
 *
 * @author Stazzer
 */
public class GotoRoom extends BasicCheat{
    private final String COMMAND_NAME = "gotoRoom";

    public GotoRoom() {
    }
    
    @Override
    public void performAction(String cheatNameString, String[] params, com.dhbw.Zombiz.gameEngine.logic.Runtime runtime){
        super.performAction(COMMAND_NAME, params, runtime);
        if(params.length < 2){
            help();
            return;
        }
        
        runtime.initNewRoom(false, Integer.parseInt(params[1]), runtime.getFrame());
        runtime.getFrame().revalidate();
        runtime.getFrame().repaint();
        
    }
    
    private void help(){
        System.out.println("HELP: /gotoRoom <roomID>");
    }
}
