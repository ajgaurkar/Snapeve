package com.umbcapp.gaurk.snapeve;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.umbcapp.gaurk.snapeve.Adapters.LeaderBoardAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.LeaderboardListItem;

import java.util.ArrayList;
import java.util.Calendar;

public class Leaderboard extends AppCompatActivity {

    int user_type = -1;
    private RecyclerView leader_board_recyclerview;
    private ArrayList<LeaderboardListItem> leaderBoardList;
    private LeaderBoardAdapter leaderBoardAdapter;
    private int maxRank = 100;
    private TextView top_10_all_sort_textview;
    private TextView leader_board_switch_grp_textview;
    private TextView leader_board_switch_you_textview;
    private int user_type_selection_status;
    private Boolean top_10_selection_status;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard_activity);

        user_type_selection_status = 0;
        top_10_selection_status = true;
        leader_board_recyclerview = (RecyclerView) findViewById(R.id.leader_board_recyclerview);

        top_10_all_sort_textview = (TextView) findViewById(R.id.top_10_all_sort_textview);
        leader_board_switch_grp_textview = (TextView) findViewById(R.id.leader_board_switch_grp_textview);
        leader_board_switch_you_textview = (TextView) findViewById(R.id.leader_board_switch_you_textview);

        System.out.println("System.currentTimeMillis() onCreate : " + System.currentTimeMillis());

        leader_board_switch_you_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                leader_board_switch_you_textview.setBackground(getResources().getDrawable(R.drawable.text_selection_left_seleted));
                leader_board_switch_grp_textview.setBackground(getResources().getDrawable(R.drawable.text_selection_right_unseleted));
                user_type_selection_status = 0;
                loadLeaderboardList(user_type_selection_status);
            }
        });

        leader_board_switch_grp_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                leader_board_switch_you_textview.setBackground(getResources().getDrawable(R.drawable.text_selection_left_unseleted));
                leader_board_switch_grp_textview.setBackground(getResources().getDrawable(R.drawable.text_selection_right_seleted));
                user_type_selection_status = 1;
                loadLeaderboardList(user_type_selection_status);

            }
        });

        top_10_all_sort_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                top_10_selection_status = !top_10_selection_status;
                if (top_10_selection_status) {
                    top_10_all_sort_textview.setText("Top 10");
                } else {
                    top_10_all_sort_textview.setText("   All    ");
                }
            }
        });

        leaderBoardList = new ArrayList<LeaderboardListItem>();

        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        loadLeaderboardList(0);

    }

    private void loadLeaderboardList(int user_type_selection_status) {

        if (user_type_selection_status == 0) {
            leaderBoardList = new ArrayList<LeaderboardListItem>();

            leaderBoardList.add(new LeaderboardListItem("LU5", "Ajinkya gaurkar", 99, "Grp UMBC", "http://keenthemes.com/preview/metronic/theme/assets/pages/media/profile/profile_user.jpg"));
            leaderBoardList.add(new LeaderboardListItem("LU8", "Pranav Rana", 73, "Grp UMBC", "http://www.dast.biz/wp-content/uploads/2016/11/John_Doe.jpg"));
            leaderBoardList.add(new LeaderboardListItem("LU6", "Srinivas sandu", 66, "Grp UMBC", "https://www.bnl.gov/today/body_pics/2017/06/stephanhruszkewycz-355px.jpg"));
            leaderBoardList.add(new LeaderboardListItem("LU2", "Neha Reddy", 56, "Grp UMBC", "http://www.medicine20congress.com/ocs/public/profiles/3141.jpg"));
            leaderBoardList.add(new LeaderboardListItem("LU9", "Rushabh Mehta", 52, "Grp UMBC", "http://www.dast.biz/wp-content/uploads/2016/11/John_Doe.jpg"));
            leaderBoardList.add(new LeaderboardListItem("LU10", "Bala Kumaran", 45, "Grp UMBC", "https://www.bnl.gov/today/body_pics/2017/06/stephanhruszkewycz-355px.jpg"));
            leaderBoardList.add(new LeaderboardListItem("LU4", "Mayur Pate", 44, "Grp UMBC", "http://www.dast.biz/wp-content/uploads/2016/11/John_Doe.jpg"));
            leaderBoardList.add(new LeaderboardListItem("LU1", "Arya Shah", 31, "Grp UMBC", "https://cdn.earthdata.nasa.gov/conduit/upload/6072/Glenn_headshot_resize.jpg"));
            leaderBoardList.add(new LeaderboardListItem("LU3", "Karen Dhruv", 18, "Grp UMBC", "https://www.bnl.gov/today/body_pics/2017/06/stephanhruszkewycz-355px.jpg"));
            leaderBoardList.add(new LeaderboardListItem("LU7", "Sri", 15, "Grp UMBC", "http://www.dast.biz/wp-content/uploads/2016/11/John_Doe.jpg"));

            leaderBoardAdapter = new LeaderBoardAdapter(leaderBoardList, maxRank, user_type_selection_status);
            leader_board_recyclerview.setLayoutManager(mLayoutManager);
            leader_board_recyclerview.setItemAnimator(new DefaultItemAnimator());
            leader_board_recyclerview.setAdapter(leaderBoardAdapter);

        }
        if (user_type_selection_status == 1) {

            leaderBoardList = new ArrayList<LeaderboardListItem>();
            leaderBoardList.add(new LeaderboardListItem("LU5", "Ajinkya gaurkar", 99, "Math Group", "https://www.jetairways.com/Images/forms/group-booking.jpg"));
            leaderBoardList.add(new LeaderboardListItem("LU8", "Pranav Rana", 73, "Biology dept", "https://i.pinimg.com/originals/cf/70/ce/cf70ce32f1981d64ed82875772e33dfa.jpg"));
            leaderBoardList.add(new LeaderboardListItem("LU6", "Srinivas sandu", 66, "Strbx emp", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTqqhjnoMVUWJiIAnOOHwUPcxfB37rxI9xgBLmjhAsp3XYkXBOH"));
            leaderBoardList.add(new LeaderboardListItem("LU2", "Neha Reddy", 56, "Grad students", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4X84zn3LElb8LDfCXoqA9P3kpW4JFWTn64tzsHx64SJE-dgbB"));
            leaderBoardList.add(new LeaderboardListItem("LU9", "Rushabh Mehta", 52, "Hindu council", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQKoSN8vTpRB5egHRBingBCjtqjpvbOJ_9nM7MRrRWh5PLC7ECu"));
            leaderBoardList.add(new LeaderboardListItem("LU10", "Bala Kumaran", 45, "Chevy chase", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTybt8DR46L_OFmDPBAF3qAIBLtlcFDME8H4PEuXSkzSdjmSKQOXA"));
            leaderBoardList.add(new LeaderboardListItem("LU4", "Mayur Pate", 44, "Halethorpe", "https://icd.hwstatic.com/static/images/3.60.3.0/groups_article_three.jpg"));
            leaderBoardList.add(new LeaderboardListItem("LU1", "Arya Shah", 31, "Common vision", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQIC3Cc33fz59_cmyr90iwzRb7AnSDKqieV63Mgeq6NrSqsXl2m"));

            leaderBoardAdapter = new LeaderBoardAdapter(leaderBoardList, maxRank, user_type_selection_status);
            leader_board_recyclerview.setLayoutManager(mLayoutManager);
            leader_board_recyclerview.setItemAnimator(new DefaultItemAnimator());
            leader_board_recyclerview.setAdapter(leaderBoardAdapter);

        }
    }



}
