package com.shahareinisim.tzachiapp.Models;

import java.util.ArrayList;
import java.util.List;

public class TfilahPart {

    public enum Key {NORMAL, TITLE, NOTE, SOD, HOLIDAY, INSIDE_NOTE, INLINE_NOTE, EMPTY}

    String part;
    Key primaryKey;
    ArrayList<Key> keys;
    List<String> notes = new ArrayList<>();

    public TfilahPart(String part) {

        this.keys = new ArrayList<>();

        // get the primary key
        String tempKey = initTempKey(part);
        // init temp key
        this.primaryKey = initType(tempKey);
        // remove key text from part
        this.part = part.replace(tempKey,"");

        // add every key to ArrayList<Key>
        while (!tempKey.equals("")) {
            // add key to the list
            keys.add(initType(tempKey));
            // get the next key from part (if exist...)
            tempKey = initTempKey(part);

            // remove key text from part
            this.part = part.replace(tempKey,"");
        }

        if (isInsideNote()) initNote();
    }

    // pull key from part
    private String initTempKey(String part) {
        if (part.equals("")) return "[e]";
        return (part.contains("[") && part.contains("]"))
                ? part.substring(part.indexOf("["),part.indexOf("]")+1) :
                part.contains("*")
                ? "[iln]" : "";
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
                return Key.HOLIDAY;
            case "[in]":
                return Key.INSIDE_NOTE;
            case "[iln]":
                return Key.INLINE_NOTE;
            case "[e]":
                return Key.EMPTY;
            default:
                return Key.NORMAL;
        }
    }

    private void initNote() {
        if (this.part.contains("*")) {
            int firstIndex = part.indexOf("*")+1;
            int secondIndex = part.indexOf("*", firstIndex+1);
            this.notes.add(part.substring(firstIndex, secondIndex));
            this.part = part.replaceFirst("\\*", "").replaceFirst("\\*", "");
            if (keys.contains(Key.INSIDE_NOTE) && notes.size() == 1) addLine();
        }
        if (this.part.contains("*")) initNote();
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

    public String getPart() {
        return part;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public boolean isTitle() {
        return keys.contains(Key.TITLE);
    }

    private boolean isInsideNote() {
        return keys.contains(Key.INSIDE_NOTE) || keys.contains(Key.INLINE_NOTE) || part.contains("*");
    }
}
