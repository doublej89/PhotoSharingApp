package com.example.memyself.myphotosharingapp.main;

import android.location.Location;

/**
 * Created by MeMyself on 7/27/2017.
 */

public interface MainPresenter {
    void onCreate();
    void onDestroy();

    void logout();
    void uploadPhoto(Location location, String path);
    void onEventMainThread(MainEvent event);
}
