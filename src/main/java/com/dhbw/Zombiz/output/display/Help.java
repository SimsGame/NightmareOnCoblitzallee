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
import com.dhbw.Zombiz.gameEngine.logic.Runtime;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *Shows hints during the game, and says what to do next. Can be called with the h-key during the game.
 *
 *
 * @author Stazzer
 */
public class Help{
    
    public static String helptext;
    public static int room;
    public static BufferedImage bg;
    
    public  Help(final JFrame frame, int roomId, BufferedImage background, Runtime runtime){
        if(runtime.currentChapter == 1){    
        	switch(runtime.getGameState()){
            
                case 0: helptext = "Ich brauche den Schlüssel um die Tür abzuschließen..."; break;
                case 1: helptext = "Ich sollte die Tür abzuschließen..."; break;
                case 2: helptext = "Ich muss in Raum 2..."; break;
                case 3: helptext = "Prof. Meier braucht die Büroklammer aus Raum 1..."; break;
                case 4: helptext = "Ich sollte Prof. Meier die Büroklammer bringen..."; break;
                case 5: helptext = "Ich sollte Prof. Meier die Büroklammer bringen..."; break;
                case 6: helptext = "Nun brauche ich noch den Zettel aus dem Sicherungskasten in Raum 3 und den vom Beamer..."; break;
                case 7: helptext = "Jetzt noch den letzten Zettel vom Projektor in Raum 1, dann habe ich alle..."; break;
                case 8: helptext = "Auf zum SG... Treppenhaus halt und nach oben. Danach den Aufzug reparieren"; break;
                case 9: helptext = "Schnell in den Aufzug und weiter nach oben zur Kuchenparty..."; break;
                case 14: helptext = "Ich muss aber die Seilwinde in Raum 2 in den 2. Stock gelangen..."; break;
                default: helptext = "Ich sollte mir die Räume noch einmal genauer anschen...";
            }
        }
        room = roomId;
        bg = background;
        showHelpText("", frame);
    }
    
    public static void showHelpText(String helpToDisplay, final JFrame frame){
        if(!helpToDisplay.isEmpty()){
            helptext = helpToDisplay;
        }
        JPanel panel = new JPanel();
        panel.setBackground(Color.black);
        panel.setBounds(10, 500, 780, 50);
        frame.getContentPane().removeAll();
        JLabel helpLabel = new JLabel(helptext);
        helpLabel.setForeground(Color.white);
        panel.add(helpLabel);
        frame.add(panel);
        JLabel label = new JLabel(new ImageIcon(bg));
        frame.add(label);
        panel.setVisible(true);
        frame.validate();
        frame.repaint();
    }
}
