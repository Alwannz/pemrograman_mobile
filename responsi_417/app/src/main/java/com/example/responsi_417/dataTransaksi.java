package com.example.responsi_417;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class dataTransaksi extends AppCompatActivity {

    protected Cursor cursor;
    String sNama, sAlamat, sMode, sBerat, sTotal, mod;
    dataHelper dbHelper;
    Button map;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tes2);

        Bundle terima = getIntent().getExtras();
        map = findViewById(R.id.btnMaps);

        dbHelper = new dataHelper(this);
        Intent intent = getIntent();

        String nama = terima.getString("nama");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("select * from transaksi where nama = '" + nama + "'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            sNama = cursor.getString(0);
            sAlamat = cursor.getString(1);
            sBerat = cursor.getString(2);
            sMode = cursor.getString(3);
            sTotal = cursor.getString(4);
        }

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dataTransaksi.this, MapsActivity.class);
                i.putExtra("nama",sNama);
                startActivity(i);
            }
        });

        if(sMode.equals("Cuci")){
            mod = "6000";
        } else if(sMode.equals("Cuci + Setrika")){
            mod = "10.000";
        }

        TextView tvNama = findViewById(R.id.JNama);
        TextView tvAlamat = findViewById(R.id.JAlamat);
        TextView tvBerat = findViewById(R.id.JBerat);
        TextView tvMode = findViewById(R.id.JMode);
        TextView tvTotal = findViewById(R.id.JTotal);
        TextView tvHitungTotal = findViewById(R.id.JHitungTotal);

        tvNama.setText(sNama);
        tvAlamat.setText(sAlamat);
        tvBerat.setText(sBerat + " kg");
        tvMode.setText(sMode);
        tvHitungTotal.setText("Total Bayar  :  " + sBerat + " x " + mod);
        tvTotal.setText("Rp." + sTotal);

        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.detailPelanggan);
        toolbar.setTitle("Detail Transaksi");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}