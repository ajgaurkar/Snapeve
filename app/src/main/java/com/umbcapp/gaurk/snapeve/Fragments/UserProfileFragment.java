package com.umbcapp.gaurk.snapeve.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.umbcapp.gaurk.snapeve.Adapters.UserContributionAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.CommentsListItem;
import com.umbcapp.gaurk.snapeve.CreateGruoups;
import com.umbcapp.gaurk.snapeve.EventDetails;
import com.umbcapp.gaurk.snapeve.Leaderboard;
import com.umbcapp.gaurk.snapeve.MainActivity;
import com.umbcapp.gaurk.snapeve.R;
import com.umbcapp.gaurk.snapeve.SessionManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class UserProfileFragment extends Fragment {

    private JsonObject jsonObjectUserProfileFragParameters;
    private ViewPager pager;
    private TabLayout tabLayout;
    private ListView user_profile_list_view;
    private TextView grp_profile_btn;
    private TextView user_profile_btn;
    private RelativeLayout profile_relative_layout;
    private ArrayList<CommentsListItem> contributionList;
    int user_type_selection_status = 0;
    private ListView user_profile_contribution_list_view;
    private UserContributionAdapter userContributionAdapter;
    private ImageView leaderboard_imageview;
    private ImageView profile_pic_image_view;
    private TextView leaderboard_text_view;
    private ImageView user_profile_settings_imageview;

    long sessionCounter = 0;
    private String temp_user_points;
    private String userName;
    private String dp_url;
    private String first_name;
    private String last_name;
    private String grp_id;
    private String grp_name;
    private String grp_dp_url;
    private String temp_total_pts;
    private TextView user_name_textview;
    private int grp_total_pts;
    private int user_total_pts;
    private TextView user_points;

    public UserProfileFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contributionList = new ArrayList<>();
        fetch_user_details();
//        fetch_group_details();

        sessionCounter = System.currentTimeMillis();
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String text = formatter.format(new Date(sessionCounter));
        System.out.println("Start @ sessionCounter : " + text);

    }

    private void fetch_group_details() {
        jsonObjectUserProfileFragParameters = new JsonObject();
        jsonObjectUserProfileFragParameters.addProperty("studentId", "check123");

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("group_details_api", jsonObjectUserProfileFragParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                System.out.println(" fetch_group_details exception    " + exception);
            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                System.out.println(" fetch_group_details success response    " + response);
            }
        });
    }

    private void fetch_user_details() {
        jsonObjectUserProfileFragParameters = new JsonObject();
        jsonObjectUserProfileFragParameters.addProperty("user_name", new SessionManager(getActivity()).getSpecificUserDetail(SessionManager.KEY_USER_NAME));

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("fetch_user_details_api", jsonObjectUserProfileFragParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                System.out.println(" fetch_user_details exception    " + exception);
            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                System.out.println(" fetch_user_details success response    " + response);

                parseResponse(response);
            }
        });
    }

    private void parseResponse(JsonElement response) {
        JsonArray responseJsonArray = response.getAsJsonArray();

        JsonObject userDetailsObj = responseJsonArray.get(0).getAsJsonObject();

        userName = userDetailsObj.get("user_name").toString();
        dp_url = userDetailsObj.get("user_name").toString();
        first_name = userDetailsObj.get("first_name").toString();
        last_name = userDetailsObj.get("last_name").toString();
        grp_id = userDetailsObj.get("grp_id").toString();
        grp_name = userDetailsObj.get("grp_name").toString();
        grp_dp_url = userDetailsObj.get("grp_dp_url").toString();
        grp_total_pts = Integer.parseInt(userDetailsObj.get("total_pts").toString());
        user_total_pts = Integer.parseInt(userDetailsObj.get("user_points").toString());

        userName = userName.substring(1, userName.length() - 1);
        dp_url = dp_url.substring(1, dp_url.length() - 1);
        first_name = first_name.substring(1, first_name.length() - 1);
        last_name = last_name.substring(1, last_name.length() - 1);
        grp_id = grp_id.substring(1, grp_id.length() - 1);
        grp_name = grp_name.substring(1, grp_name.length() - 1);
        grp_dp_url = grp_dp_url.substring(1, grp_dp_url.length() - 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.userprofile, container, false);

        grp_profile_btn = (TextView) rootView.findViewById(R.id.profile_switch_grp_textview);
        user_name_textview = (TextView) rootView.findViewById(R.id.user_name);
        user_profile_btn = (TextView) rootView.findViewById(R.id.profile_switch_you_textview);
        user_points = (TextView) rootView.findViewById(R.id.user_points);
        user_profile_list_view = (ListView) rootView.findViewById(R.id.user_profile_contribution_list_view);
        profile_relative_layout = (RelativeLayout) rootView.findViewById(R.id.profile_relative_layout);
        user_profile_contribution_list_view = (ListView) rootView.findViewById(R.id.user_profile_contribution_list_view);
        leaderboard_text_view = (TextView) rootView.findViewById(R.id.leaderboard_text_view);
        profile_pic_image_view = (ImageView) rootView.findViewById(R.id.profile_pic_image_view);
        user_profile_settings_imageview = (ImageView) rootView.findViewById(R.id.user_profile_settings_imageview);

//        Picasso.get().load("https://www.goldenglobes.com/sites/default/files/styles/portrait_medium/public/gallery_images/17-tomcruiseag.jpg?itok=qNj0cQGV&c=c9a73b7bdf609d72214d226ab9ea015e")
//                .fit().centerCrop().into(profile_pic_image_view);

        loadContributionList(0);

        user_profile_contribution_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                CommentsListItem selectedCommentsListItem = contributionList.get(position);
                Intent eventDetailIntent = new Intent(getActivity(), EventDetails.class);
                eventDetailIntent.putExtra("user_id", selectedCommentsListItem.getUser_id());
                eventDetailIntent.putExtra("user_name", selectedCommentsListItem.getUser_name());
                eventDetailIntent.putExtra("img_url", selectedCommentsListItem.getImage_url());
                eventDetailIntent.putExtra("comm_time", selectedCommentsListItem.getComment_time());
                eventDetailIntent.putExtra("user_comment", selectedCommentsListItem.getUser_comment());
                startActivity(eventDetailIntent);

            }
        });

        profile_relative_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        user_profile_settings_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateGruoups.class));
            }
        });

        leaderboard_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getActivity(), Leaderboard.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt("Profile_type", user_type_selection_status);
                mIntent.putExtras(mBundle);
                startActivity(mIntent);
            }
        });

        user_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_profile_btn.setBackground(getResources().getDrawable(R.drawable.text_selection_left_seleted));
                grp_profile_btn.setBackground(getResources().getDrawable(R.drawable.text_selection_right_unseleted));
                user_type_selection_status = 0;
                Picasso.get().load(dp_url)
                        .fit().centerCrop().into(profile_pic_image_view);
                loadContributionList(user_type_selection_status);
                user_name_textview.setText(userName);
                user_points.setText(String.valueOf(user_total_pts));
            }
        });

        grp_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grp_profile_btn.setBackground(getResources().getDrawable(R.drawable.text_selection_right_seleted));
                user_profile_btn.setBackground(getResources().getDrawable(R.drawable.text_selection_left_unseleted));
                user_type_selection_status = 1;
                Picasso.get().load(grp_dp_url)
                        .fit().centerCrop().into(profile_pic_image_view);
                loadContributionList(user_type_selection_status);
                user_name_textview.setText(grp_name);
                user_points.setText(String.valueOf(grp_total_pts));
            }
        });

        return rootView;
    }

    private void loadContributionList(int user_type_selection_status) {

        if (user_type_selection_status == 0) {
            contributionList = new ArrayList<>();
            contributionList.add(new CommentsListItem("u1", "Jhon Paul", "speaking to a a packed crowd at Sanders Theatre, a nobel laureate discussing her most recent scientific discovery, or the Harvard senior talent show, there’s always something happening at Harvard.", "Jan 05", "https://www.goldenglobes.com/sites/default/files/styles/portrait_medium/public/gallery_images/17-tomcruiseag.jpg?itok=qNj0cQGV&c=c9a73b7bdf609d72214d226ab9ea015e"));
            contributionList.add(new CommentsListItem("u1", "Jhon Paul", "Good", "Jan 05", "https://www.goldenglobes.com/sites/default/files/styles/portrait_medium/public/gallery_images/17-tomcruiseag.jpg?itok=qNj0cQGV&c=c9a73b7bdf609d72214d226ab9ea015e"));
            contributionList.add(new CommentsListItem("u1", "Jhon Paul", "Good", "Jan 05", "https://www.goldenglobes.com/sites/default/files/styles/portrait_medium/public/gallery_images/17-tomcruiseag.jpg?itok=qNj0cQGV&c=c9a73b7bdf609d72214d226ab9ea015e"));
            contributionList.add(new CommentsListItem("u1", "Jhon Paul", "Whether you have meditated for a long time or have never meditated, come join us for this time of practice together. Come to relax, quiet your mind, learn how to experience less suffering and stress, explore Buddhist philosophy and psychology, just talk about what it means to live from compassion and awareness -- or come because you are curious. This group meets on Tuesdays from 5 - 6 p.m. in in Chapin Chapel, and is led by Mark Hart, Buddhist Advisor.", "Jan 05", "https://www.goldenglobes.com/sites/default/files/styles/portrait_medium/public/gallery_images/17-tomcruiseag.jpg?itok=qNj0cQGV&c=c9a73b7bdf609d72214d226ab9ea015e"));
            contributionList.add(new CommentsListItem("u1", "Jhon Paul", "Good", "Jan 05", "https://www.goldenglobes.com/sites/default/files/styles/portrait_medium/public/gallery_images/17-tomcruiseag.jpg?itok=qNj0cQGV&c=c9a73b7bdf609d72214d226ab9ea015e"));
            contributionList.add(new CommentsListItem("u1", "Jhon Paul", "Good", "Jan 05", "https://www.goldenglobes.com/sites/default/files/styles/portrait_medium/public/gallery_images/17-tomcruiseag.jpg?itok=qNj0cQGV&c=c9a73b7bdf609d72214d226ab9ea015e"));
            contributionList.add(new CommentsListItem("u1", "Jhon Paul", "Good", "Jan 05", "https://www.goldenglobes.com/sites/default/files/styles/portrait_medium/public/gallery_images/17-tomcruiseag.jpg?itok=qNj0cQGV&c=c9a73b7bdf609d72214d226ab9ea015e"));
            userContributionAdapter = new UserContributionAdapter(getActivity(), contributionList);

            user_profile_contribution_list_view.setAdapter(userContributionAdapter);

        }
        if (user_type_selection_status == 1) {

            contributionList = new ArrayList<>();
            contributionList.add(new CommentsListItem("u1", "Kevin Ryan", "speaking to a a packed crowd at Sanders Theatre, a nobel laureate discussing her most recent scientific discovery, or the Harvard senior talent show, there’s always something happening at Harvard.", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
            contributionList.add(new CommentsListItem("u1", "Jhon Paul", "Good", "Jan 05", "http://keenthemes.com/preview/metronic/theme/assets/pages/media/profile/profile_user.jpg"));
            contributionList.add(new CommentsListItem("u1", "Srini Hari", "Good", "Jan 05", "http://www.dast.biz/wp-content/uploads/2016/11/John_Doe.jpg"));
            contributionList.add(new CommentsListItem("u1", "Sam ken", "Whether you have meditated for a long time or have never meditated, come join us for this time of practice together. Come to relax, quiet your mind, learn how to experience less suffering and stress, explore Buddhist philosophy and psychology, just talk about what it means to live from compassion and awareness -- or come because you are curious. This group meets on Tuesdays from 5 - 6 p.m. in in Chapin Chapel, and is led by Mark Hart, Buddhist Advisor.", "Jan 05", "https://a0.muscache.com/im/pictures/fe062a6a-6e30-4d64-bca5-69b037b6bff4.jpg?aki_policy=profile_x_medium"));
            contributionList.add(new CommentsListItem("u1", "Neha Josh", "Good", "Jan 05", "https://www.bnl.gov/today/body_pics/2017/06/stephanhruszkewycz-355px.jpg"));
            contributionList.add(new CommentsListItem("u1", "Sid Patro", "Good", "Jan 05", "http://www.medicine20congress.com/ocs/public/profiles/3141.jpg"));
            contributionList.add(new CommentsListItem("u1", "Nayab Sidi", "Good", "Jan 05", "https://cdn.earthdata.nasa.gov/conduit/upload/6072/Glenn_headshot_resize.jpg"));
            userContributionAdapter = new UserContributionAdapter(getActivity(), contributionList);

            user_profile_contribution_list_view.setAdapter(userContributionAdapter);

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        sessionCounter = System.currentTimeMillis() - sessionCounter;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(sessionCounter);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(sessionCounter);

        System.out.println("PROFILE sessionCounter : " + minutes + "m " + seconds + "s");
    }
}
