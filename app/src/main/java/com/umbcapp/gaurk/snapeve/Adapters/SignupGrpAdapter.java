package com.umbcapp.gaurk.snapeve.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.umbcapp.gaurk.snapeve.Controllers.AttendiesListItem;
import com.umbcapp.gaurk.snapeve.Controllers.SignUpGrpListItem;
import com.umbcapp.gaurk.snapeve.Listview_communicator;
import com.umbcapp.gaurk.snapeve.R;
import com.umbcapp.gaurk.snapeve.Signup_grp_join;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupGrpAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    List<SignUpGrpListItem> SignUpGrpList;

    public SignupGrpAdapter(Context context, List<SignUpGrpListItem> SignUpGrpList) {

        this.SignUpGrpList = SignUpGrpList;
        this.context = context;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return SignUpGrpList.size();
    }

    @Override
    public SignUpGrpListItem getItem(int position) {
        return SignUpGrpList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        SignupGrpAdapter.ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new SignupGrpAdapter.ViewHolder();
            view = this.inflater.inflate(R.layout.signup_grp_list_item, parent, false);

            viewHolder.signup_list_item_grp_pic_img_view = (CircleImageView) view.findViewById(R.id.signup_list_item_grp_pic_img_view);
            viewHolder.signup_list_item_grp_name_text_view = (TextView) view.findViewById(R.id.signup_list_item_grp_name_text_view);
            viewHolder.signup_list_total_members_count_text_view = (TextView) view.findViewById(R.id.signup_list_total_members_count_text_view);
            viewHolder.signup_list_item_request_accept_btn_text_view = (TextView) view.findViewById(R.id.signup_list_item_request_accept_btn_text_view);

            view.setTag(viewHolder);
        } else {
            viewHolder = (SignupGrpAdapter.ViewHolder) view.getTag();
        }

        SignUpGrpListItem selectedSignUpGrpListItem = SignUpGrpList.get(position);

        //0 : request to join...from signup grp join
        //1 : accept invitation from user profile frag
        if (selectedSignUpGrpListItem.getAccept_or_request_flag() == 0) {
            viewHolder.signup_list_item_request_accept_btn_text_view.setText("     Info      |      Join    ");
        }
        if (selectedSignUpGrpListItem.getAccept_or_request_flag() == 1) {
            viewHolder.signup_list_item_request_accept_btn_text_view.setText("  Accept  ");
        }

        //condition for null PD
        if (selectedSignUpGrpListItem.getGrpDpUrl() != null) {
            System.out.println("SignupGrpAdapter getGrpDpUrl found" + selectedSignUpGrpListItem.getGrpDpUrl());
            Picasso.with(context).load(selectedSignUpGrpListItem.getGrpDpUrl())
                    .fit().centerCrop().into(viewHolder.signup_list_item_grp_pic_img_view);
        } else {
            System.out.println("SignupGrpAdapter getGrpDpUrl null" + selectedSignUpGrpListItem.getGrpDpUrl());
            viewHolder.signup_list_item_grp_pic_img_view.setImageResource(R.drawable.avatar_100_3);
        }


        viewHolder.signup_list_item_grp_name_text_view.setText(selectedSignUpGrpListItem.getGrpName());

        viewHolder.signup_list_total_members_count_text_view.setText(String.valueOf(selectedSignUpGrpListItem.getMembersCount()) + " Members");


        return view;
    }

    public class ViewHolder {

        private CircleImageView signup_list_item_grp_pic_img_view;
        private TextView signup_list_item_grp_name_text_view;
        private TextView signup_list_total_members_count_text_view;
        private TextView signup_list_item_request_accept_btn_text_view;


    }
}
