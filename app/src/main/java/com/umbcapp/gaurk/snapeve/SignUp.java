package com.umbcapp.gaurk.snapeve;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SignUp extends AppCompatActivity {

    private EditText signup_page_f_name_edittext;
    private EditText signup_page_l_name_edittext;
    private EditText signup_page_email_edittext;
    private EditText signup_user_name_edittext;
    private EditText signup_page_confirm_pass_edittext;
    private EditText signup_page_pass_edittext;
    private TextView signup_page_signup_btn_card_textview;
    private String last_name;
    private String first_name;
    private String confirm_pass;
    private String pass;
    private String user_name;
    private String email;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        signup_page_f_name_edittext = (EditText) findViewById(R.id.signup_page_f_name_edittext);
        signup_page_l_name_edittext = (EditText) findViewById(R.id.signup_page_l_name_edittext);
        signup_page_email_edittext = (EditText) findViewById(R.id.signup_page_email_edittext);
        signup_user_name_edittext = (EditText) findViewById(R.id.signup_user_name_edittext);
        signup_page_pass_edittext = (EditText) findViewById(R.id.signup_page_pass_edittext);
        signup_page_confirm_pass_edittext = (EditText) findViewById(R.id.signup_page_confirm_pass_edittext);
        signup_page_signup_btn_card_textview = (TextView) findViewById(R.id.signup_page_signup_btn_card_textview);

        signup_page_signup_btn_card_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateInputs()) {
                    executeSignupApi();
                }
            }
        });


    }

    private void executeSignupApi() {

        final ProgressDialog progressDialog = new ProgressDialog(SignUp.this);
        progressDialog.setTitle("Signing up, Please wait...");
        progressDialog.create();
        progressDialog.show();
        JsonObject jsonObjectParameters = new JsonObject();


        first_name = signup_page_f_name_edittext.getText().toString().trim();
        last_name = signup_page_l_name_edittext.getText().toString().trim();
        email = signup_page_email_edittext.getText().toString().trim();
        user_name = signup_user_name_edittext.getText().toString().trim();
        pass = signup_page_pass_edittext.getText().toString().trim();
        confirm_pass = signup_page_confirm_pass_edittext.getText().toString().trim();

        jsonObjectParameters.addProperty("first_name", first_name);
        jsonObjectParameters.addProperty("last_name", last_name);
        jsonObjectParameters.addProperty("email", email);
        jsonObjectParameters.addProperty("user_name", user_name);
        jsonObjectParameters.addProperty("pass", pass);


        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("sign_up_api", jsonObjectParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                progressDialog.dismiss();
                System.out.println(" sign_up_api exception    " + exception);

            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                progressDialog.dismiss();

                System.out.println(" sign_up_api success response    " + response);

//
                if (response.toString().contains("Email Exist")) {
                    Toast.makeText(getApplicationContext(), "Email Already Exist!", Toast.LENGTH_SHORT).show();
                } else if (response.toString().contains("Username Exist")) {
                    Toast.makeText(getApplicationContext(), "Username Not Available!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Account Created", Toast.LENGTH_SHORT).show();
                    parseResponse(response);
                }

            }
        });

    }

    private void parseResponse(JsonElement response) {

        startActivity(new Intent(getApplicationContext(), MainActivity.class));

    }

    private boolean validateInputs() {
        System.out.println("IN VALIDATION");

        first_name = signup_page_f_name_edittext.getText().toString().trim();
        last_name = signup_page_l_name_edittext.getText().toString().trim();
        email = signup_page_email_edittext.getText().toString().trim();
        user_name = signup_user_name_edittext.getText().toString().trim();
        pass = signup_page_pass_edittext.getText().toString().trim();
        confirm_pass = signup_page_confirm_pass_edittext.getText().toString().trim();


        if ((email.isEmpty()) || (first_name.isEmpty()) || (last_name.isEmpty()) || (user_name.isEmpty()) || (pass.isEmpty()) || (confirm_pass.isEmpty())) {

            System.out.println("VALIDATION FALSE");
            Toast.makeText(getApplicationContext(), "Fields empty", Toast.LENGTH_SHORT).show();
            return false;
        } else {

            if (email.matches(emailPattern)) {
//            if (email != null) {

                if (pass.length() > 3) {
                    if (pass.equals(confirm_pass)) {

                        return true;

                    } else {
                        Toast.makeText(getApplicationContext(), "Password mismatch", Toast.LENGTH_SHORT).show();

                        return false;
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Password short", Toast.LENGTH_SHORT).show();
                    return false;
                }

            } else {
                Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                return false;
            }


        }
    }
}
