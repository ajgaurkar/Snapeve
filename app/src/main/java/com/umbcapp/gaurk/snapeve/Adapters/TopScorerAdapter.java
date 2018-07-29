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
import com.umbcapp.gaurk.snapeve.Controllers.LeaderboardListItem;
import com.umbcapp.gaurk.snapeve.Listview_communicator;
import com.umbcapp.gaurk.snapeve.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopScorerAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    List<LeaderboardListItem> top_Scorer_List;

    public TopScorerAdapter(Context context, List<LeaderboardListItem> top_Scorer_List) {

        this.top_Scorer_List = top_Scorer_List;
        this.context = context;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return top_Scorer_List.size();
    }

    @Override
    public LeaderboardListItem getItem(int position) {
        return top_Scorer_List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        TopScorerAdapter.ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new TopScorerAdapter.ViewHolder();
            view = this.inflater.inflate(R.layout.top_scorer_list_item, parent, false);

            viewHolder.topscorer_list_item_dp_imageview = (CircleImageView) view.findViewById(R.id.topscorer_list_item_dp_imageview);
            viewHolder.topscorer_list_item_rank_textview = (TextView) view.findViewById(R.id.topscorer_list_item_rank_textview);
            viewHolder.topscorer_list_item_name_textview = (TextView) view.findViewById(R.id.topscorer_list_item_name_textview);

            view.setTag(viewHolder);

        } else {

            viewHolder = (TopScorerAdapter.ViewHolder) view.getTag();

        }

        LeaderboardListItem selectedLeaderBoardListItem = top_Scorer_List.get(position);

        if (selectedLeaderBoardListItem.getUserPicUrl() == null) {
            viewHolder.topscorer_list_item_dp_imageview.setImageResource(R.drawable.avatar_100_3);

        } else {
            Picasso.get().load(selectedLeaderBoardListItem.getUserPicUrl())
                    .fit().centerCrop().into(viewHolder.topscorer_list_item_dp_imageview);
        }
        viewHolder.topscorer_list_item_name_textview.setText(selectedLeaderBoardListItem.getUserName());
        viewHolder.topscorer_list_item_rank_textview.setText(String.valueOf(selectedLeaderBoardListItem.getUserRank()));


        return view;
    }

    public class ViewHolder {

        private CircleImageView topscorer_list_item_dp_imageview;
        private TextView topscorer_list_item_name_textview;
        private TextView topscorer_list_item_rank_textview;


    }
}