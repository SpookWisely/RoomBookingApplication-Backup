package com.example.roombookingapplication;

//Simple use class constructor with getter methods.

public class User {

    private int id;
    private String fullName,userName,email;

    public User(int id, String fullName, String userName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.userName = userName;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }
}
