package com.example.responsi_417;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class login extends AppCompatActivity {


    Button loginAdm;
    EditText username, password;
//    TextInputLayout inputLayoutUsername, inputLayoutPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        inputLayoutUsername =(TextInputLayout) findViewById(R.id.user);
//        inputLayoutPassword = (TextInputLayout) findViewById(R.id.pw);
//        username = (EditText) findViewById(R.id.user);
//        password = (EditText) findViewById(R.id.password);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginAdm = findViewById(R.id.login);

        loginAdm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usr = username.getText().toString();
                String pwd = password.getText().toString();

                if (usr.equals("admin") && pwd.equals("admin")) {
                    Toast.makeText(getApplicationContext(), "Anda berhasil login sebagai ADMIN", Toast.LENGTH_SHORT).show();
                    Intent log = new Intent(login.this, admin.class);
                    startActivity(log);
                } else if(usr.equals("budi") && pwd.equals("budi")){
                    Toast.makeText(getApplicationContext(), "Anda berhasil login", Toast.LENGTH_SHORT).show();
                    Intent log = new Intent(login.this, homeUser.class);
                    log.putExtra("USERNAME_LOGIN", usr);
                    startActivity(log);
                } else {
                    Toast.makeText(getApplicationContext(), "Username / Password Salah", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}