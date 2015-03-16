/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhbw.Zombiz.gameEngine.cheats;

import com.dhbw.Zombiz.gameEngine.logic.Runtime;

/**
 *
 * @author Stazzer
 */
public class BasicCheat {
    public BasicCheat(){
        
    }
    
    public void performAction(String cheatNameString, String[] params, Runtime runtime){
        if(!cheatNameString.equals(params[0])){
            return;
        }
    }
}
