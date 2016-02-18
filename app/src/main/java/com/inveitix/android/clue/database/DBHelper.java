package com.inveitix.android.clue.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.inveitix.android.clue.cmn.Museum;

public class DBHelper extends SQLiteOpenHelper {


    protected SQLiteDatabase database;

    public DBHelper(Context context) {
        super(context, DBConstants.DB_NAME, null, DBConstants.DB_VERSION);
        open();
    }

    public void insertMuseum(Museum museum) {
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.KEY_ID, museum.getId());
        cv.put(DBConstants.KEY_DESCRIPTION, museum.getDescription());
        cv.put(DBConstants.KEY_LOCATION, museum.getLocation());
        cv.put(DBConstants.KEY_NAME, museum.getName());
        cv.put(DBConstants.KEY_MAP_SIZE, museum.getMapSizeKB());

        database.insert(DBConstants.TABLE_NAME, null, cv);
    }

    private void open() {
        database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createMuseumTable());
    }

    private String createMuseumTable() {
        return "CREATE TABLE " + DBConstants.TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstants.KEY_ID + " INT, "
                + DBConstants.KEY_DESCRIPTION + " TEXT, "
                + DBConstants.KEY_LOCATION + " TEXT, "
                + DBConstants.KEY_NAME + " TEXT, "
                + DBConstants.KEY_MAP_SIZE + " INT);";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLE_NAME);
        onCreate(db);
    }

    public void close() {
        database.close();
    }

    public Cursor getValues() {
        return this.database.query(DBConstants.TABLE_NAME, new String[]{DBConstants.KEY_ID,
                DBConstants.KEY_DESCRIPTION,DBConstants.KEY_LOCATION, DBConstants.KEY_NAME,
                DBConstants.KEY_MAP_SIZE}, null, null, null, null, null);
    }
}
