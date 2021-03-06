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
package com.dhbw.Zombiz.gameEngine.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for all conversations in the game, which contains all DialogEntries.
 *
 * Gets stuff from the XML parser, which creates these objects for the game
 *
 * @author Jan Brodhaecker
 */
public class Conversation {

    int conversationId;
    String title;
    String picPath;
    String description;
    String actor;
    String conversant;
    String act;
    String chapter;
    String scene;
    String level;
    String mood;
    String primaryLoc;

    List<DialogEntry> dialogEntries = new ArrayList<DialogEntry>();

    public void conversate() {

    }

    public Conversation(int id) {
        this.conversationId = id;
    }

    public DialogEntry getDialogEntryById(int id) {
        DialogEntry de = null;
        for (int cnt = 0; cnt < this.dialogEntries.size(); cnt++) {
            if (this.dialogEntries.get(cnt).getDialogEntryId() == id) {
                de = dialogEntries.get(cnt);
            }
        }
        return de;
    }

    public List<DialogEntry> getDialogEntries() {
        return dialogEntries;
    }

    public void setDialogEntries(List<DialogEntry> dialogEntries) {
        this.dialogEntries = dialogEntries;
    }

    public void addDialogEntry(DialogEntry de) {
        this.dialogEntries.add(de);
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getConversant() {
        return conversant;
    }

    public void setConversant(String conversant) {
        this.conversant = conversant;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getPrimaryLoc() {
        return primaryLoc;
    }

    public void setPrimaryLoc(String primaryLoc) {
        this.primaryLoc = primaryLoc;
    }

}
