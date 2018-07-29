package com.umbcapp.gaurk.snapeve;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Amey on 29-07-2018.
 */

public class SnapeveInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "SparshInstanceIdService";

    @Override
    public void onTokenRefresh() {
        Log.d(TAG, "Refreshing GCM Registration Token");

        Intent intent = new Intent(this, RegisterNotificationTags.class);
        startService(intent);
    }
}
