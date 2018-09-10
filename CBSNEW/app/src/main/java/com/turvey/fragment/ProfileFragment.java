package com.turvey.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.turvey.activity.KeepLogin;
import com.turvey.activity.KeepMain;
import com.turvey.cbs.R;

public class ProfileFragment extends Fragment {
    CircularImageView image;
    Button logout;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsing_toolbar;
    FloatingActionButton editcover;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        logout = (Button) view.findViewById(R.id.logoutprofile);
        editcover = (FloatingActionButton) view.findViewById(R.id.fabcoveredit);
        image = (CircularImageView) view.findViewById(R.id.image);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.app_bar_layout);
        collapsing_toolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);

        initComponent();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogout();
            }
        });


        return view;
    }


    private void initComponent() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int min_height = ViewCompat.getMinimumHeight(collapsing_toolbar) * 2;
                float scale = (float) (min_height + verticalOffset) / min_height;
                image.setScaleX(scale >= 0 ? scale : 0);
                image.setScaleY(scale >= 0 ? scale : 0);
                editcover.setScaleX(scale >= 0 ? scale : 0);
                editcover.setScaleY(scale >= 0 ? scale : 0);
            }
        });
    }

    public void doLogout() {
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
        startActivity(new Intent(getContext(), KeepLogin.class));
        KeepMain.getInstance().finish();
    }
}
