package com.umbcapp.gaurk.snapeve;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class BrowseGroupProfile extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.browser_grp_profile);

        Intent grpId = getIntent();
        String grp_id = grpId.getStringExtra("grp_id");
        System.out.println("BrowseGroupProfile grp_id : " + grp_id);


        fetchGrpProfile(grp_id);
    }



    private void fetchGrpProfile(String grp_id) {
        final ProgressDialog progressDialog = new ProgressDialog(BrowseGroupProfile.this);
        progressDialog.setTitle("Loading, Please wait...");
        progressDialog.create();
        progressDialog.show();
        JsonObject jsonObjectParameters = new JsonObject();
        jsonObjectParameters.addProperty("grp_id", grp_id);

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("browse_grp_profile_api", jsonObjectParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                progressDialog.dismiss();
                System.out.println(" browse_grp_profile_api exception    " + exception);

            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                progressDialog.dismiss();

                System.out.println(" browse_grp_profile_api success response    " + response);


            }
        });
    }
}
