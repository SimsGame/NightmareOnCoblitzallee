package com.dhbw.Zombiz.output.display;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;



import javax.media.CannotRealizeException;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.x.XFullScreenStrategy;

import com.dhbw.Zombiz.gameEngine.logic.BuildRoom;
import com.dhbw.Zombiz.gameEngine.logic.Runtime;
import com.dhbw.Zombiz.logic.*;

 
public class Video{

	/**
	 * @param args
	 */
	 private static final String[] ARGS = {//"-vvv",
	        "--ignore-config",
	        "-I dummy",
	        "-V opengl"};
	
	public Video(final JFrame frame){
	
		NativeLibrary.addSearchPath("vlc", "/Applications/VLC.app/Contents/MacOS/lib");

		final JFrame videoFrame = new JFrame();
        videoFrame.setSize(800,600);
        videoFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        videoFrame.setResizable(false);
        
        videoFrame.setAlwaysOnTop(true);
        videoFrame.setLocation((frame.getLocationOnScreen()));
        
        frame.setVisible(false);
		   
		    final EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent() {
				   protected String[] onGetMediaPlayerFactoryArgs() {
				     return new String[] {""};
				   }
				 
				   protected FullScreenStrategy onGetFullScreenStrategy() {
				     return new XFullScreenStrategy(frame);
				   }
				   
				   public void videoOutputAvailable(MediaPlayer mediaPlayer, boolean videoOutput) {
				   }

				   public void error(MediaPlayer mediaPlayer) {
				   }
				   
				   public void finished(MediaPlayer mediaPlayer) {
					   System.out.println("Video finished !");
					   
					   videoFrame.dispose();
					   frame.setVisible(true);
					   
					   
				       
				     
				   
				    
				       
				       
				       
				

					   
					   	
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
