package com.example.instagramclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("5Q0LEpvYZY0sSRR35OdyniAz2BSEmYLjx6lbRTjH")
                // if defined
                .clientKey("uCfvhSyLxigHXgxkYs33W9e3W0gqZWXjvywwQaPI")
                .server("https://parseapi.back4app.com/")
                .build()
        );

    }
}
