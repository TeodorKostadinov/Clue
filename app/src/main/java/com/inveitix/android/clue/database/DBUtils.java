package com.inveitix.android.clue.database;

import android.content.Context;
import android.database.Cursor;

import com.inveitix.android.clue.cmn.Museum;

public class DBUtils {
    private static DBHelper db;

    public static void writeRecord(Context c, Museum museum) {
        initDB(c);
        db.insertMuseum(museum);
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
