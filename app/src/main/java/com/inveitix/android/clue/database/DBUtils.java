package com.inveitix.android.clue.database;

import android.content.Context;
import android.database.Cursor;

import com.inveitix.android.clue.cmn.Door;
import com.inveitix.android.clue.cmn.MapPoint;
import com.inveitix.android.clue.cmn.Museum;
import com.inveitix.android.clue.cmn.MuseumMap;
import com.inveitix.android.clue.cmn.QR;
import com.inveitix.android.clue.cmn.Room;
import com.inveitix.android.clue.constants.DBConstants;

public class DBUtils {
    private static DBUtils instance;
    private DBHelper db;

    private DBUtils(Context context) {
        initDB(context);
    }

    public static DBUtils getInstance(Context context) {
        if (instance == null) {
            instance = new DBUtils(context);
        }
        return instance;
    }

    public void writeQrRecord(QR qr) {
        db.insertQr(qr);
    }

    public void updateMapStatus(int museumId, int mapStatus) {
        db.museumStatusUpdate(museumId, mapStatus);
    }

    public void writeMuseumRecord(Museum museum) {
        db.insertMuseum(museum);
    }

    public void writeRoomRecord(Room room) {
        db.insertRoom(room);
    }

    public void writeDoorRecord(Door door) {
        db.insertDoor(door);
    }

    public void writeShapeRecord(MapPoint point) {
        db.insertShape(point);
    }

    public void writeMapRecord(MuseumMap map) {
        db.insertMap(map);
    }

    public Cursor readQrRecord() {
        return db.getQrValues();
    }

    public Cursor readMuseumRecord() {
        return db.getMuseumValues();
    }

    public Cursor readRoomRecord() {
        return db.getRoomValues();
    }

    public Cursor readMapRecord() {
        return db.getMapValues();
    }

    public Cursor readDoorRecord() {
        return db.getDoorValues();
    }

    public Cursor readShapeRecord() {
        return db.getShapeValues();
    }

    private DBHelper initDB(Context context) {
        if (db == null) {
            db = new DBHelper(context);
        }
        return db;
    }

    public boolean isTableEmpty() {
        return db.isTableEmpty(DBConstants.DB_TABLE_MUSEUMS);
    }
}
