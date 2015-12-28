package com.inveitix.android.clue.database;

import android.content.Context;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.inveitix.android.clue.adapters.RecListAdapter;
import com.inveitix.android.clue.cmn.Museum;
import com.inveitix.android.clue.cmn.MuseumMap;

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

    public void loadingDataBase(final List<MuseumMap> maps, final List<Museum> museums, final RecListAdapter adapter) {
        loadingOnlineMuseumDataBase(museums, adapter);
        loadingOnlineMapsDataBase(maps, adapter);
    }

    private void loadingOnlineMuseumDataBase(final List<Museum> museums, final RecListAdapter adapter) {

        fireBaseRef.child("museums").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postMuseum : dataSnapshot.getChildren()) {
                    String name = (String) postMuseum.child("name").getValue();
                    String description = (String) postMuseum.child("description").getValue();
                    int id = Integer.parseInt(postMuseum.child("id").getValue().toString());
                    String location = (String) postMuseum.child("location").getValue();
                    int mapSizeKB = Integer.parseInt(postMuseum.child("mapSizeKB").getValue().toString());
                    Museum museum = new Museum(name, description, id, location, mapSizeKB);

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
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(TAG, firebaseError.toString());
            }
        });
    }


    private void loadingOnlineMapsDataBase(final List<MuseumMap> maps, final RecListAdapter adapter) {

        fireBaseRef.child("maps").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postMaps : dataSnapshot.getChildren()) {
                    MuseumMap map = postMaps.getValue(MuseumMap.class);

                    if (maps.size() < 1) {
                        maps.add(map);
                    } else {
                        for (int i = 0; i < maps.size(); i++) {
                            if (maps.get(i).getId().equals(map.getId())) {
                                maps.remove(i);
                                maps.add(map);
                            } else {
                                maps.add(map);
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(TAG, firebaseError.toString());
            }
        });

    }
}
