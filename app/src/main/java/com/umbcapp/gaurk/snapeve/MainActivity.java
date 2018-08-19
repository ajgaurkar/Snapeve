package com.umbcapp.gaurk.snapeve;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;
import com.umbcapp.gaurk.snapeve.Adapters.Dash_Event_ListAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.Event_dash_list_obj;
import com.umbcapp.gaurk.snapeve.Fragments.MapsFragment;
import com.umbcapp.gaurk.snapeve.Fragments.NotificationFragment;
import com.umbcapp.gaurk.snapeve.Fragments.SettingsFragment;
import com.umbcapp.gaurk.snapeve.Fragments.UserProfileFragment;

import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements Listview_communicator {

    public static MobileServiceClient mClient;
    private JsonObject jsonObjectLoginParameters;
    private UserProfileFragment userProfileFragment;
    private SettingsFragment settingsFragment;
    private MapsFragment mapsFragment;
    private NotificationFragment notificationFragment;
    private SwipeRefreshLayout pullToRefresh;
    private long sessionCounter;
    private ListView main_event_list_view;
    private ArrayList<Event_dash_list_obj> event_main_list;
    private FloatingActionButton main_img_pick_fab;
    private Dash_Event_ListAdapter dash_event_listAdapter;
    private JsonObject jsonObjectUserProfileParameters;
    private String dp_url = null;
    private int user_total_pts;
    private String last_name;
    private String userName;
    private String first_name;
    private int admin_flag;
    private String grp_id;
    private String grp_name;
    private String grp_dp_url;
    private int grp_total_pts;
    private Spinner add_comment_dialog_user_name_spinner;
    private TextView add_comment_dialog_clear_textview;
    private EditText add_comment_dialog_comment_edittext;
    private String tempCommentDescVar;
    private int server_action_like = 0;
    private int server_action_spam = 0;
    int selcted_item_position = -1;
//    private Button refreshBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionCounter = System.currentTimeMillis();
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String text = formatter.format(new Date(sessionCounter));
        System.out.println("Start @ sessionCounter : " + text);

        new SessionManager(getApplicationContext()).checkLogin();

        System.out.println("2 LOGIN USER ID " + new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_USER_ID));
        System.out.println("2 LOGIN GRP ID " + new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_GRP_ID));
        System.out.println("2 LOGIN KEY_GRP_NAME " + new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_GRP_NAME));
        System.out.println("2 LOGIN KEY_REQ_PENDING_GRP_ID " + new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_REQ_PENDING_GRP_ID));


        testNotification();

        event_main_list = new ArrayList<Event_dash_list_obj>();
        main_event_list_view = (ListView) findViewById(R.id.dashboard_event_listview);
        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.dashboard_pull_refresh_layout);

        main_img_pick_fab = (FloatingActionButton) findViewById(R.id.main_img_pick_fab);

        userProfileFragment = new UserProfileFragment();
        settingsFragment = new SettingsFragment();
        mapsFragment = new MapsFragment();
        notificationFragment = new NotificationFragment();

        // Mobile Service URL and key
        //Old access url(aj account)
        try {
            //New access url(amey account)
            mClient = Singleton.Instance().mClientMethod(this);

        } catch (Exception e) {
            e.printStackTrace();
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        executeGetFeedsApi();

        main_img_pick_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Add_event.class));
            }
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                executeGetFeedsApi();
            }
        });

        System.out.println("GRPP ID " + new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_GRP_ID));


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction1;
        //        FragmentTransaction fragmentTransaction2;

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    fragmentTransaction1 = fragmentManager.beginTransaction();
                    fragmentTransaction1.remove(mapsFragment);
                    fragmentTransaction1.remove(userProfileFragment);
                    fragmentTransaction1.remove(settingsFragment);
                    fragmentTransaction1.remove(notificationFragment);
                    fragmentTransaction1.commit();

                    System.out.print("home");
                    Toast.makeText(getApplicationContext(), "toast", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.navigation_maps:
                    System.out.print("dashboard");

                    fragmentTransaction1 = fragmentManager.beginTransaction();
                    fragmentTransaction1.replace(R.id.dashboard_main_frame_layout, mapsFragment);
                    fragmentTransaction1.commit();

                    Toast.makeText(getApplicationContext(), "toast", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.navigation_user:
//                    startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));

                    fragmentTransaction1 = fragmentManager.beginTransaction();
                    fragmentTransaction1.replace(R.id.dashboard_main_frame_layout, userProfileFragment);
                    fragmentTransaction1.commit();


                    System.out.print("navigation_user");
                    Toast.makeText(getApplicationContext(), "toast", Toast.LENGTH_SHORT).show();
                    return true;

                case R.id.navigation_notification:
//                    startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));

                    fragmentTransaction1 = fragmentManager.beginTransaction();
                    fragmentTransaction1.replace(R.id.dashboard_main_frame_layout, notificationFragment);
                    fragmentTransaction1.commit();

                    testNotification();

                    System.out.print("notificationFragment");
                    Toast.makeText(getApplicationContext(), "toast", Toast.LENGTH_SHORT).show();
                    return true;

                case R.id.navigation_settings:
//                    startActivity(new Intent(getApplicationContext(), MapsActivityFragment.class));
                    System.out.print("notification");
                    fragmentTransaction1 = fragmentManager.beginTransaction();
                    fragmentTransaction1.replace(R.id.dashboard_main_frame_layout, settingsFragment);
                    fragmentTransaction1.commit();
                    Toast.makeText(getApplicationContext(), "toast", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }
    };

    private void testNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.walking_48);
        builder.setTicker("Ticker");
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle("Titke");
        builder.setContentText("Content");

        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(8978, builder.build());

    }

    private void executeGetFeedsApi() {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading feeds, Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.create();
        progressDialog.show();
        jsonObjectLoginParameters = new JsonObject();
        jsonObjectLoginParameters.addProperty("logged_in_user_id", new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_USER_ID));

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
//        ListenableFuture<JsonElement> serviceFilterFuture = mClient.invokeApi("Login_api", jsonObjectLoginParameters);
        ListenableFuture<JsonElement> serviceFilterFuture = mClient.invokeApi("get_event_feeds_api", jsonObjectLoginParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                System.out.println(" get_event_feeds_api exception    " + exception);
                pullToRefresh.setRefreshing(false);
                progressDialog.dismiss();
                fetchUserDetails();

//                Toast.makeText(MainActivity.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
                showNoResponseDialog();
            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                System.out.println(" get_event_feeds_api success response    " + response);

                fetchUserDetails();

                progressDialog.dismiss();
                poupulateList(response);
                pullToRefresh.setRefreshing(false);

            }
        });
    }

    private void showNoResponseDialog() {
        new FancyAlertDialog.Builder(MainActivity.this)
                .setTitle("Oopsy daisy!")
                .setBackgroundColor(Color.parseColor("#3F51B5"))  //Don't pass R.color.colorvalue
                .setMessage("Something went wrong")
                .setIcon(R.drawable.traingale_exclamation_100, Icon.Visible)
                .setPositiveBtnText("Reload")
                .setPositiveBtnBackground(Color.parseColor("#303F9F"))//Don't pass R.color.colorvalue
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {

                        executeGetFeedsApi();
                    }
                })
                .setNegativeBtnBackground(Color.parseColor("#003F51B5"))//Don't pass R.color.colorvalue
                .setNegativeBtnText("")
                .build();
    }

    private void fetchUserDetails() {
        jsonObjectUserProfileParameters = new JsonObject();
        //might need to change it to user id from user name
        jsonObjectUserProfileParameters.addProperty("user_name", new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_USER_NAME));

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("fetch_user_details_api", jsonObjectUserProfileParameters);

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

                parseResponse(response.getAsJsonObject().getAsJsonArray("userDetails"));

            }
        });
    }

    private void parseResponse(JsonArray userDetails) {
        JsonObject userDetailsObj = userDetails.get(0).getAsJsonObject();

        userName = userDetailsObj.get("user_name").getAsString();
        first_name = userDetailsObj.get("first_name").getAsString();
        last_name = userDetailsObj.get("last_name").getAsString();
        user_total_pts = Integer.parseInt(userDetailsObj.get("user_points").toString());

        try {
            dp_url = userDetailsObj.get("dp_url").getAsString();
            System.out.println("Main activity user dp_url : " + dp_url);
        } catch (Exception e) {
            e.printStackTrace();
            dp_url = null;
            System.out.println(" dp_url is null, set local image");
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

            try {
                grp_dp_url = userDetailsObj.get("grp_dp_url").getAsString();
                grp_total_pts = userDetailsObj.get("total_pts").getAsInt();
                System.out.println("Main activity grp_dp_url : " + grp_dp_url);
            } catch (Exception e) {
                e.printStackTrace();
                grp_dp_url = null;
                grp_total_pts = 0;
                System.out.println(" grp_dp_url is null, set local image");
            }
        }

        System.out.println("SESSION MANAGER 1");
        System.out.println(grp_dp_url);
        System.out.println(grp_name);
        System.out.println(grp_id);
        new SessionManager(getApplicationContext()).setSpecificUserDetail(SessionManager.KEY_GRP_DP_URL, grp_dp_url);
        new SessionManager(getApplicationContext()).setSpecificUserDetail(SessionManager.KEY_GRP_NAME, grp_name);
        new SessionManager(getApplicationContext()).setSpecificUserDetail(SessionManager.KEY_GRP_ID, grp_id);
        System.out.println("SESSION MANAGER 2");
    }

    private void poupulateList(JsonElement response) {

        event_main_list = new ArrayList<Event_dash_list_obj>();
        DateFormat feedsDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        DateFormat displayDtFormat = new SimpleDateFormat("MMM dd HH:mm");

        System.out.println(" IN PARSE JASON");

        JsonArray feedsJsonArray = response.getAsJsonArray();

        for (int j = 0; j < feedsJsonArray.size(); j++) {
            JsonObject feeds_list_object = feedsJsonArray.get(j).getAsJsonObject();
            System.out.println(" feeds_list_object  " + feeds_list_object);

            String post_id = null;
            try {
                post_id = feeds_list_object.get("post_id").getAsString();
                String post_img_url = feeds_list_object.get("img_url").getAsString();
                String post_user_id = feeds_list_object.get("initializer_id").getAsString();
                String post_dt = feeds_list_object.get("post_date").getAsString();
                String event_start_dt_time = feeds_list_object.get("event_start_dt_time").getAsString();
                String event_end_dt_time = feeds_list_object.get("event_end_dt_time").getAsString();
                boolean event_all_day_status = feeds_list_object.get("all_day").getAsBoolean();
                String initializer_name = feeds_list_object.get("initializer_name").getAsString();
                int post_as = feeds_list_object.get("post_as").getAsInt();
                int event_type = feeds_list_object.get("event_type").getAsInt();
                int scope = feeds_list_object.get("scope").getAsInt();
                int location_type = feeds_list_object.get("location_type").getAsInt();


                //getting likes and spam data/count. replace null with 0
                //5 attributes
                int total_likes = 0;
                try {
                    total_likes = feeds_list_object.get("total_likes").getAsInt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int total_spam = 0;
                try {
                    total_spam = feeds_list_object.get("total_spam").getAsInt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int user_spam = 0;
                try {
                    user_spam = feeds_list_object.get("user_spam").getAsInt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int user_likes = 0;
                try {
                    user_likes = feeds_list_object.get("user_likes").getAsInt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int total_comments = 0;
                try {
                    total_comments = feeds_list_object.get("total_comments").getAsInt();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String img_comment = null;
                try {
                    img_comment = feeds_list_object.get("img_comment").getAsString();
                } catch (Exception e) {
                    e.printStackTrace();
                    img_comment = "";
                }

                String post_grp_id = null;
                String post_grp_dp_url = null;
                String post_grp_name = null;
                try {
                    //sequence of these 3 thing sis importatnt
                    //if id is null then everything should be null
                    //if id is present then name will be definatly present but dp_url may or may not be present so it can be kepy last for exception
                    post_grp_id = feeds_list_object.get("grp_id").getAsString();
                    post_grp_name = feeds_list_object.get("grp_name").getAsString();

                    post_grp_dp_url = feeds_list_object.get("grp_dp_url").getAsString();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String post_dp_url = null;
                try {
                    post_dp_url = feeds_list_object.get("dp_url").getAsString();
                    System.out.println("Main activity feeds user dp_url : " + post_dp_url);
                } catch (Exception e) {
                    e.printStackTrace();
                    post_dp_url = null;
                }

                Date date = null;
                try {
                    date = feedsDateFormat.parse(post_dt);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                System.out.println(date);
                post_dt = displayDtFormat.format(date);
                System.out.println("Report Date: " + post_dt);

                event_main_list.add(0, new Event_dash_list_obj(post_user_id, post_dp_url, initializer_name, img_comment,
                        post_dt, post_img_url, post_id, post_dt, event_start_dt_time, event_end_dt_time,
                        event_all_day_status, post_as, event_type, location_type, scope, post_grp_name, post_grp_id, post_grp_dp_url,
                        user_likes, user_spam, total_likes, total_spam, total_comments));


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(" event_main_list " + event_main_list.size());
        dash_event_listAdapter = new Dash_Event_ListAdapter(this, event_main_list);
        main_event_list_view.setAdapter(dash_event_listAdapter);
    }

    private void openAddCommentDialog(final int selectedItemPosition) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater flater = this.getLayoutInflater();
        View view = flater.inflate(R.layout.add_comment_dialog, null);
        alertDialog.setView(view);
        alertDialog.setTitle("Add comment");
        alertDialog.setCancelable(false);


        add_comment_dialog_user_name_spinner = (Spinner) view.findViewById(R.id.add_comment_dialog_user_name_spinner);

//        add_comment_dialog_clear_textview = (TextView) view.findViewById(R.id.add_comment_dialog_clear_textview);
        add_comment_dialog_comment_edittext = (EditText) view.findViewById(R.id.add_comment_dialog_comment_edittext);
        add_comment_dialog_comment_edittext.setText(tempCommentDescVar);

//        add_comment_dialog_clear_textview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                add_comment_dialog_user_name_spinner.setSelection(0);
//            }
//        });

        alertDialog.setPositiveButton("Comment", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                validateAndPostComment(add_comment_dialog_comment_edittext.getText().toString().trim(), selectedItemPosition);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();
    }

    private void validateAndPostComment(String comment, final int selectedItemPosition) {

        String[] userIdArray = {null, "13e2388c-5102-4e91-980d-fc97d044a483",
                "ed77236d-0526-4019-b77e-9cdae29b9017",
                "f034e41d-42e5-48c1-8b2e-9d5e79d1c7ed",
                "1a4bb4a2-0afa-4c9e-9ea6-22b5789baad7",
                "73d62c6f-1c32-420a-89c5-89c987ed5aa3"};

        tempCommentDescVar = comment;
//        tempCommentSpinnerVar = selectedItemPosition;

        if (!comment.equals("")) {
            final ProgressDialog mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Commenting. Please wait...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

            JsonObject jsonObjectPostEventParameters = new JsonObject();

            jsonObjectPostEventParameters.addProperty("source_user_id", new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_USER_ID));
//            jsonObjectPostEventParameters.addProperty("target_user_id", userIdArray[selectedItemPosition]);
            jsonObjectPostEventParameters.addProperty("target_user_id", userIdArray[0]);
            jsonObjectPostEventParameters.addProperty("post_id", event_main_list.get(selectedItemPosition).getPost_id());
            jsonObjectPostEventParameters.addProperty("commentDate", Calendar.getInstance().getTimeInMillis());
            jsonObjectPostEventParameters.addProperty("comment", comment);

            final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
            ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("image_comments_api", jsonObjectPostEventParameters);

            Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
                @Override
                public void onFailure(Throwable exception) {
                    resultFuture.setException(exception);
                    Toast.makeText(getApplicationContext(), "Something went wrong. Try again", Toast.LENGTH_SHORT).show();

                    openAddCommentDialog(selectedItemPosition);

                    System.out.println(" image_comments_api exception    " + exception);
                    mProgressDialog.dismiss();
                }

                @Override
                public void onSuccess(JsonElement response) {
                    resultFuture.set(response);
                    System.out.println(" image_comments_api success response    " + response);
                    validateCommentResponse(response, selectedItemPosition);

                    //reset values if success otherwise need these values o repopulate dialog if api call or validation failed
//                    tempCommentSpinnerVar = 0;
                    tempCommentDescVar = "";
                    mProgressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Comment field empty. Try again", Toast.LENGTH_SHORT).show();
            openAddCommentDialog(selectedItemPosition);

        }
    }

    private void validateCommentResponse(JsonElement response, int selectedItemPosition) {

        System.out.println("validateCommentResponse : " + response);
        if (!response.toString().contains("true")) {
            Toast.makeText(getApplicationContext(), "Something went wrong. Try again", Toast.LENGTH_SHORT).show();
            openAddCommentDialog(selectedItemPosition);

        }
    }

    @Override
    public void main_event_listview_element_clicked(int position, int click_code) {

        selcted_item_position = position;
        System.out.print("position " + selcted_item_position);
        System.out.print("click_code " + click_code);

        int currentstatus = 0;
        String userComment = "";
        switch (click_code) {


            case 0:
                Log.d("click_code ", +selcted_item_position + " " + click_code);
                openEventDetails(selcted_item_position);
                break;
            case 1:
                Log.d("click_code ", +selcted_item_position + " " + click_code);
                calculateAction(selcted_item_position, click_code);

//                actionEvent(event_main_list.get(position).getPost_id(), event_main_list.get(position).getUser_id(), currentstatus, click_code, userComment);

                break;
            case 2:
                userComment = "Test comment";
                Log.d("click_code ", +selcted_item_position + " " + click_code);
//                actionEvent(event_main_list.get(position).getPost_id(), event_main_list.get(position).getUser_id(), currentstatus, click_code, userComment);
                openAddCommentDialog(selcted_item_position);
                break;
            case 3:
                calculateAction(selcted_item_position, click_code);

                System.out.print("click_code " + selcted_item_position + " " + click_code);
//                actionEvent(event_main_list.get(position).getPost_id(), event_main_list.get(position).getUser_id(), currentstatus, click_code, userComment);
                break;
        }

    }

    private void calculateAction(int selcted_item_position, int click_code) {
        //action_to_be_performed 1: insert , 2: delete , 3 : update from 1 to other
        //click_code 1: like , 2:comment(not used here) 3:spam

        //get Server values of likes and spam for action
        server_action_like = event_main_list.get(selcted_item_position).getUser_like();
        server_action_spam = event_main_list.get(selcted_item_position).getUser_spam();

        switch (click_code) {
            case 1:
                if (server_action_like + server_action_spam == 0) {
                    System.out.println("ACTION ACTION : insert like");
                    actionEvent(1, click_code, "Test");

                }
                if (server_action_like + server_action_spam == 1) {
                    if (server_action_like == 1) {
                        System.out.println("ACTION ACTION : delete like");
                        actionEvent(2, click_code, "Test");

                    }
                    if (server_action_like == 0) {
                        System.out.println("ACTION ACTION : show dialog - update SPAM -> LIKE ");

                        openLikeSpamConfirmationDialog(click_code, selcted_item_position);

                    }
                }
                break;

            case 3:
                if (server_action_like + server_action_spam == 0) {
                    System.out.println("ACTION ACTION : insert spam");
                    actionEvent(1, click_code, "Test");

                }
                if (server_action_like + server_action_spam == 1) {
                    if (server_action_spam == 1) {
                        System.out.println("ACTION ACTION : delete spam");
                        actionEvent(2, click_code, "Test");

                    }
                    if (server_action_spam == 0) {
                        System.out.println("ACTION ACTION : show dialog - update LIKE -> SPAM ");
                        openLikeSpamConfirmationDialog(click_code, selcted_item_position);

                    }
                }
                break;
        }
    }

    private void openLikeSpamConfirmationDialog(final int click_code, int selcted_position) {
        System.out.print("IN DIALOG");
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        builder1.setCancelable(false);

        if (click_code == 1) {
            System.out.println("Do you wish to UNSPAM and LIKE");
            builder1.setMessage("Do you wish to UNSPAM and VERIFY");

        }
        if (click_code == 3) {
            System.out.println("Do you wish to UNLIKE and SPAM");
            builder1.setMessage("Do you wish to DISPROVE and SPAM");

        }

        builder1.setPositiveButton(
                "CONFIRM",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        actionEvent(3, click_code, "Test");

                    }
                });

        builder1.setNegativeButton(
                "CANCLE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder1.create();
        alertDialog.show();


    }

    //action_to_be_performed 1: insert , 2: delete , 3 : update from 1 to other
    //click_code 1: like , 2:comment(not used here) 3:spam
    private void actionEvent(int action_to_be_performed, int click_code, String userComment) {
        JsonObject jsonObjectPostEventParameters = new JsonObject();


        if (click_code == 1) {
            jsonObjectPostEventParameters.addProperty("verify_status", 1);
            jsonObjectPostEventParameters.addProperty("spam_status", 0);
        }
        if (click_code == 2) {
            jsonObjectPostEventParameters.addProperty("verify_status", 0);
            jsonObjectPostEventParameters.addProperty("spam_status", 0);
        }
        if (click_code == 3) {
            jsonObjectPostEventParameters.addProperty("verify_status", 0);
            jsonObjectPostEventParameters.addProperty("spam_status", 1);
        }

        jsonObjectPostEventParameters.addProperty("userComment", userComment);
        jsonObjectPostEventParameters.addProperty("user_id", new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_USER_ID));
        jsonObjectPostEventParameters.addProperty("post_id", event_main_list.get(selcted_item_position).getPost_id());
        jsonObjectPostEventParameters.addProperty("click_code", click_code);
        jsonObjectPostEventParameters.addProperty("action_to_be_performed", action_to_be_performed);


        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Updating. Please wait...");

        mProgressDialog.show();

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("action_event_api", jsonObjectPostEventParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                System.out.println(" actionEvent exception    " + exception);
                mProgressDialog.dismiss();
            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                System.out.println(" actionEvent success response    " + response);

                parseActionResponse(response);
                mProgressDialog.dismiss();

            }
        });
    }

    private void parseActionResponse(JsonElement response) {
        System.out.println("validateActionResponse : " + response);
        if (!response.toString().contains("true")) {
            Toast.makeText(getApplicationContext(), "Something went wrong. Try again", Toast.LENGTH_SHORT).show();

        } else {
            executeGetFeedsApi();
        }
    }

    private void openEventDetails(int selcted_item_position) {


        Event_dash_list_obj selectedEvent_dash_list_obj = event_main_list.get(selcted_item_position);
        Intent eventDetailIntent = new Intent(getApplicationContext(), EventDetails.class);
        eventDetailIntent.putExtra("user_id", selectedEvent_dash_list_obj.getUser_id());
        eventDetailIntent.putExtra("user_name", selectedEvent_dash_list_obj.getUser_name());
        eventDetailIntent.putExtra("img_url", selectedEvent_dash_list_obj.getImage_url());
        eventDetailIntent.putExtra("comm_time", selectedEvent_dash_list_obj.getComment_time());
        eventDetailIntent.putExtra("user_comment", selectedEvent_dash_list_obj.getUser_comment());
        eventDetailIntent.putExtra("intent_type", 0);
        eventDetailIntent.putExtra("post_id", selectedEvent_dash_list_obj.getPost_id());
        eventDetailIntent.putExtra("user_dp_url", selectedEvent_dash_list_obj.getUser_dp_url());

        eventDetailIntent.putExtra("grp_dp_url", selectedEvent_dash_list_obj.getGrp_dp_url());
        eventDetailIntent.putExtra("grp_name", selectedEvent_dash_list_obj.getGrp_name());
        eventDetailIntent.putExtra("grp_id", selectedEvent_dash_list_obj.getGrp_id());
        eventDetailIntent.putExtra("post_as", selectedEvent_dash_list_obj.getPost_as());

        startActivity(eventDetailIntent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        sessionCounter = System.currentTimeMillis() - sessionCounter;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(sessionCounter);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(sessionCounter);

        System.out.println("MAINACTIVITY sessionCounter : " + minutes + "m " + seconds + "s");
    }

    @Override
    public void onResume() {
        executeGetFeedsApi();

        super.onResume();
    }
}
