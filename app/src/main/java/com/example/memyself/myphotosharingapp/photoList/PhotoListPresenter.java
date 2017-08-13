package com.example.memyself.myphotosharingapp.photoList;

import com.example.memyself.myphotosharingapp.PhotoEvent;
import com.example.memyself.myphotosharingapp.photo.Photo;

/**
 * Created by MeMyself on 7/31/2017.
 */

public interface PhotoListPresenter {
    void onCreate();
    void onDestroy();

    void subscribe();
    void unsubscribe();

    void removePhoto(Photo photo);
    void onEventMainThread(PhotoEvent event);
}
