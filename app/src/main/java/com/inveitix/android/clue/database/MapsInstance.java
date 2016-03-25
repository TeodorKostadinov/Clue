package com.inveitix.android.clue.database;

import com.inveitix.android.clue.cmn.MuseumMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fos on 30.12.2015 г..
 */
public class MapsInstance {
    private static MapsInstance instance;
    private List<MuseumMap> maps;

    private MapsInstance() {
        maps = new ArrayList<>();
    }

    public static MapsInstance getInstance() {
        if (instance == null) {
            instance = new MapsInstance();
        }
        return instance;
    }

    public void addMap(MuseumMap map) {
        maps.add(map);
    }

    public MuseumMap getMapByMuseumId(int museumId) {
        for (MuseumMap map :
                maps) {
            if (map.getMuseumId() == museumId) return map;
        }
        return null;
    }
}
