package com.example.memyself.myphotosharingapp.main;

/**
 * Created by MeMyself on 7/27/2017.
 */

public interface MainView {
    void onUploadInit();
    void onUploadComplete();
    void onUploadError(String error);
}
