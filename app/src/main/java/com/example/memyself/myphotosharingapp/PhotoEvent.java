package com.example.memyself.myphotosharingapp;

import com.example.memyself.myphotosharingapp.photo.Photo;

/**
 * Created by MeMyself on 7/31/2017.
 */

public class PhotoEvent {
    private int type;
    private Photo photo;
    private String error;

    public final static int READ_EVENT = 0;
    public final static int DELETE_EVENT = 1;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }
}
