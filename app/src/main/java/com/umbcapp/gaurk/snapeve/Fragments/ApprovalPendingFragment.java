package com.umbcapp.gaurk.snapeve.Fragments;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.umbcapp.gaurk.snapeve.Adapters.CreateGroupAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.CreateGroupListItem;
import com.umbcapp.gaurk.snapeve.Listview_communicator;
import com.umbcapp.gaurk.snapeve.R;

import java.util.ArrayList;

public class ApprovalPendingFragment extends Fragment {

    private Parcelable state;
    private ListView appr_pending_frag_listview;

    public ApprovalPendingFragment() {

    }

    private CreateGroupAdapter createGroupAdapter;

    ArrayList<CreateGroupListItem> groupList = new ArrayList<CreateGroupListItem>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.appr_pending_fragment, container, false);

        appr_pending_frag_listview = (ListView) rootView.findViewById(R.id.appr_pending_frag_listview);

        groupList.clear();
        groupList.add(new CreateGroupListItem("Pranav Rana", "GU1", 1, "user_1@gmail.com", "https://www.bnl.gov/today/body_pics/2017/06/stephanhruszkewycz-355px.jpg"));
        groupList.add(new CreateGroupListItem("Rushabh mehta", "GU1", 1, "user_1@gmail.com", "https://cdn.earthdata.nasa.gov/conduit/upload/6072/Glenn_headshot_resize.jpg"));

        createGroupAdapter = new CreateGroupAdapter(getActivity(), groupList);
        appr_pending_frag_listview.setAdapter(createGroupAdapter);

        appr_pending_frag_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("position :" + position);
                openCancelRequestDialog(position);
            }
        });

        return rootView;
    }

    private void openCancelRequestDialog(int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setMessage("Please confirm approval");
        alertDialogBuilder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("CONFIRM");

            }
        });
        alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("CANCEL");
            }
        });
        alertDialogBuilder.show();
    }


    private void modifyRequestStatus(int position) {

        CreateGroupListItem selectedCreateGroupListItem = groupList.get(position);

        int tempStatus = selectedCreateGroupListItem.getUserReqStatus();
        Log.d("tempStatus before", tempStatus + "");

        if (tempStatus == 0) {
            selectedCreateGroupListItem.setUserReqStatus(1);
        }
        if (tempStatus == 1) {
            selectedCreateGroupListItem.setUserReqStatus(0);
        }
        groupList.set(position, selectedCreateGroupListItem);

        //Update listview and set selection
        createGroupAdapter.notifyDataSetChanged();
        appr_pending_frag_listview.onRestoreInstanceState(state);

    }
}
