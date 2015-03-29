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

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import com.dhbw.Zombiz.gameEngine.logic.BuildRoom;
import com.dhbw.Zombiz.gameEngine.logic.CheatThread;
import com.dhbw.Zombiz.gameEngine.logic.Runtime;
import com.dhbw.Zombiz.output.audio.SoundPlayer;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 * Shows the main menue, when the game starts.
 *
 * @author Nadir Yuldashev
 * @author Jan Brodhaecker
 *
 */
public class Menu {

    JFrame frame;
    BufferedImage backgroundImage = null;

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public Menu(final JFrame frame, boolean addClicks) {
        setFrame(frame);

        KeyListener[] keyListeners = frame.getKeyListeners();
        for (int cnt = 0; cnt < keyListeners.length; cnt++) {
            frame.removeKeyListener(keyListeners[cnt]);
        }
        drawMainMenue(frame);
		//Runtime.ts.run(); 
    }

    public void drawMainMenue(final JFrame frame) {

        String path = "src/main/resources/Picture/Menue/";

        try {
            backgroundImage = ImageIO.read(new File(path + "backgroundv2.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel label = new JLabel(new ImageIcon(backgroundImage));

        JLabel startGamelabel = new JLabel();
        startGamelabel.setBounds(69, 370, 93, 60);
        JLabel startGamelabel2 = new JLabel();
        startGamelabel2.setBounds(129, 347, 50, 43);
        JLabel startGamelabel3 = new JLabel();
        startGamelabel3.setBounds(188, 307, 33, 34);
        JLabel startGamelabel4 = new JLabel();
        startGamelabel4.setBounds(203, 285, 27, 23);

        JLabel loadGamelabel = new JLabel();
        loadGamelabel.setBounds(179, 382, 74, 58);
        JLabel loadGamelabel2 = new JLabel();
        loadGamelabel2.setBounds(179, 382, 74, 58);
        JLabel loadGamelabel3 = new JLabel();
        loadGamelabel3.setBounds(179, 382, 74, 58);

        JLabel creditsGamelabel = new JLabel();
        creditsGamelabel.setBounds(276, 334, 43, 55);
        JLabel creditsGamelabel2 = new JLabel();
        creditsGamelabel2.setBounds(287, 301, 29, 34);

        JLabel exitGamelabel = new JLabel();
        exitGamelabel.setBounds(334, 374, 83, 79);
        JLabel exitGamelabel2 = new JLabel();
        exitGamelabel2.setBounds(327, 336, 54, 35);
        JLabel exitGamelabel3 = new JLabel();
        exitGamelabel3.setBounds(319, 310, 44, 27);
        JLabel exitGamelabel4 = new JLabel();
        exitGamelabel4.setBounds(316, 290, 33, 19);

        startGamelabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                startNewGame(frame);
            }
        });
        startGamelabel2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                startNewGame(frame);
            }
        });
        startGamelabel3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                startNewGame(frame);
            }
        });
        loadGamelabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                loadGame(frame);
            }
        });
        loadGamelabel2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                loadGame(frame);
            }
        });
        loadGamelabel3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                loadGame(frame);
            }
        });
        creditsGamelabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                showCredits(frame);
            }
        });
        creditsGamelabel2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                showCredits(frame);
            }
        });
        exitGamelabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                closeGame();
            }
        });
        exitGamelabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                closeGame();
            }
        });
        exitGamelabel2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                closeGame();
            }
        });
        exitGamelabel3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                closeGame();
            }
        });
        exitGamelabel4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                closeGame();
            }
        });

        frame.add(startGamelabel);
        frame.add(startGamelabel2);
        frame.add(startGamelabel3);
        frame.add(loadGamelabel);
        frame.add(loadGamelabel2);
        frame.add(loadGamelabel3);
        frame.add(creditsGamelabel);
        frame.add(creditsGamelabel2);
        frame.add(exitGamelabel);
        frame.add(exitGamelabel2);
        frame.add(exitGamelabel3);
        frame.add(exitGamelabel4);

        frame.add(label);
        frame.setVisible(true);
        frame.repaint();
        frame.pack();

    }

    public static void closeGame() {
        SoundPlayer.soundClick(false);
        System.exit(0);
    }

    public boolean checkIfGameIsLoadable() {
        File f = new File("src/main/resources/savegame.sav");
        if (f.exists()) {
            return true;
        } else {
            showWarning(frame);
            return false;
        }
    }

    public void showWarning(final JFrame frame) {
        deleteFrame(frame);

        String path = "src/main/resources/Picture/Menue/";

        BufferedImage noSaveGameWarning = null;
        BufferedImage backgroundImageNew = null;
        try {
            backgroundImageNew = ImageIO.read(new File(path + "backgroundv2.jpg"));
            noSaveGameWarning = ImageIO.read(new File(path + "noSavedGame.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int x = (backgroundImage.getWidth() - noSaveGameWarning.getWidth()) / 2;
        int y = (backgroundImage.getHeight() - noSaveGameWarning.getHeight()) / 2;
        backgroundImageNew.getGraphics().drawImage(noSaveGameWarning, x, y, null);

        JLabel clickLabel = new JLabel();
        clickLabel.setBounds(383, 317, 29, 34);
        clickLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                deleteFrame(frame);
                drawMainMenue(frame);
                frame.repaint();
                SoundPlayer.soundClick(false);

            }
        });
        addKeyListeners(frame);
        frame.add(clickLabel);
        JLabel label = new JLabel(new ImageIcon(backgroundImageNew));
        frame.add(label);
        frame.repaint();
    }

    public void deleteFrame(JFrame frame) {
        JPanel contentPane = (JPanel) frame.getContentPane();
        contentPane.removeAll();
        contentPane.revalidate();
        contentPane.repaint();
    }

    public void startNewGame(final JFrame frame) {
        frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        SoundPlayer.soundClick(false);
        final Runtime r = new Runtime(true, frame);
        CheatThread t = new CheatThread(r);
        Thread test = new Thread(t);
        test.start();
        r.initNewRoom(true, r.currRoomId, frame);
        System.out.println("Java Version " + System.getProperty("os.arch"));

        // checks if JVM is 32bit or 64bit. VLJC supports only 32bit.
        if (System.getProperty("os.arch").equalsIgnoreCase("x86")) {
            Video v = new Video(frame);
        }
    }

    public void loadGame(final JFrame frame) {
        SoundPlayer.soundClick(false);
        final Runtime r = new Runtime(true, frame);

        if (checkIfGameIsLoadable()) {
            r.loadGame();
        }
    }

    public void showCredits(final JFrame frame) {
        SoundPlayer.soundClick(false);
        
        Credits c = new Credits(frame);
    }

    public void addKeyListeners(final JFrame frame) {
        KeyAdapter keyListener = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    deleteFrame(frame);
                    drawMainMenue(frame);
                    frame.repaint();
                    SoundPlayer.soundClick(false);

                }

            }
        };

        frame.addKeyListener(keyListener);
    }

}
