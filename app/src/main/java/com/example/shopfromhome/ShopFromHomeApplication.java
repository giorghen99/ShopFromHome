
package com.example.shopfromhome;

import android.app.Application;

import com.example.shopfromhome.network.ApiConfig;

public class ShopFromHomeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ApiConfig.initialize(this);
    }
}
