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

import com.cg.lostfoundapp.model.LoginWSResponse;
import com.cg.lostfoundapp.model.User;
import com.cg.lostfoundapp.service.LoginWSController;

public class LoginActivity extends AppCompatActivity {
    private String username;
    private String password;
    //private User user;
    private Bundle extras = null;

    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnLogin;
    private Button btnRegister;
    private CheckBox cbRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponents();

        extras = getIntent().getExtras();
        if(extras != null){
            username = extras.getString("username");
            password = extras.getString("password");
            txtUsername.setText(username);
            txtPassword.setText(password);
        }

        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                username = txtUsername.getText().toString();
                password = txtPassword.getText().toString();
                new LoginAsyncTask().execute(username, password);
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

    private void initComponents() {
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        cbRememberMe = (CheckBox) findViewById(R.id.cbRememberMe);
    }

    private void clearFields(){
        txtUsername.setText("");
        txtPassword.setText("");
    }

    private void startMainActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("username",username);
        intent.putExtra("password",password);
        startActivity(intent);
        finish();
    }


    private class LoginAsyncTask extends AsyncTask<String, Void, LoginWSResponse> {

        @Override
        protected void onPostExecute(LoginWSResponse response) {
            if (response!=null) {
                Toast.makeText(LoginActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "Server communication error!", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected LoginWSResponse doInBackground(String... params) {
            String username = params[0];
            String password = params[1];

            User user = new User(username, password);

            LoginWSResponse response = LoginWSController.getInstance().login(user);

            return response;
        }
    }

}