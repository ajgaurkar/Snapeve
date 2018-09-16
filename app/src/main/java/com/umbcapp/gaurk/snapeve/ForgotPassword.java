package com.umbcapp.gaurk.snapeve;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class ForgotPassword extends AppCompatActivity {

    private EditText forgotpassword_user_name_edittext;
    private EditText forgotpassword_security_answer_edittext;
    private Spinner forgotpassword_security_question_spinner;
    private ArrayList<String> questionsList = new ArrayList<>();
    private CardView forgotpassword_btn_card;
    private String getUserName, forgotPasswordSecurityQuestion, getSecuirtyQuestionAnswer;
    private CharSequence label;
    private String user_pass, first_name, last_name;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword);

        forgotpassword_user_name_edittext = (EditText) findViewById(R.id.forgotpassword_user_name_edittext);
        forgotpassword_security_answer_edittext = (EditText) findViewById(R.id.forgotpassword_security_answer_edittext);
        forgotpassword_security_question_spinner = (Spinner) findViewById(R.id.forgotpassword_security_question_spinner);
        forgotpassword_btn_card = (CardView) findViewById(R.id.forgotpassword_btn_card);


        questionsList.add("[ Select a security question ]");
        questionsList.add("What was your childhood nickname?");
        questionsList.add("What is your favorite movie?");
        questionsList.add("Town of your first school?");
        questionsList.add("Mother's maiden name?");
        questionsList.add("Year when your father was born?");
        questionsList.add("What is your pet’s name?");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.signup_spinner_item, questionsList);
        dataAdapter.setDropDownViewResource(R.layout.signup_spinner_dropdown_item);
        forgotpassword_security_question_spinner.setAdapter(dataAdapter);


        forgotpassword_btn_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateInputs()) {
                    fetchUserLoginCredential();
                } else {
                    System.out.println("Something went worng in validateInputs");

                }


            }
        });

    }

    private void fetchUserLoginCredential() {

        final ProgressDialog progressDialog = new ProgressDialog(ForgotPassword.this);
        progressDialog.setTitle("Fetching user detail...");
        progressDialog.create();
        progressDialog.show();

        JsonObject jsonObjectParameters = new JsonObject();
        jsonObjectParameters.addProperty("user_name", getUserName);
        jsonObjectParameters.addProperty("security_answer", getSecuirtyQuestionAnswer);
        jsonObjectParameters.addProperty("security_question", forgotPasswordSecurityQuestion);


        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("forgot_password_api", jsonObjectParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                progressDialog.dismiss();
                System.out.println(" Forgot password  exception    " + exception);

            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                progressDialog.dismiss();

                System.out.println(" Forgot password success response    " + response);
                parseResponse(response);

            }
        });

    }

    private boolean validateInputs() {
        getUserName = forgotpassword_user_name_edittext.getText().toString();
        forgotPasswordSecurityQuestion = questionsList.get(forgotpassword_security_question_spinner.getSelectedItemPosition());
        getSecuirtyQuestionAnswer = forgotpassword_security_answer_edittext.getText().toString();
        System.out.println("getUserName----------  " + getUserName);
        System.out.println("forgotPasswordSecurity_question----------  " + forgotPasswordSecurityQuestion);
        System.out.println("getSecuirtyQuestionAnswer----------  " + getSecuirtyQuestionAnswer);
        if ((getUserName.isEmpty()) || (forgotPasswordSecurityQuestion.isEmpty()) || (getSecuirtyQuestionAnswer.isEmpty())) {
            Toast.makeText(getApplicationContext(), "Fields Empty", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (getUserName.length() > 0) {
                if (forgotpassword_security_question_spinner.getSelectedItemPosition() != 0) {

                    if (getSecuirtyQuestionAnswer.length() > 0) {
                        return true;
                    } else {
                        Toast.makeText(getApplicationContext(), "Security question should be correct", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Select a security question", Toast.LENGTH_SHORT).show();
                    return false;
                }

            } else {
                Toast.makeText(getApplicationContext(), "User Name should correct", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

    }


    private void parseResponse(JsonElement response) {

        JsonArray fetchPasswordJsonArray = response.getAsJsonArray();

        JsonObject fetchPasswordJson_Object = fetchPasswordJsonArray.get(0).getAsJsonObject();

        System.out.println("fetchPasswordJson_Object--------  " + fetchPasswordJson_Object);

        String user_name = fetchPasswordJson_Object.get("user_name").toString();
        user_pass = fetchPasswordJson_Object.get("user_pass").toString();
        first_name = fetchPasswordJson_Object.get("first_name").toString();
        last_name = fetchPasswordJson_Object.get("last_name").toString();


        String user_fullname = first_name + " " + last_name;
        String user_password_bold = "<strong>" + user_pass + "</strong>";

        AlertDialog.Builder openFtechPasswordDialog = new AlertDialog.Builder(ForgotPassword.this);
        openFtechPasswordDialog.setTitle("Hey ,\n" + user_fullname);
        openFtechPasswordDialog.setMessage("User Password is " + Html.fromHtml(user_password_bold));
        openFtechPasswordDialog.setPositiveButton("Copy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(label, user_pass);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(ForgotPassword.this, "Copied", Toast.LENGTH_LONG).show();
            }
        });
        openFtechPasswordDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        openFtechPasswordDialog.create().show();


    }

}
