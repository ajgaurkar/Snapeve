package com.umbcapp.gaurk.snapeve.Adapters;

import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.squareup.picasso.Picasso;
import com.umbcapp.gaurk.snapeve.Controllers.LeaderboardListItem;
import com.umbcapp.gaurk.snapeve.Leaderboard;
import com.umbcapp.gaurk.snapeve.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.MyViewHolder> {
    private List<LeaderboardListItem> leaderboardList;
    int maxRank = 100;


    public LeaderBoardAdapter(ArrayList<LeaderboardListItem> leaderboardList, int maxRank) {
        this.leaderboardList = leaderboardList;
        this.maxRank = maxRank;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public TextView userRank;
        public TextView youTextView;
        public RoundCornerProgressBar userRankBar;
        public CircleImageView userPic;

        public MyViewHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.leaderboard_list_item_name_textview);
            userRank = (TextView) view.findViewById(R.id.leaderboard_list_item_rank_textview);
            youTextView = (TextView) view.findViewById(R.id.leaderboard_list_item_you_textview);
            userPic = (CircleImageView) view.findViewById(R.id.leaderboard_list_item_dp_imageview);
            userRankBar = (RoundCornerProgressBar) view.findViewById(R.id.leaderboard_list_item_rank_progress_bar);
//            year = (TextView) view.findViewById(R.id.year);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LeaderboardListItem selectedLeaderboardListItem = leaderboardList.get(position);
        holder.userName.setText(selectedLeaderboardListItem.getUserName());
//        holder.userRankBar.setProgress(maxRank);
//        holder.userRankBar.setSecondaryProgress(selectedLeaderboardListItem.getUserRank());
        holder.userRankBar.setProgress(selectedLeaderboardListItem.getUserRank());
        holder.userRankBar.setSecondaryProgress(maxRank);

        if (selectedLeaderboardListItem.getUserId().equals("LU6")) {
            holder.youTextView.setVisibility(View.VISIBLE);
        } else {
            holder.youTextView.setVisibility(View.INVISIBLE);
        }

        holder.userRank.setText(String.valueOf(selectedLeaderboardListItem.getUserRank()));
        Picasso.get().load(selectedLeaderboardListItem.getUserPicUrl())
                .fit().centerCrop().into(holder.userPic);
        System.out.println("INSIDE LeaderBoardAdapter");

    }

    @Override
    public int getItemCount() {

        return leaderboardList.size();

    }


}