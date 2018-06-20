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


            view.setTag(viewHolder);

        } else {

            viewHolder = (SignupGrpAdapter.ViewHolder) view.getTag();

        }

        SignUpGrpListItem selectedSignUpGrpListItem = SignUpGrpList.get(position);

        Picasso.get().load(selectedSignUpGrpListItem.getGrpDpUrl())
                .fit().centerCrop().into(viewHolder.signup_list_item_grp_pic_img_view);
        viewHolder.signup_list_item_grp_name_text_view.setText(selectedSignUpGrpListItem.getGrpName());
        viewHolder.signup_list_total_members_count_text_view.setText(String.valueOf(selectedSignUpGrpListItem.getMembersCount()));


        return view;
    }

    public class ViewHolder {

        private CircleImageView signup_list_item_grp_pic_img_view;
        private TextView signup_list_item_grp_name_text_view;
        private TextView signup_list_total_members_count_text_view;


    }
}
