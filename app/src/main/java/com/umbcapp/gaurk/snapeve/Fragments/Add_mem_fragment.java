package com.umbcapp.gaurk.snapeve.Fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
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
import android.widget.ListView;
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
import com.umbcapp.gaurk.snapeve.Controllers.CreateGroupListItem;
import com.umbcapp.gaurk.snapeve.MainActivity;
import com.umbcapp.gaurk.snapeve.ManageGroups;
import com.umbcapp.gaurk.snapeve.R;
import com.umbcapp.gaurk.snapeve.SessionManager;
import com.umbcapp.gaurk.snapeve.Signup_grp_join;

import java.util.ArrayList;

public class Add_mem_fragment extends Fragment {

    private ListView add_mem_frag_listview;
    private Parcelable state;
    private ManageGroupAdapter manageGroupAdapter;
    private AutoCompleteTextView addmemberAutoCompleteTextView;
    ArrayList<String> userArrayList;
    private ArrayAdapter<String> userNamesAdapter;
    private String grp_id;

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

        String add_mem_response = getArguments().getString("add_mem_response");
        grp_id = getArguments().getString("grp_id");
        System.out.println("add_mem_response " + add_mem_response);

        addmemberAutoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.addmemberAutoCompleteTextView);
        add_mem_frag_listview = (ListView) rootView.findViewById(R.id.add_mem_frag_listview);

        userArrayList = new ArrayList<String>();

        //convert string to jsonelement
        parseAvailableMemberResponse(new Gson().fromJson(add_mem_response, JsonElement.class));


        add_mem_frag_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("position :" + position);
                openCancelRequestDialog(position);
            }
        });

        return rootView;
    }

    private void parseAvailableMemberResponse(JsonElement add_mem_response) {

        JsonArray grpDetailsJSONArray = add_mem_response.getAsJsonArray();
        groupList.clear();

        for (int i = 0; i < grpDetailsJSONArray.size(); i++) {

            JsonObject grpDetails_list_object = grpDetailsJSONArray.get(i).getAsJsonObject();

            String user_id = grpDetails_list_object.get("user_id").getAsString();
            String user_name = grpDetails_list_object.get("user_name").getAsString();
            String first_name = grpDetails_list_object.get("first_name").getAsString();
            String last_name = grpDetails_list_object.get("last_name").getAsString();
            String email = grpDetails_list_object.get("email").getAsString();

            //dp_url might come null
            String dp_url = null;
            try {
                dp_url = grpDetails_list_object.get("dp_url").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //hint array list
            userArrayList.add(first_name + " " + last_name);
            //main array list
            groupList.add(new CreateGroupListItem(user_name, first_name, last_name, user_id, 0, email, dp_url));

            //set data on activity members count tab
//            ((ManageGroups) getActivity()).takeNumbers(4, groupList.size());
        }
        userNamesAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, userArrayList);
        addmemberAutoCompleteTextView.setAdapter(userNamesAdapter);

        manageGroupAdapter = new ManageGroupAdapter(getActivity(), groupList);
        add_mem_frag_listview.setAdapter(manageGroupAdapter);
    }

    private void openCancelRequestDialog(final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        groupList.get(position).getUserName();
        alertDialogBuilder.setMessage("Do you want to add " + groupList.get(position).getUserName() + " to the group?");
        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("YES");
                reqToAddMemberToTheGroup(groupList.get(position).getUserId(), grp_id);
            }
        });
        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("NO");
            }
        });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.show();
    }

    private void reqToAddMemberToTheGroup(String userId, String grpId) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Sending request, Please wait...");
        progressDialog.create();
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        JsonObject jsonObjectParameters = new JsonObject();
        jsonObjectParameters.addProperty("req_code", 11);
        jsonObjectParameters.addProperty("grpId", grpId);
        jsonObjectParameters.addProperty("userId", userId);

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("group_request_handler_api", jsonObjectParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                progressDialog.dismiss();
                System.out.println(" group_request_handler_api exception    " + exception);

            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                progressDialog.dismiss();
                System.out.println(" group_request_handler_api success response    " + response);

                Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();

                if (response.toString().contains("true")) {
                    System.out.println("response OK");
                    ((ManageGroups) getActivity()).fetchAllFragsData();

                }

            }
        });

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
        add_mem_frag_listview.onRestoreInstanceState(state);

    }

}
