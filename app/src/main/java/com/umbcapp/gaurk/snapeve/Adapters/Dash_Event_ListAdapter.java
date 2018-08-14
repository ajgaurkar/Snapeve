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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
            viewHolder.list_item_user_img = (ImageView) view.findViewById(R.id.list_item_user_img);

            viewHolder.list_item_verify_iv = (ImageView) view.findViewById(R.id.list_item_verify_iv);
//            viewHolder.list_item_deny_iv = (ImageView) view.findViewById(R.id.list_item_deny_iv);
            viewHolder.list_item_comment_iv = (ImageView) view.findViewById(R.id.list_item_comment_iv);
            viewHolder.list_item_spam_iv = (ImageView) view.findViewById(R.id.list_item_spam_iv);
            viewHolder.list_item_verify_tv = (TextView) view.findViewById(R.id.list_item_verify_tv);
            viewHolder.list_item_post_dt_time = (TextView) view.findViewById(R.id.post_dt_time_textview);
            viewHolder.list_item_event_statr_end_dt_time_textview = (TextView) view.findViewById(R.id.list_item_event_statr_end_dt_time_textview);
            viewHolder.list_item_comment_tv = (TextView) view.findViewById(R.id.list_item_comment_tv);
            viewHolder.list_item_spam_tv = (TextView) view.findViewById(R.id.list_item_spam_tv);

            viewHolder.list_item_user_name = (TextView) view.findViewById(R.id.list_item_username);
            viewHolder.list_item_event_title = (TextView) view.findViewById(R.id.list_item_user_description);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Event_dash_list_obj event_dash_list_obj = event_dash_list.get(position);


        DateFormat feedsDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

        if (event_dash_list_obj.getAll_day_status()) {
            DateFormat displayAlldayDtFormat = new SimpleDateFormat("MMM dd");

            Date startDateTime = null;
            Date endDateTime = null;
            try {
                startDateTime = feedsDateFormat.parse(event_dash_list_obj.getPost_start_dt_time());
                //just 1 of the 2 dates needed for all_day
                //endDateTime = feedsDateFormat.parse(event_dash_list_obj.getPost_end_dt_time());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            viewHolder.list_item_event_statr_end_dt_time_textview.setText(displayAlldayDtFormat.format(startDateTime) + ", All day event");

        } else {
            DateFormat displayStartDtFormat = new SimpleDateFormat("MMM dd, HH:MM - ");
            DateFormat displayEndTimeFormat = new SimpleDateFormat("HH:mm");

            Date startDateTime = null;
            Date endDateTime = null;
            try {
                startDateTime = feedsDateFormat.parse(event_dash_list_obj.getPost_start_dt_time());
                endDateTime = feedsDateFormat.parse(event_dash_list_obj.getPost_end_dt_time());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            viewHolder.list_item_event_statr_end_dt_time_textview.setText(displayStartDtFormat.format(startDateTime) + displayEndTimeFormat.format(endDateTime));
        }
        Picasso.get().load(event_dash_list_obj.getImage_url()).fit().centerCrop().into(viewHolder.main_card_imageview);
        viewHolder.list_item_event_title.setText(event_dash_list_obj.getUser_comment());
        viewHolder.list_item_post_dt_time.setText(event_dash_list_obj.getPost_dt());

        switch (event_dash_list_obj.getPost_as()) {
            //0: individual
            //1: group
            //2: anonymous
            case 0:
                viewHolder.list_item_user_name.setText(event_dash_list_obj.getUser_name());

                if (event_dash_list_obj.getUser_dp_url() == null) {
                    viewHolder.list_item_user_img.setImageResource(R.drawable.avatar_100_3);
                } else {
                    Picasso.get().load(event_dash_list_obj.getUser_dp_url())
                            .fit().centerCrop().into(viewHolder.list_item_user_img);
                }
                break;
            case 1:
                viewHolder.list_item_user_name.setText(event_dash_list_obj.getGrp_name());
                System.out.println("ADAPTER " + event_dash_list_obj.getGrp_name());
                if (event_dash_list_obj.getGrp_dp_url() == null) {
                    viewHolder.list_item_user_img.setImageResource(R.drawable.avatar_100_3);
                } else {
                    Picasso.get().load(event_dash_list_obj.getGrp_dp_url())
                            .fit().centerCrop().into(viewHolder.list_item_user_img);
                }
                break;
            case 2:
                viewHolder.list_item_user_name.setText("Anonymous");

                viewHolder.list_item_user_img.setImageResource(R.drawable.anonymous_dark_100);
                break;
        }


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
//        viewHolder.list_item_deny_iv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.print("position 2 " + position);
//                Toast.makeText(context, "2 " + position, Toast.LENGTH_SHORT).show();
//
//                ((Listview_communicator) context).main_event_listview_element_clicked(position, 2);
//            }
//        });
//        viewHolder.list_item_deny_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.print("position 2 " + position);
//                Toast.makeText(context, "2 " + position, Toast.LENGTH_SHORT).show();
//
//                ((Listview_communicator) context).main_event_listview_element_clicked(position, 2);
//            }
//        });
        viewHolder.list_item_comment_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("position 2 " + position);
                Toast.makeText(context, "2 " + position, Toast.LENGTH_SHORT).show();

                ((Listview_communicator) context).main_event_listview_element_clicked(position, 2);
            }
        });
        viewHolder.list_item_comment_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("position 2 " + position);
                Toast.makeText(context, "2 " + position, Toast.LENGTH_SHORT).show();

                ((Listview_communicator) context).main_event_listview_element_clicked(position, 2);
            }
        });
        viewHolder.list_item_spam_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("position 2 " + position);
                Toast.makeText(context, "2 " + position, Toast.LENGTH_SHORT).show();

                ((Listview_communicator) context).main_event_listview_element_clicked(position, 3);
            }
        });
        viewHolder.list_item_spam_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("position 2 " + position);
                Toast.makeText(context, "2 " + position, Toast.LENGTH_SHORT).show();


                ((Listview_communicator) context).main_event_listview_element_clicked(position, 3);
            }
        });


        return view;
    }

    public class ViewHolder {

        private ImageView main_card_imageview;
        private ImageView list_item_user_img;

        private ImageView list_item_verify_iv;
        //        private ImageView list_item_deny_iv;
        private ImageView list_item_comment_iv;
        private ImageView list_item_spam_iv;
        private TextView list_item_verify_tv;
        //        private TextView list_item_deny_tv;
        private TextView list_item_comment_tv;
        private TextView list_item_spam_tv;
        private TextView list_item_event_title;
        private TextView list_item_event_statr_end_dt_time_textview;
        private TextView list_item_user_name;
        private TextView list_item_post_dt_time;
    }
}