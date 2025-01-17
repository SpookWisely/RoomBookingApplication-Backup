//Some code based off of
//https://www.simplifiedcoding.net/android-login-and-registration-tutorial/#Creating-a-new-PHP-Project
package com.example.roombookingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterPage extends AppCompatActivity {
    EditText regText1,regText2,regText3,regText4,regText5;
    Button regButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //Declaring variables to assigned GUI Elements
        regText1=(EditText)findViewById(R.id.RegFullName);
        regText2=(EditText)findViewById(R.id.RegUserName);
        regText3=(EditText)findViewById(R.id.RegEmail);
        regText4=(EditText)findViewById(R.id.RegPassword);
        regText5=(EditText)findViewById(R.id.RegPasswordCon);


        regButton=(Button)findViewById(R.id.submitButton);
        Context context = getApplicationContext();
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                registerUser();
                Toast.makeText(context, "Registration Completed", Toast.LENGTH_SHORT).show();
                openLoginScreen();
            }
        });


    }

    public boolean emailMatcher(String email){

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }


        public boolean isntEmpty(String text) {
        return text.trim().length() > 0;
    }


   /* public void openLoginPage() {
        Intent intent = new Intent(this, LoginScreen.class);
        startActivity(intent);
    }*/

    private void registerUser() {
        Log.d("Testing","Entered Input Gathering and validation");
        //Gets data on button click from the EditText boxes and
        // convert them to Strings and store them for manipulation.
        String fullName = regText1.getText().toString().trim();
        String userName = regText2.getText().toString().trim();
        String email = regText3.getText().toString().trim();
        String password = regText4.getText().toString().trim();
        String passwordConf = regText5.getText().toString().trim();
        Log.d("Testing","Gathered Inputs");
        Context context = getApplicationContext();
        //Check to see if any fields are empty then prompts the user if statement fails
        if (isntEmpty(fullName) || isntEmpty(userName) || isntEmpty(email) || isntEmpty(password) || isntEmpty(passwordConf)) {
            Log.d("Testing","Text boxes aren't empty");
        } else {
            Toast.makeText(context, "Please Fill in Missing Fields", Toast.LENGTH_SHORT).show();
            return;
        }

        //checks if email matches the pattern which is [a-z,A-Z,0-9,symbols(:./!)] @ [a-z,A-Z,0-9,symbols(:./!)]

        if (emailMatcher(email)) {
            Log.d("Testing","Email Matches Template");
        } else {
            Toast.makeText(context, "Enter valid Email", Toast.LENGTH_SHORT).show();
            return;
        }

        //checks if the password and password confirmation are the same.
        if (!password.equals(passwordConf)) {
            Toast.makeText(context, "Passwords Don't Match", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Log.d("Testing","Passwords Match");

        }

        Log.d("Testing" , "finished validation" );
        class RegisterUser extends AsyncTask<Void, Void, String> {

            //private ProgressBar progressBar;

            protected String doInBackground(Void... voids) {
                //This use of this hashmap is used to create the variables that will be sent
                // to the php to be stored.
                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("fullName", fullName);
                parameters.put("userName", userName);
                parameters.put("email", email);
                parameters.put("password",password);
                Log.d("Testing" , "Gathered strings and pushed them through parameters." );
                return requestHandler.sendPostsRequests(URLStorage.URL_REGISTER, parameters);

            }

            protected void onPreExecute() {
                super.onPreExecute();

               // progressBar = (ProgressBar) findViewById(R.id.progressBar);
               // progressBar.setVisibility(View.VISIBLE);
            }

            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.d("Testing" , "Entered onPostExecute" );
                //progressBar.setVisibility(View.GONE);
                try {
                    //Declares a new jsonfile that contain the parameters stored above.
                    JSONObject object = new JSONObject(s);

                if(!object.getBoolean("error")) {
                    JSONObject userJSON = object.getJSONObject("user");


                    User newUser = new User(
                            userJSON.getInt("id"),
                            userJSON.getString("fullName"),
                            userJSON.getString("userName"),
                            userJSON.getString("email"),
                            userJSON.getInt("admin"),
                            userJSON.getInt("management")
                    );

                    SharedPreferenceManager.getInstance(getApplicationContext()).userLogin(newUser);
                    finish();
                    openLoginScreen();
                    Log.d("Testing" , "Registration complete" );
                } else {
                    Toast.makeText(context, "An Error occurred while registering", Toast.LENGTH_SHORT).show();
                }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        RegisterUser ru = new RegisterUser();
        ru.execute();
    }

    public void openLoginScreen() {
        Intent logIntent = new Intent(this, LoginScreen.class);
        startActivity(logIntent);
    }
}


