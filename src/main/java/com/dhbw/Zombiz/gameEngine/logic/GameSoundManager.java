/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dhbw.Zombiz.gameEngine.logic;

import com.dhbw.Zombiz.output.audio.Sound;

/**
 *
 * @author Stazzer
 */
public final class GameSoundManager {
    
    public static Thread ts;
    private static Sound s;
    
    private GameSoundManager(){
    }
    
    public static void createSound() {
        s = new Sound("Background");
        s.setRepeat(true);
        ts = new Thread(s, "Background Sound");
    }

    public static void stopSound() {
        s.stop();
    }
}
