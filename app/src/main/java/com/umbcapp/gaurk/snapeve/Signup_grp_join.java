package com.umbcapp.gaurk.snapeve;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.umbcapp.gaurk.snapeve.Adapters.SignupGrpAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.SignUpGrpListItem;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Signup_grp_join extends AppCompatActivity {

    private TextView signup_grp_skip_textview;
    private TextView signup_grp_create_grp_textview;
    private ListView grpNameListView;
    private ArrayList<SignUpGrpListItem> signupGrpList;
    private CircleImageView create_group_dialog_profile_pic_image_view;
    private TextView create_group_dialog_create_btn_card_textview;
    private EditText create_group_dialog_grp_name_edittext;
    private int page_mode;
    private String temp_req_pending_grp_name_to_push_to_sp;
    private String temp_req_pending_grp_id_to_push_to_sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_grp_join);

        Intent intent = getIntent();
        page_mode = intent.getIntExtra("page_open_mode", 1);

        grpNameListView = (ListView) findViewById(R.id.signup_grp_list_view);
        signup_grp_create_grp_textview = (TextView) findViewById(R.id.signup_grp_create_grp_textview);
        signup_grp_skip_textview = (TextView) findViewById(R.id.signup_grp_skip_textview);

        fetchGroupList();

        if (page_mode == 0) {
            signup_grp_skip_textview.setVisibility(View.VISIBLE);
        }
        if (page_mode == 1) {
            signup_grp_skip_textview.setVisibility(View.GONE);
        }

        signup_grp_skip_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        signup_grp_create_grp_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
//                startActivity(new Intent(getApplicationContext(), ManageGroups.class));
                showCreateGroupDialog();
            }
        });
        grpNameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                SignUpGrpListItem selectedGrpItem = signupGrpList.get(position);

                openGroupOptions(position);

            }
        });

    }

    private void openGroupOptions(final int position) {
        final Dialog alertDialog = new Dialog(this);
        LayoutInflater flater = this.getLayoutInflater();
        View view = flater.inflate(R.layout.signup_grp_join_options, null);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(view);
        alertDialog.setTitle("Choose an option");

        CardView signup_grp_join_grp_info_btn_card_view = (CardView) view.findViewById(R.id.signup_grp_join_grp_info_btn_card_view);
        CardView signup_grp_join_send_req_btn_card_view = (CardView) view.findViewById(R.id.signup_grp_join_send_req_btn_card_view);

        signup_grp_join_grp_info_btn_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                System.out.println("grp info");

                Intent groupInfoIntent = new Intent(getApplicationContext(), BrowseGroupProfile.class);
                groupInfoIntent.putExtra("grp_id", signupGrpList.get(position).getGrpId());
                startActivity(groupInfoIntent);
            }
        });
        signup_grp_join_send_req_btn_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                requestToJoinGrp(signupGrpList.get(position).getGrpId(), new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_USER_ID));
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(true);
        alertDialog.show();

    }

    private void requestToJoinGrpDialog(final String grpId, String grpName, final String userId) {

        temp_req_pending_grp_id_to_push_to_sp = grpId;
        temp_req_pending_grp_name_to_push_to_sp = grpName;

        final AlertDialog.Builder grpJoinDialog = new AlertDialog.Builder(Signup_grp_join.this);

        grpJoinDialog.setTitle("Confirm");
        grpJoinDialog.setMessage("Do you wish to join " + grpName);
        grpJoinDialog.setCancelable(false);

        grpJoinDialog.setPositiveButton("Join", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestToJoinGrp(grpId, userId);
            }
        });
        grpJoinDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        grpJoinDialog.create().show();
    }

    private void congratulateDialog(int congratulateCode) {

        final AlertDialog.Builder grpJoinDialog = new AlertDialog.Builder(Signup_grp_join.this);

        if (congratulateCode == 0) {

            grpJoinDialog.setTitle("Your request is on the way");
            grpJoinDialog.setMessage("You will get notified when the group accepts your request");
            grpJoinDialog.setCancelable(false);

            grpJoinDialog.setPositiveButton("Ok,  Lets Proceed", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //set boolean as pending status true
                    new SessionManager(getApplicationContext()).setSpecificUserBooleanDetail(SessionManager.KEY_REQ_PENDING_GRP_STATUS, true);

                    //set data for pending status
                    new SessionManager(getApplicationContext()).setSpecificUserDetail(SessionManager.KEY_REQ_PENDING_GRP_ID, temp_req_pending_grp_id_to_push_to_sp);
                    new SessionManager(getApplicationContext()).setSpecificUserDetail(SessionManager.KEY_REQ_PENDING_GRP_NAME, temp_req_pending_grp_name_to_push_to_sp);

                    //                either parse response or store response grp data in SP as created grp id OR request to join grp as pending request
                    //depending on intent type open dashboard page or finish this page and go to profile mgmnt
//                0 : open dashboard
//                1 : finish and go back

                    if (page_mode == 0) {
                        finish();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                    if (page_mode == 1) {
                        finish();
//                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }

                }
            });
        } else {
            grpJoinDialog.setTitle("Something went wrong");
            grpJoinDialog.setMessage("Please try again");
            grpJoinDialog.setCancelable(false);

            grpJoinDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }

        grpJoinDialog.create().show();
    }

    private void showCreateGroupDialog() {

        Dialog alertDialog = new Dialog(this);
        LayoutInflater flater = this.getLayoutInflater();
        View view = flater.inflate(R.layout.create_grp_dialog, null);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(view);
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);

        create_group_dialog_grp_name_edittext = (EditText) view.findViewById(R.id.create_group_dialog_grp_name_edittext);
        create_group_dialog_create_btn_card_textview = (TextView) view.findViewById(R.id.create_group_dialog_create_btn_card_textview);
        create_group_dialog_profile_pic_image_view = (CircleImageView) view.findViewById(R.id.create_group_dialog_profile_pic_image_view);

        create_group_dialog_create_btn_card_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (create_group_dialog_grp_name_edittext.getText().toString().length() > 0) {
                    executeCreateGroupApi(create_group_dialog_grp_name_edittext.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Enter group name", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.show();

    }

    private void executeCreateGroupApi(String grp_name) {

        final ProgressDialog progressDialog = new ProgressDialog(Signup_grp_join.this);
        progressDialog.setTitle("Creating group, Please wait...");
        progressDialog.create();
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        JsonObject jsonObjectParameters = new JsonObject();
        jsonObjectParameters.addProperty("req_code", 10);
        jsonObjectParameters.addProperty("grpName", grp_name);
        jsonObjectParameters.addProperty("userId", new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_USER_ID));

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("create_grp_api", jsonObjectParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                progressDialog.dismiss();
                System.out.println(" create_grp_api exception    " + exception);

            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                progressDialog.dismiss();
                System.out.println(" create_grp_api success response    " + response);

                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), ManageGroups.class));

//                if (response.toString().contains("true")) {
//                    System.out.println("response OK");
//                    finish();
//                    startActivity(new Intent(getApplicationContext(), ManageGroups.class));
//                } else {
//                    System.out.println("Something went wrong, Try again");
//                }

            }
        });

    }

    private void requestToJoinGrp(String grpId, String userId) {

        final ProgressDialog progressDialog = new ProgressDialog(Signup_grp_join.this);
        progressDialog.setTitle("Sending request to join group, Please wait...");
        progressDialog.create();
        progressDialog.show();
        JsonObject jsonObjectParameters = new JsonObject();
        jsonObjectParameters.addProperty("req_code", 10);
        jsonObjectParameters.addProperty("grpId", grpId);
        jsonObjectParameters.addProperty("userId", userId);

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("group_request_handler_api", jsonObjectParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                progressDialog.dismiss();
                System.out.println(" group_request_handler_api exception    " + exception);

            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                progressDialog.dismiss();
                System.out.println(" group_request_handler_api success response    " + response);

                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();

                if (response.toString().contains("true")) {
                    System.out.println("response OK");

                    congratulateDialog(0);

                } else {

                    congratulateDialog(1);
                }


//                    parseJoinRequestResponse(response);
            }
        });

    }

    private void fetchGroupList() {
        final ProgressDialog progressDialog = new ProgressDialog(Signup_grp_join.this);
        progressDialog.setTitle("fetching groups, Please wait...");
        progressDialog.create();
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        JsonObject jsonObjectParameters = new JsonObject();

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("all_grp_list_api", jsonObjectParameters);
//        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("neha_test_api", jsonObjectParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                progressDialog.dismiss();
                System.out.println(" all_grp_list_api exception    " + exception);

            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                progressDialog.dismiss();

                System.out.println(" all_grp_list_api success response    " + response);

                if (response.toString().equals("[]")) {
                    Toast.makeText(getApplicationContext(), "No groups found", Toast.LENGTH_SHORT).show();
                } else {
                    parseResponse(response);
                }
            }
        });
    }

    private void parseResponse(JsonElement response) {
//    private void parseResponse() {

        signupGrpList = new ArrayList<SignUpGrpListItem>();

        System.out.println(" IN PARSE JASON");

        JsonArray groupsJSONArray = response.getAsJsonArray();

        for (int j = 0; j < groupsJSONArray.size(); j++) {
            JsonObject group_list_object = groupsJSONArray.get(j).getAsJsonObject();
            System.out.println(" group_list_object  " + group_list_object);

            String grp_id = group_list_object.get("id").getAsString();
            String grp_name = group_list_object.get("grp_name").getAsString();
            int grp_points = Integer.parseInt(group_list_object.get("total_pts").toString());
            int member_count = Integer.parseInt(group_list_object.get("member_count").toString());
            String grp_dp_url = null;
            try {
                grp_dp_url = group_list_object.get("grp_dp_url").getAsString();
            } catch (Exception e) {

            }
            System.out.println(" user_id " + grp_id);
            System.out.println(" grp_name " + grp_name);
            System.out.println(" grp_points " + grp_points);
            System.out.println(" dp_url " + grp_dp_url);

            //accept_or_request_flag : 5th param
            //0 : request
            //1 : accept
            signupGrpList.add(new SignUpGrpListItem(grp_id, member_count, grp_name, grp_dp_url, 0));

        }
        System.out.println(" signupGrpList " + signupGrpList.size());
        SignupGrpAdapter signupGrpAdapter = new SignupGrpAdapter(Signup_grp_join.this, signupGrpList);
        grpNameListView.setAdapter(signupGrpAdapter);

    }

    @Override
    public void onBackPressed() {
        if (page_mode == 0) {
            //logic to exit the app
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        } else {
            super.onBackPressed();
        }

    }


}
