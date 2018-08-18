package com.umbcapp.gaurk.snapeve.Fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.umbcapp.gaurk.snapeve.Adapters.ManageGroupAdapter;
import com.umbcapp.gaurk.snapeve.BrowseUserProfile;
import com.umbcapp.gaurk.snapeve.Controllers.CreateGroupListItem;
import com.umbcapp.gaurk.snapeve.MainActivity;
import com.umbcapp.gaurk.snapeve.ManageGroups;
import com.umbcapp.gaurk.snapeve.R;
import com.umbcapp.gaurk.snapeve.SessionManager;

import java.util.ArrayList;

public class Mem_joined_fragment extends Fragment {

    private Parcelable state;

    private ListView mem_joined_frag_listview;
    private ManageGroupAdapter manageGroupAdapter;
    private int mem_joined_dialog_selected_option;
    private Context mContext;
    private int selectedMemberPosition = -1;
    private String grp_id;
    private AlertDialog close_Dialog_temp_obj;
    private int user_admin_flag = 0;

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

        String mem_joined_response = getArguments().getString("mem_joined_response");
        grp_id = getArguments().getString("grp_id");
        user_admin_flag = getArguments().getInt("user_admin_flag");
        System.out.println("mem_joined_response " + mem_joined_response);

        mem_joined_frag_listview = (ListView) rootView.findViewById(R.id.mem_joined_frag_listview);

        //convert string to jsonelement and parse
        parseFetchGroupDetailsResponse(new Gson().fromJson(mem_joined_response, JsonElement.class));

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

    private void parseFetchGroupDetailsResponse(JsonElement fetchGroupDetailsResponse) {

        JsonArray grpDetailsJSONArray = fetchGroupDetailsResponse.getAsJsonArray();
        groupList.clear();

        for (int i = 0; i < grpDetailsJSONArray.size(); i++) {

            JsonObject grpDetails_list_object = grpDetailsJSONArray.get(i).getAsJsonObject();

//            System.out.println("-- grpDetails_list_object  " + grpDetails_list_object);

            String user_id = grpDetails_list_object.get("user_id").getAsString();
            String user_name = grpDetails_list_object.get("user_name").getAsString();
            String first_name = grpDetails_list_object.get("first_name").getAsString();
            String last_name = grpDetails_list_object.get("last_name").getAsString();
            String email = grpDetails_list_object.get("email").getAsString();
            int privilege_type = grpDetails_list_object.get("PRIVILEGE_TYPE").getAsInt();

            //dp_url might come null
            String dp_url = null;
            try {
                dp_url = grpDetails_list_object.get("dp_url").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            int group_admin_flag = 0;
            try {
                group_admin_flag = Integer.parseInt(grpDetails_list_object.get("group_admin_flag").toString());
            } catch (Exception e) {
                e.printStackTrace();
            }


//            System.out.println(" user_id " + user_id);
//            System.out.println(" user_name " + user_name);
//            System.out.println(" first_name " + first_name);
//            System.out.println(" last_name " + last_name);
//            System.out.println(" dp_url " + dp_url);

            if (!user_id.equals(new SessionManager(getActivity()).getSpecificUserDetail(SessionManager.KEY_USER_ID))) {
                //req status is sent like privilege type
                //this is mem_joined frag hence req_status is not present. instead privilege_type is needed that will tell the privilege_type of joined user
                groupList.add(new CreateGroupListItem(user_name, first_name, last_name, user_id, privilege_type, email, dp_url, group_admin_flag));
            } else {
                System.out.println("List item skipped : item same as admin");
            }

        }
        manageGroupAdapter = new ManageGroupAdapter(getActivity(), groupList);
        mem_joined_frag_listview.setAdapter(manageGroupAdapter);

        //set data on activity members count tab
        ((ManageGroups) getActivity()).takeNumbers(1, groupList.size());
    }

    private void openOptionsDialog(final int memebr_position) {

        selectedMemberPosition = memebr_position;

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

        if (groupList.get(memebr_position).getGrpAdminFlag() == 0) {
            memJoinedOptionsList.add("Modify user Privilege to:");
            memJoinedOptionsList.add("          " + getString(R.string.set_privilege_set_3));
            memJoinedOptionsList.add("          " + getString(R.string.set_privilege_set_1));
            memJoinedOptionsList.add("          " + getString(R.string.set_privilege_set_2));
            memJoinedOptionsList.add("Remove from group");

            //set pointr showing current user privileges
            switch (groupList.get(memebr_position).getUserReqStatus()) {

                case 1:
                    memJoinedOptionsList.set(2, "  -->   " + getString(R.string.set_privilege_set_3));
                    break;
                case 2:
                    memJoinedOptionsList.set(3, "  -->   " + getString(R.string.set_privilege_set_1));
                    break;
                case 3:
                    memJoinedOptionsList.set(4, "  -->   " + getString(R.string.set_privilege_set_2));
                    break;

            }
        }
        if (user_admin_flag == 1) {
            memJoinedOptionsList.add("Make admin");
        }


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
        mem_joined_frag_user_name_textview.setText(groupList.get(memebr_position).getUserEmail());

        mem_joined_frag_confirm_btn_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "CONFIRM CLICKED", Toast.LENGTH_SHORT).show();
                takeAction(mem_joined_dialog_selected_option);
                close_Dialog_temp_obj.dismiss();
            }
        });

        alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        mem_joined_frag_options_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mem_joined_frag_confirm_textview.setText("");
                mem_joined_frag_confirm_textview.setVisibility(View.GONE);
                mem_joined_frag_btn_lin_layout.setVisibility(View.GONE);

                switch (position) {
                    case 0:
                        System.out.println(memJoinedOptionsList.get(position));
                        mem_joined_dialog_selected_option = position;

                        Intent userProfileIntent = new Intent(getActivity(), BrowseUserProfile.class);
                        userProfileIntent.putExtra("user_id", groupList.get(memebr_position).getUserId());
                        startActivity(userProfileIntent);

                        break;
                    case 1:
//                        System.out.println(memJoinedOptionsList.get(position));
//                        mem_joined_dialog_selected_option = position;

                        //dummy list element. used as header for privilege list indented items

                        Toast.makeText(getActivity(), "Select any privilege type below", Toast.LENGTH_SHORT).show();
                        break;

                    case 2:
                        //req status I.E. privilege type in this frag
                        if (groupList.get(memebr_position).getUserReqStatus() == 1) {
                            Toast.makeText(getActivity(), "Already existing privilege", Toast.LENGTH_SHORT).show();
                        } else {
                            System.out.println(memJoinedOptionsList.get(position));
                            mem_joined_frag_confirm_textview.setText("Confirm granting privileges only to READ group posts");
                            mem_joined_frag_confirm_textview.setVisibility(View.VISIBLE);
                            mem_joined_frag_btn_lin_layout.setVisibility(View.VISIBLE);
                            mem_joined_dialog_selected_option = position;
                        }
                        break;

                    case 3:
                        if (groupList.get(memebr_position).getUserReqStatus() == 2) {
                            Toast.makeText(getActivity(), "Already existing privilege", Toast.LENGTH_SHORT).show();
                        } else {
                            System.out.println(memJoinedOptionsList.get(position));
                            mem_joined_frag_confirm_textview.setText("Confirm granting privileges to POST on behalf of the group");
                            mem_joined_frag_confirm_textview.setVisibility(View.VISIBLE);
                            mem_joined_frag_btn_lin_layout.setVisibility(View.VISIBLE);
                            mem_joined_dialog_selected_option = position;
                        }
                        break;

                    case 4:
                        if (groupList.get(memebr_position).getUserReqStatus() == 3) {
                            Toast.makeText(getActivity(), "Already existing privilege", Toast.LENGTH_SHORT).show();
                        } else {
                            System.out.println(memJoinedOptionsList.get(position));
                            mem_joined_frag_confirm_textview.setText("Confirm granting privileges to POST and manage group member");
                            mem_joined_frag_btn_lin_layout.setVisibility(View.VISIBLE);
                            mem_joined_frag_confirm_textview.setVisibility(View.VISIBLE);
                            mem_joined_dialog_selected_option = position;
                        }
                        break;

                    case 5:
                        mem_joined_frag_confirm_textview.setText("Confirm removing the user from this group");
                        mem_joined_frag_btn_lin_layout.setVisibility(View.VISIBLE);
                        mem_joined_frag_confirm_textview.setVisibility(View.VISIBLE);
                        System.out.println(memJoinedOptionsList.get(position));
                        mem_joined_dialog_selected_option = position;

                        break;

                    case 6:
                        mem_joined_frag_confirm_textview.setText("Confirm giving rights of admin. Doing this will result in your admin rights to get revoked");
                        mem_joined_frag_btn_lin_layout.setVisibility(View.VISIBLE);
                        mem_joined_frag_confirm_textview.setVisibility(View.VISIBLE);
                        System.out.println(memJoinedOptionsList.get(position));
                        mem_joined_dialog_selected_option = position;

                        break;

                }
            }
        });

        //temp variable, because direct dismiss on alertdialog is not available ..hence take it to alertdilog again and then dismis(weird)
        close_Dialog_temp_obj = alertDialog.show();

    }

    private void takeAction(int mem_joined_dialog_selected_option) {

        switch (mem_joined_dialog_selected_option) {

            case 2:
                modifyUserPrivileges(groupList.get(selectedMemberPosition).getUserId(), grp_id, 1);
                break;
            case 3:
                modifyUserPrivileges(groupList.get(selectedMemberPosition).getUserId(), grp_id, 2);

                break;
            case 4:
                modifyUserPrivileges(groupList.get(selectedMemberPosition).getUserId(), grp_id, 3);

                break;
            case 5:
                //                modifyUserPrivileges(groupList.get(selectedMemberPosition).getUserId(), grp_id, 4);
                break;
            case 6:
                updateAdminRights(grp_id,groupList.get(selectedMemberPosition).getUserId(), new SessionManager(getActivity()).getSpecificUserDetail(SessionManager.KEY_USER_ID));
//                modifyUserPrivileges(groupList.get(selectedMemberPosition).getUserId(), grp_id, 5);
                break;
        }
    }

    private void updateAdminRights(String grp_id, String newAdminId, String oldAdminId) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Updating, Please wait...");
        progressDialog.create();
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        JsonObject jsonObjectParameters = new JsonObject();
        jsonObjectParameters.addProperty("newAdminId", newAdminId);
        jsonObjectParameters.addProperty("grpId", grp_id);
        jsonObjectParameters.addProperty("oldAdminId", oldAdminId);

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("make_other_user_admin_api", jsonObjectParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                progressDialog.dismiss();
                System.out.println(" make_other_user_admin_api exception    " + exception);

            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                progressDialog.dismiss();
                System.out.println(" make_other_user_admin_api success response    " + response);

                Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();

                if (response.toString().contains("true")) {
                    System.out.println("response OK");
                    ((ManageGroups) getActivity()).fetchAllMemberData(1);

                }

            }
        });

    }

    private void modifyUserPrivileges(String userId, String grpId, int modify_code) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Updating, Please wait...");
        progressDialog.create();
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        JsonObject jsonObjectParameters = new JsonObject();
        jsonObjectParameters.addProperty("modify_code", modify_code);
        jsonObjectParameters.addProperty("grpId", grpId);
        jsonObjectParameters.addProperty("userId", userId);

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("modify_privileges_api", jsonObjectParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                progressDialog.dismiss();
                System.out.println(" modify_privileges_api exception    " + exception);

            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                progressDialog.dismiss();
                System.out.println(" modify_privileges_api success response    " + response);

                Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();

                if (response.toString().contains("true")) {
                    System.out.println("response OK");
                    ((ManageGroups) getActivity()).fetchAllMemberData(1);

                }

            }
        });

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
        manageGroupAdapter.notifyDataSetChanged();
        mem_joined_frag_listview.onRestoreInstanceState(state);

    }

}
