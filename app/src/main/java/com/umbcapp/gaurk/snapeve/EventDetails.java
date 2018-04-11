package com.umbcapp.gaurk.snapeve;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.umbcapp.gaurk.snapeve.Adapters.CommentsAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.CommentsListItem;

import java.util.ArrayList;

public class EventDetails extends AppCompatActivity {

    ArrayList<CommentsListItem> commentsList = new ArrayList<CommentsListItem>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);


        ListView eventDetailsCommentsListView = (ListView) findViewById(R.id.event_detail_comments_list_view);
        commentsList.add(new CommentsListItem("u1", "Jhon Paul", "speaking to a a packed crowd at Sanders Theatre, a nobel laureate discussing her most recent scientific discovery, or the Harvard senior talent show, there’s always something happening at Harvard.", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
        commentsList.add(new CommentsListItem("u1", "Jhon Paul", "Good", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
        commentsList.add(new CommentsListItem("u1", "Jhon Paul", "Good", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
        commentsList.add(new CommentsListItem("u1", "Jhon Paul", "Whether you have meditated for a long time or have never meditated, come join us for this time of practice together. Come to relax, quiet your mind, learn how to experience less suffering and stress, explore Buddhist philosophy and psychology, just talk about what it means to live from compassion and awareness -- or come because you are curious. This group meets on Tuesdays from 5 - 6 p.m. in in Chapin Chapel, and is led by Mark Hart, Buddhist Advisor.", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
        commentsList.add(new CommentsListItem("u1", "Jhon Paul", "Good", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
        commentsList.add(new CommentsListItem("u1", "Jhon Paul", "Good", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));
        commentsList.add(new CommentsListItem("u1", "Jhon Paul", "Good", "Jan 05", "https://ais2017.umbc.edu/files/2017/09/umbc.jpg"));


        CommentsAdapter commentsAdapter = new CommentsAdapter(getApplicationContext(),commentsList);
        eventDetailsCommentsListView.setAdapter(commentsAdapter);

    }
}
