package com.umbcapp.gaurk.snapeve.Adapters;

import android.content.Context;
import android.media.Image;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.umbcapp.gaurk.snapeve.AddFourHours;
import com.umbcapp.gaurk.snapeve.Controllers.Event_dash_list_obj;
import com.umbcapp.gaurk.snapeve.Listview_communicator;
import com.umbcapp.gaurk.snapeve.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
            viewHolder.post_location_imageview = (ImageView) view.findViewById(R.id.post_location_imageview);
            viewHolder.list_item_verify_tv = (TextView) view.findViewById(R.id.list_item_verify_tv);
            viewHolder.post_location_textview = (TextView) view.findViewById(R.id.post_location_textview);
            viewHolder.likes_status_textview = (TextView) view.findViewById(R.id.likes_status_textview);
            viewHolder.list_item_post_dt_time = (TextView) view.findViewById(R.id.post_dt_time_textview);
            viewHolder.list_item_event_statr_end_dt_time_textview = (TextView) view.findViewById(R.id.list_item_event_statr_end_dt_time_textview);
            viewHolder.list_item_comment_tv = (TextView) view.findViewById(R.id.list_item_comment_tv);
            viewHolder.list_item_spam_tv = (TextView) view.findViewById(R.id.list_item_spam_tv);
            viewHolder.feeds_item_live_tag_textview = (TextView) view.findViewById(R.id.feeds_item_live_tag_textview);

            viewHolder.list_item_user_name = (TextView) view.findViewById(R.id.list_item_username);
            viewHolder.list_item_event_title = (TextView) view.findViewById(R.id.list_item_user_description);

            viewHolder.feeds_item_live_tag_textview.setVisibility(View.GONE);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Event_dash_list_obj event_dash_list_obj = event_dash_list.get(position);

//        DateFormat feedsDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        DateFormat feedsDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        if (event_dash_list_obj.getAll_day_status()) {
            DateFormat displayAlldayDtFormat = new SimpleDateFormat("MMM dd");

            Date startDateTime = null;
            Date endDateTime = null;
            try {
                startDateTime = feedsDateFormat.parse(event_dash_list_obj.getPost_start_dt_time());

                AddFourHours addFourHours = new AddFourHours();
                startDateTime = addFourHours.addHours(startDateTime).getCurrent_date();


                if (compareDate(startDateTime, new Date()) == 0) {
                    viewHolder.feeds_item_live_tag_textview.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.feeds_item_live_tag_textview.setVisibility(View.GONE);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            viewHolder.list_item_event_statr_end_dt_time_textview.setText(displayAlldayDtFormat.format(startDateTime) + ", All day event");

        } else {
            DateFormat displayStartDtFormat = new SimpleDateFormat("MMM dd, HH:mm - ");
            DateFormat displayEndTimeFormat = new SimpleDateFormat("HH:mm");

            Date startDateTime = null;
            Date endDateTime = null;

            try {
                startDateTime = feedsDateFormat.parse(event_dash_list_obj.getPost_start_dt_time());
                endDateTime = feedsDateFormat.parse(event_dash_list_obj.getPost_end_dt_time());

                AddFourHours addFourHours = new AddFourHours();
                startDateTime = addFourHours.addHours(startDateTime).getCurrent_date();
                endDateTime = addFourHours.addHours(endDateTime).getCurrent_date();

                if (startDateTime.compareTo(new Date()) < 0 && endDateTime.compareTo(new Date()) > 0) {
                    System.out.println("LIVE EVENT 1");
                    viewHolder.feeds_item_live_tag_textview.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.feeds_item_live_tag_textview.setVisibility(View.GONE);

                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            viewHolder.list_item_event_statr_end_dt_time_textview.setText(displayStartDtFormat.format(startDateTime) + displayEndTimeFormat.format(endDateTime));
        }
        // Picasso.get().load(event_dash_list_obj.getImage_url()).fit().centerCrop().into(viewHolder.main_card_imageview);
        Picasso.with(context).load(event_dash_list_obj.getImage_url()).fit().centerCrop().into(viewHolder.main_card_imageview);
        viewHolder.list_item_event_title.setText(event_dash_list_obj.getUser_comment());
        viewHolder.list_item_post_dt_time.setText(event_dash_list_obj.getPost_dt());
        viewHolder.list_item_post_dt_time.setText(event_dash_list_obj.getPost_dt());

        System.out.println("event_dash_list_obj.getUser_likes() " + event_dash_list_obj.getUser_like());
        System.out.println("event_dash_list_obj.getUser_spam() " + event_dash_list_obj.getUser_spam());
        System.out.println("event_dash_list_obj.getTotal_likes() " + event_dash_list_obj.getTotal_likes());
        System.out.println("event_dash_list_obj.getTotal_spam() " + event_dash_list_obj.getTotal_spam());
        System.out.println("event_dash_list_obj.getTotal_comments() " + event_dash_list_obj.getTotal_comments());

        //set count of likes/spam/comments
        String actionCountString = "";
        if (event_dash_list_obj.getTotal_likes() > 0) {
            actionCountString = "• " + event_dash_list_obj.getTotal_likes() + " Verified ";
        }
        if (event_dash_list_obj.getTotal_spam() > 0) {
            actionCountString = actionCountString + " • " + event_dash_list_obj.getTotal_spam() + " Marked Spam";
        }
        if (event_dash_list_obj.getTotal_comments() > 0) {
            actionCountString = actionCountString + " • " + event_dash_list_obj.getTotal_comments() + " Commented";
        }
        if (actionCountString.equals("")) {
            viewHolder.likes_status_textview.setVisibility(View.GONE);
        } else {
            viewHolder.likes_status_textview.setVisibility(View.VISIBLE);
            viewHolder.likes_status_textview.setText(actionCountString);
        }


        System.out.print(" TYPE no   " + event_dash_list_obj.getLocation_type());
        System.out.println("TYPE name " + event_dash_list_obj.getLocation_name());

        //set location type
        switch (event_dash_list_obj.getLocation_type()) {
            case 0:
                viewHolder.post_location_textview.setText("Map location");
                viewHolder.post_location_imageview.setVisibility(View.VISIBLE);
                break;
            case 1:
                viewHolder.post_location_textview.setText("Map location");
                viewHolder.post_location_imageview.setVisibility(View.VISIBLE);
                break;
            case 2:
                viewHolder.post_location_textview.setText("@ " + event_dash_list_obj.getLocation_name());
                viewHolder.post_location_imageview.setVisibility(View.INVISIBLE);
                break;
        }

        //set user like/spam icons
        switch (event_dash_list_obj.getUser_like()) {
            case 0:
                viewHolder.list_item_verify_iv.setImageResource(R.drawable.approve_light_grey_48);
                break;

            case 1:
                viewHolder.list_item_verify_iv.setImageResource(R.drawable.approve_blue_48);
                break;
        }
        switch (event_dash_list_obj.getUser_spam()) {
            case 0:
                viewHolder.list_item_spam_iv.setImageResource(R.drawable.spam_light_grey_48);
                break;

            case 1:
                viewHolder.list_item_spam_iv.setImageResource(R.drawable.spam_orange_48);
                break;
        }

        switch (event_dash_list_obj.getPost_as()) {
            //0: individual
            //1: group
            //2: anonymous
            case 0:
                viewHolder.list_item_user_name.setText(event_dash_list_obj.getUser_name());

                if (event_dash_list_obj.getUser_dp_url() == null) {
                    viewHolder.list_item_user_img.setImageResource(R.drawable.avatar_100_3);
                } else {
                    Picasso.with(context).load(event_dash_list_obj.getUser_dp_url())
                            .fit().centerCrop().into(viewHolder.list_item_user_img);
                }
                break;
            case 1:
                viewHolder.list_item_user_name.setText(event_dash_list_obj.getGrp_name());
                System.out.println("ADAPTER " + event_dash_list_obj.getGrp_name());
                if (event_dash_list_obj.getGrp_dp_url() == null) {
                    viewHolder.list_item_user_img.setImageResource(R.drawable.avatar_100_3);
                } else {
                    Picasso.with(context).load(event_dash_list_obj.getGrp_dp_url())
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

    private int compareDate(Date startDateTime, Date date) {

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(startDateTime).compareTo(dateFormat.format(date));
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
        private TextView feeds_item_live_tag_textview;
        private TextView list_item_event_title;
        private TextView list_item_event_statr_end_dt_time_textview;
        private TextView list_item_user_name;
        private TextView list_item_post_dt_time;
        private TextView likes_status_textview;
        private TextView post_location_textview;
        private ImageView post_location_imageview;
    }
}