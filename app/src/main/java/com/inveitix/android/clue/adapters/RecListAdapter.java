package com.inveitix.android.clue.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.inveitix.android.clue.R;
import com.inveitix.android.clue.cmn.Museum;
import com.inveitix.android.clue.interfaces.RecyclerViewOnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecListAdapter extends RecyclerView.Adapter<RecListAdapter.ViewHolder> {

    private final OnDownloadClickedListener listener;
    private RecyclerViewOnItemClickListener itemClickListener;
    private List<Museum> museums;
    private Context context;

    public RecListAdapter(Context context, List<Museum> museums, OnDownloadClickedListener listener) {
        this.museums = museums;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_museum, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Museum museum = museums.get(position);
        holder.txtName.setText(museum.getName());
        holder.museumID = museum.getId();

        if (museum.getMapStatus() == Museum.STATUS_DOWNLOADED) {
            holder.btnDownload.setVisibility(View.INVISIBLE);
            holder.progressBar.setVisibility(View.GONE);
        } else if (museum.getMapStatus() == Museum.STATUS_NOT_DOWNLOADED) {
            holder.btnDownload.setVisibility(View.VISIBLE);
            holder.progressBar.setVisibility(View.GONE);
        } else {
            holder.btnDownload.setVisibility(View.INVISIBLE);
            holder.progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return museums.size();
    }

    public void setOnItemClickListener(final RecyclerViewOnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void updateItem(int museumID, int museumStatus) {

        for (int i = 0; i < museums.size(); i++) {
            if (museums.get(i).getId() == museumID) {
                if (museumStatus == Museum.STATUS_DOWNLOADING) {
                    museums.get(i).setMapStatus(Museum.STATUS_DOWNLOADING);
                    break;
                } else if (museumStatus == Museum.STATUS_DOWNLOADED) {
                    museums.get(i).setMapStatus(Museum.STATUS_DOWNLOADED);
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    public interface OnDownloadClickedListener {
        void onDownloadClicked(int museumID);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.txt_museum_name)
        TextView txtName;
        @Bind(R.id.btn_download)
        Button btnDownload;
        @Bind(R.id.progress_bar)
        ProgressBar progressBar;
        int museumID;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            btnDownload.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_download) {
                listener.onDownloadClicked(museumID);
            } else {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, this.getPosition());
                }
            }
        }
    }
}
