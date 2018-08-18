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

import java.util.ArrayList;

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
    private int desiredFragPosition = 1;
    private int user_admin_flag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_groups);

        Intent intent = getIntent();
        grp_id = intent.getStringExtra("Grp_id");
        grp_name = intent.getStringExtra("Grp_name");
        grp_dp_url = intent.getStringExtra("Grp_dp_url");
        user_admin_flag = intent.getIntExtra("user_admin_flag",0);

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

//        fetchAllFragsData(0);
        fetchAllMemberData(1);

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

        create_group_user_name_text_view.setText(grp_name);
        if (grp_dp_url == null || grp_dp_url.equals("")) {
            create_group_profile_pic_image_view.setImageResource(R.drawable.avatar_100_3);

        } else {
            Picasso.get().load(grp_dp_url).fit().centerCrop().into(create_group_profile_pic_image_view);
        }

        add_mem_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyViews(4);
            }
        });
        mem_joined_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyViews(1);
            }
        });
        req_pend_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyViews(3);
            }
        });
        appr_pend_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyViews(2);
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

    public void fetchAllMemberData(int position) {
        desiredFragPosition = position;
        progressDialog.create();
        progressDialog.show();

        JsonObject jsonObjectParameters = new JsonObject();
//        jsonObjectParameters.addProperty("admin_id", new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_USER_ID));
        jsonObjectParameters.addProperty("grp_id", grp_id);

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("group_management_data_fetch_api", jsonObjectParameters);

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
                System.out.println(" group_management_data_fetch_api success response    " + response);

                divideAndSendData(response);

            }
        });

    }

    private void divideAndSendData(JsonElement response) {
        JsonObject membersResponse = response.getAsJsonObject();
        JsonArray joinedMembers = membersResponse.getAsJsonArray("joinedMembers");
        JsonArray availableMembers = membersResponse.getAsJsonArray("availableMembers");
        JsonArray pendingMembers = membersResponse.getAsJsonArray("pendingMembers");

        System.out.println("joinedMembers" + joinedMembers);
        System.out.println("pendingMembers" + pendingMembers);
        System.out.println("availableMembers " + availableMembers);

        //set data to fragment bundel
        Bundle bundle = new Bundle();

        bundle = new Bundle();
        bundle.putString("mem_joined_response", joinedMembers.toString());
        bundle.putString("grp_id", grp_id);
        bundle.putInt("user_admin_flag", user_admin_flag);
        mem_joined_fragment.setArguments(bundle);
        modifyViews(1);

        bundle = new Bundle();
        bundle.putString("grp_req_response", pendingMembers.toString());
        bundle.putString("grp_id", grp_id);
        approval_pending_fragment.setArguments(bundle);
        modifyViews(2);
        req_pending_fragment.setArguments(bundle);
        modifyViews(3);

        bundle = new Bundle();
        bundle.putString("add_mem_response", availableMembers.toString());
        bundle.putString("grp_id", grp_id);
        add_mem_fragment.setArguments(bundle);
        modifyViews(4);

        modifyViews(desiredFragPosition);
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
