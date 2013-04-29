package com.dhbw.Zombiz.output.display;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.dhbw.Zombiz.gameEngine.logic.Actor;
import com.dhbw.Zombiz.gameEngine.logic.BuildRoom;
import com.dhbw.Zombiz.gameEngine.logic.Conversation;
import com.dhbw.Zombiz.gameEngine.logic.DialogEntry;
import com.dhbw.Zombiz.gameEngine.logic.Item;
import com.dhbw.Zombiz.gameEngine.logic.Runtime;





public class ItemInspect {

	
	public int rooRoomId;
	public boolean bigPicIsAvailable;
	public String itemBackground;
	
	
	public String getItemBackground() {
		return itemBackground;
	}

	public void setItemBackground(String itemBackground) {
		this.itemBackground = itemBackground;
	}

	public boolean isBigPicIsAvailable() {
		return bigPicIsAvailable;
	}

	public void setBigPicIsAvailable(boolean bigPicIsAvailable) {
		this.bigPicIsAvailable = bigPicIsAvailable;
	}

	public int getRooRoomId() {
		return rooRoomId;
	}

	public void setRooRoomId(int rooRoomId) {
		this.rooRoomId = rooRoomId;
	}




	public ItemInspect(JFrame frame, BufferedImage backgroundImage, Item item, int rootRoomId){
		setRooRoomId(rootRoomId);
		
		if(checkIfBigPicIsAvailable(item)){
			setBigPicIsAvailable(true);
		}
		else{
			setBigPicIsAvailable(false);
		}
		
		JTextArea dialog =  getItemDescription(frame, item);
		frame.add(dialog);
		drawItemInspectBackground(frame, backgroundImage);
		
		
	}
	
	public boolean checkIfBigPicIsAvailable(Item item){
		String picPath = item.getPicturePath();
		picPath = picPath.trim();
		picPath = picPath.substring(1, picPath.length() - 1);
		picPath = picPath.replace(".png", "");
		picPath = picPath + "_big.png";
		
		File f = new File(picPath);
		
		
		
		if(f.exists()){
			setItemBackground(picPath);
			return true;
		}
		else {
			return false;
		}
	}
	
	
	public void drawItemInspectBackground(JFrame frame, BufferedImage backgroundImage){
		BufferedImage bigItemPic = null; 
		if(isBigPicIsAvailable()){
			try {
				bigItemPic = ImageIO.read(new File(getItemBackground()));
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			backgroundImage.getGraphics().drawImage(bigItemPic, 100, 100, 100,100, null);
		}
		
		
		
		BufferedImage dialogBackground = null; 
		BufferedImage closeInspection	= null;
		try {
			dialogBackground = ImageIO.read(new File("src/main/resources/Picture/DialogBackgrounds/dialogGround.png"));
			closeInspection = ImageIO.read(new File("src/main/resources/Picture/DialogBackgrounds/closeInspection.png"));

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JLabel label = new JLabel(new ImageIcon(backgroundImage));
		backgroundImage.getGraphics().drawImage(dialogBackground, 0, -10, null);
		backgroundImage.getGraphics().drawImage(closeInspection, 680, 400, 100, 60, null);
		
		addClickableFunction(frame, 680, 400, 100, 60, "close:InspectItem");
		frame.add(label);
		frame.repaint();
	}
	
	
	public JTextArea getItemDescription(final JFrame frame, Item item){
		String text = null;
		
		
		
		text = item.getDescription();
		
		JTextArea dialog = new JTextArea();
		dialog.setText(text);
		dialog.setForeground(Color.WHITE);
		dialog.setLineWrap(true);
    	dialog.setWrapStyleWord(true);
    	dialog.setEditable(false);
    	dialog.setOpaque(false);
    	dialog.setSize(600, 200);
    	dialog.setLocation(10, 420);
    	dialog.setBorder(new EmptyBorder(10, 10, 10, 10) );
    	return dialog;
    	
		}
	
	
	
	
	
	
	
	
	public void addClickableFunction(final JFrame frame, int xLoc, int yLoc,int width, int height, final String type){
		JLabel label = new JLabel();
		label.setBounds(xLoc, yLoc, width, height);
	
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				
				if(type.equalsIgnoreCase("close:InspectItem")){
					BuildRoom r = new BuildRoom(getRooRoomId(), frame);
				}
			
			}});
				
		
		
		frame.add(label);
		
	}
	

}
