package com.example.roombookingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginScreen extends AppCompatActivity {
    EditText t1,t2;
    Button b1,b2,b3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        b1 =(Button)findViewById(R.id.regButton);
        b2 =(Button)findViewById(R.id.forgotDetailsButton);
        b3 =(Button)findViewById(R.id.loginButton);
        t1 =(EditText)findViewById(R.id.UserNameText);
        t2 =(EditText)findViewById(R.id.PasswordText);

        b1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openRegPage();
            }

        });

    }

    public void openRegPage() {
        Intent intent = new Intent(this, RegisterPage.class);
        startActivity(intent);
    }

}