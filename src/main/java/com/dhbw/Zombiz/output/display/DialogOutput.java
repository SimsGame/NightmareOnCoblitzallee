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
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

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
import com.dhbw.Zombiz.gameEngine.logic.Runtime;
import com.dhbw.Zombiz.output.audio.SoundPlayer;

/**
 * Creates the Dialog Output, when the player wants to talk to a NPC. The class
 * also gives the chance to let the user to decide what to say, if there are
 * more than one possibilities.
 *
 *
 *
 * @author Jan Brodhaecker
 *
 */
public class DialogOutput {

    public int nextDeId;
    public Conversation conv;
    public List<Actor> actors;
    public BufferedImage backgroundImage;

    public static int option;
    public static int optionOne;
    public static int optionTwo;
    public static int optionThree;

    public int maxDeSize;
    public int roomId;

    public boolean isGroup;
    public KeyAdapter keyListener;

    public KeyListener getKeyListener() {
        return keyListener;
    }

    public void setKeyListener(KeyAdapter keyListener) {
        this.keyListener = keyListener;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getMaxDeSize() {
        return maxDeSize;
    }

    public void setMaxDeSize(int maxDeSize) {
        this.maxDeSize = maxDeSize;
    }

    public static int getOptionOne() {
        return optionOne;
    }

    public static void setOptionOne(int optionOne) {
        DialogOutput.optionOne = optionOne;
    }

    public static int getOptionTwo() {
        return optionTwo;
    }

    public static void setOptionTwo(int optionTwo) {
        DialogOutput.optionTwo = optionTwo;
    }

    public static int getOptionThree() {
        return optionThree;
    }

    public static void setOptionThree(int optionThree) {
        DialogOutput.optionThree = optionThree;
    }

    public static int getOption() {
        return option;
    }

    public static void setOption(int option) {
        DialogOutput.option = option;
    }

    public BufferedImage getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(BufferedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public Conversation getConv() {
        return conv;
    }

    public void setConv(Conversation conv) {
        this.conv = conv;
    }

    public int getNextDeId() {
        return nextDeId;
    }

    public void setNextDeId(int nextDeId) {
        this.nextDeId = nextDeId;
    }

    //Constructor ! 
    public DialogOutput(JFrame frame, Conversation c, BufferedImage backgroundImage, List<Actor> actors, int rootRoomId, Runtime runtime) {

        SoundPlayer.soundStartConv();
        frame.getContentPane().removeAll();

        setMaxDeSize(c.getDialogEntries().size());
        setRoomId(rootRoomId);

        if (runtime.currentChapter == 2) {
            for (int i = 7; i <= 100; i++) {
                if (actors.get(i) != null) {
                    actors.set(i - 6, actors.get(i));
                }
            }
        }
        setActors(actors);
        setBackgroundImage(backgroundImage);
        setConv(c);

        Conversation conversation = c;
        JTextArea dialog = getDialogEntry(frame, conversation, actors, true, runtime);

        frame.add(dialog);

        frame.repaint();
        setDialogBackground(frame, backgroundImage, runtime);

    }

    public JTextArea getDialogEntry(final JFrame frame, Conversation c, final List<Actor> actors, boolean firstCall, Runtime runtime) {
        String text = null;
        if (firstCall) {
            DialogEntry deFirst = c.getDialogEntryById(1);
            runtime.checkDialog(1, conv.getConversationId());
            String actorName = actors.get(Integer.parseInt(deFirst.getActor())).getName();

            text = actorName + " : " + deFirst.getDialogText();
            setNextDeId(deFirst.getLinkedDialogEntries().get(0));

        } else {
            DialogEntry de = c.getDialogEntryById(getNextDeId());
            runtime.checkDialog(de.getDialogEntryId(), conv.getConversationId());
            String actorName = actors.get(Integer.parseInt(de.getActor())).getName();

            text = actorName + " : " + de.getDialogText();

            int nextId = 1;
            int optionOne = 0;
            int optionTwo = 0;
            int optionThree = 0;

            if (!(de.isGroup())) {
                nextId = de.getLinkedDialogEntries().get(0);
                System.out.println("You want to display dialog entry " + nextId + " next.");
            } else {
                setGroup(true);
                if (de.getLinkedDialogEntries().size() > 0) {
                    optionOne = de.getLinkedDialogEntries().get(0);
                    setOptionOne(de.getLinkedDialogEntries().get(0));
                }

                if (de.getLinkedDialogEntries().size() > 1) {
                    optionTwo = de.getLinkedDialogEntries().get(1);
                    setOptionTwo(de.getLinkedDialogEntries().get(1));
                }
                if (de.getLinkedDialogEntries().size() > 2) {
                    optionThree = de.getLinkedDialogEntries().get(2);
                    setOptionThree(de.getLinkedDialogEntries().get(2));
                }

                String option1 = null, option2 = null, option3 = null;
                if (optionOne != 0) {
                    option1 = "\n 1: " + c.getDialogEntryById(optionOne).getDialogText() + "\n";
                }
                if (optionTwo != 0) {
                    option2 = option1 + "\n 2: " + c.getDialogEntryById(optionTwo).getDialogText() + "\n";
                }
                if (optionThree != 0) {
                    option3 = option2 + "\n 3: " + c.getDialogEntryById(optionThree).getDialogText();
                }

                if (optionOne != 0 && optionTwo != 0) {
                    text = option2;
                    String[] lines = option2.split(" ");

                    if (lines.length <= 25) {
                        addClickableFunction(frame, 25, 445, 535, 20, "option:1", runtime);
                        addClickableFunction(frame, 25, 475, 535, 20, "option:2", runtime);
                    }
                    if (lines.length >= 25) {
                        addClickableFunction(frame, 25, 445, 535, 30, "option:1", runtime);
                        addClickableFunction(frame, 25, 495, 535, 30, "option:2", runtime);
                    }

                }

                if (optionOne != 0 && optionTwo != 0 && optionThree != 0) {
                    text = option3;
                    addClickableFunction(frame, 25, 445, 535, 20, "option:1", runtime);
                    addClickableFunction(frame, 25, 475, 535, 20, "option:2", runtime);
                    addClickableFunction(frame, 25, 510, 535, 20, "option:3", runtime);
                }

            }

            //to keep tab of which dialog entry is displayed
            runtime.checkDialog(nextId, conv.getConversationId());
            setNextDeId(nextId);
        }

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

		//System.out.println(text);
    }

    public void setDialogBackground(JFrame frame, BufferedImage backgroundImage, Runtime runtime) {

        BufferedImage dialogBackground = null;
        BufferedImage dialogNext = null;
        try {
            dialogBackground = ImageIO.read(new File("src/main/resources/Picture/DialogBackgrounds/dialogGround.png"));
            dialogNext = ImageIO.read(new File("src/main/resources/Picture/DialogBackgrounds/nextBtn.png"));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JLabel label = new JLabel(new ImageIcon(backgroundImage));
        backgroundImage.getGraphics().drawImage(dialogBackground, 0, -15, null);
        backgroundImage.getGraphics().drawImage(dialogNext, 600, 500, 166, 84, null);

        if (isGroup()) {
            setGroup(false);
        } else {
            addClickableFunction(frame, 600, 500, 166, 84, "nextDialogEntry", runtime);
        }
        addKeyListenerFunction(frame, runtime);
        frame.addKeyListener(getKeyListener());

        frame.add(label);
        frame.repaint();
    }

    public void addClickableFunction(final JFrame frame, int xLoc, int yLoc, int width, int height, final String type, Runtime runtime) {
        final Runtime innerRuntime = runtime;
        JLabel label = new JLabel();
        label.setBounds(xLoc, yLoc, width, height);

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {

                if (type.equalsIgnoreCase("nextDialogEntry")) {
                    if (getNextDeId() <= getMaxDeSize() - 1) {
                        SoundPlayer.soundNextDialog();
                        frame.getContentPane().removeAll();
                        JTextArea dialog = getDialogEntry(frame, getConv(), actors, false, innerRuntime);
                        frame.add(dialog);
                        frame.repaint();
                        setDialogBackground(frame, backgroundImage, innerRuntime);
                        frame.removeKeyListener(keyListener);
                    } else {

                        KeyListener[] keyl = frame.getKeyListeners();
                        for (int cnt = 0; cnt < keyl.length; cnt++) {
                            System.out.println("No " + cnt);
                            frame.removeKeyListener(keyl[cnt]);
                        }

                        frame.removeKeyListener(keyListener);
                        innerRuntime.initNewRoom(false, getRoomId(), frame);
                        //BuildRoom r = new BuildRoom(getRoomId(), frame);
			//System.out.println("I built room "+getRoomId());

                    }

                }

                if (type.equalsIgnoreCase("option:1")) {
                    setNextDeId(getOptionOne());
                    frame.getContentPane().removeAll();
                    JTextArea dialog = getDialogEntry(frame, getConv(), actors, false, innerRuntime);
                    frame.add(dialog);
                    frame.repaint();
                    setDialogBackground(frame, backgroundImage, innerRuntime);
                }

                if (type.equalsIgnoreCase("option:2")) {
                    setNextDeId(getOptionTwo());
                    frame.getContentPane().removeAll();
                    JTextArea dialog = getDialogEntry(frame, getConv(), actors, false, innerRuntime);
                    frame.add(dialog);
                    frame.repaint();
                    setDialogBackground(frame, backgroundImage, innerRuntime);
                }

                if (type.equalsIgnoreCase("option:3")) {
                    setNextDeId(getOptionThree());
                    frame.getContentPane().removeAll();
                    JTextArea dialog = getDialogEntry(frame, getConv(), actors, false, innerRuntime);
                    frame.add(dialog);
                    frame.repaint();
                    setDialogBackground(frame, backgroundImage, innerRuntime);
                }

            }
        });

        frame.add(label);

    }

    public void addKeyListenerFunction(final JFrame frame, Runtime runtime) {
        final Runtime innerRuntime= runtime;
        keyListener = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                // Char Space 32, Char Enter 10
                if (e.getKeyCode() == 10 || e.getKeyCode() == 32) {
                    if (getNextDeId() <= getMaxDeSize() - 1) {
                        SoundPlayer.soundNextDialog();
                        frame.getContentPane().removeAll();
                        JTextArea dialog = getDialogEntry(frame, getConv(), actors, false, innerRuntime);
                        frame.add(dialog);
                        frame.repaint();
                        setDialogBackground(frame, backgroundImage, innerRuntime);
                        frame.removeKeyListener(keyListener);
                    } else {
                        KeyListener[] keyl = frame.getKeyListeners();
                        for (int cnt = 0; cnt < keyl.length; cnt++) {
                            frame.removeKeyListener(keyl[cnt]);
                        }
                        innerRuntime.initNewRoom(false, getRoomId(), frame);
                        //BuildRoom r = new BuildRoom(getRoomId(), frame);
                    }
                }
            }
        ;
    }
;
}
		 
	
	
	
}
