package com.umbcapp.gaurk.snapeve;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserAgreementActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_agreement);

        Button useragreementconfirmbtn = (Button) findViewById(R.id.useragreementconfirmbtn);


        TextView user_agreement_text = (TextView) findViewById(R.id.user_agreement_text);
        user_agreement_text.setClickable(true);
        user_agreement_text.setMovementMethod(LinkMovementMethod.getInstance());
        String text1 = "<a href='https://sites.google.com/view/snapeve-user-agreement/home'> End user license agreement </a>";
        user_agreement_text.setText(Html.fromHtml(text1));

        TextView privacy_policy_text = (TextView) findViewById(R.id.privacy_policy_text);
        privacy_policy_text.setClickable(true);
        privacy_policy_text.setMovementMethod(LinkMovementMethod.getInstance());
        String text2 = "<a href='https://sites.google.com/view/snapeve-privacy-policy/home'> Privacy policy </a>";
        privacy_policy_text.setText(Html.fromHtml(text2));


        useragreementconfirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    @Override
    public void onBackPressed() {

    }
}
