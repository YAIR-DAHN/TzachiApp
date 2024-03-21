package com.shahareinisim.tzachiapp.Models;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TfilahPart {

    public enum Key {NORMAL, TITLE, NOTE, SOD, INSIDE_NOTE, ADDON, EMPTY}

    String part;
    Key primaryKey;
    ArrayList<Key> keys = new ArrayList<>();
    ArrayList<Note> notes = new ArrayList<>();

    public TfilahPart(String part) {

        // get the primary key
        String tempKey = initTempKey(part);
        // init temp key
        this.primaryKey = initType(tempKey);
        // init this.part and remove key text from part
        this.part = part.replace(tempKey,"");

        int count = 0;
        // add every key to ArrayList<Key>
        while (!tempKey.isEmpty()) {
            count++;
            // add key to the list
            keys.add(initType(tempKey));
            // get the next key from part (if exist...)
            tempKey = initTempKey(this.part);

            // remove key text from part
            this.part = this.part.replace(tempKey,"");

        }
        if (this.part.isEmpty()) keys.add(Key.EMPTY);

        if (isInsideNote()) initNote();
    }

    private String initTempKey(String part) {
        // avoid looping on empty line...
        if (keys.contains(Key.EMPTY)) return "";
        if (part.isEmpty()) return "[e]";
        return (part.contains("[") && part.contains("]"))
                ? part.substring(part.indexOf("["), part.indexOf("]")+1) : "";
    }

    // pull key from part
    // TODO organize notes by {startIndex, endIndex} instead of just Strings
    // ...it could be buggy if part has 2 words and one is 'note' and the other 'normal'
    private void initNote() {
        if (this.part.contains("*")) {
            int firstIndex = part.indexOf("*")+1;
            int secondIndex = part.indexOf("*", firstIndex+1);
            this.notes.add(new Note(firstIndex, secondIndex));
            this.part = part.replaceFirst("\\*", "").replaceFirst("\\*", "");
//            if (keys.contains(Key.INSIDE_NOTE) && notes.size() == 1) addLine();
        }

        if (this.part.contains("*")) initNote();
    }

    private Key initType(String tempKey) {
        switch (tempKey) {
            case "[t]":
                return Key.TITLE;
            case "[n]":
                return Key.NOTE;
            case "[sod]":
                return Key.SOD;
            case "[h]":
            case "[c]":
            case "[p]":
            case "[yv]":
            case "[rc]":
            case "[halel]":
            case "[megilat ester]":
                return Key.ADDON;
            case "[in]":
                return Key.INSIDE_NOTE;
            case "[e]":
                return Key.EMPTY;
            default:
                return Key.NORMAL;
        }
    }

    private void addLine() {
        int lastIndex;
        if (part.length() > notes.get(notes.size()-1).length()) {
            lastIndex = notes.get(notes.size() - 1).length() + 1;
        } else lastIndex = notes.get(notes.size()-1).length();

        String part1 = part.substring(0, lastIndex);
        String part2 = part.substring(lastIndex);
        this.part = part1.concat("\n").concat(part2);
    }

    public Key getPrimaryKey() {
        return primaryKey;
    }

    public ArrayList<Key> getKeys() {
        return keys;
    }

    public void addKey(Key key) {
        keys.add(key);
    }

    public String getPart() {
        return part;
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public boolean isTitle() {
        return keys.contains(Key.TITLE);
    }

    public boolean isInsideNote() {
        if (part.contains("*") && !keys.contains(Key.INSIDE_NOTE)) keys.add(Key.INSIDE_NOTE);
        return keys.contains(Key.INSIDE_NOTE);
    }

    public boolean isEmpty() {
        return keys.contains(Key.EMPTY);
    }
}
