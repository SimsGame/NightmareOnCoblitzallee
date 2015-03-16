/*******************************************************************************
 * Copyright (c) 2013 DHBW.
 * This source is subject to the DHBW Permissive License.
 * Please see the License.txt file for more information.
 * All other rights reserved.
 * 
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY 
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
 * PARTICULAR PURPOSE.
 * 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 *Project: Zombiz
 *Package: com.dhbw.zombiz
 ********************************************************************************/
package com.dhbw.Zombiz.output.display;



import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.x.XFullScreenStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.dhbw.Zombiz.output.audio.Sound;
import com.dhbw.Zombiz.output.audio.SoundPlayer;
import com.sun.jna.NativeLibrary;


/**
 * Plays the prolog Video (and should play other videos also ...)
 * 
 * Using the VLCj Framework to play the video in a new frame. the game frame is disabled and will be enabled after the video is finished.
 * 
 * 
 * 
 * @author Jan Brodhaecker
 * 
 */
public class Video{

	 private static final String[] ARGS = {//"-vvv",
		 "--no-plugins-cache",
	      "--no-video-title-show",
	      "--no-snapshot-preview",
	      "--quiet",
	      "--quiet-synchro",
	      "--intf",
	      ":no-video-title-show",
	      "dummy"};
	
	public Video(final JFrame frame){
	
		
		SoundPlayer.stopSound();
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), System.getProperty("user.dir") + "\\lib\\");
		System.out.println("PATH !!!! :"+ System.getProperty("user.dir") + "\\lib\\");
		
		
		final JFrame videoFrame = new JFrame();
        videoFrame.setSize(800,600);
        videoFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        videoFrame.setResizable(false);
        videoFrame.setUndecorated(true);
        
        
        
        videoFrame.setAlwaysOnTop(true);
        videoFrame.setLocation((frame.getLocationOnScreen()));
        
        frame.setVisible(false);
		   
		    final EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent() {
				   protected String[] onGetMediaPlayerFactoryArgs() {
				     return new String[] {""};
				   }
				 
				   protected FullScreenStrategy onGetFullScreenStrategy() {
				     return new XFullScreenStrategy(videoFrame);
				   }
				   
				   public void videoOutputAvailable(MediaPlayer mediaPlayer, boolean videoOutput) {
				   }

				   public void error(MediaPlayer mediaPlayer) {
				   }
				   
				   public void finished(MediaPlayer mediaPlayer) {
					
					   
					   videoFrame.dispose();
					   frame.setVisible(true);
					   
					   
					   
					   Thread ts;
					   Sound s = new Sound("Background");
					   s.setRepeat(true);
					   ts = new Thread(s,"Background Sound");
					   ts.start();
					   
				   }
				 };
		    
		    
		    
		    
		    
		    
		    
		  
				 videoFrame.setIconImage(new ImageIcon(Video.class.getResource("")).getImage());
				 videoFrame.setSize(800, 600);
				 videoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		   /* frame.addWindowListener(new WindowAdapter() {
		      @Override
		      public void windowClosing(WindowEvent e) {
		        mediaPlayerComponent.release();
		      }
		    });*/
				 videoFrame.setContentPane(mediaPlayerComponent);
				 videoFrame.setVisible(true);

				 mediaPlayerComponent.getMediaPlayer().playMedia("src/main/resources/Video/prolog.mp4");
				 
				 	
		    
		    
		  /*  try {
				Thread.currentThread().join();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} */
		    
		    
		    
		    
		   
		
	}
	
	

}
