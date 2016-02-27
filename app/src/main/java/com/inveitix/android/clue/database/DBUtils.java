package com.inveitix.android.clue.database;

import android.content.Context;
import android.database.Cursor;

import com.inveitix.android.clue.cmn.Door;
import com.inveitix.android.clue.cmn.MapPoint;
import com.inveitix.android.clue.cmn.Museum;
import com.inveitix.android.clue.cmn.MuseumMap;
import com.inveitix.android.clue.cmn.QR;
import com.inveitix.android.clue.cmn.Room;

public class DBUtils {
    private static DBHelper db;

    public static void writeQrRecord(Context c, QR qr) {
        initDB(c);
        db.insertQr(qr);
    }

    public static void writeMuseumRecord(Context c, Museum museum) {
        initDB(c);
        db.insertMuseum(museum);
    }

    public static void writeRoomRecord(Context c, Room room) {
        initDB(c);
        db.insertRoom(room);
    }

    public static void writeDoorRecord(Context c, Door door) {
        initDB(c);
        db.insertDoor(door);
    }

    public static void writeShapeRecord(Context c, MapPoint point) {
        initDB(c);
        db.insertShape(point);
    }

    public static void writeMapRecord(Context c, MuseumMap map) {
        initDB(c);
        db.insertMap(map);
    }

    public static Cursor readQrRecord(Context context) {
        initDB(context);
        return db.getQrValues();
    }

    public static Cursor readMuseumRecord(Context context) {
        initDB(context);
        return db.getMuseumValues();
    }

    public static Cursor readRoomRecord(Context context) {
        initDB(context);
        return db.getRoomValues();
    }

    public static Cursor readMapRecord(Context context) {
        initDB(context);
        return db.getMapValues();
    }

    public static Cursor readDoorRecord(Context context) {
        initDB(context);
        return db.getDoorValues();
    }

    public static Cursor readShapeRecord(Context context) {
        initDB(context);
        return db.getShapeValues();
    }

    private static void initDB(Context context) {

        if (db == null) {
            db = new DBHelper(context);
        }
    }


}
