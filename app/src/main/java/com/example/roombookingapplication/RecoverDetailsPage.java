package com.example.roombookingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RecoverDetailsPage extends AppCompatActivity {


    private Button b1,b2;
    private EditText recEmailTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_details_page);

        b1 = findViewById(R.id.returnLogin);
        b2 = findViewById(R.id.submitRecEmail);
        recEmailTxt = findViewById(R.id.RecoverEmail);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { }});

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { }});

    }

    public void openLoginPage() {
        Intent loginIntent = new Intent( this, LoginScreen.class);
        startActivity(loginIntent);
    }

    private void recoverDetails() {
        String recEmail = recEmailTxt.getText().toString().trim();
        if (recEmailTxt == null) {
            Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show();
            return;
        }

        class recoverDetails extends AsyncTask<Void,Void, String> {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSONObject object = null;
                try {
                    object = new JSONObject(s);

                    if (!object.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            protected String doInBackground(Void... voids) {

                RequestHandler reqHandler = new RequestHandler();
                HashMap<String,String> param = new HashMap<>();
                param.put("Email",recEmail );
                return reqHandler.sendPostsRequests(URLStorage.URL_RecoverDetails,param);
            }
        }
        recoverDetails rd = new recoverDetails();
        rd.execute();
    }
}