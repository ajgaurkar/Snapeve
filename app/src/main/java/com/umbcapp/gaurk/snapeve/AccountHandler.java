package com.umbcapp.gaurk.snapeve;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.surveymonkey.surveymonkeyandroidsdk.SurveyMonkey;

public class AccountHandler extends AppCompatActivity implements View.OnClickListener {

    private SurveyMonkey sdkInstance;
    //old code
//    public static final String SURVEY_HASH = "ZGTLQ38";

    //individual users survey code
    public static final String SURVEY_HASH = "GCGXXMM";

    //group survey code
//    public static final String SURVEY_HASH = "GQLRGCC";

    public static final int SM_REQUEST_CODE = 0;
    public static final String SAMPLE_APP = "Sample App";
    private Button openFeedback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_handler);
        openFeedback = findViewById(R.id.account_handler_change_pass_btn);
        openFeedback.setOnClickListener(this);

        sdkInstance = new SurveyMonkey();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_handler_change_pass_btn:
                sdkInstance.startSMFeedbackActivityForResult(this, SM_REQUEST_CODE, SURVEY_HASH);
                break;

        }

    }
}
