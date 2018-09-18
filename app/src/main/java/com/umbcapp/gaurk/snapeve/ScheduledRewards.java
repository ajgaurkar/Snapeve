package com.umbcapp.gaurk.snapeve;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.umbcapp.gaurk.snapeve.Adapters.TopScorerAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.LeaderboardListItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class ScheduledRewards extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ListView topScorerlistView;
    private CircleImageView scheduled_rewards_profile_pic_image_view;
    private TextView scheduled_rewards_user_fullname_name_textview;
    private TextView scheduled_rewards_user_name_textview;
    private RelativeLayout scheduled_rewards_main_layout;
    private KonfettiView konfettiView;
    private int width;
    private ArrayList<LeaderboardListItem> individualScorerList;
    private ArrayList<LeaderboardListItem> grpScorerList;
    private ArrayList<LeaderboardListItem> teamMembersList;
    private TextView scheduled_rewards_runnerup_label;
    private long sessionCounter = 0;
    private int currentSelectedTab = 1;
    private TextView scheduled_rewards_date_range_textview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionCounter = System.currentTimeMillis();
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String text = formatter.format(new Date(sessionCounter));

        setContentView(R.layout.scheduled_rewards);

        topScorerlistView = (ListView) findViewById(R.id.scheduled_rewards_scorer_list);
        scheduled_rewards_profile_pic_image_view = (CircleImageView) findViewById(R.id.scheduled_rewards_profile_pic_image_view);
        scheduled_rewards_main_layout = (RelativeLayout) findViewById(R.id.scheduled_rewards_main_layout);
        scheduled_rewards_user_fullname_name_textview = (TextView) findViewById(R.id.scheduled_rewards_user_fullname_name_textview);
        scheduled_rewards_user_name_textview = (TextView) findViewById(R.id.scheduled_rewards_user_name_textview);
        scheduled_rewards_date_range_textview = (TextView) findViewById(R.id.scheduled_rewards_date_range_textview);
        scheduled_rewards_runnerup_label = (TextView) findViewById(R.id.scheduled_rewards_runnerup_label);
        konfettiView = findViewById(R.id.viewKonfetti);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.scheduled_rewards_bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        System.out.println("grphhh " + new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_GRP_ID));
        if (new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_GRP_ID) == null
                || new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_GRP_ID).equals("xxxxx____xxxxx")) {
            navigation.getMenu().removeItem(R.id.rewards_navigation_team);
        }

        konfettiView.post(new Runnable() {
            @Override
            public void run() {
                width = konfettiView.getMeasuredWidth();
                getTopScorers();
                //method called in runnable because width is calculated after view load. hence width come to 0 if confetti called before/directly
            }
        });
        System.out.println("scheduled_rewards_main_layout width() " + width);

    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.rewards_navigation_user:
                    calculateSession(currentSelectedTab);

                    System.out.print("rewards_navigation_group");
                    currentSelectedTab = 1;
                    showWinners(currentSelectedTab);


                    return true;

                case R.id.rewards_navigation_group:
                    calculateSession(currentSelectedTab);

                    currentSelectedTab = 2;
                    showWinners(currentSelectedTab);
                    System.out.print("rewards_navigation_group");


                    return true;

                case R.id.rewards_navigation_team:
                    calculateSession(currentSelectedTab);

                    currentSelectedTab = 3;
                    showWinners(currentSelectedTab);
                    System.out.print("rewards_navigation_team");


                    return true;
            }
            return false;
        }
    };

    private void calculateSession(int currentSelectedTab) {


        sessionCounter = System.currentTimeMillis() - sessionCounter;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(sessionCounter);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(sessionCounter);


        //reset counter to current time
        sessionCounter = System.currentTimeMillis();

        switch (currentSelectedTab) {
            case 1:
                System.out.println("WR1 : " + minutes + "m " + seconds + "s");
                break;

            case 2:
                System.out.println("SCHEDULEDREWARDS 2 onPause sessionCounter : " + minutes + "m " + seconds + "s");
                break;

            case 3:
                System.out.println("SCHEDULEDREWARDS 3 onPause sessionCounter : " + minutes + "m " + seconds + "s");
                break;
        }
    }

    private void showWinners(int winnerType) {
        TopScorerAdapter topScorerAdapter = null;
        ArrayList<LeaderboardListItem> tempList = null;
        scheduled_rewards_date_range_textview.setText("Sep 09 to Sep 16");
        switch (winnerType) {
            case 1:
                System.out.println("IN 1");

                tempList = new ArrayList<>(individualScorerList);
                //setting top person values
                if (tempList.get(0).getUserPicUrl() == null) {
                    scheduled_rewards_profile_pic_image_view.setImageResource(R.drawable.avatar_100_3);

                } else {
                    Picasso.with(getApplicationContext()).load(tempList.get(0).getUserPicUrl()).fit().centerCrop().into(scheduled_rewards_profile_pic_image_view);
                }
                scheduled_rewards_user_fullname_name_textview.setText(tempList.get(0).getUserName());
                scheduled_rewards_user_name_textview.setText("Score this week : " + tempList.get(0).getUserRank());

                //removing top person from list
                tempList.remove(0);

                if (tempList.size() > 0) {
                    topScorerAdapter = new TopScorerAdapter(getApplicationContext(), tempList);
                    scheduled_rewards_runnerup_label.setText("Runner Ups");

                } else {
                    scheduled_rewards_runnerup_label.setText("No Runner Ups");
                    System.out.println("NO USER RUNNER UP TO SHOW");
                }

                break;
            case 2:
                System.out.println("IN 2");
                tempList = new ArrayList<>(grpScorerList);
                //setting top person values
                if (tempList.get(0).getUserPicUrl() == null) {
                    scheduled_rewards_profile_pic_image_view.setImageResource(R.drawable.avatar_100_3);
                } else {
                    Picasso.with(getApplicationContext()).load(tempList.get(0).getUserPicUrl()).fit().centerCrop().into(scheduled_rewards_profile_pic_image_view);
                }
                scheduled_rewards_user_fullname_name_textview.setText(tempList.get(0).getUserName());
                scheduled_rewards_user_name_textview.setText("Score this week : " + tempList.get(0).getUserRank());

                //removing top person from list
                tempList.remove(0);

                if (tempList.size() > 0) {
                    topScorerAdapter = new TopScorerAdapter(getApplicationContext(), tempList);
                    scheduled_rewards_runnerup_label.setText("Runner Ups");
                } else {
                    scheduled_rewards_runnerup_label.setText("No Runner Ups");
                    System.out.println("NO GRP RUNNER UP TO SHOW");
                }
                break;
            case 3:
                System.out.println("IN 3");
                tempList = new ArrayList<>(teamMembersList);
                //setting top person values
                if (tempList.get(0).getUserPicUrl() == null) {
                    scheduled_rewards_profile_pic_image_view.setImageResource(R.drawable.avatar_100_3);
                } else {
                    Picasso.with(getApplicationContext()).load(tempList.get(0).getUserPicUrl()).fit().centerCrop().into(scheduled_rewards_profile_pic_image_view);
                }
                scheduled_rewards_user_fullname_name_textview.setText(tempList.get(0).getUserName());
                scheduled_rewards_user_name_textview.setText("Score this week : " + tempList.get(0).getUserRank());

                //removing top person from list
                tempList.remove(0);

                if (tempList.size() > 0) {
                    topScorerAdapter = new TopScorerAdapter(getApplicationContext(), tempList);
                    scheduled_rewards_runnerup_label.setText("Runner Ups");
                } else {
                    scheduled_rewards_runnerup_label.setText("No Runner Ups");
                    System.out.println("NO TEAM MEMBER RUNNER UP TO SHOW");
                }
                break;
        }

        topScorerlistView.setAdapter(topScorerAdapter);

    }

    private void getTopScorers() {
        System.out.println("IN fetch_post_comments_api");
        progressDialog = new ProgressDialog(ScheduledRewards.this);
        progressDialog.setMessage("Getting winners");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.create();
        progressDialog.show();

        JsonObject jsonObjectPostEventParameters = new JsonObject();

        //setting grp_id_param to "NULL" for api
        String grp_id_param = new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_GRP_ID);
        if (grp_id_param == null || grp_id_param.isEmpty() || grp_id_param.equals("null") || grp_id_param.equals("xxxxx____xxxxx")) {
            grp_id_param = "NULL";
        }
        jsonObjectPostEventParameters.addProperty("grp_id", grp_id_param);

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("Fetch_weekly_winners_api", jsonObjectPostEventParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                Toast.makeText(getApplicationContext(), "Something went wrong. Try again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                System.out.println(" Fetch_weekly_winners_api exception    " + exception);

            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                System.out.println(" Fetch_weekly_winners_api success response    " + response);
                progressDialog.dismiss();
                divideRewardsData(response);
            }
        });

        setConfetti();
    }

    //method called in runnable because width is calculated after view load. hence width come to 0 if confetti called before/directly
    private void setConfetti() {
        konfettiView.build()
                .addColors(Color.RED, Color.WHITE, Color.GREEN, Color.YELLOW)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(800L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(16, 5f))
                .setPosition(0f, (float) width, 0f, 0f)
                .streamFor(30, 50000L);

    }

    private void divideRewardsData(JsonElement response) {

        JsonObject rewardsResponse = response.getAsJsonObject();

        JsonArray individuals = null;
        individualScorerList = new ArrayList<>();
        try {
            individuals = rewardsResponse.getAsJsonArray("individualList");
            System.out.println("individuals" + individuals);
            //initialize list before(just in case data is 0)
            if (individuals.size() > 0) {
                parseIndividuals(individuals);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonArray groups = null;
        grpScorerList = new ArrayList<>();
        try {
            groups = rewardsResponse.getAsJsonArray("groupList");
            System.out.println("groups " + groups);
            //initialize list before(just in case data is 0)
            if (groups.size() > 0) {
                parseGroups(groups);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonArray teamMems = null;
        //initialize list before(just in case data is 0)
        teamMembersList = new ArrayList<>();
        try {
            teamMems = rewardsResponse.getAsJsonArray("teamMembers");
            System.out.println("teamMems " + teamMems);
            if (teamMems.size() > 0) {
                parseTeamMems(teamMems);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        showWinners(1);

    }

    private void parseTeamMems(JsonArray teamMems) {
        teamMembersList = new ArrayList<LeaderboardListItem>();

        for (int j = 0; j < teamMems.size(); j++) {
            JsonObject topscorer_list_object = teamMems.get(j).getAsJsonObject();
            System.out.println(" topscorer_list_object " + topscorer_list_object);

            String dp_url = null;
            String userId = topscorer_list_object.get("user_id").getAsString();
            String userName = topscorer_list_object.get("user_name").getAsString();
            String first_name = topscorer_list_object.get("first_name").getAsString();
            String last_name = topscorer_list_object.get("last_name").getAsString();
            String userGroup = "XX";
            int userRank = topscorer_list_object.get("user_points").getAsInt();

            try {
                dp_url = topscorer_list_object.get("dp_url").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            if (j == 0) {
//
//                if (dp_url == null) {
//                    scheduled_rewards_profile_pic_image_view.setImageResource(R.drawable.avatar_100_3);
//
//                } else {
//                    Picasso.get().load(dp_url).fit().centerCrop().into(scheduled_rewards_profile_pic_image_view);
//                }
//                scheduled_rewards_user_fullname_name_textview.setText(first_name + " " + last_name);
//                scheduled_rewards_user_name_textview.setText(userName);
//
//            } else {
            teamMembersList.add(new LeaderboardListItem(0, userId, userName, userRank, userGroup, dp_url));
//
//            }

        }

    }

    private void parseGroups(JsonArray groups) {
        grpScorerList = new ArrayList<LeaderboardListItem>();

        for (int j = 0; j < groups.size(); j++) {
            JsonObject topscorer_list_object = groups.get(j).getAsJsonObject();
            System.out.println(" topscorer_list_object " + topscorer_list_object);

            String grp_dp_url = null;
            String grp_id = topscorer_list_object.get("grp_id").getAsString();
            String grp_name = topscorer_list_object.get("grp_name").getAsString();

            //redundant and useless field
            String userGroup = "XX";

            int grp_points = topscorer_list_object.get("grp_points").getAsInt();

            try {
                grp_dp_url = topscorer_list_object.get("grp_dp_url").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            if (j == 0) {
//
//                if (dp_url == null) {
//                    scheduled_rewards_profile_pic_image_view.setImageResource(R.drawable.avatar_100_3);
//
//                } else {
//                    Picasso.get().load(dp_url).fit().centerCrop().into(scheduled_rewards_profile_pic_image_view);
//                }
//                scheduled_rewards_user_fullname_name_textview.setText(first_name + " " + last_name);
//                scheduled_rewards_user_name_textview.setText(userName);
//
//            } else {
            grpScorerList.add(new LeaderboardListItem(0, grp_id, grp_name, grp_points, userGroup, grp_dp_url));
//
//            }

        }
    }

    private void parseIndividuals(JsonArray individuals) {
        individualScorerList = new ArrayList<LeaderboardListItem>();

        for (int j = 0; j < individuals.size(); j++) {
            JsonObject topscorer_list_object = individuals.get(j).getAsJsonObject();
            System.out.println(" topscorer_list_object " + topscorer_list_object);

            String dp_url = null;
            String userId = topscorer_list_object.get("user_id").getAsString();
            String userName = topscorer_list_object.get("user_name").getAsString();
            String first_name = topscorer_list_object.get("first_name").getAsString();
            String last_name = topscorer_list_object.get("last_name").getAsString();
            String userGroup = "XX";
            int userRank = topscorer_list_object.get("user_points").getAsInt();

            try {
                dp_url = topscorer_list_object.get("dp_url").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            if (j == 0) {
//
//                if (dp_url == null) {
//                    scheduled_rewards_profile_pic_image_view.setImageResource(R.drawable.avatar_100_3);
//
//                } else {
//                    Picasso.get().load(dp_url).fit().centerCrop().into(scheduled_rewards_profile_pic_image_view);
//                }
//                scheduled_rewards_user_fullname_name_textview.setText(first_name + " " + last_name);
//                scheduled_rewards_user_name_textview.setText(userName);
//
//            } else {
            individualScorerList.add(new LeaderboardListItem(0, userId, userName, userRank, userGroup, dp_url));
//
//            }

        }

    }

    private void parseTopScorerResponse(JsonElement topScorerResponse) {

        individualScorerList = new ArrayList<LeaderboardListItem>();

        System.out.println(" IN PARSE JASON");
        JsonArray topScorerJSONArray = topScorerResponse.getAsJsonArray();

        for (int j = 0; j < topScorerJSONArray.size(); j++) {
            JsonObject topscorer_list_object = topScorerJSONArray.get(j).getAsJsonObject();
            System.out.println(" topscorer_list_object " + topscorer_list_object);

            String dp_url = null;
            String userId = topscorer_list_object.get("user_id").getAsString();
            String userName = topscorer_list_object.get("user_name").getAsString();
            String first_name = topscorer_list_object.get("first_name").getAsString();
            String last_name = topscorer_list_object.get("last_name").getAsString();
            String userGroup = "XX";
            int userRank = topscorer_list_object.get("user_points").getAsInt();

            try {
                dp_url = topscorer_list_object.get("dp_url").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (j == 0) {

                if (dp_url == null) {
                    scheduled_rewards_profile_pic_image_view.setImageResource(R.drawable.avatar_100_3);

                } else {
                    Picasso.with(getApplicationContext()).load(dp_url).fit().centerCrop().into(scheduled_rewards_profile_pic_image_view);
                }
                scheduled_rewards_user_fullname_name_textview.setText(first_name + " " + last_name);
                scheduled_rewards_user_name_textview.setText(userName);

            } else {
                individualScorerList.add(new LeaderboardListItem(0, userId, userName, userRank, userGroup, dp_url));

            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();

        calculateSession(currentSelectedTab);

    }

    @Override
    public void onResume() {
        sessionCounter = System.currentTimeMillis();
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String text = formatter.format(new Date(sessionCounter));

        super.onResume();
    }

}
