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
import com.umbcapp.gaurk.snapeve.Adapters.LeaderBoardAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.Event_dash_list_obj;
import com.umbcapp.gaurk.snapeve.Controllers.LeaderboardListItem;

import java.util.ArrayList;

public class Leaderboard extends AppCompatActivity implements Listview_communicator {

    int user_type = -1;
    private RecyclerView leader_board_recyclerview;
    private ArrayList<LeaderboardListItem> leaderBoardIndList;
    private ArrayList<LeaderboardListItem> leaderBoardGrpList;
    private LeaderBoardAdapter leaderBoardAdapter;
    private int maxIndividualRank = 0;
    private int maxGrpRank = 0;
    private TextView top_10_all_sort_textview;
    private TextView leader_board_switch_grp_textview;
    private TextView leader_board_switch_you_textview;
    private int user_type_selection_status;
    private Boolean top_10_selection_status;
    private RecyclerView.LayoutManager mLayoutManager;
    private JsonObject jsonObjectLoginParameters;
    private JsonElement bkupUserResponse;
    private JsonElement bkupGrpResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard_activity);

        user_type_selection_status = 0;
        top_10_selection_status = true;
        leader_board_recyclerview = (RecyclerView) findViewById(R.id.leader_board_recyclerview);

        top_10_all_sort_textview = (TextView) findViewById(R.id.top_10_all_sort_textview);
        leader_board_switch_grp_textview = (TextView) findViewById(R.id.leader_board_switch_grp_textview);
        leader_board_switch_you_textview = (TextView) findViewById(R.id.leader_board_switch_you_textview);

        System.out.println("System.currentTimeMillis() onCreate : " + System.currentTimeMillis());

        //get individual data
        getLeaderboardData(0);
        //get grp data
        getLeaderboardData(1);

        leader_board_switch_you_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                leader_board_switch_you_textview.setBackground(getResources().getDrawable(R.drawable.text_selection_left_seleted));
                leader_board_switch_grp_textview.setBackground(getResources().getDrawable(R.drawable.text_selection_right_unseleted));
                user_type_selection_status = 0;
                populateLeaderboardListView(user_type_selection_status);
            }
        });

        leader_board_switch_grp_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                leader_board_switch_you_textview.setBackground(getResources().getDrawable(R.drawable.text_selection_left_unseleted));
                leader_board_switch_grp_textview.setBackground(getResources().getDrawable(R.drawable.text_selection_right_seleted));
                user_type_selection_status = 1;
                populateLeaderboardListView(user_type_selection_status);

            }
        });

        top_10_all_sort_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                top_10_selection_status = !top_10_selection_status;
                if (top_10_selection_status) {
                    top_10_all_sort_textview.setText("Top 10");
                } else {
                    top_10_all_sort_textview.setText("   All    ");
                }
            }
        });

        leaderBoardIndList = new ArrayList<LeaderboardListItem>();

        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        leader_board_recyclerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user_type_selection_status == 0) {

                    int selectedItemPosition = leader_board_recyclerview.getChildLayoutPosition(v);
//                    leaderBoardIndList.get(selectedItemPosition).getUserId();
                    System.out.println(leaderBoardIndList.get(selectedItemPosition).getUserId());

                }
                if (user_type_selection_status == 1) {

                    int selectedItemPosition = leader_board_recyclerview.getChildLayoutPosition(v);
//                    leaderBoardGrpList.get(selectedItemPosition).getUserId();
                    System.out.println(leaderBoardGrpList.get(selectedItemPosition).getUserId());
                }
            }
        });


    }

    private void getLeaderboardData(final int code) {

        final ProgressDialog progressDialog = new ProgressDialog(Leaderboard.this);
        progressDialog.setTitle("Loading, Please wait...");
        progressDialog.create();
        progressDialog.show();
        jsonObjectLoginParameters = new JsonObject();
        jsonObjectLoginParameters.addProperty("fetch_code", code);

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("get_leader_board_info_api", jsonObjectLoginParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                progressDialog.dismiss();
                System.out.println(" get_leader_board_info_api exception    " + exception);

            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                progressDialog.dismiss();

                System.out.println(" get_leader_board_info_api success response    " + response);

                if (code == 0) {
                    bkupUserResponse = response;
                    createUsersArrayList(response);
                }
                if (code == 1) {
                    bkupGrpResponse = response;
                    createGrpArrayList(response);
                }

            }
        });
    }

    private void createUsersArrayList(JsonElement response) {
        leaderBoardIndList = new ArrayList<LeaderboardListItem>();

        System.out.println(" IN PARSE JASON");

        JsonArray rankingJSONArray = response.getAsJsonArray();

        for (int j = 0; j < rankingJSONArray.size(); j++) {
            JsonObject ranking_list_object = rankingJSONArray.get(j).getAsJsonObject();
            System.out.println(" ranking_list_object  " + ranking_list_object);

            String user_id = ranking_list_object.get("id").toString();
            String user_name = ranking_list_object.get("user_name").toString();
            int user_points = Integer.parseInt(ranking_list_object.get("user_points").toString());
            String dp_url = ranking_list_object.get("dp_url").toString();

            if (user_points > maxIndividualRank) {
                maxIndividualRank = user_points;
            }

            System.out.println(" user_id " + user_id);
            System.out.println(" user_name " + user_name);
            System.out.println(" user_points " + user_points);
            System.out.println(" dp_url " + dp_url);

            //Remove " from start and end from every string
            user_id = user_id.substring(1, user_id.length() - 1);
            user_name = user_name.substring(1, user_name.length() - 1);
            dp_url = dp_url.substring(1, dp_url.length() - 1);

            leaderBoardIndList.add(new LeaderboardListItem(user_id, user_name, user_points, "Grp UMBC", dp_url));

        }
        System.out.println(" leaderBoardIndList " + leaderBoardIndList.size());
        leaderBoardAdapter = new LeaderBoardAdapter(Leaderboard.this, leaderBoardIndList, maxIndividualRank, user_type_selection_status);
        leader_board_recyclerview.setLayoutManager(mLayoutManager);
        leader_board_recyclerview.setItemAnimator(new DefaultItemAnimator());
        leader_board_recyclerview.setAdapter(leaderBoardAdapter);
    }

    private void createGrpArrayList(JsonElement response) {
        leaderBoardGrpList = new ArrayList<LeaderboardListItem>();

        System.out.println(" IN PARSE JASON");

        JsonArray rankingJSONArray = response.getAsJsonArray();

        for (int j = 0; j < rankingJSONArray.size(); j++) {
            JsonObject ranking_list_object = rankingJSONArray.get(j).getAsJsonObject();
            System.out.println(" ranking_list_object  " + ranking_list_object);

            String grp_id = ranking_list_object.get("id").toString();
            String grp_name = ranking_list_object.get("grp_name").toString();
            int grp_points = Integer.parseInt(ranking_list_object.get("total_pts").toString());
            String grp_dp_url = ranking_list_object.get("grp_dp_url").toString();

            if (grp_points > maxGrpRank) {
                maxGrpRank = grp_points;
            }

            System.out.println(" grp_id " + grp_id);
            System.out.println(" grp_name " + grp_name);
            System.out.println(" grp_points " + grp_points);
            System.out.println(" grp_dp_url " + grp_dp_url);

            //Remove " from start and end from every string
            grp_id = grp_id.substring(1, grp_id.length() - 1);
            grp_name = grp_name.substring(1, grp_name.length() - 1);
            grp_dp_url = grp_dp_url.substring(1, grp_dp_url.length() - 1);

            leaderBoardGrpList.add(new LeaderboardListItem(grp_id, "place holder", grp_points, grp_name, grp_dp_url));

        }
        System.out.println(" leaderBoardIndList " + leaderBoardIndList.size());
    }

    private void populateLeaderboardListView(int user_type_selection_status) {

        if (user_type_selection_status == 0) {

            leaderBoardAdapter = new LeaderBoardAdapter(Leaderboard.this, leaderBoardIndList, maxIndividualRank, user_type_selection_status);
            leader_board_recyclerview.setLayoutManager(mLayoutManager);
            leader_board_recyclerview.setItemAnimator(new DefaultItemAnimator());
            leader_board_recyclerview.setAdapter(leaderBoardAdapter);

        }
        if (user_type_selection_status == 1) {

            leaderBoardAdapter = new LeaderBoardAdapter(Leaderboard.this, leaderBoardGrpList, maxGrpRank, user_type_selection_status);
            leader_board_recyclerview.setLayoutManager(mLayoutManager);
            leader_board_recyclerview.setItemAnimator(new DefaultItemAnimator());
            leader_board_recyclerview.setAdapter(leaderBoardAdapter);

        }
    }


    @Override
    public void main_event_listview_element_clicked(int position, int click_code) {
        System.out.println("position :" + position);



        if (user_type_selection_status == 0) {
            leaderBoardIndList.get(position);
            System.out.println(leaderBoardIndList.get(position).getUserId());


            Intent browseUserIntent = new Intent(getApplicationContext(), BrowseUserProfile.class);
            browseUserIntent.putExtra("user_id",leaderBoardIndList.get(position).getUserId());
            startActivity(browseUserIntent);
        }
        if (user_type_selection_status == 1) {
            leaderBoardGrpList.get(position);
            System.out.println(leaderBoardGrpList.get(position).getUserId());

            Intent browseGrpIntent = new Intent(getApplicationContext(), BrowseGroupProfile.class);
            browseGrpIntent.putExtra("grp_id",leaderBoardGrpList.get(position).getUserId());
            startActivity(browseGrpIntent);

        }

    }
}
