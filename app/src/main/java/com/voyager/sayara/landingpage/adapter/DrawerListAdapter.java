package com.voyager.sayara.landingpage.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.voyager.sayara.R;
import com.voyager.sayara.landingpage.model.drawerList.DrawerItems;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 15-May-18.
 */

public class DrawerListAdapter extends RecyclerView.Adapter<DrawerListAdapter.DrawerListViewHolder> {

    List<DrawerItems> drawerItemsList =new ArrayList<>();
    private Context context;


    public DrawerListAdapter(List<DrawerItems> drawerItemsList, Context context) {
        this.drawerItemsList = drawerItemsList;
        this.context = context;
    }

    @Override
    public DrawerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_list_menu_card, parent, false);
        DrawerListViewHolder pvh = new DrawerListViewHolder(v);
        return pvh;
    }


    @Override
    public void onBindViewHolder(final DrawerListViewHolder holder, final int position) {
        if (drawerItemsList.get(position).getIconDraw() != null) {
            Drawable yourDrawable = MaterialDrawableBuilder.with(context) // provide a context
                    .setIcon(drawerItemsList.get(position).getIconDraw()) // provide an icon
                    .setColor(context.getResources().getColor(R.color.iconColor)) // set the icon color
                    .setSizeDp(24) // set the icon size
                    .build();
            holder.ivDrawerItem.setImageDrawable(yourDrawable);
        }
        holder.tvDrawerItem.setText(drawerItemsList.get(position).getName());

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
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * ViewHolder class which holds Initialisation to all views and buttons.
     */

    public static class DrawerListViewHolder extends RecyclerView.ViewHolder {

        TextView tvDrawerItem;
        ImageView ivDrawerItem;

        DrawerListViewHolder(View itemView) {
            super(itemView);
            ivDrawerItem = (ImageView) itemView.findViewById(R.id.ivDrawerItem);
            tvDrawerItem = (TextView) itemView.findViewById(R.id.tvDrawerItem);
        }
    }
}
