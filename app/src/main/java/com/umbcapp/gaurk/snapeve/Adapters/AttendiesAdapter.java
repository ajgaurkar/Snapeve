package com.umbcapp.gaurk.snapeve.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.umbcapp.gaurk.snapeve.Controllers.AttendiesListItem;
import com.umbcapp.gaurk.snapeve.Controllers.CommentsListItem;
import com.umbcapp.gaurk.snapeve.Listview_communicator;
import com.umbcapp.gaurk.snapeve.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AttendiesAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    List<AttendiesListItem> attendies_List;
    String entryDate = null;
    int attendies_type = 0;

    public AttendiesAdapter(Context context, List<AttendiesListItem> comments_list, int attendies_type) {

        this.attendies_List = comments_list;
        this.attendies_type = attendies_type;
        this.context = context;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return attendies_List.size();
    }

    @Override
    public AttendiesListItem getItem(int position) {
        return attendies_List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        AttendiesAdapter.ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new AttendiesAdapter.ViewHolder();
            view = this.inflater.inflate(R.layout.attendies_list_item, parent, false);

            viewHolder.attendies_list_item_user_pic_img_view = (CircleImageView) view.findViewById(R.id.attendies_list_item_user_pic_img_view);
            viewHolder.attendies_list_item_user_name_text_view = (TextView) view.findViewById(R.id.attendies_list_item_user_name_text_view);
//            viewHolder.attendies_list_item_request_status_img_view = (ImageView) view.findViewById(R.id.attendies_list_item_request_status_img_view);
            viewHolder.attendies_list_item_request_status_text_view = (TextView) view.findViewById(R.id.attendies_list_item_request_status_text_view);
            viewHolder.attendies_list_item_user_attending_status_text_view = (TextView) view.findViewById(R.id.attendies_list_item_user_attending_status_text_view);
            viewHolder.attendies_list_separator_view = (View) view.findViewById(R.id.attendies_list_separator_view);


            view.setTag(viewHolder);

//            Listview_communicator communicator;
//            communicator = ((Listview_communicator)context).main_event_listview_element_clicked();
//            communicator.main_event_listview_element_clicked();
//


        } else {

            viewHolder = (AttendiesAdapter.ViewHolder) view.getTag();

        }

        AttendiesListItem selectedAttendiesListItem = attendies_List.get(position);

        if (selectedAttendiesListItem.getImage_url() == null) {
            viewHolder.attendies_list_item_user_pic_img_view.setImageResource(R.drawable.avatar_100_3);
        } else {
            Picasso.get().load(selectedAttendiesListItem.getImage_url()).fit().centerCrop().into(viewHolder.attendies_list_item_user_pic_img_view);
        }

        viewHolder.attendies_list_item_user_name_text_view.setText(selectedAttendiesListItem.getUser_name());

        if (attendies_type == 1) {
//                    viewHolder.attendies_list_item_request_status_img_view.setVisibility(View.VISIBLE);
//            viewHolder.attendies_list_item_request_status_text_view.setText(selectedAttendiesListItem.getRequest_status());
            viewHolder.attendies_list_item_request_status_text_view.setVisibility(View.INVISIBLE);
        }
        if (attendies_type == 0) {
            if (selectedAttendiesListItem.getAttend_status() == 0) {
                viewHolder.attendies_list_item_user_attending_status_text_view.setText("Not responded");
                viewHolder.attendies_list_item_request_status_text_view.setVisibility(View.VISIBLE);
                if (selectedAttendiesListItem.getRequest_status() == 0) {
                    viewHolder.attendies_list_item_request_status_text_view.setText("Request to attend");
                    viewHolder.attendies_list_item_request_status_text_view.setTextColor(Color.parseColor("#469ac3"));
                    viewHolder.attendies_list_item_request_status_text_view.setBackground(context.getResources().getDrawable(R.drawable.blue_bg_five_corner_round));

                }
                if (selectedAttendiesListItem.getRequest_status() == 1) {
                    viewHolder.attendies_list_item_request_status_text_view.setText("Request sent");
                    viewHolder.attendies_list_item_request_status_text_view.setTextColor(Color.parseColor("#bbbbbb"));
                    viewHolder.attendies_list_item_request_status_text_view.setBackground(context.getResources().getDrawable(R.drawable.grey_bg_five_corner_round));
                }
            }
            if (selectedAttendiesListItem.getAttend_status() == 1) {
                viewHolder.attendies_list_item_user_attending_status_text_view.setText("Attending");
                viewHolder.attendies_list_item_request_status_text_view.setVisibility(View.INVISIBLE);
            }

        }

        viewHolder.attendies_list_item_request_status_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("position 2 " + position);
                ((Listview_communicator) context).main_event_listview_element_clicked(position, 0);
            }
        });

        return view;
    }

    public class ViewHolder {

        private ImageView attendies_list_item_user_pic_img_view;
        private TextView attendies_list_item_user_name_text_view;
        //        private ImageView attendies_list_item_request_status_img_view;
        private TextView attendies_list_item_request_status_text_view;
        private TextView attendies_list_item_user_attending_status_text_view;
        private View attendies_list_separator_view;


    }
}