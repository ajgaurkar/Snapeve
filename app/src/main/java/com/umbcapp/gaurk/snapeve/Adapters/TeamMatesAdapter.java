package com.umbcapp.gaurk.snapeve.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.squareup.picasso.Picasso;
import com.umbcapp.gaurk.snapeve.BrowseGroupProfile;
import com.umbcapp.gaurk.snapeve.Controllers.CreateGroupListItem;
import com.umbcapp.gaurk.snapeve.Controllers.LeaderboardListItem;
import com.umbcapp.gaurk.snapeve.Controllers.TeammatesListItem;
import com.umbcapp.gaurk.snapeve.Fragments.UserProfileFragment;
import com.umbcapp.gaurk.snapeve.Listview_communicator;
import com.umbcapp.gaurk.snapeve.R;
import com.umbcapp.gaurk.snapeve.SessionManager;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeamMatesAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    List<TeammatesListItem> userProfileList;
    String entryDate = null;
    int attendies_type = 0;

    /*
    STATUS 0 = Already joined
    STATUS 1 = Pending approval
    STATUS 2 = Request Sent
    STATUS 3 = Not a member
    */

    public TeamMatesAdapter(Context context, List<TeammatesListItem> userProfileList) {

        this.userProfileList = userProfileList;
        this.attendies_type = attendies_type;
        this.context = context;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return userProfileList.size();
    }

    @Override
    public TeammatesListItem getItem(int position) {
        return userProfileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        TeamMatesAdapter.ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new TeamMatesAdapter.ViewHolder();
            view = this.inflater.inflate(R.layout.team_mates_list_item, parent, false);

            viewHolder.teammates_list_item_dp_imageview = (CircleImageView) view.findViewById(R.id.teammates_list_item_dp_imageview);
            viewHolder.teammates_list_item_name_textview = (TextView) view.findViewById(R.id.teammates_list_item_name_textview);
            viewHolder.teammates_list_item_rank_textview = (TextView) view.findViewById(R.id.teammates_list_item_rank_textview);
            viewHolder.teammates_list_item_username = (TextView) view.findViewById(R.id.teammates_list_item_username);

            view.setTag(viewHolder);

        } else {

            viewHolder = (TeamMatesAdapter.ViewHolder) view.getTag();

        }

        TeammatesListItem selectedTeammatesListItem = userProfileList.get(position);


        if (selectedTeammatesListItem.getUserPicUrl() == null) {
            viewHolder.teammates_list_item_dp_imageview.setImageResource(R.drawable.avatar_100_3);
        } else {
            Picasso.get().load(selectedTeammatesListItem.getUserPicUrl())
                    .fit().centerCrop().into(viewHolder.teammates_list_item_dp_imageview);
        }

        viewHolder.teammates_list_item_name_textview.setText(selectedTeammatesListItem.getUserName());
        viewHolder.teammates_list_item_username.setText(selectedTeammatesListItem.getUserFullName());
        viewHolder.teammates_list_item_rank_textview.setText(String.valueOf(selectedTeammatesListItem.getUserRank()));

        return view;
    }

    public class ViewHolder {

        public CircleImageView teammates_list_item_dp_imageview;
        public TextView teammates_list_item_name_textview;
        public TextView teammates_list_item_rank_textview;
        public TextView teammates_list_item_username;

    }
}