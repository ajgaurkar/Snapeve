package com.umbcapp.gaurk.snapeve.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.umbcapp.gaurk.snapeve.Controllers.MessagesPersonalListItem;
import com.umbcapp.gaurk.snapeve.Controllers.MessagesThreadListItem;
import com.umbcapp.gaurk.snapeve.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesThreadAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    List<MessagesThreadListItem> message_main_list;

    public MessagesThreadAdapter(Context context, List<MessagesThreadListItem> message_main_list) {

        this.message_main_list = message_main_list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return message_main_list.size();
    }

    @Override
    public MessagesThreadListItem getItem(int position) {
        return message_main_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        MessagesThreadAdapter.ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new MessagesThreadAdapter.ViewHolder();
            view = this.inflater.inflate(R.layout.message_thread_list_item, parent, false);

//            viewHolder.message_list_item_circle_img_view = (CircleImageView) view.findViewById(R.id.notification_list_item_circle_img_view);
//            viewHolder.message_list_item_other_user_name_text_view = (TextView) view.findViewById(R.id.notification_list_item_header_text_view);
            viewHolder.message_thread_list_item_message_textview = (TextView) view.findViewById(R.id.message_thread_list_item_message_textview);
//            viewHolder.message_list_item_last_message_text_view = (TextView) view.findViewById(R.id.notification_list_item_description_text_view);

            view.setTag(viewHolder);

//            Listview_communicator communicator;
//            communicator = ((Listview_communicator)context).main_event_listview_element_clicked();
//            communicator.main_event_listview_element_clicked();
//

        } else {

            viewHolder = (MessagesThreadAdapter.ViewHolder) view.getTag();

        }
        MessagesThreadListItem selectedMessagesThreadListItem = message_main_list.get(position);


        viewHolder.message_thread_list_item_message_textview.setText(selectedMessagesThreadListItem.getMsg_payload());

        return view;
    }

    public class ViewHolder {

        private TextView message_thread_list_item_message_textview;
        private TextView message_list_item_message_text_view;

    }
}