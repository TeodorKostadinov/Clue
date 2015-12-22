package com.inveitix.android.clue.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.inveitix.android.clue.MainActivity;
import com.inveitix.android.clue.R;
import com.inveitix.android.clue.cmn.Museum;
import com.inveitix.android.clue.interfaces.RecyclerViewOnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class RecListAdapter extends RecyclerView.Adapter<RecListAdapter.ViewHolder> {

    private RecyclerViewOnItemClickListener itemClickListener;
    private List<Museum> museums;
    private Context context;
    private boolean isAvailable = false;

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

    public void setOnItemClickListener(final RecyclerViewOnItemClickListener listener) {
        this.itemClickListener = listener;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.txt_museum_name)
        TextView txtName;
        @Bind(R.id.btn_download)
        Button btnDownload;
        @Bind(R.id.progress_bar)
        ProgressBar progressBar;


        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            btnDownload.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_download && isAvailable == false) {

                btnDownload.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                isAvailable = true;
                Toast.makeText(context, "downloading", Toast.LENGTH_SHORT).show();

            } else {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, this.getPosition());
                }
            }

        }


    }
}
