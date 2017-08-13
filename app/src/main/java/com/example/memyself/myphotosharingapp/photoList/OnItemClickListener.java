package com.example.memyself.myphotosharingapp.photoList;

import android.widget.ImageView;

import com.example.memyself.myphotosharingapp.photo.Photo;

/**
 * Created by MeMyself on 7/31/2017.
 */

public interface OnItemClickListener {
    void onPlaceClick(Photo photo);
    void onShareClick(Photo photo, ImageView view);
    void onDeleteClick(Photo photo);
}
