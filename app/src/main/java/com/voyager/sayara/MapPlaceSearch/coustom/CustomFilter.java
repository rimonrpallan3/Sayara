package com.voyager.sayara.MapPlaceSearch.coustom;

import android.widget.Filter;

import com.voyager.sayara.MapPlaceSearch.Adapter.ListMapApiDirectionSourceAdapter;
import com.voyager.sayara.MapPlaceSearch.model.placesearch.Predictions;
import com.voyager.sayara.MapPlaceSearch.model.placesearch.StructuredFormatting;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by rimon on 08-02-2018.
 */

public class CustomFilter extends Filter {
    private ListMapApiDirectionSourceAdapter mAdapter;
    private List<Predictions> filteredList;
    private List<Predictions> predictionses;
    public CustomFilter(ListMapApiDirectionSourceAdapter mAdapter, List<Predictions> predictionses) {
        super();
        this.mAdapter = mAdapter;
        this.predictionses = predictionses;
        filteredList = new ArrayList<>();
    }
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredList.clear();
        final FilterResults results = new FilterResults();

        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toLowerCase();
            //STORE OUR FILTERED PLAYERS
            List<Predictions> filteredPlayers=new ArrayList<>();

            for (int i=0;i<predictionses.size();i++) {
                Predictions predictions1 = predictionses.get(i);
                StructuredFormatting structuredFormatting = (StructuredFormatting) predictions1.structuredFormatting;
                System.out.println("-------MapPlaceSearchPresenter getTripDirection MainText : " + structuredFormatting.getMainText() +
                        " SecondaryText : " + structuredFormatting.getSecondaryText());
                filteredPlayers.add(predictionses.get(i));//CHECK
                /*if(structuredFormatting.getMainText().toLowerCase().contains(constraint))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredPlayers.add(predictionses.get(i));
                }*/
            }

            results.count=filteredPlayers.size();
            results.values=filteredPlayers;
        }else
        {
            results.count=predictionses.size();
            results.values=predictionses;

        }
      /*  if (constraint.length() == 0) {
            filteredList.addAll(predictionses);
        } else {
            final String filterPattern = constraint.toString().toLowerCase().trim();
            for (final Predictions mWords : predictionses) {
                if (mWords.structuredFormatting.getMainText().toLowerCase().startsWith(filterPattern)) {
                    filteredList.add(mWords);
                }
            }
        }
        System.out.println("Count Number " + filteredList.size());
        results.values = filteredList;
        results.count = filteredList.size();*/
        return results;
    }
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        System.out.println("Count Number 2 " + ((List<Predictions>) results.values).size());
        mAdapter.mValues = (ArrayList<Predictions>) results.values;
        mAdapter.notifyDataSetChanged();
    }
}
