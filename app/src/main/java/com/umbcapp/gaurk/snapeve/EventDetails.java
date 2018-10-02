package com.umbcapp.gaurk.snapeve;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.umbcapp.gaurk.snapeve.Adapters.AttendiesAdapter;
import com.umbcapp.gaurk.snapeve.Adapters.CommentsAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.AttendiesListItem;
import com.umbcapp.gaurk.snapeve.Controllers.CommentsListItem;
import com.umbcapp.gaurk.snapeve.DatabaseRepository.SnapeveDatabaseRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventDetails extends AppCompatActivity implements Listview_communicator {

    ArrayList<CommentsListItem> commentsList = new ArrayList<CommentsListItem>();
    private ImageView event_detail_event_image_image_view;
    private Intent eventDetailIntent;
    private String user_id;
    private String img_url;
    private String comm_dt_time;
    private String user_comment;
    private TextView event_detail_user_name_text_view;
    private TextView event_detail_user_post_dt_time_text_view;
    private TextView event_detail_user_comment_text_view;
    private String user_name;
    private TextView event_detail_attendies_text_view;
    private TextView attendies_dialog_layout_attendies_count_textview;
    private TextView attendies_dialog_switch_group_textview;
    private TextView attendies_dialog_switch_all_textview;
    private ListView attendies_dialog_layout_attendies_listview;
    private ArrayList<AttendiesListItem> attendiesGrpMemberArrayList;
    private ArrayList<AttendiesListItem> attendiesOutGrpMemberArrayList;
    private int attendies_type_selection_status;
    private AttendiesAdapter attendiesAdapter;
    private int listViewVisiblePosition;
    private Parcelable state;
    private int intent_type;
    private RelativeLayout merge_options_layout;
    private TextView merge_event_cancel_btn_text_view;
    private TextView merge_event_merge_btn_text_view;
    private ImageView list_item_verify_iv;
    private ImageView list_item_spam_iv;
    private String post_id;
    private Spinner list_item_status_spinner;
    private ProgressBar attendies_dialog_layout_progressbar;
    private TextView list_item_spam_tv;
    private TextView list_item_verify_tv;
    private TextView event_detail_individual_image_comment_textview;
    private Spinner add_comment_dialog_user_name_spinner;
    private EditText add_comment_dialog_comment_edittext;
    private TextView add_comment_dialog_clear_textview;
    private String tempCommentDescVar = "";
    private int tempCommentSpinnerVar = 0;
    private TextView event_details_comment_label_tv;
    private ProgressBar event_details_comments_loading_progress_bar;
    private ListView eventDetailsCommentsListView;
    private int server_attendance_status = 0;
    private int server_action_like = 0;
    private int server_action_spam = 0;
    private TextView event_details_comments_tap_to_load_textview;
    private View event_detail_grey_view_panel;
    private String user_dp_url;
    private CircleImageView event_detail_profile_pic_image_view;
    private String grp_dp_url;
    private int post_as;
    private String grp_id;
    private String logged_in_user_grp_id;
    private String grp_name;
    private int server_like_count = 0;
    private int server_spam_count = 0;
    private int server_comment_count = 0;
    private TextView event_details_actions_count_textview;
    private long sessionCounter = 0;
    private String post_start_time = null;
    private String post_end_time = null;
    private boolean post_all_day_status = false;
    private TextView event_details_item_live_tag_textview;
    private TextView event_details_event_statr_end_dt_time_textview;
    private String post_location_name;
    private int post_location_type;
    private ImageView event_details_location_imageview;
    private TextView event_details_location_textview;
    private ImageView event_details_event_statr_end_dt_time_imageview;
    private ImageView list_item_status_spinner_imageview_1;
    private View list_item_status_spinner_imageview_2;
    private SnapeveDatabaseRepository snapeveDatabaseRepository;
    private Intent browseProfileIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionCounter = System.currentTimeMillis();
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String text = formatter.format(new Date(sessionCounter));

        setContentView(R.layout.event_details);

        attendies_type_selection_status = 0;
        eventDetailIntent = getIntent();
        user_id = eventDetailIntent.getStringExtra("user_id");
        post_id = eventDetailIntent.getStringExtra("post_id");
        intent_type = eventDetailIntent.getIntExtra("intent_type", 3);
        user_name = eventDetailIntent.getStringExtra("user_name");
        img_url = eventDetailIntent.getStringExtra("img_url");
        comm_dt_time = eventDetailIntent.getStringExtra("comm_time");
        user_comment = eventDetailIntent.getStringExtra("user_comment");
        user_dp_url = eventDetailIntent.getStringExtra("user_dp_url");

        grp_dp_url = eventDetailIntent.getStringExtra("grp_dp_url");
        grp_name = eventDetailIntent.getStringExtra("grp_name");
        grp_id = eventDetailIntent.getStringExtra("grp_id");
        post_as = eventDetailIntent.getIntExtra("post_as", 0);

        post_location_type = eventDetailIntent.getIntExtra("post_location_type", 0);
        post_location_name = eventDetailIntent.getStringExtra("post_location_name");

        post_start_time = eventDetailIntent.getStringExtra("post_start_time");
        post_end_time = eventDetailIntent.getStringExtra("post_end_time");
        post_all_day_status = eventDetailIntent.getBooleanExtra("post_all_day_status", false);

        logged_in_user_grp_id = new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_GRP_ID);

        System.out.println("EVENT DETAILS ----------");
        System.out.println("user_id : " + user_id);
        System.out.println("post_id : " + post_id);
        System.out.println("intent_type: " + intent_type);
        System.out.println("user_name : " + user_name);
        System.out.println("img_url : " + img_url);
        System.out.println("comm_dt_time : " + comm_dt_time);
        System.out.println("user_comment : " + user_comment);
        System.out.println("user_dp_url : " + user_dp_url);
        System.out.println("grp_dp_url : " + grp_dp_url);
        System.out.println("grp_name : " + grp_name);
        System.out.println("grp_id : " + grp_id);
        System.out.println("post_as : " + post_as);
        System.out.println("EVENT DETAILS ----------");

        merge_options_layout = (RelativeLayout) findViewById(R.id.merge_options_layout);
        event_detail_grey_view_panel = (View) findViewById(R.id.event_detail_grey_view_panel);
        list_item_status_spinner = (Spinner) findViewById(R.id.list_item_status_spinner_textview);
        event_detail_user_name_text_view = (TextView) findViewById(R.id.event_detail_user_name_text_view);
        event_details_event_statr_end_dt_time_textview = (TextView) findViewById(R.id.event_details_event_statr_end_dt_time_textview);
        event_details_event_statr_end_dt_time_imageview = (ImageView) findViewById(R.id.event_details_event_statr_end_dt_time_imageview);
        event_details_event_statr_end_dt_time_imageview.setVisibility(View.INVISIBLE);
        event_details_event_statr_end_dt_time_textview.setVisibility(View.INVISIBLE);

        event_details_item_live_tag_textview = (TextView) findViewById(R.id.event_details_item_live_tag_textview);
        event_details_comments_tap_to_load_textview = (TextView) findViewById(R.id.event_details_comments_tap_to_load_textview);
        event_detail_user_post_dt_time_text_view = (TextView) findViewById(R.id.event_detail_user_post_dt_time_text_view);
        event_detail_user_comment_text_view = (TextView) findViewById(R.id.event_detail_user_comment_text_view);
        event_details_comment_label_tv = (TextView) findViewById(R.id.event_details_comment_label_tv);
        event_details_actions_count_textview = (TextView) findViewById(R.id.event_details_actions_count_textview);
        event_detail_profile_pic_image_view = (CircleImageView) findViewById(R.id.event_detail_profile_pic_image_view);
        event_details_comments_loading_progress_bar = (ProgressBar) findViewById(R.id.event_details_comments_loading_progress_bar);

        merge_event_cancel_btn_text_view = (TextView) findViewById(R.id.merge_event_cancel_btn_text_view);
        event_details_location_textview = (TextView) findViewById(R.id.event_details_location_textview);
        merge_event_merge_btn_text_view = (TextView) findViewById(R.id.merge_event_merge_btn_text_view);
        event_detail_individual_image_comment_textview = (TextView) findViewById(R.id.event_detail_individual_image_comment_textview);

        event_detail_attendies_text_view = (TextView) findViewById(R.id.event_detail_attendies_text_view);
        event_detail_event_image_image_view = (ImageView) findViewById(R.id.event_detail_event_image_image_view);
        event_details_location_imageview = (ImageView) findViewById(R.id.event_details_location_imageview);
        list_item_status_spinner_imageview_1 = (ImageView) findViewById(R.id.list_item_status_spinner_imageview_1);
        list_item_status_spinner_imageview_2 = (ImageView) findViewById(R.id.list_item_status_spinner_imageview_2);

        list_item_verify_iv = (ImageView) findViewById(R.id.list_item_verify_iv);
        list_item_verify_tv = (TextView) findViewById(R.id.list_item_verify_tv);
//        list_item_deny_iv = (ImageView) findViewById(R.id.list_item_deny_iv);
        list_item_spam_iv = (ImageView) findViewById(R.id.list_item_spam_iv);
        list_item_spam_tv = (TextView) findViewById(R.id.list_item_spam_tv);

        eventDetailsCommentsListView = (ListView) findViewById(R.id.event_detail_comments_list_view);

        //it works only from main activity not from any other activity due to bundle params
        if (post_start_time != null) {
            setLocationDateTimeValues(post_start_time, post_end_time, post_all_day_status);
        }

        fetchCommentsApi(post_id);
        fillAttendingSpinner();

        event_detail_user_post_dt_time_text_view.setText(comm_dt_time);
        event_detail_user_comment_text_view.setText(user_comment);
        Picasso.with(getApplicationContext()).load(img_url).fit().centerCrop().into(event_detail_event_image_image_view);

        browseProfileIntent = null;

        switch (post_as) {
            case 0://User
                System.out.println("CASE 1 ");
                event_detail_user_name_text_view.setText(user_name);

                if (user_dp_url == null) {
                    System.out.println("CASE 1 IF");
                    event_detail_profile_pic_image_view.setImageResource(R.drawable.avatar_100_3);
                } else {
                    System.out.println("CASE 1 ELSE");
                    Picasso.with(getApplicationContext()).load(user_dp_url).fit().centerCrop().into(event_detail_profile_pic_image_view);
                }
                browseProfileIntent = new Intent(getApplicationContext(), BrowseUserProfile.class);
                browseProfileIntent.putExtra("user_id", user_id);

                break;
            case 1://Group
                event_detail_user_name_text_view.setText(grp_name);
                System.out.println("CASE 2 ");

                if (grp_dp_url == null || grp_dp_url.equals("xxxxx____xxxxx")) {
                    System.out.println("CASE 2 IF");
                    event_detail_profile_pic_image_view.setImageResource(R.drawable.avatar_100_3);
                } else {
                    System.out.println("CASE 2 ELSE");
                    Picasso.with(getApplicationContext()).load(grp_dp_url).fit().centerCrop().into(event_detail_profile_pic_image_view);
                }
                browseProfileIntent = new Intent(getApplicationContext(), BrowseGroupProfile.class);
                browseProfileIntent.putExtra("grp_id", grp_id);

                break;
            case 2://Anonymous
                event_detail_user_name_text_view.setText("Anonymous");
                event_detail_profile_pic_image_view.setImageResource(R.drawable.anonymous_dark_100);
                System.out.println("CASE 3 ");
                break;
        }


        event_detail_user_name_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (post_as != 2) {
                    startActivity(browseProfileIntent);
                } else {
                    Toast.makeText(EventDetails.this, "Anonymous User!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        event_detail_profile_pic_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (post_as != 2) {
                    startActivity(browseProfileIntent);
                } else {
                    Toast.makeText(EventDetails.this, "Anonymous User!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        event_details_comments_tap_to_load_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCommentsApi(post_id);
            }
        });

//        commentsList.add(new CommentsListItem("u1", "Jhon Paul", "speaking to a a packed crowd at Sanders Theatre, a nobel laureate discussing her most recent scientific discovery, or the Harvard senior talent show, there’s always something happening at Harvard.", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
//        commentsList.add(new CommentsListItem("u1", "Jhon Paul", "Good", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
//        commentsList.add(new CommentsListItem("u1", "Jhon Paul", "Good", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
//        commentsList.add(new CommentsListItem("u1", "Jhon Paul", "Whether you have meditated for a long time or have never meditated, come join us for this time of practice together. Come to relax, quiet your mind, learn how to experience less suffering and stress, explore Buddhist philosophy and psychology, just talk about what it means to live from compassion and awareness -- or come because you are curious. This group meets on Tuesdays from 5 - 6 p.m. in in Chapin Chapel, and is led by Mark Hart, Buddhist Advisor.", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
//        commentsList.add(new CommentsListItem("u1", "Jhon Paul", "Good", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
//        commentsList.add(new CommentsListItem("u1", "Jhon Paul", "Good", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
//        commentsList.add(new CommentsListItem("u1", "Jhon Paul", "Good", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));

//        CommentsAdapter commentsAdapter = new CommentsAdapter(getApplicationContext(), commentsList);
//        eventDetailsCommentsListView.setAdapter(commentsAdapter);

        event_detail_event_image_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageDetailIntent = new Intent(getApplicationContext(), ImageFullscreenOpen.class);
                imageDetailIntent.putExtra("img_url", img_url);
                startActivity(imageDetailIntent);
            }
        });
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
                returnIntent.putExtra("Merge_action", "1");
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
        event_detail_individual_image_comment_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddCommentDialog();
            }
        });
        merge_event_cancel_btn_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("Merge_action", "2");
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        list_item_status_spinner_imageview_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_item_status_spinner.performClick();
            }
        });
        list_item_status_spinner_imageview_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_item_status_spinner.performClick();
            }
        });

        //action_to_be_performed 1: insert , 2: delete , 3 : update from 1 to other
        //click_code 1: like , 2:comment(not used here) 3: spam
        list_item_verify_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//0 : like btn 1 : spam btn
                calculateAction(1);

            }
        });
        list_item_verify_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAction(1);

//                actionEvent(1, 1, "Test");

            }
        });
        list_item_spam_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAction(3);

//                actionEvent(1, 3, "Test");

            }
        });
        list_item_spam_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAction(3);

//                actionEvent(1, 3, "Test");

            }
        });
//        list_item_deny_iv = (ImageView) findViewById(R.id.list_item_deny_iv);
//        list_item_spam_iv = (ImageView) findViewById(R.id.list_item_spam_iv);

        list_item_status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Spinner position " + position);

                if (position != 0) {
                    executeAttendEventApi(position);

                    if (position == 4) {
                        list_item_status_spinner.setSelection(0);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        event_detail_grey_view_panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //blank listener to override clicks
            }
        });
    }

    private void setLocationDateTimeValues(String post_start_time, String post_end_time, boolean post_all_day_status) {
        DateFormat feedsDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        event_details_event_statr_end_dt_time_textview.setVisibility(View.VISIBLE);
        event_details_event_statr_end_dt_time_imageview.setVisibility(View.VISIBLE);

        if (post_all_day_status) {
            DateFormat displayAlldayDtFormat = new SimpleDateFormat("MMM dd");

            Date startDateTime = null;
            Date endDateTime = null;
            try {
                startDateTime = feedsDateFormat.parse(post_start_time);

                if (compareDate(startDateTime, new Date()) == 0) {
                    event_details_item_live_tag_textview.setVisibility(View.VISIBLE);
                } else {
                    event_details_item_live_tag_textview.setVisibility(View.GONE);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            event_details_event_statr_end_dt_time_textview.setText(displayAlldayDtFormat.format(startDateTime) + ", All day event");

        } else {
            DateFormat displayStartDtFormat = new SimpleDateFormat("MMM dd, HH:MM - ");
            DateFormat displayEndTimeFormat = new SimpleDateFormat("HH:mm");

            Date startDateTime = null;
            Date endDateTime = null;
            try {
                startDateTime = feedsDateFormat.parse(post_start_time);
                endDateTime = feedsDateFormat.parse(post_end_time);


                if (startDateTime.compareTo(new Date()) < 0 && endDateTime.compareTo(new Date()) > 0) {
                    System.out.println("LIVE EVENT 1");
                    event_details_item_live_tag_textview.setVisibility(View.VISIBLE);
                } else {
                    event_details_item_live_tag_textview.setVisibility(View.GONE);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            event_details_event_statr_end_dt_time_textview.setText(displayStartDtFormat.format(startDateTime) + displayEndTimeFormat.format(endDateTime));
        }


        //set location type
        switch (post_location_type) {
            case 0:
                event_details_location_textview.setText("Map location");
                event_details_location_imageview.setVisibility(View.VISIBLE);
                break;
            case 1:
                event_details_location_textview.setText("Map location");
                event_details_location_imageview.setVisibility(View.VISIBLE);
                break;
            case 2:
                event_details_location_textview.setText("@ " + post_location_name);
                event_details_location_imageview.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private int compareDate(Date startDateTime, Date date) {

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(startDateTime).compareTo(dateFormat.format(date));
    }

    private void calculateAction(int click_code) {
        //action_to_be_performed 1: insert , 2: delete , 3 : update from 1 to other
        //click_code 1: like , 2:comment(not used here) 3:spam
        switch (click_code) {
            case 1:
                if (server_action_like + server_action_spam == 0) {
                    System.out.println("ACTION ACTION : insert like");
                    actionEvent(1, click_code, "Test");

                }
                if (server_action_like + server_action_spam == 1) {
                    if (server_action_like == 1) {
                        System.out.println("ACTION ACTION : delete like");
                        actionEvent(2, click_code, "Test");

                    }
                    if (server_action_like == 0) {
                        System.out.println("ACTION ACTION : show dialog - update SPAM -> LIKE ");

                        openLikeSpamConfirmationDialog(click_code);

//                        actionEvent(3, 1, "Test");

                    }
                }
                break;

            case 3:
                if (server_action_like + server_action_spam == 0) {
                    System.out.println("ACTION ACTION : insert spam");
                    actionEvent(1, click_code, "Test");

                }
                if (server_action_like + server_action_spam == 1) {
                    if (server_action_spam == 1) {
                        System.out.println("ACTION ACTION : delete spam");
                        actionEvent(2, click_code, "Test");

                    }
                    if (server_action_spam == 0) {
                        System.out.println("ACTION ACTION : show dialog - update LIKE -> SPAM ");
                        openLikeSpamConfirmationDialog(click_code);

                    }
                }
                break;
        }
    }

    private void openLikeSpamConfirmationDialog(final int click_code) {
        System.out.print("IN DIALOG");
        AlertDialog.Builder builder1 = new AlertDialog.Builder(EventDetails.this);
        builder1.setCancelable(false);

        if (click_code == 1) {
            System.out.println("Do you wish to UNSPAM and LIKE");
            builder1.setMessage("Do you wish to UNSPAM and VERIFY");

        }
        if (click_code == 3) {
            System.out.println("Do you wish to UNLIKE and SPAM");
            builder1.setMessage("Do you wish to DISPROVE and SPAM");

        }

        builder1.setPositiveButton(
                "CONFIRM",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        actionEvent(3, click_code, "Test");

                    }
                });

        builder1.setNegativeButton(
                "CANCLE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder1.create();
        alertDialog.show();


    }

    private void fetchCommentsApi(String post_id) {

        System.out.println("IN fetch_post_comments_api");
        event_details_comment_label_tv.setText("Loading comments ...");
        event_details_comments_loading_progress_bar.setVisibility(View.VISIBLE);

        JsonObject jsonObjectPostEventParameters = new JsonObject();

        jsonObjectPostEventParameters.addProperty("post_id", post_id);
        jsonObjectPostEventParameters.addProperty("userId", new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_USER_ID));

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("fetch_post_details_api", jsonObjectPostEventParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                Toast.makeText(getApplicationContext(), "Something went wrong. Try again", Toast.LENGTH_SHORT).show();

                System.out.println(" fetch_post_comments_api exception    " + exception);
                event_details_comments_loading_progress_bar.setVisibility(View.GONE);
                event_details_comments_tap_to_load_textview.setVisibility(View.VISIBLE);
                event_details_comment_label_tv.setText("Coudnt load Comments. Tap to retry");

            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                System.out.println(" fetch_post_comments_api success response    " + response);
                parsePostDetails(response);
                event_details_comments_loading_progress_bar.setVisibility(View.GONE);
                event_details_comments_tap_to_load_textview.setVisibility(View.GONE);

            }
        });
    }

    private void parsePostDetails(JsonElement commentsResponse) {
        commentsList = new ArrayList<CommentsListItem>();

        System.out.println(" IN PARSE JASON");
        JsonArray commentsJsonArray = (JsonArray) commentsResponse.getAsJsonObject().get("post_comments");
        JsonArray attendStatusJsonArray = (JsonArray) commentsResponse.getAsJsonObject().get("attendance");
        JsonArray actionJsonArray = (JsonArray) commentsResponse.getAsJsonObject().get("action_status");

        System.out.println(" attendStatusJsonArray : " + attendStatusJsonArray);
        System.out.println(" commentsJsonArray : " + commentsJsonArray);
        System.out.println(" actionJsonArray : " + actionJsonArray);
//        JsonArray commentsJsonArray = post_comments.getAsJsonArray();

        if (actionJsonArray.size() == 1) {
            System.out.println("actionJsonArray.get(0) :" + actionJsonArray.get(0).getAsJsonObject().get("action_like").getAsInt());
            System.out.println("actionJsonArray.get(0) :" + actionJsonArray.get(0).getAsJsonObject().get("action_spam").getAsInt());
            server_action_spam = actionJsonArray.get(0).getAsJsonObject().get("action_spam").getAsInt();
            server_action_like = actionJsonArray.get(0).getAsJsonObject().get("action_like").getAsInt();

            //getting likes and spam data/count. replace null with 0
            //5 attributes
            server_like_count = 0;
            try {
                server_like_count = actionJsonArray.get(0).getAsJsonObject().get("like_count").getAsInt();
            } catch (Exception e) {
                e.printStackTrace();
            }
            server_spam_count = 0;
            try {
                server_spam_count = actionJsonArray.get(0).getAsJsonObject().get("action_spam").getAsInt();
            } catch (Exception e) {
                e.printStackTrace();
            }
            server_comment_count = 0;
            try {
                server_comment_count = actionJsonArray.get(0).getAsJsonObject().get("total_comments").getAsInt();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println(" NO Actions found");
        }

        setLikeSpamImageFromServerValues();

        if (attendStatusJsonArray.size() == 1) {
            System.out.println("attendStatusJsonArray.get(0) :" + attendStatusJsonArray.get(0).getAsJsonObject().get("attend_status").getAsInt());
            server_attendance_status = attendStatusJsonArray.get(0).getAsJsonObject().get("attend_status").getAsInt();
        } else {
            System.out.println(" NO Attendance found");
        }
        fillAttendingSpinner();


        for (int j = 0; j < commentsJsonArray.size(); j++) {
            JsonObject comments_list_object = commentsJsonArray.get(j).getAsJsonObject();
            System.out.println(" comments_list_object " + comments_list_object);

            String comment = comments_list_object.get("comment").getAsString();
            String source_user_id = comments_list_object.get("source_user_id").getAsString();
            String src_U_name = comments_list_object.get("src_U_name").getAsString();
            String date_time = comments_list_object.get("date_time").getAsString();

            String src_dp_url = null;
            try {
                src_dp_url = comments_list_object.get("src_dp_url").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            String target_user_id = null;
            String trgt_U_name = null;
            try {
                target_user_id = comments_list_object.get("target_user_id").getAsString();
                trgt_U_name = comments_list_object.get("trgt_U_name").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            commentsList.add(0, new CommentsListItem(source_user_id, target_user_id, src_U_name, trgt_U_name, comment, date_time, src_dp_url));

        }

        if (commentsList.size() > 0) {
            CommentsAdapter commentsAdapter = new CommentsAdapter(getApplicationContext(), commentsList);
            eventDetailsCommentsListView.setAdapter(commentsAdapter);
            event_details_comment_label_tv.setText("Comments");
        } else {
            event_details_comment_label_tv.setText("No comments");
        }
    }

    private void setLikeSpamImageFromServerValues() {
        if (server_action_like == 0) {
            list_item_verify_iv.setImageResource(R.drawable.approve_light_grey_48);
        } else {
            list_item_verify_iv.setImageResource(R.drawable.approve_blue_48);
        }
        if (server_action_spam == 0) {
            list_item_spam_iv.setImageResource(R.drawable.spam_light_grey_48);
        } else {
            list_item_spam_iv.setImageResource(R.drawable.spam_orange_48);
        }


        //set count of likes/spam/comments
        String actionCountString = "";
        if (server_like_count > 0) {
            actionCountString = "• " + server_like_count + " Verified ";
        }
        if (server_spam_count > 0) {
            actionCountString = actionCountString + " • " + server_spam_count + " Marked Spam";
        }
        if (server_comment_count > 0) {
            actionCountString = actionCountString + " • " + server_comment_count + " Commented";
        }
        event_details_actions_count_textview.setText(actionCountString);

//        if (actionCountString.equals("")) {
//            event_details_actions_count_textview.setVisibility(View.GONE);
//        } else {
//            event_details_actions_count_textview.setVisibility(View.VISIBLE);
//            event_details_actions_count_textview.setText(actionCountString);
//        }

    }

    private void openAddCommentDialog() {
        //make api call and fetch attendies list
        fetchAttendieslist();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater flater = this.getLayoutInflater();
        View view = flater.inflate(R.layout.add_comment_dialog, null);
        alertDialog.setView(view);
        alertDialog.setTitle("Add comment");
        alertDialog.setCancelable(false);

        add_comment_dialog_user_name_spinner = (Spinner) view.findViewById(R.id.add_comment_dialog_user_name_spinner);

        fillCommentsUserNameSpinner();

        add_comment_dialog_clear_textview = (TextView) view.findViewById(R.id.add_comment_dialog_clear_textview);
        add_comment_dialog_comment_edittext = (EditText) view.findViewById(R.id.add_comment_dialog_comment_edittext);
        add_comment_dialog_comment_edittext.setText(tempCommentDescVar);

        add_comment_dialog_clear_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_comment_dialog_user_name_spinner.setSelection(0);
            }
        });

        alertDialog.setPositiveButton("Comment", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                validateAndPostComment(add_comment_dialog_comment_edittext.getText().toString().trim(), add_comment_dialog_user_name_spinner.getSelectedItemPosition());
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();
    }

    private void validateAndPostComment(String comment, int selectedItemPosition) {

        String[] userIdArray = {null, "13e2388c-5102-4e91-980d-fc97d044a483",
                "ed77236d-0526-4019-b77e-9cdae29b9017",
                "f034e41d-42e5-48c1-8b2e-9d5e79d1c7ed",
                "1a4bb4a2-0afa-4c9e-9ea6-22b5789baad7",
                "73d62c6f-1c32-420a-89c5-89c987ed5aa3"};

        tempCommentDescVar = comment;
        tempCommentSpinnerVar = selectedItemPosition;

        if (!comment.equals("")) {
            final ProgressDialog mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Commenting. Please wait...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

            JsonObject jsonObjectPostEventParameters = new JsonObject();

            jsonObjectPostEventParameters.addProperty("source_user_id", new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_USER_ID));
            jsonObjectPostEventParameters.addProperty("target_user_id", userIdArray[selectedItemPosition]);
            jsonObjectPostEventParameters.addProperty("post_id", post_id);
            jsonObjectPostEventParameters.addProperty("commentDate", Calendar.getInstance().getTimeInMillis());
            jsonObjectPostEventParameters.addProperty("comment", comment);

            final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
            ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("image_comments_api", jsonObjectPostEventParameters);

            Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
                @Override
                public void onFailure(Throwable exception) {
                    resultFuture.setException(exception);
                    Toast.makeText(getApplicationContext(), "Something went wrong. Try again", Toast.LENGTH_SHORT).show();

                    openAddCommentDialog();

                    System.out.println(" image_comments_api exception    " + exception);
                    mProgressDialog.dismiss();
                }

                @Override
                public void onSuccess(JsonElement response) {
                    resultFuture.set(response);
                    System.out.println(" image_comments_api success response    " + response);
                    validateCommentResponse(response);

                    //reset values if success otherwise need these values o repopulate dialog if api call or validation failed
                    tempCommentSpinnerVar = 0;
                    tempCommentDescVar = "";
                    mProgressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Comment field empty. Try again", Toast.LENGTH_SHORT).show();
            openAddCommentDialog();

        }
    }

    private void validateCommentResponse(JsonElement response) {
        System.out.println("validateCommentResponse : " + response);
        if (!response.toString().contains("true")) {
            Toast.makeText(getApplicationContext(), "Something went wrong. Try again", Toast.LENGTH_SHORT).show();
            openAddCommentDialog();

        } else {
            Toast.makeText(getApplicationContext(), "Comment Added", Toast.LENGTH_SHORT).show();
            fetchCommentsApi(post_id);
        }
    }

    //action_to_be_performed 1: insert , 2: delete , 3 : update from 1 to other
    //click_code 1: like , 2:comment(not used here) 3:spam
    private void actionEvent(int action_to_be_performed, int click_code, String userComment) {
        JsonObject jsonObjectPostEventParameters = new JsonObject();


        if (click_code == 1) {
            jsonObjectPostEventParameters.addProperty("verify_status", 1);
            jsonObjectPostEventParameters.addProperty("spam_status", 0);
        }
        if (click_code == 2) {
            jsonObjectPostEventParameters.addProperty("verify_status", 0);
            jsonObjectPostEventParameters.addProperty("spam_status", 0);
        }
        if (click_code == 3) {
            jsonObjectPostEventParameters.addProperty("verify_status", 0);
            jsonObjectPostEventParameters.addProperty("spam_status", 1);
        }

        jsonObjectPostEventParameters.addProperty("userComment", userComment);
        jsonObjectPostEventParameters.addProperty("user_id", new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_USER_ID));
        jsonObjectPostEventParameters.addProperty("post_id", post_id);
        jsonObjectPostEventParameters.addProperty("click_code", click_code);
        jsonObjectPostEventParameters.addProperty("action_to_be_performed", action_to_be_performed);


        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Updating. Please wait...");

        mProgressDialog.show();

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("action_event_api", jsonObjectPostEventParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                System.out.println(" actionEvent exception    " + exception);
                mProgressDialog.dismiss();
            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                System.out.println(" actionEvent success response    " + response);

                parseActionResponse(response);
                mProgressDialog.dismiss();

            }
        });
    }

    private void parseActionResponse(JsonElement actionEventResponse) {

        if (actionEventResponse.toString().contains("DELETE_LIKE_SPAM") && actionEventResponse.toString().contains("true")) {
            System.out.println("CLEAR THE VALUES OF LIKES AND SPAM FROM LOCAL VARIABLES");
            server_action_like = 0;
            server_action_spam = 0;
            setLikeSpamImageFromServerValues();
        }
        if (actionEventResponse.toString().contains("INSERT_LIKE") && actionEventResponse.toString().contains("true")) {
            System.out.println("CHANGE THE VALUES OF LIKE TO 1 AND SPAM 0 TO LOCAL VARIABLES");
            server_action_like = 1;
            server_action_spam = 0;
            setLikeSpamImageFromServerValues();
        }
        if (actionEventResponse.toString().contains("INSERT_SPAM") && actionEventResponse.toString().contains("true")) {
            System.out.println("CHANGE THE VALUES OF LIKE TO 0 AND SPAM 1 TO LOCAL VARIABLES");
            server_action_like = 0;
            server_action_spam = 1;
            setLikeSpamImageFromServerValues();
        }
        if (actionEventResponse.toString().contains("UPDATE_LIKE_TO_SPAM") && actionEventResponse.toString().contains("true")) {
            System.out.println("CHANGE THE VALUES OF LIKE TO 0 AND SPAM 1 TO LOCAL VARIABLES");
            server_action_like = 0;
            server_action_spam = 1;
            setLikeSpamImageFromServerValues();
        }
        if (actionEventResponse.toString().contains("UPDATE_SPAM_TO_LIKE") && actionEventResponse.toString().contains("true")) {
            System.out.println("CHANGE THE VALUES OF LIKE TO 0 AND SPAM 1 TO LOCAL VARIABLES");
            server_action_like = 1;
            server_action_spam = 0;
            setLikeSpamImageFromServerValues();
        }

    }

    private void fetchAttendieslist() {

        JsonObject jsonObjectPostEventParameters = new JsonObject();

        jsonObjectPostEventParameters.addProperty("post_id", post_id);
        jsonObjectPostEventParameters.addProperty("user_id", new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_USER_ID));

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("fetch_attendies_list_api", jsonObjectPostEventParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                System.out.println(" fetch_attendies_list exception    " + exception);
                attendies_dialog_layout_progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                System.out.println(" fetch_attendies_list success response    " + response);
                parseAttendiesResponse(response);
                attendies_dialog_layout_progressbar.setVisibility(View.GONE);
            }
        });
    }

    private void parseAttendiesResponse(JsonElement attendiesResponse) {
        attendiesGrpMemberArrayList = new ArrayList<AttendiesListItem>();
        attendiesOutGrpMemberArrayList = new ArrayList<AttendiesListItem>();

        System.out.println(" IN PARSE JASON");

        JsonArray attendiesJsonArray = attendiesResponse.getAsJsonArray();

        for (int j = 0; j < attendiesJsonArray.size(); j++) {
            JsonObject attendies_list_object = attendiesJsonArray.get(j).getAsJsonObject();
            System.out.println(" attendies_list_object  " + attendies_list_object);

            String user_id = attendies_list_object.get("user_id").getAsString();
            String user_name = attendies_list_object.get("user_name").getAsString();
//            String first_name = attendies_list_object.get("first_name").getAsString();
//            String last_name = attendies_list_object.get("last_name").getAsString();


            // -1 is null
            // 1 to 3 are statuses
            int attend_status = -1;
            try {
                attend_status = attendies_list_object.get("attend_status").getAsInt();
            } catch (Exception e) {
                e.printStackTrace();
            }
            int req_status = -1;
            try {
                req_status = attendies_list_object.get("req_status").getAsInt();
            } catch (Exception e) {
                e.printStackTrace();
            }

            String grp_id = null;
            try {
                grp_id = attendies_list_object.get("grp_id").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String req_sender_id = null;
            try {
                req_sender_id = attendies_list_object.get("req_sender_id").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String requester_id = null;
            try {
                requester_id = attendies_list_object.get("requester_id").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            String post_or_event_id = null;
            try {
                post_or_event_id = attendies_list_object.get("post_or_event_id").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            String img_dp_url = null;
            try {
                img_dp_url = attendies_list_object.get("dp_url").getAsString();
            } catch (Exception e) {

            }


            System.out.println("shash grp id " + logged_in_user_grp_id);

            if (logged_in_user_grp_id.equals("xxxxx____xxxxx")) {
                //user not in any group -  add all entries to out group
                attendiesOutGrpMemberArrayList.add(0, new AttendiesListItem(user_id, user_name, attend_status, img_dp_url, req_status, req_sender_id, requester_id));


            } else {
                //user in some group - now filter based on grp id

                //condition to check if requester id is same as loggedin user id or not - if not ..simply dont add record to avoid duplicates
                if (requester_id == null || requester_id.equals(new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_USER_ID))) {

                    if (grp_id == null) {
                        //other user not in any grp - add in others list

                        attendiesOutGrpMemberArrayList.add(0, new AttendiesListItem(user_id, user_name, attend_status, img_dp_url, req_status, req_sender_id, requester_id));
                    } else {
                        //other user in grp - chek same grp or diff
                        if (logged_in_user_grp_id.equals(grp_id)) {
                            attendiesGrpMemberArrayList.add(0, new AttendiesListItem(user_id, user_name, attend_status, img_dp_url, req_status, req_sender_id, requester_id));
                        } else {
                            attendiesOutGrpMemberArrayList.add(0, new AttendiesListItem(user_id, user_name, attend_status, img_dp_url, req_status, req_sender_id, requester_id));
                        }
                    }
                } else {
                    System.out.println("Requester ID diff - OMIT RECORD");
                }

            }


//            if (attend_status == -1) {
//                if (logged_in_user_grp_id.equals(grp_id)) {
//                    attendiesGrpMemberArrayList.add(0, new AttendiesListItem(user_id, user_name, 0, attend_status, img_dp_url));
//                }
//            } else {
//
//                if (grp_id == null) {
//                    attendiesOutGrpMemberArrayList.add(0, new AttendiesListItem(user_id, user_name, 0, attend_status, img_dp_url));
//                } else {
//                    if (logged_in_user_grp_id.equals(grp_id)) {
//                        attendiesGrpMemberArrayList.add(0, new AttendiesListItem(user_id, user_name, 0, attend_status, img_dp_url));
//                    } else {
//                        attendiesOutGrpMemberArrayList.add(0, new AttendiesListItem(user_id, user_name, 0, attend_status, img_dp_url));
//                    }
//                }
//
//            }


            //MISSING GRP ID IN SESSION MANAGER. NEED TO ADD IT. NEED IT HERE
//            if(grp_id.equals(new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY)))
//            if (grp_id == null || !(grp_id.equals("fbcc3d0a-15ba-4200-89ce-5c1bb97c7e99"))) {
//                attendiesOutGrpMemberArrayList.add(0, new AttendiesListItem(user_id, user_name, 0, attend_status, img_dp_url));
//            } else {
//                attendiesGrpMemberArrayList.add(0, new AttendiesListItem(user_id, user_name, 0, attend_status, img_dp_url));
//            }

        }

        populateAttendiesList(0);

    }

    private void fillAttendingSpinner() {

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("");
        categories.add("Interested");
        categories.add("Attending");
        categories.add("Not Interested");
        categories.add("Clear response");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.blank_first_simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        list_item_status_spinner.setAdapter(dataAdapter);
        list_item_status_spinner.setSelection(server_attendance_status);

    }

    private void fillCommentsUserNameSpinner() {

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("");
        categories.add("meera1");
        categories.add("ajgaur");
        categories.add("ameybawiskar");
        categories.add("Neha_reddy");
        categories.add("ethel");
        categories.add("rushab");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.comments_blank_first_simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_comment_dialog_user_name_spinner.setAdapter(dataAdapter);
        add_comment_dialog_user_name_spinner.setSelection(tempCommentSpinnerVar);

    }

    private void executeAttendEventApi(int interested_status) {
        JsonObject jsonObjectPostEventParameters = new JsonObject();


        jsonObjectPostEventParameters.addProperty("code", interested_status);
        jsonObjectPostEventParameters.addProperty("postId", post_id);
        jsonObjectPostEventParameters.addProperty("userId", new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_USER_ID));

        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("attendies_req_handler_api", jsonObjectPostEventParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                System.out.println(" attendies_req_handler_api exception    " + exception);
                mProgressDialog.dismiss();
            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                System.out.println(" attendies_req_handler_api success response    " + response);
                mProgressDialog.dismiss();

            }
        });
    }

    private void showAttendiesDialog() {
        //make api call and fetch attendies list
        fetchAttendieslist();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater flater = this.getLayoutInflater();
        View view = flater.inflate(R.layout.attendies_dialog, null);
        alertDialog.setView(view);
        alertDialog.setCancelable(true);

        attendies_dialog_layout_attendies_count_textview = (TextView) view.findViewById(R.id.attendies_dialog_layout_attendies_count_textview);
        attendies_dialog_layout_progressbar = (ProgressBar) view.findViewById(R.id.attendies_dialog_layout_progressbar);
        attendies_dialog_layout_attendies_listview = (ListView) view.findViewById(R.id.attendies_dialog_layout_attendies_listview);
        attendies_dialog_switch_group_textview = (TextView) view.findViewById(R.id.attendies_dialog_switch_group_textview);
        attendies_dialog_switch_all_textview = (TextView) view.findViewById(R.id.attendies_dialog_switch_all_textview);

        attendies_dialog_switch_group_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendies_dialog_switch_group_textview.setBackground(getResources().getDrawable(R.drawable.text_selection_left_seleted));
                attendies_dialog_switch_all_textview.setBackground(getResources().getDrawable(R.drawable.text_selection_right_unseleted));
                attendies_type_selection_status = 0;
                populateAttendiesList(attendies_type_selection_status);
            }
        });
        attendies_dialog_switch_all_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendies_dialog_switch_group_textview.setBackground(getResources().getDrawable(R.drawable.text_selection_left_unseleted));
                attendies_dialog_switch_all_textview.setBackground(getResources().getDrawable(R.drawable.text_selection_right_seleted));
                attendies_type_selection_status = 1;
                populateAttendiesList(attendies_type_selection_status);
            }
        });

        alertDialog.show();
    }

    private void populateAttendiesList(int attendies_type_selection_status) {


        if (attendies_type_selection_status == 0) {
            attendies_dialog_layout_attendies_count_textview.setText(attendiesGrpMemberArrayList.size() + " Group members attending");
            System.out.println(" attendiesGrpMemberArrayList " + attendiesGrpMemberArrayList.size());
            attendiesAdapter = new AttendiesAdapter(EventDetails.this, attendiesGrpMemberArrayList, attendies_type_selection_status);
            attendies_dialog_layout_attendies_listview.setAdapter(attendiesAdapter);

        }
        if (attendies_type_selection_status == 1) {
            attendies_dialog_layout_attendies_count_textview.setText(attendiesOutGrpMemberArrayList.size() + " others attending");
            System.out.println(" attendiesGrpMemberArrayList " + attendiesOutGrpMemberArrayList.size());
            attendiesAdapter = new AttendiesAdapter(EventDetails.this, attendiesOutGrpMemberArrayList, attendies_type_selection_status);
            attendies_dialog_layout_attendies_listview.setAdapter(attendiesAdapter);

        }

    }

    @Override
    public void main_event_listview_element_clicked(int position, int click_code) {

// Restore previous state (including selected item index and scroll position)
        state = attendies_dialog_layout_attendies_listview.onSaveInstanceState();

        modifyRequestStatus(position);

    }

    private void modifyRequestStatus(int position) {

        AttendiesListItem selectedAttendiesListItem = attendiesGrpMemberArrayList.get(position);

        int tempStatus = selectedAttendiesListItem.getReq_status();
        Log.d("tempStatus before", tempStatus + "position " + position);

        if (tempStatus == 0) {
            selectedAttendiesListItem.setReq_status(1);
        }
        if (tempStatus == 1) {
            selectedAttendiesListItem.setReq_status(0);
        }
        attendiesGrpMemberArrayList.set(position, selectedAttendiesListItem);

        //Update listview and set selection
        attendiesAdapter.notifyDataSetChanged();
        attendies_dialog_layout_attendies_listview.onRestoreInstanceState(state);

    }

    @Override
    public void onPause() {
        super.onPause();

//        sessionCounter = System.currentTimeMillis() - sessionCounter;
//        long minutes = TimeUnit.MILLISECONDS.toMinutes(sessionCounter);
//        long seconds = TimeUnit.MILLISECONDS.toSeconds(sessionCounter);
//
//        System.out.println("ADDEVENT onPause sessionCounter : " + minutes + "m " + seconds + "s");

        long startTime = sessionCounter;
        long endTime = System.currentTimeMillis();
        long duration = System.currentTimeMillis() - sessionCounter;

        sessionCounter = System.currentTimeMillis() - sessionCounter;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(sessionCounter);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(sessionCounter);

        System.out.println("ADDEVENT onPause sessionCounter : " + minutes + "m " + seconds + "s");

        snapeveDatabaseRepository = new SnapeveDatabaseRepository(getApplicationContext());

        snapeveDatabaseRepository.insertSnapeveSession("ED", startTime, endTime, duration, 0);
    }

    @Override
    public void onResume() {
        sessionCounter = System.currentTimeMillis();
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String text = formatter.format(new Date(sessionCounter));

        super.onResume();
    }

}
