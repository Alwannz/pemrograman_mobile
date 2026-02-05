package com.example.responsi_417;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class dataHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;

    private static final String DATABASE_NAME = "laundry.db";
    private static final int DATABASE_VERSION = 10;

    public dataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON");
        db.execSQL("create table pelanggan (" +
                "nama text," +
                "alamat text," +
                "berat_pakaian int," +
                "mode text," +
                "status text default 'Proses'," +
                "primary key(nama)" +
                ");");
        db.execSQL("create table transaksi (" +
                "nama text," +
                "alamat text," +
                "berat_pakaian int," +
                "mode text," +
                "total_bayar int," +
                "primary key(nama)" +
                ");" +
                "");
    }

    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<String>();
        String selectQuery = "select * from pelanggan";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return categories;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS pelanggan");
        db.execSQL("DROP TABLE IF EXISTS transaksi");
        onCreate(db);
    }
}
