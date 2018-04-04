package com.umbcapp.gaurk.snapeve.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.umbcapp.gaurk.snapeve.Controllers.Event_dash_list_obj;
import com.umbcapp.gaurk.snapeve.Listview_communicator;
import com.umbcapp.gaurk.snapeve.MainActivity;
import com.umbcapp.gaurk.snapeve.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Dash_Event_ListAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    List<Event_dash_list_obj> event_dash_list;
    String entryDate = null;

    public Dash_Event_ListAdapter(Context context, List<Event_dash_list_obj> event_dash_list) {

        this.event_dash_list = event_dash_list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return event_dash_list.size();
    }

    @Override
    public Event_dash_list_obj getItem(int position) {
        return event_dash_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = this.inflater.inflate(R.layout.dash_list_view_item, parent, false);

            viewHolder.calendarTitleTextView = (TextView) view.findViewById(R.id.textetx);
            viewHolder.imageview = (ImageView) view.findViewById(R.id.cardimgview);

            view.setTag(viewHolder);

//            Listview_communicator communicator;
//            communicator = ((Listview_communicator)context).listview_element();
//            communicator.listview_element();
//


        } else {

            viewHolder = (ViewHolder) view.getTag();

        }

        Event_dash_list_obj event_dash_list_obj = event_dash_list.get(position);


        //setting type face dynamically. Issues in setting through XML
//        viewHolder.calendarTitleTextView.setText(event_dash_list_obj.getTitle());


        Picasso.get().load(event_dash_list_obj.getTitle())
                .fit().centerCrop().into(viewHolder.imageview);


        viewHolder.calendarTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("position 1 " + position);
                Toast.makeText(context, "1 " + position, Toast.LENGTH_SHORT).show();

                ((Listview_communicator) context).listview_element(position, 1);
            }
        });
        viewHolder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("position 2 " + position);
                Toast.makeText(context, "2 " + position, Toast.LENGTH_SHORT).show();

                ((Listview_communicator) context).listview_element(position, 2);
            }
        });

        return view;
    }

    public class ViewHolder {
        private TextView calendarTitleTextView;

        public ImageView imageview;
    }
}