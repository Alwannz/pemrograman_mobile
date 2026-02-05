package com.example.responsi_417;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.responsi_417.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;
import java.util.List;

public class dataLokasi extends AppCompatActivity {

    protected Cursor cursor;
    dataHelper dbHelper;
    String uAlamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void buttonGetCoordinates(View view){

        dbHelper = new dataHelper(this);
        Bundle terima = getIntent().getExtras();
        Intent intent = getIntent();
        String name = terima.getString("nama");

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("select * from pelanggan where nama = '" + name + "'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            uAlamat = cursor.getString(1);
        }

        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocationName(uAlamat, 1);

            if (addressList != null){
                double doubleLat = addressList.get(0).getLatitude();
                double doubleLong = addressList.get(0).getLongitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}