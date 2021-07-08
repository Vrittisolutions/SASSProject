package com.vritti.sass;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;


import com.facebook.drawee.view.SimpleDraweeView;
import com.ortiz.touchview.TouchImageView;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Created by sharvari on 27-Mar-18.
 */

public class ImageFullScreenActivity extends AppCompatActivity {


    SimpleDraweeView img_full;
    Button btn_continue;
    Intent intent;
    WebView webView;
    ProgressBar toolbar_progress_App_bar;
    //private ScaleGestureDetector mScaleGestureDetector;
   // private float mScaleFactor = 1.0f;
   // private ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_image_fullscreen_lay);

        img_full = findViewById(R.id.img_full);


        intent = getIntent();



        String Path = intent.getStringExtra("share_image_path");

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(ImageFullScreenActivity.this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        img_full.setVisibility(View.VISIBLE);


        //String  image="http://z207.ekatm.com/Attachments/View%20Attachment/"+Path;
        String  image="http://z207.ekatm.co.in/Attachments/View%20Attachment/"+Path;
        img_full.setImageURI(image);





        }


    }




