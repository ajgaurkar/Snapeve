package com.umbcapp.gaurk.snapeve;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.umbcapp.gaurk.snapeve.Adapters.LeaderBoardAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.LeaderboardListItem;
import com.umbcapp.gaurk.snapeve.R;

import java.util.ArrayList;

public class Leaderboard extends AppCompatActivity {

    int user_type = -1;
    private RecyclerView leader_board_recyclerview;
    private ArrayList<LeaderboardListItem> leaderBoardList;
    private LeaderBoardAdapter leaderBoardAdapter;
    private int maxRank = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard_activity);

        leader_board_recyclerview = (RecyclerView) findViewById(R.id.leader_board_recyclerview);
        leaderBoardList = new ArrayList<LeaderboardListItem>();
        Toast.makeText(getApplicationContext(),"HELLO 1",Toast.LENGTH_SHORT).show();
//        user_type = getIntent().getExtras().getInt("Profile_type");
//
//        if (user_type == 0) {
//            System.out.print("USER");
//            Log.d("USER", "USER");
//
//        }
//        if (user_type == 1) {
//            System.out.print("GROUP");
//            Log.d("GROUP", "GROUP");
//
//        }
//        Log.d("user_type : ", user_type + "");
//

        leaderBoardList.add(new LeaderboardListItem("LU1", "Aj", 31, "Grp UMBC", "https://www.goldenglobes.com/sites/default/files/styles/portrait_medium/public/gallery_images/17-tomcruiseag.jpg?itok=qNj0cQGV&c=c9a73b7bdf609d72214d226ab9ea015e"));
        leaderBoardList.add(new LeaderboardListItem("LU2", "Neha", 56, "Grp UMBC", "https://www.goldenglobes.com/sites/default/files/styles/portrait_medium/public/gallery_images/17-tomcruiseag.jpg?itok=qNj0cQGV&c=c9a73b7bdf609d72214d226ab9ea015e"));
        leaderBoardList.add(new LeaderboardListItem("LU3", "Sid", 18, "Grp UMBC", "https://www.goldenglobes.com/sites/default/files/styles/portrait_medium/public/gallery_images/17-tomcruiseag.jpg?itok=qNj0cQGV&c=c9a73b7bdf609d72214d226ab9ea015e"));
        leaderBoardList.add(new LeaderboardListItem("LU4", "Sri", 99, "Grp UMBC", "https://www.goldenglobes.com/sites/default/files/styles/portrait_medium/public/gallery_images/17-tomcruiseag.jpg?itok=qNj0cQGV&c=c9a73b7bdf609d72214d226ab9ea015e"));
        leaderBoardList.add(new LeaderboardListItem("LU1", "Bala", 45, "Grp UMBC", "https://www.goldenglobes.com/sites/default/files/styles/portrait_medium/public/gallery_images/17-tomcruiseag.jpg?itok=qNj0cQGV&c=c9a73b7bdf609d72214d226ab9ea015e"));
        System.out.println("xxx leaderBoardList "+leaderBoardList.size());

        leaderBoardAdapter = new LeaderBoardAdapter(leaderBoardList, maxRank);
        System.out.println("xxx leaderBoardAdapter "+leaderBoardAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        RecyclerView.LayoutManager mLayoutManager =
//                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

//        leader_board_recyclerview.setLayoutManager(mLayoutManager);
//        leader_board_recyclerview.setItemAnimator(new DefaultItemAnimator());
//
//        leader_board_recyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        leader_board_recyclerview.setLayoutManager(mLayoutManager);
        leader_board_recyclerview.setItemAnimator(new DefaultItemAnimator());

        leader_board_recyclerview.setAdapter(leaderBoardAdapter);
        System.out.println("xxx leader_board_recyclerview "+leader_board_recyclerview);

    }
}
