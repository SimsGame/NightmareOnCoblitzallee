/**
 * *****************************************************************************
 * Copyright (c) 2013 DHBW. This source is subject to the DHBW Permissive
 * License. Please see the License.txt file for more information. All other
 * rights reserved.
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND,
 * EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A PARTICULAR PURPOSE.
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * Project: Zombiz Package: com.dhbw.zombiz
 *******************************************************************************
 */
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
import com.dhbw.Zombiz.output.audio.SoundPlayer;

/**
 * Shows a description and - if there is a big picture of the item - a picture,
 * when you want to inspect a item.
 *
 * @author Jan Brodhaecker
 *
 */
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

    public ItemInspect(JFrame frame, BufferedImage backgroundImage, Item item, int rootRoomId, Runtime runtime) {
        SoundPlayer.soundStartConv();

        setRooRoomId(rootRoomId);

        setBigPicIsAvailable(checkIfBigPicIsAvailable(item));

        JTextArea dialog = getItemDescription(frame, item);
        frame.add(dialog);
        drawItemInspectBackground(frame, backgroundImage, runtime);
    }

    public boolean checkIfBigPicIsAvailable(Item item) {
        String picPath = item.getPicturePath();
        picPath = picPath.trim();
        picPath = picPath.substring(1, picPath.length() - 1);
        picPath = picPath.replace(".png", "");
        picPath = picPath + "_big.png";
        File f = new File(picPath);

        if (f.exists()) {
            setItemBackground(picPath);
            return true;
        } else {
            return false;
        }
    }

    public void drawItemInspectBackground(JFrame frame, BufferedImage backgroundImage, Runtime runtime) {
        BufferedImage bigItemPic = null;

        if (isBigPicIsAvailable()) {
            System.out.println("Here");
            try {
                bigItemPic = ImageIO.read(new File(getItemBackground()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            backgroundImage.getGraphics().drawImage(bigItemPic, 100, 100, 100, 100, null);
        }

        BufferedImage dialogBackground = null;
        BufferedImage closeInspection = null;
        try {
            dialogBackground = ImageIO.read(new File("src/main/resources/Picture/DialogBackgrounds/dialogGround.png"));
            closeInspection = ImageIO.read(new File("src/main/resources/Picture/DialogBackgrounds/closeInspection.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel label = new JLabel(new ImageIcon(backgroundImage));
        backgroundImage.getGraphics().drawImage(dialogBackground, 0, -15, null);
        backgroundImage.getGraphics().drawImage(closeInspection, 680, 400, 100, 60, null);
        addClickableFunction(frame, 680, 400, 100, 60, "close:InspectItem", runtime);
        frame.add(label);
        frame.repaint();
    }

    public JTextArea getItemDescription(final JFrame frame, Item item) {
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
        dialog.setBorder(new EmptyBorder(10, 10, 10, 10));
        return dialog;

    }

    public void addClickableFunction(final JFrame frame, int xLoc, int yLoc, int width, int height, final String type, Runtime runtime) {
        final Runtime innerRuntime = runtime;
        JLabel label = new JLabel();
        label.setBounds(xLoc, yLoc, width, height);

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {

                if (type.equalsIgnoreCase("close:InspectItem")) {
                    SoundPlayer.soundNextDialog();
                    innerRuntime.initNewRoom(false, getRooRoomId(), frame);
                    //BuildRoom r = new BuildRoom(getRooRoomId(), frame);
                }
            }
        });

        frame.add(label);

    }

}
