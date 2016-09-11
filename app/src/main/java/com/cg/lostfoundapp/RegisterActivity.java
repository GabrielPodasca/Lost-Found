package com.cg.lostfoundapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cg.lostfoundapp.model.LoginWSResponse;
import com.cg.lostfoundapp.model.User;
import com.cg.lostfoundapp.service.LoginWSController;

public class RegisterActivity extends AppCompatActivity{
    private String username;
    private String password;
    private String phoneNumber;

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText retypePasswordEditText;
    private EditText phoneNumberEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initComponents();

        initEvents();
    }

    private void initComponents() {
        usernameEditText = (EditText) findViewById(R.id.txtUsername);
        passwordEditText = (EditText) findViewById(R.id.txtPassword);
        retypePasswordEditText = (EditText) findViewById(R.id.retypePasswordEditText);
        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
        registerButton = (Button) findViewById(R.id.btnRegister);
    }

    private void initEvents() {

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = passwordEditText.getText().toString();
                if(password.equals(retypePasswordEditText.getText().toString())){
                    username = usernameEditText.getText().toString();
                    phoneNumber = phoneNumberEditText.getText().toString();

                    new RegisterAsyncTask().execute(username, password, phoneNumber);

                }else{
                    Toast.makeText(RegisterActivity.this,"Re-type password again!",Toast.LENGTH_SHORT).show();
                    retypePasswordEditText.setText("");
                }
            }
        });
    }



    private class RegisterAsyncTask extends AsyncTask<String, Void, LoginWSResponse> {

        @Override
        protected void onPreExecute() {
            registerButton.setEnabled(false);
        }

        @Override
        protected void onPostExecute(LoginWSResponse response) {

            registerButton.setEnabled(true);

            if (response!=null) {
                Toast.makeText(RegisterActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                if (response.getUser() != null) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.putExtra("user", response.getUser());
                    startActivity(intent);
                    finish();
                }

            } else {
                Toast.makeText(RegisterActivity.this, "Server communication error!", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected LoginWSResponse doInBackground(String... params) {

            String username = params[0];
            String password = params[1];
            String phoneNumber = params[2];

            User user = new User(username, password, phoneNumber);

            LoginWSResponse response = LoginWSController.getInstance().register(user);

            return response;
        }
    }
}
