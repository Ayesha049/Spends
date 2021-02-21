package com.ayeshapp.spends.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Spends.db";
    public static final int DATABASE_VERSION = 4;

    public static final String EXPENSE_TABLE = "Expense";
    public static final String COL_ITEM_CATEGORY = "category";
    public static final String COL_ITEM_NAME = "itemname";
    public static final String COL_DATE = "date";
    public static final String COL_ITEM_PRICE = "price";
    public static final String COL_EXPENSE_RECEIPT = "expense_receipt";

    public static final String INCOME_TABLE= "Income";
    public static final String COL_INCOME_CATEGORY = "income_category";
    public static final String COL_INCOME_DATE = "income_date";
    public static final String COL_INCOME_AMOUNT = "income_amount";
    public static final String COL_INCOME_RECEIPT = "income_receipt";


    /*
    SELECT *
    FROM Spend
    WHERE date(date) BETWEEN date('2020-06-02') AND date('2020-06-08')

    SELECT * FROM Spend WHERE strftime('%W', date) = strftime('%W', '2020-06-12') and strftime('%Y', date) = '2020';

    SELECT * FROM Spend WHERE strftime('%m', date) = '06'
    and strftime('%Y', date) = '2020';
    */


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e("inDB", "Database initiated");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + EXPENSE_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_DATE + " TEXT," +
                COL_ITEM_NAME + " TEXT," +
                COL_ITEM_CATEGORY + " TEXT," +
                COL_ITEM_PRICE + " REAL," +
                COL_EXPENSE_RECEIPT + " TEXT)");

        db.execSQL("create table " + INCOME_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_INCOME_DATE + " TEXT," +
                COL_INCOME_CATEGORY + " TEXT," +
                COL_INCOME_AMOUNT + " REAL," +
                COL_INCOME_RECEIPT + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EXPENSE_TABLE);
        onCreate(db);
    }

    public long insertExpenseData(String date, String itemName, String category, String price, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_ITEM_NAME, itemName);
        contentValues.put(COL_ITEM_CATEGORY, category);
        contentValues.put(COL_ITEM_PRICE, price);
        contentValues.put(COL_EXPENSE_RECEIPT, imagePath);
        long result = db.insert(EXPENSE_TABLE, null, contentValues);
        return result;
    }

    public long insertIncomeData(String date, String categoryIncome, String amount, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_INCOME_DATE, date);
        contentValues.put(COL_INCOME_CATEGORY, categoryIncome);
        contentValues.put(COL_INCOME_AMOUNT, amount);
        contentValues.put(COL_INCOME_RECEIPT, imagePath);
        long result = db.insert(INCOME_TABLE, null, contentValues);
        return result;
    }

    public void updateExpenseData(String id, String date, String itemName, String category, String price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", id);
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_ITEM_NAME, itemName);
        contentValues.put(COL_ITEM_CATEGORY, category);
        contentValues.put(COL_ITEM_PRICE, price);
        db.update(EXPENSE_TABLE, contentValues, "ID = ?", new String[]{id});
    }

    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(EXPENSE_TABLE, "ID = ?", new String[]{id});
    }

    public Cursor getAllData(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + EXPENSE_TABLE + " WHERE " + COL_DATE + " LIKE " + "'%" + date + "%' ORDER BY ID ASC";
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }

    public Cursor getDistincDates() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT DISTINCT " + COL_DATE + " FROM " + EXPENSE_TABLE;
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }

    public Cursor getDataPeriodically(String s, String e) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT DISTINCT " + COL_DATE + " FROM " + EXPENSE_TABLE + " WHERE date(" + COL_DATE + ") BETWEEN date('" +
                s + "') AND date('" + e + "') ORDER BY date(" + COL_DATE + ") DESC";
        Log.i("query", selectQuery);
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }

    public Cursor getDataYearly(String s) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT DISTINCT " + COL_DATE + " FROM " + EXPENSE_TABLE + " WHERE strftime('%Y', " +
                COL_DATE + ") = '" + s + "' ORDER BY date(" + COL_DATE + ") DESC";
        Log.i("query", selectQuery);
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }

    public Cursor getDataMonthly(String y, String m) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT DISTINCT " + COL_DATE + " FROM " + EXPENSE_TABLE + " WHERE strftime('%m', date) = '" + m
                + "' and strftime('%Y', date) = '" + y + "' ORDER BY date(" + COL_DATE + ") DESC";
        Log.i("query", selectQuery);
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }
}
