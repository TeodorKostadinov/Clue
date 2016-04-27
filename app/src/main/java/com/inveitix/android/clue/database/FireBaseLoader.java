package com.inveitix.android.clue.database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.inveitix.android.clue.cmn.Door;
import com.inveitix.android.clue.cmn.MapPoint;
import com.inveitix.android.clue.cmn.Museum;
import com.inveitix.android.clue.cmn.MuseumMap;
import com.inveitix.android.clue.cmn.QR;
import com.inveitix.android.clue.cmn.Room;
import com.inveitix.android.clue.interfaces.DownloadListener;

import java.util.ArrayList;
import java.util.List;

public class FireBaseLoader {
    private static final String TAG = "FireBaseLoader";
    private static FireBaseLoader instance;
    private DBUtils dbUtils;
    private Firebase fireBaseRef;

    public FireBaseLoader(Context context) {
        Firebase.setAndroidContext(context);
        dbUtils = DBUtils.getInstance(context);
        fireBaseRef = new Firebase(FireBaseConstants.FIREBASE_URL);
    }

    public static FireBaseLoader getInstance(Context context) {
        if (instance == null) {
            instance = new FireBaseLoader(context);
        }
        return instance;
    }

    public void downloadMuseumsList(final DownloadListener listener) {
        final boolean isEmpty = dbUtils.isEmpty();
        fireBaseRef.child("museums").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Museum> museums = new ArrayList<>();

                for (DataSnapshot postMuseum : dataSnapshot.getChildren()) {

                    Museum museum = postMuseum.getValue(Museum.class);

                    if (!duplicateCheck(museums, museum)) {
                        museums.add(museum);
                        dbUtils.writeMuseumRecord(museum);
                    }
                }
                if (isEmpty){
                    listener.onMuseumListDownloaded(museums);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(TAG, firebaseError.toString());
            }
        });
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

    public void downloadMap(final int museumId, final DownloadListener listener) {

        fireBaseRef.child("maps").orderByChild("museumId").equalTo(museumId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postMaps : dataSnapshot.getChildren()) {

                    MuseumMap map = postMaps.getValue(MuseumMap.class);
                    Cursor cursor = dbUtils.readMapRecord();

                    boolean isDuplicated = false;
                    if (cursor.moveToFirst()) {
                        do {
                            if (cursor.getString(cursor.getColumnIndex(DBConstants.KEY_ID)).equals(map.getId())) {
                                isDuplicated = true;
                            }
                        } while (cursor.moveToNext());
                    }
                    if (!isDuplicated) {
                        dbUtils.writeMapRecord(map);
                        for (Room room : map.getRooms()) {
                            dbUtils.writeRoomRecord(room);
                            for (QR qr : room.getQrs()) {
                                dbUtils.writeQrRecord(qr);
                            }

                            for (Door door : room.getDoors()) {
                                dbUtils.writeDoorRecord(door);
                            }

                            for (MapPoint shape : room.getShape()) {
                                dbUtils.writeShapeRecord(shape);
                            }
                        }
                        listener.onMuseumDownloaded(map);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(TAG, firebaseError.toString());
            }
        });

    }
}
