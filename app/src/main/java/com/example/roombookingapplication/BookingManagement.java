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
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class BookingManagement extends AppCompatActivity {

    private Button b1,b2;
    private String line = null;
    private ArrayList<Booking> bookingArrayList = new ArrayList<Booking>();
    private InputStream inputStream = null;
    private String urlGetBookings = URLStorage.URL_FindAllBookings;
    private String result = null;
    private TableLayout bookingLayout;
    private Booking currentlySelectedBooking = new Booking(0,0,0,0,
            null,null,null,null,null);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_management);
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        b1 = findViewById(R.id.returnAdminBookings);
        b2 = findViewById(R.id.deleteBookingButton);

        getBookings();

        populateTable();
        bookingLayout = findViewById(R.id.bookingTabLayout);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openAdminProfile(); }});

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder delConfirm = new AlertDialog.Builder(BookingManagement.this);
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
                Toast.makeText(BookingManagement.this, "Currently Selected Booking ID is  " + currentlySelectedBooking.getBookingID(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getBookings() {
        Log.d("Testing" , "Entered GetBookings Method");
        try {

            URL sectionURL = new URL(urlGetBookings);
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

                int tempBookingID = Integer.parseInt(jsObject.getString("BookingID"));
                Log.d("Testing", "Here is  tempbooking values : " + tempBookingID );
                int tempUserID = Integer.parseInt(jsObject.getString("UserID"));
                Log.d("Testing", "Here is  tempbooking values : " + tempUserID );
                int tempSectionID = Integer.parseInt(jsObject.getString("SectionID"));
                Log.d("Testing", "Here is  tempbooking values : " + tempSectionID);
                int tempAmountSeats = Integer.parseInt(jsObject.getString("SeatAmount"));
                Log.d("Testing", "Here is  tempbooking values : " + tempAmountSeats );
                String tempCreationDate = jsObject.getString("CreationDate");
                Log.d("Testing", "Here is  tempbooking values : " + tempCreationDate );
                String tempCreationTime = jsObject.getString("CreationTime");
                Log.d("Testing", "Here is  tempbooking values : " + tempCreationTime );
                String tempDateOfBooking = jsObject.getString("DateOfBooking");
                Log.d("Testing", "Here is  tempbooking values : " + tempDateOfBooking );
                String tempBookingStartTime = jsObject.getString("BookingStartTime");
                Log.d("Testing", "Here is  tempbooking values : " +  tempBookingStartTime);
                String tempBookingEndTime = jsObject.getString("BookingEndTime");
                Log.d("Testing", "Here is  tempbooking values : " +  tempBookingEndTime);

                Booking tempBooking = new Booking(tempBookingID,tempUserID,tempSectionID,tempAmountSeats,
                        tempCreationDate,tempCreationTime,
                        tempDateOfBooking,tempBookingStartTime,tempBookingEndTime);
                //Log.d("Testing", "Here is the value of " + tempBooking.getUserID() + tempBooking.getSectionID());
                bookingArrayList.add(i,tempBooking);
                //Log.d("Testing", )
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void populateTable() {
        bookingLayout = findViewById(R.id.bookingTabLayout);

        for( int i = 0; i < bookingArrayList.size(); i++) {
            TableRow bookingRow = new TableRow(this);
            TableRow.LayoutParams  params1=new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT,1);
            TableRow.LayoutParams params2=new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

            params1.setMargins(0,5,0,5);

            TextView bookingID = new TextView(this);
            bookingID.setText(Integer.toString(bookingArrayList.get(i).getBookingID()));
            bookingID.setLayoutParams(params1);
            bookingID.setPadding(0,2,4,2);
            bookingID.setGravity(Gravity.RIGHT);

            TextView userIDTxt = new TextView(this);
            userIDTxt.setText(Integer.toString(bookingArrayList.get(i).getUserID()));
            userIDTxt.setLayoutParams(params1);
            //userIDTxt.setPadding(0,2,4,2);
            userIDTxt.setGravity(Gravity.RIGHT);

            TextView sectionIDTxt = new TextView(this);
            sectionIDTxt.setText(Integer.toString(bookingArrayList.get(i).getSectionID()));
            sectionIDTxt.setLayoutParams(params1);
            sectionIDTxt.setPadding(0,2,4,2);
            sectionIDTxt.setGravity(Gravity.RIGHT);

            TextView amountSeatTxt = new TextView(this);
            amountSeatTxt.setText(Integer.toString(bookingArrayList.get(i).getAmountSeats()));
            amountSeatTxt.setLayoutParams(params1);
           // sectionIDTxt.setPadding(0,2,4,2);
            amountSeatTxt.setGravity(Gravity.RIGHT);

            TextView creationDateTxt = new TextView(this);
            creationDateTxt.setText(bookingArrayList.get(i).getCreationDate());
            creationDateTxt.setLayoutParams(params1);
            //creationDateTxt.setPadding(5,0,0,0);
            creationDateTxt.setGravity(Gravity.RIGHT);

            TextView creationTimeTxt = new TextView(this);
            creationTimeTxt.setText(bookingArrayList.get(i).getCreationTime());
            creationTimeTxt.setLayoutParams(params1);
           // creationTimeTxt.setPadding(0,2,4,2);
            creationTimeTxt.setGravity(Gravity.RIGHT);

            TextView dobTxt = new TextView(this);
            dobTxt.setText(bookingArrayList.get(i).getDateOfBooking());
            dobTxt.setLayoutParams(params1);
           // dobTxt.setPadding(0,2,4,2);
            dobTxt.setGravity(Gravity.RIGHT);

            TextView startTimeTxt = new TextView(this);
            startTimeTxt.setText(bookingArrayList.get(i).getStartTime());
            startTimeTxt.setLayoutParams(params1);
           // startTimeTxt.setPadding(0,2,4,2);
            startTimeTxt.setGravity(Gravity.RIGHT);

            TextView endTimeTxt = new TextView(this);
            endTimeTxt.setText(bookingArrayList.get(i).getEndTime());
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
           // bookingLayout.isColumnShrinkable(1);
           // bookingLayout.isColumnStretchable(2);
           // bookingLayout.isColumnStretchable(3);
           // bookingLayout.isColumnStretchable(4);
           // bookingLayout.isColumnShrinkable(5);
           // bookingLayout.isColumnShrinkable(6);
           // bookingLayout.isColumnShrinkable(7);
           // bookingLayout.isColumnShrinkable(8);
            //userLayout.setStretchAllColumns(true);
            bookingLayout.addView(bookingRow);

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

    public void openAdminProfile() {
        Intent adminProfIntent = new Intent(this, AdminPage.class);
        startActivity(adminProfIntent);

    }
}