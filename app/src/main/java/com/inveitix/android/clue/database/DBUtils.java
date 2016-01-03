package com.inveitix.android.clue.database;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by Tito on 3.1.2016 г..
 */
public class DBUtils {
    private static DBHelper db;

    public static void writeRecord(Context c, String record) {
        initDB(c);
        db.insert(record);
    }

    public static Cursor readRecord(Context context){
        initDB(context);
        return db.getValues();
    }

    private static void initDB(Context context) {
        if (db == null) {
            db = new DBHelper(context);
        }
    }
}
