package com.umbcapp.gaurk.snapeve;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Add_event extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888; // field
    private ImageView selected_posting_image_view;

    final static int PERMISSION_CODE = 1;
    final static String[] PERMISSIONS = {Manifest.permission.CAMERA};
    private RelativeLayout camera_gallery_selector_img_rel_layout;
    private ImageView select_image_trash_can_image_view;
    private ImageView cameraOpenImageView;
    private TextView similar_posts_cardview_skip_text_view;
    private CardView similar_posts_list_cardview;
    private CardView add_event_card_1;
    private CardView add_event_card_2;
    private CardView add_event_card_3;
    private CardView add_event_card_4;
    private CardView similar_button_cardview;
    private RelativeLayout add_event_card_5_rel_layout;
    private CardView submit_button_cardview;
    private JsonObject jsonObjectPostEventParameters;
    private Switch all_day_switch;
    private EditText post_event_description_text_view;
    private RadioGroup post_scope_radio_grp;
    private RadioGroup post_location_radio_grp;
    private RadioGroup post_event_type_radio_grp;
    private String postDescription;
    private String postDescriptionTimeDt;
    private TextView post_event_time_dt_text_view;
    private boolean all_day_status;
    private int post_scope_radio_value;
    private int event_type_radio_value;
    private int location_type_radio_value;
    private int PICK_IMAGE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_event);
        initiate_permission_check();

        camera_gallery_selector_img_rel_layout = (RelativeLayout) findViewById(R.id.camera_gallery_selector_img_rel_layout);
        selected_posting_image_view = (ImageView) findViewById(R.id.selected_posting_image_view);
        cameraOpenImageView = (ImageView) findViewById(R.id.camera_gallery_selector_camera_img_view);
        similar_posts_cardview_skip_text_view = (TextView) findViewById(R.id.similar_posts_cardview_skip_text_view);
        selected_posting_image_view = (ImageView) findViewById(R.id.selected_posting_image_view);
        select_image_trash_can_image_view = (ImageView) findViewById(R.id.select_image_trash_can_image_view);
        select_image_trash_can_image_view.setVisibility(View.GONE);


        all_day_switch = (Switch) findViewById(R.id.all_day_switch);
        post_event_description_text_view = (EditText) findViewById(R.id.post_event_description_text_view);
        post_event_time_dt_text_view = (TextView) findViewById(R.id.post_event_time_dt_text_view);
        post_scope_radio_grp = (RadioGroup) findViewById(R.id.post_scope_radio_grp);
        post_location_radio_grp = (RadioGroup) findViewById(R.id.post_location_radio_grp);
        post_event_type_radio_grp = (RadioGroup) findViewById(R.id.post_event_type_radio_grp);

        similar_posts_list_cardview = (CardView) findViewById(R.id.similar_posts_list_cardview);
        submit_button_cardview = (CardView) findViewById(R.id.submit_button_cardview);
        add_event_card_1 = (CardView) findViewById(R.id.add_event_card_1);
        add_event_card_2 = (CardView) findViewById(R.id.add_event_card_2);
        add_event_card_3 = (CardView) findViewById(R.id.add_event_card_3);
        add_event_card_4 = (CardView) findViewById(R.id.add_event_card_4);
        similar_button_cardview = (CardView) findViewById(R.id.similar_button_cardview);
        add_event_card_5_rel_layout = (RelativeLayout) findViewById(R.id.add_event_card_5_rel_layout);

        similar_posts_list_cardview.setVisibility(View.GONE);

        all_day_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    post_event_time_dt_text_view.setText(new SimpleDateFormat("MMM dd, YYYY", Locale.getDefault()).format(new Date()));
                    post_event_time_dt_text_view.setError(null);
                } else {
                    post_event_time_dt_text_view.setText("");
                    post_event_time_dt_text_view.setError(null);
                }
            }
        });

        cameraOpenImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        selected_posting_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selcectFromGallery();
            }
        });
        select_image_trash_can_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selected_posting_image_view.setImageBitmap(null); //for example I put bmp in an ImageView
                camera_gallery_selector_img_rel_layout.setVisibility(View.VISIBLE);
                select_image_trash_can_image_view.setVisibility(View.GONE);

            }
        });
        similar_posts_cardview_skip_text_view.setOnClickListener(new View.OnClickListener() {
            //        similar_posts_list_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                add_event_card_2.setVisibility(View.VISIBLE);
                add_event_card_3.setVisibility(View.VISIBLE);
                add_event_card_4.setVisibility(View.VISIBLE);
                add_event_card_5_rel_layout.setVisibility(View.VISIBLE);
                similar_posts_list_cardview.setVisibility(View.GONE);
            }
        });
        submit_button_cardview.setOnClickListener(new View.OnClickListener() {
            //        similar_posts_list_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                add_event_card_2.setVisibility(View.VISIBLE);
                add_event_card_3.setVisibility(View.VISIBLE);
                add_event_card_4.setVisibility(View.VISIBLE);
                add_event_card_5_rel_layout.setVisibility(View.VISIBLE);
                similar_posts_list_cardview.setVisibility(View.GONE);
                post_event();
            }
        });
        similar_button_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_event_card_2.setVisibility(View.INVISIBLE);
                add_event_card_3.setVisibility(View.INVISIBLE);
                add_event_card_4.setVisibility(View.INVISIBLE);
                add_event_card_5_rel_layout.setVisibility(View.INVISIBLE);
                similar_posts_list_cardview.setVisibility(View.VISIBLE);

            }
        });
    }

    private void selcectFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    private void takePicture() { //you can call this every 5 seconds using a timer or whenever you want

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {

        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap picture = (Bitmap) data.getExtras().get("data");//this is your bitmap image and now you can do whatever you want with this
//            picture.compress(Bitmap.CompressFormat.JPEG,100,)
            selected_posting_image_view.setImageBitmap(picture); //for example I put bmp in an ImageView
            camera_gallery_selector_img_rel_layout.setVisibility(View.GONE);
            select_image_trash_can_image_view.setVisibility(View.VISIBLE);
        }
    }


    private void initiate_permission_check() {
//Permission check to set switch status
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionGranted()) {
        } else {
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean isPermissionGranted() {
        if (this.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

            return true;
        } else {
            return false;
        }
    }

    private void post_event() {
        jsonObjectPostEventParameters = new JsonObject();

        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Posting. Please wait...");

        if (fetchUiParams()) {

            mProgressDialog.show();

            final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
            ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("post_event_api", jsonObjectPostEventParameters);

            Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
                @Override
                public void onFailure(Throwable exception) {
                    resultFuture.setException(exception);
                    System.out.println(" post_event exception    " + exception);
                    mProgressDialog.dismiss();
                }

                @Override
                public void onSuccess(JsonElement response) {
                    resultFuture.set(response);
                    System.out.println(" post_event success response    " + response);
                    mProgressDialog.dismiss();

                }
            });
        }
    }

    private boolean fetchUiParams() {


        if (!post_event_description_text_view.getText().toString().trim().equals("")) {
            postDescription = post_event_description_text_view.getText().toString().trim();
        } else {
            post_event_description_text_view.setError("Input required");
            return false;
        }

        if (all_day_switch.isChecked()) {
            all_day_status = true;
        } else {
            all_day_status = false;
            if (!post_event_time_dt_text_view.getText().toString().trim().equals("")) {
                postDescriptionTimeDt = post_event_time_dt_text_view.getText().toString().trim();
            } else {
                post_event_time_dt_text_view.setError("Input required");
                return false;
            }
        }

        post_scope_radio_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.post_scope_public_radio:
                        post_scope_radio_value = 0;
                        break;
                    case R.id.post_scope_grponly_radio:
                        post_scope_radio_value = 1;
                        break;
                    case R.id.post_scope_anonymous_radio:
                        post_scope_radio_value = 2;
                        break;
                }
            }
        });
        post_event_type_radio_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.post_event_type_event_radio:
                        event_type_radio_value = 0;
                        break;
                    case R.id.post_event_type_incident_radio:
                        event_type_radio_value = 1;
                        break;
                }
            }
        });
        post_location_radio_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.post_location_current_radio:
                        location_type_radio_value = 0;
                        break;
                    case R.id.post_location_pin_radio:
                        location_type_radio_value = 1;
                        break;
                    case R.id.post_location_picklist_radio:
                        location_type_radio_value = 2;
                        break;
                }
            }
        });


        System.out.println("postDescription : " + postDescription);
        System.out.println("postDescriptionTimeDt : " + postDescriptionTimeDt);
        System.out.println("post_scope_radio_value : " + post_scope_radio_value);
        System.out.println("event_type_radio_value : " + event_type_radio_value);
        System.out.println("location_type_radio_value : " + location_type_radio_value);
        System.out.println("all_day_status : " + all_day_status);

        jsonObjectPostEventParameters.addProperty("user_id", "check123");
        jsonObjectPostEventParameters.addProperty("location_type", location_type_radio_value);
        jsonObjectPostEventParameters.addProperty("event_type", event_type_radio_value);
        jsonObjectPostEventParameters.addProperty("scope", post_scope_radio_value);
        jsonObjectPostEventParameters.addProperty("description", postDescription);
        jsonObjectPostEventParameters.addProperty("all_day", all_day_status);
        jsonObjectPostEventParameters.addProperty("location_name", "Breezway");

        return true;

    }


}
