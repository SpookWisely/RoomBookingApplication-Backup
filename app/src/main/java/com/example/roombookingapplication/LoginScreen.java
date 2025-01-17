package com.example.roombookingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginScreen extends AppCompatActivity {
    EditText t1, t2;
    Button b1, b2, b3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        b1 = (Button) findViewById(R.id.regButton);
        b2 = (Button) findViewById(R.id.forgotDetailsButton);
        b3 = (Button) findViewById(R.id.loginButton);
        t1 = (EditText) findViewById(R.id.UserNameText);
        t2 = (EditText) findViewById(R.id.PasswordText);

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegPage();
            }

        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openRecDetailsPage(); }});
    }


    public boolean isntEmpty(String text) {
        return text.trim().length() > 0;
    }


    //Method for Logging in the User
    private void loginUser() {
        String userName = t1.getText().toString().trim();
        String passWord = t2.getText().toString().trim();
        Context context = getApplicationContext();
        //validation Checks
        if (isntEmpty(userName) || isntEmpty(passWord)) {
            Log.d("LogTesting", "UserName and Password have been inputted.");
        } else {
            Toast.makeText(context, "Please Fill in Missing Fields", Toast.LENGTH_SHORT).show();
            return;
        }


        class LoginUser extends AsyncTask<Void, Void, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                Log.d("LogTesting", "Entered PreExecute");

            }

            @Override
            protected void onPostExecute(String string) {

                super.onPostExecute(string);
                Log.d("LogTesting", "Entered Post Execute");

                try {
                    Log.d("LogTesting", string);
                    JSONObject object = new JSONObject(string);

                    Log.d("LogTesting", "Just Before If Statement");

                    if (!object.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();

                        Log.d("LogTesting", "Passed Error If Statement");

                        JSONObject userJson = object.getJSONObject("user");

                        User user = new User(
                                userJson.getInt("userid"),
                                userJson.getString("FullName"),
                                userJson.getString("username"),
                                userJson.getString("email"),
                                userJson.getInt("adminCheck"),
                                userJson.getInt("Management")
                        );

                        SharedPreferenceManager.getInstance(getApplicationContext()).userLogin(user);
                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();

                        Log.d("LogTesting", "Login Successful");
                        finish();
                        openCrashPage();

                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                Log.d("LogTesting", "Entered doInBackGround");
                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("userName", userName);
                params.put("password", passWord);

                return requestHandler.sendPostsRequests(URLStorage.URL_Login, params);
            }
        }
        LoginUser ul = new LoginUser();
        ul.execute();
    }

    public void openCrashPage() {
        Intent crashIntent = new Intent(this, CrashPage.class);
        startActivity(crashIntent);
    }

    public void openRegPage() {
        Intent regIntent = new Intent(this, RegisterPage.class);
        startActivity(regIntent);
    }

    public void openRecDetailsPage() {
        Intent recIntent = new Intent( this, RecoverDetailsPage.class);
        startActivity(recIntent);
    }
}