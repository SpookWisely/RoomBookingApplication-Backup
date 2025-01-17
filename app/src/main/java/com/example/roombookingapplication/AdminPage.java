package com.example.roombookingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class AdminPage extends AppCompatActivity {

    private Button signOutButton, returnToCrash,manageUsersButton ,manageBookingsButton, manageSectionsButton,manageYourBookingsButton ;
    private TextView dynUserID, dynFullName, dynEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page2);

        User currentUser = SharedPreferenceManager.getInstance(this).getUser();
        signOutButton = (Button) findViewById(R.id.LogOutButton);
        manageBookingsButton = (Button) findViewById(R.id.manage_AllBookings);
        manageSectionsButton = (Button) findViewById(R.id.manage_AllSections);
        manageUsersButton = (Button) findViewById(R.id.manage_AllUsers);
        returnToCrash =(Button) findViewById(R.id.ReturnToCrashButt);
        dynUserID= (TextView) findViewById(R.id.userIDChange);
        dynFullName= (TextView) findViewById(R.id.fullNameChange);
        dynEmail= (TextView) findViewById(R.id.emailChange);
        manageYourBookingsButton = (Button) findViewById(R.id.manageYourAdminBookings);

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

        manageUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            openUserManagementPage();
            }
        });
        manageSectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            openSectionManagementPage();
            }
        });
        manageBookingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            openBookingManagementPage();
            }
        });
        returnToCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCrashPage();
            }
        });

        manageYourBookingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openManagementPage();}});
    }

    public void openManagementPage() {
        Intent manageIntent = new Intent(this, UserBookingManagement.class);
        startActivity(manageIntent);
    }

    public void openCrashPage() {
        Intent intent = new Intent(this, CrashPage.class);
        startActivity(intent);
    }
    public void openUserManagementPage() {
        Intent intent = new Intent(this, UserManagement.class);
        startActivity(intent);
    }
    public void openSectionManagementPage() {
        Intent intent = new Intent(this, SectionManagement.class);
        startActivity(intent);
    }
    public void openBookingManagementPage() {
        Intent intent = new Intent(this, BookingManagement.class);
        startActivity(intent);
    }
}