/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhbw.Zombiz.gameEngine.cheats;

/**
 *
 * @author Stazzer
 */
public class SetGameState extends BasicCheat {
    private final String COMMAND_NAME = "setGameState";
    
    public SetGameState() {
    }
    
    @Override
    public void performAction(String cheatNameString, String[] params, com.dhbw.Zombiz.gameEngine.logic.Runtime runtime){
        super.performAction(COMMAND_NAME, params, runtime);
        if(params.length < 2){
            help();
            return;
        }
        
        runtime.setGameState(Integer.parseInt(params[1]));
        
    }
    
    private void help(){
        System.out.println("HELP: /setGameState <GameState>");
    }
}
