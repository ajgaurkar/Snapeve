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
import com.umbcapp.gaurk.snapeve.Controllers.SimilarPostsListItem;
import com.umbcapp.gaurk.snapeve.Listview_communicator;
import com.umbcapp.gaurk.snapeve.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SimilarPostsAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    List<SimilarPostsListItem> similarPostsList;
    String entryDate = null;
    int attendies_type = 0;

    public SimilarPostsAdapter(Context context, List<SimilarPostsListItem> similarPostsList) {

        this.similarPostsList = similarPostsList;
        this.context = context;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return similarPostsList.size();
    }

    @Override
    public SimilarPostsListItem getItem(int position) {
        return similarPostsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        SimilarPostsAdapter.ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new SimilarPostsAdapter.ViewHolder();
            view = this.inflater.inflate(R.layout.similar_post_list_item, parent, false);

            viewHolder.similar_post_list_item_user_pic_img_view = (CircleImageView) view.findViewById(R.id.similar_post_list_item_user_pic_img_view);

            view.setTag(viewHolder);

        } else {

            viewHolder = (SimilarPostsAdapter.ViewHolder) view.getTag();

        }

        SimilarPostsListItem selectedAttendiesListItem = similarPostsList.get(position);

       // Picasso.get().load(selectedAttendiesListItem.getImg_url())
         //       .fit().centerCrop().into(viewHolder.similar_post_list_item_user_pic_img_view);


        return view;
    }

    public class ViewHolder {

        private CircleImageView similar_post_list_item_user_pic_img_view;
        private TextView similar_post_list_item_title_textview;
        private TextView similar_post_list_item_dt_time_textview;
        private TextView similar_post_list_item_likes_count_textview;
        private TextView similar_post_list_item_user_name_text_view;


    }
}