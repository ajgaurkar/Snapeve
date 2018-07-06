package com.umbcapp.gaurk.snapeve.Fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.umbcapp.gaurk.snapeve.Adapters.CreateGroupAdapter;
import com.umbcapp.gaurk.snapeve.BrowseUserProfile;
import com.umbcapp.gaurk.snapeve.Controllers.CreateGroupListItem;
import com.umbcapp.gaurk.snapeve.Listview_communicator;
import com.umbcapp.gaurk.snapeve.R;

import java.sql.Array;
import java.util.ArrayList;

public class Mem_joined_fragment extends Fragment {

    private Parcelable state;

    private ListView mem_joined_frag_listview;
    private CreateGroupAdapter createGroupAdapter;
    private int mem_joined_dialog_selected_option;

    public Mem_joined_fragment() {

    }

    ArrayList<CreateGroupListItem> groupList = new ArrayList<CreateGroupListItem>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mem_joined_fragment, container, false);

        mem_joined_frag_listview = (ListView) rootView.findViewById(R.id.mem_joined_frag_listview);

        groupList.clear();
        groupList.add(new CreateGroupListItem("Ajinkya Gaurkar", "GU1", 0, "user_1@gmail.com", "http://keenthemes.com/preview/metronic/theme/assets/pages/media/profile/profile_user.jpg"));
        groupList.add(new CreateGroupListItem("Siddhrth Ptro", "GU1", 0, "user_1@gmail.com", "http://www.dast.biz/wp-content/uploads/2016/11/John_Doe.jpg"));
        groupList.add(new CreateGroupListItem("Pranav rana", "GU1", 0, "user_1@gmail.com", "https://www.bnl.gov/today/body_pics/2017/06/stephanhruszkewycz-355px.jpg"));
        groupList.add(new CreateGroupListItem("Neha reddy", "GU1", 0, "user_1@gmail.com", "http://www.medicine20congress.com/ocs/public/profiles/3141.jpg"));
        groupList.add(new CreateGroupListItem("Gautam Rao", "GU1", 0, "user_1@gmail.com", "https://cdn.earthdata.nasa.gov/conduit/upload/6072/Glenn_headshot_resize.jpg"));
        groupList.add(new CreateGroupListItem("Charles Nikola", "GU1", 0, "user_1@gmail.com", "http://www.medicine20congress.com/ocs/public/profiles/3141.jpg"));
        groupList.add(new CreateGroupListItem("Rushabh mehta", "GU1", 0, "user_1@gmail.com", "https://cdn.earthdata.nasa.gov/conduit/upload/6072/Glenn_headshot_resize.jpg"));

        createGroupAdapter = new CreateGroupAdapter(getActivity(), groupList);
        mem_joined_frag_listview.setAdapter(createGroupAdapter);
        mem_joined_frag_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("position :" + position);
//                openCancelRequestDialog(position);
                openOptionsDialog(position);
            }
        });

        return rootView;
    }

    private void openOptionsDialog(final int memebr_position) {

        LayoutInflater flater = getActivity().getLayoutInflater();
        View view = flater.inflate(R.layout.mem_joined_frag_options_dialog, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setView(view);
        alertDialog.setCancelable(false);
        alertDialog.setTitle(groupList.get(memebr_position).getUserName());

        System.out.println("groupList.get(memebr_position).getUserName() " + groupList.get(memebr_position).getUserName());
        mem_joined_dialog_selected_option = 0;

        final ArrayList<String> memJoinedOptionsList = new ArrayList<>();
        memJoinedOptionsList.add("View Profile");
        memJoinedOptionsList.add(getString(R.string.set_privilege_set_1));
        memJoinedOptionsList.add(getString(R.string.set_privilege_set_2));
        memJoinedOptionsList.add("Make admin");
        memJoinedOptionsList.add("Remove from group");

        ListView mem_joined_frag_options_listview = (ListView) view.findViewById(R.id.mem_joined_frag_options_listview);
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.members_joined_options_list_item, memJoinedOptionsList);
        mem_joined_frag_options_listview.setAdapter(adapter);

        final TextView mem_joined_frag_confirm_textview = (TextView) view.findViewById(R.id.mem_joined_frag_confirm_textview);
        final TextView mem_joined_frag_user_name_textview = (TextView) view.findViewById(R.id.mem_joined_frag_user_name_textview);
        final LinearLayout mem_joined_frag_btn_lin_layout = (LinearLayout) view.findViewById(R.id.mem_joined_frag_btn_lin_layout);
//        CardView mem_joined_frag_cancel_btn_card_view = (CardView) view.findViewById(R.id.mem_joined_frag_cancel_btn_card_view);
        CardView mem_joined_frag_confirm_btn_card_view = (CardView) view.findViewById(R.id.mem_joined_frag_confirm_btn_card_view);
        mem_joined_frag_btn_lin_layout.setVisibility(View.GONE);
        mem_joined_frag_confirm_textview.setVisibility(View.GONE);
        mem_joined_frag_user_name_textview.setText(groupList.get(memebr_position).getUserId());

        mem_joined_frag_confirm_btn_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "CONFIRM CLICKED", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        mem_joined_frag_options_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        System.out.println(memJoinedOptionsList.get(position));
                        mem_joined_dialog_selected_option = position;

                        Intent userProfileIntent = new Intent(getActivity(), BrowseUserProfile.class);
                        userProfileIntent.putExtra("user_id", groupList.get(memebr_position).getUserId());
                        startActivity(userProfileIntent);

                        break;

                    case 1:
                        System.out.println(memJoinedOptionsList.get(position));
                        mem_joined_frag_confirm_textview.setText("Confirm granting privileges to POST on behalf of the group");
                        mem_joined_frag_confirm_textview.setVisibility(View.VISIBLE);
                        mem_joined_frag_btn_lin_layout.setVisibility(View.VISIBLE);
                        mem_joined_dialog_selected_option = position;

                        break;

                    case 2:
                        System.out.println(memJoinedOptionsList.get(position));
                        mem_joined_frag_confirm_textview.setText("Confirm granting privileges to POST and manage group member");
                        mem_joined_frag_btn_lin_layout.setVisibility(View.VISIBLE);
                        mem_joined_frag_confirm_textview.setVisibility(View.VISIBLE);
                        mem_joined_dialog_selected_option = position;

                        break;

                    case 3:
                        mem_joined_frag_confirm_textview.setText("Confirm giving rights of admin. Doing this will result in your admin rights to get revoked");
                        mem_joined_frag_btn_lin_layout.setVisibility(View.VISIBLE);
                        mem_joined_frag_confirm_textview.setVisibility(View.VISIBLE);
                        System.out.println(memJoinedOptionsList.get(position));
                        mem_joined_dialog_selected_option = position;

                        break;

                    case 4:
                        mem_joined_frag_confirm_textview.setText("Confirm removing the user from this group");
                        mem_joined_frag_btn_lin_layout.setVisibility(View.VISIBLE);
                        mem_joined_frag_confirm_textview.setVisibility(View.VISIBLE);
                        System.out.println(memJoinedOptionsList.get(position));
                        mem_joined_dialog_selected_option = position;

                        break;
                }
            }
        });

        alertDialog.show();
    }

    private void openCancelRequestDialog(int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setMessage("Do you want to Remove member?");
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
        alertDialogBuilder.create().show();
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
        mem_joined_frag_listview.onRestoreInstanceState(state);

    }
}
