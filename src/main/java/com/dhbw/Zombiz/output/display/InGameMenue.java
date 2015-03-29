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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.dhbw.Zombiz.gameEngine.logic.BuildRoom;
import com.dhbw.Zombiz.gameEngine.logic.Runtime;
import com.dhbw.Zombiz.output.audio.SoundPlayer;

/**
 * Creates the Ingame Menu, which gives the chance to save, to load, to continue
 * and to leave the game.
 *
 *
 * @author Jan Brodhaecker
 *
 */
public class InGameMenue {

    int rootRoomId;
    BufferedImage backgroundImg;

    public BufferedImage getBackgroundImage() {
        return backgroundImg;
    }

    public void setBackgroundImage(BufferedImage backgroundImage) {
        this.backgroundImg = backgroundImage;
    }

    public int getRootRoomId() {
        return rootRoomId;
    }

    public void setRootRoomId(int rootRoomId) {
        this.rootRoomId = rootRoomId;
    }

    /**
     * @param args
     */
    public InGameMenue(JFrame frame, BufferedImage backgroundImage, int rootRoomId, Runtime runtime) {
        setBackgroundImage(backgroundImage);
        setRootRoomId(rootRoomId);
        setBackground(frame, backgroundImage, runtime);
        addKeyListeners(frame, runtime);
    }

    public void setBackground(JFrame frame, BufferedImage backgroundImage, Runtime runtime) {
        BufferedImage inGameMenue = null;
        try {
            inGameMenue = ImageIO.read(new File("src/main/resources/Picture/Menue/inGameMenue.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel label = new JLabel(new ImageIcon(backgroundImage));

        int x = (backgroundImage.getWidth() - inGameMenue.getWidth()) / 2;
        int y = (backgroundImage.getHeight() - inGameMenue.getHeight()) / 2;

        backgroundImage.getGraphics().drawImage(inGameMenue, x, y, null);

        addClickable(frame, 225, 205, 125, 35, "continue", runtime);
        addClickable(frame, 225, 320, 135, 35, "loadGame", runtime);

        addClickable(frame, 225, 260, 185, 35, "saveGame", runtime);
        addClickable(frame, 225, 375, 175, 35, "exitGame", runtime);

        frame.add(label);
        frame.repaint();
    }

    public void addClickable(final JFrame frame, int xLoc, int yLoc, int width, int height, final String type, Runtime runtime) {
        final Runtime innerRuntime = runtime;
        JLabel label = new JLabel();
        label.setBounds(xLoc, yLoc, width, height);

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {

                if (type.equalsIgnoreCase("continue")) {
                    SoundPlayer.soundClick(innerRuntime.br.muted);
                    innerRuntime.initNewRoom(false, getRootRoomId(), frame);
                    //BuildRoom r = new BuildRoom(getRootRoomId(), frame);
                }
                if (type.equalsIgnoreCase("loadGame")) {
                    loadGame(frame, innerRuntime);
                }
                if (type.equalsIgnoreCase("saveGame")) {
                    SoundPlayer.soundClick(innerRuntime.br.muted);
                    innerRuntime.saveGame();
                    showSuccessSavingGame(frame, innerRuntime);
                }
                if (type.equalsIgnoreCase("exitGame")) {
                    System.exit(0);
                }
            }
        });

        frame.add(label);

    }

    public void showSuccessSavingGame(final JFrame frame, Runtime runtime) {
        final Runtime innerRuntime = runtime;
        deleteFrame(frame);
        BufferedImage backgroundImg = getBackgroundImage();
        BufferedImage successSavingGame = null;
        try {
            successSavingGame = ImageIO.read(new File("src/main/resources/Picture/Menue/successSaveGame.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JLabel label = new JLabel(new ImageIcon(backgroundImg));

        int x = (backgroundImg.getWidth() - successSavingGame.getWidth()) / 2;
        int y = (backgroundImg.getHeight() - successSavingGame.getHeight()) / 2;

        JLabel clickLabel = new JLabel();
        clickLabel.setBounds(383, 316, 36, 28);

        clickLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                SoundPlayer.soundClick(innerRuntime.br.muted);
                innerRuntime.initNewRoom(false, getRootRoomId(), frame);
                //BuildRoom r = new BuildRoom(getRootRoomId(), frame);
            }
        });

        addKeyListenerForCheck(frame, runtime);
        backgroundImg.getGraphics().drawImage(successSavingGame, x, y, null);
        frame.add(clickLabel);
        frame.add(label);
        frame.repaint();

    }

    public void addKeyListenerForCheck(final JFrame frame, Runtime runtime) {
        final Runtime innerRuntime = runtime;
        KeyAdapter keyListener = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    SoundPlayer.soundClick(innerRuntime.br.muted);
                    innerRuntime.initNewRoom(false, getRootRoomId(), frame);
                    //BuildRoom r = new BuildRoom(getRootRoomId(), frame);
                }

            }
        };

        frame.addKeyListener(keyListener);
    }

    public void addKeyListeners(final JFrame frame, Runtime runtime) {
        final Runtime innerRuntime = runtime;
        KeyAdapter keyListener = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 27) {
                    innerRuntime.initNewRoom(false, getRootRoomId(), frame);
                    //BuildRoom r = new BuildRoom(getRootRoomId(), frame);
                }
            }
        };

        frame.addKeyListener(keyListener);
    }

    public void deleteFrame(JFrame frame) {
        JPanel contentPane = (JPanel) frame.getContentPane();
        contentPane.removeAll();
        contentPane.revalidate();
        contentPane.repaint();
    }

    public void loadGame(JFrame frame, Runtime runtime) {
        File f = new File("src/main/resources/savegame.sav");
        if (f.exists()) {
            SoundPlayer.soundClick(runtime.br.muted);
            runtime.loadGame();
        } else {
            showWarning(frame, runtime);
        }
    }

    public void showWarning(final JFrame frame, Runtime runtime) {
        final Runtime innerRuntime = runtime; 
        deleteFrame(frame);
        String path = "src/main/resources/Picture/Menue/";

        BufferedImage noSaveGameWarning = null;
        try {
            noSaveGameWarning = ImageIO.read(new File(path + "noSavedGame.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int x = (backgroundImg.getWidth() - noSaveGameWarning.getWidth()) / 2;
        int y = (backgroundImg.getHeight() - noSaveGameWarning.getHeight()) / 2;
        backgroundImg.getGraphics().drawImage(noSaveGameWarning, x, y, null);

        JLabel clickLabel = new JLabel();
        clickLabel.setBounds(383, 317, 29, 34);
        clickLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                deleteFrame(frame);
                innerRuntime.initNewRoom(false, getRootRoomId(), frame);
                //BuildRoom r = new BuildRoom(getRootRoomId(), frame);
                frame.repaint();
                SoundPlayer.soundClick(innerRuntime.br.muted);

            }
        });
        addKeyListenerForFailLoad(frame, innerRuntime);
        frame.add(clickLabel);
        JLabel label = new JLabel(new ImageIcon(backgroundImg));
        frame.add(label);
        frame.repaint();

    }

    public void addKeyListenerForFailLoad(final JFrame frame, Runtime runtime) {
        final Runtime innerRuntime = runtime;
        KeyAdapter keyListener = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    innerRuntime.initNewRoom(false, getRootRoomId(), frame);
                    //BuildRoom r = new BuildRoom(getRootRoomId(), frame);
                }
            }
        };

        frame.addKeyListener(keyListener);
    }
}
