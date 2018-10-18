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
import java.util.Locale;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.notification_list_item, parent, false);
            holder.notification_list_item_circle_img_view = (CircleImageView) convertView.findViewById(R.id.notification_list_item_circle_img_view);
            holder.notification_list_item_header_text_view = (TextView) convertView.findViewById(R.id.notification_list_item_header_text_view);
            holder.notification_list_item_time_text_view = (TextView) convertView.findViewById(R.id.notification_list_item_time_text_view);
            holder.notification_list_item_description_text_view = (TextView) convertView.findViewById(R.id.notification_list_item_description_text_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        SnapeveNotification selectedNotificationListItem = notifications_list.get(position);
        System.out.println("selectedNotificationListItem-------  " + selectedNotificationListItem);
        holder.notification_list_item_header_text_view.setText(String.valueOf(selectedNotificationListItem.getNotificationTitle()));
        holder.notification_list_item_description_text_view.setText(selectedNotificationListItem.getNotificationDescription());
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm:ss", Locale.getDefault());
            System.out.println("current millis" + selectedNotificationListItem.getNotificationTimeStamp());

            Timestamp timestamp = new Timestamp(selectedNotificationListItem.getNotificationTimeStamp());
            holder.notification_list_item_time_text_view.setText(sdf.format(timestamp));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("ddddd"+selectedNotificationListItem.getNotificationTag());

        try {


            switch (selectedNotificationListItem.getNotificationTag()) {
                case "Action spammed":
                    System.out.println("1");
                    holder.notification_list_item_circle_img_view.setImageResource(R.drawable.notification_icon_spam_round_96);
                    break;
                case "Action verified":
                    System.out.println("11");
                    holder.notification_list_item_circle_img_view.setImageResource(R.drawable.notification_icon_like_round_96);
                    break;
                case "Join request received":
                    System.out.println("111");
                    holder.notification_list_item_circle_img_view.setImageResource(R.drawable.notification_icon_new_request_96);
                    break;
                case "Action comment":
                    System.out.println("111");
                    holder.notification_list_item_circle_img_view.setImageResource(R.drawable.notification_icon_new_comment_96);
                    break;
                case "New Event":
                    System.out.println("1111");
                    holder.notification_list_item_circle_img_view.setImageResource(R.drawable.notification_icon_new_event_96);
                    break;
                case "Invitation received":
                    System.out.println("11111");
                    holder.notification_list_item_circle_img_view.setImageResource(R.drawable.notification_icon_invite_96);
                    break;
                case "Added to the group":
                    System.out.println("111111");
                    holder.notification_list_item_circle_img_view.setImageResource(R.drawable.notification_icon_added_togrp_90);
                    break;
                case "code used":
                    System.out.println("14444444");
                    holder.notification_list_item_circle_img_view.setImageResource(R.drawable.notification_icon_gift_card_round_96);
                    break;
                case "Weekly reward":
                    System.out.println("155555555");
                    holder.notification_list_item_circle_img_view.setImageResource(R.drawable.notification_icon_gift_card_round_96);
                    break;
                default:
                    holder.notification_list_item_circle_img_view.setImageResource(R.drawable.notification_icon_gift_card_round_96);
                    break;
            }
        }catch (Exception e){

            System.out.println("null Pointer in Tag ");

        }
        return convertView;
    }


    public class ViewHolder {

        private CircleImageView notification_list_item_circle_img_view;
        private TextView notification_list_item_header_text_view;
        private TextView notification_list_item_time_text_view;
        private TextView notification_list_item_description_text_view;

    }
}

