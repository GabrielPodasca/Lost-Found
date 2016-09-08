package com.cg.lostfoundapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LostActivity extends AppCompatActivity {
    private String username = "";
    private String password = "";
    private String phoneNumber = "";

    private EditText iLostEditText;
    private EditText whereLostText;
    private Button viewOnMapButton;
    private EditText whenLostText;
    private EditText descriptionLostText;
    private Button postLostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);

        initComponents();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
            password = extras.getString("password");
            phoneNumber = extras.getString("phoneNumber");
        }
    }

    private void initComponents(){
        iLostEditText = (EditText) findViewById(R.id.iLostEditText);
        whereLostText = (EditText) findViewById(R.id.whereLostText);
        viewOnMapButton = (Button) findViewById(R.id.viewOnMapButton);
        whenLostText = (EditText) findViewById(R.id.whenLostText);
        descriptionLostText = (EditText) findViewById(R.id.descriptionLostText);
        postLostButton = (Button) findViewById(R.id.postLostButton);
    }
}
