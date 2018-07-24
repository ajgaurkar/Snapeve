package com.umbcapp.gaurk.snapeve;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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
import com.umbcapp.gaurk.snapeve.Controllers.Event_dash_list_obj;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

        attendies_type_selection_status = 0;
        eventDetailIntent = getIntent();
        user_id = eventDetailIntent.getStringExtra("user_id");
        post_id = eventDetailIntent.getStringExtra("post_id");
        intent_type = eventDetailIntent.getIntExtra("intent_type", 3);
        user_name = eventDetailIntent.getStringExtra("user_name");
        img_url = eventDetailIntent.getStringExtra("img_url");
        comm_time = eventDetailIntent.getStringExtra("comm_time");
        user_comment = eventDetailIntent.getStringExtra("user_comment");

        merge_options_layout = (RelativeLayout) findViewById(R.id.merge_options_layout);
        list_item_status_spinner = (Spinner) findViewById(R.id.list_item_status_spinner_textview);
        event_detail_user_name_text_view = (TextView) findViewById(R.id.event_detail_user_name_text_view);
        event_detail_user_post_dt_time_text_view = (TextView) findViewById(R.id.event_detail_user_post_dt_time_text_view);
        event_detail_user_comment_text_view = (TextView) findViewById(R.id.event_detail_user_comment_text_view);

        merge_event_cancel_btn_text_view = (TextView) findViewById(R.id.merge_event_cancel_btn_text_view);
        merge_event_merge_btn_text_view = (TextView) findViewById(R.id.merge_event_merge_btn_text_view);
        event_detail_individual_image_comment_textview = (TextView) findViewById(R.id.event_detail_individual_image_comment_textview);

        event_detail_attendies_text_view = (CardView) findViewById(R.id.event_detail_attendies_card_view);
        event_detail_event_image_image_view = (ImageView) findViewById(R.id.event_detail_event_image_image_view);

        list_item_verify_iv = (ImageView) findViewById(R.id.list_item_verify_iv);
        list_item_verify_tv = (TextView) findViewById(R.id.list_item_verify_tv);
//        list_item_deny_iv = (ImageView) findViewById(R.id.list_item_deny_iv);
        list_item_spam_iv = (ImageView) findViewById(R.id.list_item_spam_iv);
        list_item_spam_tv = (TextView) findViewById(R.id.list_item_spam_tv);

        fillAttendingSpinner();

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

        list_item_verify_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                actionEvent(1, 1, "Test");

            }
        });
        list_item_verify_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                actionEvent(1, 1, "Test");

            }
        });
        list_item_spam_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                actionEvent(1, 3, "Test");

            }
        });
        list_item_spam_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                actionEvent(1, 3, "Test");

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


        if (!comment.equals("")) {
            final ProgressDialog mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Commenting. Please wait...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

            JsonObject jsonObjectPostEventParameters = new JsonObject();

            jsonObjectPostEventParameters.addProperty("source_user_id", new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_USER_ID));
            jsonObjectPostEventParameters.addProperty("target_user_id", "");
            jsonObjectPostEventParameters.addProperty("post_id", post_id);
            jsonObjectPostEventParameters.addProperty("comment", comment);

            final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
            ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("image_comments_api", jsonObjectPostEventParameters);

            Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
                @Override
                public void onFailure(Throwable exception) {
                    resultFuture.setException(exception);
                    System.out.println(" image_comments_api exception    " + exception);
                    mProgressDialog.dismiss();
                }

                @Override
                public void onSuccess(JsonElement response) {
                    resultFuture.set(response);
                    System.out.println(" image_comments_api success response    " + response);
                    validateCommentResponse(response);
                    mProgressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Comment field empty. Try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void validateCommentResponse(JsonElement response) {
        System.out.println("validateCommentResponse : " + response);
    }

    private void actionEvent(int currentstatus, int click_code, String userComment) {
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
        jsonObjectPostEventParameters.addProperty("user_id", user_id);
        jsonObjectPostEventParameters.addProperty("post_id", post_id);
        jsonObjectPostEventParameters.addProperty("click_code", click_code);


        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Posting. Please wait...");

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
                mProgressDialog.dismiss();

            }
        });
    }

    private void fetchAttendieslist() {

        JsonObject jsonObjectPostEventParameters = new JsonObject();

        jsonObjectPostEventParameters.addProperty("post_id", post_id);

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

        System.out.println(" IN PARSE JASON");

        JsonArray attendiesJsonArray = attendiesResponse.getAsJsonArray();

        for (int j = 0; j < attendiesJsonArray.size(); j++) {
            JsonObject attendies_list_object = attendiesJsonArray.get(j).getAsJsonObject();
            System.out.println(" attendies_list_object  " + attendies_list_object);

            String user_id = attendies_list_object.get("USER_ID").getAsString();
            int attend_status = attendies_list_object.get("ATTEND_STATUS").getAsInt();
            String img_dp_url = attendies_list_object.get("DP_URL").getAsString();
            String user_name = attendies_list_object.get("USER_NAME").getAsString();
            String first_name = attendies_list_object.get("FIRST_NAME").getAsString();
            String last_name = attendies_list_object.get("LAST_NAME").getAsString();
            String grp_id = attendies_list_object.get("GRP_ID").getAsString();

            //MISSING GRP ID IN SESSION MANAGER. NEED TO ADD IT. NEED IT HERE
//            if(grp_id.equals(new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY)))

            attendiesGrpMemberArrayList.add(0, new AttendiesListItem(user_id, user_name, 0, attend_status, img_dp_url));

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

//        list_item_status_spinner.setPrompt("sdhv");

    }

    private void fillCommentsUserNameSpinner() {

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("");
        categories.add("T_cruise");
        categories.add("Aj_gaur");
        categories.add("Amey_bawiskar");
        categories.add("Neha_reddy");
        categories.add("Kapil_k");
        categories.add("Rushabh");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.comments_blank_first_simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_comment_dialog_user_name_spinner.setAdapter(dataAdapter);

    }

    private void executeAttendEventApi(int interested_status) {
        JsonObject jsonObjectPostEventParameters = new JsonObject();


        jsonObjectPostEventParameters.addProperty("code", interested_status);
        jsonObjectPostEventParameters.addProperty("postId", post_id);
        jsonObjectPostEventParameters.addProperty("userId", user_id);

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

        System.out.println(" attendiesGrpMemberArrayList " + attendiesGrpMemberArrayList.size());
        attendiesAdapter = new AttendiesAdapter(EventDetails.this, attendiesGrpMemberArrayList, attendies_type_selection_status);

        attendies_dialog_layout_attendies_listview.setAdapter(attendiesAdapter);

        if (attendies_type_selection_status == 0) {
            attendies_dialog_layout_attendies_count_textview.setText(attendiesGrpMemberArrayList.size() + " Group members attending");
        }
        if (attendies_type_selection_status == 1) {
            attendies_dialog_layout_attendies_count_textview.setText(attendiesGrpMemberArrayList.size() + " others attending");
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

        int tempStatus = selectedAttendiesListItem.getRequest_status();
        Log.d("tempStatus before", tempStatus + "position " + position);

        if (tempStatus == 0) {
            selectedAttendiesListItem.setRequest_status(1);
        }
        if (tempStatus == 1) {
            selectedAttendiesListItem.setRequest_status(0);
        }
        attendiesGrpMemberArrayList.set(position, selectedAttendiesListItem);

        //Update listview and set selection
        attendiesAdapter.notifyDataSetChanged();
        attendies_dialog_layout_attendies_listview.onRestoreInstanceState(state);

    }
}
