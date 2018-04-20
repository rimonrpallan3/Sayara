package com.voyager.sayara.TermsAndConduction;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.voyager.sayara.R;
import com.voyager.sayara.TermsAndConduction.adapters.ConductionListsRecycleAdapter;
import com.voyager.sayara.TermsAndConduction.model.ConductionModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by rimon on 1/9/17.
 */

public class TermsAndConduction extends AppCompatActivity {

    private List<ConductionModel> conductionList = new ArrayList<>();
    private RecyclerView conductionListRecycleView;
    private ConductionListsRecycleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_conduction);

        conductionListRecycleView = (RecyclerView) findViewById(R.id.conductionListRecycleView);

        mAdapter = new ConductionListsRecycleAdapter(conductionList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        conductionListRecycleView.setLayoutManager(mLayoutManager);
        conductionListRecycleView.setItemAnimator(new DefaultItemAnimator());
        conductionListRecycleView.setAdapter(mAdapter);

        setConductionList();

    }
    public void setConductionList(){
        ConductionModel conductionModel = new ConductionModel("Terms and Conditions are a set of rules and guidelines that a user must agree to in order to use your website or mobile app. It acts as a legal contract between you (the company) who has the website or mobile app and the user who access your website and mobile app.");
        conductionList.add(conductionModel);

        conductionModel = new ConductionModel("It’s up to you to set the rules and guidelines that the user must agree to. You can think of your Terms and Conditions agreement as the legal agreement where you maintain your rights to exclude users from your app in the event that they abuse your app, and where you maintain your legal rights against potential app abusers, and so on.");
        conductionList.add(conductionModel);

        conductionModel = new ConductionModel("Terms and Conditions are also known as Terms of Service or Terms of Use.");
        conductionList.add(conductionModel);

        conductionModel = new ConductionModel("The Intellectual Property disclosure will inform users that the contents, logo and other visual media you created is your property and is protected by copyright laws.");
        conductionList.add(conductionModel);

        conductionModel = new ConductionModel("A Termination clause will inform that users’ accounts on your website and mobile app or users’ access to your website and mobile (if users can’t have an account with you) can be terminated in case of abuses or at your sole discretion.");
        conductionList.add(conductionModel);

        conductionModel = new ConductionModel("A Governing Law will inform users which laws govern the agreement. This should the country in which your company is headquartered or the country from which you operate your website and mobile app.");
        conductionList.add(conductionModel);

        conductionModel = new ConductionModel("A Links To Other Web Sites clause will inform users that you are not responsible for any third party websites that you link to. This kind of clause will generally inform users that they are responsible for reading and agreeing (or disagreeing) with the Terms and Conditions or Privacy Policies of these third parties.");
        conductionList.add(conductionModel);

        conductionModel = new ConductionModel("If your website or mobile app allows users to create content and make that content public to other users, a Content section will inform users that they own the rights to the content they have created.\n" +
                "The “Content” clause usually mentions that users must give you (the website or mobile app developer) a license so that you can share this content on your website/mobile app and to make it available to other users.");
        conductionList.add(conductionModel);

        conductionModel = new ConductionModel("A Limit What Users Can Do clause can inform users that by agreeing to use your service, they’re also agreeing to not do certain things. This can be part of a very long and thorough list in your Terms and Conditions agreements so as to encompass the most amount of negative uses.\n" +
                "Here’s how 500px lists its prohibited activities:");
        conductionList.add(conductionModel);

        conductionModel = new ConductionModel("All copyright, trade marks, design rights, patents and other intellectual property rights (registered and unregistered) in and on BBC Online Services and BBC Content belong to the BBC and/or third parties (which may include you or other users.) The BBC reserves all of its rights in BBC Content and BBC Online Services. Nothing in the Terms grants you a right or license to use any trade mark, design right or copyright owned or controlled by the BBC or any other third party except as expressly provided in the Terms.");
        conductionList.add(conductionModel);

        conductionModel = new ConductionModel("No matter what kind of goods you sell, best practices direct you to present any warranties you are disclaiming and liabilities you are limiting in a way that your customers will notice.");
        conductionList.add(conductionModel);

        conductionModel = new ConductionModel("You’ve probably noticed that these clauses in contracts are always in blocks of all-caps text and really do stand out from the rest of the document.");
        conductionList.add(conductionModel);

        conductionModel = new ConductionModel("Back to the Future");
        conductionList.add(conductionModel);

        conductionModel = new ConductionModel("Raiders of the Lost Ark");
        conductionList.add(conductionModel);

        conductionModel = new ConductionModel("Goldfinger");
        conductionList.add(conductionModel);

        conductionModel = new ConductionModel("Guardians of the Galaxy");
        conductionList.add(conductionModel);

        mAdapter.notifyDataSetChanged();
    }
}
