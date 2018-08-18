package com.umbcapp.gaurk.snapeve.Fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
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

import java.util.ArrayList;

public class RequestPendingFragment extends Fragment {

    private Parcelable state;

    private ListView req_pending_frag_listview;
    private ManageGroupAdapter manageGroupAdapter;
    private String grp_id;

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

        String grp_req_response = getArguments().getString("grp_req_response");
        grp_id = getArguments().getString("grp_id");

        System.out.println("grp_req_response " + grp_req_response);

        req_pending_frag_listview = (ListView) rootView.findViewById(R.id.req_pending_frag_listview);

        //convert string to jsonelement and parse
        parseGroupReqResponse(new Gson().fromJson(grp_req_response, JsonElement.class));

        req_pending_frag_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("position :" + position);
                openCancelRequestDialog(position);
            }
        });

        return rootView;
    }

    private void parseGroupReqResponse(JsonElement grp_req_response) {

        JsonArray grpDetailsJSONArray = grp_req_response.getAsJsonArray();
        groupList.clear();

        for (int i = 0; i < grpDetailsJSONArray.size(); i++) {

            JsonObject grpDetails_list_object = grpDetailsJSONArray.get(i).getAsJsonObject();
            int req_code = grpDetails_list_object.get("REQ_CODE").getAsInt();

            if (req_code == 11) {

                System.out.println("** grpDetails_list_object  " + grpDetails_list_object);

                String user_id = grpDetails_list_object.get("USER_ID").getAsString();
                String user_name = grpDetails_list_object.get("USER_NAME").getAsString();
                String first_name = grpDetails_list_object.get("FIRST_NAME").getAsString();
                String last_name = grpDetails_list_object.get("LAST_NAME").getAsString();
                String email = grpDetails_list_object.get("email").getAsString();

                //dp_url might come null
                String dp_url = null;
                try {
                    dp_url = grpDetails_list_object.get("DP_URL").getAsString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//            System.out.println(" user_id " + user_id);
//            System.out.println(" user_name " + user_name);
//            System.out.println(" first_name " + first_name);
//            System.out.println(" last_name " + last_name);
//            System.out.println(" dp_url " + dp_url);

                groupList.add(new CreateGroupListItem(user_name, first_name, last_name, user_id, 0, email, dp_url, 0));
            }
        }
        manageGroupAdapter = new ManageGroupAdapter(getActivity(), groupList);
        req_pending_frag_listview.setAdapter(manageGroupAdapter);

        //set data on activity members count tab
        ((ManageGroups) getActivity()).takeNumbers(3, groupList.size());
    }

    private void openCancelRequestDialog(final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setMessage("Do you want to cancel request?");
        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("YES");
                cancelSentRequest(groupList.get(position).getUserId(), grp_id);

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

    private void cancelSentRequest(String userId, String grpId) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Sending request, Please wait...");
        progressDialog.create();
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        JsonObject jsonObjectParameters = new JsonObject();
        jsonObjectParameters.addProperty("req_code", -11);
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
                    ((ManageGroups) getActivity()).fetchAllMemberData(3);

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
        req_pending_frag_listview.onRestoreInstanceState(state);

    }

}
