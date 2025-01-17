package com.example.roombookingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;

public class SectionCreation extends AppCompatActivity {


    private Button b1,b2;
    private Spinner sp1;
    private EditText et1,et2;
    private int isRoom,isSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_creation);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        b1 = findViewById(R.id.sectionManageButton);
        b2 = findViewById(R.id.sectionSubmitButton);
        sp1 = findViewById(R.id.sectionChoiceSpinner);

        et1 = findViewById(R.id.sectionRoomName);
        et2 = findViewById(R.id.sectionTotalCapacity);
        String[] sectionChoice=getResources().getStringArray(R.array.sectionChoice);
        ArrayAdapter<String> sectionAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, sectionChoice);


        sp1.setAdapter(sectionAdapter);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openSectionManagmentPage();}});


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sp1.getSelectedItemPosition() == 0) {
                    isRoom = 1;
                    isSection = 0;
                } else if(sp1.getSelectedItemPosition() == 1) {
                    isRoom = 0;
                    isSection = 1;
                }
                registerSection();}});
    }

    private void openSectionManagmentPage() {
        Intent secManIntent = new Intent(this, SectionManagement.class);
        startActivity(secManIntent);
    }

    private void registerSection() {

        String section_RoomName  = et1.getText().toString().trim();
        int totalCapacity = Integer.parseInt(et2.getText().toString().trim());
        Context context = getApplicationContext();





        if(section_RoomName != null && totalCapacity != 0) {

            class registerSection extends AsyncTask<Void,Void,String> {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    Toast.makeText(context, "Section Named: " + section_RoomName +
                     " with a capacity of " + totalCapacity + " has been created" , Toast.LENGTH_SHORT).show();
                }

                @Override
                protected String doInBackground(Void... voids) {

                    RequestHandler requestHandler = new RequestHandler();

                    HashMap<String,String> params = new HashMap<>();
                    params.put("Section_RoomName", section_RoomName);
                    params.put("TotalCapacity", String.valueOf(totalCapacity));
                    params.put("IsRoom",String.valueOf(isRoom));
                    params.put("IsSection",String.valueOf(isSection));

                    return requestHandler.sendPostsRequests(URLStorage.URL_SectionCreation, params);

                }

            }
            registerSection rs = new registerSection();
            rs.execute();

        } else {
            Toast.makeText(context, "Enter fill in Missing Fields", Toast.LENGTH_SHORT).show();
            return;
        }

    }

}