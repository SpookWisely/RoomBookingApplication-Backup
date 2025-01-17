package com.example.roombookingapplication;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Booking {

    private int bookingID, userID, sectionID,amountSeats;
    private String creationDate = new SimpleDateFormat("YYYY-MM-DD", Locale.getDefault()).format(new Date());
    private String creationTime = new SimpleDateFormat("HH-mm-ss", Locale.getDefault()).format(new Date());
    private String dateOfBooking;

    private String startTime, endTime;

    public Booking (int bookingID, int userID, int sectionID,int amountSeats,
                 String creationDate,String creationTime, String dateOfBooking, String startTime, String endTime) {
                    this.bookingID = bookingID;
                    this.userID = userID;
                    this.sectionID = sectionID;
                    this.amountSeats = amountSeats;
                    this.creationDate = creationDate;
                    this.creationTime = creationTime;
                    this.dateOfBooking = dateOfBooking;
                    this.startTime = startTime;
                    this.endTime = endTime;

    }

    public int getBookingID() {
        return bookingID;
    }
    public int getUserID() {
        return userID;
    }
    public int getSectionID() {
        return sectionID;
    }
    public int getAmountSeats() { return amountSeats; }

    public String getCreationDate() {
        return creationDate;
    }
    public String getCreationTime() {
        return creationTime;
    }
    public String getDateOfBooking() {
        return dateOfBooking;
    }
    public String getStartTime() {
        return startTime;
    }
    public String getEndTime() {
        return endTime;
    }


    public void setAmountSeats(int amountSeats) { this.amountSeats = amountSeats; }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public void setDateOfBooking(String dateOfBooking) {
        this.dateOfBooking = dateOfBooking;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
