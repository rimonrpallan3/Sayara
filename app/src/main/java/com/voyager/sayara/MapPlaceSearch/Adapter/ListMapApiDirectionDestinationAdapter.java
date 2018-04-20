package com.voyager.sayara.MapPlaceSearch.Adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.voyager.sayara.MapPlaceSearch.model.QuizObject;
import com.voyager.sayara.MapPlaceSearch.model.placesearch.Predictions;
import com.voyager.sayara.MapPlaceSearch.model.placesearch.StructuredFormatting;
import com.voyager.sayara.MapPlaceSearch.view.IMapPlaceSearchView;
import com.voyager.sayara.R;

import java.util.List;

/*
*
 * Created by rimon on 08-02-2018.
*/


// create a custom RecycleViewAdapter class

public class ListMapApiDirectionDestinationAdapter extends RecyclerView.Adapter<ListMapApiDirectionDestinationAdapter.ViewHolder>  {

    public List<Predictions> mValues;
    IMapPlaceSearchView iMapPlaceSearchView;

    public ListMapApiDirectionDestinationAdapter(List<Predictions> items,IMapPlaceSearchView iMapPlaceSearchView) {
        this.iMapPlaceSearchView = iMapPlaceSearchView;
        mValues = items;
        System.out.println("MapPlaceSearch has ben ListMapApiDirectionSourceAdapter ");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        System.out.println("MapPlaceSearch has ben ListMapApiDirectionSourceAdapter  onBindViewHolder");
        final Predictions predictions1 = mValues.get(position);
        StructuredFormatting structuredFormatting = (StructuredFormatting) predictions1.structuredFormatting;
        System.out.println("-------MapPlaceSearchPresenter onBindViewHolder MainText : " + structuredFormatting.getMainText() +
                " SecondaryText : " + structuredFormatting.getSecondaryText());
        holder.mainText.setText(structuredFormatting.getMainText());
        holder.secondaryText.setText(structuredFormatting.getSecondaryText());
        holder.linearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iMapPlaceSearchView.destinationText(holder.mainText.getText().toString(),predictions1.getPlaceId());
            }
        });



    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mainText;
        public final TextView secondaryText;
        public final LinearLayout linearView;
        public QuizObject mItem;

        public ViewHolder(View view) {
            super(view);
            mainText = (TextView) view.findViewById(R.id.mainText);
            secondaryText = (TextView) view.findViewById(R.id.secondaryText);
            linearView = (LinearLayout) view.findViewById(R.id.linearView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + secondaryText.getText() + "'";
        }
    }

}
