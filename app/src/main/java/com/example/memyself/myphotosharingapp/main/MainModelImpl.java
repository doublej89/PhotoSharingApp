package com.example.memyself.myphotosharingapp.main;

import android.location.Location;

import com.example.memyself.myphotosharingapp.FirebaseHelper;
import com.example.memyself.myphotosharingapp.photo.Photo;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * Created by MeMyself on 8/1/2017.
 */

public class MainModelImpl implements MainModel {
    private EventBus eventBus;
    private FirebaseHelper firebaseHelper;
    private ImageStorage storage;

    public MainModelImpl(EventBus eventBus, FirebaseHelper firebaseHelper, ImageStorage storage) {
        this.eventBus = eventBus;
        this.firebaseHelper = firebaseHelper;
        this.storage = storage;
    }

    @Override
    public void logout() {
        firebaseHelper.logout();
    }

    @Override
    public void uploadPhoto(Location location, String path) {
        String id = firebaseHelper.create();
        final Photo photo = new Photo();
        photo.setId(id);
        photo.setEmail(firebaseHelper.getAuthEmail());
        if (location != null) {
            photo.setLatitutde(location.getLatitude());
            photo.setLongitude(location.getLongitude());
        }
        post(MainEvent.UPLOAD_INIT);
        storage.upload(new File(path), photo.getId(), new ImageStorageListener() {
            @Override
            public void onSuccess() {
                photo.setUrl(storage.getImageUrl(photo.getId()));
                firebaseHelper.update(photo);
                post(MainEvent.UPLOAD_COMPLETE);
            }

            @Override
            public void onError(String error) {
                post(MainEvent.UPLOAD_ERROR);
            }
        });
    }

    private void post(int type){
        post(type, null);
    }

    private void post(int type, String error){
        MainEvent event = new MainEvent();
        event.setType(type);
        event.setError(error);
        eventBus.post(event);
    }
}
