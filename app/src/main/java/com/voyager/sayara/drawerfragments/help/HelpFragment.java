package com.voyager.sayara.drawerfragments.help;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.voyager.sayara.R;
import com.voyager.sayara.landingpage.helper.BackHandledFragment;

/**
 * Created by User on 28-Sep-17.
 */

public class HelpFragment extends BackHandledFragment implements View.OnClickListener{



    public HelpFragment() {
    }


    @Override
    public String getTagText() {
        return null;
    }

    @Override
    public boolean onBackPressed() {
        getActivity().getFragmentManager().popBackStack();
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.help_fragment, container, false);
        System.out.println("RatingTabFragment");
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.map:
                break;
        }
    }
}
