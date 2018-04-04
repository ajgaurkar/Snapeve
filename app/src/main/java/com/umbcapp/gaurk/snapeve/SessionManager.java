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
    public static final String KEY_USER_NAME = "username";
    public static final String KEY_PASSWORD = "password";


    public static final String KEY_CLIENT_ID = "clientid";
    public static final String KEY_CLIENT_SECRET = "clientsecret";
    public static final String KEY_ACCESS_TOKEN = "accesstoken";


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


    public Boolean createLoginSession(String name, String password) {
        Log.d("INTO shapref CREAT SESS", "INTO shapref CREAT SESS");
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USER_NAME, name);
        System.out.println("KEY_PASSWORD : " + name);
        editor.putString(KEY_PASSWORD, password);
        System.out.println("KEY_PASSWORD : " + password);

        // commit changes
        editor.commit();

        return true;
    }

    public Boolean createSignInSession(String client_id, String client_secret, String access_tkn) {
        Log.d("INTO shapref CREAT SESS", "INTO shapref CREAT SESS");
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_CLIENT_ID, client_id);
        System.out.println("client_id : " + client_id);
        editor.putString(KEY_CLIENT_SECRET, client_secret);
        System.out.println("client_secret : " + client_secret);
        editor.putString(KEY_ACCESS_TOKEN, access_tkn);
        System.out.println("access_tkn : " + access_tkn);

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
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        } else {
            Log.d("data present", "data present");

        }
    }

    /**
     * Get stored session data
     */


    public String getSpecificUserDetail(String dataKEY) {

        String itemValue = pref.getString(dataKEY, null);

        return itemValue;
    }

    public void setSpecificUserDetail(String dataKEY, String dataValue) {
        editor.putString(dataKEY, dataValue);
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
        Intent i = new Intent(_context, MainActivity.class);
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