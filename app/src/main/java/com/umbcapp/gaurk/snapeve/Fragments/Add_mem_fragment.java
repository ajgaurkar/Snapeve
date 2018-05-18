package com.umbcapp.gaurk.snapeve.Fragments;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.umbcapp.gaurk.snapeve.Adapters.CreateGroupAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.AttendiesListItem;
import com.umbcapp.gaurk.snapeve.Controllers.CommentsListItem;
import com.umbcapp.gaurk.snapeve.Controllers.CreateGroupListItem;
import com.umbcapp.gaurk.snapeve.EventDetails;
import com.umbcapp.gaurk.snapeve.Leaderboard;
import com.umbcapp.gaurk.snapeve.Listview_communicator;
import com.umbcapp.gaurk.snapeve.MainActivity;
import com.umbcapp.gaurk.snapeve.R;

import java.util.ArrayList;

public class Add_mem_fragment extends Fragment {

    private ListView add_mem_frag_listview;
    private Parcelable state;
    private CreateGroupAdapter createGroupAdapter;
    private AutoCompleteTextView addmemberAutoCompleteTextView;

    public Add_mem_fragment() {

    }

    ArrayList<CreateGroupListItem> groupList = new ArrayList<CreateGroupListItem>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_mem_fragment, container, false);

        add_mem_frag_listview = (ListView) rootView.findViewById(R.id.add_mem_frag_listview);
        addmemberAutoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.addmemberAutoCompleteTextView);


        ArrayList<String> userArrayList = new ArrayList<String>();
        userArrayList.add("Ajinkya Gaurkar");
        userArrayList.add("Siddhrth Ptro");
        userArrayList.add("Pranav rana");
        userArrayList.add("Neha reddy");
        userArrayList.add("Rushabh mehta");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, userArrayList);
        addmemberAutoCompleteTextView.setAdapter(adapter);

        groupList.clear();
        groupList.add(new CreateGroupListItem("Ajinkya Gaurkar", "GU1", 3, "user_1@gmail.com", "http://keenthemes.com/preview/metronic/theme/assets/pages/media/profile/profile_user.jpg"));
        groupList.add(new CreateGroupListItem("Siddhrth Ptro", "GU1", 3, "user_1@gmail.com", "http://www.dast.biz/wp-content/uploads/2016/11/John_Doe.jpg"));
        groupList.add(new CreateGroupListItem("Pranav rana", "GU1", 3, "user_1@gmail.com", "https://www.bnl.gov/today/body_pics/2017/06/stephanhruszkewycz-355px.jpg"));
        groupList.add(new CreateGroupListItem("Neha reddy", "GU1", 3, "user_1@gmail.com", "http://www.medicine20congress.com/ocs/public/profiles/3141.jpg"));
        groupList.add(new CreateGroupListItem("Rushabh mehta", "GU1", 3, "user_1@gmail.com", "https://cdn.earthdata.nasa.gov/conduit/upload/6072/Glenn_headshot_resize.jpg"));

        createGroupAdapter = new CreateGroupAdapter(getActivity(), groupList);
        add_mem_frag_listview.setAdapter(createGroupAdapter);

        add_mem_frag_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        alertDialogBuilder.setMessage("Do you want to add member to the group?");
        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("YES");

            }
        });
        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("NO");
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
        add_mem_frag_listview.onRestoreInstanceState(state);

    }

}
