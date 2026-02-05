package com.example.responsi_417;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class dataUser extends AppCompatActivity {

    ListView ListView1;
    protected Cursor cursor;
    dataHelper dbcenter;
    String userAktif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_user);

        userAktif = getIntent().getStringExtra("USERNAME_LOGIN");
        dbcenter = new dataHelper(this);
        ListView1 = findViewById(R.id.listView1);

        RefreshList();
        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.infoPelanggan);
        toolbar.setTitle("Riwayat Pesanan: " + userAktif);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void RefreshList() {
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        // Pastikan nama kolom 'status' sudah ada di database versi terbaru Anda
        cursor = db.rawQuery("SELECT nama, status FROM pelanggan WHERE LOWER(nama) = LOWER(?)",
                new String[]{userAktif.trim()});

        List<Map<String, String>> fillMaps = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Map<String, String> map = new HashMap<>();
                map.put("nama", cursor.getString(0));
                map.put("status", cursor.getString(1));
                fillMaps.add(map);
            } while (cursor.moveToNext());
        }

        SimpleAdapter adapter = new SimpleAdapter(this, fillMaps, R.layout.custom_item,
                new String[]{"nama", "status"},
                new int[]{R.id.textNama, R.id.textStatus}) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tvStatus = view.findViewById(R.id.textStatus);

                String status = fillMaps.get(position).get("status");

                if (tvStatus != null && status != null) {
                    if ("Selesai".equalsIgnoreCase(status)) {
                        tvStatus.setBackgroundResource(R.drawable.bg_status_selesai);
                    } else {
                        tvStatus.setBackgroundResource(R.drawable.bg_status_proses);
                    }
                }
                return view;
            }
        };

        ListView1.setAdapter(adapter);
        ListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                @SuppressWarnings("unchecked")
                Map<String, String> obj = (Map<String, String>) arg0.getItemAtPosition(arg2);
                String selection = obj.get("nama");
                Intent i = new Intent(dataUser.this, dataPelanggan.class);
                i.putExtra("nama", selection);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}