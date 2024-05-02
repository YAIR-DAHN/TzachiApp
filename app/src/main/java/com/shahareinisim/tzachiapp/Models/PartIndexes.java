package com.shahareinisim.tzachiapp.Models;

import android.util.Log;

public class PartIndexes {

    String part;
    int startIndex, endIndex;

    public PartIndexes(String part, int startIndex, int endIndex) {
        this.part = part;
        this.startIndex = startIndex;
        this.endIndex = endIndex-1;
    }

    public PartIndexes(String text, String textToFind) {
        if (!text.contains(textToFind)) {
            this.startIndex = 0;
            this.endIndex = 0;
            this.part = "";
            return;
        }
        this.startIndex = text.indexOf(textToFind);
        this.endIndex = text.indexOf(textToFind, startIndex+1) + textToFind.length();
        this.part = text.substring(startIndex, endIndex);
        Log.d("##### PartIndexes #####", String.format("startIndex: %s, endIndex: %s\npart: %s", startIndex, endIndex, part));
    }

    public String getPart() {
        return part;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public int length() {
        return endIndex - startIndex;
    }
}
