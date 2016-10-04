package com.inveitix.android.clue.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.inveitix.android.clue.cmn.Door;
import com.inveitix.android.clue.cmn.MapPoint;
import com.inveitix.android.clue.cmn.Museum;
import com.inveitix.android.clue.cmn.MuseumMap;
import com.inveitix.android.clue.cmn.QR;
import com.inveitix.android.clue.cmn.Room;
import com.inveitix.android.clue.constants.DBConstants;

public class DBHelper extends SQLiteOpenHelper {


    protected SQLiteDatabase database;

    public DBHelper(Context context) {
        super(context, DBConstants.DB_NAME, null, DBConstants.DB_VERSION);
        open();
    }

    public void insertMuseum(Museum museum) {
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.KEY_ID, museum.getId());
        cv.put(DBConstants.KEY_URL, museum.getImageURL());
        cv.put(DBConstants.KEY_DESCRIPTION, museum.getDescription());
        cv.put(DBConstants.KEY_LOCATION, museum.getLocation());
        cv.put(DBConstants.KEY_NAME, museum.getName());
        cv.put(DBConstants.KEY_MAP_STATUS, museum.getMapStatus());
        cv.put(DBConstants.KEY_MAP_SIZE, museum.getMapSizeKB());

        database.insert(DBConstants.DB_TABLE_MUSEUMS, null, cv);
    }

    public void insertRoom(Room room) {
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.KEY_ID, room.getId());
        for (Door door : room.getDoors()) {
            cv.put(DBConstants.KEY_DOORS, door.getConnectedTo());
        }
        cv.put(DBConstants.KEY_MAP_ID, room.getMapId());
        for (QR qr : room.getQrs()) {
            cv.put(DBConstants.KEY_QRS, qr.getId());
        }
        for (MapPoint point : room.getShape()) {
            cv.put(DBConstants.KEY_SHAPE, point.getX());
        }
        database.insert(DBConstants.DB_TABLE_ROOMS, null, cv);
    }

    public void insertMap(MuseumMap map) {
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.KEY_ID, map.getId());
        for (Room room : map.getRooms()) {
            cv.put(DBConstants.KEY_ROOM_ID, room.getId());
        }
        cv.put(DBConstants.KEY_MUSEUM_ID, map.getMuseumId());
        cv.put(DBConstants.KEY_ENTRANCE_ROOM_ID, map.getEntranceRoomId());
        database.insert(DBConstants.DB_TABLE_MAPS, null, cv);
    }

    public void insertDoor(Door door) {
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.KEY_CONNECTED_TO, door.getConnectedTo());
        cv.put(DBConstants.KEY_X, door.getX());
        cv.put(DBConstants.KEY_Y, door.getY());
        cv.put(DBConstants.KEY_MAP_ID, door.getMapId());
        cv.put(DBConstants.KEY_ROOM_ID, door.getRoomId());
        cv.put(DBConstants.KEY_ID, door.getId());
        database.insert(DBConstants.DB_TABLE_DOORS, null, cv);
    }

    public void insertShape(MapPoint point) {
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.KEY_X, point.getX());
        cv.put(DBConstants.KEY_Y, point.getY());
        cv.put(DBConstants.KEY_MAP_ID, point.getMapId());
        cv.put(DBConstants.KEY_ID, point.getId());
        cv.put(DBConstants.KEY_ROOM_ID, point.getRoomId());
        database.insert(DBConstants.DB_TABLE_SHAPE, null, cv);
    }

    public void insertQr(QR qr) {
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.KEY_ID, qr.getId());
        cv.put(DBConstants.KEY_INFO, qr.getInfo());
        cv.put(DBConstants.KEY_X, qr.getX());
        cv.put(DBConstants.KEY_Y, qr.getY());
        cv.put(DBConstants.KEY_MAP_ID, qr.getMapId());
        cv.put(DBConstants.KEY_ROOM_ID, qr.getRoomId());
        database.insert(DBConstants.DB_TABLE_QRS, null, cv);
    }

    private void open() {
        database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createMuseumTable());
        db.execSQL(createMapTable());
        db.execSQL(createRoomsTable());
        db.execSQL(createQrsTable());
        db.execSQL(createDoorsTable());
        db.execSQL(createShapeTable());
    }

    private String createDoorsTable() {
        return "CREATE TABLE IF NOT EXISTS " + DBConstants.DB_TABLE_DOORS + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstants.KEY_CONNECTED_TO + " TEXT, "
                + DBConstants.KEY_MAP_ID + " TEXT, "
                + DBConstants.KEY_ID + " TEXT, "
                + DBConstants.KEY_ROOM_ID + " TEXT, "
                + DBConstants.KEY_X + " REAL, "
                + DBConstants.KEY_Y + " REAL);";
    }

    private String createShapeTable() {
        return "CREATE TABLE IF NOT EXISTS " + DBConstants.DB_TABLE_SHAPE + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstants.KEY_X + " REAL, "
                + DBConstants.KEY_ROOM_ID + " TEXT, "
                + DBConstants.KEY_MAP_ID + " TEXT, "
                + DBConstants.KEY_ID + " TEXT, "
                + DBConstants.KEY_Y + " REAL);";
    }

    private String createRoomsTable() {
        return "CREATE TABLE IF NOT EXISTS " + DBConstants.DB_TABLE_ROOMS + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstants.KEY_DOORS + " TEXT, "
                + DBConstants.KEY_ID + " TEXT, "
                + DBConstants.KEY_SHAPE + " REAL, "
                + DBConstants.KEY_MAP_ID + " TEXT, "
                + DBConstants.KEY_QRS + " TEXT);";
    }

    private String createMapTable() {
        return "CREATE TABLE IF NOT EXISTS " + DBConstants.DB_TABLE_MAPS + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstants.KEY_ROOM_ID + " TEXT, "
                + DBConstants.KEY_ID + " TEXT, "
                + DBConstants.KEY_MUSEUM_ID + " INT, "
                + DBConstants.KEY_ENTRANCE_ROOM_ID + " TEXT);";
    }

    private String createMuseumTable() {
        return "CREATE TABLE IF NOT EXISTS " + DBConstants.DB_TABLE_MUSEUMS + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstants.KEY_ID + " INT, "
                + DBConstants.KEY_DESCRIPTION + " TEXT, "
                + DBConstants.KEY_LOCATION + " TEXT, "
                + DBConstants.KEY_NAME + " TEXT, "
                + DBConstants.KEY_URL + " TEXT, "
                + DBConstants.KEY_MAP_STATUS + " INT, "
                + DBConstants.KEY_MAP_SIZE + " INT);";
    }

    private String createQrsTable() {
        return "CREATE TABLE IF NOT EXISTS " + DBConstants.DB_TABLE_QRS + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstants.KEY_INFO + " TEXT, "
                + DBConstants.KEY_ID + " TEXT, "
                + DBConstants.KEY_X + " REAL, "
                + DBConstants.KEY_MAP_ID + " TEXT, "
                + DBConstants.KEY_ROOM_ID + " TEXT, "
                + DBConstants.KEY_Y + " REAL);";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.DB_TABLE_MUSEUMS);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.DB_TABLE_MAPS);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.DB_TABLE_ROOMS);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.DB_TABLE_QRS);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.DB_TABLE_DOORS);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.DB_TABLE_SHAPE);
        onCreate(db);
    }

    public void museumStatusUpdate(int museumId, int mapStatus) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.KEY_MAP_STATUS, mapStatus);
        database.update(DBConstants.DB_TABLE_MUSEUMS, values, DBConstants.KEY_ID + "=" + museumId, null);
    }

    public boolean isTableEmpty(String tableName) {
        String count = "SELECT count(*) FROM " + tableName;
        Cursor mCursor = database.rawQuery(count, null);
        mCursor.moveToFirst();
        int iCount = mCursor.getInt(0);
        mCursor.close();
        return iCount <= 0;
    }

    public void close() {
        database.close();
    }

    public Cursor getShapeValues() {
        return this.database.query(DBConstants.DB_TABLE_SHAPE, new String[]{
                        DBConstants.KEY_X, DBConstants.KEY_Y, DBConstants.KEY_ROOM_ID, DBConstants.KEY_MAP_ID, DBConstants.KEY_ID},
                null, null, null, null, null);
    }

    public Cursor getDoorValues() {
        return this.database.query(DBConstants.DB_TABLE_DOORS, new String[]{DBConstants.KEY_CONNECTED_TO,
                        DBConstants.KEY_MAP_ID, DBConstants.KEY_ROOM_ID, DBConstants.KEY_ID, DBConstants.KEY_X, DBConstants.KEY_Y},
                null, null, null, null, null);
    }

    public Cursor getQrValues() {
        return this.database.query(DBConstants.DB_TABLE_QRS, new String[]{DBConstants.KEY_ID,
                DBConstants.KEY_INFO, DBConstants.KEY_X, DBConstants.KEY_Y, DBConstants.KEY_MAP_ID,
                DBConstants.KEY_ROOM_ID}, null, null, null, null, null);
    }

    public Cursor getMuseumValues() {
        return this.database.query(DBConstants.DB_TABLE_MUSEUMS, new String[]{DBConstants.KEY_ID,
                DBConstants.KEY_DESCRIPTION, DBConstants.KEY_LOCATION, DBConstants.KEY_MAP_STATUS,
                DBConstants.KEY_NAME, DBConstants.KEY_MAP_SIZE, DBConstants.KEY_URL}
                , null, null, null, null, null);
    }

    public Cursor getRoomValues() {
        return this.database.query(DBConstants.DB_TABLE_ROOMS, new String[]{DBConstants.KEY_ID,
                DBConstants.KEY_DOORS, DBConstants.KEY_MAP_ID, DBConstants.KEY_SHAPE,
                DBConstants.KEY_QRS}, null, null, null, null, null);
    }

    public Cursor getMapValues() {
        return this.database.query(DBConstants.DB_TABLE_MAPS, new String[]{DBConstants.KEY_ID,
                DBConstants.KEY_ROOM_ID, DBConstants.KEY_MUSEUM_ID, DBConstants.KEY_ENTRANCE_ROOM_ID}
                , null, null, null, null, null);
    }
}
