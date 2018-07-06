package com.umbcapp.gaurk.snapeve.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.umbcapp.gaurk.snapeve.Controllers.AttendiesListItem;
import com.umbcapp.gaurk.snapeve.Controllers.NotificationListItem;
import com.umbcapp.gaurk.snapeve.Listview_communicator;
import com.umbcapp.gaurk.snapeve.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationsAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    List<NotificationListItem> notifications_list;
    String entryDate = null;

    public NotificationsAdapter(Context context, List<NotificationListItem> notifications_list) {

        this.notifications_list = notifications_list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return notifications_list.size();
    }

    @Override
    public NotificationListItem getItem(int position) {
        return notifications_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        NotificationsAdapter.ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new NotificationsAdapter.ViewHolder();
            view = this.inflater.inflate(R.layout.notification_list_item, parent, false);

            viewHolder.notification_list_item_circle_img_view = (CircleImageView) view.findViewById(R.id.notification_list_item_circle_img_view);
            viewHolder.notification_list_item_header_text_view = (TextView) view.findViewById(R.id.notification_list_item_header_text_view);
            viewHolder.notification_list_item_time_text_view = (TextView) view.findViewById(R.id.notification_list_item_time_text_view);
            viewHolder.notification_list_item_description_text_view = (TextView) view.findViewById(R.id.notification_list_item_description_text_view);

            view.setTag(viewHolder);

//            Listview_communicator communicator;
//            communicator = ((Listview_communicator)context).main_event_listview_element_clicked();
//            communicator.main_event_listview_element_clicked();
//

        } else {

            viewHolder = (NotificationsAdapter.ViewHolder) view.getTag();

        }

        NotificationListItem selectedNotificationListItem = notifications_list.get(position);

        viewHolder.notification_list_item_header_text_view.setText(selectedNotificationListItem.getHeader());
        viewHolder.notification_list_item_description_text_view.setText(selectedNotificationListItem.getPayload());
        viewHolder.notification_list_item_time_text_view.setText(selectedNotificationListItem.getDt_time());

        viewHolder.notification_list_item_circle_img_view.setImageResource(R.drawable.accept_96);

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