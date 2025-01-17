package com.example.roombookingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserProfilePage extends AppCompatActivity {

    private Button signOutButton, returnButton, manageButton;
    private TextView dynUserID, dynFullName, dynEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_page);
        User currentUser = SharedPreferenceManager.getInstance(this).getUser();
        signOutButton = (Button) findViewById(R.id.LogOutButton);
        returnButton = (Button) findViewById(R.id.ReturnToCrashButt);
        manageButton = (Button) findViewById(R.id.manage_UserBookings);
        dynUserID= (TextView) findViewById(R.id.userIDChange);
        dynFullName= (TextView) findViewById(R.id.fullNameChange);
        dynEmail= (TextView) findViewById(R.id.emailChange);

        dynUserID.setText(Integer.toString(currentUser.getId()));
        dynFullName.setText(currentUser.getFullName());
        dynEmail.setText(currentUser.getEmail());
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceManager.getInstance(getApplicationContext()).logout();
                finish();
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCrashPage();
            }
        });

        manageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openManagementPage();
            }
        });
    }


    public void openManagementPage() {
        Intent manageIntent = new Intent(this, UserBookingManagement.class);
        startActivity(manageIntent);
    }

    public void openCrashPage() {
        Intent crashIntent = new Intent(this, CrashPage.class);
        startActivity(crashIntent);
    }
}

