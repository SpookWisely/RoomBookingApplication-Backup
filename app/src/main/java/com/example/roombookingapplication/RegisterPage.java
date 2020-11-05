package com.example.roombookingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;

public class RegisterPage extends AppCompatActivity {
    EditText regText1,regText2,regText3,regText4,regText5;
    Button regButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        //Declaring variables to assigned GUI Elements
        regText1=(EditText)findViewById(R.id.RegFullName);
        regText2=(EditText)findViewById(R.id.RegUserName);
        regText3=(EditText)findViewById(R.id.RegEmail);
        regText4=(EditText)findViewById(R.id.RegPassword);
        regText5=(EditText)findViewById(R.id.RegPasswordCon);

        regButton=(Button)findViewById(R.id.submitButton);
        Context context =getApplicationContext();
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                //Gets data on button click from the EditText boxes and
                // convert them to Strings and store them for manipulation.
                String fullName = regText1.getText().toString();
                String userName = regText2.getText().toString();
                String email = regText3.getText().toString();
                String password = regText4.getText().toString();
                String passwordConf = regText5.getText().toString();


                if(isntEmpty(fullName) ||isntEmpty(userName) ||isntEmpty(email)||isntEmpty(password)||isntEmpty(passwordConf)) {

                    if(passwordConf == password) {

                    } else {
                        Toast.makeText(context,"Passwords Don't Match",Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context,"Please Fill in Missing Fields",Toast.LENGTH_SHORT).show();
                }

            }


        });


    }

    public boolean isntEmpty(String text) {
        return text.trim().length() > 0;
    }


    public void openLoginPage() {
        Intent intent = new Intent(this, LoginScreen.class);
        startActivity(intent);
    }
}