package com.umbcapp.gaurk.snapeve;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.umbcapp.gaurk.snapeve.Adapters.CommentsAdapter;
import com.umbcapp.gaurk.snapeve.Adapters.TopScorerAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.LeaderboardListItem;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class ScheduledRewards extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ArrayList<LeaderboardListItem> scorerList;
    private ListView topScorerlistView;
    private CircleImageView scheduled_rewards_profile_pic_image_view;
    private TextView scheduled_rewards_user_fullname_name_textview;
    private TextView scheduled_rewards_user_name_textview;
    private RelativeLayout scheduled_rewards_main_layout;
    private KonfettiView konfettiView;
    private int width;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduled_rewards);

        topScorerlistView = (ListView) findViewById(R.id.scheduled_rewards_scorer_list);
        scheduled_rewards_profile_pic_image_view = (CircleImageView) findViewById(R.id.scheduled_rewards_profile_pic_image_view);
        scheduled_rewards_main_layout = (RelativeLayout) findViewById(R.id.scheduled_rewards_main_layout);
        scheduled_rewards_user_fullname_name_textview = (TextView) findViewById(R.id.scheduled_rewards_user_fullname_name_textview);
        scheduled_rewards_user_name_textview = (TextView) findViewById(R.id.scheduled_rewards_user_name_textview);
        konfettiView = findViewById(R.id.viewKonfetti);

        konfettiView.post(new Runnable() {
            @Override
            public void run() {
                width = konfettiView.getMeasuredWidth();
                getTopScorers();
                //method called in runnable because width is calculated after view load. hence width come to 0 if confetti called before/directly
            }
        });
        System.out.println("scheduled_rewards_main_layout width() " + width);


    }

    private void getTopScorers() {
        System.out.println("IN fetch_post_comments_api");
        progressDialog = new ProgressDialog(ScheduledRewards.this);
        progressDialog.setMessage("Fetching Details");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.create();

        JsonObject jsonObjectPostEventParameters = new JsonObject();

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("Fetch_weekly_winners_api", jsonObjectPostEventParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                Toast.makeText(getApplicationContext(), "Something went wrong. Try again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                System.out.println(" Fetch_weekly_winners_api exception    " + exception);

            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                System.out.println(" Fetch_weekly_winners_api success response    " + response);
                progressDialog.dismiss();
                parseTopScorerResponse(response);
            }
        });

        setConfetti();
    }

    //method called in runnable because width is calculated after view load. hence width come to 0 if confetti called before/directly
    private void setConfetti() {
        konfettiView.build()
                .addColors(Color.RED, Color.WHITE, Color.GREEN, Color.YELLOW)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(800L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(16, 5f))
                .setPosition(0f, (float) width, 0f, 0f)
                .streamFor(30, 50000L);

    }

    private void parseTopScorerResponse(JsonElement topScorerResponse) {

        scorerList = new ArrayList<LeaderboardListItem>();

        System.out.println(" IN PARSE JASON");
        JsonArray topScorerJSONArray = topScorerResponse.getAsJsonArray();

        for (int j = 0; j < topScorerJSONArray.size(); j++) {
            JsonObject topscorer_list_object = topScorerJSONArray.get(j).getAsJsonObject();
            System.out.println(" topscorer_list_object " + topscorer_list_object);

            String dp_url = null;
            String userId = topscorer_list_object.get("user_id").getAsString();
            String userName = topscorer_list_object.get("user_name").getAsString();
            String first_name = topscorer_list_object.get("first_name").getAsString();
            String last_name = topscorer_list_object.get("last_name").getAsString();
            String userGroup = "XX";
            int userRank = topscorer_list_object.get("user_points").getAsInt();

            try {
                dp_url = topscorer_list_object.get("dp_url").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (j == 0) {

                if (dp_url == null) {
                    scheduled_rewards_profile_pic_image_view.setImageResource(R.drawable.avatar_100_3);

                } else {
                    Picasso.get().load(dp_url).fit().centerCrop().into(scheduled_rewards_profile_pic_image_view);
                }
                scheduled_rewards_user_fullname_name_textview.setText(first_name + " " + last_name);
                scheduled_rewards_user_name_textview.setText(userName);

            } else {
                scorerList.add(new LeaderboardListItem(userId, userName, userRank, userGroup, dp_url));

            }

        }
        TopScorerAdapter topScorerAdapter = new TopScorerAdapter(getApplicationContext(), scorerList);
        topScorerlistView.setAdapter(topScorerAdapter);

    }
}
