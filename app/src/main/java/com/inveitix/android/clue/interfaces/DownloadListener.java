package com.inveitix.android.clue.interfaces;

import com.inveitix.android.clue.cmn.Museum;
import com.inveitix.android.clue.cmn.MuseumMap;

import java.util.List;

public interface DownloadListener {
    /**
     * Gets downloaded museums
     * @param museums
     */
    void onMuseumListDownloaded(List<Museum> museums);
}
