package com.umbcapp.gaurk.snapeve.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.umbcapp.gaurk.snapeve.Controllers.CommentsListItem;
import com.umbcapp.gaurk.snapeve.Controllers.UserContributionListItem;
import com.umbcapp.gaurk.snapeve.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserContributionAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    List<UserContributionListItem> contribution_list;
    String entryDate = null;

    public UserContributionAdapter(Context context, List<UserContributionListItem> contribution_list) {
        this.contribution_list = contribution_list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return contribution_list.size();
    }

    @Override
    public UserContributionListItem getItem(int position) {
        return contribution_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        UserContributionAdapter.ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new UserContributionAdapter.ViewHolder();
            view = this.inflater.inflate(R.layout.user_profile_contribution_list_item, parent, false);

            viewHolder.user_contri_list_item_user_pic_img_view = (CircleImageView) view.findViewById(R.id.user_contri_list_item_user_pic_img_view);
            viewHolder.user_contri_list_item_user_time_text_view = (TextView) view.findViewById(R.id.user_contri_list_item_user_time_text_view);
            viewHolder.user_contri_list_item_description_text_view = (TextView) view.findViewById(R.id.user_contri_list_item_description_text_view);
            viewHolder.user_contri_list_item_like_status_text_view = (TextView) view.findViewById(R.id.user_contri_list_item_like_status_text_view);
            viewHolder.user_contri_list_item_user_name_text_view = (TextView) view.findViewById(R.id.user_contri_list_item_user_name_text_view);

            view.setTag(viewHolder);

        } else {

            viewHolder = (UserContributionAdapter.ViewHolder) view.getTag();

        }

        UserContributionListItem selectedUserContributionListItem = contribution_list.get(position);


//        Picasso.get().load(userContributionListItem.getImage_url())
//                .fit().centerCrop().into(viewHolder.user_contri_list_item_user_pic_img_view);

        if (selectedUserContributionListItem.getDp_url() == null) {
            viewHolder.user_contri_list_item_user_pic_img_view.setImageResource(R.drawable.avatar_100_3);
        } else {
            Picasso.get().load(selectedUserContributionListItem.getDp_url())
                    .fit().centerCrop().into(viewHolder.user_contri_list_item_user_pic_img_view);
        }

        viewHolder.user_contri_list_item_user_time_text_view.setText(selectedUserContributionListItem.getCreated_at_dt_time());

        viewHolder.user_contri_list_item_description_text_view.setText(selectedUserContributionListItem.getDescription());
        viewHolder.user_contri_list_item_user_name_text_view.setText(selectedUserContributionListItem.getUser_name());
        viewHolder.user_contri_list_item_like_status_text_view.setText("12 Likes  24 Comments  3 Spam");

        return view;
    }

    public class ViewHolder {

        private CircleImageView user_contri_list_item_user_pic_img_view;
        private TextView user_contri_list_item_user_time_text_view;
        private TextView user_contri_list_item_description_text_view;
        private TextView user_contri_list_item_like_status_text_view;
        private TextView user_contri_list_item_user_name_text_view;

    }
}