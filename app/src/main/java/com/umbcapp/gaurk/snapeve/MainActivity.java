package com.umbcapp.gaurk.snapeve;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.umbcapp.gaurk.snapeve.Adapters.Dash_Event_ListAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.Event_dash_list_obj;
import com.umbcapp.gaurk.snapeve.Fragments.MapsFragment;
import com.umbcapp.gaurk.snapeve.Fragments.UserProfileFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Listview_communicator {


    private UserProfileFragment userProfileFragment;
    private MapsFragment mapsFragment;
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
                case R.id.navigation_settings:
//                    startActivity(new Intent(getApplicationContext(), MapsActivityFragment.class));
                    System.out.print("notification");
                    Toast.makeText(getApplicationContext(), "toast", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }
    };
    private ListView main_event_list_view;
    private ArrayList<Event_dash_list_obj> event_main_list;
    private FloatingActionButton main_img_pick_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_event_list_view = (ListView) findViewById(R.id.dashboard_event_listview);
        main_img_pick_fab = (FloatingActionButton) findViewById(R.id.main_img_pick_fab);

        event_main_list = new ArrayList<Event_dash_list_obj>();
        event_main_list.add(new Event_dash_list_obj("http://www.stonybrook.edu/commcms/undergraduate-colleges/_images/field-day_0.jpg"));
        event_main_list.add(new Event_dash_list_obj("http://www.amarsound.com/images/school_college_events/1.jpg"));
        event_main_list.add(new Event_dash_list_obj("https://www.csee.umbc.edu/wp-content/uploads/2018/02/hackumbc_slider-1000x450.png"));
        event_main_list.add(new Event_dash_list_obj("https://news.umbc.edu/wp-content/uploads/2016/03/Campus_Entrance_sign-2828-1-e1458838962758-1920x768.jpg"));
        event_main_list.add(new Event_dash_list_obj("https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
        event_main_list.add(new Event_dash_list_obj("https://news.umbc.edu/wp-content/uploads/2018/01/Fall-Campus17-6588-1920x768.jpg"));

        userProfileFragment = new UserProfileFragment();
        mapsFragment = new MapsFragment();


//        Dash_Event_ListAdapter dash_event_listAdapter = new Dash_Event_ListAdapter(event_main_list, this, width * 90 / 100);
        Dash_Event_ListAdapter dash_event_listAdapter = new Dash_Event_ListAdapter(this, event_main_list);
        main_event_list_view.setAdapter(dash_event_listAdapter);

//        startActivity(new Intent(getApplicationContext(), LoginActivity.class));

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        main_img_pick_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Add_event.class));

            }
        });

    }

    public void testlistevents() {
        System.out.print("testlistevents");
    }

    @Override
    public void main_event_listview_element_clicked(int position, int click_code) {

        System.out.print("position " + position);
        System.out.print("click_code " + click_code);
//        Toast.makeText(getApplicationContext(),""+position,Toast.LENGTH_SHORT).show();

        switch (click_code) {

            case 0:
                Log.d("click_code ", +position + " " + click_code);

                startActivity(new Intent(getApplicationContext(), EventDetails.class));
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
}
