package com.example.memyself.myphotosharingapp.map;

import com.example.memyself.myphotosharingapp.photo.Photo;

/**
 * Created by MeMyself on 7/29/2017.
 */

public interface MapView {
    void addPhoto(Photo photo);
    void removePhoto(Photo photo);
    void onPhotosError(String error);
}
