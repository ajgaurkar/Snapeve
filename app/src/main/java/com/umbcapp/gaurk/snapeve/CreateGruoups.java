package com.umbcapp.gaurk.snapeve;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.umbcapp.gaurk.snapeve.Fragments.Add_mem_fragment;
import com.umbcapp.gaurk.snapeve.Fragments.ApprovalPendingFragment;
import com.umbcapp.gaurk.snapeve.Fragments.Mem_joined_fragment;
import com.umbcapp.gaurk.snapeve.Fragments.RequestPendingFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateGruoups extends Activity {
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_groups);

        fragmentManager = getFragmentManager();

        add_mem_fragment = new Add_mem_fragment();
        mem_joined_fragment = new Mem_joined_fragment();
        req_pending_fragment = new RequestPendingFragment();
        approval_pending_fragment = new ApprovalPendingFragment();

        add_mem_layout = (RelativeLayout) findViewById(R.id.add_mem_add_mem_layout);
        mem_joined_layout = (RelativeLayout) findViewById(R.id.add_mem_joined_mem_layout);
        req_pend_layout = (RelativeLayout) findViewById(R.id.add_mem_req_sent_layout);
        appr_pend_layout = (RelativeLayout) findViewById(R.id.add_mem_pending_appr_layout);

        create_group_profile_pic_image_view = (CircleImageView) findViewById(R.id.create_group_profile_pic_image_view);

        create_group_cardview_bottom_view = (View) findViewById(R.id.create_group_cardview_bottom_view);
        create_group_cardview_req_pending_bottom_view = (View) findViewById(R.id.create_group_cardview_req_pending_bottom_view);
        create_group_cardview_req_sent_bottom_view = (View) findViewById(R.id.create_group_cardview_req_sent_bottom_view);
        create_group_cardview_add_mem_bottom_view = (View) findViewById(R.id.create_group_cardview_add_mem_bottom_view);

        Picasso.get().load("https://thumbs.dreamstime.com/b/group-friends-having-fun-beach-summer-holidays-vacation-happy-people-concept-34394694.jpg")
                .fit().centerCrop().into(create_group_profile_pic_image_view);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.create_group_list_layout, mem_joined_fragment);
        fragmentTransaction.commit();

        add_mem_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

            }
        });
        mem_joined_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

            }
        });
        req_pend_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

            }
        });
        appr_pend_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

            }
        });

    }
}
