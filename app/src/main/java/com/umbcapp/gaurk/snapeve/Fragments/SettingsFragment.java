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
import com.umbcapp.gaurk.snapeve.AccountHandler;
import com.umbcapp.gaurk.snapeve.R;
import com.umbcapp.gaurk.snapeve.ResetPassword;
import com.umbcapp.gaurk.snapeve.ScheduledRewards;
import com.umbcapp.gaurk.snapeve.SessionManager;
import com.umbcapp.gaurk.snapeve.SnapeveFeedback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class SettingsFragment extends PreferenceFragment {


    private JsonObject jsonObjectUserProfileFragParameters;
    private UserProfileFragment userProfileFragment;
    private long sessionCounter=0;

    public SettingsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionCounter = System.currentTimeMillis();
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String text = formatter.format(new Date(sessionCounter));

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

        Uri uri;
        switch (preference.getKey()) {
            case "userAccountPreferenceKey":
//                startActivity(new Intent(getActivity(), AccountHandler.class));
                startActivity(new Intent(getActivity(), ResetPassword.class));
                break;

            case "snapeveFeedbackPreferenceKey":
                startActivity(new Intent(getActivity(), SnapeveFeedback.class));
                break;

            case "notificationPreferenceKey":
                break;

            case "rateusPreferenceKey":
                uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(goToMarket);
                break;

            case "appInfoPreferenceKey":
                startActivity(new Intent(getActivity(), ScheduledRewards.class));
                break;

            case "termsAndPolicyInfoPreferenceKey":
                break;

            case "fAQPreferenceKey":
                break;

//            case "inviteFriendsPreferenceKey":
//                uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
//                Intent nav_share_Intent = new Intent(android.content.Intent.ACTION_SEND);
//                nav_share_Intent.setType("text/plain");
//                nav_share_Intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Snapeve");
//                nav_share_Intent.putExtra(android.content.Intent.EXTRA_TEXT, "You should try SnapEve, It's an awesome app for campus events. Get 10 bonus points when you sign up using my referral code '"+new SessionManager(getActivity()).getSpecificUserDetail(SessionManager.KEY_USER_NAME)+"' https://play.google.com/store/apps/developer?id=MAAK+Services");
//                startActivity(Intent.createChooser(nav_share_Intent, "Share via :"));
//                break;

            case "logoutPreferenceKey":
                new SessionManager(getActivity()).logoutUser();
                break;

        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
    @Override
    public void onResume() {
        super.onResume();

        sessionCounter = System.currentTimeMillis();
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String text = formatter.format(new Date(sessionCounter));
//        System.out.println("Start @ sessionCounter : " + sessionCounter);
    }

    @Override
    public void onPause() {
        super.onPause();

        sessionCounter = System.currentTimeMillis() - sessionCounter;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(sessionCounter);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(sessionCounter);

        System.out.println("SETTINGS onPause sessionCounter : " + minutes + "m " + seconds + "s");

    }

}
