package com.umbcapp.gaurk.snapeve.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.umbcapp.gaurk.snapeve.Adapters.MessagesPersonalAdapter;
import com.umbcapp.gaurk.snapeve.Adapters.NotificationsAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.MessagesPersonalListItem;
import com.umbcapp.gaurk.snapeve.Controllers.NotificationListItem;
import com.umbcapp.gaurk.snapeve.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {


    private JsonObject jsonObjectUserProfileFragParameters;
    private TextView notification_switch_notification_textview;
    private TextView notification_switch_messages_textview;
    private ListView notification_layout_listview;

    public NotificationFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.notification_fragment, container, false);

        RelativeLayout main_notification_layout = (RelativeLayout) rootView.findViewById(R.id.main_notification_layout);
        notification_switch_notification_textview = (TextView) rootView.findViewById(R.id.notification_switch_notification_textview);
        notification_switch_messages_textview = (TextView) rootView.findViewById(R.id.notification_switch_messages_textview);
        notification_layout_listview = (ListView) rootView.findViewById(R.id.notification_layout_listview);

        populateData(1);

        notification_switch_notification_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notification_switch_notification_textview.setBackground(getResources().getDrawable(R.drawable.text_selection_left_seleted));
                notification_switch_messages_textview.setBackground(getResources().getDrawable(R.drawable.text_selection_right_unseleted));
                populateData(1);
            }
        });


        notification_switch_messages_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notification_switch_messages_textview.setBackground(getResources().getDrawable(R.drawable.text_selection_right_seleted));
                notification_switch_notification_textview.setBackground(getResources().getDrawable(R.drawable.text_selection_left_unseleted));
                populateData(2);
            }
        });


        //dummy listener to override clicks of main layout
        main_notification_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        startActivity(new Intent(getActivity(), Login_snapeve_activity.class));

        return rootView;
    }

    private void populateData(int type_code) {
        System.out.println("type_code : " + type_code);

        switch (type_code) {
            case 1:
                ArrayList<NotificationListItem> notificationsList = new ArrayList<>();
                notificationsList.add(new NotificationListItem("u1", "n1", 1, "Request approved", "Your request to join the group has been accepted", "Today 12:39 PM", true));
                notificationsList.add(new NotificationListItem("u1", "n1", 1, "Post liked", "Your post from last week has been liked", "Wed 04:12 PM", false));
                notificationsList.add(new NotificationListItem("u1", "n1", 1, "Invitation for event", "Your group member has invited to you attend an event", "Jul 03, 09:30 AM", false));

                NotificationsAdapter notificationsAdapter = new NotificationsAdapter(getActivity(), notificationsList);
                notification_layout_listview.setAdapter(notificationsAdapter);
                break;
            case 2:
                ArrayList<MessagesPersonalListItem> messagePersonalList = new ArrayList<>();
                messagePersonalList.add(new MessagesPersonalListItem("u1", "ou1", "Kevin_Jones", "http://www.dast.biz/wp-content/uploads/2016/11/John_Doe.jpg", "Today 11:30 AM", "I will be there", true));
                messagePersonalList.add(new MessagesPersonalListItem("u1", "ou1", "T_Cruise", "https://a0.muscache.com/im/pictures/fe062a6a-6e30-4d64-bca5-69b037b6bff4.jpg?aki_policy=profile_x_medium", "Mon 03:30 PM", "Thanks for the update", true));
                messagePersonalList.add(new MessagesPersonalListItem("u1", "ou1", "Neha_Red", "https://www.bnl.gov/today/body_pics/2017/06/stephanhruszkewycz-355px.jpg", "Jul 01, 11:30 AM", "Never mind", true));
                messagePersonalList.add(new MessagesPersonalListItem("u1", "ou1", "Josh_Ed", "http://www.medicine20congress.com/ocs/public/profiles/3141.jpg", "Jun 23, 09:38 AM", "Hope to see you again", true));
                messagePersonalList.add(new MessagesPersonalListItem("u1", "ou1", "Cool_Dude", "https://cdn.earthdata.nasa.gov/conduit/upload/6072/Glenn_headshot_resize.jpg", "Jun 13, 11:30 AM", "Cya tomorrow ", true));

                MessagesPersonalAdapter messagesPersonalAdapter = new MessagesPersonalAdapter(getActivity(), messagePersonalList);
                notification_layout_listview.setAdapter(messagesPersonalAdapter);
                break;
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}
