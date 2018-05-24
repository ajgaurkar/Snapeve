package com.umbcapp.gaurk.snapeve;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
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
import java.util.Date;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.umbcapp.gaurk.snapeve.Adapters.Dash_Event_ListAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.CommentsListItem;
import com.umbcapp.gaurk.snapeve.Controllers.Event_dash_list_obj;
import com.umbcapp.gaurk.snapeve.Fragments.MapsFragment;
import com.umbcapp.gaurk.snapeve.Fragments.NotificationFragment;
import com.umbcapp.gaurk.snapeve.Fragments.SettingsFragment;
import com.umbcapp.gaurk.snapeve.Fragments.UserProfileFragment;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements Listview_communicator {

    public static MobileServiceClient mClient;
    private JsonObject jsonObjectLoginParameters;
    private UserProfileFragment userProfileFragment;
    private SettingsFragment settingsFragment;
    private MapsFragment mapsFragment;
    private NotificationFragment notificationFragment;
    private long sessionCounter;
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

    private ListView main_event_list_view;
    private ArrayList<Event_dash_list_obj> event_main_list;
    private FloatingActionButton main_img_pick_fab;
    private Dash_Event_ListAdapter dash_event_listAdapter;
    private Button refreshBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionCounter = System.currentTimeMillis();
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String text = formatter.format(new Date(sessionCounter));
        System.out.println("Start @ sessionCounter : " + text);

        testNotification();

        event_main_list = new ArrayList<Event_dash_list_obj>();

        main_event_list_view = (ListView) findViewById(R.id.dashboard_event_listview);
        main_img_pick_fab = (FloatingActionButton) findViewById(R.id.main_img_pick_fab);
        refreshBtn = (Button) findViewById(R.id.button2);


        userProfileFragment = new UserProfileFragment();
        settingsFragment = new SettingsFragment();
        mapsFragment = new MapsFragment();
        notificationFragment = new NotificationFragment();

        // Mobile Service URL and key
        try {
            mClient = new MobileServiceClient(
                    "https://azure-test-app.azurewebsites.net",
                    this);

            // Extend timeout from default of 10s to 20s
            mClient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(20, TimeUnit.SECONDS);
                    client.setWriteTimeout(20, TimeUnit.SECONDS);
                    return client;
                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

//        startActivity(new Intent(getApplicationContext(), LoginActivity.class));

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        main_img_pick_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Add_event.class));

            }
        });

        executeLoginApi();

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeLoginApi();
            }
        });

    }

    private void executeLoginApi() {
        jsonObjectLoginParameters = new JsonObject();
        jsonObjectLoginParameters.addProperty("studentId", "check123");

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
//        ListenableFuture<JsonElement> serviceFilterFuture = mClient.invokeApi("Login_api", jsonObjectLoginParameters);
        ListenableFuture<JsonElement> serviceFilterFuture = mClient.invokeApi("get_event_feeds_api", jsonObjectLoginParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                System.out.println(" executeLoginApi exception    " + exception);

            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                System.out.println(" executeLoginApi success response    " + response);

                poupulateList(response);

            }
        });
    }

    private void poupulateList(JsonElement response) {

        event_main_list = new ArrayList<Event_dash_list_obj>();

        System.out.println(" IN PARSE JASON");

        JsonArray feedsJsonArray = response.getAsJsonArray();

        for (int j = 0; j < feedsJsonArray.size(); j++) {
            JsonObject feeds_list_object = feedsJsonArray.get(j).getAsJsonObject();
            System.out.println(" feeds_list_object  " + feeds_list_object);

            String img_comment = feeds_list_object.get("img_comment").toString();
            String feed_img_url = feeds_list_object.get("img_url").toString();
            String feed_user_id = feeds_list_object.get("initializer_id").toString();
            System.out.println(" img_comment " + img_comment);
            System.out.println(" feed_img_url " + feed_img_url);

            //Remove " from start and end from every string
            img_comment = img_comment.substring(1, img_comment.length() - 1);
            feed_user_id = feed_user_id.substring(1, feed_user_id.length() - 1);
            feed_img_url = feed_img_url.substring(1, feed_img_url.length() - 1);

//                    try {
//                        Date date = format.parse(timestamp);
//                        System.out.println(date);
//
//                        new_date = newDateFormat.format(date);
//
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }

            event_main_list.add(0, new Event_dash_list_obj(feed_user_id, "Name_x", img_comment, "10 hrs ago", feed_img_url));


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

        switch (click_code) {

            case 0:
                Log.d("click_code ", +position + " " + click_code);
                openEventDetails(position);
                break;
            case 1:
                Log.d("click_code ", +position + " " + click_code);
                break;
            case 2:
                Log.d("click_code ", +position + " " + click_code);
                break;
            case 3:
                Log.d("click_code ", +position + " " + click_code);
                System.out.print("click_code " + position + " " + click_code);
                break;
            case 4:
                Log.d("click_code ", +position + " " + click_code);
                System.out.print("click_code " + position + " " + click_code);
                break;

        }

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

}
