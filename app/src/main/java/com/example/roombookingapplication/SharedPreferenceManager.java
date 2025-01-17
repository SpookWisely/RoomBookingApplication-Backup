package com.example.roombookingapplication;
//https://www.simplifiedcoding.net/android-login-and-registration-tutorial/#Creating-a-new-PHP-Project
//Link above is what code is based off.
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;

public class SharedPreferenceManager {

    //User Variables for use in methods such as login/logout etc
    private static final String SHARED_PREFERENCE_NAME = "BookingPref";
    private static final String KEY_FULLNAME = "keyfullName";
    private static final String KEY_USERNAME = "keyuserName";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_ID = "keyid";
    private static final String KEY_ADMIN = "keyadmin";
    private static final String KEY_MANAGEMENT = "keymanagement";
    //Section Variables for
    private static final String KEY_SECTIONID = "keysectionid";
    private static final String KEY_SECTION_ROOMNAME = "keysection_roomname";
    private static final String KEY_TOTALCAPACITY = "keytotalcapacity";
    private static final String KEY_ISROOM = "keyisroom";
    private static final String KEY_ISSECTION = "keyissection";

    private static Context machineContext;
    private static SharedPreferenceManager sharedInstance;

    private SharedPreferenceManager(Context context) {
        machineContext = context;
    }

    public static synchronized SharedPreferenceManager getInstance(Context context) {
        if(sharedInstance == null) {
            sharedInstance = new SharedPreferenceManager(context);
        }
        return sharedInstance;
    }

    public void userLogin(User user) {
        SharedPreferences sharePreferences = machineContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharePreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_FULLNAME, user.getFullName());
        editor.putString(KEY_USERNAME, user.getUserName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putInt(KEY_ADMIN, user.getAdmin());
        editor.putInt(KEY_MANAGEMENT, user.getManagement());
        editor.apply();
    }

    public void sectionCreation(Section section) {
        SharedPreferences sharePreferences = machineContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharePreferences.edit();
        editor.putInt(KEY_SECTIONID, section.getSectionID());
        editor.putString(KEY_SECTION_ROOMNAME, section.getSection_RoomName());
        editor.putInt(KEY_TOTALCAPACITY, section.getTotalCapacity());
        editor.apply();
    }
    public boolean isLoggedIn() {
        SharedPreferences sharePreferences = machineContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharePreferences.getString(KEY_USERNAME,null) != null;
    }

public User getUser() {
    SharedPreferences sharePreferences = machineContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return new User (
                sharePreferences.getInt(KEY_ID, -1),
                sharePreferences.getString(KEY_FULLNAME,null),
                sharePreferences.getString(KEY_USERNAME,null),
                sharePreferences.getString(KEY_EMAIL,null),
                sharePreferences.getInt(KEY_ADMIN, 0),
                sharePreferences.getInt(KEY_MANAGEMENT, 0)
    );
}
public Section getSection() {
    SharedPreferences sharePreferences = machineContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    return new Section (
            sharePreferences.getInt(KEY_SECTIONID, -1),
            sharePreferences.getString(KEY_SECTION_ROOMNAME, null),
            sharePreferences.getInt(KEY_TOTALCAPACITY, 1),
            sharePreferences.getInt(KEY_ISROOM,0),
            sharePreferences.getInt(KEY_ISSECTION,0)

            );

}

public void logout() {
    SharedPreferences sharePreferences = machineContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharePreferences.edit();
    //Clears the currently logged in user.
    editor.clear();
    //Applies the changes
    editor.apply();
    machineContext.startActivity(new Intent(machineContext,LoginScreen.class));

}
}
