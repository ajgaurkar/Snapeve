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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.umbcapp.gaurk.snapeve.Adapters.UserContributionAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.CommentsListItem;
import com.umbcapp.gaurk.snapeve.EventDetails;
import com.umbcapp.gaurk.snapeve.Leaderboard;
import com.umbcapp.gaurk.snapeve.MainActivity;
import com.umbcapp.gaurk.snapeve.R;

import java.util.ArrayList;

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

    public UserProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contributionList = new ArrayList<>();
        fetch_group_details();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.userprofile, container, false);


        grp_profile_btn = (TextView) rootView.findViewById(R.id.profile_switch_grp_textview);
        user_profile_btn = (TextView) rootView.findViewById(R.id.profile_switch_you_textview);
        user_profile_list_view = (ListView) rootView.findViewById(R.id.user_profile_contribution_list_view);
        profile_relative_layout = (RelativeLayout) rootView.findViewById(R.id.profile_relative_layout);
        user_profile_contribution_list_view = (ListView) rootView.findViewById(R.id.user_profile_contribution_list_view);
        leaderboard_imageview = (ImageView) rootView.findViewById(R.id.leaderboard_imageview);


        loadContributionList(0);

        user_profile_contribution_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), EventDetails.class));
            }
        });

        profile_relative_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        leaderboard_imageview.setOnClickListener(new View.OnClickListener() {
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
                loadContributionList(user_type_selection_status);
            }
        });

        grp_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grp_profile_btn.setBackground(getResources().getDrawable(R.drawable.text_selection_right_seleted));
                user_profile_btn.setBackground(getResources().getDrawable(R.drawable.text_selection_left_unseleted));
                user_type_selection_status = 1;
                loadContributionList(user_type_selection_status);
            }
        });

        return rootView;
    }

    private void loadContributionList(int user_type_selection_status) {

        if (user_type_selection_status == 0) {
            contributionList = new ArrayList<>();
            contributionList.add(new CommentsListItem("u1", "Jhon Paul", "speaking to a a packed crowd at Sanders Theatre, a nobel laureate discussing her most recent scientific discovery, or the Harvard senior talent show, there’s always something happening at Harvard.", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
            contributionList.add(new CommentsListItem("u1", "Jhon Paul", "Good", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
            contributionList.add(new CommentsListItem("u1", "Jhon Paul", "Good", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
            contributionList.add(new CommentsListItem("u1", "Jhon Paul", "Whether you have meditated for a long time or have never meditated, come join us for this time of practice together. Come to relax, quiet your mind, learn how to experience less suffering and stress, explore Buddhist philosophy and psychology, just talk about what it means to live from compassion and awareness -- or come because you are curious. This group meets on Tuesdays from 5 - 6 p.m. in in Chapin Chapel, and is led by Mark Hart, Buddhist Advisor.", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
            contributionList.add(new CommentsListItem("u1", "Jhon Paul", "Good", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
            contributionList.add(new CommentsListItem("u1", "Jhon Paul", "Good", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
            contributionList.add(new CommentsListItem("u1", "Jhon Paul", "Good", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
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


}
