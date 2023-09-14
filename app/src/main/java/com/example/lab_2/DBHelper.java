package com.example.lab_2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper  extends SQLiteOpenHelper{

    private final Context context;
    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "users";
    private static final String COLUMN_ID = "id_users";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASS = "pass";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PASS + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_EMAIL, email);
        cv.put(COLUMN_PASS, password);
        return db.insert(TABLE_NAME, null, cv);
    }

    public Boolean getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + "=?";
        return db.rawQuery(query, new String[] { email }).moveToFirst();
    }

    public Boolean getPassword(String email, String pass) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = " SELECT " + COLUMN_PASS + " FROM " + TABLE_NAME + " WHERE " +
                COLUMN_EMAIL + "=?";
        Cursor cursor = db.rawQuery(query, new String[] { email });
        String dbPassword = "";
        if (cursor.moveToFirst()) {
            int passIndex = cursor.getColumnIndex(COLUMN_PASS);
            dbPassword = cursor.getString(passIndex);
        }
        return pass.equals(dbPassword);
    }

    public String getName(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = " SELECT " + COLUMN_NAME + " FROM " + TABLE_NAME + " WHERE " +
                COLUMN_EMAIL + "=?";
        Cursor cursor = db.rawQuery(query, new String[] { email });
        cursor.moveToFirst();
        int passIndex = cursor.getColumnIndex(COLUMN_NAME);
        String dbName = cursor.getString(passIndex);
        return dbName;
    }

}