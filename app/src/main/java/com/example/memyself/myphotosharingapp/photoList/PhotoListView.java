package com.example.memyself.myphotosharingapp.photoList;

import com.example.memyself.myphotosharingapp.photo.Photo;

/**
 * Created by MeMyself on 7/31/2017.
 */

public interface PhotoListView {
    void showList();
    void hideList();
    void showProgress();
    void hideProgress();

    void addPhoto(Photo photo);
    void removePhoto(Photo photo);
    void onPhotosError(String error);
}
