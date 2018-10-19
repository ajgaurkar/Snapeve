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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.umbcapp.gaurk.snapeve.Adapters.GroupMembersAdapter;
import com.umbcapp.gaurk.snapeve.Adapters.TeamMatesAdapter;
import com.umbcapp.gaurk.snapeve.Adapters.UserContributionAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.LeaderboardListItem;
import com.umbcapp.gaurk.snapeve.Controllers.TeammatesListItem;
import com.umbcapp.gaurk.snapeve.Controllers.UserContributionListItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class BrowseGroupProfile extends AppCompatActivity implements Listview_communicator {


    private ArrayList<TeammatesListItem> userProfileList;
    ListView user_profile_contribution_list_view;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView grp_member_count_text_view;
    private TextView browse_grp_profile_no_member_label_text_view;
    private TextView grp_post_count_textview;
    private JsonElement bkupMemberResponse;
    private JsonElement bkupPostResponse;
    private ArrayList<UserContributionListItem> grpContributionList;
    private UserContributionAdapter userContributionAdapter;
    private boolean memberListOpenFlag = false;
    private ImageView grp_member_count_text_view_icon;
    private TextView grp_user_name;
    private TextView grp_points;
    private CircleImageView profile_pic_image_view;
    private TextView grp_status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.browser_grp_profile);
        user_profile_contribution_list_view = (ListView) findViewById(R.id.user_profile_contribution_list_view);
        grp_member_count_text_view = (TextView) findViewById(R.id.grp_member_count_text_view);
        grp_user_name = (TextView) findViewById(R.id.grp_user_name);
        grp_points = (TextView) findViewById(R.id.grp_points);
        grp_status = (TextView) findViewById(R.id.grp_status);
        browse_grp_profile_no_member_label_text_view = (TextView) findViewById(R.id.browse_grp_profile_no_member_label_text_view);
        grp_post_count_textview = (TextView) findViewById(R.id.grp_post_count_textview);
        grp_member_count_text_view_icon = (ImageView) findViewById(R.id.grp_member_count_text_view_icon);
        profile_pic_image_view = (CircleImageView) findViewById(R.id.profile_pic_image_view);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        Intent grpId = getIntent();
        String grp_id = grpId.getStringExtra("grp_id");
        System.out.println("BrowseGroupProfile grp_id : " + grp_id);

        fetchGrpdata(grp_id);
//        fetchGrpdata(grp_id, 1);

        //add 2 listeners to switch between posts and members
        //and call parseMemberdata(bkupMemberResponse); OR parseGrpPostData(bkupPostResponse); accordingly

        grp_member_count_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!memberListOpenFlag) {
                    loadContributionList(1);
                    memberListOpenFlag = true;
                    grp_member_count_text_view_icon.setImageResource(R.drawable.up_arrow_white_24);

                } else {
                    loadContributionList(0);
                    grp_member_count_text_view_icon.setImageResource(R.drawable.down_arrow_white_24);
                    memberListOpenFlag = false;
                }


            }
        });
        user_profile_contribution_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (memberListOpenFlag) {
                    UserContributionListItem selectedContributionListItem = null;

                    selectedContributionListItem = grpContributionList.get(position);

                    Intent eventDetailIntent = new Intent(getApplicationContext(), EventDetails.class);
                    eventDetailIntent.putExtra("user_id", selectedContributionListItem.getUser_id());
                    eventDetailIntent.putExtra("user_name", selectedContributionListItem.getUser_name());
                    eventDetailIntent.putExtra("img_url", selectedContributionListItem.getImage_url());
                    eventDetailIntent.putExtra("comm_time", selectedContributionListItem.getCreated_at_dt_time());
                    eventDetailIntent.putExtra("user_comment", selectedContributionListItem.getDescription());
                    eventDetailIntent.putExtra("post_id", selectedContributionListItem.getPost_id());
                    eventDetailIntent.putExtra("user_dp_url", selectedContributionListItem.getDp_url());
                    eventDetailIntent.putExtra("intent_type", 0);
                    startActivity(eventDetailIntent);
                }
            }
        });
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
        grp_points.setText("Level  " + calculatedReward.getLevel() + "  •  " + reward_points + "  Points");
        grp_status.setText(pointsToNextLevel + "  Points to next level");


    }

    private void fetchGrpdata(String grp_id) {
        final ProgressDialog progressDialog = new ProgressDialog(BrowseGroupProfile.this);
        progressDialog.setTitle("Loading, Please wait...");
        progressDialog.create();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        JsonObject jsonObjectParameters = new JsonObject();
        jsonObjectParameters.addProperty("grp_id", grp_id);
//        jsonObjectParameters.addProperty("fetch_type_post_or_members", fetch_type_post_or_members);

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
                divideGrpAndPostData(response);

            }
        });
    }

    private void divideGrpAndPostData(JsonElement response) {

        JsonObject grpInfoResponse = response.getAsJsonObject();
        JsonArray grpMembers = grpInfoResponse.getAsJsonArray("grp_members");
        JsonArray grpPost = grpInfoResponse.getAsJsonArray("grp_post");
        JsonArray grpInfo = grpInfoResponse.getAsJsonArray("grp_info");

        System.out.println("grpInfo" + grpInfo);
        parseGrpInfo(grpInfo);

        System.out.println("grpMembers" + grpMembers);
        //initialize list before(just in case data is 0)
        userProfileList = new ArrayList<>();
        if (grpMembers.size() > 0) {
            parseMemberdata(grpMembers);
        }

        System.out.println("grpPost " + grpPost);
        //initialize list before(just in case data is 0)
        grpContributionList = new ArrayList<>();
        if (grpPost.size() > 0) {
            parsePostdata(grpPost);
        }
    }

    private void parseGrpInfo(JsonArray grpInfo) {
        JsonObject grp_info_list_object = grpInfo.get(0).getAsJsonObject();
        String this_grp_name = grp_info_list_object.get("grp_name").getAsString();

        grp_user_name.setText(this_grp_name);

        int this_grp_points = 0;
        try {
            this_grp_points = grp_info_list_object.get("total_pts").getAsInt();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        grp_points.setText(String.valueOf(this_grp_points));

        setRewardProgress(this_grp_points);


        String this_grp_dp_url = null;
        try {
            this_grp_dp_url = grp_info_list_object.get("grp_dp_url").getAsString();
            Picasso.with(getApplicationContext()).load(this_grp_dp_url)
                    .fit().centerCrop().into(profile_pic_image_view);
        } catch (Exception e) {
            e.printStackTrace();
            profile_pic_image_view.setImageResource(R.drawable.avatar_100_3);
        }

    }

    private void parsePostdata(JsonArray grpPost) {

        System.out.println("parsePostdata " + grpPost);

        for (int i = 0; i < grpPost.size(); i++) {
            JsonObject grpPost_Data_list_object = grpPost.get(i).getAsJsonObject();

            System.out.println(" grpPost_Data_list_object  " + grpPost_Data_list_object);

            String member_id = grpPost_Data_list_object.get("member_id").getAsString();
            String first_name = grpPost_Data_list_object.get("first_name").getAsString();
            String last_name = grpPost_Data_list_object.get("last_name").getAsString();
            String user_name = grpPost_Data_list_object.get("user_name").getAsString();
            String post_id = grpPost_Data_list_object.get("post_id").getAsString();
            String post_date = grpPost_Data_list_object.get("createdAt").getAsString();
            String description = grpPost_Data_list_object.get("description").getAsString();
            String img_url = grpPost_Data_list_object.get("img_url").getAsString();
            int post_as = grpPost_Data_list_object.get("post_as").getAsInt();

            //getting likes and spam data/count. replace null with 0
            //3 attributes
            int total_likes = 0;
            try {
                total_likes = grpPost_Data_list_object.get("total_likes").getAsInt();
            } catch (Exception e) {
                e.printStackTrace();
            }
            int total_spam = 0;
            try {
                total_spam = grpPost_Data_list_object.get("total_spam").getAsInt();
            } catch (Exception e) {
                e.printStackTrace();
            }
            int total_comments = 0;
            try {
                total_comments = grpPost_Data_list_object.get("total_comments").getAsInt();
            } catch (Exception e) {
                e.printStackTrace();
            }

            String dp_url = null;
            try {
                dp_url = grpPost_Data_list_object.get("dp_url").getAsString();
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
                postDate = srcDateFormat.parse(grpPost_Data_list_object.get("createdAt").getAsString());

                AddFourHours addFourHours = new AddFourHours();
                postDate = addFourHours.addHours(postDate).getCurrent_date();

            } catch (ParseException e) {
                e.printStackTrace();
            }

            //skip adding tem to list if post_as = 2 (anonymous post)
            if (post_as != 2) {
                grpContributionList.add(0, new UserContributionListItem(post_id, member_id, user_name, first_name, last_name, displayDtFormat.format(postDate), img_url, dp_url, description, total_likes, total_spam, total_comments, post_as));
            }

        }
        loadContributionList(0);
    }

    private void parseMemberdata(JsonArray grpMembers) {
        System.out.println(" IN PARSE JASON");

        userProfileList = new ArrayList<>();
        int max_user_points = 0;
        for (int i = 0; i < grpMembers.size(); i++) {
            JsonObject userData_list_object = grpMembers.get(i).getAsJsonObject();

            System.out.println(" userData_list_object  " + userData_list_object);

//            user_id = userData_list_object.get("user_id").toString();

//            System.out.println("user_id NOT NULL : " + user_id);
            String user_name = userData_list_object.get("user_name").getAsString();
            String user_id = userData_list_object.get("user_id").getAsString();
            String first_name = userData_list_object.get("first_name").getAsString();
            String last_name = userData_list_object.get("last_name").getAsString();
            int user_points = userData_list_object.get("user_points").getAsInt();

            String dp_url = null;
            try {
                dp_url = userData_list_object.get("dp_url").getAsString();
                System.out.println("userprofilefragment user dp_url : " + dp_url);
            } catch (Exception e) {
                e.printStackTrace();
                dp_url = null;
                System.out.println(" dp_url is null, set local image");
            }

            System.out.println(" user_name " + user_name);
            System.out.println(" user_points " + user_points);
            System.out.println(" dp_url " + dp_url);
            System.out.println(" first_name " + first_name);
            System.out.println(" last_name " + last_name);

            if (user_points > max_user_points) {
                max_user_points = user_points;
                userProfileList.add(0, new TeammatesListItem(user_id, user_name, user_points, first_name + " " + last_name, dp_url));
            } else {
                userProfileList.add(new TeammatesListItem(user_id, user_name, user_points, first_name + " " + last_name, dp_url));
            }

        }


        System.out.println("userProfileList size after parsing : " + userProfileList.size());

    }

    private void loadContributionList(int user_type_selection_status) {

        if (user_type_selection_status == 1) {
            userContributionAdapter = new UserContributionAdapter(getApplicationContext(), grpContributionList);
            user_profile_contribution_list_view.setAdapter(userContributionAdapter);

//            if (userProfileList.size() == 0) {
//                grp_member_count_text_view.setVisibility(View.GONE);
//                browse_grp_profile_no_member_label_text_view.setVisibility(View.VISIBLE);
//                grp_member_count_text_view.setText("No Members Found");
//            } else if (userProfileList.size() == 1) {
//                grp_member_count_text_view.setVisibility(View.VISIBLE);
//                grp_member_count_text_view.setText(userProfileList.size() + " Member");
//                browse_grp_profile_no_member_label_text_view.setVisibility(View.INVISIBLE);
//
//            } else {
//                grp_member_count_text_view.setVisibility(View.VISIBLE);
//                grp_member_count_text_view.setText(userProfileList.size() + " Members");
//                browse_grp_profile_no_member_label_text_view.setVisibility(View.INVISIBLE);
//
//            }
//
//            grp_post_count_textview.setVisibility(View.VISIBLE);
//            if (grpContributionList.size() == 0) {
//                grp_post_count_textview.setText("");
//                browse_grp_profile_no_member_label_text_view.setVisibility(View.VISIBLE);
//                grp_member_count_text_view.setText("No Group Posts Found");
//            } else if (grpContributionList.size() == 1) {
//                grp_post_count_textview.setText("1 Post");
//                browse_grp_profile_no_member_label_text_view.setVisibility(View.INVISIBLE);
//            } else {
//                browse_grp_profile_no_member_label_text_view.setVisibility(View.INVISIBLE);
//                grp_post_count_textview.setText(grpContributionList.size() + " Group Posts");
//            }

        }
        if (user_type_selection_status == 0) {

            TeamMatesAdapter teamMatesAdapter = new TeamMatesAdapter(getApplicationContext(), userProfileList);
            user_profile_contribution_list_view.setAdapter(teamMatesAdapter);
            grp_post_count_textview.setVisibility(View.GONE);

        }


        if (userProfileList.size() == 0) {
            grp_member_count_text_view.setVisibility(View.GONE);
            browse_grp_profile_no_member_label_text_view.setVisibility(View.VISIBLE);
            grp_member_count_text_view.setText("No Members Found");
        } else if (userProfileList.size() == 1) {
            grp_member_count_text_view.setVisibility(View.VISIBLE);
            grp_member_count_text_view.setText(userProfileList.size() + " Member");
            browse_grp_profile_no_member_label_text_view.setVisibility(View.INVISIBLE);

        } else {
            grp_member_count_text_view.setVisibility(View.VISIBLE);
            grp_member_count_text_view.setText(userProfileList.size() + " Members");
            browse_grp_profile_no_member_label_text_view.setVisibility(View.INVISIBLE);

        }

        grp_post_count_textview.setVisibility(View.VISIBLE);
        if (grpContributionList.size() == 0) {
            grp_post_count_textview.setText("");
            browse_grp_profile_no_member_label_text_view.setVisibility(View.VISIBLE);
            grp_post_count_textview.setText("No Group Posts Found");
        } else if (grpContributionList.size() == 1) {
            grp_post_count_textview.setText("1 Post");
            browse_grp_profile_no_member_label_text_view.setVisibility(View.INVISIBLE);
        } else {
            browse_grp_profile_no_member_label_text_view.setVisibility(View.INVISIBLE);
            grp_post_count_textview.setText(grpContributionList.size() + " Group Posts");
        }
    }


//    private void parseMemberdata(JsonElement response) {
//        System.out.println(" IN PARSE JASON");
//
//        userProfileList = new ArrayList<>();
//        JsonArray userDataJSONArray = response.getAsJsonArray();
//        int max_user_points = 0;
//        for (int i = 0; i < userDataJSONArray.size(); i++) {
//            JsonObject userData_list_object = userDataJSONArray.get(i).getAsJsonObject();
//
//            System.out.println(" userData_list_object  " + userData_list_object);
//
//            String user_id = userData_list_object.get("user_id").toString();
//
//            if (user_id == null || user_id.length() == 0 || user_id.equals("null")) {
////            if (TextUtils.isEmpty(user_id)) {
//                System.out.println("user_id NULL : " + user_id);
//            } else {
//
//                System.out.println("user_id NOT NULL : " + user_id);
//                String user_name = userData_list_object.get("user_name").toString();
//                String dp_url = userData_list_object.get("dp_url").toString();
//                String first_name = userData_list_object.get("first_name").toString();
//                String last_name = userData_list_object.get("last_name").toString();
//                int user_points = Integer.parseInt(userData_list_object.get("user_points").toString());
//                if (user_points > max_user_points) {
//                    max_user_points = user_points;
//                }
//
//                System.out.println(" user_name " + user_name);
//                System.out.println(" user_points " + user_points);
//                System.out.println(" dp_url " + dp_url);
//                System.out.println(" first_name " + first_name);
//                System.out.println(" last_name " + last_name);
//
//                //Remove " from start and end from every string
//                user_name = user_name.substring(1, user_name.length() - 1);
//                first_name = first_name.substring(1, first_name.length() - 1);
//                last_name = last_name.substring(1, last_name.length() - 1);
//                dp_url = dp_url.substring(1, dp_url.length() - 1);
//                user_id = user_id.substring(1, user_id.length() - 1);
//                userProfileList.add(new TeammatesListItem(user_id, user_name, user_points, "", dp_url));
//
//            }
//        }
//
//        GroupMembersAdapter groupMembersAdapter = new GroupMembersAdapter(BrowseGroupProfile.this, userProfileList, max_user_points);
//        user_profile_contribution_rec_view.setLayoutManager(mLayoutManager);
//        user_profile_contribution_rec_view.setItemAnimator(new DefaultItemAnimator());
//        user_profile_contribution_rec_view.setAdapter(groupMembersAdapter);
//
//        if (userProfileList.size() == 0) {
//            browse_grp_profile_no_member_label_text_view.setVisibility(View.VISIBLE);
//            grp_member_count_text_view.setText(userProfileList.size() + " Members");
//        } else if (userProfileList.size() == 1) {
//            browse_grp_profile_no_member_label_text_view.setVisibility(View.GONE);
//            grp_member_count_text_view.setText(userProfileList.size() + " Member");
//        } else {
//            browse_grp_profile_no_member_label_text_view.setVisibility(View.GONE);
//            grp_member_count_text_view.setText(userProfileList.size() + " Members");
//        }
//
//    }

    @Override
    public void main_event_listview_element_clicked(int position, int click_code) {
        userProfileList.get(position);
        System.out.println(userProfileList.get(position).getUserId());
    }

}
