package com.umbcapp.gaurk.snapeve.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.umbcapp.gaurk.snapeve.Login_snapeve_activity;
import com.umbcapp.gaurk.snapeve.MainActivity;
import com.umbcapp.gaurk.snapeve.R;
import com.umbcapp.gaurk.snapeve.ScheduledRewards;
import com.umbcapp.gaurk.snapeve.SessionManager;
import com.umbcapp.gaurk.snapeve.WelcomeActivity;

public class SettingsFragment extends Fragment {


    private JsonObject jsonObjectUserProfileFragParameters;

    public SettingsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_fragment, container, false);

        TextView logout_textview = (TextView) rootView.findViewById(R.id.settings_logout);
        TextView testlogin = (TextView) rootView.findViewById(R.id.testlogin);
        TextView test_y = (TextView) rootView.findViewById(R.id.test_y);
        TextView test_x = (TextView) rootView.findViewById(R.id.test_x);

        logout_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SessionManager(getActivity()).logoutUser();
            }
        });
        test_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WelcomeActivity.class));
            }
        });
        test_y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scheduledRewardsIntent = new Intent(getActivity(), ScheduledRewards.class);
                startActivity(scheduledRewardsIntent);
            }
        });

        testlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Login_snapeve_activity.class));

            }
        });
//        startActivity(new Intent(getActivity(), WelcomeActivity.class));

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}
