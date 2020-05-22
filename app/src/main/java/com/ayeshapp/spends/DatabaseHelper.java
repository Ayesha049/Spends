package com.ayeshapp.spends;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Spends.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "Spend";
    public static final String COL_ITEM_NAME = "itemname";
    public static final String COL_ITEM_QUANTUTY = "quantity";
    public static final String COL_DATE = "date";
    public static final String COL_ITEM_PRICE = "price";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
