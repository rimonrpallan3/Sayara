package com.voyager.sayara.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.voyager.sayara.R;
import com.voyager.sayara.costom.CircleImageView;
import com.voyager.sayara.landingpage.LandingPage;
import com.voyager.sayara.registerpage.model.UserDetails;
import com.voyager.sayara.updateprofile.UpdateProfile;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 28-May-18.
 */

public class Settings extends AppCompatActivity implements View.OnClickListener  {



    @BindView(R.id.llProfile)
    LinearLayout llProfile;
    @BindView(R.id.llSignOut)
    LinearLayout llSignOut;
    @BindView(R.id.ivSignOut)
    ImageView ivSignOut;
    @BindView(R.id.tvSignOut)
    TextView tvSignOut;
    @BindView(R.id.civSettingImg)
    CircleImageView civSettingImg;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tbSettings)
    Toolbar tbSettings;

    UserDetails userDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_page);
        ButterKnife.bind(this);

        setSupportActionBar(tbSettings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.action_settings));
        tbSettings.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
        Intent intent = getIntent();
        userDetails = (UserDetails) intent.getParcelableExtra("UserDetails");
        if (userDetails != null) {
            Gson gson = new Gson();
            String json = gson.toJson(userDetails);
            System.out.println("UpdateProfile -- UserDetails - name : " + json);
        }
        tvUserName.setText(userDetails.getFName());


        try {
            Picasso.with(this)
                    .load(userDetails.getImgPath())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .resize(0, 200)
                    .into(civSettingImg, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            //Try again online if cache failed
                            Picasso.with(getParent())
                                    .load(userDetails.getImgPath())
                                    .error(R.drawable.profile)
                                    .resize(0, 200)
                                    .into(civSettingImg, new Callback() {
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
        llProfile.setOnClickListener(this);
        llSignOut.setOnClickListener(this);
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

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llProfile:
                Intent intent = new Intent(Settings.this, UpdateProfile.class);
                intent.putExtra("UserDetails", userDetails);
                startActivity(intent);
                break;
            case R.id.llSignOut:
                Toast.makeText(this,"Plase wait for this feather",Toast.LENGTH_LONG).show();
                break;

        }
    }
}