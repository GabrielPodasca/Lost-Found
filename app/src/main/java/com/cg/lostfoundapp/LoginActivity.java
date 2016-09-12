package com.cg.lostfoundapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.cg.lostfoundapp.manager.PreferencesManager;
import com.cg.lostfoundapp.model.LoginWSResponse;
import com.cg.lostfoundapp.model.User;
import com.cg.lostfoundapp.service.LoginWSController;
import com.cg.lostfoundapp.utils.ViewUtils;

public class LoginActivity extends AppCompatActivity {

    private String username;
    private String password;

    private PreferencesManager preferencesManager;

    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnLogin;
    private Button btnRegister;
    private CheckBox cbRememberMe;

    View progressOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponents();

        initEvents();

        initRemember();

    }

    private void initComponents() {
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        cbRememberMe = (CheckBox) findViewById(R.id.cbRememberMe);

        progressOverlay = findViewById(R.id.progress_overlay);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            User user = (User) extras.getSerializable("user");
            if (user!=null) {
                txtUsername.setText(user.getUsername());
                txtPassword.setText(user.getPassword());
            }

        }
    }

    private void initEvents() {
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){

                username = txtUsername.getText().toString();
                password = txtPassword.getText().toString();
                executeLoginTask(username, password);
            }
        });


        btnRegister.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initRemember() {
        preferencesManager = new PreferencesManager(this);
        boolean rememberMe = preferencesManager.getBoolean("remember");
        cbRememberMe.setChecked(rememberMe);

        username = preferencesManager.getString("username");
        password = preferencesManager.getStringSecure("password");


        if (username!=null
                && password!=null) {
            txtUsername.setText(username);
            txtPassword.setText(password);
            executeLoginTask(username,password);
        }
    }

    private void saveRemember(String username, String password) {
        preferencesManager
                .setBoolean("remember", true)
                .setString("username", username)
                .setStringSecure("password", password)
                .commit();
    }

    private void removeRemember() {
        preferencesManager
                .remove("remember")
                .remove("username")
                .remove("password")
                .commit();
    }

    private void executeLoginTask(String username, String password) {
        new LoginAsyncTask().execute(username, password);
    }

    private void startMainActivity(User user){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
        finish();
    }


    private class LoginAsyncTask extends AsyncTask<String, Void, LoginWSResponse> {

        @Override
        protected void onPreExecute() {
            btnLogin.setEnabled(false);
            ViewUtils.animateView(progressOverlay, View.VISIBLE, 0.4f, 200);
        }

        @Override
        protected LoginWSResponse doInBackground(String... params) {

            String username = params[0];
            String password = params[1];

            User user = new User(username, password);

            LoginWSResponse response = LoginWSController.getInstance().login(user);

            return response;
        }

        @Override
        protected void onPostExecute(LoginWSResponse response) {

            ViewUtils.animateView(progressOverlay, View.GONE, 0, 200);
            btnLogin.setEnabled(true);


            if (response!=null) {
                Toast.makeText(LoginActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                User user = response.getUser();
                if (user != null) {
                    if (cbRememberMe.isChecked()) {
                        saveRemember(user.getUsername(), user.getPassword());
                    } else {
                        removeRemember();
                    }
                    startMainActivity(user);
                }
            } else {
                Toast.makeText(LoginActivity.this, "Server communication error!", Toast.LENGTH_SHORT).show();
            }

        }


    }

}