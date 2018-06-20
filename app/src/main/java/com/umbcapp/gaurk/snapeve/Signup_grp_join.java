package com.umbcapp.gaurk.snapeve;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
        parseResponse();

    }

    private void fetchGroupList() {
        final ProgressDialog progressDialog = new ProgressDialog(Signup_grp_join.this);
        progressDialog.setTitle("fetaching groups, Please wait...");
        progressDialog.create();
        progressDialog.show();
        JsonObject jsonObjectParameters = new JsonObject();

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
//        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("group_details_api", jsonObjectParameters);
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("neha_test_api", jsonObjectParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                progressDialog.dismiss();
                System.out.println(" group_details_api exception    " + exception);

            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                progressDialog.dismiss();

                System.out.println(" group_details_api success response    " + response);

                if (response.toString().equals("[]")) {
                    Toast.makeText(getApplicationContext(), "No groups found", Toast.LENGTH_SHORT).show();
                } else {
//                    parseResponse(response);
                }

            }
        });


    }

//    private void parseResponse(JsonElement response) {
    private void parseResponse() {

        signupGrpList = new ArrayList<SignUpGrpListItem>();

        System.out.println(" IN PARSE JASON");

//        JsonArray rankingJSONArray = response.getAsJsonArray();

//        for (int j = 0; j < rankingJSONArray.size(); j++) {
        for (int j = 0; j < 5; j++) {
//            JsonObject ranking_list_object = rankingJSONArray.get(j).getAsJsonObject();
//            System.out.println(" ranking_list_object  " + ranking_list_object);
//
//            String user_id = ranking_list_object.get("id").toString();
//            String user_name = ranking_list_object.get("user_name").toString();
//            int user_points = Integer.parseInt(ranking_list_object.get("user_points").toString());
//            String dp_url = ranking_list_object.get("dp_url").toString();
//
//
//            System.out.println(" user_id " + user_id);
//            System.out.println(" user_name " + user_name);
//            System.out.println(" user_points " + user_points);
//            System.out.println(" dp_url " + dp_url);

            //Remove " from start and end from every string
//            user_id = user_id.substring(1, user_id.length() - 1);
//            user_name = user_name.substring(1, user_name.length() - 1);
//            dp_url = dp_url.substring(1, dp_url.length() - 1);

            signupGrpList.add(new SignUpGrpListItem(12,"Strbx emp","https://icd.hwstatic.com/static/images/3.60.3.0/groups_article_three.jpg"));
            signupGrpList.add(new SignUpGrpListItem(12,"Strbx emp","https://icd.hwstatic.com/static/images/3.60.3.0/groups_article_three.jpg"));
            signupGrpList.add(new SignUpGrpListItem(12,"Strbx emp","https://icd.hwstatic.com/static/images/3.60.3.0/groups_article_three.jpg"));
            signupGrpList.add(new SignUpGrpListItem(12,"Strbx emp","https://icd.hwstatic.com/static/images/3.60.3.0/groups_article_three.jpg"));
            signupGrpList.add(new SignUpGrpListItem(12,"Strbx emp","https://icd.hwstatic.com/static/images/3.60.3.0/groups_article_three.jpg"));

        }
        System.out.println(" signupGrpList " + signupGrpList.size());
        SignupGrpAdapter signupGrpAdapter = new SignupGrpAdapter(Signup_grp_join.this, signupGrpList);
        grpNameListView.setAdapter(signupGrpAdapter);

    }

}
