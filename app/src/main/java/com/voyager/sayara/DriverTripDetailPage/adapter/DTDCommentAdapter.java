package com.voyager.sayara.DriverTripDetailPage.adapter;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.voyager.sayara.DriverTripDetailPage.model.Comments;
import com.voyager.sayara.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 13-Oct-17.
 */

public class DTDCommentAdapter extends RecyclerView.Adapter<DTDCommentAdapter.PlaceDetailViewHolder> {

    List<Comments> userCommentsList =new ArrayList<>();
    private Activity activity;


    public DTDCommentAdapter(List<Comments> userCommentsList) {
        this.userCommentsList = userCommentsList;
    }

    @Override
    public PlaceDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_comment_on_driver, parent, false);
        PlaceDetailViewHolder pvh = new PlaceDetailViewHolder(v);
        return pvh;
    }


    @Override
    public void onBindViewHolder(final PlaceDetailViewHolder holder, final int position) {
        final Comments userComments = userCommentsList.get(position);
        holder.commentTxt.setText(userComments.getTripComment());
    }

    @Override
    public int getItemCount() {
        if (userCommentsList != null && userCommentsList.size() > 0) {
            return userCommentsList.size();
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

        TextView commentTxt;
        ImageView commentImg;

        PlaceDetailViewHolder(View itemView) {
            super(itemView);
            commentImg = (ImageView) itemView.findViewById(R.id.driverImg);
            commentTxt = (TextView) itemView.findViewById(R.id.commentTxt);
        }
    }
}
