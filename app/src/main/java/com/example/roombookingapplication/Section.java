package com.example.roombookingapplication;

public class Section {

    private int sectionID,totalCapacity, isRoom, isSection;
    private String section_RoomName;

    public Section(int sectionID, String section_RoomName, int totalCapacity, int isRoom, int isSection) {
        this.sectionID = sectionID;
        this.section_RoomName = section_RoomName;
        this.totalCapacity = totalCapacity;
        this.isRoom = isRoom;
        this.isSection = isSection;
    }

    public int getSectionID() { return sectionID;}

    public String getSection_RoomName() {return section_RoomName;}

    public int getTotalCapacity() {return totalCapacity;}

    public int getIsRoom() {
        return isRoom;
    }

    public int getIsSection() { return isSection; }

    public void setIsRoom(int isRoom) {  this.isRoom = isRoom; }

    public void setIsSection(int isSection) {  this.isSection = isSection; }

    public void setSectionID(int sectionID) {this.sectionID = sectionID; };

    public void setSection_RoomName(String section_RoomName) {this.section_RoomName = section_RoomName;}

    public void setTotalCapacity(int totalCapacity) {this.totalCapacity = totalCapacity;}
}
