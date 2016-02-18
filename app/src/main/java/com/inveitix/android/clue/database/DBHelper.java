package com.inveitix.android.clue.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.inveitix.android.clue.cmn.Museum;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "DB_TABLE_MUSEUMS";
    public static final String COLUMN_ID = "IDS";
    public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_MAP_SIZE = "MAP_SIZE";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_LOCATION = "LOCATION";

    protected SQLiteDatabase database;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        open();
    }

    public void insertMuseum(Museum museum) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, museum.getId());
        cv.put(COLUMN_DESCRIPTION, museum.getDescription());
        cv.put(COLUMN_LOCATION, museum.getLocation());
        cv.put(COLUMN_NAME, museum.getName());
        cv.put(COLUMN_MAP_SIZE, museum.getMapSizeKB());

        database.insert(TABLE_NAME, null, cv);
    }

    private void open() {
        database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createMuseumTable());
    }

    private String createMuseumTable() {
        return "CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_ID + " INT, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_LOCATION + " TEXT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_MAP_SIZE + " INT);";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void close() {
        database.close();
    }

    public Cursor getValues() {
        return this.database.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_DESCRIPTION,
                COLUMN_LOCATION, COLUMN_NAME, COLUMN_MAP_SIZE}, null, null, null, null, null);
    }
}
