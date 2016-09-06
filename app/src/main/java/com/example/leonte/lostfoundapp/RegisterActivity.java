package com.example.leonte.lostfoundapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leonte.lostfoundapp.model.User;
import com.example.leonte.lostfoundapp.service.UserService;

public class RegisterActivity extends AppCompatActivity{
    private String username;
    private String password;
    private String phoneNumber;
    private User user;

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

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = passwordEditText.getText().toString();
                if(password.equals(retypePasswordEditText.getText().toString())){
                    username = usernameEditText.getText().toString();
                    phoneNumber = phoneNumberEditText.getText().toString();
                    String message = UserService.getInstance().register(RegisterActivity.this,username,password,phoneNumber);
                    if(message.equals("nok")){
                        Toast.makeText(RegisterActivity.this,"Username not available!",Toast.LENGTH_SHORT).show();
                        clearFields();
                    }
                    if(message.equals("ok")){
                        //Toast.makeText(RegisterActivity.this,"Success!",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.putExtra("username",username);
                        intent.putExtra("password",password);
                        intent.putExtra("phoneNumber",phoneNumber);
                        startActivity(intent);
                        //finish();
                    }

                }else{
                    Toast.makeText(RegisterActivity.this,"Re-type password again!",Toast.LENGTH_SHORT).show();
                    retypePasswordEditText.setText("");
                }
            }
        });
    }

    private void initComponents() {
        usernameEditText = (EditText) findViewById(R.id.txtUsername);
        passwordEditText = (EditText) findViewById(R.id.txtPassword);
        retypePasswordEditText = (EditText) findViewById(R.id.retypePasswordEditText);
        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
        registerButton = (Button) findViewById(R.id.btnRegister);
    }

    private void clearFields(){
        usernameEditText.setText("");
        retypePasswordEditText.setText("");
    }
}
