package com.umbcapp.gaurk.snapeve.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.umbcapp.gaurk.snapeve.Controllers.CommentsListItem;
import com.umbcapp.gaurk.snapeve.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    List<CommentsListItem> comments_list;
    String entryDate = null;

    public CommentsAdapter(Context context, List<CommentsListItem> comments_list) {

        this.comments_list = comments_list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return comments_list.size();
    }

    @Override
    public CommentsListItem getItem(int position) {
        return comments_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        CommentsAdapter.ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new CommentsAdapter.ViewHolder();
            view = this.inflater.inflate(R.layout.comments_list_item, parent, false);

            viewHolder.comments_list_item_user_pic_img_view = (CircleImageView) view.findViewById(R.id.comments_list_item_user_pic_img_view);
            viewHolder.comments_list_item_user_name_text_view = (TextView) view.findViewById(R.id.comments_list_item_src_user_name_text_view);
            viewHolder.comments_list_item_user_time_text_view = (TextView) view.findViewById(R.id.comments_list_item_user_time_text_view);
            viewHolder.comments_list_item_user_comment_text_view = (TextView) view.findViewById(R.id.comments_list_item_user_comment_text_view);
            viewHolder.comments_list_item_trgt_user_name_text_view = (TextView) view.findViewById(R.id.comments_list_item_trgt_user_name_text_view);

            view.setTag(viewHolder);

        } else {

            viewHolder = (CommentsAdapter.ViewHolder) view.getTag();

        }

        CommentsListItem commentsListItem = comments_list.get(position);

        Picasso.get().load(commentsListItem.getImage_url())
                .fit().centerCrop().into(viewHolder.comments_list_item_user_pic_img_view);
        viewHolder.comments_list_item_user_name_text_view.setText(commentsListItem.getSrc_user_name());
        viewHolder.comments_list_item_user_time_text_view.setText(commentsListItem.getComment_time());
        viewHolder.comments_list_item_user_comment_text_view.setText(commentsListItem.getUser_comment());
        if (commentsListItem.getTrgt_user_name() != null) {
            viewHolder.comments_list_item_trgt_user_name_text_view.setText(">>"+commentsListItem.getTrgt_user_name());
        } else {
            viewHolder.comments_list_item_trgt_user_name_text_view.setText("");
        }
        //setting type face dynamically. Issues in setting through XML
//        viewHolder.calendarTitleTextView.setText(event_dash_list_obj.getTitle());


        return view;
    }

    public class ViewHolder {

        private ImageView comments_list_item_user_pic_img_view;
        private TextView comments_list_item_user_name_text_view;
        private TextView comments_list_item_user_time_text_view;
        private TextView comments_list_item_user_comment_text_view;
        private TextView comments_list_item_trgt_user_name_text_view;


    }
}