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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class transaksi extends Fragment {
    String[] daftar;
    int[] id;
    ListView ListView1;
    Menu menu;
    protected Cursor cursor;
    dataHelper dbcenter;
    public static transaksi m;
    Button map;

    public transaksi(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaksi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        map = view.findViewById(R.id.btnMaps);
//
//        map.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(SewaFragment.this.getActivity(), MapsActivity.class);
//                startActivity(i);
//            }
//        });

        m = this;
        dbcenter = new dataHelper(getActivity());

        RefreshList(view);
        setupToolbar(view);

    }

    private void setupToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.infoTransaksi);
        toolbar.setTitle("Daftar Transaksi");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void RefreshList(View view) {
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM transaksi", null);
        daftar = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            daftar[i] = cursor.getString(0);
        }

        ListView1 = view.findViewById(R.id.listView1);
        ListView1.setAdapter(new ArrayAdapter(this.getActivity(), R.layout.custom, daftar));
        ListView1.setSelected(true);
        ListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                final String selection = daftar[arg2];
                final CharSequence[] dialogitem = {"Lihat Data", "Update Data", "Hapus Data"};
                AlertDialog.Builder builder = new AlertDialog.Builder(transaksi.this.getActivity());
                builder.setTitle("Pilih aksi");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0: {
                                Intent i = new Intent(transaksi.this.getActivity(), dataTransaksi.class);
                                i.putExtra("nama", selection);
                                startActivity(i);
                                break;
                            }
                            case 1: {
                                Intent a = new Intent(transaksi.this.getActivity(), formUpdate.class);
                                a.putExtra("nama", selection);
                                startActivity(a);
                                break;
                            }
                            case 2: {
                                SQLiteDatabase db = dbcenter.getWritableDatabase();
                                db.execSQL("DELETE FROM transaksi where nama = '" + selection + "'");
                                db.execSQL("DELETE FROM pelanggan where nama = '" + selection + "'");
                                RefreshList(view);
                                Toast.makeText(transaksi.this.getActivity(), "Data berhasil dihapus :)", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }
                });
                builder.create().show();
            }
        });
        ((ArrayAdapter) ListView1.getAdapter()).notifyDataSetInvalidated();

    }
}