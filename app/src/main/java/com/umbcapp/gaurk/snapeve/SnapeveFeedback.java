package com.umbcapp.gaurk.snapeve;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.AsynchronousByteChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import me.iwf.photopicker.PhotoPicker;

import static com.umbcapp.gaurk.snapeve.AzureConfiguration.getContainer;

/**
 * Created by Amey on 04-08-2018.
 */

public class SnapeveFeedback extends AppCompatActivity {
    private static final int PICK_GALLERY_IMAGE = 1;
    private EditText feedback_title;
    private Spinner feedback_type;
    private EditText feedback_description;
    private CardView feedback_submit_button;
    private ImageView feedback_attachment_imageview;
    private TextView feedback_attachment_textview;
    private List<String> feedback_type_list;
    private ArrayAdapter<String> feedback_list_adpter;
    private MobileServiceTable<feedback_table> mFeedbackTable;
    private MobileServiceClient mClient;
    private HashMap<String, Uri> mapForUploadingFeedbackAttachment;
    private feedback_table feedback_table;
    private String feedbackSubmittedByUserName;
    private String feedbackposttedDate;
    private CloudBlobContainer container;
    private String selectedFeedbackAttachmentName;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snapeve_feedback);
        feedback_title = (EditText) findViewById(R.id.feedback_title_editext);
        feedback_type = (Spinner) findViewById(R.id.feedback_type_spinner);
        feedback_description = (EditText) findViewById(R.id.feedback_description_edittext);
        feedback_submit_button = (CardView) findViewById(R.id.feedback_submit_button_cardview);
        feedback_attachment_imageview = (ImageView) findViewById(R.id.feedback_attachment_imageview);
        feedback_attachment_textview = (TextView) findViewById(R.id.feedback_attachment_textview);
        feedback_attachment_textview.setVisibility(View.VISIBLE);

        SessionManager sessionManager = new SessionManager(SnapeveFeedback.this);
        feedbackSubmittedByUserName = sessionManager.getSpecificUserDetail(SessionManager.KEY_FIRST_NAME) + " " + sessionManager.getSpecificUserDetail(SessionManager.KEY_LAST_NAME);
        System.out.println("feedbackSubmittedByUserName---------  " + feedbackSubmittedByUserName);

        feedbackposttedDate = getCurrentTime();

        feedback_type_list = new ArrayList<String>();
        feedback_type_list.add("[ Select category ]");
        feedback_type_list.add("Found a bug");
        feedback_type_list.add("Suggestion");
        feedback_type_list.add("Miscellaneous feedback");

        feedback_list_adpter = new ArrayAdapter<String>(SnapeveFeedback.this, android.R.layout.simple_spinner_item, feedback_type_list);
        feedback_list_adpter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feedback_type.setAdapter(feedback_list_adpter);


        mClient = Singleton.Instance().mClientMethod(SnapeveFeedback.this);
        mFeedbackTable = mClient.getTable(feedback_table.class);

        feedback_attachment_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFromGallery();
            }
        });
        feedback_submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeedback();
            }
        });
    }

    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        System.out.println(formatter.format(date));
        return formatter.format(date);
    }

    private void selectFromGallery() {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(false)
                .setShowGif(true)
                .setPreviewEnabled(false)
                .start(SnapeveFeedback.this, PICK_GALLERY_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_GALLERY_IMAGE && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                System.out.println("Photos-List Size-- " + photos.size());

                File imgFile = new File(photos.get(0));
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                feedback_attachment_textview.setVisibility(View.INVISIBLE);
                feedback_attachment_imageview.setImageBitmap(myBitmap);

                /*Intialization of Map;
                Why this is here because we want to keep map size always ONE
                 */
                mapForUploadingFeedbackAttachment = new HashMap<>();

                int index = photos.get(0).lastIndexOf('/');
                selectedFeedbackAttachmentName = photos.get(0).substring(index + 1);

                String addFileString = "file://" + photos.get(0);
                Uri getUriToSendAzure = Uri.parse(addFileString);
                System.out.println("selectedImageName------- " + selectedFeedbackAttachmentName);
                System.out.println("getUriToSendAzure------- " + getUriToSendAzure);
                mapForUploadingFeedbackAttachment.put(selectedFeedbackAttachmentName, getUriToSendAzure);
                System.out.println("mapForUploadingFeedbackAttachment-------  " + mapForUploadingFeedbackAttachment);

            }
        }
    }


    private void submitFeedback() {

        if (feedback_title.getText().toString().length() > 0) {

            if (feedback_type.getSelectedItemPosition() == 0) {
                Toast.makeText(SnapeveFeedback.this, "Select a category", Toast.LENGTH_LONG).show();

            } else {
                new SubmitFeedbackAsyncTask().execute();
            }
        } else {
            Toast.makeText(SnapeveFeedback.this, "Enter title", Toast.LENGTH_LONG).show();
        }


    }


    public class SubmitFeedbackAsyncTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(SnapeveFeedback.this);
            progressDialog.setMessage("Submitting Feedback.....");
            progressDialog.show();


            feedback_table = new feedback_table();
            feedback_table.setAttachment_url(AzureConfiguration.Storage_url + selectedFeedbackAttachmentName);
            feedback_table.setCategory(feedback_type.getSelectedItem().toString());
            feedback_table.setTitle(feedback_title.getText().toString());
            feedback_table.setPostdate(feedbackposttedDate);
            feedback_table.setSubmitted_by(feedbackSubmittedByUserName);


        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                String getFeedbackID = mFeedbackTable.insert(feedback_table).get().getId();
                if (getFeedbackID.length() > 0) {
                    for (Map.Entry<String, Uri> entry : mapForUploadingFeedbackAttachment.entrySet()) {

                        final InputStream imageStream = getContentResolver().openInputStream(entry.getValue());
                        final int imageLength = imageStream.available();
                        container = getContainer();
                        CloudBlockBlob imageBlob = container.getBlockBlobReference(entry.getKey());
                        imageBlob.upload(imageStream, imageLength);

                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (StorageException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressDialog.dismiss();
            new FancyAlertDialog.Builder(SnapeveFeedback.this)
                    .setTitle("Thank You for Feedback")
                    .setBackgroundColor(Color.parseColor("#303F9F"))  //Don't pass R.color.colorvalue
                    .setMessage("Thnak You for Valueble feedback")
                    .setNegativeBtnText("Back")
                    .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))  //Don't pass R.color.colorvalue
                    .setAnimation(Animation.POP)
                    .isCancellable(false)
                    .setIcon(R.drawable.ic_star_border_black_24dp, Icon.Visible)
                    .OnNegativeClicked(new FancyAlertDialogListener() {
                        @Override
                        public void OnClick() {
                            onBackPressed();
                        }
                    })
                    .build();


        }
    }

}
