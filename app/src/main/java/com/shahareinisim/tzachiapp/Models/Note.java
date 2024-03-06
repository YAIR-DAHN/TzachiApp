package com.shahareinisim.tzachiapp.Models;

public class Note {

    int startIndex, endIndex;

    public Note(int startIndex, int endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex-1;
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
