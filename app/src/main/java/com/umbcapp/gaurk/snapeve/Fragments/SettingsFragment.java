package com.umbcapp.gaurk.snapeve.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.umbcapp.gaurk.snapeve.Login_snapeve_activity;
import com.umbcapp.gaurk.snapeve.R;
import com.umbcapp.gaurk.snapeve.ScheduledRewards;
import com.umbcapp.gaurk.snapeve.SessionManager;
import com.umbcapp.gaurk.snapeve.SnapeveFeedback;

public class SettingsFragment extends PreferenceFragment {


    private JsonObject jsonObjectUserProfileFragParameters;
    private UserProfileFragment userProfileFragment;

    public SettingsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.snapeve_setting);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //   View rootView = inflater.inflate(R.layout.settings_fragment, container, false);
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        rootView.setBackgroundColor(getResources().getColor(android.R.color.white));

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        switch (preference.getKey()) {
            case "snapeveFeedbackPreferenceKey":
                startActivity(new Intent(getActivity(), SnapeveFeedback.class));
                break;

            case "notificationPreferenceKey":
                break;

            case "helpPreferenceKey":
                startActivity(new Intent(getActivity(), ScheduledRewards.class));

                break;

            case "rateusPreferenceKey":
                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(goToMarket);
                break;

            case "appInfoPreferenceKey":
                break;

            case "termsAndPolicyInfoPreferenceKey":
                break;

            case "fAQPreferenceKey":
                break;

            case "inviteFriendsPreferenceKey":
                Intent nav_share_Intent = new Intent(android.content.Intent.ACTION_SEND);
                nav_share_Intent.setType("text/plain");
                nav_share_Intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Snapeve");
                nav_share_Intent.putExtra(android.content.Intent.EXTRA_TEXT, "Random Text About your app");
                nav_share_Intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"umbcsnapeve@gmail.com"});
                startActivity(Intent.createChooser(nav_share_Intent, "Share via :"));
                break;

            case "logoutPreferenceKey":
                new SessionManager(getActivity()).logoutUser();
//                startActivity(new Intent(getActivity(), Login_snapeve_activity.class));
                break;

        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
