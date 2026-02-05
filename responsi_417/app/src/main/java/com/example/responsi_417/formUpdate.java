package com.example.responsi_417;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.content.ContentValues;

public class formUpdate extends AppCompatActivity {

    protected Cursor cursor;
    EditText nama, alamat, berat;
    RadioGroup mode, statusGroup; // Tambahkan statusGroup
    RadioButton cuci, setrika, rbProses, rbSelesai; // Tambahkan rbProses, rbSelesai
    Button selesai;
    int berat2, harga, total;

    String uNama, uAlamat, uMode, uBerat, uStatus; // Tambahkan uStatus
    String sNama, sAlamat, pil, sBerat, sStatus;

    dataHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_update);

        dbHelper = new dataHelper(this);

        Bundle terima = getIntent().getExtras();
        if (terima == null) return;
        String name = terima.getString("nama");

        // Inisialisasi View
        nama = findViewById(R.id.etNama);
        alamat = findViewById(R.id.etAlamat);
        berat = findViewById(R.id.etBerat);
        mode = findViewById(R.id.modeGroup);
        cuci = findViewById(R.id.rbCuci);
        setrika = findViewById(R.id.rbSetrika);

        // HUBUNGKAN VIEW STATUS
        statusGroup = findViewById(R.id.statusGroup);
        rbProses = findViewById(R.id.rbProses);
        rbSelesai = findViewById(R.id.rbSelesai);

        selesai = findViewById(R.id.selesai);

        // Ambil data lama dari database
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("select * from pelanggan where nama = ?", new String[]{name});

        if (cursor.moveToFirst()) {
            uNama = cursor.getString(0);
            uAlamat = cursor.getString(1);
            uBerat = cursor.getString(2);
            uMode = cursor.getString(3);
            uStatus = cursor.getString(4); // Ambil status lama
        }
        cursor.close();

        // Tampilkan data lama ke Form
        nama.setText(uNama);
        nama.setEnabled(false); // Nama dikunci karena Primary Key
        alamat.setText(uAlamat);
        berat.setText(uBerat);

        // Set RadioButton Mode
        if ("Cuci".equals(uMode)) {
            cuci.setChecked(true);
        } else {
            setrika.setChecked(true);
        }

        // Set RadioButton Status
        if ("Selesai".equals(uStatus)) {
            rbSelesai.setChecked(true);
        } else {
            rbProses.setChecked(true);
        }

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sNama = nama.getText().toString();
                sAlamat = alamat.getText().toString();
                sBerat = berat.getText().toString();

                if (sNama.isEmpty() || sAlamat.isEmpty() || sBerat.isEmpty()) {
                    Toast.makeText(formUpdate.this, "Data tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Logika Mode
                if (cuci.isChecked()) {
                    pil = "Cuci";
                    harga = 6000;
                } else {
                    pil = "Cuci + Setrika";
                    harga = 10000;
                }

                // Logika Status
                if (rbProses.isChecked()) {
                    sStatus = "Proses";
                } else {
                    sStatus = "Selesai";
                }

                berat2 = Integer.parseInt(sBerat);
                total = (harga * berat2);

                SQLiteDatabase dbH = dbHelper.getWritableDatabase();

                // UPDATE TABEL PELANGGAN
                ContentValues vPelanggan = new ContentValues();
                vPelanggan.put("alamat", sAlamat);
                vPelanggan.put("berat_pakaian", sBerat);
                vPelanggan.put("mode", pil);
                vPelanggan.put("status", sStatus); // UPDATE STATUS DISINI
                dbH.update("pelanggan", vPelanggan, "nama = ?", new String[]{name});

                // UPDATE TABEL TRANSAKSI
                ContentValues vTransaksi = new ContentValues();
                vTransaksi.put("alamat", sAlamat);
                vTransaksi.put("berat_pakaian", sBerat);
                vTransaksi.put("mode", pil);
                vTransaksi.put("total_bayar", total);
                dbH.update("transaksi", vTransaksi, "nama = ?", new String[]{name});

                Toast.makeText(formUpdate.this, "Data Laundry Berhasil Diperbarui!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        setupToolbar();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.infoSewa);
        toolbar.setTitle("Update Pesanan Laundry");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}