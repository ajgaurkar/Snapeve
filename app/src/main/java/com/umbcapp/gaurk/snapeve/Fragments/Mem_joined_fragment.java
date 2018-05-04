package com.umbcapp.gaurk.snapeve.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umbcapp.gaurk.snapeve.R;

public class Mem_joined_fragment extends Fragment {

    public Mem_joined_fragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mem_joined_fragment, container, false);

        return rootView;
    }


}
