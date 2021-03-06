package com.umbcapp.gaurk.snapeve.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.umbcapp.gaurk.snapeve.Controllers.CreateGroupListItem;
import com.umbcapp.gaurk.snapeve.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ManageGroupAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    List<CreateGroupListItem> create_grp_List;
    String entryDate = null;
    int attendies_type = 0;

    /*
    STATUS 0 = Already joined
    STATUS 1 = Pending approval
    STATUS 2 = Request Sent
    STATUS 3 = Not a member
    */

    public ManageGroupAdapter(Context context, List<CreateGroupListItem> comments_list) {

        this.create_grp_List = comments_list;
        this.attendies_type = attendies_type;
        this.context = context;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return create_grp_List.size();
    }

    @Override
    public CreateGroupListItem getItem(int position) {
        return create_grp_List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        ManageGroupAdapter.ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ManageGroupAdapter.ViewHolder();
            view = this.inflater.inflate(R.layout.create_grp_list_item, parent, false);

            viewHolder.create_grp_list_item_user_pic_img_view = (CircleImageView) view.findViewById(R.id.attendies_list_item_user_pic_img_view);
            viewHolder.create_grp_list_item_user_name_text_view = (TextView) view.findViewById(R.id.attendies_list_item_user_name_text_view);
            viewHolder.create_grp_list_item_request_status_text_view = (TextView) view.findViewById(R.id.attendies_list_item_request_status_text_view);
            viewHolder.create_grp_list_item_user_email_text_view = (TextView) view.findViewById(R.id.attendies_list_item_user_attending_status_text_view);

            view.setTag(viewHolder);

        } else {

            viewHolder = (ManageGroupAdapter.ViewHolder) view.getTag();

        }

        CreateGroupListItem selectedCreateGroupListItem = create_grp_List.get(position);

        if (selectedCreateGroupListItem.getUserPicUrl() == null) {
            viewHolder.create_grp_list_item_user_pic_img_view.setImageResource(R.drawable.avatar_100_3);
        } else {
            Picasso.with(context).load(selectedCreateGroupListItem.getUserPicUrl())
                    .fit().centerCrop().into(viewHolder.create_grp_list_item_user_pic_img_view);
        }

        if (selectedCreateGroupListItem.getGrpAdminFlag() == 1) {
            viewHolder.create_grp_list_item_user_name_text_view.setText(selectedCreateGroupListItem.getUserName() + " (Admin)");
        } else {
            viewHolder.create_grp_list_item_user_name_text_view.setText(selectedCreateGroupListItem.getUserName());
        }
        viewHolder.create_grp_list_item_user_email_text_view.setText(selectedCreateGroupListItem.getUserFirstName() + " " + selectedCreateGroupListItem.getUserLastName());

//        switch (selectedCreateGroupListItem.getUserReqStatus()) {
            /*STATUS 0 = Already joined
            STATUS 1 = Pending approval
            STATUS 2 = Request Sent
            STATUS 3 = Not a member*/

        viewHolder.create_grp_list_item_request_status_text_view.setText(" i ");


//            case 0:
//                viewHolder.create_grp_list_item_request_status_text_view.setText("More");
//                break;
//            case 1:
//                viewHolder.create_grp_list_item_request_status_text_view.setText("Approve");
//                break;
//            case 2:
//                viewHolder.create_grp_list_item_request_status_text_view.setText("Request sent");
//                break;
//            case 3:
//                viewHolder.create_grp_list_item_request_status_text_view.setText("Send request");
//                break;
//
//        }

//        viewHolder.create_grp_list_item_request_status_text_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.print("position 2 " + position);
//                ((Listview_communicator) context).main_event_listview_element_clicked(position, 0);
//            }
//        });

        return view;
    }

    public class ViewHolder {

        public CircleImageView create_grp_list_item_user_pic_img_view;
        public TextView create_grp_list_item_user_name_text_view;
        public TextView create_grp_list_item_request_status_text_view;
        public TextView create_grp_list_item_user_email_text_view;

    }
}