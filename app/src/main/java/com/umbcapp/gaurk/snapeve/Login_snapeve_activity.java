package com.umbcapp.gaurk.snapeve;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

/**
 * Created by SSSI Dev-1 on 6/1/2018.
 */

public class Login_snapeve_activity extends AppCompatActivity {

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    String email;
    String password;
    private TextView login_page_login_google_textview;
    private TextView login_page_login_btn_textview;
    private EditText login_page_pass_edittext;
    private TextView login_page_signup_btn_textview;
    private TextView forgot_pass_textview;
    private EditText login_page_email_edittext;
    private CardView login_btn_card;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_snapeve);

        login_page_email_edittext = (EditText) findViewById(R.id.login_page_email_edittext);
        login_page_pass_edittext = (EditText) findViewById(R.id.login_page_pass_edittext);
        login_page_login_btn_textview = (TextView) findViewById(R.id.login_page_login_btn_textview);
        login_btn_card = (CardView) findViewById(R.id.login_btn_card);
        login_page_signup_btn_textview = (TextView) findViewById(R.id.login_page_signup_btn_textview);
        forgot_pass_textview = (TextView) findViewById(R.id.forgot_pass_textview);
        login_page_login_google_textview = (TextView) findViewById(R.id.login_page_login_google_textview);

        login_page_login_btn_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("VALIDATION");

                String validationResponse = validateInputs();

                if (validationResponse.equals("TRUE")) {
                    executeLoginApi();
                } else {
                    Toast.makeText(getApplicationContext(), validationResponse, Toast.LENGTH_SHORT).show();
                }

            }
        });
        login_page_signup_btn_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), validateInputs(), Toast.LENGTH_SHORT).show();
                System.out.println("VALIDATION");

                startActivity(new Intent(getApplicationContext(), SignUp.class));

            }
        });

    }

    private void executeLoginApi() {

        final ProgressDialog progressDialog = new ProgressDialog(Login_snapeve_activity.this);
        progressDialog.setTitle("Logging in, Please wait...");
        progressDialog.create();
        progressDialog.show();
        JsonObject jsonObjectParameters = new JsonObject();
        jsonObjectParameters.addProperty("user_name", login_page_email_edittext.getText().toString().trim());
        jsonObjectParameters.addProperty("user_password", login_page_pass_edittext.getText().toString().trim());

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("login_api", jsonObjectParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                progressDialog.dismiss();
                System.out.println(" executeLoginApi exception    " + exception);
                Toast.makeText(getApplicationContext(), "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                progressDialog.dismiss();

                System.out.println(" executeLoginApi success response    " + response);

                if (response.toString().contains("Authentication Failed")) {
                    Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                } else {
                    parseResponse(response);
                }
            }
        });
    }

    private void parseResponse(JsonElement response) {

        JsonArray userDataJSONArray = response.getAsJsonArray();

        JsonObject userData_list_object = userDataJSONArray.get(0).getAsJsonObject();

        System.out.println(" userData_list_object  " + userData_list_object);

        String user_id = userData_list_object.get("id").toString();
        String user_name = userData_list_object.get("user_name").toString();
        String user_pass = userData_list_object.get("user_pass").toString();
        String dp_url = userData_list_object.get("dp_url").toString();
        String first_name = userData_list_object.get("first_name").toString();
        String last_name = userData_list_object.get("last_name").toString();
        String email = userData_list_object.get("email").toString();
        int user_points = Integer.parseInt(userData_list_object.get("user_points").toString());

        dp_url = dp_url.substring(1, dp_url.length() - 1);
        user_id = user_id.substring(1, user_id.length() - 1);
        user_name = user_name.substring(1, user_name.length() - 1);
        user_pass = user_pass.substring(1, user_pass.length() - 1);
        first_name = first_name.substring(1, first_name.length() - 1);
        last_name = last_name.substring(1, last_name.length() - 1);
        email = email.substring(1, email.length() - 1);


        if (new SessionManager(getApplicationContext()).createLoginSession(user_id, user_name, user_pass, first_name, last_name, email)) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    private String validateInputs() {
        System.out.println("IN VALIDATION");

        email = login_page_email_edittext.getText().toString().trim();
        password = login_page_pass_edittext.getText().toString().trim();
        String response = "";

        if ((email.isEmpty()) || (password.isEmpty())) {

            System.out.println("VALIDATION FALSE");
            response = "Fields Empty";
        } else {


//            if (email.matches(emailPattern)) {
            if (email != null) {
                response = "Email success";

                if (password.length() < 4) {
                    response = "Password short";
                } else {
                    response = "TRUE";
                }

            } else {
                response = "Email failed";
            }

        }
        return response;
    }


}
