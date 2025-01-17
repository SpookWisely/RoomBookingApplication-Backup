package com.example.roombookingapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CrashPage extends AppCompatActivity {
    Button profileButton, bookingsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_page);
        profileButton = (Button) findViewById(R.id.ProfileButton);
        bookingsButton = (Button) findViewById(R.id.CreateBookingButton);

        if (!SharedPreferenceManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this ,LoginScreen.class));
        }

        User currentUser = SharedPreferenceManager.getInstance(this).getUser();
        profileButton.setText("UserProfile: " + currentUser.getId());

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser.getAdmin() == 1) { openAdminProfile();
                }else if(currentUser.getManagement() == 1) { openManagementProfile();
                }else{ openUserProfile(); }
            }
        });

        bookingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder bookingConfirm = new AlertDialog.Builder(CrashPage.this);
                bookingConfirm.setMessage("Would you like to book a seat in a section or a room?")
                        .setTitle("Please Choose!");

                bookingConfirm.setPositiveButton("Section", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { openSectionBookingPage();
                    }
                });

                bookingConfirm.setNeutralButton("Room", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    openBookingPage();
                    }
                });

                bookingConfirm.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return; }});
                bookingConfirm.show();
            }});
    }

    public void openUserProfile() {
        Intent userProfIntent = new Intent(this, UserProfilePage.class);
        startActivity(userProfIntent);
    }

    public void openAdminProfile() {
        Intent adminProfIntent = new Intent(this, AdminPage.class);
        startActivity(adminProfIntent);
    }
    public void openManagementProfile() {
        Intent managementProfIntent = new Intent(this, MangementPage.class);
        startActivity(managementProfIntent);
    }

    public void openSectionBookingPage() {
        Intent sectionBookingIntent = new Intent(this , SectionBooking.class);
        startActivity(sectionBookingIntent);
    }
    public void openBookingPage() {
        Intent bookingIntent = new Intent( this, BookingPage.class);
        startActivity(bookingIntent);
    }
}