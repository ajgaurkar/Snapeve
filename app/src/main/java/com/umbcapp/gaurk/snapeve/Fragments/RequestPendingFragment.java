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
import java.util.List;

public class RequestPendingFragment extends Fragment {

    private Parcelable state;

    private ListView req_pending_frag_listview;
    private CreateGroupAdapter createGroupAdapter;

    public RequestPendingFragment() {

    }

    ArrayList<CreateGroupListItem> groupList = new ArrayList<CreateGroupListItem>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.req_pending_fragment, container, false);

        req_pending_frag_listview = (ListView) rootView.findViewById(R.id.req_pending_frag_listview);

        groupList.clear();
        groupList.add(new CreateGroupListItem("Ajinkya Gaurkar", "GU1", 2, "user_1@gmail.com", "http://keenthemes.com/preview/metronic/theme/assets/pages/media/profile/profile_user.jpg"));
        groupList.add(new CreateGroupListItem("Siddhrth Ptro", "GU1", 2, "user_1@gmail.com", "http://www.dast.biz/wp-content/uploads/2016/11/John_Doe.jpg"));
        groupList.add(new CreateGroupListItem("Pranav rana", "GU1", 2, "user_1@gmail.com", "https://www.bnl.gov/today/body_pics/2017/06/stephanhruszkewycz-355px.jpg"));
        groupList.add(new CreateGroupListItem("Neha reddy", "GU1", 2, "user_1@gmail.com", "http://www.medicine20congress.com/ocs/public/profiles/3141.jpg"));
        groupList.add(new CreateGroupListItem("Rushabh mehta", "GU1", 2, "user_1@gmail.com", "https://cdn.earthdata.nasa.gov/conduit/upload/6072/Glenn_headshot_resize.jpg"));

        createGroupAdapter = new CreateGroupAdapter(getActivity(), groupList);
        req_pending_frag_listview.setAdapter(createGroupAdapter);


        req_pending_frag_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        alertDialogBuilder.setMessage("Do you want to cancel request?");
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
        req_pending_frag_listview.onRestoreInstanceState(state);

    }

}
