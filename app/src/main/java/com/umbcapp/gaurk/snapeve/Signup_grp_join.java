package com.umbcapp.gaurk.snapeve;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.umbcapp.gaurk.snapeve.Adapters.LeaderBoardAdapter;
import com.umbcapp.gaurk.snapeve.Adapters.SignupGrpAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.LeaderboardListItem;
import com.umbcapp.gaurk.snapeve.Controllers.SignUpGrpListItem;

import java.util.ArrayList;

public class Signup_grp_join extends AppCompatActivity {

    private TextView signup_grp_skip_textview;
    private TextView signup_grp_create_grp_textview;
    private ListView grpNameListView;
    private ArrayList<SignUpGrpListItem> signupGrpList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_grp_join);

        grpNameListView = (ListView) findViewById(R.id.signup_grp_list_view);
        signup_grp_create_grp_textview = (TextView) findViewById(R.id.signup_grp_create_grp_textview);
        signup_grp_skip_textview = (TextView) findViewById(R.id.signup_grp_skip_textview);

        fetchGroupList();

        signup_grp_skip_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        signup_grp_create_grp_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), CreateGruoups.class));
            }
        });
        grpNameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                SignUpGrpListItem selectedGrpItem = signupGrpList.get(position);

                requestToJoinGrpDialog(signupGrpList.get(position).getGrpId(), signupGrpList.get(position).getGrpName(), new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_USER_ID));

            }
        });

    }

    private void requestToJoinGrpDialog(final String grpId, String grpName, final String userId) {
        final AlertDialog.Builder grpJoinDialog = new AlertDialog.Builder(Signup_grp_join.this);

        grpJoinDialog.setTitle("Confirm");
        grpJoinDialog.setMessage("Do you wish to join " + grpName);
        grpJoinDialog.setCancelable(false);

        grpJoinDialog.setPositiveButton("Join", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestToJoinGrp(grpId, userId);
            }
        });
        grpJoinDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        grpJoinDialog.create().show();
    }

    private void requestToJoinGrp(String grpId, String userId) {

        final ProgressDialog progressDialog = new ProgressDialog(Signup_grp_join.this);
        progressDialog.setTitle("Sending request to join group, Please wait...");
        progressDialog.create();
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        JsonObject jsonObjectParameters = new JsonObject();
        jsonObjectParameters.addProperty("req_code", 0);
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

                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();

                if (response.toString().contains("JOIN REQUEST RESPONSE")) {
                    System.out.println("response OK");
                }

//                    parseJoinRequestResponse(response);
            }
        });

    }

    private void fetchGroupList() {
        final ProgressDialog progressDialog = new ProgressDialog(Signup_grp_join.this);
        progressDialog.setTitle("fetaching groups, Please wait...");
        progressDialog.create();
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        JsonObject jsonObjectParameters = new JsonObject();

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("all_grp_list_api", jsonObjectParameters);
//        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("neha_test_api", jsonObjectParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                progressDialog.dismiss();
                System.out.println(" all_grp_list_api exception    " + exception);

            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                progressDialog.dismiss();

                System.out.println(" all_grp_list_api success response    " + response);

                if (response.toString().equals("[]")) {
                    Toast.makeText(getApplicationContext(), "No groups found", Toast.LENGTH_SHORT).show();
                } else {
                    parseResponse(response);
                }
            }
        });
    }

    private void parseResponse(JsonElement response) {
//    private void parseResponse() {

        signupGrpList = new ArrayList<SignUpGrpListItem>();

        System.out.println(" IN PARSE JASON");

        JsonArray groupsJSONArray = response.getAsJsonArray();

        for (int j = 0; j < groupsJSONArray.size(); j++) {
            JsonObject group_list_object = groupsJSONArray.get(j).getAsJsonObject();
            System.out.println(" group_list_object  " + group_list_object);

            String grp_id = group_list_object.get("id").toString();
            String grp_name = group_list_object.get("grp_name").toString();
            int grp_points = Integer.parseInt(group_list_object.get("total_pts").toString());
            int member_count = Integer.parseInt(group_list_object.get("member_count").toString());
            String dp_url = group_list_object.get("grp_dp_url").toString();

            System.out.println(" user_id " + grp_id);
            System.out.println(" grp_name " + grp_name);
            System.out.println(" grp_points " + grp_points);
            System.out.println(" dp_url " + dp_url);

//            Remove " from start and end from every string
            grp_id = grp_id.substring(1, grp_id.length() - 1);
            grp_name = grp_name.substring(1, grp_name.length() - 1);
            dp_url = dp_url.substring(1, dp_url.length() - 1);

            signupGrpList.add(new SignUpGrpListItem(grp_id, member_count, grp_name, dp_url));

        }
        System.out.println(" signupGrpList " + signupGrpList.size());
        SignupGrpAdapter signupGrpAdapter = new SignupGrpAdapter(Signup_grp_join.this, signupGrpList);
        grpNameListView.setAdapter(signupGrpAdapter);

    }

}
