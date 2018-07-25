package com.umbcapp.gaurk.snapeve;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.umbcapp.gaurk.snapeve.Adapters.LeaderBoardAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.LeaderboardListItem;

import java.util.ArrayList;

public class BrowseUserProfile extends AppCompatActivity {

    private TextView user_name_textview;
    private TextView user_points_textview;
    private RelativeLayout profile_relative_layout;
    private ListView user_profile_contribution_list_view;
    private TextView user_grp_name_text_view;
    private ImageView profile_pic_image_view;
    private TextView user_full_name_textview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.browser_user_profile);

        Intent userId = getIntent();
        String user_id = userId.getStringExtra("user_id");
        System.out.println("BrowseUserProfile user_id : " + user_id);


        user_name_textview = (TextView) findViewById(R.id.browse_profile_user_name);
        user_full_name_textview = (TextView) findViewById(R.id.user_full_name);
        user_points_textview = (TextView) findViewById(R.id.user_points);
        profile_relative_layout = (RelativeLayout) findViewById(R.id.profile_relative_layout);
        user_profile_contribution_list_view = (ListView) findViewById(R.id.user_profile_contribution_list_view);
        user_grp_name_text_view = (TextView) findViewById(R.id.grp_name_text_view);
        profile_pic_image_view = (ImageView) findViewById(R.id.profile_pic_image_view);


        fetchUserProfile(user_id);

    }

    private void fetchUserProfile(String user_id) {
        final ProgressDialog progressDialog = new ProgressDialog(BrowseUserProfile.this);
        progressDialog.setTitle("Loading, Please wait...");
        progressDialog.create();
        progressDialog.show();
        JsonObject jsonObjectParameters = new JsonObject();
        jsonObjectParameters.addProperty("user_id", user_id);

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("browse_user_profile_api", jsonObjectParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                progressDialog.dismiss();
                System.out.println(" browse_user_profile_api exception    " + exception);

            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                progressDialog.dismiss();

                System.out.println(" browse_user_profile_api success response    " + response);
                parseUserDataResponse(response);

            }
        });
    }

    private void parseUserDataResponse(JsonElement response) {
        System.out.println(" IN PARSE JASON");
        String dp_url = null;
        JsonArray userDataJSONArray = response.getAsJsonArray();

        JsonObject userData_list_object = userDataJSONArray.get(0).getAsJsonObject();


        System.out.println(" userData_list_object  " + userData_list_object);

        String grp_id = userData_list_object.get("grp_id").toString();
        String user_name = userData_list_object.get("user_name").toString();
        String grp_name = userData_list_object.get("grp_name").toString();
        String first_name = userData_list_object.get("first_name").toString();
        String last_name = userData_list_object.get("last_name").toString();
        int user_points = Integer.parseInt(userData_list_object.get("user_points").toString());

        //exception handling for image url null for user
        try {

            dp_url = userData_list_object.get("dp_url").getAsString();
            System.out.println(" dp_url found" + dp_url);
            Picasso.get().load(dp_url).fit().centerCrop().into(profile_pic_image_view);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" dp_url is null, set local image");
            profile_pic_image_view.setImageResource(R.drawable.avatar_100_1);
        }
//        String grp_dp_url = userData_list_object.get("grp_dp_url").getAsString();


        System.out.println(" user_name " + user_name);
        System.out.println(" user_points " + user_points);

        //Remove " from start and end from every string
        grp_id = grp_id.substring(1, grp_id.length() - 1);
        user_name = user_name.substring(1, user_name.length() - 1);
        first_name = first_name.substring(1, first_name.length() - 1);
        last_name = last_name.substring(1, last_name.length() - 1);
        grp_name = grp_name.substring(1, grp_name.length() - 1);

        user_name_textview.setText(user_name);
        user_full_name_textview.setText(first_name + " " + last_name);
        user_points_textview.setText(String.valueOf(user_points));
        user_grp_name_text_view.setText("Group : " + grp_name);
        profile_pic_image_view = (ImageView) findViewById(R.id.profile_pic_image_view);

    }

}
