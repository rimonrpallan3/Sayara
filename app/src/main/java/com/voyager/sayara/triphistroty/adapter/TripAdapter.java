package com.voyager.sayara.triphistroty.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.voyager.sayara.R;
import com.voyager.sayara.triphistroty.model.TripDetails;
import com.voyager.sayara.yourtripdetail.YourTripDetail;

import java.util.List;

/**
 * Created by User on 26-Dec-17.
 */

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.MyViewHolder> {

    private List<TripDetails> tripList;
    Activity activity;
    String time;
    String date;
    int userID;

    public TripAdapter(List<TripDetails> tripList, Activity activity, int userId) {
        this.tripList = tripList;
        this.activity = activity;
        this.userID = userId;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final TripDetails tripDetails = tripList.get(position);
        Toast.makeText(activity,"U have entered onBindViewHolder "+position,Toast.LENGTH_SHORT).show();
        tripDetails.setUserId(userID);
        String[] DateTime = tripDetails.getDateTime().split("\\s");
        for (int x=0; x<DateTime.length; x++) {
            System.out.println("x : "+x+" = "+DateTime[x]);
            date = DateTime[0];
            holder.THDate.setText(date);
            time = DateTime[1];
            holder.THTime.setText(time);
        }

        holder.THCarName.setText(tripDetails.getDriverCar());
        holder.THDBAmount.setText(tripDetails.getCash());
        holder.THCarName.setText(tripDetails.getDriverCar());
        holder.THPaymentMode.setText(tripDetails.getCashMode());

        Picasso.with(activity)
                .load(tripDetails.getTripImgUrl())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .resize(0, 200)
                .into(holder.mapCardTripHistoryImg, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(activity)
                                .load(tripDetails.getTripImgUrl())
                                .error(R.drawable.profile)
                                .resize(0, 200)
                                .into(holder.mapCardTripHistoryImg, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });

        holder.THCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity,"U have clicked this Card: "+position,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, YourTripDetail.class);
                intent.putExtra("TripDetails", tripDetails);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView THDate;
        public TextView THTime;
        public TextView THDBAmount;
        public TextView THCarName;
        public TextView THPaymentMode;
        public ImageView mapCardTripHistoryImg;
        public ImageView THStar1;
        public ImageView THStar2;
        public ImageView THStar3;
        public ImageView THStar4;
        public LinearLayout THCardView;


        public MyViewHolder(View view) {
            super(view);
            THDate = (TextView) view.findViewById(R.id.THDate);
            THTime = (TextView) view.findViewById(R.id.THTime);
            THDBAmount = (TextView) view.findViewById(R.id.THDBAmount);
            THCarName = (TextView) view.findViewById(R.id.THCarName);
            THPaymentMode = (TextView) view.findViewById(R.id.THPaymentMode);
            mapCardTripHistoryImg = (ImageView) view.findViewById(R.id.mapCardTripHistory);
            THStar1 = (ImageView) view.findViewById(R.id.THStar1);
            THStar2 = (ImageView) view.findViewById(R.id.THStar2);
            THStar3 = (ImageView) view.findViewById(R.id.THStar3);
            THStar4 = (ImageView) view.findViewById(R.id.THStar4);
            THCardView = (LinearLayout) view.findViewById(R.id.THCardView);
        }
    }
}
