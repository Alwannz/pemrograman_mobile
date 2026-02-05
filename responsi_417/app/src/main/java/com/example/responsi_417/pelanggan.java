package com.example.responsi_417;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class pelanggan extends Fragment {

    ListView ListView1;
    protected Cursor cursor;
    dataHelper dbcenter;

    public pelanggan() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pelanggan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbcenter = new dataHelper(getActivity());
        RefreshList(view);
        setupToolbar(view);
    }

    private void setupToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.infoPelanggan);
        toolbar.setTitle("Daftar Pelanggan");
    }

    public void RefreshList(View view) {
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        // Hanya ambil Nama dan Status agar tidak membingungkan SimpleAdapter
        cursor = db.rawQuery("SELECT nama, status FROM pelanggan", null);

        List<Map<String, String>> fillMaps = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Map<String, String> map = new HashMap<>();
                map.put("nama", cursor.getString(0));
                map.put("status", cursor.getString(1));
                fillMaps.add(map);
            } while (cursor.moveToNext());
        }

        // Bridge antara Data dan Layout custom_item
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), fillMaps, R.layout.custom_item,
                new String[]{"nama", "status"},
                new int[]{R.id.textNama, R.id.textStatus}) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView tvStatus = v.findViewById(R.id.textStatus);
                String status = fillMaps.get(position).get("status");

                // Logika pewarnaan kotak status
                if ("Selesai".equalsIgnoreCase(status)) {
                    tvStatus.setBackgroundResource(R.drawable.bg_status_selesai);
                    tvStatus.setText("Selesai");
                } else {
                    tvStatus.setBackgroundResource(R.drawable.bg_status_proses);
                    tvStatus.setText("Proses");
                }
                return v;
            }
        };

        ListView1 = view.findViewById(R.id.listView1);
        ListView1.setAdapter(adapter);
        ListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // Ambil nama dari map berdasarkan posisi klik
                Map<String, String> obj = (Map<String, String>) parent.getItemAtPosition(position);
                final String selection = obj.get("nama");

                final CharSequence[] dialogitem = {"Lihat Data", "Update Data", "Hapus Data"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Pilih aksi untuk " + selection);
                builder.setItems(dialogitem, (dialog, item) -> {
                    switch (item) {
                        case 0:
                            startActivity(new Intent(getActivity(), dataPelanggan.class).putExtra("nama", selection));
                            break;
                        case 1:
                            startActivity(new Intent(getActivity(), formUpdate.class).putExtra("nama", selection));
                            break;
                        case 2:
                            db.delete("pelanggan", "nama = ?", new String[]{selection});
                            db.delete("transaksi", "nama = ?", new String[]{selection});
                            RefreshList(view);
                            Toast.makeText(getActivity(), "Berhasil dihapus", Toast.LENGTH_SHORT).show();
                            break;
                    }
                });
                builder.create().show();
            }
        });
    }
}