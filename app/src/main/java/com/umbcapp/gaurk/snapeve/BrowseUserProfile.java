package com.umbcapp.gaurk.snapeve;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.umbcapp.gaurk.snapeve.Adapters.LeaderBoardAdapter;
import com.umbcapp.gaurk.snapeve.Adapters.UserContributionAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.LeaderboardListItem;
import com.umbcapp.gaurk.snapeve.Controllers.UserContributionListItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BrowseUserProfile extends AppCompatActivity {

    private TextView user_name_textview;
    private TextView user_points_textview;
    private RelativeLayout profile_relative_layout;
    private ListView user_profile_contribution_list_view;
    private TextView user_grp_name_text_view;
    private ImageView profile_pic_image_view;
    private TextView user_full_name_textview;
    private TextView user_next_level_tv;
    private TextView user_total_posts_textview;
    private ArrayList<UserContributionListItem> userContributionList;
    private UserContributionAdapter userContributionAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.browser_user_profile);

        Intent userId = getIntent();
        String user_id = userId.getStringExtra("user_id");
        System.out.println("BrowseUserProfile user_id : " + user_id);

        user_name_textview = (TextView) findViewById(R.id.user__name);
        user_next_level_tv = (TextView) findViewById(R.id.user_next_level_tv);
        user_points_textview = (TextView) findViewById(R.id.browse_profile_user_points);
        user_total_posts_textview = (TextView) findViewById(R.id.user_total_posts_textview);
        profile_relative_layout = (RelativeLayout) findViewById(R.id.profile_relative_layout);
        user_profile_contribution_list_view = (ListView) findViewById(R.id.user_profile_contribution_list_view);
        user_grp_name_text_view = (TextView) findViewById(R.id.grp_name_text_view);
        profile_pic_image_view = (ImageView) findViewById(R.id.profile_pic_image_view);

        fetchUserProfile(user_id);

    }

    private void fetchUserProfile(String user_id) {
        final ProgressDialog progressDialog = new ProgressDialog(BrowseUserProfile.this);
        progressDialog.setTitle("Loading, Please wait...");
        progressDialog.create();
        progressDialog.show();
        JsonObject jsonObjectParameters = new JsonObject();
        jsonObjectParameters.addProperty("user_id", user_id);

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("fetch_user_details_api", jsonObjectParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                progressDialog.dismiss();
                System.out.println(" browse_user_profile_api exception    " + exception);

            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                progressDialog.dismiss();

                System.out.println(" browse_user_profile_api success response    " + response);
                parseAndDivideUserInfo(response);

//                parseUserDataResponse(response);

            }
        });
    }

    private void parseAndDivideUserInfo(JsonElement response) {

        JsonObject userInfoResponse = response.getAsJsonObject();
        JsonArray userDetails = userInfoResponse.getAsJsonArray("userDetails");
        JsonArray userActivity = userInfoResponse.getAsJsonArray("userActivity");

        parseUserDetails(userDetails);

        System.out.println("userDetails " + userDetails);
        System.out.println("userActivity " + userActivity);

        if (userActivity.size() > 0) {
            parseUserPostdata(userActivity);
        }

    }

    private void parseUserPostdata(JsonArray userActivity) {

        System.out.println("parseUserPostdata " + userActivity);
        userContributionList = new ArrayList<>();

        for (int i = 0; i < userActivity.size(); i++) {
            JsonObject user_activity_list_object = userActivity.get(i).getAsJsonObject();

            System.out.println(" user_activity_list_object  " + user_activity_list_object);

            String user_name = user_activity_list_object.get("user_name").getAsString();
            String first_name = user_activity_list_object.get("first_name").getAsString();
            String last_name = user_activity_list_object.get("last_name").getAsString();
            String description = user_activity_list_object.get("description").getAsString();
            String post_id = user_activity_list_object.get("post_id").getAsString();
            String user_id = user_activity_list_object.get("user_id").getAsString();
            String img_url = user_activity_list_object.get("img_url").getAsString();
            int post_as = user_activity_list_object.get("post_as").getAsInt();


            //getting likes and spam data/count. replace null with 0
            //3 attributes
            int total_likes = 0;
            try {
                total_likes = user_activity_list_object.get("total_likes").getAsInt();
            } catch (Exception e) {
                e.printStackTrace();
            }
            int total_spam = 0;
            try {
                total_spam = user_activity_list_object.get("total_spam").getAsInt();
            } catch (Exception e) {
                e.printStackTrace();
            }
            int total_comments = 0;
            try {
                total_comments = user_activity_list_object.get("total_comments").getAsInt();
            } catch (Exception e) {
                e.printStackTrace();
            }

            String dp_url = null;
            try {
                dp_url = user_activity_list_object.get("dp_url").getAsString();
                System.out.println("userprofilefragment user dp_url : " + dp_url);
            } catch (Exception e) {
                e.printStackTrace();
                dp_url = null;
                System.out.println(" dp_url is null, set local image");
            }

//            DateFormat srcDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            DateFormat srcDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            DateFormat displayDtFormat = new SimpleDateFormat("MMM dd HH:mm");

            Date postDate = null;
            try {
                postDate = srcDateFormat.parse(user_activity_list_object.get("createdAt").getAsString());

                AddFourHours addFourHours = new AddFourHours();
                postDate = addFourHours.addHours(postDate).getCurrent_date();

            } catch (ParseException e) {
                e.printStackTrace();
            }

            //skip adding tem to list if post_as = 2 (anonymous post)
            if (post_as != 2) {
                userContributionList.add(0, new UserContributionListItem(post_id, user_id, user_name, first_name, last_name, displayDtFormat.format(postDate), img_url, dp_url, description, total_likes, total_spam, total_comments, post_as));
            }

            loadContributionList();
        }
    }

    private void loadContributionList() {
        userContributionAdapter = new UserContributionAdapter(getApplicationContext(), userContributionList);
        user_profile_contribution_list_view.setAdapter(userContributionAdapter);

        user_total_posts_textview.setVisibility(View.VISIBLE);
        if (userContributionList.size() == 0) {
            user_total_posts_textview.setText("No Posts Found");
        } else if (userContributionList.size() == 1) {
            user_total_posts_textview.setText("1 Post");
        } else {
            user_total_posts_textview.setText(userContributionList.size() + " Posts");
        }
    }

    private void parseUserDetails(JsonArray userDataJSONArray) {

        String dp_url = null;

        JsonObject userData_list_object = userDataJSONArray.get(0).getAsJsonObject();


        System.out.println(" userData_list_object  " + userData_list_object);

//        String grp_id = userData_list_object.get("grp_id").getAsString();
        String user_name = userData_list_object.get("user_name").getAsString();
//        String first_name = userData_list_object.get("first_name").getAsString();
//        String last_name = userData_list_object.get("last_name").getAsString();
        int user_points = Integer.parseInt(userData_list_object.get("user_points").toString());

        //exception handling for image url null for user
        try {

            dp_url = userData_list_object.get("dp_url").getAsString();
            System.out.println(" dp_url found" + dp_url);
            Picasso.with(getApplicationContext()).load(dp_url).fit().centerCrop().into(profile_pic_image_view);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" dp_url is null, set local image");
            profile_pic_image_view.setImageResource(R.drawable.avatar_100_3);
        }

        String grp_name = null;
        try {
            grp_name = userData_list_object.get("grp_name").getAsString();
            user_grp_name_text_view.setText("Group : " + grp_name);
        } catch (Exception e) {
            e.printStackTrace();
            user_grp_name_text_view.setText("");
        }
        setRewardProgress(user_points);

        System.out.println(" user_name " + user_name);
        System.out.println(" user_points " + user_points);

        user_name_textview.setText(user_name);

    }

    private void setRewardProgress(int reward_points) {
        RewardCalcuator rewardCalcuator = new RewardCalcuator();
        RewardCalcuator calculatedReward = null;

        calculatedReward = rewardCalcuator.calculate((float) reward_points);
        System.out.println("calculatedReward " + calculatedReward.getCurrent_points());
        System.out.println("calculatedReward " + calculatedReward.getMin_range());
        System.out.println("calculatedReward " + calculatedReward.getMax_range());
        System.out.println("calculatedReward " + calculatedReward.getRelativerewardsValue());
        System.out.println("calculatedReward " + calculatedReward.getLevel());

        int pointsToNextLevel = (int) (calculatedReward.getMax_range() - calculatedReward.getCurrent_points());
        pointsToNextLevel++;
        user_points_textview.setText("Level  " + calculatedReward.getLevel() + "  •  " + reward_points + "  Points");
        user_next_level_tv.setText(pointsToNextLevel + "  Points to next level");


    }

    private void parseUserDataResponse(JsonElement response) {
        System.out.println(" IN PARSE JASON");
        String dp_url = null;
        JsonArray userDataJSONArray = response.getAsJsonArray();

        JsonObject userData_list_object = userDataJSONArray.get(0).getAsJsonObject();


        System.out.println(" userData_list_object  " + userData_list_object);

        String grp_id = userData_list_object.get("grp_id").toString();
        String user_name = userData_list_object.get("user_name").toString();
        String grp_name = userData_list_object.get("grp_name").toString();
        String first_name = userData_list_object.get("first_name").toString();
        String last_name = userData_list_object.get("last_name").toString();
        int user_points = Integer.parseInt(userData_list_object.get("user_points").toString());

        //exception handling for image url null for user
        try {

            dp_url = userData_list_object.get("dp_url").getAsString();
            System.out.println(" dp_url found" + dp_url);
            Picasso.with(getApplicationContext()).load(dp_url).fit().centerCrop().into(profile_pic_image_view);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" dp_url is null, set local image");
            profile_pic_image_view.setImageResource(R.drawable.avatar_100_3);
        }
//        String grp_dp_url = userData_list_object.get("grp_dp_url").getAsString();


        System.out.println(" user_name " + user_name);
        System.out.println(" user_points " + user_points);

        //Remove " from start and end from every string
        grp_id = grp_id.substring(1, grp_id.length() - 1);
        user_name = user_name.substring(1, user_name.length() - 1);
        first_name = first_name.substring(1, first_name.length() - 1);
        last_name = last_name.substring(1, last_name.length() - 1);
        grp_name = grp_name.substring(1, grp_name.length() - 1);

        user_name_textview.setText(user_name);
        user_full_name_textview.setText(first_name + " " + last_name);
        user_points_textview.setText(String.valueOf(user_points));
        user_grp_name_text_view.setText("Group : " + grp_name);
        profile_pic_image_view = (ImageView) findViewById(R.id.profile_pic_image_view);

    }

}
