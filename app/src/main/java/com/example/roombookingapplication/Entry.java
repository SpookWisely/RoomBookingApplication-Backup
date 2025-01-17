package com.example.roombookingapplication;

public class Entry {
    private int sectionID, count;

    public Entry (int sectionID,int count) {
        this.sectionID = sectionID;
        this.count = count ;
    }

    public int getSectionID() {
        return sectionID;
    }

    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
