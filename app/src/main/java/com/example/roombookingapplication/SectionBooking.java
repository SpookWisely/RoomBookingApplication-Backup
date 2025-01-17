package com.example.roombookingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;

public class SectionBooking extends AppCompatActivity {


    private Spinner sp1, sp2;
    private Button b1;
    private TextView t1,t2,t3,t4;
    private String urlSectionGather = URLStorage.URL_SectionGather;
    private ArrayAdapter<String> sectionAdapter;
    private InputStream inputStream = null;
    private String line = null;
    private String result = null;
    private String[] sections;
    private Integer[] seatArray;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private User currentUser = SharedPreferenceManager.getInstance(this).getUser();
    private boolean isChanged = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_booking);
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        b1 = findViewById(R.id.sectionSubmitBooking);
        sp1 = findViewById(R.id.SectionSelection);
        sp2 = findViewById(R.id.sectionTimeSpinner);

        t1 = findViewById(R.id.sectionSeatsText);
        t2 = findViewById(R.id.DateText);
        t3 = findViewById(R.id.currentSeatsText);
        t4 = findViewById(R.id.sectionAmountOfSeats);

        String[] times=getResources().getStringArray(R.array.times_List);
        ArrayAdapter<String> timesAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, times);


        sp2.setAdapter(timesAdapter);
        getSections();
        //Log.d("Testing", String.valueOf(sections));
        sectionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,sections);
        sp1.setAdapter(sectionAdapter);


        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getSeats();
                getCurrentSeats();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            } });


        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(t2.getText() != "Please Select A Date" && sp1.getSelectedItem() != "00:00:00") {
                    getCurrentSeats();

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }});



        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar dateCalendar = Calendar.getInstance();
                int year = dateCalendar.get(Calendar.YEAR);
                int month = dateCalendar.get(Calendar.MONTH);
                int day = dateCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        SectionBooking.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener, year, month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                //);
                getCurrentSeats();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = year + "-" + month + "-" + day;
                Log.d("Testing", date);
                t2.setText(date);
                isChanged = true;
               // getCurrentSeats();
            }
        };

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    submitBooking();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } });

    }

    private void getSections ()  {
        try {

            URL sectionURL = new URL(urlSectionGather);
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
            sections = new String[jsArray.length()];

            for(int i = 0; i <jsArray.length(); i++) {
                jsObject = jsArray.getJSONObject(i);

                sections[i] = jsObject.getString("Section_RoomName");


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCurrentSeats() {
        String sectionName = sp1.getSelectedItem().toString();
        String time = sp2.getSelectedItem().toString();
        String date = t2.getText().toString();

        class getCurrentSeats extends AsyncTask<Void,Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.d("Testing", "Current Seats Response  " + s);
                try {
                    JSONObject object = new JSONObject(s);

                    if(!object.getBoolean("error")) {
                        JSONObject seatJSON = object.getJSONObject("Section");

                        int currentSeats = seatJSON.getInt("CurrentCapacity");
                        seatArray = new Integer[currentSeats];
                       // Log.d("Testing" , String.valueOf(currentSeats));
                        for (int i = 0; i < currentSeats; i++) {
                        seatArray[i] = i;
                        }


                        t3.setText(Integer.toString(currentSeats));
                        Log.d("Testing" , "Gotten Section and Exited Method");

                        //t1.setText(section.getTotalCapacity());

                    } } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();

                HashMap<String,String> parameters = new HashMap<>();
                parameters.put("Section_RoomName", sectionName);
                parameters.put("DateOfBooking", date);
                parameters.put("BookingStartTime", time);


                return requestHandler.sendPostsRequests(URLStorage.URL_GetCurrentSeats, parameters);

            }
        }
        getCurrentSeats gcs = new getCurrentSeats();
        gcs.execute();
    }


    private void getSeats() {
        String sectionName = sp1.getSelectedItem().toString();

        class getSeats extends AsyncTask<Void,Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.d("Testing", "Get Seats Response " + s);
                try{
                    JSONObject object = new JSONObject(s);

                    if(!object.getBoolean("error")) {
                        JSONObject sectionJSON = object.getJSONObject("Section");

                        Section section = new Section (
                                sectionJSON.getInt("SectionID"),
                                sectionJSON.getString("Section_RoomName"),
                                sectionJSON.getInt("TotalCapacity"),
                                sectionJSON.getInt("isRoom"),
                                sectionJSON.getInt("isSection")
                        );

                        SharedPreferenceManager.getInstance(getApplicationContext()).sectionCreation(section);
                        Section currentSection = SharedPreferenceManager.getInstance(getApplicationContext()).getSection();
                        t1.setText(Integer.toString(currentSection.getTotalCapacity()));
                        Log.d("Testing" , "Gotten Section and Exited Method");

                        //t1.setText(section.getTotalCapacity());

                    } } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();

                HashMap<String,String> parameters = new HashMap<>();
                parameters.put("Section_RoomName", sectionName);

                return requestHandler.sendPostsRequests(URLStorage.URL_GetSeats, parameters);

            }
        }
        getSeats gs = new getSeats();
        gs.execute();
    }

    private void submitBooking() throws ParseException {
        Section selectedSection = SharedPreferenceManager.getInstance(getApplicationContext()).getSection();
        int userID = currentUser.getId();
        String userIDString = String.valueOf(userID);
        int sectionID = selectedSection.getSectionID();

        String amountOfSeatsString = t4.getText().toString();

        if (isChanged == false) {
            Toast.makeText(this, "Please Select a Date!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(t4.length() == 0) {
            Toast.makeText(this, "Amount of Seats is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        int amountOfSeatsInt = Integer.parseInt(amountOfSeatsString);

        String currentCapacityString = (String) t3.getText();
        int currentCapacity = Integer.parseInt(currentCapacityString);

        String sectionIDString = String.valueOf(sectionID);
        String bookingDateText = (String) t2.getText();
        String bookingStartTime = (String) sp2.getSelectedItem();
        String bookingEndTime = null;



        if(amountOfSeatsInt > currentCapacity) {
            Toast.makeText(this, "Amount of seats is larger than available capacity", Toast.LENGTH_SHORT).show();
            return;
        }



        if(sp2.getSelectedItemPosition() == 23) {
            bookingEndTime = (String) sp2.getItemAtPosition(0);

        } else {

            int posPlus = sp2.getSelectedItemPosition() + 1;
            bookingEndTime = (String) sp2.getItemAtPosition(posPlus);
        }


        Log.d("Testing", String.valueOf(userID));
        Log.d("Testing", String.valueOf(sectionID));
        Log.d("Testing",amountOfSeatsString);
        Log.d("Testing", bookingDateText);
        Log.d("Testing", bookingStartTime);
        Log.d("Testing", bookingEndTime);

        String finalBookingEndTime = bookingEndTime;
        class SubmitBooking extends AsyncTask<Void, Void, String> {

            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler reqHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("UserID",userIDString);
                params.put("SectionID",sectionIDString);
                params.put("SeatAmount",amountOfSeatsString);
                params.put("DateOfBooking",bookingDateText);
                params.put("BookingStartTime", bookingStartTime);
                params.put("BookingEndTime", finalBookingEndTime);

                return reqHandler.sendPostsRequests(URLStorage.URL_BookingCreation,params);
            }

            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.d("Testing", "Value of String on PostExecute " + s);
                try {
                    JSONObject obj = new JSONObject(s);

                    if(!obj.getBoolean("error")) {

                        Toast.makeText(getApplicationContext(), "Booking Created Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                        openCrashPage();
                    } else {
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        SubmitBooking sb = new SubmitBooking();
        sb.execute();

    }
    public void openCrashPage() {
        Intent crashIntent = new Intent(this, CrashPage.class);
        startActivity(crashIntent);
    }
}