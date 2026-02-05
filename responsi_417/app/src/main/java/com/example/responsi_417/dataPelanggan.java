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
import android.widget.ImageView;
import android.widget.TextView;

public class dataPelanggan extends AppCompatActivity {

    protected Cursor cursor;
    String sNama, sAlamat, sMode, sStatus, sBerat, sTotal;
    dataHelper dbHelper;
    Button map;
    TextView tvNama, tvAlamat, tvBerat, tvMode, tvStatus, tvTotal;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tes);

        map = findViewById(R.id.btnMaps);
        tvNama = findViewById(R.id.JNama);
        tvAlamat = findViewById(R.id.JAlamat);
        tvBerat = findViewById(R.id.JBerat);
        tvMode = findViewById(R.id.JMode);
        tvStatus = findViewById(R.id.JStatus);
        tvTotal = findViewById(R.id.JTotalBayar);

        dbHelper = new dataHelper(this);
        Bundle terima = getIntent().getExtras();
        if (terima == null) return;

        String nama = terima.getString("nama");

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        cursor = db.rawQuery("SELECT p.nama, p.alamat, p.berat_pakaian, p.mode, p.status, t.total_bayar " +
                "FROM pelanggan p LEFT JOIN transaksi t ON p.nama = t.nama " +
                "WHERE p.nama = ?", new String[]{nama});

        if (cursor.moveToFirst()) {
            tvNama.setText(cursor.getString(0));
            tvAlamat.setText(cursor.getString(1));
            tvBerat.setText(cursor.getString(2) + " kg");
            tvMode.setText(cursor.getString(3));
            String status = cursor.getString(4);
            String total = cursor.getString(5);

            tvStatus.setText(status != null ? status : "Proses");
            tvTotal.setText("Rp " + (total != null ? total : "0"));
        }
        cursor.close();

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dataPelanggan.this, MapsActivity.class);
                i.putExtra("nama", sNama);
                startActivity(i);
            }
        });

        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.detailPelanggan);
        toolbar.setTitle("Detail Pelanggan");
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