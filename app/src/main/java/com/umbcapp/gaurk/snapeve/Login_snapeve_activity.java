package com.umbcapp.gaurk.snapeve;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
                Toast.makeText(getApplicationContext(), validateInputs(), Toast.LENGTH_SHORT).show();
                System.out.println("VALIDATION");

            }
        });

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


            if (email.matches(emailPattern)) {
                response = "Email success";
            } else {
                response = "Email failed";
            }


            if (password.length() > 3) {
                response = "Password short";
            } else {
                response = "Goto login";
            }
        }

        return response;
    }


}
