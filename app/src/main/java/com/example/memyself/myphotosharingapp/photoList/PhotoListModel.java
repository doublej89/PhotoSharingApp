package com.example.memyself.myphotosharingapp.photoList;

import com.example.memyself.myphotosharingapp.photo.Photo;

/**
 * Created by MeMyself on 7/31/2017.
 */

public interface PhotoListModel {
    void subscribe();
    void unSubscribe();
    void remove(Photo photo);
}
