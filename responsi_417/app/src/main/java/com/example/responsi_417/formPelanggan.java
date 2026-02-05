package com.example.responsi_417;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues; // Tambahkan import ini
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class formPelanggan extends AppCompatActivity {

    EditText nama, alamat, berat;
    RadioGroup mode;
    RadioButton cuci, setrika;
    Button selesai;
    int berat2, harga, total;
    String sNama, sAlamat, pil, sBerat, namaOtomatis;
    dataHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pelanggan);

        dbHelper = new dataHelper(this);

        namaOtomatis = getIntent().getStringExtra("USERNAME_LOGIN");

        nama = findViewById(R.id.etNama);
        if (namaOtomatis != null) {
            nama.setText(namaOtomatis);
            nama.setEnabled(false);
        }
        alamat = findViewById(R.id.etAlamat);
        berat = findViewById(R.id.etBerat);
        mode = findViewById(R.id.modeGroup);
        cuci = findViewById(R.id.rbCuci);
        setrika = findViewById(R.id.rbSetrika);
        selesai = findViewById(R.id.selesai);

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sNama = nama.getText().toString();
                sAlamat = alamat.getText().toString();
                sBerat = berat.getText().toString();

                if (sNama.isEmpty() || sAlamat.isEmpty() || sBerat.isEmpty()) {
                    Toast.makeText(formPelanggan.this, "Data wajib diisi!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (cuci.isChecked()) {
                    pil = "Cuci";
                    harga = 6000;
                } else {
                    pil = "Cuci + Setrika";
                    harga = 10000;
                }

                berat2 = Integer.parseInt(sBerat);
                total = (harga * berat2);

                SQLiteDatabase dbH = dbHelper.getWritableDatabase();

                ContentValues vPelanggan = new ContentValues();
                vPelanggan.put("nama", sNama);
                vPelanggan.put("alamat", sAlamat);
                vPelanggan.put("berat_pakaian", sBerat);
                vPelanggan.put("mode", pil);
                vPelanggan.put("status", "Proses"); // Inisialisasi status awal
                dbH.insert("pelanggan", null, vPelanggan);

                // 2. Simpan ke Tabel Transaksi
                ContentValues vTransaksi = new ContentValues();
                vTransaksi.put("nama", sNama);
                vTransaksi.put("alamat", sAlamat);
                vTransaksi.put("berat_pakaian", sBerat);
                vTransaksi.put("mode", pil);
                vTransaksi.put("total_bayar", total);
                dbH.insert("transaksi", null, vTransaksi);

                Toast.makeText(formPelanggan.this, "Anda berhasil memesan Laundry :)", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        setupToolbar();
    }

    // Perbaikan navigasi back
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.infoSewa);
        toolbar.setTitle("Pesan Laundry");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}