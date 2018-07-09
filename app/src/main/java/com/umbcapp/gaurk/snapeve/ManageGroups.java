package com.umbcapp.gaurk.snapeve;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.umbcapp.gaurk.snapeve.Fragments.Add_mem_fragment;
import com.umbcapp.gaurk.snapeve.Fragments.ApprovalPendingFragment;
import com.umbcapp.gaurk.snapeve.Fragments.Mem_joined_fragment;
import com.umbcapp.gaurk.snapeve.Fragments.RequestPendingFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class ManageGroups extends Activity {
    private RelativeLayout add_mem_layout;
    private RelativeLayout mem_joined_layout;
    private RelativeLayout appr_pend_layout;
    private RelativeLayout req_pend_layout;
    private View create_group_cardview_bottom_view;
    private View create_group_cardview_req_pending_bottom_view;
    private View create_group_cardview_req_sent_bottom_view;
    private View create_group_cardview_add_mem_bottom_view;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    Add_mem_fragment add_mem_fragment;
    Mem_joined_fragment mem_joined_fragment;
    RequestPendingFragment req_pending_fragment;
    ApprovalPendingFragment approval_pending_fragment;
    private CircleImageView create_group_profile_pic_image_view;
    private TextView create_group_user_name_text_view;
    private String grp_id;
    private String grp_name;
    private String grp_dp_url;
    private ProgressDialog progressDialog;
    private TextView create_group_cardview_joined_mem_textview;
    private TextView create_group_cardview_req_pending_mem_textview;
    private TextView create_group_cardview_req_sent_mem_textview;
    private int currentFragPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_groups);

        Intent intent = getIntent();
        grp_id = intent.getStringExtra("Grp_id");
        grp_name = intent.getStringExtra("Grp_name");
        grp_dp_url = intent.getStringExtra("Grp_dp_url");

        fragmentManager = getFragmentManager();
        add_mem_fragment = new Add_mem_fragment();
        mem_joined_fragment = new Mem_joined_fragment();
        req_pending_fragment = new RequestPendingFragment();
        approval_pending_fragment = new ApprovalPendingFragment();

        //progress dialog properties
        progressDialog = new ProgressDialog(ManageGroups.this);
        progressDialog.setTitle("Fetching details, Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        fetchAllFragsData();

        add_mem_layout = (RelativeLayout) findViewById(R.id.add_mem_add_mem_layout);
        mem_joined_layout = (RelativeLayout) findViewById(R.id.add_mem_joined_mem_layout);
        req_pend_layout = (RelativeLayout) findViewById(R.id.add_mem_req_sent_layout);
        appr_pend_layout = (RelativeLayout) findViewById(R.id.add_mem_pending_appr_layout);

        create_group_profile_pic_image_view = (CircleImageView) findViewById(R.id.create_group_profile_pic_image_view);
        create_group_user_name_text_view = (TextView) findViewById(R.id.create_group_user_name_text_view);

        create_group_cardview_joined_mem_textview = (TextView) findViewById(R.id.create_group_cardview_joined_mem_textview);
        create_group_cardview_req_pending_mem_textview = (TextView) findViewById(R.id.create_group_cardview_req_pending_mem_textview);
        create_group_cardview_req_sent_mem_textview = (TextView) findViewById(R.id.create_group_cardview_req_sent_mem_textview);

        create_group_cardview_bottom_view = (View) findViewById(R.id.create_group_cardview_bottom_view);
        create_group_cardview_req_pending_bottom_view = (View) findViewById(R.id.create_group_cardview_req_pending_bottom_view);
        create_group_cardview_req_sent_bottom_view = (View) findViewById(R.id.create_group_cardview_req_sent_bottom_view);
        create_group_cardview_add_mem_bottom_view = (View) findViewById(R.id.create_group_cardview_add_mem_bottom_view);

//        Picasso.get().load("https://thumbs.dreamstime.com/b/group-friends-having-fun-beach-summer-holidays-vacation-happy-people-concept-34394694.jpg")
//                .fit().centerCrop().into(create_group_profile_pic_image_view);

        create_group_user_name_text_view.setText(grp_name);
        Picasso.get().load(grp_dp_url).fit().centerCrop().into(create_group_profile_pic_image_view);

        //first fragment to be loaded at startup
//        fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.create_group_list_layout, mem_joined_fragment);
//        fragmentTransaction.commit();

        add_mem_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                modifyViews(4);

//                create_group_cardview_bottom_view.setVisibility(View.INVISIBLE);
//                create_group_cardview_req_pending_bottom_view.setVisibility(View.INVISIBLE);
//                create_group_cardview_req_sent_bottom_view.setVisibility(View.INVISIBLE);
//                create_group_cardview_add_mem_bottom_view.setVisibility(View.VISIBLE);
//
//
//                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.remove(mem_joined_fragment);
//                fragmentTransaction.remove(req_pending_fragment);
//                fragmentTransaction.remove(approval_pending_fragment);
//
//                fragmentTransaction.replace(R.id.create_group_list_layout, add_mem_fragment);
//                fragmentTransaction.commit();

            }
        });
        mem_joined_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyViews(1);

//                create_group_cardview_bottom_view.setVisibility(View.VISIBLE);
//                create_group_cardview_req_pending_bottom_view.setVisibility(View.INVISIBLE);
//                create_group_cardview_req_sent_bottom_view.setVisibility(View.INVISIBLE);
//                create_group_cardview_add_mem_bottom_view.setVisibility(View.INVISIBLE);
//
//                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.remove(add_mem_fragment);
//                fragmentTransaction.remove(req_pending_fragment);
//                fragmentTransaction.remove(approval_pending_fragment);
//
//                fragmentTransaction.replace(R.id.create_group_list_layout, mem_joined_fragment);
//                fragmentTransaction.commit();

            }
        });
        req_pend_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyViews(3);

//                create_group_cardview_bottom_view.setVisibility(View.INVISIBLE);
//                create_group_cardview_req_pending_bottom_view.setVisibility(View.INVISIBLE);
//                create_group_cardview_req_sent_bottom_view.setVisibility(View.VISIBLE);
//                create_group_cardview_add_mem_bottom_view.setVisibility(View.INVISIBLE);
//
//                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.remove(mem_joined_fragment);
//                fragmentTransaction.remove(add_mem_fragment);
//                fragmentTransaction.remove(approval_pending_fragment);
//
//                fragmentTransaction.replace(R.id.create_group_list_layout, req_pending_fragment);
//                fragmentTransaction.commit();

            }
        });
        appr_pend_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                modifyViews(2);

//                create_group_cardview_bottom_view.setVisibility(View.INVISIBLE);
//                create_group_cardview_req_pending_bottom_view.setVisibility(View.VISIBLE);
//                create_group_cardview_req_sent_bottom_view.setVisibility(View.INVISIBLE);
//                create_group_cardview_add_mem_bottom_view.setVisibility(View.INVISIBLE);
//
//                fragmentTransaction = fragmentManager.beginTransaction();
//
//                fragmentTransaction.remove(mem_joined_fragment);
//                fragmentTransaction.remove(add_mem_fragment);
//                fragmentTransaction.remove(req_pending_fragment);
//
//                fragmentTransaction.replace(R.id.create_group_list_layout, approval_pending_fragment);
//                fragmentTransaction.commit();

            }
        });

    }

    private void modifyViews(int selectedFragPosition) {
        switch (selectedFragPosition) {
            case 1:
                currentFragPosition = 1;

                create_group_cardview_bottom_view.setVisibility(View.VISIBLE);
                create_group_cardview_req_pending_bottom_view.setVisibility(View.INVISIBLE);
                create_group_cardview_req_sent_bottom_view.setVisibility(View.INVISIBLE);
                create_group_cardview_add_mem_bottom_view.setVisibility(View.INVISIBLE);

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(add_mem_fragment);
                fragmentTransaction.remove(req_pending_fragment);
                fragmentTransaction.remove(approval_pending_fragment);

                fragmentTransaction.replace(R.id.create_group_list_layout, mem_joined_fragment);
                fragmentTransaction.commit();
                break;
            case 2:
                currentFragPosition = 2;

                create_group_cardview_bottom_view.setVisibility(View.INVISIBLE);
                create_group_cardview_req_pending_bottom_view.setVisibility(View.VISIBLE);
                create_group_cardview_req_sent_bottom_view.setVisibility(View.INVISIBLE);
                create_group_cardview_add_mem_bottom_view.setVisibility(View.INVISIBLE);

                fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.remove(mem_joined_fragment);
                fragmentTransaction.remove(add_mem_fragment);
                fragmentTransaction.remove(req_pending_fragment);

                fragmentTransaction.replace(R.id.create_group_list_layout, approval_pending_fragment);
                fragmentTransaction.commit();
                break;
            case 3:
                currentFragPosition = 3;

                create_group_cardview_bottom_view.setVisibility(View.INVISIBLE);
                create_group_cardview_req_pending_bottom_view.setVisibility(View.INVISIBLE);
                create_group_cardview_req_sent_bottom_view.setVisibility(View.VISIBLE);
                create_group_cardview_add_mem_bottom_view.setVisibility(View.INVISIBLE);

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(mem_joined_fragment);
                fragmentTransaction.remove(add_mem_fragment);
                fragmentTransaction.remove(approval_pending_fragment);

                fragmentTransaction.replace(R.id.create_group_list_layout, req_pending_fragment);
                fragmentTransaction.commit();

                break;
            case 4:
                currentFragPosition = 4;
                create_group_cardview_bottom_view.setVisibility(View.INVISIBLE);
                create_group_cardview_req_pending_bottom_view.setVisibility(View.INVISIBLE);
                create_group_cardview_req_sent_bottom_view.setVisibility(View.INVISIBLE);
                create_group_cardview_add_mem_bottom_view.setVisibility(View.VISIBLE);


                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(mem_joined_fragment);
                fragmentTransaction.remove(req_pending_fragment);
                fragmentTransaction.remove(approval_pending_fragment);

                fragmentTransaction.replace(R.id.create_group_list_layout, add_mem_fragment);
                fragmentTransaction.commit();
                break;

        }
    }

    public void fetchAllFragsData() {

        String userId = new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_USER_ID);
        fetchJoinedMembers(userId);
        fetchAvailableMembers("DUMMY_PLACEHOLDER_ID");
        fetchGroupRequestDetails(userId);
//        fetchGroupReqDetails(new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_USER_ID));
    }

    private void fetchGroupRequestDetails(String admin_id) {
        progressDialog.create();
        progressDialog.show();

        JsonObject jsonObjectParameters = new JsonObject();
        jsonObjectParameters.addProperty("req_code", 11);
        jsonObjectParameters.addProperty("admin_id", admin_id);

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("group_mgmt_details_fetch_api", jsonObjectParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                progressDialog.dismiss();
                System.out.println(" fetchGroupRequestDetails exception    " + exception);

            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                progressDialog.dismiss();
                System.out.println(" fetchGroupRequestDetails success response    " + response);
//                parseFetchGroupDetailsResponse(response);

                //set data to fragment bundel
                Bundle bundle = new Bundle();
                bundle.putString("grp_req_response", response.toString());
                bundle.putString("grp_id", grp_id);
                req_pending_fragment.setArguments(bundle);
                approval_pending_fragment.setArguments(bundle);


                //2 frag called just to set mem count value on main screen
//                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.create_group_list_layout, approval_pending_fragment);
//                fragmentTransaction.commit();
//
//                fragmentTransaction.replace(R.id.create_group_list_layout, req_pending_fragment);
//                fragmentTransaction.commit();

            }
        });

    }

    private void fetchAvailableMembers(String admin_id) {
        progressDialog.create();
        progressDialog.show();
        System.out.println("IN fetchAvailableMembers");
        JsonObject jsonObjectParameters = new JsonObject();
        jsonObjectParameters.addProperty("req_code", 12);
        //admin_id not needed. just placeholder for method signature
        jsonObjectParameters.addProperty("admin_id", admin_id);
//        jsonObjectParameters.addProperty("userId", new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_USER_ID));

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("group_mgmt_details_fetch_api", jsonObjectParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                progressDialog.dismiss();
                System.out.println(" fetchAvailableMembers exception    " + exception);

            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                progressDialog.dismiss();
                System.out.println(" fetchAvailableMembers success response    " + response);
//                parseFetchGroupDetailsResponse(response);

                //set data to fragment bundel
                Bundle bundle = new Bundle();
                bundle.putString("add_mem_response", response.toString());
                bundle.putString("grp_id", grp_id);
                add_mem_fragment.setArguments(bundle);

            }
        });

    }

    private void fetchJoinedMembers(String admin_id) {

        progressDialog.create();
        progressDialog.show();

        JsonObject jsonObjectParameters = new JsonObject();
        jsonObjectParameters.addProperty("req_code", 10);
        jsonObjectParameters.addProperty("admin_id", admin_id);
//        jsonObjectParameters.addProperty("userId", new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_USER_ID));

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("group_mgmt_details_fetch_api", jsonObjectParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                progressDialog.dismiss();
                System.out.println(" fetchJoinedMembers exception    " + exception);

            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                progressDialog.dismiss();
                System.out.println(" fetchJoinedMemberss success response    " + response);
//                parseFetchGroupDetailsResponse(response);

                //set data to fragment bundel
                Bundle bundle = new Bundle();
                bundle.putString("mem_joined_response", response.toString());
                mem_joined_fragment.setArguments(bundle);

                modifyViews(1);
                //first fragment to be loaded at startup
//                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.create_group_list_layout, mem_joined_fragment);
//                fragmentTransaction.commit();
            }
        });


    }

    private void parseFetchGroupDetailsResponse(JsonElement fetchGroupDetailsResponse) {

        JsonArray grpDetailsJSONArray = fetchGroupDetailsResponse.getAsJsonArray();

        for (int i = 0; i < grpDetailsJSONArray.size(); i++) {

            JsonObject grpDetails_list_object = grpDetailsJSONArray.get(i).getAsJsonObject();


            System.out.println(" grpDetails_list_object  " + grpDetails_list_object);

            String grp_id = grpDetails_list_object.get("grp_id").toString();
            String grp_name = grpDetails_list_object.get("GRP_NAME").toString();
            String grp_dp_url = grpDetails_list_object.get("GRP_DP_URL").toString();
            String user_id = grpDetails_list_object.get("user_id").toString();

            System.out.println(" grp_id " + grp_id);
            System.out.println(" grp_name " + grp_name);
            System.out.println(" user_id " + user_id);
            System.out.println(" grp_dp_url " + grp_dp_url);

            //Remove " from start and end from every string
            grp_id = grp_id.substring(1, grp_id.length() - 1);
            grp_dp_url = grp_dp_url.substring(1, grp_dp_url.length() - 1);
            user_id = user_id.substring(1, user_id.length() - 1);
            grp_name = grp_name.substring(1, grp_name.length() - 1);

        }
    }

    private void parseFetchGroupReqDetailsResponse(JsonElement fetchGroupDetailsResponse) {

        JsonArray grpDetailsJSONArray = fetchGroupDetailsResponse.getAsJsonArray();

        for (int i = 0; i < grpDetailsJSONArray.size(); i++) {

            JsonObject grpDetails_list_object = grpDetailsJSONArray.get(i).getAsJsonObject();

            System.out.println(" grpDetails_list_object  " + grpDetails_list_object);

            String user_id = grpDetails_list_object.get("USER_ID").toString();
            String user_name = grpDetails_list_object.get("USER_NAME").toString();
            String user_f_name = grpDetails_list_object.get("FIRST_NAME").toString();
            String user_l_name = grpDetails_list_object.get("LAST_NAME").toString();
            int req_code = Integer.parseInt(grpDetails_list_object.get("REQ_CODE").toString());
            Boolean req_settled = Boolean.valueOf(grpDetails_list_object.get("REQ_SETTLED").toString());

            System.out.println(" user_id " + user_id);
            System.out.println(" req_code " + req_code);
            System.out.println(" user_name " + user_name);
            System.out.println(" user_f_name " + user_f_name);
            System.out.println(" user_l_name " + user_l_name);

            try {
                String user_dp_url = grpDetails_list_object.get("DP_URL").toString();
                System.out.println(" user_dp_url " + user_dp_url);
                user_dp_url = user_dp_url.substring(1, user_dp_url.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Remove " from start and end from every string
            user_id = user_id.substring(1, user_id.length() - 1);
            user_name = user_name.substring(1, user_name.length() - 1);
            user_f_name = user_f_name.substring(1, user_f_name.length() - 1);
            user_l_name = user_l_name.substring(1, user_l_name.length() - 1);

        }
    }

    private void fetchGroupReqDetails(String admin_id) {

        final ProgressDialog progressDialog = new ProgressDialog(ManageGroups.this);
        progressDialog.setTitle("Fetching details, Please wait...");
        progressDialog.create();
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        JsonObject jsonObjectParameters = new JsonObject();
        jsonObjectParameters.addProperty("req_code", 11);
        jsonObjectParameters.addProperty("admin_id", admin_id);

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("group_mgmt_details_fetch_api", jsonObjectParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                progressDialog.dismiss();
                System.out.println(" group_mgmt_details_fetch_api exception    " + exception);

            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                progressDialog.dismiss();
                System.out.println(" group_mgmt_details_fetch_api success response    " + response);
                parseFetchGroupReqDetailsResponse(response);
            }
        });


    }


    public void takeNumbers(int frag_position, int member_count) {
        System.out.println("frag_position" + frag_position);
        switch (frag_position) {
            case 1:
                create_group_cardview_joined_mem_textview.setText(String.valueOf(member_count));
                break;
            case 2:
                create_group_cardview_req_pending_mem_textview.setText(String.valueOf(member_count));
                break;
            case 3:
                create_group_cardview_req_sent_mem_textview.setText(String.valueOf(member_count));
                break;
        }
    }

}
