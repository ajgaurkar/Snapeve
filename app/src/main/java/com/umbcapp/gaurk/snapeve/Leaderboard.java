package com.umbcapp.gaurk.snapeve;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.umbcapp.gaurk.snapeve.R;

public class Leaderboard extends AppCompatActivity {

    int user_type = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard_activity);

        user_type = getIntent().getExtras().getInt("Profile_type");

        if (user_type == 0) {
            System.out.print("USER");
            Log.d("USER", "USER");

        }
        if (user_type == 1) {
            System.out.print("GROUP");
            Log.d("GROUP", "GROUP");

        }
        Log.d("user_type : ", user_type+"");


    }
}
