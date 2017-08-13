package com.example.memyself.myphotosharingapp.map;

import com.example.memyself.myphotosharingapp.PhotoEvent;

/**
 * Created by MeMyself on 7/29/2017.
 */

public interface MapPresenter {
    void onCreate();
    void onDestroy();

    void subscribe();
    void unsubscribe();

    void onEventMainThread(PhotoEvent event);
}
