package com.inveitix.android.clue.database;

import android.content.Context;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.inveitix.android.clue.adapters.RecListAdapter;
import com.inveitix.android.clue.cmn.Museum;

import java.util.List;

/**
 * Created by Tito on 23.12.2015 г..
 */
public class FireBaseLoader {
    private static final String TAG = "FireBaseLoader";
    private Firebase fireBaseRef;

    private static FireBaseLoader instance;

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

    public void loadingOnlineDataBase(final List<Museum> museums, final RecListAdapter adapter) {

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
}
