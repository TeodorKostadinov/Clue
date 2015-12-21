package com.inveitix.android.clue.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.inveitix.android.clue.R;
import com.inveitix.android.clue.cmn.Museum;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class RecListAdapter extends RecyclerView.Adapter<RecListAdapter.ViewHolder> {

    private List<Museum> museums;
    private Context context;

    public RecListAdapter(Context context, List<Museum> museums) {
        this.museums = museums;
        this.context = context;
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
        holder.txtName.setText(museum.name);
    }


    @Override
    public int getItemCount() {
        return museums.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txt_museum_name)
        TextView txtName;
        @Bind(R.id.btn_download)
        Button btnDownload;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
