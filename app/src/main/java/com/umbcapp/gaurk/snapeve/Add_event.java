package com.umbcapp.gaurk.snapeve;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.umbcapp.gaurk.snapeve.Adapters.SimilarPostsAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.SimilarPostsListItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import me.iwf.photopicker.PhotoPicker;

import static android.media.CamcorderProfile.get;
import static com.umbcapp.gaurk.snapeve.AzureConfiguration.getContainer;

public class Add_event extends AppCompatActivity implements LocationListener {
    private static final int CAMERA_REQUEST = 1888; // field
    private static final int MERGE_OPTION = 4;
    private ImageView selected_posting_image_view;
    final static int PERMISSION_CODE = 1;
    // final static String[] PERMISSIONS = {Manifest.permission.CAMERA};
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
    private RadioGroup post_as_radio_grp;
    private String postDescription;
    private String postDescriptionTimeDt;
    private TextView post_event_time_dt_text_view;
    private boolean all_day_status;
    private String eventLocation = "NULL";
    private int post_scope_radio_value;
    private int event_type_radio_value;
    private int location_type_radio_value;
    private int PICK_GALLERY_IMAGE = 1;
    private int PICK_LOCATION = 3;
    private ImageView galleryOpenImageView;
    LocationManager locationManager;
    private TextView add_event_card_3_textview;
    private Spinner pick_location_card_3_spinner;
    double selectedLat = 0.0;
    double selectedLng = 0.0;
    double currentLat = 0.0;
    double currentLng = 0.0;
    private DecimalFormat decimalFormat;
    private int timePickerFlag;
    private ProgressBar similarPostBar;
    private TextView submit_btn_status_textview;
    private TextView similarpost_count_textview;
    private ListView similar_posts_cardview_list_view;
    private SimilarPostsAdapter similarPostAdapter;
    private ArrayList<SimilarPostsListItem> similarPostsList;
    private String CHANNEL_ID = "chchchchc";
    private File mCameraImageFile;
    public Uri mCameraImageFileUri = null;
    static final int REQUEST_TAKE_PHOTO = 5;
    final static int PERMISSION_ALL = 1;
    final static String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECORD_AUDIO};
    public LocationManager mLocationManager;
    private MobileServiceClient mClient;
    private CloudBlobContainer container;
    private Map<String, Uri> mapForUploadingSelectedImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_2);

        initiate_permission_check();

        createNotificationChannel();
        setNotification();
        // find_similar_posts
        mClient = Singleton.Instance().mClientMethod(this);

        decimalFormat = new DecimalFormat("#.#######");
        decimalFormat.setRoundingMode(RoundingMode.CEILING);

        camera_gallery_selector_img_rel_layout = (RelativeLayout) findViewById(R.id.camera_gallery_selector_img_rel_layout);
        similar_posts_cardview_list_view = (ListView) findViewById(R.id.similar_posts_cardview_list_view);
        selected_posting_image_view = (ImageView) findViewById(R.id.selected_posting_image_view);
        cameraOpenImageView = (ImageView) findViewById(R.id.camera_gallery_selector_camera_img_view);
        galleryOpenImageView = (ImageView) findViewById(R.id.camera_gallery_selector_gallery_img_view);
        similar_posts_cardview_skip_text_view = (TextView) findViewById(R.id.similar_posts_cardview_skip_text_view);
        selected_posting_image_view = (ImageView) findViewById(R.id.selected_posting_image_view);
        select_image_trash_can_image_view = (ImageView) findViewById(R.id.select_image_trash_can_image_view);
        pick_location_card_3_spinner = (Spinner) findViewById(R.id.add_event_card_3_spinner);
        select_image_trash_can_image_view.setVisibility(View.GONE);

        all_day_switch = (Switch) findViewById(R.id.all_day_switch);
        post_event_description_text_view = (EditText) findViewById(R.id.post_event_description_text_view);
        post_event_time_dt_text_view = (TextView) findViewById(R.id.post_event_time_dt_text_view);
        add_event_card_3_textview = (TextView) findViewById(R.id.add_event_card_3_textview);
        submit_btn_status_textview = (TextView) findViewById(R.id.submit_btn_status_textview);
        similarpost_count_textview = (TextView) findViewById(R.id.similarpost_count_textview);
        post_scope_radio_grp = (RadioGroup) findViewById(R.id.post_scope_radio_grp);
        post_location_radio_grp = (RadioGroup) findViewById(R.id.post_location_radio_grp);
        post_event_type_radio_grp = (RadioGroup) findViewById(R.id.post_event_type_radio_grp);
        post_as_radio_grp = (RadioGroup) findViewById(R.id.post_as_radio_grp);

        similar_posts_list_cardview = (CardView) findViewById(R.id.similar_posts_list_cardview);
        submit_button_cardview = (CardView) findViewById(R.id.submit_button_cardview);
        add_event_card_1 = (CardView) findViewById(R.id.add_event_card_1);
        add_event_card_2 = (CardView) findViewById(R.id.add_event_card_2);
        add_event_card_3 = (CardView) findViewById(R.id.add_event_card_3);
        add_event_card_4 = (CardView) findViewById(R.id.add_event_card_4);
        similar_button_cardview = (CardView) findViewById(R.id.similar_button_cardview);
        add_event_card_5_rel_layout = (RelativeLayout) findViewById(R.id.add_event_card_5_rel_layout);

        pick_location_card_3_spinner.setVisibility(View.INVISIBLE);
        add_event_card_3_textview.setVisibility(View.VISIBLE);
        getCurrentLocation();

        similar_posts_list_cardview.setVisibility(View.GONE);
        fillLocationSpinner();

        all_day_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    post_event_time_dt_text_view.setText(new SimpleDateFormat("MMM dd, YYYY", Locale.getDefault()).format(new Date()));
                    post_event_time_dt_text_view.setText("May 10, 2018 12:30 PM - 02:30 PM");
                    post_event_time_dt_text_view.setError(null);
                    fetch_similar_events(2);

                } else {
                    post_event_time_dt_text_view.setText("");
                    post_event_time_dt_text_view.setError(null);
                }

            }
        });

        cameraOpenImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("cameraOpenImageView");
                takePicture();
            }
        });
        galleryOpenImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selcectFromGallery();


                System.out.println("galleryOpenImageView");
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

                //text data upload START
                //Lines commented temporarily for image uploading process
//                add_event_card_2.setVisibility(View.VISIBLE);
//                add_event_card_3.setVisibility(View.VISIBLE);
//                add_event_card_4.setVisibility(View.VISIBLE);
//                add_event_card_5_rel_layout.setVisibility(View.VISIBLE);
//                similar_posts_list_cardview.setVisibility(View.GONE);
//                post_event();
                //text data upload END

                //----------------------------------------------------------------------------//

                //temporary for image upload
                uploadEventImage();

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

        post_event_type_radio_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                System.out.println("checkedId : " + checkedId);

                switch (checkedId) {
                    case R.id.post_event_type_event_radio:
                        event_type_radio_value = 0;
                        System.out.println("Event");
                        break;
                    case R.id.post_event_type_incident_radio:
                        System.out.println("Incident");
                        event_type_radio_value = 1;
                        break;

                }
            }
        });

        post_location_radio_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                System.out.println("checkedId : " + checkedId);

                switch (checkedId) {

                    case R.id.post_location_current_radio:
                        System.out.println("Current");
                        location_type_radio_value = 0;
                        pick_location_card_3_spinner.setVisibility(View.INVISIBLE);
                        add_event_card_3_textview.setVisibility(View.VISIBLE);
                        getCurrentLocation();
                        break;
                    case R.id.post_location_picklist_radio:
                        System.out.println("Picklist");
                        location_type_radio_value = 2;
                        pick_location_card_3_spinner.setVisibility(View.VISIBLE);
                        add_event_card_3_textview.setVisibility(View.INVISIBLE);

                        break;
                    case R.id.post_location_pin_radio:
                        System.out.println("Pin");
                        location_type_radio_value = 1;
                        startActivityForResult(new Intent(getApplicationContext(), DropPinMapsActivity.class), PICK_LOCATION);
                        pick_location_card_3_spinner.setVisibility(View.INVISIBLE);
                        add_event_card_3_textview.setVisibility(View.VISIBLE);
                        break;

                }
            }
        });

        post_scope_radio_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                System.out.println("checkedId : " + checkedId);

                switch (checkedId) {

                    case R.id.post_scope_public_radio:
                        System.out.println("Public");
                        post_scope_radio_value = 0;

                        break;
                    case R.id.post_scope_anonymous_radio:
                        System.out.println("Anonymous");
                        post_scope_radio_value = 2;

                        break;
                    case R.id.post_scope_grponly_radio:
                        System.out.println("Grp only");
                        post_scope_radio_value = 1;

                        break;

                }
            }
        });
        post_event_time_dt_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerFlag = 0;
                selectDateTimeRange();
            }
        });
        pick_location_card_3_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fetch_similar_events(1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        similar_posts_cardview_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openMegrePostDialog(position);
            }
        });

//        submit_btn_status_textview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });

    }

    private void setNotification() {
        Intent intent = new Intent(this, NotificationReceiver.class);
        Intent LeaderBoardIntent = new Intent(this, Leaderboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        PendingIntent leaderboardpendingIntent = PendingIntent.getActivity(this, 0, LeaderBoardIntent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .addAction(R.drawable.approve_dark_grey_48, "Approve", leaderboardpendingIntent)
                .addAction(R.drawable.deny_dark_grey_48, "Snooze", pendingIntent)
                .addAction(R.drawable.comments_48_dark_grey, "Spam", leaderboardpendingIntent)
                .setSmallIcon(R.drawable.locations_24)
                .setContentTitle("New event")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.walking_48))
                .setContentText("Art exhibition in Erickson field")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Tonight exhibition is sponsored by OCSS"))
                .setContentIntent(pendingIntent)

                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "CH1";
            String description = "CH DES 1";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void openMegrePostDialog(int position) {


//        final Dialog alertDialog = new Dialog(this);
//        LayoutInflater flater = this.getLayoutInflater();
//        View view = flater.inflate(R.layout.similar_post_dialog, null);
//        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        alertDialog.setContentView(view);
//        alertDialog.setCancelable(true);
//        alertDialog.setCanceledOnTouchOutside(false);
//
//        TextView merge_event_merge_btn_text_view = (TextView) view.findViewById(R.id.merge_event_merge_btn_text_view);
//        TextView merge_event_cancel_btn_text_view = (TextView) view.findViewById(R.id.merge_event_cancel_btn_text_view);
//
//        merge_event_merge_btn_text_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                attendies_dialog_switch_group_textview.setBackground(getResources().getDrawable(R.drawable.text_selection_left_seleted));
////                attendies_dialog_switch_all_textview.setBackground(getResources().getDrawable(R.drawable.text_selection_right_unseleted));
////                attendies_type_selection_status = 0;
////                fetchAndPopulateAttendiesList(attendies_type_selection_status);
//            }
//        });
//        merge_event_cancel_btn_text_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                alertDialog.dismiss();
//
//            }
//        });

//        fetchAndPopulateAttendiesList(0);

//        alertDialog.show();

        /*------------------------------------------------*/
        SimilarPostsListItem selectedSimilarPst = similarPostsList.get(position);

        Intent eventDetailIntent = new Intent(getApplicationContext(), EventDetails.class);
        eventDetailIntent.putExtra("user_id", selectedSimilarPst.getInitializer_id());
        eventDetailIntent.putExtra("user_name", "Dummy name");
        eventDetailIntent.putExtra("img_url", selectedSimilarPst.getImg_url());
        eventDetailIntent.putExtra("comm_time", selectedSimilarPst.getPost_dt());
        eventDetailIntent.putExtra("user_comment", selectedSimilarPst.getImg_description());
        eventDetailIntent.putExtra("intent_type", 1);
        startActivityForResult(eventDetailIntent, MERGE_OPTION);


    }

    String temp;

    private void selectDateTimeRange() {

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        final TimePickerDialog mTimePicker;
        temp = "";
        mTimePicker = new TimePickerDialog(Add_event.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                System.out.println("temp 1 : " + selectedHour + " " + selectedMinute);

                if (timePickerFlag == 0) {
                    post_event_time_dt_text_view.setText(selectedHour + ":" + selectedMinute);
                    temp = temp + selectedHour + ":" + selectedMinute + " ";
                }
                if (timePickerFlag == 1) {
                    post_event_time_dt_text_view.setText(selectedHour + ":" + selectedMinute);
                    temp = temp + selectedHour + ":" + selectedMinute + " ";

                }

                if (timePickerFlag < 2) {
                    System.out.println("temp : " + temp);

                    selectDateTimeRange();
                }
                timePickerFlag++;

                post_event_time_dt_text_view.setText(temp);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
        mTimePicker.setCanceledOnTouchOutside(false);

    }

    private void fillLocationSpinner() {

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("[ Select a Location ]");
        categories.add("Breez way");
        categories.add("UMBC Commons");
        categories.add("Administration garage");
        categories.add("Bookstore");
        categories.add("Commons street");
        categories.add("Flat Tuesdays");
        categories.add("Gameroom");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pick_location_card_3_spinner.setAdapter(dataAdapter);
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {

        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } catch (Exception e) {

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("onLocationChanged :" + location);
        add_event_card_3_textview.setText("Current Location: " + location.getLatitude() + ", " + location.getLongitude());
        currentLng = location.getLongitude();
        currentLat = location.getLatitude();
        System.out.println("currentLng------------ " + currentLng);
        System.out.println("currentLat------------ " + currentLat);
    }

    @Override
    public void onProviderDisabled(String provider) {
        System.out.println("onProviderDisabled ");

        add_event_card_3_textview.setText("Please Enable GPS and Internet");

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    private void selcectFromGallery() {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(false)
                .setShowGif(true)
                .setPreviewEnabled(false)
                .start(Add_event.this, PICK_GALLERY_IMAGE);
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            System.out.println("takePictureIntent not Null");

            try {
                mCameraImageFile = createImageFile();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Created Camera Image --- " + mCameraImageFile);

            if (mCameraImageFile != null) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    // Do something for Nougat and above versions
                    System.out.println("Above Nougat ");
                    takePictureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    mCameraImageFileUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", mCameraImageFile);
                } else {
                    System.out.println("Below Nougat ");
                    // do something for phones running an SDK before Nougat
                    mCameraImageFileUri = Uri.fromFile(mCameraImageFile);
                }

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraImageFileUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }


        } else {
            System.out.println("takePictureIntent  Null");

        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = Environment.getExternalStorageDirectory();

        File createdImageFile = File.createTempFile(imageFileName,/* prefix */".jpg",/* suffix */ storageDir /* directory */);
        System.out.println("===createdImageFile---  " + createdImageFile);

        return createdImageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("vv IN onActivityResult");
        System.out.println("vv data : " + data);
        System.out.println("vv requestCode : " + requestCode);

        if (requestCode == PICK_GALLERY_IMAGE && resultCode == Activity.RESULT_OK) {

            System.out.println("Selected Gallery Image---");
            if (data != null) {
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                System.out.println("Photos-List Size-- " + photos.size());

                File imgFile = new File(photos.get(0));
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                selected_posting_image_view.setImageBitmap(myBitmap);

                /*Intialization of Map;
                Why this is here because we want to keep map size always ONE
                 */
                mapForUploadingSelectedImage = new HashMap<>();

                int index = photos.get(0).lastIndexOf('/');
                String selectedGalleryImageName = photos.get(0).substring(index + 1);

                String addFileString = "file://" + photos.get(0);
                Uri getUriToSendAzure = Uri.parse(addFileString);
                System.out.println("selectedImageName------- " + selectedGalleryImageName);
                System.out.println("getUriToSendAzure------- " + getUriToSendAzure);
                mapForUploadingSelectedImage.put(selectedGalleryImageName, getUriToSendAzure);
                System.out.println("Gallery : mapForUploadingSelectedImage---- Size  " + mapForUploadingSelectedImage.size());


            }

        }
        if (requestCode == PICK_LOCATION) {
            System.out.println("vv PICK_LOCATION");
            System.out.println("vv LAT : " + data.getStringExtra("Lat"));
            System.out.println("vv LNG : " + data.getStringExtra("Lng"));
        }

        if (requestCode == 4 && resultCode == Activity.RESULT_OK) {
            System.out.println("vv MERGE_OPTION-------------*");
            System.out.println("vv MERGE_OPTION : " + data.getStringExtra("Merge_action"));
            if (data.getStringExtra("Merge_action").equals("1")) {
                System.out.println("CALL MERGE ACTION");
            }
        }

        if (requestCode == 3) {

            System.out.println("vv PICK_LOCATION");
            System.out.println("vv LAT : " + data.getStringExtra("Lat"));
            System.out.println("vv LNG : " + data.getStringExtra("Lng"));

            selectedLat = Double.parseDouble(decimalFormat.format(Double.valueOf(data.getStringExtra("Lat"))));
            selectedLng = Double.parseDouble(decimalFormat.format(Double.valueOf(data.getStringExtra("Lng"))));

            add_event_card_3_textview.setText("Dropped pin at : " + selectedLat + "," + selectedLng);
        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap picture = (Bitmap) data.getExtras().get("data");//this is your bitmap image and now you can do whatever you want with this
            selected_posting_image_view.setImageBitmap(picture); //for example I put bmp in an ImageView
            camera_gallery_selector_img_rel_layout.setVisibility(View.GONE);
            select_image_trash_can_image_view.setVisibility(View.VISIBLE);

        }


        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {


            try {
                System.out.println("Check Photo----");
                String attachedFilePath = mCameraImageFile.getAbsolutePath();
                String attachedFilePath_From_SaveImage = CompressionOfImage(attachedFilePath);
                System.out.println("attachedFilePath_From_SaveImage----------- " + attachedFilePath_From_SaveImage);
                File imgFile = new File(attachedFilePath_From_SaveImage);
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                selected_posting_image_view.setImageBitmap(myBitmap);

                //delete original large File
                mCameraImageFile.delete();

                mapForUploadingSelectedImage = new HashMap<>();

                int index = attachedFilePath_From_SaveImage.lastIndexOf('/');
                String selectedCameraImageName = attachedFilePath_From_SaveImage.substring(index + 1);

                String addFileString = "file://" + attachedFilePath_From_SaveImage;
                Uri getUriToSendAzure = Uri.parse(addFileString);
                System.out.println("selectedImageName------- " + selectedCameraImageName);
                System.out.println("getUriToSendAzure------- " + getUriToSendAzure);
                mapForUploadingSelectedImage.put(selectedCameraImageName, getUriToSendAzure);
                System.out.println("Camera : mapForUploadingSelectedImage---- Size  " + mapForUploadingSelectedImage.size());


            } catch (Exception e) {
                System.out.println("image not capture from camera " + e.getMessage());
            }

            camera_gallery_selector_img_rel_layout.setVisibility(View.GONE);
            select_image_trash_can_image_view.setVisibility(View.VISIBLE);
        }


    }

    private void initiate_permission_check() {
//Permission check to set switch status
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionGranted()) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
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
            ListenableFuture<JsonElement> serviceFilterFuture = mClient.invokeApi("post_event_api", jsonObjectPostEventParameters);

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

    private void fetch_similar_events(int search_code) {
        jsonObjectPostEventParameters = new JsonObject();

        if (search_code == 0) {
            jsonObjectPostEventParameters.addProperty("search_code", search_code);
        }

        // 1 : spinner location change similar post check
        if (search_code == 1) {
            eventLocation = pick_location_card_3_spinner.getSelectedItem().toString();
            jsonObjectPostEventParameters.addProperty("location_name", eventLocation);
            jsonObjectPostEventParameters.addProperty("search_code", search_code);
        }
        // 2 : all day switch checked true
        if (search_code == 2) {
            eventLocation = pick_location_card_3_spinner.getSelectedItem().toString();
            jsonObjectPostEventParameters.addProperty("location_name", eventLocation);
            jsonObjectPostEventParameters.addProperty("search_code", search_code);
        }

        similarpost_count_textview.setVisibility(View.INVISIBLE);

        similarPostBar = (ProgressBar) findViewById(R.id.submit_btn_status_progressbar);
        similarPostBar.setVisibility(View.VISIBLE);
        similar_button_cardview.setVisibility(View.GONE);
        submit_btn_status_textview.setText("Searching similar events");

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("find_similar_posts", jsonObjectPostEventParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                System.out.println(" fetch_similar_events exception    " + exception);
                similarPostBar.setVisibility(View.INVISIBLE);
                submit_btn_status_textview.setText("Submit Post");
                similar_button_cardview.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                System.out.println(" fetch_similar_events success response    " + response);
                similarPostBar.setVisibility(View.INVISIBLE);
                submit_btn_status_textview.setText("Submit Post");
                similar_button_cardview.setVisibility(View.VISIBLE);

                parseSimilarPostRespone(response);

            }
        });
    }

    private void parseSimilarPostRespone(JsonElement response) {
        JsonArray feedsJsonArray = response.getAsJsonArray();
        System.out.println("feedsJsonArray.size() : " + feedsJsonArray.size());

        similarPostsList = new ArrayList<SimilarPostsListItem>();
        similarPostsList.add(new SimilarPostsListItem(null, null, null, null, null, null, null, 0, 0, 0));
        similarPostsList.add(new SimilarPostsListItem(null, null, null, null, null, null, null, 0, 0, 0));
        similarPostsList.add(new SimilarPostsListItem(null, null, null, null, null, null, null, 0, 0, 0));
        similarPostsList.add(new SimilarPostsListItem(null, null, null, null, null, null, null, 0, 0, 0));

        if (feedsJsonArray.size() > 0) {
            similarpost_count_textview.setText("" + feedsJsonArray.size());
            similarpost_count_textview.setVisibility(View.VISIBLE);

            for (int j = 0; j < feedsJsonArray.size(); j++) {

            }
            similarPostAdapter = new SimilarPostsAdapter(getApplicationContext(), similarPostsList);
            similar_posts_cardview_list_view.setAdapter(similarPostAdapter);
        } else {
            similar_button_cardview.setVisibility(View.GONE);
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

        eventLocation = pick_location_card_3_spinner.getSelectedItem().toString();

        System.out.println("postDescription : " + postDescription);
        System.out.println("postDescriptionTimeDt : " + postDescriptionTimeDt);
        System.out.println("post_scope_radio_value : " + post_scope_radio_value);
        System.out.println("event_type_radio_value : " + event_type_radio_value);
        System.out.println("location_type_radio_value : " + location_type_radio_value);
        System.out.println("all_day_status : " + all_day_status);

        jsonObjectPostEventParameters.addProperty("user_id", new SessionManager(getApplicationContext()).getSpecificUserDetail(SessionManager.KEY_USER_ID));
        jsonObjectPostEventParameters.addProperty("location_type", location_type_radio_value);
        jsonObjectPostEventParameters.addProperty("event_type", event_type_radio_value);
        jsonObjectPostEventParameters.addProperty("scope", post_scope_radio_value);
        jsonObjectPostEventParameters.addProperty("description", postDescription);
        jsonObjectPostEventParameters.addProperty("all_day", all_day_status);
        jsonObjectPostEventParameters.addProperty("location_name", eventLocation);
        jsonObjectPostEventParameters.addProperty("lattitude", selectedLat);
        jsonObjectPostEventParameters.addProperty("longitude", selectedLng);

        Random random = new Random();
        int x = random.nextInt(900) + 100;

        jsonObjectPostEventParameters.addProperty("img_url", "https://picsum.photos/400/200/?image=" + x);
//        jsonObjectPostEventParameters.addProperty("img_url", "https://source.unsplash.com/random");

        return true;

    }

    public String CompressionOfImage(String selectedImagePath) {
        //************************compress logic start****************
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 500;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE) {
            scale *= 2;
            System.out.println("Scaling start@@@@@@@@@@@");
        }
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        System.out.println("selectedImagePath" + selectedImagePath);
        Bitmap bm = BitmapFactory.decodeFile(selectedImagePath, options);
        System.out.println("selectedImagefile bitmap" + bm);

        return SaveImage(bm);

        //************************compress logic end****************
    }

    private String SaveImage(Bitmap finalBitmap) {
        String filePath = null;

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + getString(R.string.folderName) + "/Event");
        System.out.println("myDir" + myDir);
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String imageFileName = getString(R.string.folderName) + "-Snapeve-" + n + ".jpg";
        File file = new File(myDir, imageFileName);
        System.out.println("myDir : " + file);
        System.out.println("_File_Name : " + imageFileName);

        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            System.out.println("out" + out);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            Uri imagePath = Uri.fromFile(file);
            filePath = imagePath.getPath();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

    private void uploadEventImage() {

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {

                    for (Map.Entry<String, Uri> entry : mapForUploadingSelectedImage.entrySet()) {
                        System.out.println("UploadinG Image------------------");

                        final InputStream imageStream = getContentResolver().openInputStream(entry.getValue());
                        final int imageLength = imageStream.available();
                        container = getContainer();
                        CloudBlockBlob imageBlob = container.getBlockBlobReference(entry.getKey());
                        imageBlob.upload(imageStream, imageLength);
                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                } catch (final Exception e) {
                    System.out.println("Image uploading Execption--- " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                //condition to check, if there is some problem uploading attachments
                System.out.println("result " + result);

            }
        };

        runAsyncTask(task);
    }

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Log.d("AsyncTask", "if..calling");
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            Log.d("AsyncTask", "else..calling");
            return task.execute();
        }
    }
}
