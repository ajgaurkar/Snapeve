package com.umbcapp.gaurk.snapeve.Fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.squareup.picasso.Picasso;
import com.umbcapp.gaurk.snapeve.Adapters.UserContributionAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.CommentsListItem;
import com.umbcapp.gaurk.snapeve.ManageGroups;
import com.umbcapp.gaurk.snapeve.EventDetails;
import com.umbcapp.gaurk.snapeve.Leaderboard;
import com.umbcapp.gaurk.snapeve.MainActivity;
import com.umbcapp.gaurk.snapeve.R;
import com.umbcapp.gaurk.snapeve.SessionManager;
import com.umbcapp.gaurk.snapeve.Signup_grp_join;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

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
    private int admin_flag = 0;
    private TextView user_profile_member_count_text_view;
    private ImageView user_profile_member_count_text_view_icon;

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
        user_total_pts = Integer.parseInt(userDetailsObj.get("user_points").toString());
        //time being untill admin flag is not 0 for all

        try {
            admin_flag = Integer.parseInt(userDetailsObj.get("group_admin_flag").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


        userName = userName.substring(1, userName.length() - 1);
        dp_url = dp_url.substring(1, dp_url.length() - 1);
        first_name = first_name.substring(1, first_name.length() - 1);
        last_name = last_name.substring(1, last_name.length() - 1);

        if (userDetailsObj.get("grp_id").toString().isEmpty() || userDetailsObj.get("grp_id").toString().equals("null") || userDetailsObj.get("grp_id") == null) {

            grp_id = "xxxxx____xxxxx";
            grp_name = "xxxxx_____xxxxx";
            grp_dp_url = "xxxxx_____xxxxx";

        } else {
            grp_id = userDetailsObj.get("grp_id").toString();
            grp_name = userDetailsObj.get("grp_name").toString();
            grp_dp_url = userDetailsObj.get("grp_dp_url").toString();
            grp_total_pts = Integer.parseInt(userDetailsObj.get("total_pts").toString());

            grp_id = grp_id.substring(1, grp_id.length() - 1);
            grp_name = grp_name.substring(1, grp_name.length() - 1);
            grp_dp_url = grp_dp_url.substring(1, grp_dp_url.length() - 1);
        }

        if (admin_flag == 1) {
            user_profile_settings_imageview.setVisibility(View.VISIBLE);
        } else {
            user_profile_settings_imageview.setVisibility(View.INVISIBLE);
        }
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

        user_profile_member_count_text_view = (TextView) rootView.findViewById(R.id.user_profile_member_count_text_view);
        user_profile_member_count_text_view_icon = (ImageView) rootView.findViewById(R.id.user_profile_member_count_text_view_icon);
        //gone by default, check for group avaiablity and show/hide accordingly
        user_profile_member_count_text_view.setVisibility(View.GONE);
        user_profile_member_count_text_view_icon.setVisibility(View.GONE);

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
                if (admin_flag == 1) {
                    startActivity(new Intent(getActivity(), ManageGroups.class));
                }

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

                user_profile_member_count_text_view.setVisibility(View.GONE);
                user_profile_member_count_text_view_icon.setVisibility(View.GONE);
            }
        });

        grp_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (grp_id.equals("xxxxx____xxxxx")) {
                    Toast.makeText(getActivity(), "No group info found", Toast.LENGTH_SHORT).show();
//                    grp_profile_btn.setError("No group info found");
                    user_profile_member_count_text_view.setVisibility(View.GONE);
                    user_profile_member_count_text_view_icon.setVisibility(View.GONE);

                    if (new SessionManager(getActivity()).getSpecificUserBooleanDetail(SessionManager.KEY_REQ_PENDING_GRP_STATUS)) {
                        showPendingGrpRequestDialog();
                    } else {
                        Intent joinGrpIntent = new Intent(getActivity(), Signup_grp_join.class);
                        joinGrpIntent.putExtra("page_open_mode", 1);
                        startActivity(joinGrpIntent);
                    }
                } else {
                    user_profile_member_count_text_view.setVisibility(View.VISIBLE);
                    user_profile_member_count_text_view_icon.setVisibility(View.VISIBLE);
                    grp_profile_btn.setBackground(getResources().getDrawable(R.drawable.text_selection_right_seleted));
                    user_profile_btn.setBackground(getResources().getDrawable(R.drawable.text_selection_left_unseleted));
                    user_type_selection_status = 1;
                    Picasso.get().load(grp_dp_url)
                            .fit().centerCrop().into(profile_pic_image_view);
                    loadContributionList(user_type_selection_status);
                    user_name_textview.setText(grp_name);
                    user_points.setText(String.valueOf(grp_total_pts));
                }
            }
        });

        return rootView;
    }

    private void showPendingGrpRequestDialog() {
        final Dialog alertDialog = new Dialog(getActivity());
        LayoutInflater flater = getActivity().getLayoutInflater();
        View view = flater.inflate(R.layout.show_pending_grp_req_dialog, null);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(view);
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);

        TextView show_pending_req_dialog_btn_grp_name_textView = (TextView) view.findViewById(R.id.show_pending_req_dialog_btn_grp_name_textView);
        CardView show_pending_req_dialog_cancel_req_btn_card_view = (CardView) view.findViewById(R.id.show_pending_req_dialog_cancel_req_btn_card_view);
        CardView show_pending_req_dialog_ok_btn_card_view = (CardView) view.findViewById(R.id.show_pending_req_dialog_ok_btn_card_view);

        show_pending_req_dialog_btn_grp_name_textView.setText(new SessionManager(getActivity()).getSpecificUserDetail(SessionManager.KEY_REQ_PENDING_GRP_NAME));

        show_pending_req_dialog_ok_btn_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        show_pending_req_dialog_cancel_req_btn_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                cancelJoinGrpPendingRequest();
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void cancelJoinGrpPendingRequest() {
        Toast.makeText(getActivity(), "Cancel request called", Toast.LENGTH_SHORT).show();

//make an API call here to cancel the request
//then
        cancelGrpJoinRequest();

        //if response = true then store SP as false and clear values of grp pending request

    }

    private void cancelGrpJoinRequest() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Cancelling request, Please wait...");
        progressDialog.create();
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        JsonObject jsonObjectParameters = new JsonObject();
        jsonObjectParameters.addProperty("req_code", -11);
        jsonObjectParameters.addProperty("grpId", new SessionManager(getActivity()).getSpecificUserDetail(SessionManager.KEY_REQ_PENDING_GRP_ID));
        jsonObjectParameters.addProperty("userId", new SessionManager(getActivity()).getSpecificUserDetail(SessionManager.KEY_USER_ID));

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

                if (response.toString().contains("true")) {
                    new SessionManager(getActivity()).setSpecificUserBooleanDetail(SessionManager.KEY_REQ_PENDING_GRP_STATUS, false);
                    new SessionManager(getActivity()).setSpecificUserDetail(SessionManager.KEY_REQ_PENDING_GRP_ID, null);
                    new SessionManager(getActivity()).setSpecificUserDetail(SessionManager.KEY_REQ_PENDING_GRP_NAME, null);
                }

            }
        });
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
