package com.umbcapp.gaurk.snapeve.Fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;
import com.squareup.picasso.Picasso;
import com.umbcapp.gaurk.snapeve.Adapters.SignupGrpAdapter;
import com.umbcapp.gaurk.snapeve.Adapters.TeamMatesAdapter;
import com.umbcapp.gaurk.snapeve.Adapters.UserContributionAdapter;
import com.umbcapp.gaurk.snapeve.AzureConfiguration;
import com.umbcapp.gaurk.snapeve.Controllers.Event_dash_list_obj;
import com.umbcapp.gaurk.snapeve.Controllers.SignUpGrpListItem;
import com.umbcapp.gaurk.snapeve.Controllers.TeammatesListItem;
import com.umbcapp.gaurk.snapeve.Controllers.UserContributionListItem;
import com.umbcapp.gaurk.snapeve.Controllers.AdminLeaveGroupMemberListItem;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    //    private ArrayList<CommentsListItem> userContributionList;
    private ArrayList<UserContributionListItem> userContributionList;
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
    private int grp_total_pts = 0;
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
    //    private List<CommentsListItem> grpContributionList;
    private List<UserContributionListItem> grpContributionList;
    private TextView user_profile_posts_count_text_view;
    private RoundCornerProgressBar profile_progress_bar;
    private TextView user_status;

    //1 : user tab, 2 : grp tab
    private int currentActiveTab = 1;
    private int user_admin_flag = 0;
    private int privilege_type = 1;
    private Spinner leave_grp_dialog_member_switch_spinner;
    private ProgressBar leave_grp_dialog_member_switch_spinner_progressbar;
    private ArrayList<AdminLeaveGroupMemberListItem> memberList = new ArrayList<AdminLeaveGroupMemberListItem>();
    private String selected_new_admin_id;
    private TextView leave_grp_dialog_header_textView;
    private CardView leave_grp_dialog_member_switch_spinner_card_view;
    private TextView show_pending_req_dialog_btn_grp_name_textView;
    private TextView show_pending_req_dialog_accept_invitation_label;
    private String pending_req_grp_id;
    private LinearLayout show_pending_req_dialog_btn_lin_layout;
    private TextView signup_grp_page_or_textview;
    private String joinMeToTheGrpId = null;


    public UserProfileFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userContributionList = new ArrayList<>();
        fetch_user_details();

        sessionCounter = System.currentTimeMillis();
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String text = formatter.format(new Date(sessionCounter));
//        System.out.println("Start @ sessionCounter : " + text);

        mClient = Singleton.Instance().mClientMethod(getActivity());
        mUserTable = mClient.getTable(user_table.class);

    }

    @Override
    public void onResume() {
        fetch_user_details();

        super.onResume();

        sessionCounter = System.currentTimeMillis();
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String text = formatter.format(new Date(sessionCounter));
//        System.out.println("Start @ sessionCounter : " + sessionCounter);
    }

    @Override
    public void onPause() {
        super.onPause();

        sessionCounter = System.currentTimeMillis() - sessionCounter;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(sessionCounter);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(sessionCounter);

        System.out.println("USERPROFILE onPause sessionCounter : " + minutes + "m " + seconds + "s");

    }

    private void fetchGrpPostAndMembers(String grp_id) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading, Please wait...");
        progressDialog.create();
        progressDialog.show();
        progressDialog.setCancelable(false);
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


            DateFormat srcDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            DateFormat displayDtFormat = new SimpleDateFormat("MMM dd HH:mm");

            Date postDate = null;
            try {
                postDate = srcDateFormat.parse(grpPost_Data_list_object.get("createdAt").getAsString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //skip adding tem to list if post_as = 2 (anonymous post)
            if (post_as != 2) {
                grpContributionList.add(0, new UserContributionListItem(post_id, member_id, user_name, first_name, last_name, displayDtFormat.format(postDate), img_url, dp_url, description, total_likes, total_spam, total_comments, post_as));
            }

        }

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
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading, Please wait...");
        progressDialog.create();
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        jsonObjectUserProfileFragParameters = new JsonObject();
        //might need to change it to user id from user name
        jsonObjectUserProfileFragParameters.addProperty("user_id", new SessionManager(getActivity()).getSpecificUserDetail(SessionManager.KEY_USER_ID));

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("fetch_user_details_api", jsonObjectUserProfileFragParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                progressDialog.dismiss();
                showNoResponseDialog();
                System.out.println(" fetch_user_details exception    " + exception);
            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                System.out.println(" fetch_user_details success response    " + response);
                progressDialog.dismiss();
                parseAndDivideUserInfo(response);
                fetchGrpPostAndMembers(grp_id);
//                fetchGrpPostAndMembers(grp_id, 1);
            }
        });
    }

    private void showNoResponseDialog() {
        new FancyAlertDialog.Builder(getActivity())
                .setTitle("Oopsy daisy!")
                .setBackgroundColor(Color.parseColor("#3F51B5"))  //Don't pass R.color.colorvalue
                .setMessage("Something went wrong")
                .setIcon(R.drawable.traingale_exclamation_100, Icon.Visible)
                .setPositiveBtnText("Reload")
                .setPositiveBtnBackground(Color.parseColor("#303F9F"))//Don't pass R.color.colorvalue
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        fetch_user_details();
                    }
                })
                .setNegativeBtnBackground(Color.parseColor("#003F51B5"))//Don't pass R.color.colorvalue
                .setNegativeBtnText("")
                .build();
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

            DateFormat srcDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            DateFormat displayDtFormat = new SimpleDateFormat("MMM dd HH:mm");

            Date postDate = null;
            try {
                postDate = srcDateFormat.parse(user_activity_list_object.get("createdAt").getAsString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //skip adding tem to list if post_as = 2 (anonymous post)
            if (post_as != 2) {
                userContributionList.add(0, new UserContributionListItem(post_id, user_id, user_name, first_name, last_name, displayDtFormat.format(postDate), img_url, dp_url, description, total_likes, total_spam, total_comments, post_as));
            }

            loadContributionList(0);

        }

    }

    private void parseUserDetails(JsonArray userDetails) {

        JsonObject userDetailsObj = userDetails.get(0).getAsJsonObject();

        userName = userDetailsObj.get("user_name").getAsString();
        first_name = userDetailsObj.get("first_name").getAsString();
        last_name = userDetailsObj.get("last_name").getAsString();
        user_total_pts = Integer.parseInt(userDetailsObj.get("user_points").toString());

        privilege_type = 0;
        try {
            privilege_type = Integer.parseInt(userDetailsObj.get("privilege_type").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        user_admin_flag = 0;
        try {
            user_admin_flag = Integer.parseInt(userDetailsObj.get("group_admin_flag").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setRewardProgress(user_total_pts, 0);

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
            grp_id = userDetailsObj.get("grp_id").getAsString();
            grp_name = userDetailsObj.get("grp_name").getAsString();
//            grp_dp_url = userDetailsObj.get("grp_dp_url").getAsString();
            grp_total_pts = Integer.parseInt(userDetailsObj.get("total_pts").toString());

            grp_dp_url = null;
            try {
                grp_dp_url = userDetailsObj.get("grp_dp_url").getAsString();
            } catch (Exception e) {

            }
        }

        if (privilege_type == 3 || privilege_type == 2 || privilege_type == 1) {
            user_profile_settings_imageview.setVisibility(View.VISIBLE);
        } else {
            user_profile_settings_imageview.setVisibility(View.INVISIBLE);
        }

        new SessionManager(getActivity()).setSpecificUserDetail(SessionManager.KEY_GRP_DP_URL, grp_dp_url);
        new SessionManager(getActivity()).setSpecificUserDetail(SessionManager.KEY_GRP_NAME, grp_name);
        new SessionManager(getActivity()).setSpecificUserDetail(SessionManager.KEY_GRP_ID, grp_id);
        populateUserInfo();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.userprofile, container, false);

        grp_profile_btn = (TextView) rootView.findViewById(R.id.profile_switch_grp_textview);
        user_profile_posts_count_text_view = (TextView) rootView.findViewById(R.id.user_profile_posts_count_text_view);
        user_status = (TextView) rootView.findViewById(R.id.user_status);
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
        profile_progress_bar = (RoundCornerProgressBar) rootView.findViewById(R.id.profile_progress_bar);

        user_profile_member_count_text_view = (TextView) rootView.findViewById(R.id.user_profile_member_count_text_view);
        user_profile_member_count_text_view_icon = (ImageView) rootView.findViewById(R.id.user_profile_member_count_text_view_icon);
        //gone by default, check for group avaiablity and show/hide accordingly
        user_profile_member_count_text_view.setVisibility(View.GONE);
        user_profile_member_count_text_view_icon.setVisibility(View.GONE);

//        Picasso.get().load("https://www.goldenglobes.com/sites/default/files/styles/portrait_medium/public/gallery_images/17-tomcruiseag.jpg?itok=qNj0cQGV&c=c9a73b7bdf609d72214d226ab9ea015e")
//                .fit().centerCrop().into(profile_pic_image_view);

//        loadContributionList(0);

        user_profile_contribution_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!memberListOpenFlag) {
                    UserContributionListItem selectedContributionListItem = null;

                    //Select object depending on type of tab active
                    switch (currentActiveTab) {
                        case 1:
                            selectedContributionListItem = userContributionList.get(position);
                            break;
                        case 2:
                            selectedContributionListItem = grpContributionList.get(position);
                            break;
                    }

                    Intent eventDetailIntent = new Intent(getActivity(), EventDetails.class);
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
//                if (privilege_type == 3) {
                openSettingsMenu(admin_flag, privilege_type);
//                }
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

                currentActiveTab = 1;
                user_profile_btn.setBackground(getResources().getDrawable(R.drawable.text_selection_left_seleted));
                grp_profile_btn.setBackground(getResources().getDrawable(R.drawable.text_selection_right_unseleted));
                populateUserInfo();

            }
        });

        grp_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (grp_id.equals("xxxxx____xxxxx")) {
                    Toast.makeText(getActivity(), "No group joined", Toast.LENGTH_SHORT).show();
                    checkForGroupRequest();
                } else {
                    currentActiveTab = 2;

                    user_profile_member_count_text_view.setVisibility(View.VISIBLE);
                    user_profile_member_count_text_view_icon.setVisibility(View.VISIBLE);
                    grp_profile_btn.setBackground(getResources().getDrawable(R.drawable.text_selection_right_seleted));
                    user_profile_btn.setBackground(getResources().getDrawable(R.drawable.text_selection_left_unseleted));
                    user_type_selection_status = 1;

                    System.out.println("XXXXX grp_dp_url " + grp_dp_url);
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

                    user_profile_member_count_text_view_icon.setImageResource(R.drawable.down_arrow_white_24);

                    setRewardProgress(grp_total_pts, 1);

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

    private void setRewardProgress(int reward_points, int tab_code) {
        RewardCalcuator rewardCalcuator = new RewardCalcuator();
        RewardCalcuator calculatedReward = null;

        calculatedReward = rewardCalcuator.calculate((float) reward_points);
        System.out.println("calculatedReward " + calculatedReward.getCurrent_points());
        System.out.println("calculatedReward " + calculatedReward.getMin_range());
        System.out.println("calculatedReward " + calculatedReward.getMax_range());
        System.out.println("calculatedReward " + calculatedReward.getRelativerewardsValue());
        System.out.println("calculatedReward " + calculatedReward.getLevel());

        if (reward_points == 0) {
            profile_progress_bar.setSecondaryProgress(0f);
        } else {
            profile_progress_bar.setSecondaryProgress(calculatedReward.getRelativerewardsValue());
        }
        profile_progress_bar.setMax(100f);
        profile_progress_bar.setProgress(100f);


        switch (tab_code) {
            case 0:
                user_status.setText("Contributor Level " + calculatedReward.getLevel());
                break;

            case 1:
                user_status.setText("Group Contribution Level " + calculatedReward.getLevel());
                break;
        }


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
                        switch (currentActiveTab) {
                            case 1:
                                System.out.println("check dp url " + dp_url);

                                if (dp_url == null) {
                                    Toast.makeText(getActivity(), "No Image found", Toast.LENGTH_SHORT).show();
                                } else {
                                    full_screen_imageview_layout.setVisibility(View.VISIBLE);
                                    Picasso.get().load(dp_url).fit().centerInside().into(full_screen_imageview);
                                }
                                break;
                            case 2:
                                if (grp_dp_url == null || grp_dp_url.equals("xxxxx____xxxxx")) {
                                    Toast.makeText(getActivity(), "No Image found", Toast.LENGTH_SHORT).show();
                                } else {
                                    full_screen_imageview_layout.setVisibility(View.VISIBLE);
                                    Picasso.get().load(grp_dp_url).fit().centerInside().into(full_screen_imageview);
                                }
                                break;
                        }
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

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading group requests, Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.create();
        progressDialog.show();

        JsonObject jsonObjectParameters = new JsonObject();
        jsonObjectParameters.addProperty("user_id", new SessionManager(getActivity()).getSpecificUserDetail(SessionManager.KEY_USER_ID));

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("check_user_group_requests_api", jsonObjectParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                progressDialog.dismiss();
                System.out.println(" check_user_group_requests_api exception    " + exception);
            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                progressDialog.dismiss();
                System.out.println(" check_user_group_requests_api success response    " + response);
                parseInvitationsData(response);

            }
        });
    }

    private void parseInvitationsData(JsonElement invitationsResponse) {
        signupGrpList = new ArrayList<SignUpGrpListItem>();

        System.out.println(" IN PARSE JASON");

        JsonArray groupsJSONArray = invitationsResponse.getAsJsonArray();

        if (groupsJSONArray.size() == 0) {

            System.out.println("No grp request present, show signup grp page");
            Intent joinGrpIntent = new Intent(getActivity(), Signup_grp_join.class);
            joinGrpIntent.putExtra("page_open_mode", 1);
            startActivity(joinGrpIntent);

        } else {
            showPendingGrpRequestDialog();
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
                if (req_code == 10) {
                    show_pending_req_dialog_btn_lin_layout.setVisibility(View.VISIBLE);
                    show_pending_req_dialog_btn_grp_name_textView.setVisibility(View.VISIBLE);
                    show_pending_req_dialog_btn_grp_name_textView.setText("Request sent : " + grp_name);
                    pending_req_grp_id = grp_id;

                    if (groupsJSONArray.size() > 1) {
                        signup_grp_page_or_textview.setVisibility(View.VISIBLE);
                    } else {
                        signup_grp_page_or_textview.setVisibility(View.INVISIBLE);
                    }
                }

                if (signupGrpList.size() > 0) {
                    System.out.println(" signupGrpList " + signupGrpList.size());
                    SignupGrpAdapter signupGrpAdapter = new SignupGrpAdapter(getActivity(), signupGrpList);
                    show_pending_req_dialog_pending_req_listview.setAdapter(signupGrpAdapter);
                    show_pending_req_dialog_accept_invitation_label.setVisibility(View.VISIBLE);
                }

            }
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
        setRewardProgress(user_total_pts, 0);

        user_profile_member_count_text_view.setVisibility(View.GONE);
        user_profile_member_count_text_view_icon.setVisibility(View.GONE);
    }

    private void openSettingsMenu(int admin_flag, final int privilege_type) {

        PopupMenu popup = new PopupMenu(getActivity(), user_profile_settings_imageview);
        if (privilege_type == 3) {
            if (admin_flag == 1) {
                popup.getMenuInflater().inflate(R.menu.user_profile_3_settings_popup_menu, popup.getMenu());
            } else {
                popup.getMenuInflater().inflate(R.menu.user_profile_2_settings_popup_menu, popup.getMenu());
            }
        }
        if (privilege_type == 2 || privilege_type == 1) {
            popup.getMenuInflater().inflate(R.menu.user_profile_1_settings_popup_menu, popup.getMenu());
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getTitle().equals("Manage Members")) {
//                    Toast.makeText(getActivity(), "Manage Members", Toast.LENGTH_SHORT).show();
                    Intent manageMembersIntent = new Intent(getActivity(), ManageGroups.class);
                    manageMembersIntent.putExtra("Grp_id", grp_id);
                    manageMembersIntent.putExtra("Grp_name", grp_name);
                    manageMembersIntent.putExtra("Grp_dp_url", grp_dp_url);
                    manageMembersIntent.putExtra("user_admin_flag", user_admin_flag);
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

        final CardView leave_grp_dialog_confirm_btn_card_view = (CardView) view.findViewById(R.id.leave_grp_dialog_confirm_btn_card_view);
        CardView leave_grp_dialog_cancel_btn_card_view = (CardView) view.findViewById(R.id.leave_grp_dialog_cancel_btn_card_view);
        leave_grp_dialog_member_switch_spinner_card_view = (CardView) view.findViewById(R.id.leave_grp_dialog_member_switch_spinner_card_view);
        leave_grp_dialog_member_switch_spinner = (Spinner) view.findViewById(R.id.leave_grp_dialog_member_switch_spinner);
        leave_grp_dialog_header_textView = (TextView) view.findViewById(R.id.leave_grp_dialog_header_textView);
        leave_grp_dialog_member_switch_spinner_progressbar = (ProgressBar) view.findViewById(R.id.leave_grp_dialog_member_switch_spinner_progressbar);

        leave_grp_dialog_cancel_btn_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        if (user_admin_flag == 1) {

            adminLeavesGroupTwoSteppedAPI(1, "");

            leave_grp_dialog_header_textView.setText("Do you really wish to leave this group? You need to pass on your admin rights to another group member before you leave");
            leave_grp_dialog_member_switch_spinner_card_view.setVisibility(View.VISIBLE);
            leave_grp_dialog_confirm_btn_card_view.setVisibility(View.INVISIBLE);

            leave_grp_dialog_confirm_btn_card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    adminLeavesGroupTwoSteppedAPI(2, selected_new_admin_id);

//                    executeLeaveGroupApi(new SessionManager(getActivity()).getSpecificUserDetail(SessionManager.KEY_USER_ID), new SessionManager(getActivity()).getSpecificUserDetail(SessionManager.KEY_GRP_ID), 4);
                }
            });
        } else {
            leave_grp_dialog_header_textView.setText("Do you really wish to leave this group?");
            leave_grp_dialog_member_switch_spinner_card_view.setVisibility(View.GONE);
            leave_grp_dialog_member_switch_spinner_progressbar.setVisibility(View.GONE);
            leave_grp_dialog_confirm_btn_card_view.setVisibility(View.VISIBLE);

            leave_grp_dialog_confirm_btn_card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    executeLeaveGroupApi(new SessionManager(getActivity()).getSpecificUserDetail(SessionManager.KEY_USER_ID), new SessionManager(getActivity()).getSpecificUserDetail(SessionManager.KEY_GRP_ID), 4);
                }
            });

        }

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();

        leave_grp_dialog_member_switch_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    leave_grp_dialog_confirm_btn_card_view.setVisibility(View.VISIBLE);
                    selected_new_admin_id = memberList.get(position - 1).getUserId();
                } else {
                    leave_grp_dialog_confirm_btn_card_view.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void adminLeavesGroupTwoSteppedAPI(final int step_code, String new_admin_id) {

        leave_grp_dialog_member_switch_spinner_progressbar.setVisibility(View.VISIBLE);
        JsonObject jsonObjectParameters = new JsonObject();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());

        if (step_code == 2) {
            progressDialog.setMessage("Updating, Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.create();
            progressDialog.show();

            jsonObjectParameters.addProperty("new_admin_code", new_admin_id);
        }

        jsonObjectParameters.addProperty("old_admin_code", new SessionManager(getActivity()).getSpecificUserDetail(SessionManager.KEY_USER_ID));
        jsonObjectParameters.addProperty("step_code", step_code);
        jsonObjectParameters.addProperty("grpId", new SessionManager(getActivity()).getSpecificUserDetail(SessionManager.KEY_GRP_ID));
        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("admin_leaves_group_two_stepped_API", jsonObjectParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                leave_grp_dialog_member_switch_spinner_progressbar.setVisibility(View.INVISIBLE);
                System.out.println(" admin_leaves_group_two_stepped_API exception    " + exception);
                if (step_code == 2) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                leave_grp_dialog_member_switch_spinner_progressbar.setVisibility(View.INVISIBLE);
                System.out.println(" admin_leaves_group_two_stepped_API success response    " + response);


                if (step_code == 1) {
                    parseMemberList(response);
                }
                if (step_code == 2) {
                    progressDialog.dismiss();
                    if (response.toString().contains("true")) {
                        System.out.println("response OK");
                        fetch_user_details();
                    }
                }
            }
        });
    }

    private void parseMemberList(JsonElement response) {

        memberList = new ArrayList<AdminLeaveGroupMemberListItem>();
        JsonArray grpMembers = response.getAsJsonArray();

        if (grpMembers.size() != 0) {

            List<String> members = new ArrayList<String>();
            members.add("[ Select a member ]");

            for (int j = 0; j < grpMembers.size(); j++) {

                JsonObject grpMembers_list_object = grpMembers.get(j).getAsJsonObject();
                memberList.add(new AdminLeaveGroupMemberListItem(grpMembers_list_object.get("user_name").getAsString(), grpMembers_list_object.get("user_id").getAsString()));

                //fill spinner list
                members.add(grpMembers_list_object.get("user_name").getAsString());
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.blank_first_simple_spinner_item, members);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            leave_grp_dialog_member_switch_spinner_card_view.setVisibility(View.VISIBLE);
            leave_grp_dialog_member_switch_spinner.setAdapter(dataAdapter);
        } else {

//            Toast.makeText(getActivity(), "No members left. Delete group instead", Toast.LENGTH_SHORT).show();
            leave_grp_dialog_header_textView.setText("No members left. Delete group instead");
            leave_grp_dialog_member_switch_spinner_card_view.setVisibility(View.INVISIBLE);
        }
    }

    private void executeLeaveGroupApi(String userId, String grpId, int modify_code) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Updating, Please wait...");
        progressDialog.create();
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        JsonObject jsonObjectParameters = new JsonObject();
        jsonObjectParameters.addProperty("modify_code", modify_code);
        jsonObjectParameters.addProperty("grpId", grpId);
        jsonObjectParameters.addProperty("userId", userId);

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("modify_privileges_api", jsonObjectParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                progressDialog.dismiss();
                System.out.println(" modify_privileges_api exception    " + exception);

            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                progressDialog.dismiss();
                System.out.println(" modify_privileges_api success response    " + response);

                Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();

                if (response.toString().contains("true")) {
                    System.out.println("response OK");
                    fetch_user_details();

                }

            }
        });


    }

    private void executeDeleteGroupApi() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Updating, Please wait...");
        progressDialog.create();
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        JsonObject jsonObjectParameters = new JsonObject();
        jsonObjectParameters.addProperty("grpId", grp_id);
        jsonObjectParameters.addProperty("adminId", new SessionManager(getActivity()).getSpecificUserDetail(SessionManager.KEY_USER_ID));

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("delete_group_api", jsonObjectParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                progressDialog.dismiss();
                System.out.println(" delete_group_api exception    " + exception);

            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                progressDialog.dismiss();
                System.out.println(" delete_group_api success response    " + response);

                Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();

                if (response.toString().contains("true")) {
                    System.out.println("response OK");
                    fetch_user_details();

                }

            }
        });

    }

    private void showPendingGrpRequestDialog() {
        final Dialog alertDialog = new Dialog(getActivity());
        LayoutInflater flater = getActivity().getLayoutInflater();
        View view = flater.inflate(R.layout.show_pending_grp_req_dialog, null);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(view);

        show_pending_req_dialog_btn_grp_name_textView = (TextView) view.findViewById(R.id.show_pending_req_dialog_btn_grp_name_textView);
        show_pending_req_dialog_accept_invitation_label = (TextView) view.findViewById(R.id.show_pending_req_dialog_accept_invitation_label);
        signup_grp_page_or_textview = (TextView) view.findViewById(R.id.signup_grp_page_or_textview);
        CardView show_pending_req_dialog_cancel_req_btn_card_view = (CardView) view.findViewById(R.id.show_pending_req_dialog_cancel_req_btn_card_view);
        CardView show_pending_req_dialog_ok_btn_card_view = (CardView) view.findViewById(R.id.show_pending_req_dialog_ok_btn_card_view);
        show_accept_invitation_dialog_invitations_layout = (RelativeLayout) view.findViewById(R.id.show_accept_invitation_dialog_invitations_layout);
        show_pending_req_dialog_btn_lin_layout = (LinearLayout) view.findViewById(R.id.show_pending_req_dialog_btn_lin_layout);
        show_pending_req_dialog_pending_req_listview = (ListView) view.findViewById(R.id.show_pending_req_dialog_pending_req_listview);

        show_pending_req_dialog_btn_lin_layout.setVisibility(View.GONE);
        show_pending_req_dialog_btn_grp_name_textView.setVisibility(View.GONE);
        show_pending_req_dialog_accept_invitation_label.setVisibility(View.GONE);

//        show_pending_req_dialog_progressBar = (ProgressBar) view.findViewById(R.id.show_pending_req_dialog_progressBar);
//        show_pending_req_dialog_progressBar_label = (TextView) view.findViewById(R.id.show_pending_req_dialog_progressBar_label);
//        show_pending_req_dialog_progressBar.setVisibility(View.VISIBLE);
//        show_pending_req_dialog_progressBar_label.setVisibility(View.VISIBLE);

//        System.out.println("PENDING " + new SessionManager(getActivity()).getSpecificUserDetail(SessionManager.KEY_REQ_PENDING_GRP_NAME));

//        show_pending_req_dialog_btn_grp_name_textView.setText("Pending request : " + new SessionManager(getActivity()).getSpecificUserDetail(SessionManager.KEY_REQ_PENDING_GRP_NAME));

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
        alertDialog.setCancelable(true);
        alertDialog.show();

        show_pending_req_dialog_pending_req_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alertDialog.dismiss();
                confirmAcceptJoiningGrp(signupGrpList.get(position).getGrpName());
                joinMeToTheGrpId = signupGrpList.get(position).getGrpId();
            }
        });

    }

    private void confirmAcceptJoiningGrp(String grpName) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Confirm joining " + grpName)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        joinMeToTheGroup();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.out.println("No ref code, GO BACK ");

                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void joinMeToTheGroup() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Sending request, Please wait...");
        progressDialog.create();
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        JsonObject jsonObjectParameters = new JsonObject();
        jsonObjectParameters.addProperty("req_code", 0);
        jsonObjectParameters.addProperty("grpId", joinMeToTheGrpId);
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

                Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();

                if (response.toString().contains("true")) {
                    System.out.println("response OK");
                    fetch_user_details();
                }

            }
        });
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
//        jsonObjectParameters.addProperty("grpId", new SessionManager(getActivity()).getSpecificUserDetail(SessionManager.KEY_REQ_PENDING_GRP_ID));
        jsonObjectParameters.addProperty("grpId", pending_req_grp_id);
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
            userContributionAdapter = new UserContributionAdapter(getActivity(), userContributionList);
            user_profile_contribution_list_view.setAdapter(userContributionAdapter);

            user_profile_posts_count_text_view.setVisibility(View.VISIBLE);
            if (userContributionList.size() == 0) {
                user_profile_posts_count_text_view.setText("No Posts Found");
            } else if (userContributionList.size() == 1) {
                user_profile_posts_count_text_view.setText("1 Post");
            } else {
                user_profile_posts_count_text_view.setText(userContributionList.size() + " Posts");
            }

        }
        if (user_type_selection_status == 1) {
            memberListOpenFlag = false;
            userContributionAdapter = new UserContributionAdapter(getActivity(), grpContributionList);

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

            user_profile_posts_count_text_view.setVisibility(View.VISIBLE);
            if (grpContributionList.size() == 0) {
                user_profile_posts_count_text_view.setText("No Posts Found");
            } else if (grpContributionList.size() == 1) {
                user_profile_posts_count_text_view.setText("1 Post");
            } else {
                user_profile_posts_count_text_view.setText(grpContributionList.size() + " Posts");
            }

        }
        if (user_type_selection_status == 2) {

            TeamMatesAdapter teamMatesAdapter = new TeamMatesAdapter(getActivity(), userProfileList);
            user_profile_contribution_list_view.setAdapter(teamMatesAdapter);
            user_profile_posts_count_text_view.setVisibility(View.GONE);

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

//        sessionCounter = System.currentTimeMillis() - sessionCounter;
//        long minutes = TimeUnit.MILLISECONDS.toMinutes(sessionCounter);
//        long seconds = TimeUnit.MILLISECONDS.toSeconds(sessionCounter);
//
//        System.out.println("PROFILE sessionCounter : " + minutes + "m " + seconds + "s");
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
