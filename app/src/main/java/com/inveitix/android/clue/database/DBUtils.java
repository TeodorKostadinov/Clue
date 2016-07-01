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

    /**
     * Write record in QRs table
     *
     * @param qr
     */
    public void writeQrRecord(QR qr) {
        db.insertQr(qr);
    }

    /**
     * Select museum from th database by ID and change map status
     *
     * @param museumId
     * @param mapStatus
     */
    public void updateMapStatus(int museumId, int mapStatus) {
        db.museumStatusUpdate(museumId, mapStatus);
    }

    /**
     * Write record in museums table
     *
     * @param museum
     */
    public void writeMuseumRecord(Museum museum) {
        db.insertMuseum(museum);
    }

    /**
     * Write record in rooms table
     *
     * @param room
     */
    public void writeRoomRecord(Room room) {
        db.insertRoom(room);
    }

    /**
     * Write record in doors table
     *
     * @param door
     */
    public void writeDoorRecord(Door door) {
        db.insertDoor(door);
    }

    /**
     * Write record in shapes table
     *
     * @param point
     */
    public void writeShapeRecord(MapPoint point) {
        db.insertShape(point);
    }

    /**
     * Write record in maps table
     *
     * @param map
     */
    public void writeMapRecord(MuseumMap map) {
        db.insertMap(map);
    }

    /**
     * Method for reading data from QRs table
     *
     * @return Cursor
     */
    public Cursor readQrRecord() {
        return db.getQrValues();
    }

    /**
     * Method for reading data from museums table
     *
     * @return Cursor
     */
    public Cursor readMuseumRecord() {
        return db.getMuseumValues();
    }

    /**
     * Method for reading data from rooms table
     *
     * @return Cursor
     */
    public Cursor readRoomRecord() {
        return db.getRoomValues();
    }

    /**
     * Method for reading data from maps table
     *
     * @return Cursor
     */
    public Cursor readMapRecord() {
        return db.getMapValues();
    }

    /**
     * Method for reading data from doors table
     *
     * @return Cursor
     */
    public Cursor readDoorRecord() {
        return db.getDoorValues();
    }

    /**
     * Method for reading data from shapes table
     *
     * @return Cursor
     */
    public Cursor readShapeRecord() {
        return db.getShapeValues();
    }

    private DBHelper initDB(Context context) {
        if (db == null) {
            db = new DBHelper(context);
        }
        return db;
    }

    /**
     * Check if museums table is empty and returns true or false
     *
     * @return boolean
     */
    public boolean isEmpty() {
        return db.isEmpty(DBConstants.DB_TABLE_MUSEUMS);
    }
}
