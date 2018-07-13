package com.umbcapp.gaurk.snapeve;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.umbcapp.gaurk.snapeve.Adapters.MessagesThreadAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.MessagesThreadListItem;

import java.util.ArrayList;

public class MessageThread extends AppCompatActivity {
    ArrayList<MessagesThreadListItem> messageThreadList = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_thread_activity);

        ListView message_thread_main_listview = (ListView) findViewById(R.id.message_thread_main_listview);

        messageThreadList = new ArrayList<>();
        messageThreadList.add(new MessagesThreadListItem("Hello How are you", 0, 1313131));
        messageThreadList.add(new MessagesThreadListItem("My name is Aj", 0, 1313131));
        messageThreadList.add(new MessagesThreadListItem("Hello How are you", 0, 1313131));
        messageThreadList.add(new MessagesThreadListItem("Hello How are you", 0, 1313131));

        MessagesThreadAdapter messagesThreadAdapter = new MessagesThreadAdapter(getApplicationContext(), messageThreadList);
        message_thread_main_listview.setAdapter(messagesThreadAdapter);

    }
}
