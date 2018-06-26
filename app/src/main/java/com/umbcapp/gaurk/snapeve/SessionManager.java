package com.umbcapp.gaurk.snapeve;


/**
 * Created by gaurk on 8/2/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by ajinkya on 10/20/2015.
 */
public class SessionManager {

    // Sharedpref file name
    private static final String PREF_NAME = "appPrefrences";

    // All Shared Preferences Keys
    // keys should be strings
    //data should be specific data type
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_USER_ID = "userid";
    public static final String KEY_USER_NAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_FIRST_NAME = "firstname";
    public static final String KEY_LAST_NAME = "lastname";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_DP_URL = "user_dp_url";
    public static final String KEY_GRP_DP_URL = "grp_dp_url";
    public static final String KEY_REQ_PENDING_GRP_STATUS = "req_pend_grp_status";
    public static final String KEY_REQ_PENDING_GRP_ID = "req_pend_grp_id";
    public static final String KEY_REQ_PENDING_GRP_NAME = "req_pend_grp_name";

    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public Boolean createLoginSession(String user_id, String user_name, String user_pass, String first_name, String last_name, String email) {
        Log.d("INTO shapref CREAT SESS", "INTO shapref CREAT SESS");
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USER_NAME, user_name);
        System.out.println("user_name : " + user_name);
        editor.putString(KEY_PASSWORD, user_pass);
        System.out.println("KEY_PASSWORD : " + user_pass);

        editor.putString(KEY_USER_ID, user_id);
        System.out.println("user_id : " + user_id);

        editor.putString(KEY_EMAIL, email);
        System.out.println("email : " + email);

        editor.putString(KEY_FIRST_NAME, first_name);
        System.out.println("first_name : " + first_name);

        editor.putString(KEY_LAST_NAME, last_name);
        System.out.println("last_name : " + last_name);

        // commit changes
        editor.commit();

        return true;
    }


    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            Log.d("data null", "data null");

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, Login_snapeve_activity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Starting Login Activity
            _context.startActivity(i);
        } else {
            Log.d("data present", "data present");

        }
    }

    /**
     * Get stored session data
     */


    public String getSpecificUserDetail(String dataKEY) {

        String itemValue = pref.getString(dataKEY, "T_cruise");

        return itemValue;
    }

    public void setSpecificUserDetail(String dataKEY, String dataValue) {
        editor.putString(dataKEY, dataValue);
        editor.commit();
    }

    public Boolean getSpecificUserBooleanDetail(String dataKEY) {

        return pref.getBoolean(dataKEY, false);
    }

    public void setSpecificUserBooleanDetail(String dataKEY, Boolean dataValue) {
        editor.putBoolean(dataKEY, dataValue);
        editor.commit();
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, Login_snapeve_activity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}