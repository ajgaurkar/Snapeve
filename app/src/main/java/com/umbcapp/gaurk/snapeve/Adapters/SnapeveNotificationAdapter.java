package com.umbcapp.gaurk.snapeve.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umbcapp.gaurk.snapeve.Controllers.SnapeveNotification;
import com.umbcapp.gaurk.snapeve.R;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SnapeveNotificationAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    List<SnapeveNotification> notifications_list;
    String entryDate = null;

    public SnapeveNotificationAdapter(Context context, List<SnapeveNotification> notifications_list) {

        this.notifications_list = notifications_list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return notifications_list.size();
    }

    @Override
    public SnapeveNotification getItem(int position) {
        return notifications_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        SnapeveNotificationAdapter.ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new SnapeveNotificationAdapter.ViewHolder();
            view = this.inflater.inflate(R.layout.notification_list_item, parent, false);

            viewHolder.notification_list_item_circle_img_view = (CircleImageView) view.findViewById(R.id.notification_list_item_circle_img_view);
            viewHolder.notification_list_item_header_text_view = (TextView) view.findViewById(R.id.notification_list_item_header_text_view);
            viewHolder.notification_list_item_time_text_view = (TextView) view.findViewById(R.id.notification_list_item_time_text_view);
            viewHolder.notification_list_item_description_text_view = (TextView) view.findViewById(R.id.notification_list_item_description_text_view);

            view.setTag(viewHolder);

//            Listview_communicator communicator;
//            communicator = ((Listview_communicator)context).main_event_listview_element_clicked();
//            communicator.main_event_listview_element_clicked();

        } else {

            viewHolder = (ViewHolder) view.getTag();

        }

        SnapeveNotification selectedNotificationListItem = notifications_list.get(position);

        viewHolder.notification_list_item_header_text_view.setText(String.valueOf(selectedNotificationListItem.getNotificationTitle()));
        viewHolder.notification_list_item_description_text_view.setText(selectedNotificationListItem.getNotificationDescription());

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:MM:SS");
        System.out.println("current millis" + selectedNotificationListItem.getNotificationTimeStamp());

        Timestamp timestamp = new Timestamp(selectedNotificationListItem.getNotificationTimeStamp());
        viewHolder.notification_list_item_time_text_view.setText(sdf.format(timestamp));

        if (selectedNotificationListItem.getNotificationTag().contains("Action spammed")) {
            viewHolder.notification_list_item_circle_img_view.setImageResource(R.drawable.notification_icon_spam_round_96);
        }
        if (selectedNotificationListItem.getNotificationTag().contains("Action verified")) {
            viewHolder.notification_list_item_circle_img_view.setImageResource(R.drawable.notification_icon_like_round_96);
        }
        if (selectedNotificationListItem.getNotificationTag().contains("Join request received")) {
            viewHolder.notification_list_item_circle_img_view.setImageResource(R.drawable.notification_icon_new_request_96);
        }
        if (selectedNotificationListItem.getNotificationTag().contains("Action comment")) {
            viewHolder.notification_list_item_circle_img_view.setImageResource(R.drawable.notification_icon_new_comment_96);
        }
        if (selectedNotificationListItem.getNotificationTag().contains("New Event")) {
            viewHolder.notification_list_item_circle_img_view.setImageResource(R.drawable.notification_icon_new_event_96);
        }
        if (selectedNotificationListItem.getNotificationTag().contains("Invitation received")) {
            viewHolder.notification_list_item_circle_img_view.setImageResource(R.drawable.notification_icon_invite_96);
        }
        if (selectedNotificationListItem.getNotificationTag().contains("Weekly reward")) {
            viewHolder.notification_list_item_circle_img_view.setImageResource(R.drawable.notification_icon_weekly_rewards_128);
        }
        if (selectedNotificationListItem.getNotificationTag().contains("Added to the group")) {
            viewHolder.notification_list_item_circle_img_view.setImageResource(R.drawable.notification_icon_added_togrp_90);
        }
        if (selectedNotificationListItem.getNotificationTag().contains("code used")) {
            viewHolder.notification_list_item_circle_img_view.setImageResource(R.drawable.notification_icon_gift_card_round_96);
        }


//                iv_logo.setImageResource(getResources().getIdentifier(logo_id, "drawable", "com.yourpackage"));


//        Picasso.get().load(selectedNotificationListItem.get())
//                .fit().centerCrop().into(viewHolder.attendies_list_item_user_pic_img_view);


//        viewHolder.attendies_list_item_request_status_text_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.print("position 2 " + position);
//                ((Listview_communicator) context).main_event_listview_element_clicked(position, 0);
//            }
//        });

        return view;
    }

    public class ViewHolder {

        private CircleImageView notification_list_item_circle_img_view;
        private TextView notification_list_item_header_text_view;
        private TextView notification_list_item_time_text_view;
        private TextView notification_list_item_description_text_view;

    }
}

