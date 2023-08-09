package com.shahareinisim.tzachiapp.Models;

import java.util.ArrayList;
import java.util.List;

public class TfilahPart {

    public enum Key {NORMAL, TITLE, NOTE, SOD, INSIDE_NOTE, INLINE_NOTE, EMPTY}
    Key key;
    String part;

    List<String> note = new ArrayList<>();

    public TfilahPart(String part) {
        String tempKey = initTempKey(part);
        this.part = part.replace(tempKey,"");
        this.key = initType(tempKey);

        if (isInsideNote()) initNote();
    }

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
            this.note.add(part.substring(firstIndex, secondIndex));
            this.part = part.replaceFirst("\\*", "").replaceFirst("\\*", "");
            if (key.equals(Key.INSIDE_NOTE) && note.size() == 1) addLine();
        }
        if (this.part.contains("*")) initNote();
    }

    private void addLine() {
        int lastIndex;
        if (part.length() > note.get(note.size()-1).length()) {
            lastIndex = note.get(note.size() - 1).length() + 1;
        } else lastIndex = note.get(note.size()-1).length();

        String part1 = part.substring(0, lastIndex);
        String part2 = part.substring(lastIndex);
        this.part = part1.concat("\n").concat(part2);
    }

    public Key getKey() {
        return key;
    }

    public String getPart() {
        return part;
    }

    public List<String> getNote() {
        return note;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public boolean isTitle() {
        return key.equals(Key.TITLE);
    }

    private boolean isInsideNote() {
        return key.equals(Key.INSIDE_NOTE) || key.equals(Key.INLINE_NOTE);
    }
}
