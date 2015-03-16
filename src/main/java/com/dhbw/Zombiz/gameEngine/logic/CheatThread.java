/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dhbw.Zombiz.gameEngine.logic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Scanner;

/**
 *
 * @author Stazzer
 */
public class CheatThread implements Runnable{

    static BufferedReader in ;
    private final Runtime runtime;
    private final String CHEAT_START_SIGN = "/";
    private final String CHEAT_PACKAGE_PATH = "com.dhbw.Zombiz.gameEngine.cheats.";
    
    public CheatThread(Runtime runtime){
        this.runtime = runtime;
        in = new BufferedReader(new InputStreamReader(System.in));
    }
    
    public void run(){
        String msg = null;
        while(true){
            try{
            msg=in.readLine();
            }catch(Exception e){}
            if(msg != null && msg.equals("Q")) {System.out.println("Closing cheat console"); break;}
            else if(msg != null){
                if(msg.indexOf(CHEAT_START_SIGN) == 0){
                    String[] params = msg.split(" ");
                    String className = params[0].substring(1);
                    className = className.substring(0,1).toUpperCase() + className.substring(1); 
                    Class<?> clazz = null;
                    try{
                        clazz = Class.forName(CHEAT_PACKAGE_PATH + className);
                    }catch(NoClassDefFoundError e){
                        System.out.println("There is no such cheat class! Please ask a developer.");
                    }catch(ClassNotFoundException e){
                        System.out.println("There is no such cheat class! Please ask a developer.");
                    }
                    try {
                        if(clazz != null){
                            Constructor<?> constructor = clazz.getConstructor();
                            Object instanceOfCheatClass = constructor.newInstance();
                            Method performActionMethod = clazz.getDeclaredMethod("performAction", String.class, String[].class, Runtime.class);
                            Object result = performActionMethod.invoke(instanceOfCheatClass, className, params, runtime);
                        }
                    } catch (Exception e) {
                        System.out.println("There is no such cheat class! Please ask a developer.");
                    }
                }
            }
        }
    }
    
}
