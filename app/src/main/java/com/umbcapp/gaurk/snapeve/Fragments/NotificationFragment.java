package com.umbcapp.gaurk.snapeve.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;
import com.umbcapp.gaurk.snapeve.Adapters.MessagesPersonalAdapter;
import com.umbcapp.gaurk.snapeve.Adapters.NotificationsAdapter;
import com.umbcapp.gaurk.snapeve.Add_event;
import com.umbcapp.gaurk.snapeve.Controllers.MessagesPersonalListItem;
import com.umbcapp.gaurk.snapeve.Controllers.NotificationListItem;
import com.umbcapp.gaurk.snapeve.MessageThread;
import com.umbcapp.gaurk.snapeve.R;
import com.umbcapp.gaurk.snapeve.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {


    private JsonObject jsonObjectUserProfileFragParameters;
    //    private TextView notification_switch_notification_textview;
//    private TextView notification_switch_messages_textview;
    private ListView notification_layout_listview;
    int page_type_code = 1;
    private ImageView notification_settings_imageview;
    private SwitchCompat notification_switch;

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
//        notification_switch_notification_textview = (TextView) rootView.findViewById(R.id.notification_switch_notification_textview);
//        notification_switch_messages_textview = (TextView) rootView.findViewById(R.id.notification_switch_messages_textview);
        notification_layout_listview = (ListView) rootView.findViewById(R.id.notification_layout_listview);
        notification_settings_imageview = (ImageView) rootView.findViewById(R.id.notification_settings_imageview);
        notification_switch = (SwitchCompat) rootView.findViewById(R.id.notification_switch);

        if (new SessionManager(getActivity()).getSpecificUserBooleanDetail(SessionManager.KEY_NOTIFICATION_ONN_OFF_STATUS)) {
            notification_switch.setChecked(true);
        } else {
            notification_switch.setChecked(false);
        }


        notification_settings_imageview.setImageResource(R.drawable.settings_white_48);

        populateData(page_type_code);

//        notification_switch_notification_textview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                page_type_code = 1;
//                notification_switch_notification_textview.setBackground(getResources().getDrawable(R.drawable.text_selection_left_seleted));
//                notification_switch_messages_textview.setBackground(getResources().getDrawable(R.drawable.text_selection_right_unseleted));
//                populateData(page_type_code);
//                notification_settings_imageview.setImageResource(R.drawable.settings_white_48);
//
//            }
//        });
//
//
//        notification_switch_messages_textview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                page_type_code = 2;
//                notification_switch_messages_textview.setBackground(getResources().getDrawable(R.drawable.text_selection_right_seleted));
//                notification_switch_notification_textview.setBackground(getResources().getDrawable(R.drawable.text_selection_left_unseleted));
//                populateData(page_type_code);
//                notification_settings_imageview.setImageResource(R.drawable.plus_white_48);
//            }
//        });

        notification_settings_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //dummy listener to override clicks of main layout
        main_notification_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        startActivity(new Intent(getActivity(), Login_snapeve_activity.class));

        notification_layout_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startActivity(new Intent(getActivity(), MessageThread.class));

            }
        });
        notification_settings_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (page_type_code) {
                    case 1:
                        System.out.println("OPEN SETTINGS DIALOG");
                        break;

                    case 2:
                        System.out.println("OPEN REQUEST CHAT DIALOG");
                        break;
                }

            }
        });

        notification_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                System.out.println("notification_switch " + isChecked);

                if (isChecked) {
                    new SessionManager(getActivity()).setSpecificUserBooleanDetail(SessionManager.KEY_NOTIFICATION_ONN_OFF_STATUS, true);
                } else {
                    showConfirmationDialog();
                }
            }
        });


        return rootView;
    }

    private void showConfirmationDialog() {
        new FancyAlertDialog.Builder(getActivity())
                .setTitle("Are you sure you want to disable app notifications?")
                .setBackgroundColor(Color.parseColor("#3F51B5"))  //Don't pass R.color.colorvalue
                .setNegativeBtnText("Keep it ON")
                .setAnimation(Animation.SLIDE)
                .setIcon(R.drawable.notification_round_blue_white_100, Icon.Visible)
                .isCancellable(false)
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        notification_switch.setChecked(true);
                    }
                })
                .setPositiveBtnText("Turn it OFF")
                .setPositiveBtnBackground(getResources().getColor(R.color.colorPrimary))//Don't pass R.color.colorvalue
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        new SessionManager(getActivity()).setSpecificUserBooleanDetail(SessionManager.KEY_NOTIFICATION_ONN_OFF_STATUS, false);
                    }
                })
                .build();
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
