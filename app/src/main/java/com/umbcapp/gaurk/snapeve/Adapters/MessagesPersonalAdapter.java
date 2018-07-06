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
import com.umbcapp.gaurk.snapeve.Controllers.MessagesPersonalListItem;
import com.umbcapp.gaurk.snapeve.Controllers.NotificationListItem;
import com.umbcapp.gaurk.snapeve.Listview_communicator;
import com.umbcapp.gaurk.snapeve.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesPersonalAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    List<MessagesPersonalListItem> message_main_list;
    String entryDate = null;

    public MessagesPersonalAdapter(Context context, List<MessagesPersonalListItem> message_main_list) {

        this.message_main_list = message_main_list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return message_main_list.size();
    }

    @Override
    public MessagesPersonalListItem getItem(int position) {
        return message_main_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        MessagesPersonalAdapter.ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new MessagesPersonalAdapter.ViewHolder();
            view = this.inflater.inflate(R.layout.attendies_list_item, parent, false);

            viewHolder.message_list_item_circle_img_view = (CircleImageView) view.findViewById(R.id.notification_list_item_circle_img_view);
            viewHolder.message_list_item_other_user_name_text_view = (TextView) view.findViewById(R.id.notification_list_item_header_text_view);
            viewHolder.message_list_item_time_text_view = (TextView) view.findViewById(R.id.notification_list_item_time_text_view);
            viewHolder.message_list_item_last_message_text_view = (TextView) view.findViewById(R.id.notification_list_item_description_text_view);

            view.setTag(viewHolder);

//            Listview_communicator communicator;
//            communicator = ((Listview_communicator)context).main_event_listview_element_clicked();
//            communicator.main_event_listview_element_clicked();
//

        } else {

            viewHolder = (MessagesPersonalAdapter.ViewHolder) view.getTag();

        }
        MessagesPersonalListItem selectedMessagesPersonalListItem = message_main_list.get(position);


        viewHolder.message_list_item_other_user_name_text_view.setText(selectedMessagesPersonalListItem.getOther_user_name());
        viewHolder.message_list_item_last_message_text_view.setText(selectedMessagesPersonalListItem.getLast_msg_data());
        viewHolder.message_list_item_time_text_view.setText(selectedMessagesPersonalListItem.getLast_msg_dt_time());

        Picasso.get().load(selectedMessagesPersonalListItem.getOther_user_dp_url())
                .fit().centerCrop().into(viewHolder.message_list_item_circle_img_view);


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

        private CircleImageView message_list_item_circle_img_view;
        private TextView message_list_item_other_user_name_text_view;
        private TextView message_list_item_time_text_view;
        private TextView message_list_item_last_message_text_view;

    }
}