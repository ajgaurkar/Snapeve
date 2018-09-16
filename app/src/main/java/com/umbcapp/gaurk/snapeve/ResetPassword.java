package com.umbcapp.gaurk.snapeve;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.io.InputStream;
import java.util.Map;

import static com.umbcapp.gaurk.snapeve.AzureConfiguration.getContainer;

public class ResetPassword extends AppCompatActivity {
    private EditText resetpassword_old_password_edittext, resetpassword_new_password_edittext, resetpassword_confirm_password_edittext;
    private CardView forgotpassword_btn_card;
    private SessionManager sessionManager;
    private String confirmPassword, newPasword, oldPassword;
    private MobileServiceTable<user_table> mUserTable;
    private MobileServiceClient mClient;
    private user_table user_table;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_activity);

        resetpassword_old_password_edittext = (EditText) findViewById(R.id.resetpassword_old_password_edittext);
        resetpassword_new_password_edittext = (EditText) findViewById(R.id.resetpassword_new_password_edittext);
        resetpassword_confirm_password_edittext = (EditText) findViewById(R.id.resetpassword_confirm_password_edittext);
        forgotpassword_btn_card = (CardView) findViewById(R.id.resetpassword_btn_card);


        sessionManager = new SessionManager(ResetPassword.this);

        System.out.println("get User Id --- " + sessionManager.getSpecificUserDetail(SessionManager.KEY_USER_ID));


        mClient = Singleton.Instance().mClientMethod(ResetPassword.this);
        mUserTable = mClient.getTable(user_table.class);

        forgotpassword_btn_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateNewPasword()) {
                    resetPassword();
                } else {
                    System.out.println("Something went worng in validateNewPasword ");
                }

            }
        });
    }

    public Boolean validateNewPasword() {
        oldPassword = resetpassword_old_password_edittext.getText().toString();
        newPasword = resetpassword_new_password_edittext.getText().toString();
        confirmPassword = resetpassword_confirm_password_edittext.getText().toString();

        if ((oldPassword.isEmpty()) || (newPasword.isEmpty()) || (confirmPassword.isEmpty())) {
            Toast.makeText(getApplicationContext(), "Fields Empty", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (oldPassword.length() > 0) {
                if (checkNewPasword()) {
                    return true;
                } else {
                    Toast.makeText(getApplicationContext(), "Password not match ", Toast.LENGTH_SHORT).show();
                    return false;
                }

            } else {
                Toast.makeText(getApplicationContext(), "Please Provide Old Password", Toast.LENGTH_SHORT).show();
                return false;
            }
        }


    }

    private boolean checkNewPasword() {
        String newPaswword = resetpassword_new_password_edittext.getText().toString();
        String confirmPassword = resetpassword_confirm_password_edittext.getText().toString();
        if (newPaswword.equals(confirmPassword)) {
            return true;

        } else {

            return false;
        }

    }


    private void resetPassword() {

        final ProgressDialog progressDialog = new ProgressDialog(ResetPassword.this);
        progressDialog.setTitle("Wait for while ");
        progressDialog.create();
        progressDialog.show();


        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    user_table = new user_table();
                    user_table.setId(new SessionManager(ResetPassword.this).getSpecificUserDetail(SessionManager.KEY_USER_ID));
                    user_table.setUser_pass(confirmPassword);
                    mUserTable.update(user_table).get();

                } catch (final Exception e) {
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                System.out.println("result " + result);
                progressDialog.dismiss();

                sessionManager.setSpecificUserBooleanDetail(SessionManager.KEY_RESET_PASSWORD_STATUS, true);
                System.out.println("showSuccessResetPasswordDialog Before");
                showSuccessResetPasswordDialog();
                System.out.println("showSuccessResetPasswordDialog After");


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


    private void showSuccessResetPasswordDialog() {
        AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(ResetPassword.this);
        passwordResetDialog.setMessage("Password Reset Succesfully.");
        passwordResetDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        passwordResetDialog.create().show();

    }

}
