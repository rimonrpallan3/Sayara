package com.voyager.sayara.landingpage.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.voyager.sayara.R;
import com.voyager.sayara.costom.CircleImageView;
import com.voyager.sayara.landingpage.model.drawerHeader.HeaderItem;
import com.voyager.sayara.landingpage.model.drawerList.DrawerItems;
import com.voyager.sayara.registerpage.model.UserDetails;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 15-May-18.
 */

public class DrawerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<DrawerItems> drawerItemsList =new ArrayList<>();
    private Context context;
    private ClickListener clickListener;
    private LayoutInflater infalter;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FOOTER = 1;




    public DrawerListAdapter(List<DrawerItems> drawerItemsList, Context context) {
        this.drawerItemsList = drawerItemsList;
        this.infalter = LayoutInflater.from(context);
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println(" ------------ DrawerListAdapter onCreateViewHolder viewType : "+viewType);
        if(viewType == 1){
            View rootView = infalter.inflate(R.layout.drawer_list_menu_card,parent,false);
            System.out.println(" ------------ DrawerListAdapter drawer_list_menu_card");
            return new DrawerListViewHolder(rootView);
        }else{
            View rootView = infalter.inflate(R.layout.drawer_header,parent,false);
            System.out.println(" ------------ DrawerListAdapter drawer_header");
            return new mHeaderViewHolder(rootView);
        }
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holderViews, final int position) {
        if(holderViews instanceof DrawerListViewHolder) {
            DrawerListViewHolder holder = (DrawerListViewHolder) holderViews;
            System.out.println(" ------------ DrawerListAdapter onBindViewHolder drawer_list_menu_card position : "+position);
            if (drawerItemsList.get(position).getIconDraw() != null) {
                Drawable yourDrawable = MaterialDrawableBuilder.with(context) // provide a context
                        .setIcon(drawerItemsList.get(position).getIconDraw()) // provide an icon
                        .setColor(context.getResources().getColor(R.color.iconColor)) // set the icon color
                        .setSizeDp(24) // set the icon size
                        .build();
                holder.ivDrawerItem.setImageDrawable(yourDrawable);
            }
            holder.tvDrawerItem.setText(drawerItemsList.get(position).getName());
        } else if (holderViews instanceof mHeaderViewHolder){
            System.out.println(" ------------ DrawerListAdapter onBindViewHolder drawer_header position : "+position);
            final mHeaderViewHolder holder = (mHeaderViewHolder) holderViews;
            final HeaderItem dataItem = (HeaderItem) drawerItemsList.get(position);
            System.out.println("DrawerListViewHolder user name : "+dataItem.getUserName());
            System.out.println("DrawerListViewHolder Image Url : "+dataItem.getImageUrl());
            holder.userName.setText(dataItem.getUserName());
               try {
                Picasso.with(context)
                        .load(dataItem.getImageUrl())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .resize(0, 200)
                        .into(holder.userCircleImg, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                //Try again online if cache failed
                                Picasso.with(context)
                                        .load(dataItem.getImageUrl())
                                        .error(R.drawable.profile)
                                        .resize(0, 200)
                                        .into(holder.userCircleImg, new Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError() {
                                                Log.v("Picasso", "Could not fetch image");
                                            }
                                        });
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    @Override
    public int getItemCount() {
        if (drawerItemsList != null && drawerItemsList.size() > 0) {
            return drawerItemsList.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        System.out.println(" ------------ DrawerListAdapter getItemViewType position : "+position);
        if(position == 0 && drawerItemsList.get(position) instanceof HeaderItem){
            System.out.println(" ------------ DrawerListAdapter onBindViewHolder getItemViewType position : "+position);
            return TYPE_HEADER;
        }
        return TYPE_FOOTER;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * ViewHolder class which holds Initialisation to all views and buttons.
     */

    public class DrawerListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvDrawerItem;
        ImageView ivDrawerItem;
        View root;

        public DrawerListViewHolder(View itemView) {
            super(itemView);
            root = itemView;
            itemView.setOnClickListener(this);
            ivDrawerItem = (ImageView) itemView.findViewById(R.id.ivDrawerItem);
            tvDrawerItem = (TextView) itemView.findViewById(R.id.tvDrawerItem);
        }

        @Override
        public void onClick(View v) {

            if(clickListener!=null){
                clickListener.itemClicked(v,getPosition());
            }

            //delete(getPosition());

        }
    }

    public  class mHeaderViewHolder extends RecyclerView.ViewHolder{


        TextView userName;
        CircleImageView userCircleImg;

        public mHeaderViewHolder(View itemView) {
            super(itemView);
            userCircleImg = (CircleImageView) itemView.findViewById(R.id.customerProfileDrawerImg);
            userName = (TextView) itemView.findViewById(R.id.customerProfileDrawerTitle);

        }
    }

    public void setClickListener(ClickListener clicklistener){

        this.clickListener = clicklistener;

    }


    public interface ClickListener{
        public void itemClicked(View view, int position);
    }

    public List<DrawerItems> getData(){

        return this.drawerItemsList;
    }

}
