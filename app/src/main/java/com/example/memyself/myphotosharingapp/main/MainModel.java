package com.example.memyself.myphotosharingapp.main;

import android.location.Location;

/**
 * Created by MeMyself on 8/1/2017.
 */

public interface MainModel {
    void logout();
    void uploadPhoto(Location location, String path);
}
