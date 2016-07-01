package com.inveitix.android.clue.interfaces;

import com.inveitix.android.clue.cmn.MuseumMap;

public interface MapDownloadListener {
    /**
     * Gets downloaded maps
     * @param museum
     */
    void onMapDownloaded(MuseumMap museum);
}
