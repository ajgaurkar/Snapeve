package com.umbcapp.gaurk.snapeve;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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
        jsonObjectLoginParameters.addProperty("studentId", "check123");

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

                Toast.makeText(MainActivity.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
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

            String post_img_url = feeds_list_object.get("img_url").getAsString();
            String post_user_id = feeds_list_object.get("initializer_id").getAsString();
            String post_id = feeds_list_object.get("post_id").getAsString();
            String post_dt = feeds_list_object.get("post_date").getAsString();
            String event_start_dt_time = feeds_list_object.get("event_start_dt_time").getAsString();
            String event_end_dt_time = feeds_list_object.get("event_end_dt_time").getAsString();
            boolean event_all_day_status = feeds_list_object.get("all_day").getAsBoolean();
            String initializer_name = feeds_list_object.get("initializer_name").getAsString();
            int post_as = feeds_list_object.get("post_as").getAsInt();
            int event_type = feeds_list_object.get("event_type").getAsInt();
            int scope = feeds_list_object.get("scope").getAsInt();
            int location_type = feeds_list_object.get("location_type").getAsInt();

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

            event_main_list.add(0, new Event_dash_list_obj(post_user_id, post_dp_url, initializer_name, img_comment, post_dt, post_img_url, post_id, post_dt, event_start_dt_time, event_end_dt_time, event_all_day_status, post_as, event_type, location_type, scope, post_grp_name, post_grp_id, post_grp_dp_url));

        }
        System.out.println(" event_main_list " + event_main_list.size());
        dash_event_listAdapter = new Dash_Event_ListAdapter(this, event_main_list);
        main_event_list_view.setAdapter(dash_event_listAdapter);
    }


    @Override
    public void main_event_listview_element_clicked(int position, int click_code) {

        System.out.print("position " + position);
        System.out.print("click_code " + click_code);
//        Toast.makeText(getApplicationContext(),""+position,Toast.LENGTH_SHORT).show();

        int currentstatus = 0;
        String userComment = "";
        switch (click_code) {


            case 0:
                Log.d("click_code ", +position + " " + click_code);
                openEventDetails(position);
                break;
            case 1:
                Log.d("click_code ", +position + " " + click_code);

                actionEvent(event_main_list.get(position).getPost_id(), event_main_list.get(position).getUser_id(), currentstatus, click_code, userComment);
                break;
            case 2:
                userComment = "Test comment";
                Log.d("click_code ", +position + " " + click_code);
                actionEvent(event_main_list.get(position).getPost_id(), event_main_list.get(position).getUser_id(), currentstatus, click_code, userComment);

                break;
            case 3:
                System.out.print("click_code " + position + " " + click_code);
                actionEvent(event_main_list.get(position).getPost_id(), event_main_list.get(position).getUser_id(), currentstatus, click_code, userComment);
                break;
        }

    }

    private void actionEvent(String post_id, String user_id, int currentstatus, int click_code, String userComment) {
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
        jsonObjectPostEventParameters.addProperty("user_id", user_id);
        jsonObjectPostEventParameters.addProperty("post_id", post_id);
        jsonObjectPostEventParameters.addProperty("click_code", click_code);


        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Posting. Please wait...");


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
                mProgressDialog.dismiss();

            }
        });
    }

    private void openEventDetails(int position) {


        Event_dash_list_obj selectedEvent_dash_list_obj = event_main_list.get(position);
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
