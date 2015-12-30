package com.inveitix.android.clue.cmn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tito on 28.12.2015 г..
 */
public class MuseumMap {

    List<Room> rooms;
    private String id;
    private int museumId;
    private String entranceRoomId;

    public MuseumMap() {
        rooms = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMuseumId() {
        return museumId;
    }

    public void setMuseumId(int museumId) {
        this.museumId = museumId;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }


    public String getEntranceRoomId() {
        return entranceRoomId;
    }

    public Room getRoomById(String roomId) {
        for(Room room : rooms) {
            if(room.getId().equals(roomId)) {
                return room;
            }
        }
        return null;
    }
}
