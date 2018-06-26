package com.umbcapp.gaurk.snapeve;

import android.content.Context;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.net.MalformedURLException;

/**
 * Created by Amey on 26-06-2018.
 */

public class Singleton {

    private static Singleton instance;
    private MobileServiceClient mClient;

    //no outer class can initialize this class's object
    private Singleton() {
    }

    public static Singleton Instance() {
        //if no instance is initialized yet then create new instance
        //else return stored instance
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public MobileServiceClient mClientMethod(Context context) {
        try {
            mClient = new MobileServiceClient("https://snapeve.azurewebsites.net", context);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return  mClient;
    }

}
