package com.example.leonte.lostfoundapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leonte.lostfoundapp.model.User;
import com.example.leonte.lostfoundapp.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity {
    private String username;
    private String password;
    private String phoneNumber;
    private User user;
    private Bundle extras = null;

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;

    private long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponents();

        extras = getIntent().getExtras();
        if(extras != null){
            username = extras.getString("username");
            password = extras.getString("password");
            phoneNumber = extras.getString("phoneNumber");
            usernameEditText.setText(username);
            passwordEditText.setText(password);
        }

        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                if(extras != null){
                    startOptionsActivity();
                }else{
                    username = usernameEditText.getText().toString();
                    password = passwordEditText.getText().toString();
                    phoneNumber = UserService.getInstance().getPhoneNumberByUsername(LoginActivity.this,username);
                    user = UserService.getInstance().login(LoginActivity.this,username,password);
                    if(user != null){
                        startOptionsActivity();
                    }else{
                        Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                        clearFields();
                    }
                }
            }
        });

        registerButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed(){
        Toast t = Toast.makeText(LoginActivity.this,"Press once again to exit!",Toast.LENGTH_SHORT);
        if(back_pressed+3000 > System.currentTimeMillis()){
            t.cancel();
            android.os.Process.killProcess(android.os.Process.myPid());
        }else{
            t.show();
        }
        back_pressed = System.currentTimeMillis();
    }

    private void initComponents() {
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);
    }

    private void clearFields(){
        usernameEditText.setText("");
        passwordEditText.setText("");
    }

    private void startOptionsActivity(){
        Intent intent = new Intent(LoginActivity.this, OptionsActivity.class);
        intent.putExtra("username",username);
        intent.putExtra("password",password);
        intent.putExtra("phoneNumber",phoneNumber);
        startActivity(intent);
        finish();
    }

}