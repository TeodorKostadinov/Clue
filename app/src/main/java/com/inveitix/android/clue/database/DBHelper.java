package com.inveitix.android.clue.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tito on 3.1.2016 г..
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "DB_TABLE_MUSEUMS";
    public static final String COLUMN_VAL = "val";
    protected SQLiteDatabase database;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        open();
    }

    public void insert(String value) {
        ContentValues cv = new ContentValues();
        cv.put("val", value);
        database.insert(TABLE_NAME, null, cv);
    }

    private void open() {
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (_id integer primary key autoincrement, "
                + COLUMN_VAL + " text not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void close() {
        database.close();
    }

    public Cursor getValues() {
        return this.database.query(TABLE_NAME, new String[]{COLUMN_VAL}, null, null, null, null, null);
    }
}
