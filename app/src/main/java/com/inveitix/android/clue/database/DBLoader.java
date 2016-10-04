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
import com.inveitix.android.clue.interfaces.DownloadListener;
import com.inveitix.android.clue.interfaces.MapDownloadListener;

import java.util.ArrayList;
import java.util.List;

public class DBLoader {
    private static DBLoader instance;
    private List<Museum> museums;
    private Context context;
    private DBUtils dbUtils;

    private DBLoader(Context context) {
        museums = new ArrayList<>();
        this.context = context;
        dbUtils = DBUtils.getInstance(context);
    }

    public static DBLoader getInstance(Context context) {
        if (instance == null) {
            instance = new DBLoader(context);
        }
        return instance;
    }

    public List<Museum> getMuseums() {
        return museums;
    }

    public void loadContent(final DownloadListener listener) {

        if (dbUtils.isTableEmpty()) {
            FireBaseLoader.getInstance(context).downloadMuseumsList(new DownloadListener() {
                @Override
                public void onMuseumListDownloaded(List<Museum> museums) {
                    loadMuseumsList(listener);
                }
            });
        } else {
            FireBaseLoader.getInstance(context).downloadMuseumsList(listener);
            loadMuseumsList(listener);
        }
    }

    public void loadDownloadedMap(final MapDownloadListener mapDownloadListener) {
        List<MuseumMap> maps = new ArrayList<>();
        Cursor cursor = dbUtils.readMapRecord();
        if (cursor.moveToFirst()) {
            do {
                MuseumMap map = new MuseumMap();
                map.setMuseumId(cursor.getInt(cursor.getColumnIndex(DBConstants.KEY_MUSEUM_ID)));
                map.setId(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_ID)));
                map.setRooms(loadRooms(map.getId()));
                map.setEntranceRoomId(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_ENTRANCE_ROOM_ID)));

                if (!duplicateCheck(maps, map)) {
                    maps.add(map);
                }
                mapDownloadListener.onMapDownloaded(map);
            } while (cursor.moveToNext());
        }
    }

    private List<QR> loadQrs(String mapId, String roomId) {
        Cursor cursor = dbUtils.readQrRecord();
        List<QR> qrs = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                QR qr = new QR();
                if (mapId.equals(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_MAP_ID)))
                        && roomId.equals(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_ROOM_ID)))) {
                    qr.setId(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_ID)));
                    qr.setMapId(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_MAP_ID)));
                    qr.setInfo(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_INFO)));
                    qr.setRoomId(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_ROOM_ID)));
                    qr.setX(cursor.getDouble(cursor.getColumnIndex(DBConstants.KEY_X)));
                    qr.setY(cursor.getDouble(cursor.getColumnIndex(DBConstants.KEY_Y)));

                    if (!duplicateCheck(qrs, qr)) {
                        qrs.add(qr);
                    }
                }
            } while (cursor.moveToNext());
        }
        return qrs;
    }

    private List<Door> loadDoors(String mapId, String roomId) {
        Cursor cursor = dbUtils.readDoorRecord();
        List<Door> doors = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Door door = new Door();
                if (mapId.equals(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_MAP_ID)))
                        && roomId.equals(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_ROOM_ID)))) {
                    door.setConnectedTo(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_CONNECTED_TO)));
                    door.setMapId(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_MAP_ID)));
                    door.setX(cursor.getFloat(cursor.getColumnIndex(DBConstants.KEY_X)));
                    door.setY(cursor.getFloat(cursor.getColumnIndex(DBConstants.KEY_Y)));
                    door.setId(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_ID)));

                    if (!duplicateCheck(doors, door)) {
                        doors.add(door);
                    }
                }
            } while (cursor.moveToNext());
        }
        return doors;
    }

    private List<MapPoint> loadShape(String mapId, String roomId) {
        Cursor cursor = dbUtils.readShapeRecord();
        List<MapPoint> shape = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                MapPoint point = new MapPoint();
                if (mapId.equals(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_MAP_ID)))
                        && roomId.equals(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_ROOM_ID)))) {
                    point.setMapId(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_MAP_ID)));
                    point.setX(cursor.getFloat(cursor.getColumnIndex(DBConstants.KEY_X)));
                    point.setY(cursor.getFloat(cursor.getColumnIndex(DBConstants.KEY_Y)));
                    point.setId(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_ID)));
                    point.setRoomId(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_ROOM_ID)));
                    if (!duplicateCheck(shape, point)) {
                        shape.add(point);
                    }
                }
            } while (cursor.moveToNext());
        }
        return shape;
    }

    private List<Room> loadRooms(String mapId) {
        Cursor cursor = dbUtils.readRoomRecord();

        List<Room> rooms = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                if (mapId.equals(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_MAP_ID)))) {
                    Room room = new Room();
                    room.setId(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_ID)));
                    room.setMapId(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_MAP_ID)));
                    room.setQrs(loadQrs(mapId, room.getId()));
                    room.setDoors(loadDoors(mapId, room.getId()));
                    room.setShape(loadShape(mapId, room.getId()));

                    if (!duplicateCheck(rooms, room)) {
                        rooms.add(room);
                    }
                }
            } while (cursor.moveToNext());
        }
        return rooms;
    }

    private void loadMuseumsList(final DownloadListener listener) {
        Cursor cursor = dbUtils.readMuseumRecord();

        if (cursor.moveToFirst()) {
            do {
                Museum museum = new Museum();
                museum.setImageURL(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_URL)));
                museum.setDescription(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_DESCRIPTION)));
                museum.setLocation(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_LOCATION)));
                museum.setMapSizeKB(cursor.getInt(cursor.getColumnIndex(DBConstants.KEY_MAP_SIZE)));
                museum.setId(cursor.getInt(cursor.getColumnIndex(DBConstants.KEY_ID)));
                museum.setName(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_NAME)));
                museum.setMapStatus(cursor.getInt(cursor.getColumnIndex(DBConstants.KEY_MAP_STATUS)));

                if (!duplicateCheck(museums, museum)) {
                    museums.add(museum);
                }
            } while (cursor.moveToNext());
        }
        listener.onMuseumListDownloaded(museums);
    }

    private boolean duplicateCheck(List<Room> rooms, Room room) {
        if (rooms.size() > 1) {
            for (int i = 0; i < rooms.size(); i++) {
                if (rooms.get(i).getId().equals(room.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean duplicateCheck(List<MuseumMap> maps, MuseumMap map) {
        if (maps.size() > 1) {
            for (int i = 0; i < maps.size(); i++) {
                if (maps.get(i).getId().equals(map.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean duplicateCheck(List<MapPoint> shape, MapPoint point) {
        if (shape.size() > 1) {
            for (int i = 0; i < shape.size(); i++) {
                if (shape.get(i).getId().equals(point.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean duplicateCheck(List<Museum> museums, Museum museum) {
        if (museums.size() > 1) {
            for (int i = 0; i < museums.size(); i++) {
                if (museums.get(i).getId() == museum.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean duplicateCheck(List<Door> doors, Door door) {
        if (doors.size() > 1) {
            for (int i = 0; i < doors.size(); i++) {
                if (doors.get(i).getId().equals(door.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean duplicateCheck(List<QR> qrs, QR qr) {
        if (qrs.size() > 1) {
            for (int i = 0; i < qrs.size(); i++) {
                if (qrs.get(i).getId().equals(qr.getId())) {
                    return true;
                }
            }
        }
        return false;
    }
}
