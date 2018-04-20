package com.umbcapp.gaurk.snapeve.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.umbcapp.gaurk.snapeve.Controllers.Event_dash_list_obj;
import com.umbcapp.gaurk.snapeve.Listview_communicator;
import com.umbcapp.gaurk.snapeve.R;

import java.util.List;

public class Dash_Event_ListAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    List<Event_dash_list_obj> event_dash_list;
    String entryDate = null;

    public Dash_Event_ListAdapter(Context context, List<Event_dash_list_obj> event_dash_list) {

        this.event_dash_list = event_dash_list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return event_dash_list.size();
    }

    @Override
    public Event_dash_list_obj getItem(int position) {
        return event_dash_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = this.inflater.inflate(R.layout.dash_list_view_item, parent, false);

            viewHolder.main_card_imageview = (ImageView) view.findViewById(R.id.cardimgview);

            viewHolder.list_item_verify_iv = (ImageView) view.findViewById(R.id.list_item_verify_iv);
            viewHolder.list_item_deny_iv = (ImageView) view.findViewById(R.id.list_item_deny_iv);
            viewHolder.list_item_comment_iv = (ImageView) view.findViewById(R.id.list_item_comment_iv);
            viewHolder.list_item_spam_iv = (ImageView) view.findViewById(R.id.list_item_spam_iv);
            viewHolder.list_item_verify_tv = (TextView) view.findViewById(R.id.list_item_verify_tv);
            viewHolder.list_item_deny_tv = (TextView) view.findViewById(R.id.list_item_deny_tv);
            viewHolder.list_item_comment_tv = (TextView) view.findViewById(R.id.list_item_comment_tv);
            viewHolder.list_item_spam_tv = (TextView) view.findViewById(R.id.list_item_spam_tv);

            viewHolder.list_item_user_name = (TextView) view.findViewById(R.id.list_item_username);
            viewHolder.list_item_event_title = (TextView) view.findViewById(R.id.list_item_user_description);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Event_dash_list_obj event_dash_list_obj = event_dash_list.get(position);

        Picasso.get().load(event_dash_list_obj.getImage_url()).fit().centerCrop().into(viewHolder.main_card_imageview);
        viewHolder.list_item_event_title.setText(event_dash_list_obj.getUser_comment());
        viewHolder.list_item_user_name.setText(event_dash_list_obj.getUser_name());

        viewHolder.main_card_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("position 2 " + position);
                Toast.makeText(context, "2 " + position, Toast.LENGTH_SHORT).show();

                ((Listview_communicator) context).main_event_listview_element_clicked(position, 0);
            }
        });

        viewHolder.list_item_verify_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("position 2 " + position);
                Toast.makeText(context, "2 " + position, Toast.LENGTH_SHORT).show();

                ((Listview_communicator) context).main_event_listview_element_clicked(position, 1);
            }
        });
        viewHolder.list_item_verify_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("position 2 " + position);
                Toast.makeText(context, "2 " + position, Toast.LENGTH_SHORT).show();

                ((Listview_communicator) context).main_event_listview_element_clicked(position, 1);
            }
        });
        viewHolder.list_item_deny_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("position 2 " + position);
                Toast.makeText(context, "2 " + position, Toast.LENGTH_SHORT).show();

                ((Listview_communicator) context).main_event_listview_element_clicked(position, 2);
            }
        });
        viewHolder.list_item_deny_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("position 2 " + position);
                Toast.makeText(context, "2 " + position, Toast.LENGTH_SHORT).show();

                ((Listview_communicator) context).main_event_listview_element_clicked(position, 2);
            }
        });
        viewHolder.list_item_comment_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("position 2 " + position);
                Toast.makeText(context, "2 " + position, Toast.LENGTH_SHORT).show();

                ((Listview_communicator) context).main_event_listview_element_clicked(position, 3);
            }
        });
        viewHolder.list_item_comment_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("position 2 " + position);
                Toast.makeText(context, "2 " + position, Toast.LENGTH_SHORT).show();

                ((Listview_communicator) context).main_event_listview_element_clicked(position, 3);
            }
        });
        viewHolder.list_item_spam_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("position 2 " + position);
                Toast.makeText(context, "2 " + position, Toast.LENGTH_SHORT).show();

                ((Listview_communicator) context).main_event_listview_element_clicked(position, 4);
            }
        });
        viewHolder.list_item_spam_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("position 2 " + position);
                Toast.makeText(context, "2 " + position, Toast.LENGTH_SHORT).show();


                ((Listview_communicator) context).main_event_listview_element_clicked(position, 4);
            }
        });


        return view;
    }

    public class ViewHolder {

        private ImageView main_card_imageview;

        private ImageView list_item_verify_iv;
        private ImageView list_item_deny_iv;
        private ImageView list_item_comment_iv;
        private ImageView list_item_spam_iv;
        private TextView list_item_verify_tv;
        private TextView list_item_deny_tv;
        private TextView list_item_comment_tv;
        private TextView list_item_spam_tv;
        private TextView list_item_event_title;
        private TextView list_item_user_name;
    }
}