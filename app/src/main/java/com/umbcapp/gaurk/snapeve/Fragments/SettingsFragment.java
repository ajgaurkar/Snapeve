package com.umbcapp.gaurk.snapeve.Fragments;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.umbcapp.gaurk.snapeve.AccountHandler;
import com.umbcapp.gaurk.snapeve.Adapters.SnapeveNotificationAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.SnapEveSession;
import com.umbcapp.gaurk.snapeve.Controllers.SnapeveNotification;
import com.umbcapp.gaurk.snapeve.DatabaseRepository.SnapeveDatabaseRepository;
import com.umbcapp.gaurk.snapeve.MainActivity;
import com.umbcapp.gaurk.snapeve.R;
import com.umbcapp.gaurk.snapeve.ResetPassword;
import com.umbcapp.gaurk.snapeve.ScheduledRewards;
import com.umbcapp.gaurk.snapeve.SessionManager;
import com.umbcapp.gaurk.snapeve.SnapeveFeedback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class SettingsFragment extends PreferenceFragment {


    private JsonObject jsonObjectUserProfileFragParameters;
    private UserProfileFragment userProfileFragment;
    private long sessionCounter = 0;
    private SnapeveDatabaseRepository snapeveDatabaseRepository;
    private JsonArray jsonArrayForSession;
    private JsonObject sessionCounterParameters;

    Context context;

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
                Intent resetIntent = new Intent(getActivity(), ResetPassword.class);
                resetIntent.putExtra("intentType", 1);
                startActivity(resetIntent);
                break;

            case "snapeveFeedbackPreferenceKey":
                startActivity(new Intent(getActivity(), SnapeveFeedback.class));
                break;

            case "takeSurveyPreferenceKey":
                startActivity(new Intent(getActivity(), AccountHandler.class));
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
                //post session data to AZURE


//                snapeveDatabaseRepository.getSessiondata().observe((LifecycleOwner) getActivity(), new Observer<List<SnapEveSession>>() {
//                    @Override
//                    public void onChanged(@Nullable List<SnapEveSession> snapEveSessions) {
//                        System.out.println("snapEveSessions sess------------  " + snapEveSessions.size());
//                        uploadSessions(snapEveSessions);
//                    }
//                });

                showLogoutDialog();
                break;

        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private void showLogoutDialog() {
        snapeveDatabaseRepository = new SnapeveDatabaseRepository(getActivity());





        snapeveDatabaseRepository.getSessiondata().observe((LifecycleOwner) getActivity(), new Observer<List<SnapEveSession>>() {
            @Override
            public void onChanged(@Nullable List<SnapEveSession> snapEveSessions) {
                System.out.println("snapEveSessions---   " + snapEveSessions);
                System.out.println("snapEveSessions---   " + snapEveSessions.size());
            }
        });


//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Confirm Logout?").setMessage("This will delete all your data from the device")
//                .setCancelable(false)
//                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                       // new SessionManager(getActivity()).logoutUser();
//                    }
//                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//            }
//        });
//        AlertDialog alert = builder.create();
//        alert.show();
    }

    private void uploadSessions(List<SnapEveSession> snapEveSessions) {

        if (snapEveSessions.size() > 0) {

            jsonArrayForSession = new JsonArray();

            for (int i = 0; i < snapEveSessions.size(); i++) {

                System.out.println("snapEveSessions.get(i).getActivityCode() " + snapEveSessions.get(i).getActivityCode());
                sessionCounterParameters = new JsonObject();
                sessionCounterParameters.addProperty("user_id", new SessionManager(getActivity()).getSpecificUserDetail(SessionManager.KEY_USER_ID));
                sessionCounterParameters.addProperty("activity_code", snapEveSessions.get(i).getActivityCode());
                sessionCounterParameters.addProperty("start_time", snapEveSessions.get(i).getStartTime());
                sessionCounterParameters.addProperty("end_time", snapEveSessions.get(i).getEndTime());
                sessionCounterParameters.addProperty("duration", snapEveSessions.get(i).getDuration());
                sessionCounterParameters.addProperty("user_name", new SessionManager(getActivity()).getSpecificUserDetail(SessionManager.KEY_USER_NAME));

                jsonArrayForSession.add(sessionCounterParameters);
            }

            System.out.println("jsonArrayForSession : " + jsonArrayForSession);

            final long timestampForSessionDeletion = System.currentTimeMillis();
            final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
            ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("session_counting_api", jsonArrayForSession);

            Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
                @Override
                public void onFailure(Throwable exception) {
                    resultFuture.setException(exception);
                    System.out.println(" session_counting_api exception    " + exception);
                }

                @Override
                public void onSuccess(JsonElement response) {
                    resultFuture.set(response);
                    System.out.println(" session_counting_api response    " + response);

                    if (response.toString().contains("true")) {
                        //delete local session data
                        System.out.println("delete local session data");
                        snapeveDatabaseRepository.deleteUploadedSession(timestampForSessionDeletion);
                    }

                    System.out.println(" session_counting_api success response    " + response);
                }
            });
        }
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

        long startTime = sessionCounter;
        long endTime = System.currentTimeMillis();
        long duration = System.currentTimeMillis() - sessionCounter;

        sessionCounter = System.currentTimeMillis() - sessionCounter;

        snapeveDatabaseRepository = new SnapeveDatabaseRepository(getActivity());

        snapeveDatabaseRepository.insertSnapeveSession("STS", startTime, endTime, duration, 0);
    }

}
