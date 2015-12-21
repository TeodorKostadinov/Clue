package com.inveitix.android.clue.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.inveitix.android.clue.cmn.Exhibit;
import com.inveitix.android.clue.cmn.Museum;

import java.util.List;

/**
 * Created by Tito on 21.12.2015 г..
 */
public class ListAdapter extends BaseAdapter {

    List<Museum> museums;

    public ListAdapter(List<Museum> museums) {
        this.museums = museums;
    }

    @Override
    public int getCount() {
        return museums.size();
    }

    @Override
    public Museum getItem(int position) {
        return museums.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
