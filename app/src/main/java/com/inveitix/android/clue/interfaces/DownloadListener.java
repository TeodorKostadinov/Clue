package com.inveitix.android.clue.interfaces;

import com.inveitix.android.clue.cmn.Museum;
import com.inveitix.android.clue.cmn.MuseumMap;

import java.util.List;

public interface DownloadListener {
    void onMuseumListDownloaded(List<Museum> museums);

    void onMuseumDownloaded(MuseumMap museum);
}
