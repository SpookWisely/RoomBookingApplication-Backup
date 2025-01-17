package com.example.roombookingapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class SectionManagement extends AppCompatActivity {


    private TableLayout sectionLayout;
    private Button b1,b2,b3;
    private String line = null;
    private ArrayList<Section> sectionArrayLists = new ArrayList<Section>();
    private InputStream inputStream = null;
    private String urlGetSections = URLStorage.URL_GetALLSectionRooms;
    private String result = null;
    private Section currentlySelectedSection = new Section(0,null,0,0,0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_management);
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        getSections();
        Log.d("Testings", String.valueOf(sectionArrayLists.size()));
        populateTable();


        b1 = findViewById(R.id.returnSectionAdminSection);
        b2 = findViewById(R.id.addSectionButton);
        b3 = findViewById(R.id.deleteSectionButton);
        sectionLayout = findViewById(R.id.sectionTabLayout);

        //Return Button
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openAdminProfile(); }});


        //Go to Section Creation Page
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openSectionCreation(); }});


        //Button to Confirm deletion of selected Section
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  AlertDialog.Builder delConfirm = new AlertDialog.Builder(SectionManagement.this);
                delConfirm.setMessage("Are you sure you wish to delete Section: " + currentlySelectedSection.getSectionID() +
                        " | This will remove associated data!")
                        .setTitle("Confirm Deletion");

                delConfirm.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteSection();
                    }
                });
                delConfirm.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return; }});
                delConfirm.show();
                Toast.makeText(SectionManagement.this, "Currently Selected Section Details are " + currentlySelectedSection.getSectionID() + "   " + currentlySelectedSection.getSection_RoomName(), Toast.LENGTH_SHORT).show();}});
    }




    private void deleteSection() {
        int sectionID = currentlySelectedSection.getSectionID();
        Context context = getApplicationContext();

        if(sectionID == 0) {
            Toast.makeText(context, "Please Select A Section", Toast.LENGTH_SHORT).show();
            return;
        }

        class deleteSection extends AsyncTask<Void, Void, String> {



            protected void onPreExecute() {
                super.onPreExecute();
            }

            protected void onPostExecute(String string) {
                super.onPostExecute(string);
                Toast.makeText(context, "Section With ID of " + sectionID + " Has been Deleted", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            }
            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("SectionID", String.valueOf(sectionID));

                return requestHandler.sendPostsRequests(URLStorage.URL_SectionDeletion, params);
            }

        }
        deleteSection ds = new deleteSection();
        ds.execute();
    }


    private void getSections() {
        Log.d("Testing" , "Entered GetSections Method");
        try {

            URL sectionURL = new URL(urlGetSections);
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


                String tempSectionName = jsObject.getString("Section_RoomName");
                Log.d("TestingString", "Here is the tempSection Section_RoomName " + tempSectionName);


                int tempTotalCapacity = Integer.parseInt(jsObject.getString("TotalCapacity"));
                Log.d("TestingString", "Here is the tempSection Total Capacity " + tempTotalCapacity);

                int tempIsRoom = Integer.parseInt(jsObject.getString("IsRoom"));

                int tempIsSection = Integer.parseInt(jsObject.getString("IsSection"));

                Section tempSection = new Section(tempID,tempSectionName,tempTotalCapacity,tempIsRoom,tempIsSection);
                sectionArrayLists.add(i,tempSection);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private void populateTable() {
        sectionLayout = findViewById(R.id.sectionTabLayout);

        for( int i = 0; i < sectionArrayLists.size(); i++) {
            TableRow sectionRow = new TableRow(this);
            TableRow.LayoutParams  params1=new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.f );
            TableRow.LayoutParams params2=new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

            params1.setMargins(0,5,0,0);

            TextView SectionIDText = new TextView(this);
            SectionIDText.setText(Integer.toString(sectionArrayLists.get(i).getSectionID()));
            SectionIDText.setLayoutParams(params1);
            SectionIDText.setPadding(4,2,4,2);
            SectionIDText.setGravity(Gravity.CENTER);

            TextView sectionRoomNameText = new TextView(this);
            sectionRoomNameText.setText(sectionArrayLists.get(i).getSection_RoomName());
            sectionRoomNameText.setLayoutParams(params1);
            sectionRoomNameText.setPadding(4,2,4,2);
            sectionRoomNameText.setGravity(Gravity.CENTER);

            TextView sectionTotalCapText = new TextView(this);
            sectionTotalCapText.setText(Integer.toString(sectionArrayLists.get(i).getTotalCapacity()));
            sectionTotalCapText.setLayoutParams(params1);
            sectionTotalCapText.setPadding(4,2,4,2);
            sectionTotalCapText.setGravity(Gravity.CENTER);

            TextView sectionIsRoomTxt = new TextView(this);
            sectionIsRoomTxt.setText(Integer.toString(sectionArrayLists.get(i).getIsRoom()));
            sectionIsRoomTxt.setLayoutParams(params1);
            sectionIsRoomTxt.setPadding(4,2,4,2);
            sectionIsRoomTxt.setGravity(Gravity.CENTER);

            TextView sectionIsSectionTxt = new TextView(this);
            sectionIsSectionTxt.setText(Integer.toString(sectionArrayLists.get(i).getIsSection()));
            sectionIsSectionTxt.setLayoutParams(params1);
            sectionIsSectionTxt.setPadding(4,2,4,2);
            sectionIsSectionTxt.setGravity(Gravity.CENTER);

            sectionRow.addView(SectionIDText);
            sectionRow.addView(sectionRoomNameText);
            sectionRow.addView(sectionTotalCapText);
            sectionRow.addView(sectionIsRoomTxt);
            sectionRow.addView(sectionIsSectionTxt);
            
            sectionRow.setClickable(true);
            sectionRow.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {


                    if (v.isSelected() == false) {
                        v.setBackgroundColor(Color.GRAY);
                        TableRow t = (TableRow) v;
                        TextView sectionID = (TextView) t.getChildAt(0);
                        TextView section_RoomName = (TextView) t.getChildAt(1);
                        TextView sectionTotalCapacity = (TextView) t.getChildAt(2);
                        TextView sectionIsRoom = (TextView) t.getChildAt(3);
                        TextView sectionIsSection = (TextView) t.getChildAt(4);

                        String selectedSectionID = sectionID.getText().toString();
                        String selectedSection_RoomName = section_RoomName.getText().toString();
                        String selectedSectionTotalCap = sectionTotalCapacity.getText().toString();
                        String selectedSectionIsRoom = sectionIsRoom.getText().toString();
                        String selectedSectionIsSection = sectionIsSection.getText().toString();

                        currentlySelectedSection.setSectionID(Integer.parseInt(selectedSectionID));
                        currentlySelectedSection.setSection_RoomName(selectedSection_RoomName);
                        currentlySelectedSection.setTotalCapacity(Integer.parseInt(selectedSectionTotalCap));
                        currentlySelectedSection.setIsRoom(Integer.parseInt(selectedSectionIsRoom));
                        currentlySelectedSection.setIsSection(Integer.parseInt(selectedSectionIsSection));


                        v.setSelected(true);
                    } else if(v.isSelected() == true) {
                        v.setBackgroundColor(Color.TRANSPARENT);
                        v.setSelected(false);
                    }
                }
            });
            sectionRow.setId(i);
            sectionLayout.isColumnShrinkable(1);
            sectionLayout.isColumnStretchable(2);
            sectionLayout.isColumnStretchable(3);
            sectionLayout.isColumnStretchable(4);
            sectionLayout.isColumnStretchable(5);

            //userLayout.setStretchAllColumns(true);
            sectionLayout.addView(sectionRow);

        }
    }




    public void openAdminProfile() {
        Intent adminProfIntent = new Intent(this, AdminPage.class);
        startActivity(adminProfIntent);

    }
    public void openSectionCreation() {
        Intent sectionCreationIntent = new Intent(this, SectionCreation.class);
        startActivity(sectionCreationIntent);

    }
}