package com.vritti.sass.model;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class Myapplication  extends Application {

    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

    }
    }
