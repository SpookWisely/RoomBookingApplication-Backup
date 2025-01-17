package com.example.roombookingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SectionLeaderBoard extends AppCompatActivity {

    private TableLayout leaderBoardLayout;
    private Button b1;
    private String line = null;
    private ArrayList<Entry> countArrayList = new ArrayList<Entry>();
    private InputStream inputStream = null;
    private String urlSectionCount = URLStorage.URL_SectionCount;
    private String result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_leader_board);

        b1 = findViewById(R.id.returnButton);
        countSections();

        populateTable();
        leaderBoardLayout = findViewById(R.id.leaderBoardTabLayout);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openManagementProfile();}});

    }

public void countSections() {
    Log.d("Testing" , "Entered countSections Method");
    try {

        URL sectionURL = new URL(urlSectionCount);
        HttpURLConnection connection = (HttpURLConnection) sectionURL.openConnection();

        connection.setRequestMethod("GET");
        inputStream = new BufferedInputStream(connection.getInputStream());

    } catch (Exception e) {
        e.printStackTrace();
    }
    try {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((inputStream)));
        StringBuilder stringBuilder = new StringBuilder();

        while((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line + "\n");

        }
        inputStream.close();
        result = stringBuilder.toString();
    } catch (Exception e) {
        e.printStackTrace();
    }
    try {
        JSONArray jsArray = new JSONArray(result);
        JSONObject jsObject = null;
        Log.d("Testing" , "Just Before For Loop");
        Log.d("Testings", String.valueOf(jsArray.length()));
        for(int i = 0; i <jsArray.length(); i++) {
            Log.d("Testing", "Has entered the For Loop");

            jsObject = jsArray.getJSONObject(i);

            int tempID = Integer.parseInt(jsObject.getString("SectionID"));
            Log.d("TestingString", "Here is the tempSectionID " + tempID);

            int tempCount = Integer.parseInt(jsObject.getString("count(*)"));
            Log.d("TestingString", "Here is the tempSection Total Capacity " + tempCount);

            Entry tempEntry = new Entry(tempID,tempCount);
            countArrayList.add(i,tempEntry);

        }
    } catch (Exception e) {
        e.printStackTrace();
    }

}


public void populateTable() {
    leaderBoardLayout = findViewById(R.id.leaderBoardTabLayout);
    for( int i = 0; i < countArrayList.size(); i++) {
        TableRow sectionRow = new TableRow(this);
        TableRow.LayoutParams  params1=new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 0.8f );
        TableRow.LayoutParams params2=new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

        params1.setMargins(0,5,0,0);
        params2.setMargins(0,5,0,0);
        TextView SectionIDText = new TextView(this);
        SectionIDText.setText(Integer.toString(countArrayList.get(i).getSectionID()));
        SectionIDText.setLayoutParams(params1);
        SectionIDText.setPadding(10,2,4,15);
        SectionIDText.setGravity(Gravity.CENTER);

        TextView sectionCountTxt = new TextView(this);
        sectionCountTxt.setText(Integer.toString(countArrayList.get(i).getCount()));
        sectionCountTxt.setLayoutParams(params2);
        sectionCountTxt.setPadding(10,5,4,15);
        sectionCountTxt.setGravity(Gravity.CENTER_HORIZONTAL);


        sectionRow.addView(SectionIDText);
        sectionRow.addView(sectionCountTxt);


        sectionRow.setId(i);

       // leaderBoardLayout.isColumnStretchable(1);
       // leaderBoardLayout.isColumnStretchable(2);
        //userLayout.setStretchAllColumns(true);
        leaderBoardLayout.addView(sectionRow);

    }
}


    public void openManagementProfile() {
        Intent managementProfIntent = new Intent(this, MangementPage.class);
        startActivity(managementProfIntent);
    }

}