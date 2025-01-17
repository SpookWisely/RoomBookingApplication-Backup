package com.example.roombookingapplication;

//Simple use class constructor with getter methods.

public class User {

    private int id, admin, management;
    private String fullName,userName,email;


    public User(int id, String fullName, String userName, String email,int admin, int managment) {
        this.id = id;
        this.fullName = fullName;
        this.userName = userName;
        this.email = email;
        this.admin = admin;
        this.management = managment;
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
    public int getAdmin() {
        return admin;
    }
    public int getManagement() {
        return management;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public void setManagement(int management) {
        this.management = management;
    }


}
