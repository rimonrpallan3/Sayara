package com.voyager.sayara.TermsAndConduction.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.voyager.sayara.R;
import com.voyager.sayara.TermsAndConduction.model.ConductionModel;

import java.util.List;

/**
 * Created by User on 9/7/2017.
 */

public class ConductionListsRecycleAdapter extends RecyclerView.Adapter<ConductionListsRecycleAdapter.MyViewHolder> {

    private List<ConductionModel> conductionList;


    public ConductionListsRecycleAdapter(List<ConductionModel> conductionList) {
        this.conductionList = conductionList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.termsandcond_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ConductionModel movie = conductionList.get(position);
        holder.txtConduction.setText(movie.getTitle());
    }

    @Override
    public int getItemCount() {
        return conductionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtConduction;

        public MyViewHolder(View view) {
            super(view);
            txtConduction = (TextView) view.findViewById(R.id.txtConduction);
        }
    }
}
