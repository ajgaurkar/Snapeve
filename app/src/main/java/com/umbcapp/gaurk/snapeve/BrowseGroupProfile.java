package com.umbcapp.gaurk.snapeve;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.umbcapp.gaurk.snapeve.Adapters.GroupMembersAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.LeaderboardListItem;

import java.util.ArrayList;

public class BrowseGroupProfile extends AppCompatActivity implements Listview_communicator {


    private ArrayList<LeaderboardListItem> userProfileList;
    RecyclerView user_profile_contribution_rec_view;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView grp_member_count_text_view;
    private TextView browse_grp_profile_no_member_label_text_view;
    private JsonElement bkupMemberResponse;
    private JsonElement bkupPostResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.browser_grp_profile);
        user_profile_contribution_rec_view = (RecyclerView) findViewById(R.id.user_profile_contribution_rec_view);
        grp_member_count_text_view = (TextView) findViewById(R.id.grp_member_count_text_view);
        browse_grp_profile_no_member_label_text_view = (TextView) findViewById(R.id.browse_grp_profile_no_member_label_text_view);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        Intent grpId = getIntent();
        String grp_id = grpId.getStringExtra("grp_id");
        System.out.println("BrowseGroupProfile grp_id : " + grp_id);

        fetchGrpdata(grp_id, 0);
        fetchGrpdata(grp_id, 1);

        //add 2 listeners to switch between posts and members
        //and call parseMemberdata(bkupMemberResponse); OR parseGrpPostData(bkupPostResponse); accordingly

    }


    private void fetchGrpdata(String grp_id, final int fetch_type_post_or_members) {
        final ProgressDialog progressDialog = new ProgressDialog(BrowseGroupProfile.this);
        progressDialog.setTitle("Loading, Please wait...");
        progressDialog.create();
        progressDialog.show();
        JsonObject jsonObjectParameters = new JsonObject();
        jsonObjectParameters.addProperty("grp_id", grp_id);
        jsonObjectParameters.addProperty("fetch_type_post_or_members", fetch_type_post_or_members);

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("browse_grp_profile_api", jsonObjectParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {


            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                progressDialog.dismiss();
                System.out.println(" browse_grp_profile_api exception    " + exception);

            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                progressDialog.dismiss();

                System.out.println(" browse_grp_profile_api success response    " + response);

                if (fetch_type_post_or_members == 0) {
                    bkupPostResponse = response;
                    parseGrpPostData(bkupPostResponse);
                }
                if (fetch_type_post_or_members == 1) {
                    bkupMemberResponse = response;
                    parseMemberdata(bkupMemberResponse);
                }
            }
        });
    }

    private void parseGrpPostData(JsonElement response) {

    }

    private void parseMemberdata(JsonElement response) {
        System.out.println(" IN PARSE JASON");

        userProfileList = new ArrayList<>();
        JsonArray userDataJSONArray = response.getAsJsonArray();
        int max_user_points = 0;
        for (int i = 0; i < userDataJSONArray.size(); i++) {
            JsonObject userData_list_object = userDataJSONArray.get(i).getAsJsonObject();

            System.out.println(" userData_list_object  " + userData_list_object);

            String user_id = userData_list_object.get("user_id").toString();

            if (user_id == null || user_id.length() == 0 || user_id.equals("null")) {
//            if (TextUtils.isEmpty(user_id)) {
                System.out.println("user_id NULL : " + user_id);
            } else {

                System.out.println("user_id NOT NULL : " + user_id);
                String user_name = userData_list_object.get("user_name").toString();
                String dp_url = userData_list_object.get("dp_url").toString();
                String first_name = userData_list_object.get("first_name").toString();
                String last_name = userData_list_object.get("last_name").toString();
                int user_points = Integer.parseInt(userData_list_object.get("user_points").toString());
                if (user_points > max_user_points) {
                    max_user_points = user_points;
                }

                System.out.println(" user_name " + user_name);
                System.out.println(" user_points " + user_points);
                System.out.println(" dp_url " + dp_url);
                System.out.println(" first_name " + first_name);
                System.out.println(" last_name " + last_name);

                //Remove " from start and end from every string
                user_name = user_name.substring(1, user_name.length() - 1);
                first_name = first_name.substring(1, first_name.length() - 1);
                last_name = last_name.substring(1, last_name.length() - 1);
                dp_url = dp_url.substring(1, dp_url.length() - 1);
                user_id = user_id.substring(1, user_id.length() - 1);
                userProfileList.add(new LeaderboardListItem(user_id, user_name, user_points, "", dp_url));

            }
        }

        GroupMembersAdapter groupMembersAdapter = new GroupMembersAdapter(BrowseGroupProfile.this, userProfileList, max_user_points);
        user_profile_contribution_rec_view.setLayoutManager(mLayoutManager);
        user_profile_contribution_rec_view.setItemAnimator(new DefaultItemAnimator());
        user_profile_contribution_rec_view.setAdapter(groupMembersAdapter);

        if (userProfileList.size() == 0) {
            browse_grp_profile_no_member_label_text_view.setVisibility(View.VISIBLE);
            grp_member_count_text_view.setText(userProfileList.size() + " Members");
        } else if (userProfileList.size() == 1) {
            browse_grp_profile_no_member_label_text_view.setVisibility(View.GONE);
            grp_member_count_text_view.setText(userProfileList.size() + " Member");
        } else {
            browse_grp_profile_no_member_label_text_view.setVisibility(View.GONE);
            grp_member_count_text_view.setText(userProfileList.size() + " Members");
        }

    }

    @Override
    public void main_event_listview_element_clicked(int position, int click_code) {
        userProfileList.get(position);
        System.out.println(userProfileList.get(position).getUserId());
    }
}
