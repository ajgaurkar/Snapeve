package com.umbcapp.gaurk.snapeve;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.umbcapp.gaurk.snapeve.Adapters.AttendiesAdapter;
import com.umbcapp.gaurk.snapeve.Adapters.CommentsAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.AttendiesListItem;
import com.umbcapp.gaurk.snapeve.Controllers.CommentsListItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class EventDetails extends AppCompatActivity implements Listview_communicator {

    ArrayList<CommentsListItem> commentsList = new ArrayList<CommentsListItem>();
    private ImageView event_detail_event_image_image_view;
    private Intent eventDetailIntent;
    private String user_id;
    private String img_url;
    private String comm_time;
    private String user_comment;
    private TextView event_detail_user_name_text_view;
    private TextView event_detail_user_post_dt_time_text_view;
    private TextView event_detail_user_comment_text_view;
    private String user_name;
    private CardView event_detail_attendies_text_view;
    private TextView attendies_dialog_layout_attendies_count_textview;
    private TextView attendies_dialog_switch_group_textview;
    private TextView attendies_dialog_switch_all_textview;
    private ListView attendies_dialog_layout_attendies_listview;
    private ArrayList<AttendiesListItem> attendiesClubbedArrayList;
    private ArrayList<AttendiesListItem> attendiesAttendingArrayList;
    private ArrayList<AttendiesListItem> attendiesNotAttendingRequestedArrayList;
    private ArrayList<AttendiesListItem> attendiesNotAttendingArrayList;
    private int attendies_type_selection_status;
    private AttendiesAdapter attendiesAdapter;
    private int listViewVisiblePosition;
    private Parcelable state;
    private int intent_type;
    private RelativeLayout merge_options_layout;
    private TextView merge_event_cancel_btn_text_view;
    private TextView merge_event_merge_btn_text_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

        attendies_type_selection_status = 0;
        eventDetailIntent = getIntent();
        user_id = eventDetailIntent.getStringExtra("user_id");
        intent_type = eventDetailIntent.getIntExtra("intent_type", 3);
        user_name = eventDetailIntent.getStringExtra("user_name");
//        img_url = eventDetailIntent.getStringExtra("img_url").replace("\\n", "\n").replace("\\", "");
        img_url = eventDetailIntent.getStringExtra("img_url");
        comm_time = eventDetailIntent.getStringExtra("comm_time");
        user_comment = eventDetailIntent.getStringExtra("user_comment");

        merge_options_layout = (RelativeLayout) findViewById(R.id.merge_options_layout);
        event_detail_user_name_text_view = (TextView) findViewById(R.id.event_detail_user_name_text_view);
        event_detail_user_post_dt_time_text_view = (TextView) findViewById(R.id.event_detail_user_post_dt_time_text_view);
        event_detail_user_comment_text_view = (TextView) findViewById(R.id.event_detail_user_comment_text_view);

        merge_event_cancel_btn_text_view = (TextView) findViewById(R.id.merge_event_cancel_btn_text_view);
        merge_event_merge_btn_text_view = (TextView) findViewById(R.id.merge_event_merge_btn_text_view);

        event_detail_attendies_text_view = (CardView) findViewById(R.id.event_detail_attendies_card_view);
        event_detail_event_image_image_view = (ImageView) findViewById(R.id.event_detail_event_image_image_view);

        event_detail_user_name_text_view.setText(user_name);
        event_detail_user_post_dt_time_text_view.setText(comm_time);
        event_detail_user_comment_text_view.setText(user_comment);
        Picasso.get().load(img_url).fit().centerCrop().into(event_detail_event_image_image_view);

        ListView eventDetailsCommentsListView = (ListView) findViewById(R.id.event_detail_comments_list_view);
        commentsList.add(new CommentsListItem("u1", "Jhon Paul", "speaking to a a packed crowd at Sanders Theatre, a nobel laureate discussing her most recent scientific discovery, or the Harvard senior talent show, there’s always something happening at Harvard.", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
        commentsList.add(new CommentsListItem("u1", "Jhon Paul", "Good", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
        commentsList.add(new CommentsListItem("u1", "Jhon Paul", "Good", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
        commentsList.add(new CommentsListItem("u1", "Jhon Paul", "Whether you have meditated for a long time or have never meditated, come join us for this time of practice together. Come to relax, quiet your mind, learn how to experience less suffering and stress, explore Buddhist philosophy and psychology, just talk about what it means to live from compassion and awareness -- or come because you are curious. This group meets on Tuesdays from 5 - 6 p.m. in in Chapin Chapel, and is led by Mark Hart, Buddhist Advisor.", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
        commentsList.add(new CommentsListItem("u1", "Jhon Paul", "Good", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
        commentsList.add(new CommentsListItem("u1", "Jhon Paul", "Good", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
        commentsList.add(new CommentsListItem("u1", "Jhon Paul", "Good", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));

        CommentsAdapter commentsAdapter = new CommentsAdapter(getApplicationContext(), commentsList);
        eventDetailsCommentsListView.setAdapter(commentsAdapter);

        event_detail_attendies_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAttendiesDialog();
            }
        });
        if (intent_type == 1) {
            merge_options_layout.setVisibility(View.VISIBLE);
        } else {
            merge_options_layout.setVisibility(View.GONE);
        }

        merge_event_merge_btn_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("Merge_action","1");
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
        merge_event_cancel_btn_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("Merge_action","2");
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

    }


    private void showAttendiesDialog() {
        Dialog alertDialog = new Dialog(this);
        LayoutInflater flater = this.getLayoutInflater();
        View view = flater.inflate(R.layout.attendies_dialog, null);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(view);
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);

        attendies_dialog_layout_attendies_count_textview = (TextView) view.findViewById(R.id.attendies_dialog_layout_attendies_count_textview);
        attendies_dialog_layout_attendies_listview = (ListView) view.findViewById(R.id.attendies_dialog_layout_attendies_listview);
        attendies_dialog_switch_group_textview = (TextView) view.findViewById(R.id.attendies_dialog_switch_group_textview);
        attendies_dialog_switch_all_textview = (TextView) view.findViewById(R.id.attendies_dialog_switch_all_textview);

        attendies_dialog_switch_group_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendies_dialog_switch_group_textview.setBackground(getResources().getDrawable(R.drawable.text_selection_left_seleted));
                attendies_dialog_switch_all_textview.setBackground(getResources().getDrawable(R.drawable.text_selection_right_unseleted));
                attendies_type_selection_status = 0;
                fetchAndPopulateAttendiesList(attendies_type_selection_status);
            }
        });
        attendies_dialog_switch_all_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendies_dialog_switch_group_textview.setBackground(getResources().getDrawable(R.drawable.text_selection_left_unseleted));
                attendies_dialog_switch_all_textview.setBackground(getResources().getDrawable(R.drawable.text_selection_right_seleted));
                attendies_type_selection_status = 1;
                fetchAndPopulateAttendiesList(attendies_type_selection_status);
            }
        });

        fetchAndPopulateAttendiesList(0);

        alertDialog.show();
    }

    private void fetchAndPopulateAttendiesList(int attendies_type_selection_status) {

        attendiesClubbedArrayList = new ArrayList<AttendiesListItem>();
        attendiesNotAttendingArrayList = new ArrayList<AttendiesListItem>();
        attendiesAttendingArrayList = new ArrayList<AttendiesListItem>();
        attendiesNotAttendingRequestedArrayList = new ArrayList<AttendiesListItem>();

//        attendiesAttendingArrayList.add(new AttendiesListItem("uid1", "Ajinkya Gaurkar", 0, 1, "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
        attendiesNotAttendingArrayList.add(new AttendiesListItem("uid1", "Ajinkya Siddharth", 0, 0, "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
//        attendiesAttendingArrayList.add(new AttendiesListItem("uid1", "Neha Reddy", 0, 1, "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
        attendiesNotAttendingRequestedArrayList.add(new AttendiesListItem("uid1", "Neha Srinivas", 1, 0, "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
//        attendiesAttendingArrayList.add(new AttendiesListItem("uid1", "Ajinkya Gaurkar", 0, 1, "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
        attendiesNotAttendingArrayList.add(new AttendiesListItem("uid1", "Ajinkya Siddharth", 0, 0, "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
//        attendiesAttendingArrayList.add(new AttendiesListItem("uid1", "Neha Reddy", 0, 1, "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
        attendiesNotAttendingRequestedArrayList.add(new AttendiesListItem("uid1", "Neha Srinivas", 1, 0, "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
        attendiesAttendingArrayList.add(new AttendiesListItem("uid1", "Ajinkya Gaurkar", 0, 1, "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
        attendiesNotAttendingArrayList.add(new AttendiesListItem("uid1", "Ajinkya Siddharth", 0, 0, "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
        attendiesAttendingArrayList.add(new AttendiesListItem("uid1", "Neha Reddy", 0, 1, "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
        attendiesNotAttendingRequestedArrayList.add(new AttendiesListItem("uid1", "Neha Srinivas", 1, 0, "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
        attendiesAttendingArrayList.add(new AttendiesListItem("uid1", "Ajinkya Gaurkar", 0, 1, "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
        attendiesNotAttendingArrayList.add(new AttendiesListItem("uid1", "Ajinkya Siddharth", 0, 0, "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
        attendiesAttendingArrayList.add(new AttendiesListItem("uid1", "Neha Reddy", 0, 1, "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
        attendiesNotAttendingRequestedArrayList.add(new AttendiesListItem("uid1", "Neha Srinivas", 1, 0, "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));

        attendiesClubbedArrayList.addAll(attendiesAttendingArrayList);
        attendiesClubbedArrayList.addAll(attendiesNotAttendingRequestedArrayList);
        attendiesClubbedArrayList.addAll(attendiesNotAttendingArrayList);
        //attendiesAdapter = new AttendiesAdapter(getApplicationContext(), attendiesClubbedArrayList, attendies_type_selection_status);
        //getApplicationContext not working for listview item click listener Interface, Hence EventDetails.this
        attendiesAdapter = new AttendiesAdapter(EventDetails.this, attendiesClubbedArrayList, attendies_type_selection_status);

        attendies_dialog_layout_attendies_listview.setAdapter(attendiesAdapter);

        if (attendies_type_selection_status == 0) {
            attendies_dialog_layout_attendies_count_textview.setText(attendiesAttendingArrayList.size() + " Group members attending");
        }
        if (attendies_type_selection_status == 1) {
            attendies_dialog_layout_attendies_count_textview.setText(attendiesClubbedArrayList.size() + " others attending");
        }


    }

    @Override
    public void main_event_listview_element_clicked(int position, int click_code) {

// Restore previous state (including selected item index and scroll position)
        state = attendies_dialog_layout_attendies_listview.onSaveInstanceState();

        modifyRequestStatus(position);

    }

    private void modifyRequestStatus(int position) {

        AttendiesListItem selectedAttendiesListItem = attendiesClubbedArrayList.get(position);

        int tempStatus = selectedAttendiesListItem.getRequest_status();
        Log.d("tempStatus before", tempStatus + "");

        if (tempStatus == 0) {
            selectedAttendiesListItem.setRequest_status(1);
        }
        if (tempStatus == 1) {
            selectedAttendiesListItem.setRequest_status(0);
        }
        attendiesClubbedArrayList.set(position, selectedAttendiesListItem);

        //Update listview and set selection
        attendiesAdapter.notifyDataSetChanged();
        attendies_dialog_layout_attendies_listview.onRestoreInstanceState(state);

    }
}
