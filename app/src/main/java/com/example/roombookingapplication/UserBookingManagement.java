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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class UserBookingManagement extends AppCompatActivity {

    private Button b1,b2;
    private String line = null;
    private ArrayList<Booking> userBookingArrayList = new ArrayList<Booking>();
    private InputStream inputStream = null;
    private String urlGetUserBookings = URLStorage.URL_FindAllBookings;
    private String result = null;
    private TableLayout userBookingLayout;
    private Booking currentlySelectedBooking = new Booking(0,0,0,0,
            null,null,null,null,null);
    User currentUser = SharedPreferenceManager.getInstance(this).getUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_booking_management);
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        b1 = findViewById(R.id.deleteUserBookingButton);
        b2 = findViewById(R.id.returnUserProfileBookings);


        userBookingLayout = findViewById(R.id.userBookingTabLayout);
        getUserBookings();

        populateTable();
        Log.d("Testings", String.valueOf(userBookingArrayList));
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder delConfirm = new AlertDialog.Builder(UserBookingManagement.this);
                delConfirm.setMessage("Are you sure you wish to delete Booking?: " + currentlySelectedBooking.getBookingID() +
                        " | This will remove associated data!")
                        .setTitle("Confirm Deletion");

                delConfirm.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteBooking();
                    }
                });
                delConfirm.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return; }});
                delConfirm.show();
                Toast.makeText(UserBookingManagement.this, "Currently Selected Booking ID is  " + currentlySelectedBooking.getBookingID(), Toast.LENGTH_SHORT).show();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(currentUser.getAdmin() == 1) { openAdminProfile();

            }else if(currentUser.getManagement() == 1) { openManagementProfile();

                }else{ openUserProfile(); }
            }});
    }

    private void getUserBookings() {
        Log.d("Testing" , "Entered GetBookings Method");
        //HashMap<String,String> param = new HashMap<>();
        //param.put("UserID", String.valueOf(currentUser.getId()));

        try {

            URL sectionURL = new URL(urlGetUserBookings);
            HttpURLConnection connection = (HttpURLConnection) sectionURL.openConnection();

            connection.setRequestMethod("POST");
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

                int tempBookingID = Integer.parseInt(jsObject.getString("BookingID"));

                int tempUserID = Integer.parseInt(jsObject.getString("UserID"));

                int tempSectionID = Integer.parseInt(jsObject.getString("SectionID"));

                int tempSeatAmount = Integer.parseInt(jsObject.getString("SeatAmount"));

                String tempCreationDate = jsObject.getString("CreationDate");

                String tempCreationTime = jsObject.getString("CreationTime");

                String tempDateOfBooking = jsObject.getString("DateOfBooking");

                String tempBookingStartTime = jsObject.getString("BookingStartTime");

                String tempBookingEndTime = jsObject.getString("BookingEndTime");

                Booking tempBooking = new Booking(tempBookingID,tempUserID,tempSectionID,tempSeatAmount,
                        tempCreationDate,tempCreationTime,
                        tempDateOfBooking,tempBookingStartTime,tempBookingEndTime);
                if(tempBooking.getUserID() == currentUser.getId()) {


                    userBookingArrayList.add(tempBooking);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void populateTable() {
        userBookingLayout = findViewById(R.id.userBookingTabLayout);

        for(int i = 0; i < userBookingArrayList.size(); i++) {
            TableRow bookingRow = new TableRow(this);
            TableRow.LayoutParams  params1=new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f );
            TableRow.LayoutParams params2=new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

            params1.setMargins(0,5,0,5);

            TextView bookingID = new TextView(this);
            bookingID.setText(Integer.toString(userBookingArrayList.get(i).getBookingID()));
            bookingID.setLayoutParams(params1);
            //bookingID.setPadding(0,2,4,2);
            bookingID.setGravity(Gravity.RIGHT);

            TextView userIDTxt = new TextView(this);
            userIDTxt.setText(Integer.toString(userBookingArrayList.get(i).getUserID()));
            userIDTxt.setLayoutParams(params1);
            //userIDTxt.setPadding(0,2,4,2);
            userIDTxt.setGravity(Gravity.RIGHT);

            TextView sectionIDTxt = new TextView(this);
            sectionIDTxt.setText(Integer.toString(userBookingArrayList.get(i).getSectionID()));
            sectionIDTxt.setLayoutParams(params1);
            sectionIDTxt.setPadding(0,2,0,2);
            sectionIDTxt.setGravity(Gravity.RIGHT);

            TextView amountSeatTxt = new TextView(this);
            amountSeatTxt.setText(Integer.toString(userBookingArrayList.get(i).getAmountSeats()));
            amountSeatTxt.setLayoutParams(params1);
            // sectionIDTxt.setPadding(0,2,4,2);
            amountSeatTxt.setGravity(Gravity.RIGHT);

            TextView creationDateTxt = new TextView(this);
            creationDateTxt.setText(userBookingArrayList.get(i).getCreationDate());
            creationDateTxt.setLayoutParams(params1);
            //creationDateTxt.setPadding(5,0,0,0);
            creationDateTxt.setGravity(Gravity.RIGHT);

            TextView creationTimeTxt = new TextView(this);
            creationTimeTxt.setText(userBookingArrayList.get(i).getCreationTime());
            creationTimeTxt.setLayoutParams(params1);
            // creationTimeTxt.setPadding(0,2,4,2);
            creationTimeTxt.setGravity(Gravity.RIGHT);

            TextView dobTxt = new TextView(this);
            dobTxt.setText(userBookingArrayList.get(i).getDateOfBooking());
            dobTxt.setLayoutParams(params1);
            // dobTxt.setPadding(0,2,4,2);
            dobTxt.setGravity(Gravity.RIGHT);

            TextView startTimeTxt = new TextView(this);
            startTimeTxt.setText(userBookingArrayList.get(i).getStartTime());
            startTimeTxt.setLayoutParams(params1);
            // startTimeTxt.setPadding(0,2,4,2);
            startTimeTxt.setGravity(Gravity.RIGHT);

            TextView endTimeTxt = new TextView(this);
            endTimeTxt.setText(userBookingArrayList.get(i).getEndTime());
            endTimeTxt.setLayoutParams(params1);
            // endTimeTxt.setPadding(0,2,4,2);
            endTimeTxt.setGravity(Gravity.CENTER);

            bookingRow.addView(bookingID);
            bookingRow.addView(userIDTxt);
            bookingRow.addView(sectionIDTxt);
            bookingRow.addView(amountSeatTxt);
            bookingRow.addView(creationDateTxt);
            bookingRow.addView(creationTimeTxt);
            bookingRow.addView(dobTxt);
            bookingRow.addView(startTimeTxt);
            bookingRow.addView(endTimeTxt);


            bookingRow.setClickable(true);
            bookingRow.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {


                    if (v.isSelected() == false) {
                        v.setBackgroundColor(Color.GRAY);
                        TableRow t = (TableRow) v;

                        TextView bookingID = (TextView) t.getChildAt(0);
                        TextView userID = (TextView) t.getChildAt(1);
                        TextView sectionID = (TextView) t.getChildAt(2);
                        TextView amountSeats = (TextView) t.getChildAt(3);
                        TextView creationDate= (TextView) t.getChildAt(4);
                        TextView creationTime = (TextView) t.getChildAt(5);
                        TextView dateOfBooking = (TextView) t.getChildAt(6);
                        TextView bookingStartTime = (TextView) t.getChildAt(7);
                        TextView bookingEndTime = (TextView) t.getChildAt(8);


                        String selectedBookingID = bookingID.getText().toString();
                        String selectedUserID = userID.getText().toString();
                        String selectedSectionID = sectionID.getText().toString();
                        String selectedAmountSeat = amountSeats.getText().toString();
                        String selectedCreationDate = creationDate.getText().toString();
                        String selectedCreationTime = creationTime.getText().toString();
                        String selectedDateOfBooking = dateOfBooking.getText().toString();
                        String selectedBookingStartTime = bookingStartTime.getText().toString();
                        String selectedBookingEndTime = bookingEndTime.getText().toString();

                        currentlySelectedBooking.setBookingID((Integer.parseInt(selectedBookingID)));
                        currentlySelectedBooking.setUserID((Integer.parseInt(selectedUserID)));
                        currentlySelectedBooking.setSectionID((Integer.parseInt(selectedSectionID)));
                        currentlySelectedBooking.setAmountSeats(((Integer.parseInt(selectedAmountSeat))));

                        currentlySelectedBooking.setCreationDate(selectedCreationDate);
                        currentlySelectedBooking.setCreationTime(selectedCreationTime);
                        currentlySelectedBooking.setDateOfBooking(selectedDateOfBooking);
                        currentlySelectedBooking.setStartTime(selectedBookingStartTime);
                        currentlySelectedBooking.setEndTime(selectedBookingEndTime);
                        v.setSelected(true);
                    } else if(v.isSelected() == true) {
                        v.setBackgroundColor(Color.TRANSPARENT);
                        v.setSelected(false);
                    }
                }
            });
            bookingRow.setId(i);
           // userBookingLayout.isColumnShrinkable(1);
           // userBookingLayout.isColumnStretchable(2);
           // userBookingLayout.isColumnStretchable(3);
           // userBookingLayout.isColumnStretchable(4);
           // userBookingLayout.isColumnShrinkable(5);
           // userBookingLayout.isColumnShrinkable(6);
           // userBookingLayout.isColumnShrinkable(7);
          //  userBookingLayout.isColumnShrinkable(8);
            //userLayout.setStretchAllColumns(true);
            userBookingLayout.addView(bookingRow);

        }
    }


    private void deleteBooking() {
        int bookingID = currentlySelectedBooking.getBookingID();
        Context context = getApplicationContext();

        if(bookingID == 0 ) {
            Toast.makeText(context, "Please Select A Booking", Toast.LENGTH_SHORT).show();
            return;
        }

        class deleteBooking extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            protected void onPostExecute(String string) {
                super.onPostExecute(string);
                Toast.makeText(context, "Booking With ID of " + bookingID + " Has been Deleted", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            }


            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler reqHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("BookingID", String.valueOf(bookingID));
                return reqHandler.sendPostsRequests(URLStorage.URL_BookingDeletion, params);
            }
        }
        deleteBooking db = new deleteBooking();
        db.execute();

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
}