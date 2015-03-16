package com.dhbw.Zombiz.logic;

import javax.swing.JFrame;

//import org.junit.Test;

import com.dhbw.Zombiz.gameEngine.logic.Runtime;

import junit.framework.TestCase;

public class RuntimeTest extends TestCase {
	
	/**@Test
         * 
         */
	public void testSaveLoad(){
		//init a new Runtime and set the current Room to 1
		JFrame frame =  new JFrame("Nightmare On Coblitzallee");
		Runtime rt = new Runtime(true, frame);
                rt.initNewRoom(true, 5, frame);
		rt.setCurrRoomId(1);
		assertEquals(1, rt.getCurrRoomId());
		//save the game in the savegame
		rt.saveGame();
		//build a new Runtime and destroy the old values
		rt = new Runtime(true, frame);
                rt.initNewRoom(true, 5, frame);
		//test if the original value is destoryd
		System.out.println(rt.getCurrRoomId());
		assertEquals("Deploying new Runtime",5, rt.getCurrRoomId());
		//load the savefile
		int i = rt.getCurrRoomId();
		System.out.println(i+"/"+rt.getCurrRoomId());
		// test the load retunr value and the old value
		assertEquals("returnvalue",1, i);
		assertEquals("new Current RoomID", 1, rt.getCurrRoomId() );
	}
}
