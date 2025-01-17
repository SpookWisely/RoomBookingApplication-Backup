package com.example.roombookingapplication;

public class URLStorage {
//ROOT_URL is just the default string used for connection.
    private static final String ROOT_URL ="http://10.15.5.72/PhPLogin/WebAPI.php?apicall=";
//Below are the combinations that allow for the switch case in the PHP files to be called.
    public static final String URL_REGISTER = ROOT_URL + "SigningUp";
    public static final String URL_Login = ROOT_URL + "LoggingIn";
    public static final String URL_SectionCreation = ROOT_URL + "SectionCreation";
    public static final String URL_SectionDeletion = ROOT_URL + "SectionDeletion";
    public static final String URL_BookingCreation = ROOT_URL + "BookingCreation";
    public static final String URL_BookingDeletion = ROOT_URL + "BookingDeletion";
    public static final String URL_SectionGather = ROOT_URL + "SectionCall";
    public static final String URL_FindUserBookings = ROOT_URL + "GetUserBookings";
    public static final String URL_FindAllBookings = ROOT_URL + "GetBookings";
    public static final String URL_GetAllUsers = ROOT_URL + "GetUsers";
    public static final String URL_GetSeats = ROOT_URL + "GetSeats";
    public static final String URL_ToggleAdmin = ROOT_URL + "EditAdmin";
    public static final String URL_ToggleManagement = ROOT_URL + "EditManagement";
    public static final String URL_DeleteUser = ROOT_URL + "UserDeletion";
    public static final String URL_RecoverDetails = ROOT_URL + "RecoverDetails";
    public static final String URL_SectionCount = ROOT_URL + "CountSections";
    public static final String URL_RoomCall = ROOT_URL + "RoomCall";
    public static final String URL_GetCurrentSeats = ROOT_URL + "GetCurrentSeats";
    public static final String URL_GetALLSectionRooms = ROOT_URL + "AllSectionRoomCall";
}


