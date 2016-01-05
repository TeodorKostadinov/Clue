package com.inveitix.android.clue.database;

import android.content.Context;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.inveitix.android.clue.cmn.Museum;
import com.inveitix.android.clue.cmn.MuseumMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tito on 23.12.2015 г..
 */
public class FireBaseLoader {
    private static final String TAG = "FireBaseLoader";
    private static FireBaseLoader instance;
    private Firebase fireBaseRef;

    public FireBaseLoader(Context context) {
        Firebase.setAndroidContext(context);
        fireBaseRef = new Firebase(FireBaseConstants.FIREBASE_URL);
    }

    public static FireBaseLoader getInstance(Context context) {
        if (instance == null) {
            instance = new FireBaseLoader(context);
        }
        return instance;
    }

    public void downloadMuseumsList(final DownloadListener listener) {

        fireBaseRef.child("museums").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Museum> museums = new ArrayList<>();

                for (DataSnapshot postMuseum : dataSnapshot.getChildren()) {

                    Museum museum = postMuseum.getValue(Museum.class);

                    if (museums.size() < 1) {
                        museums.add(museum);
                    } else {
                        for (int i = 0; i < museums.size(); i++) {
                            if (museums.get(i).getId() == museum.getId()) {
                                museums.remove(i);
                                museums.add(museum);
                            } else {
                                museums.add(museum);
                            }
                        }
                    }
                }
                listener.onMuseumListDownloaded(museums);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(TAG, firebaseError.toString());
            }
        });
    }


    public void downloadMap(final int museumId, final DownloadListener listener) {

        fireBaseRef.child("maps").orderByChild("museumId").equalTo(museumId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postMaps : dataSnapshot.getChildren()) {
                    Log.e("MAP", String.valueOf(postMaps.getValue()));
                    MuseumMap map = postMaps.getValue(MuseumMap.class);
                    Log.e("MAP", "MuseumID: " + String.valueOf(map.getMuseumId()));
                    listener.onMuseumDownloaded(map);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(TAG, firebaseError.toString());
            }
        });

    }

    public interface DownloadListener {
        void onMuseumListDownloaded(List<Museum> museums);

        void onMuseumDownloaded(MuseumMap museum);
    }
}
