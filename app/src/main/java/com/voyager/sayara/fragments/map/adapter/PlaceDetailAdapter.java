package com.voyager.sayara.fragments.map.adapter;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.voyager.sayara.R;
import com.voyager.sayara.fragments.map.model.PlaceDetailModel;

import java.util.List;

/**
 * Created by User on 13-Oct-17.
 */

public class PlaceDetailAdapter extends RecyclerView.Adapter<PlaceDetailAdapter.PlaceDetailViewHolder> {

    List<PlaceDetailModel> placeDetailModel;
    private Activity activity;
    ImageView imageview_clergy_popup;


    public PlaceDetailAdapter(Activity activity, List<PlaceDetailModel> placeDetailModel) {
        this.activity = activity;
        this.placeDetailModel = placeDetailModel;
    }

    @Override
    public PlaceDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_detail_card, parent, false);
        PlaceDetailViewHolder pvh = new PlaceDetailViewHolder(v);
        return pvh;
    }


    @Override
    public void onBindViewHolder(final PlaceDetailViewHolder holder, final int position) {


    }

    @Override
    public int getItemCount() {
        if (placeDetailModel != null && placeDetailModel.size() > 0) {
            return placeDetailModel.size();
        } else {
            return 0;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * ViewHolder class which holds Initialisation to all views and buttons.
     */

    public static class PlaceDetailViewHolder extends RecyclerView.ViewHolder {

        TextView card_clergy_view_title;
        ImageView card_view_clergy_photo, faith_clergy_card_view_outer_img_;
        ImageView image_view_clergy_call;
        TextView clergy_designation;
        TextView tv_image_name;
        LinearLayout clergy_image_title_layout;

        PlaceDetailViewHolder(View itemView) {
            super(itemView);

        }
    }
}
