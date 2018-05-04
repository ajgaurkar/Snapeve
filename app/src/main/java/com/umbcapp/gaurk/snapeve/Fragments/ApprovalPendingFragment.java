package com.umbcapp.gaurk.snapeve.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umbcapp.gaurk.snapeve.R;

public class ApprovalPendingFragment extends Fragment {

    public ApprovalPendingFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.appr_pending_fragment, container, false);

        return rootView;
    }


}
