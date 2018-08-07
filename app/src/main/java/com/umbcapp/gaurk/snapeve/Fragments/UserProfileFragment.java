package com.umbcapp.gaurk.snapeve.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.picasso.Picasso;
import com.umbcapp.gaurk.snapeve.Adapters.GroupMembersAdapter;
import com.umbcapp.gaurk.snapeve.Adapters.SignupGrpAdapter;
import com.umbcapp.gaurk.snapeve.Adapters.TeamMatesAdapter;
import com.umbcapp.gaurk.snapeve.Adapters.UserContributionAdapter;
import com.umbcapp.gaurk.snapeve.Add_event;
import com.umbcapp.gaurk.snapeve.AzureConfiguration;
import com.umbcapp.gaurk.snapeve.BrowseUserProfile;
import com.umbcapp.gaurk.snapeve.Controllers.CommentsListItem;
import com.umbcapp.gaurk.snapeve.Controllers.LeaderboardListItem;
import com.umbcapp.gaurk.snapeve.Controllers.SignUpGrpListItem;
import com.umbcapp.gaurk.snapeve.Controllers.TeammatesListItem;
import com.umbcapp.gaurk.snapeve.EventDetails;
import com.umbcapp.gaurk.snapeve.Leaderboard;
import com.umbcapp.gaurk.snapeve.MainActivity;
import com.umbcapp.gaurk.snapeve.ManageGroups;
import com.umbcapp.gaurk.snapeve.R;
import com.umbcapp.gaurk.snapeve.RewardCalcuator;
import com.umbcapp.gaurk.snapeve.SessionManager;
import com.umbcapp.gaurk.snapeve.Signup_grp_join;
import com.umbcapp.gaurk.snapeve.Singleton;
import com.umbcapp.gaurk.snapeve.user_table;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import me.iwf.photopicker.PhotoPicker;

import static android.app.Activity.RESULT_OK;
import static com.umbcapp.gaurk.snapeve.AzureConfiguration.getContainer;

public class UserProfileFragment extends Fragment {

    private static final int PICK_GALLERY_IMAGE = 2;
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
    private CircleImageView profile_pic_image_view;
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
    private JsonElement bkupPostResponse;
    private JsonElement bkupMemberResponse;
    private ArrayList<TeammatesListItem> userProfileList = new ArrayList<>();
    private ProgressBar show_pending_req_dialog_progressBar;
    private TextView show_pending_req_dialog_progressBar_label;
    private RelativeLayout show_accept_invitation_dialog_invitations_layout;
    private ArrayList<SignUpGrpListItem> signupGrpList;
    private ListView show_pending_req_dialog_pending_req_listview;
    private RelativeLayout full_screen_imageview_layout;
    private ImageView full_screen_imageview;
    private File mCameraImageFile;
    private Uri mCameraImageFileUri = null;
    static final int REQUEST_TAKE_PHOTO = 1;
    private Map<String, Uri> mapForUploadingSelectedProfilePic;
    private CloudBlobContainer container;
    private user_table user_table;
    private String ImageFileName;
    private MobileServiceTable<user_table> mUserTable;
    private MobileServiceClient mClient;
    private String user_id;
    private boolean memberListOpenFlag = false;


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
        mClient = Singleton.Instance().mClientMethod(getActivity());
        mUserTable = mClient.getTable(user_table.class);

    }

    private void fetchGrpPostAndMembers(String grp_id, final int fetch_type_post_or_members) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading, Please wait...");
        progressDialog.create();
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

//                if (fetch_type_post_or_members == 0) {
//                    bkupPostResponse = response;
//                    parseGrpPostData(bkupPostResponse);
//                }
//                if (fetch_type_post_or_members == 1) {
//                    bkupMemberResponse = response;
//                    parseMemberdata(bkupMemberResponse);
//                }
            }
        });
    }

    private void divideGrpAndPostData(JsonElement response) {

        JsonObject grpInfoResponse = response.getAsJsonObject();
        JsonArray grpMembers = grpInfoResponse.getAsJsonArray("grp_members");
        JsonArray grpPost = grpInfoResponse.getAsJsonArray("grp_post");

        System.out.println("grpMembers" + grpMembers);
        if (grpMembers.size() > 0) {
            parseMemberdata(grpMembers);
        }

        System.out.println("grpPost " + grpPost);
        if (grpPost.size() > 0) {
            parsePostdata(grpPost);
        }
    }

    private void parsePostdata(JsonArray grpPost) {

    }

    private void parseMemberdata(JsonArray grpMembers) {
        System.out.println(" IN PARSE JASON");

        userProfileList = new ArrayList<>();
        int max_user_points = 0;
        for (int i = 0; i < grpMembers.size(); i++) {
            JsonObject userData_list_object = grpMembers.get(i).getAsJsonObject();

            System.out.println(" userData_list_object  " + userData_list_object);

            user_id = userData_list_object.get("user_id").toString();

            System.out.println("user_id NOT NULL : " + user_id);
            String user_name = userData_list_object.get("user_name").getAsString();
            String dp_url = userData_list_object.get("dp_url").getAsString();
            String user_id = userData_list_object.get("user_id").getAsString();
            String first_name = userData_list_object.get("first_name").getAsString();
            String last_name = userData_list_object.get("last_name").getAsString();
            int user_points = userData_list_object.get("user_points").getAsInt();


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

//        GroupMembersAdapter groupMembersAdapter = new GroupMembersAdapter(BrowseGroupProfile.this, userProfileList, max_user_points);
//        user_profile_contribution_rec_view.setLayoutManager(mLayoutManager);
//        user_profile_contribution_rec_view.setItemAnimator(new DefaultItemAnimator());
//        user_profile_contribution_rec_view.setAdapter(groupMembersAdapter);
//
//        if (userProfileList.size() == 0) {
//            user_profile_member_count_text_view.setVisibility(View.VISIBLE);
//            user_profile_member_count_text_view.setText(userProfileList.size() + " Members");
//        } else if (userProfileList.size() == 1) {
//            user_profile_member_count_text_view.setVisibility(View.GONE);
//            user_profile_member_count_text_view.setText(userProfileList.size() + " Member");
//        } else {
//            user_profile_member_count_text_view.setVisibility(View.GONE);
//            user_profile_member_count_text_view.setText(userProfileList.size() + " Members");
//        }

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
        //might need to change it to user id from user name
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
                fetchGrpPostAndMembers(grp_id, 0);
//                fetchGrpPostAndMembers(grp_id, 1);
            }
        });
    }

    private void parseResponse(JsonElement response) {
        JsonArray responseJsonArray = response.getAsJsonArray();

        JsonObject userDetailsObj = responseJsonArray.get(0).getAsJsonObject();

        userName = userDetailsObj.get("user_name").getAsString();
        first_name = userDetailsObj.get("first_name").getAsString();
        last_name = userDetailsObj.get("last_name").getAsString();
        user_total_pts = Integer.parseInt(userDetailsObj.get("user_points").toString());

        RewardCalcuator rewardCalcuator = new RewardCalcuator();
        RewardCalcuator calculatedReward = rewardCalcuator.calculate(user_total_pts);
        System.out.println("calculatedReward " + calculatedReward.getCurrent_points());
        System.out.println("calculatedReward " + calculatedReward.getMin_range());
        System.out.println("calculatedReward " + calculatedReward.getMax_range());

        try {
            dp_url = userDetailsObj.get("dp_url").getAsString();
            System.out.println("userprofilefragment user dp_url : " + dp_url);
        } catch (Exception e) {
            e.printStackTrace();
            dp_url = null;
            System.out.println(" dp_url is null, set local image");
            profile_pic_image_view.setImageResource(R.drawable.avatar_100_3);
        }


        //time being untill admin flag is not 0 for all
        try {
            admin_flag = Integer.parseInt(userDetailsObj.get("group_admin_flag").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


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
        populateUserInfo();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.userprofile, container, false);

        grp_profile_btn = (TextView) rootView.findViewById(R.id.profile_switch_grp_textview);
        user_name_textview = (TextView) rootView.findViewById(R.id._user_name);
        user_profile_btn = (TextView) rootView.findViewById(R.id.profile_switch_you_textview);
        user_points = (TextView) rootView.findViewById(R.id.user_points);
        profile_relative_layout = (RelativeLayout) rootView.findViewById(R.id.profile_relative_layout);
        full_screen_imageview_layout = (RelativeLayout) rootView.findViewById(R.id.full_screen_imageview_layout);
        full_screen_imageview = (ImageView) rootView.findViewById(R.id.full_screen_imageview);
        user_profile_contribution_list_view = (ListView) rootView.findViewById(R.id.user_profile_contribution_list_view);
        leaderboard_text_view = (TextView) rootView.findViewById(R.id.leaderboard_text_view);
        profile_pic_image_view = (CircleImageView) rootView.findViewById(R.id.profile_pic_image_view);
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
                eventDetailIntent.putExtra("user_id", selectedCommentsListItem.getSrc_user_id());
                eventDetailIntent.putExtra("user_name", selectedCommentsListItem.getSrc_user_name());
                eventDetailIntent.putExtra("img_url", selectedCommentsListItem.getImage_url());
                eventDetailIntent.putExtra("comm_time", selectedCommentsListItem.getComment_time());
                eventDetailIntent.putExtra("user_comment", selectedCommentsListItem.getUser_comment());
                startActivity(eventDetailIntent);

            }
        });

        user_profile_member_count_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!memberListOpenFlag) {
                    loadContributionList(2);
                    memberListOpenFlag = true;
                    user_profile_member_count_text_view_icon.setImageResource(R.drawable.up_arrow_white_24);

                } else {
                    loadContributionList(1);
                    user_profile_member_count_text_view_icon.setImageResource(R.drawable.down_arrow_white_24);
                    memberListOpenFlag = false;
                }


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

                    openSettingsMenu();
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
                populateUserInfo();
            }
        });

        grp_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (grp_id.equals("xxxxx____xxxxx")) {
                    checkForGroupRequest();
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
                    if (grp_dp_url == null) {
                        profile_pic_image_view.setImageResource(R.drawable.avatar_100_3);
                    } else {
                        Picasso.get().load(grp_dp_url).fit().centerCrop().into(profile_pic_image_view);
                    }
//                    Picasso.get().load(grp_dp_url)
//                            .fit().centerCrop().into(profile_pic_image_view);
                    loadContributionList(user_type_selection_status);
                    user_name_textview.setText(grp_name);
                    user_points.setText(String.valueOf(grp_total_pts));

                }
                memberListOpenFlag = false;
            }
        });
        profile_pic_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageOptionsDialog();
            }
        });
        return rootView;
    }

    private void openImageOptionsDialog() {

        LayoutInflater flater = getActivity().getLayoutInflater();
        View view = flater.inflate(R.layout.edit_profile_pic_options_dialog, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setView(view);
        alertDialog.setCancelable(true);


        final AlertDialog dissmissAlertDialog = alertDialog.show();


        final ArrayList<String> memJoinedOptionsList = new ArrayList<>();
        memJoinedOptionsList.add("View Profile Pic");
        memJoinedOptionsList.add("Update from Camera");
        memJoinedOptionsList.add("Update from Gallery");
        memJoinedOptionsList.add("Remove Profile Pic");

        ListView edit_profile_pic_options_dialog_listview = (ListView) view.findViewById(R.id.edit_profile_pic_options_dialog_listview);
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.members_joined_options_list_item, memJoinedOptionsList);
        edit_profile_pic_options_dialog_listview.setAdapter(adapter);

        alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        edit_profile_pic_options_dialog_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        dissmissAlertDialog.dismiss();
                        full_screen_imageview_layout.setVisibility(View.VISIBLE);
                        Picasso.get().load(dp_url).fit().centerInside().into(full_screen_imageview);
                        break;

                    case 1:
                        //open Camera and update picture
                        dissmissAlertDialog.dismiss();
                        takePicture();
                        Toast.makeText(getActivity(), "OPEN CAMERA : CHANGE DP", Toast.LENGTH_LONG).show();
                        break;

                    case 2:
                        //open Gallery and update picture
                        dissmissAlertDialog.dismiss();
                        selcectFromGallery();

                        Toast.makeText(getActivity(), "OPEN GALLERY : CHANGE DP", Toast.LENGTH_LONG).show();
                        break;

                    case 3:
                        dissmissAlertDialog.dismiss();
                        Toast.makeText(getActivity(), "DELETE DP API CALL", Toast.LENGTH_LONG).show();
                        //Remove profile pic
                        break;


                }
            }
        });
        //alertDialog.show();

    }

    private void checkForGroupRequest() {
        JsonObject jsonObjectParameters = new JsonObject();
        jsonObjectParameters.addProperty("user_id", new SessionManager(getActivity()).getSpecificUserDetail(SessionManager.KEY_USER_ID));

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("check_user_group_requests_api", jsonObjectParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
//                progressDialog.dismiss();
                System.out.println(" check_user_group_requests_api exception    " + exception);
                show_pending_req_dialog_progressBar.setVisibility(View.GONE);
                show_pending_req_dialog_progressBar_label.setVisibility(View.GONE);
                show_accept_invitation_dialog_invitations_layout.setVisibility(View.GONE);

            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
//                progressDialog.dismiss();
                System.out.println(" check_user_group_requests_api success response    " + response);
                show_pending_req_dialog_progressBar.setVisibility(View.GONE);
                show_pending_req_dialog_progressBar_label.setVisibility(View.GONE);
                show_accept_invitation_dialog_invitations_layout.setVisibility(View.VISIBLE);
                parseInvitationsData(response);

            }
        });
    }

    private void parseInvitationsData(JsonElement invitationsResponse) {
        signupGrpList = new ArrayList<SignUpGrpListItem>();

        System.out.println(" IN PARSE JASON");

        JsonArray groupsJSONArray = invitationsResponse.getAsJsonArray();

        for (int j = 0; j < groupsJSONArray.size(); j++) {
            JsonObject group_list_object = groupsJSONArray.get(j).getAsJsonObject();
            System.out.println(" group_list_object  " + group_list_object);

            String grp_id = group_list_object.get("grp_id").getAsString();
            String grp_name = group_list_object.get("grp_name").getAsString();
            String grp_dp_url = null;
            try {
                grp_dp_url = group_list_object.get("grp_dp_url").getAsString();
            } catch (Exception e) {

            }
            int req_code = group_list_object.get("REQ_CODE").getAsInt();

            System.out.println(" user_id " + grp_id);
            System.out.println(" grp_name " + grp_name);
            System.out.println(" grp_dp_url " + grp_dp_url);
            System.out.println(" req_code " + req_code);

            //condition to put data only if code = 11 (Invitation from admin)
            if (req_code == 11) {
                signupGrpList.add(new SignUpGrpListItem(grp_id, 0, grp_name, grp_dp_url, 1));
            }

        }
        if (signupGrpList.size() > 0) {
            System.out.println(" signupGrpList " + signupGrpList.size());
            SignupGrpAdapter signupGrpAdapter = new SignupGrpAdapter(getActivity(), signupGrpList);
            show_pending_req_dialog_pending_req_listview.setAdapter(signupGrpAdapter);
        } else {
            show_accept_invitation_dialog_invitations_layout.setVisibility(View.GONE);
        }

    }


    private void populateUserInfo() {

        user_type_selection_status = 0;
        if (dp_url == null) {
            profile_pic_image_view.setImageResource(R.drawable.avatar_100_3);
        } else {
            Picasso.get().load(dp_url).fit().centerCrop().into(profile_pic_image_view);
        }
        loadContributionList(user_type_selection_status);
        user_name_textview.setText(userName);
        user_points.setText(String.valueOf(user_total_pts));

        user_profile_member_count_text_view.setVisibility(View.GONE);
        user_profile_member_count_text_view_icon.setVisibility(View.GONE);
    }

    private void openSettingsMenu() {

//        if (userPermission == P0 || P1) {
//            open leave group dialog
//        }
//
//        if (userPermission == P2) {
//            open 2 options settings menu
//        }
//
//        if (userPermission == P3) {
//            open 3 options settings menu
//        }

        PopupMenu popup = new PopupMenu(getActivity(), user_profile_settings_imageview);
        popup.getMenuInflater().inflate(R.menu.user_profile_3_settings_popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getTitle().equals("Manage Members")) {
//                    Toast.makeText(getActivity(), "Manage Members", Toast.LENGTH_SHORT).show();
                    Intent manageMembersIntent = new Intent(getActivity(), ManageGroups.class);
                    manageMembersIntent.putExtra("Grp_id", grp_id);
                    manageMembersIntent.putExtra("Grp_name", grp_name);
                    manageMembersIntent.putExtra("Grp_dp_url", grp_dp_url);
                    startActivity(manageMembersIntent);

                } else if (item.getTitle().equals("Leave Group")) {
                    openLeaveGroupDialog();
                } else if (item.getTitle().equals("Delete Group")) {
                    openLDeleteGroupDialog();
                }
                return false;
            }
        });
        popup.show();
    }

    private void openLDeleteGroupDialog() {
        final Dialog alertDialog = new Dialog(getActivity());
        LayoutInflater flater = getActivity().getLayoutInflater();
        View view = flater.inflate(R.layout.delete_grp_dialog, null);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(view);
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);

        CardView delete_grp_dialog_confirm_btn_card_view = (CardView) view.findViewById(R.id.delete_grp_dialog_confirm_btn_card_view);
        CardView delete_grp_dialog_cancel_btn_card_view = (CardView) view.findViewById(R.id.delete_grp_dialog_cancel_btn_card_view);
        final EditText delete_grp_dialog_type_grp_name_edittext = (EditText) view.findViewById(R.id.delete_grp_dialog_type_grp_name_edittext);
        final TextView delete_grp_validation_error_footer_textView = (TextView) view.findViewById(R.id.delete_grp_validation_error_footer_textView);

        delete_grp_dialog_cancel_btn_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        delete_grp_dialog_confirm_btn_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("11 : " + delete_grp_dialog_type_grp_name_edittext.getText().toString());
                System.out.println("12 : " + grp_name);
                if (delete_grp_dialog_type_grp_name_edittext.getText().toString().equalsIgnoreCase(grp_name)) {
                    alertDialog.dismiss();
                    executeDeleteGroupApi();
                    delete_grp_validation_error_footer_textView.setVisibility(View.GONE);
                } else {
                    delete_grp_validation_error_footer_textView.setVisibility(View.VISIBLE);
                }
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void openLeaveGroupDialog() {
        final Dialog alertDialog = new Dialog(getActivity());
        LayoutInflater flater = getActivity().getLayoutInflater();
        View view = flater.inflate(R.layout.leave_grp_dialog, null);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(view);
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);

        CardView leave_grp_dialog_confirm_btn_card_view = (CardView) view.findViewById(R.id.leave_grp_dialog_confirm_btn_card_view);
        CardView leave_grp_dialog_cancel_btn_card_view = (CardView) view.findViewById(R.id.leave_grp_dialog_cancel_btn_card_view);

        leave_grp_dialog_cancel_btn_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        leave_grp_dialog_confirm_btn_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                executeLeaveGroupApi();
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void executeLeaveGroupApi() {
        //execute leave group api and if response success, delete all data related to group from SP
        Toast.makeText(getActivity(), "executeLeaveGroupApi", Toast.LENGTH_SHORT).show();
    }

    private void executeDeleteGroupApi() {
        //execute delete group api and if response success, delete all data related to group from SP
        Toast.makeText(getActivity(), "executeDeleteGroupApi", Toast.LENGTH_SHORT).show();
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
        show_accept_invitation_dialog_invitations_layout = (RelativeLayout) view.findViewById(R.id.show_accept_invitation_dialog_invitations_layout);
        show_pending_req_dialog_pending_req_listview = (ListView) view.findViewById(R.id.show_pending_req_dialog_pending_req_listview);

        show_pending_req_dialog_progressBar = (ProgressBar) view.findViewById(R.id.show_pending_req_dialog_progressBar);
        show_pending_req_dialog_progressBar_label = (TextView) view.findViewById(R.id.show_pending_req_dialog_progressBar_label);
        show_pending_req_dialog_progressBar.setVisibility(View.VISIBLE);
        show_pending_req_dialog_progressBar_label.setVisibility(View.VISIBLE);

        show_pending_req_dialog_btn_grp_name_textView.setText("Pending request for : " + new SessionManager(getActivity()).getSpecificUserDetail(SessionManager.KEY_REQ_PENDING_GRP_NAME));

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
            memberListOpenFlag = false;
            contributionList = new ArrayList<>();
            contributionList.add(new CommentsListItem("u1", "", "Jhon Paul", "", "speaking to a a packed crowd at Sanders Theatre, a nobel laureate discussing her most recent scientific discovery, or the Harvard senior talent show, there’s always something happening at Harvard.", "Jan 05", "https://www.goldenglobes.com/sites/default/files/styles/portrait_medium/public/gallery_images/17-tomcruiseag.jpg?itok=qNj0cQGV&c=c9a73b7bdf609d72214d226ab9ea015e"));
            contributionList.add(new CommentsListItem("u1", "", "Jhon Paul", "", "Good", "Jan 05", "https://www.goldenglobes.com/sites/default/files/styles/portrait_medium/public/gallery_images/17-tomcruiseag.jpg?itok=qNj0cQGV&c=c9a73b7bdf609d72214d226ab9ea015e"));
            contributionList.add(new CommentsListItem("u1", "", "Jhon Paul", "", "Good", "Jan 05", "https://www.goldenglobes.com/sites/default/files/styles/portrait_medium/public/gallery_images/17-tomcruiseag.jpg?itok=qNj0cQGV&c=c9a73b7bdf609d72214d226ab9ea015e"));
            contributionList.add(new CommentsListItem("u1", "", "Jhon Paul", "", "Whether you have meditated for a long time or have never meditated, come join us for this time of practice together. Come to relax, quiet your mind, learn how to experience less suffering and stress, explore Buddhist philosophy and psychology, just talk about what it means to live from compassion and awareness -- or come because you are curious. This group meets on Tuesdays from 5 - 6 p.m. in in Chapin Chapel, and is led by Mark Hart, Buddhist Advisor.", "Jan 05", "https://www.goldenglobes.com/sites/default/files/styles/portrait_medium/public/gallery_images/17-tomcruiseag.jpg?itok=qNj0cQGV&c=c9a73b7bdf609d72214d226ab9ea015e"));
            contributionList.add(new CommentsListItem("u1", "", "Jhon Paul", "", "Good", "Jan 05", "https://www.goldenglobes.com/sites/default/files/styles/portrait_medium/public/gallery_images/17-tomcruiseag.jpg?itok=qNj0cQGV&c=c9a73b7bdf609d72214d226ab9ea015e"));
            contributionList.add(new CommentsListItem("u1", "", "Jhon Paul", "", "Good", "Jan 05", "https://www.goldenglobes.com/sites/default/files/styles/portrait_medium/public/gallery_images/17-tomcruiseag.jpg?itok=qNj0cQGV&c=c9a73b7bdf609d72214d226ab9ea015e"));
            contributionList.add(new CommentsListItem("u1", "", "Jhon Paul", "", "Good", "Jan 05", "https://www.goldenglobes.com/sites/default/files/styles/portrait_medium/public/gallery_images/17-tomcruiseag.jpg?itok=qNj0cQGV&c=c9a73b7bdf609d72214d226ab9ea015e"));
            userContributionAdapter = new UserContributionAdapter(getActivity(), contributionList);

            user_profile_contribution_list_view.setAdapter(userContributionAdapter);

        }
        if (user_type_selection_status == 1) {
            memberListOpenFlag = false;
            contributionList = new ArrayList<>();
            contributionList.add(new CommentsListItem("u1", "", "Kevin Ryan", "", "speaking to a a packed crowd at Sanders Theatre, a nobel laureate discussing her most recent scientific discovery, or the Harvard senior talent show, there’s always something happening at Harvard.", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
            contributionList.add(new CommentsListItem("u1", "", "Jhon Paul", "", "Good", "Jan 05", "http://keenthemes.com/preview/metronic/theme/assets/pages/media/profile/profile_user.jpg"));
            contributionList.add(new CommentsListItem("u1", "", "Srini Hari", "", "Good", "Jan 05", "http://www.dast.biz/wp-content/uploads/2016/11/John_Doe.jpg"));
            contributionList.add(new CommentsListItem("u1", "", "Sam ken", "", "Whether you have meditated for a long time or have never meditated, come join us for this time of practice together. Come to relax, quiet your mind, learn how to experience less suffering and stress, explore Buddhist philosophy and psychology, just talk about what it means to live from compassion and awareness -- or come because you are curious. This group meets on Tuesdays from 5 - 6 p.m. in in Chapin Chapel, and is led by Mark Hart, Buddhist Advisor.", "Jan 05", "https://a0.muscache.com/im/pictures/fe062a6a-6e30-4d64-bca5-69b037b6bff4.jpg?aki_policy=profile_x_medium"));
            contributionList.add(new CommentsListItem("u1", "", "Neha Josh", "", "Good", "Jan 05", "https://www.bnl.gov/today/body_pics/2017/06/stephanhruszkewycz-355px.jpg"));
            contributionList.add(new CommentsListItem("u1", "", "Sid Patro", "", "Good", "Jan 05", "http://www.medicine20congress.com/ocs/public/profiles/3141.jpg"));
            contributionList.add(new CommentsListItem("u1", "", "Nayab Sidi", "", "Good", "Jan 05", "https://cdn.earthdata.nasa.gov/conduit/upload/6072/Glenn_headshot_resize.jpg"));
            userContributionAdapter = new UserContributionAdapter(getActivity(), contributionList);

            user_profile_contribution_list_view.setAdapter(userContributionAdapter);

            if (userProfileList.size() == 0) {
                user_profile_member_count_text_view.setVisibility(View.GONE);
//                user_profile_member_count_text_view.setText(userProfileList.size() + " Members");
            } else if (userProfileList.size() == 1) {
                user_profile_member_count_text_view.setVisibility(View.VISIBLE);
                user_profile_member_count_text_view.setText(userProfileList.size() + " Member");
            } else {
                user_profile_member_count_text_view.setVisibility(View.VISIBLE);
                user_profile_member_count_text_view.setText(userProfileList.size() + " Members");
            }

        }
        if (user_type_selection_status == 2) {

            TeamMatesAdapter teamMatesAdapter = new TeamMatesAdapter(getActivity(), userProfileList);
            user_profile_contribution_list_view.setAdapter(teamMatesAdapter);

//            if (userProfileList.size() == 0) {
//                user_profile_member_count_text_view.setVisibility(View.GONE);
////                user_profile_member_count_text_view.setText(userProfileList.size() + " Members");
//            } else if (userProfileList.size() == 1) {
//                user_profile_member_count_text_view.setVisibility(View.VISIBLE);
//                user_profile_member_count_text_view.setText(userProfileList.size() + " Member");
//            } else {
//                user_profile_member_count_text_view.setVisibility(View.VISIBLE);
//                user_profile_member_count_text_view.setText(userProfileList.size() + " Members");
//            }

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


    private void selcectFromGallery() {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(false)
                .setShowGif(true)
                .setPreviewEnabled(false)
                .start(getActivity(), PICK_GALLERY_IMAGE);

//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_GALLERY_IMAGE);
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            System.out.println("takePictureIntent not Null");

            try {
                mCameraImageFile = createImageFile();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Created Camera Image --- " + mCameraImageFile);

            if (mCameraImageFile != null) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    // Do something for Nougat and above versions
                    System.out.println("Above Nougat ");
                    takePictureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    mCameraImageFileUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", mCameraImageFile);
                } else {
                    System.out.println("Below Nougat ");
                    // do something for phones running an SDK before Nougat
                    mCameraImageFileUri = Uri.fromFile(mCameraImageFile);
                }

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraImageFileUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }


        } else {
            System.out.println("takePictureIntent  Null");

        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = Environment.getExternalStorageDirectory();

        File createdImageFile = File.createTempFile(imageFileName,/* prefix */".jpg",/* suffix */ storageDir /* directory */);
        System.out.println("===createdImageFile---  " + createdImageFile);

        return createdImageFile;
    }


    public String CompressionOfImage(String selectedImagePath) {
        //************************compress logic start****************
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 500;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE) {
            scale *= 2;
            System.out.println("Scaling start@@@@@@@@@@@");
        }
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        System.out.println("selectedImagePath" + selectedImagePath);
        Bitmap bm = BitmapFactory.decodeFile(selectedImagePath, options);
        System.out.println("selectedImagefile bitmap" + bm);

        return SaveImage(bm);

        //************************compress logic end****************
    }

    private String SaveImage(Bitmap finalBitmap) {
        String filePath = null;

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + getString(R.string.folderName) + "/Event");
        System.out.println("myDir" + myDir);
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String imageFileName = getString(R.string.folderName) + "-Snapeve-" + n + ".jpg";
        File file = new File(myDir, imageFileName);
        System.out.println("myDir : " + file);
        System.out.println("_File_Name : " + imageFileName);

        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            System.out.println("out" + out);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            Uri imagePath = Uri.fromFile(file);
            filePath = imagePath.getPath();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {


            try {
                System.out.println("Check Photo----");
                String attachedFilePath = mCameraImageFile.getAbsolutePath();
                String attachedFilePath_From_SaveImage = CompressionOfImage(attachedFilePath);
                System.out.println("attachedFilePath_From_SaveImage----------- " + attachedFilePath_From_SaveImage);
                File imgFile = new File(attachedFilePath_From_SaveImage);
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                profile_pic_image_view.setImageBitmap(myBitmap);

                //delete original large File
                mCameraImageFile.delete();

                mapForUploadingSelectedProfilePic = new HashMap<>();

                int index = attachedFilePath_From_SaveImage.lastIndexOf('/');
                String selectedCameraImageName = attachedFilePath_From_SaveImage.substring(index + 1);

                String addFileString = "file://" + attachedFilePath_From_SaveImage;
                Uri getUriToSendAzure = Uri.parse(addFileString);
                System.out.println("selectedImageName------- " + selectedCameraImageName);
                System.out.println("getUriToSendAzure------- " + getUriToSendAzure);
                mapForUploadingSelectedProfilePic.put(selectedCameraImageName, getUriToSendAzure);
                uploadProfileImage();

            } catch (Exception e) {
                System.out.println("image not capture from camera " + e.getMessage());
            }

        }

        if (requestCode == PICK_GALLERY_IMAGE && resultCode == RESULT_OK) {

            System.out.println("Pick Gallery Image");

        }

    }

    private void uploadProfileImage() {


        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {

                    for (Map.Entry<String, Uri> entry : mapForUploadingSelectedProfilePic.entrySet()) {
                        System.out.println("UploadinG Image------------------");

                        final InputStream imageStream = getActivity().getContentResolver().openInputStream(entry.getValue());
                        final int imageLength = imageStream.available();
                        container = getContainer();
                        CloudBlockBlob imageBlob = container.getBlockBlobReference(entry.getKey());
                        ImageFileName = entry.getKey();
                        imageBlob.upload(imageStream, imageLength);

                    }
                    System.out.println("beofre update user Tabele");
                    System.out.println(AzureConfiguration.Storage_url + ImageFileName);
                    user_table = new user_table();
                    System.out.println("user_id------------ " + new SessionManager(getActivity()).getSpecificUserDetail(SessionManager.KEY_USER_ID));
                    user_table.setId(new SessionManager(getActivity()).getSpecificUserDetail(SessionManager.KEY_USER_ID));
                    user_table.setDp_url(AzureConfiguration.Storage_url + ImageFileName);
                    mUserTable.update(user_table).get();
                    System.out.println("After update user Tabele");

                } catch (final Exception e) {
                    System.out.println("Image uploading Execption--- " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                //condition to check, if there is some problem uploading attachments
                System.out.println("result " + result);
                //method temporary called....may or may not needed here. depending on how image upload and text upload is written
            }
        };

        runAsyncTask(task);
    }

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Log.d("AsyncTask", "if..calling");
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            Log.d("AsyncTask", "else..calling");
            return task.execute();
        }
    }
}
