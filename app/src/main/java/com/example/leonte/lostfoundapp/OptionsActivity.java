package com.example.leonte.lostfoundapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OptionsActivity extends AppCompatActivity {
    private Button lostButton;
    private Button foundButton;
    private Button checkButton;
    private String username;
    private String password;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        initComponents();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
            password = extras.getString("password");
            phoneNumber = extras.getString("phoneNumber");
        }

        lostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionsActivity.this,LostActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("password",password);
                intent.putExtra("phoneNumber",phoneNumber);
                startActivity(intent);
            }
        });

        foundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OptionsActivity.this,FoundActivity.class);
                startActivity(intent);
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(OptionsActivity.this,MainActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("password",password);
                intent.putExtra("phoneNumber",phoneNumber);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed(){
        finish();
        Intent intent = new Intent(OptionsActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    private void initComponents(){
        lostButton = (Button) findViewById(R.id.lostButton);
        foundButton = (Button) findViewById(R.id.foundButton);
        checkButton = (Button) findViewById(R.id.checkButton);
    }
}
