package com.example.roombookingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MangementPage extends AppCompatActivity {

    private Button b1,b2,b3,b4,b5;
    private TextView t1,t2,t3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mangement_page);

        User currentUser = SharedPreferenceManager.getInstance(this).getUser();
        t1 = findViewById(R.id.managementIDChange);
        t2 = findViewById(R.id.managementFullNameChange);
        t3 = findViewById(R.id.managementEmailChange);

        t1.setText(String.valueOf(currentUser.getId()));
        t2.setText(currentUser.getFullName());
        t3.setText(currentUser.getEmail());

        b1 = findViewById(R.id.ReturnToCrashButtManage);
        b2 = findViewById(R.id.getMostBookedSection);
        b3 = findViewById(R.id.getMostBookedTime);
        b4 = findViewById(R.id.manageYourBookingButt);
        b5 = findViewById(R.id.LogOutManageButton);

    b1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {openCrashPage(); }});


    b2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {openSectionCount(); }});

    b3.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(MangementPage.this, "Feature Not Currently Available", Toast.LENGTH_SHORT).show();
        }});

    b4.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) { openManagementPage();}});

    b5.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferenceManager.getInstance(getApplicationContext()).logout();
            finish();
        }});
    }


    public void openSectionCount() {
     Intent secCountIntent = new Intent(this, SectionLeaderBoard.class);
     startActivity(secCountIntent);
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