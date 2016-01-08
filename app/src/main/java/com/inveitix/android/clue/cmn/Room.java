package com.inveitix.android.clue.cmn;

import java.util.List;

/**
 * Created by Tito on 21.12.2015 г..
 */
public class Room {

    private List<Door> doors;
    private String id;
    private String mapId;
    private List<QR> qrs;
    private List<MapPoint> shape;

    public Room() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Door> getDoors() {
        return doors;
    }

    public void setDoors(List<Door> doors) {
        this.doors = doors;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public List<QR> getQrs() {
        return qrs;
    }

    public void setQrs(List<QR> qrs) {
        this.qrs = qrs;
    }

    public List<MapPoint> getShape() {
        return shape;
    }

    public void setShape(List<MapPoint> point) {
        this.shape = point;
    }

    public QR getQrById(String qrId) {
        for (QR qr :
                qrs) {
            if (qr.getId().equals(qrId)) return qr;
        }
        return null;
    }
}
