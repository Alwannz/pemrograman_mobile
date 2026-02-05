package com.example.responsi_417;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class homeUser extends AppCompatActivity {

    Button isi, lihat, logout;
    String namaLogin; // Deklarasikan sebagai variabel global class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        // Ambil data dari login
        namaLogin = getIntent().getStringExtra("USERNAME_LOGIN");

        isi = findViewById(R.id.isiData);
        lihat = findViewById(R.id.lihatData);
        logout = findViewById(R.id.btnLogout);

        isi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homeUser.this, formPelanggan.class);
                // Pastikan variabel namaLogin yang dikirim
                intent.putExtra("USERNAME_LOGIN", namaLogin);
                startActivity(intent);
            }
        });

        lihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homeUser.this, dataUser.class);
                intent.putExtra("USERNAME_LOGIN", namaLogin);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogout();
            }
        });
    }

    private void performLogout() {
        Intent intent = new Intent(homeUser.this, login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}