package com.inveitix.android.clue.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inveitix.android.clue.R;
import com.inveitix.android.clue.cmn.Museum;
import com.inveitix.android.clue.constants.MuseumConstants;
import com.inveitix.android.clue.ui.MuseumDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecListAdapter extends RecyclerView.Adapter<RecListAdapter.ViewHolder> {

    private List<Museum> museums;
    private Context context;
    private Activity activity;

    public RecListAdapter(Activity activity) {
        this.museums = new ArrayList<>();
        this.context = activity.getApplicationContext();
        this.activity = activity;
    }

    /**
     * Add museums list to adapter
     * @param museums
     */
    public void addItems(List<Museum> museums) {
        this.museums = museums;
        refreshMuseumList();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_museum, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Museum museum = museums.get(position);
        holder.txtName.setText(museum.getName());
        holder.museumID = museum.getId();
        if (isConnected()) {
            Picasso.with(context)
                    .load(museum.getImageURL())
                    .resize(300, 200)
                    .centerCrop()
                    .into(holder.viewBackground);
        }
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI
                    || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return museums.size();
    }

    /**
     * Refresh museums list when you made any change in data
     */
    public void refreshMuseumList() {
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.view_background)
        ImageView viewBackground;
        @Bind(R.id.txt_museum_name)
        TextView txtName;
        int museumID;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intentDetails = new Intent(activity, MuseumDetailsActivity.class);
            intentDetails.putExtra(MuseumConstants.MUSEUM_ID, museumID);
            String transitionName = activity.getString(R.string.transition_museum_cover);
            ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                            viewBackground,
                            transitionName);
            ActivityCompat.startActivity(activity, intentDetails, options.toBundle());
        }
    }
}
