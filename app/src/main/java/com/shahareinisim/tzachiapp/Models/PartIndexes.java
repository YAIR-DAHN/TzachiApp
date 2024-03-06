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
        this.startIndex = text.indexOf(textToFind)+1;
        this.endIndex = text.indexOf(textToFind, startIndex) + textToFind.length() + 1;
        Log.d("***", textToFind + "\n"
                + text + "\n" +
                "start: " + startIndex + ", end: " + endIndex);
        this.part = text.substring(startIndex, endIndex);
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
