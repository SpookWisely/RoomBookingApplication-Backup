package com.example.roombookingapplication;

import android.app.AsyncNotedAppOp;
import android.app.DatePickerDialog;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class UserManagement extends AppCompatActivity {


    private Button b1, b2, b3;
    private String line = null;
    private ArrayList<User> userArrayLists = new ArrayList<User>();
    private InputStream inputStream = null;
    private String urlGetUsers = URLStorage.URL_GetAllUsers;
    private String result = null;
    private TableLayout userLayout;
    private User currentlySelectedUser = new User(0,null,null,null,0,0);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_mangement);
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        getUsers();
        Log.d("Testings", String.valueOf(userArrayLists.size()));
        populateTable();
        b1 = findViewById(R.id.returnAdmin);
        b2 = findViewById(R.id.deleteUserButton);
        b3 = findViewById(R.id.editUserPrivButton);
        userLayout = findViewById(R.id.userTabLayout);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openAdminProfile(); }});


        //Button for Deleting the User
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if (currentlySelectedUser.getFullName() == null) {
                AlertDialog.Builder delConfirm = new AlertDialog.Builder(UserManagement.this);
                delConfirm.setMessage("Are you sure you wish to delete User: " + currentlySelectedUser.getId() +
                        " | This will remove associated data!")
                        .setTitle("Confirm Deletion");

                delConfirm.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    deleteUser();
                    }
                });
                delConfirm.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return; }});
                delConfirm.show();
                    Toast.makeText(UserManagement.this, "Currently Selected User Details are " + currentlySelectedUser.getId() + "   " + currentlySelectedUser.getFullName(), Toast.LENGTH_SHORT).show();
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder editPrivileges = new AlertDialog.Builder(UserManagement.this);
                editPrivileges.setMessage("Make A Selection for User Change " + currentlySelectedUser.getId())
                        .setTitle("Choose Edit");

                editPrivileges.setPositiveButton("Toggle Admin", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    toggleAdmin();
                    }
                });


                editPrivileges.setNeutralButton("Toggle Management", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    toggleManagement();
                    }
                });


                editPrivileges.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                editPrivileges.show();
            }

        });

        /*userLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentText = ((TextView)v).getText().toString();
                Toast.makeText(UserManagement.this, currentText, Toast.LENGTH_SHORT).show();
            }
        }); */

    }

    public void openAdminProfile() {
        Intent adminProfIntent = new Intent(this, AdminPage.class);
        startActivity(adminProfIntent);

    }

    private void getUsers() {
        Log.d("Testing" , "Entered GetUsers Method");
        try {

            URL sectionURL = new URL(urlGetUsers);
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


                int tempID = Integer.parseInt(jsObject.getString("UserID"));
                Log.d("TestingString", "Here is the tempUserID " + tempID);


                String tempFullName = jsObject.getString("FullName");
                Log.d("TestingString", "Here is the tempUser FullName " + tempFullName);


                String tempUserName = jsObject.getString("UserName");
                Log.d("TestingString", "Here is the tempUserName " + tempUserName);


                String tempEmail = jsObject.getString("Email");
                Log.d("TestingString", "Here is the tempEmail " + tempEmail);


                int tempAdmin = Integer.parseInt(jsObject.getString("AdminCheck"));
                Log.d("TestingString", "Here is the tempAdmin " + tempAdmin);


                int tempManagement = Integer.parseInt(jsObject.getString("Management"));
                Log.d("TestingString", "Here is the tempManagement " + tempManagement);

                User tempUser = new User(tempID,tempFullName,tempUserName,tempEmail,tempAdmin,tempManagement);

                Log.d("Testing", "TempUser is equal to :" + tempUser.getId() +  " | " + tempUser.getFullName());
                userArrayLists.add(i,tempUser);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void deleteUser() {
    int UserID = currentlySelectedUser.getId();
    Context context = getApplicationContext();
    User currentlyLoggedUser = SharedPreferenceManager.getInstance(this).getUser();

    if(UserID == 0 ) {
        //Validation for their being a selected User
        Toast.makeText(context, "Please Select A User", Toast.LENGTH_SHORT).show();
        return;
    }

    if(UserID == currentlyLoggedUser.getId()) {
        Toast.makeText(context, "You cannot delete yourself from the Database", Toast.LENGTH_SHORT).show();
        return;
    }


    class deleteUser extends AsyncTask<Void, Void, String> {


        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            Toast.makeText(context, "User With ID of " + UserID + " Has been Deleted", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(getIntent());
        }


        @Override
        protected String doInBackground(Void... voids) {

            RequestHandler reqHandler = new RequestHandler();

            HashMap<String, String> params = new HashMap<>();
            params.put("UserID", String.valueOf(UserID));
            return reqHandler.sendPostsRequests(URLStorage.URL_DeleteUser, params);

        }
    }
    deleteUser du = new deleteUser();
    du.execute();
    }




    private void toggleAdmin() {
        int UserID = currentlySelectedUser.getId();
        Context context = getApplicationContext();

        if(UserID == 0 ) {
            //Validation for their being a selected User
            Toast.makeText(context, "Please Select A User", Toast.LENGTH_SHORT).show();
            return;
        }

        class toggleAdmin extends AsyncTask<Void,Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(context, "User with ID of " + UserID + " Has Toggled Their Admin Privileges ", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler reqHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("UserID", String.valueOf(UserID));
                return reqHandler.sendPostsRequests(URLStorage.URL_ToggleAdmin, params);
            }
        }
        toggleAdmin ta = new toggleAdmin();
                ta.execute();
    }




    private void toggleManagement() {
        int UserID = currentlySelectedUser.getId();
        Context context = getApplicationContext();

        if(UserID == 0 ) {
            //Validation for their being a selected User
            Toast.makeText(context, "Please Select A User", Toast.LENGTH_SHORT).show();
            return;
        }

        class toggleManagement extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(context, "User with ID of " + UserID + " Has Toggled Their Management Privileges ", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler reqHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("UserID", String.valueOf(UserID));
                return reqHandler.sendPostsRequests(URLStorage.URL_ToggleManagement, params);

            }
            }
        toggleManagement tm = new toggleManagement();
        tm.execute();
        }

    private void populateTable() {

        userLayout = findViewById(R.id.userTabLayout);

        for( int i = 0; i < userArrayLists.size(); i++) {
            TableRow userRow = new TableRow(this);
            TableRow.LayoutParams  params1=new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 0.f );
            TableRow.LayoutParams params2=new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

            params1.setMargins(15,5,0,0);

            TextView userIDTxt = new TextView(this);
            userIDTxt.setText(Integer.toString(userArrayLists.get(i).getId()));
            userIDTxt.setLayoutParams(params1);
            userIDTxt.setPadding(4,2,4,2);
            userIDTxt.setGravity(Gravity.CENTER);

            TextView fullNameTxt = new TextView(this);
            fullNameTxt.setText(userArrayLists.get(i).getFullName());
            fullNameTxt.setLayoutParams(params1);
            fullNameTxt.setPadding(4,2,4,2);
            fullNameTxt.setGravity(Gravity.CENTER);

            TextView userNameTxt = new TextView(this);
            userNameTxt.setText(userArrayLists.get(i).getUserName());
            userNameTxt.setLayoutParams(params1);
            userNameTxt.setPadding(4,2,4,2);
            userNameTxt.setGravity(Gravity.CENTER);

            TextView emailTxt = new TextView(this);
            emailTxt.setText(userArrayLists.get(i).getEmail());
            emailTxt.setLayoutParams(params1);
            emailTxt.setPadding(4,2,4,2);
            emailTxt.setGravity(Gravity.CENTER);

            TextView adminTxt = new TextView(this);
            adminTxt.setText(Integer.toString(userArrayLists.get(i).getAdmin()));
            adminTxt.setLayoutParams(params1);
            adminTxt.setPadding(4,2,4,2);
            adminTxt.setGravity(Gravity.CENTER);

            TextView managementTxt = new TextView(this);
            managementTxt.setText(Integer.toString(userArrayLists.get(i).getManagement()));
            managementTxt.setLayoutParams(params1);
            managementTxt.setPadding(4,2,4,2);
            managementTxt.setGravity(Gravity.CENTER);

            userRow.addView(userIDTxt);
            userRow.addView(fullNameTxt);
            userRow.addView(userNameTxt);
            userRow.addView(emailTxt);
            userRow.addView(adminTxt);
            userRow.addView(managementTxt);
            userRow.setClickable(true);
            userRow.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {


                    if (v.isSelected() == false) {
                        v.setBackgroundColor(Color.GRAY);
                        TableRow t = (TableRow) v;
                        TextView userID = (TextView) t.getChildAt(0);
                        TextView fullName = (TextView) t.getChildAt(1);
                        TextView userName = (TextView) t.getChildAt(2);
                        TextView email = (TextView) t.getChildAt(3);
                        TextView admin = (TextView) t.getChildAt(4);
                        TextView management = (TextView) t.getChildAt(5);

                        String selectedUserID = userID.getText().toString();
                        String selectedFullName = fullName.getText().toString();
                        String selectedUserName = userName.getText().toString();
                        String selectedEmail = email.getText().toString();
                        String selectedAdmin = admin.getText().toString();
                        String selectedManagement = management.getText().toString();

                        currentlySelectedUser.setId(Integer.parseInt(selectedUserID));
                        currentlySelectedUser.setFullName(selectedFullName);
                        currentlySelectedUser.setUserName(selectedUserName);
                        currentlySelectedUser.setEmail(selectedEmail);
                        currentlySelectedUser.setAdmin(Integer.parseInt(selectedAdmin));
                        currentlySelectedUser.setManagement(Integer.parseInt(selectedManagement));
                        v.setSelected(true);
                    } else if(v.isSelected() == true) {
                        v.setBackgroundColor(Color.TRANSPARENT);
                        v.setSelected(false);
                    }
                }
            });
            userRow.setId(i);
            userLayout.isColumnShrinkable(1);
            userLayout.isColumnStretchable(2);
            userLayout.isColumnStretchable(3);
            userLayout.isColumnStretchable(4);
            userLayout.isColumnShrinkable(5);
            userLayout.isColumnShrinkable(6);
            //userLayout.setStretchAllColumns(true);
            userLayout.addView(userRow);

        }
    }


}