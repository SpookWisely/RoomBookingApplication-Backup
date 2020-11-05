package com.example.roombookingapplication;

public class URLStorage {
//ROOT_URL is just the default string used for connection.
    private static final String ROOT_URL ="http://10.15.5.72/PhPLogin/WebAPI.php?apicall=";
//Below are the combinations that allow for the switch case in the PHP files to be called.
    public static final String URL_REGISTER= ROOT_URL + "SigningUp";
    public static final String URL_Login = ROOT_URL + "LoggingIn";
}


