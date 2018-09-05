package com.umbcapp.gaurk.snapeve;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageFullscreenOpen extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.image_fullscreen);

        ImageView fullscreen_imageview = (ImageView) findViewById(R.id.fullscreen_imageview);

        Intent imageDetailIntent = getIntent();

        String img_url = imageDetailIntent.getStringExtra("img_url");
        Picasso.with(getApplicationContext()).load(img_url).fit().centerInside().into(fullscreen_imageview);


    }

}
