package com.umbcapp.gaurk.snapeve;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

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
    private String security_answer;
    private String security_question;
    private String pass;
    private String user_name;
    String ref_code;
    private String email;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private Spinner signup_page_security_question_spinner;
    private EditText signup_page_referral_code_edittext;
    private EditText signup_page_security_answer_edittext;
    private ArrayList<String> questionsList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        signup_page_f_name_edittext = (EditText) findViewById(R.id.signup_page_f_name_edittext);
        signup_page_l_name_edittext = (EditText) findViewById(R.id.signup_page_l_name_edittext);
        signup_page_email_edittext = (EditText) findViewById(R.id.signup_page_email_edittext);
        signup_user_name_edittext = (EditText) findViewById(R.id.signup_user_name_edittext);
        signup_page_pass_edittext = (EditText) findViewById(R.id.signup_page_pass_edittext);
        signup_page_security_answer_edittext = (EditText) findViewById(R.id.signup_page_security_answer_edittext);
        signup_page_referral_code_edittext = (EditText) findViewById(R.id.signup_page_referral_code_edittext);
        signup_page_confirm_pass_edittext = (EditText) findViewById(R.id.signup_page_confirm_pass_edittext);
        signup_page_signup_btn_card_textview = (TextView) findViewById(R.id.signup_page_signup_btn_card_textview);
        signup_page_security_question_spinner = (Spinner) findViewById(R.id.signup_page_security_question_spinner);

        questionsList.add("[ Select a security question ]");
        questionsList.add("What was your childhood nickname?");
        questionsList.add("What is your favorite movie?");
        questionsList.add("Town of your first school?");
        questionsList.add("Mother's maiden name?");
        questionsList.add("Year when your father was born?");
        questionsList.add("What is your pet’s name?");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.signup_spinner_item, questionsList);
        dataAdapter.setDropDownViewResource(R.layout.signup_spinner_dropdown_item);
        signup_page_security_question_spinner.setAdapter(dataAdapter);


        signup_page_signup_btn_card_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                startActivity(new Intent(getApplicationContext(), Signup_grp_join.class));

                if (validateInputs()) {


                    verifyRefCodeApi();

//                    executeSignupApi();
                }
            }
        });

    }

    private void verifyRefCodeApi() {

        ref_code = signup_page_referral_code_edittext.getText().toString().trim();

        if (ref_code == null || ref_code.isEmpty()) {
//            System.out.println("Show no ref dialog");
            showNoRefCodeDialog(1);
        } else {

            final ProgressDialog progressDialog = new ProgressDialog(SignUp.this);
            progressDialog.setTitle("Verifying code, Please wait...");
            progressDialog.create();
            progressDialog.show();
            JsonObject jsonObjectParameters = new JsonObject();

            jsonObjectParameters.addProperty("ref_code", ref_code);

            final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
            ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("verify_referral_code_api", jsonObjectParameters);

            Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
                @Override
                public void onFailure(Throwable exception) {
                    resultFuture.setException(exception);
                    progressDialog.dismiss();
                    System.out.println(" verify_referral_code_api exception    " + exception);

                }

                @Override
                public void onSuccess(JsonElement response) {
                    resultFuture.set(response);
                    progressDialog.dismiss();

                    System.out.println(" verify_referral_code_api success response    " + response);

                    if (response.toString().contains("true")) {
                        System.out.println("REF TRUE");
                        executeSignupApi();
                    } else {
                        showNoRefCodeDialog(2);
                        System.out.println("REF FALSE");
                    }

                }
            });
        }

    }

    private void showNoRefCodeDialog(int code) {
        String message = null;
        ref_code = null;
        if (code == 1) {
            message = "No Referral code applied. Do you want to proceed?";
        }
        if (code == 2) {
            message = "Invalid code. Do you still want to proceed?";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes, proceed", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.out.println("No ref code, execute signup api ");
                        executeSignupApi();
                    }
                })
                .setNegativeButton("No, Go back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.out.println("No ref code, GO BACK ");

                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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
        jsonObjectParameters.addProperty("ref_code", ref_code);
        jsonObjectParameters.addProperty("security_answer", security_answer);
        jsonObjectParameters.addProperty("security_question", security_question);

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

        //currently its jsonobject/element in response. It might change to Array later

//        JsonArray userDataJSONArray = response.getAsJsonArray();

//        JsonObject userData_list_object = userDataJSONArray.get(0).getAsJsonObject();
        JsonObject userData_list_object = response.getAsJsonObject();

        System.out.println(" userData_list_object  " + userData_list_object);

        String user_id = userData_list_object.get("id").toString();
        String user_name = userData_list_object.get("user_name").toString();
        String user_pass = userData_list_object.get("user_pass").toString();
//        String dp_url = userData_list_object.get("dp_url").toString();
        String first_name = userData_list_object.get("first_name").toString();
        String last_name = userData_list_object.get("last_name").toString();
        String email = userData_list_object.get("email").toString();
        int user_points = Integer.parseInt(userData_list_object.get("user_points").toString());

//        dp_url = dp_url.substring(1, dp_url.length() - 1);
        user_id = user_id.substring(1, user_id.length() - 1);
        user_name = user_name.substring(1, user_name.length() - 1);
        user_pass = user_pass.substring(1, user_pass.length() - 1);
        first_name = first_name.substring(1, first_name.length() - 1);
        last_name = last_name.substring(1, last_name.length() - 1);
        email = email.substring(1, email.length() - 1);

        if (new SessionManager(getApplicationContext()).createLoginSession(user_id, user_name, user_pass, first_name, last_name, email)) {
            Intent joinGrpIntent = new Intent(getApplicationContext(), Signup_grp_join.class);
            joinGrpIntent.putExtra("page_open_mode", 0);
            startActivity(joinGrpIntent);
        }

    }

    private boolean validateInputs() {
        System.out.println("IN VALIDATION");

        first_name = "_";
        last_name = "_";

//        first_name = signup_page_f_name_edittext.getText().toString().trim();
//        last_name = signup_page_l_name_edittext.getText().toString().trim();
//        email = signup_page_email_edittext.getText().toString().trim();
        email = "abx@xyz.com";
        user_name = signup_user_name_edittext.getText().toString().trim();
        pass = signup_page_pass_edittext.getText().toString().trim();
        confirm_pass = signup_page_confirm_pass_edittext.getText().toString().trim();
        security_answer = signup_page_security_answer_edittext.getText().toString().trim();
        security_question = questionsList.get(signup_page_security_question_spinner.getSelectedItemPosition());

//        if ((email.isEmpty()) || (first_name.isEmpty()) || (last_name.isEmpty()) || (user_name.isEmpty()) || (security_answer.isEmpty()) || (pass.isEmpty()) || (confirm_pass.isEmpty())) {
        if ((user_name.isEmpty()) || (security_answer.isEmpty()) || (pass.isEmpty()) || (confirm_pass.isEmpty())) {

            System.out.println("VALIDATION FALSE");
            Toast.makeText(getApplicationContext(), "Fields empty", Toast.LENGTH_SHORT).show();
            return false;
        } else {

            if (email.matches(emailPattern)) {
//            if (email != null) {

                if (pass.length() > 3) {
                    if (pass.equals(confirm_pass)) {

                        if (signup_page_security_question_spinner.getSelectedItemPosition() != 0) {

                            System.out.println("all ok");
                            return true;
                        } else {
                            Toast.makeText(getApplicationContext(), "Select a security question", Toast.LENGTH_SHORT).show();
                            return false;
                        }

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
