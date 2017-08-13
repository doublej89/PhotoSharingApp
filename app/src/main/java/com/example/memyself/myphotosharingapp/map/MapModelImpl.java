package com.example.memyself.myphotosharingapp.map;

import android.text.TextUtils;

import com.example.memyself.myphotosharingapp.FirebaseHelper;
import com.example.memyself.myphotosharingapp.PhotoEvent;
import com.example.memyself.myphotosharingapp.photo.FirebaseEventListenerCallback;
import com.example.memyself.myphotosharingapp.photo.Photo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by MeMyself on 7/30/2017.
 */

public class MapModelImpl implements MapModel{
    private EventBus eventBus;
    private FirebaseHelper firebaseHelper;

    public MapModelImpl(EventBus eventBus, FirebaseHelper firebaseHelper) {
        this.eventBus = eventBus;
        this.firebaseHelper = firebaseHelper;
    }

    @Override
    public void subscribe() {
        firebaseHelper.subscribe(new FirebaseEventListenerCallback() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot) {
                Photo photo = dataSnapshot.getValue(Photo.class);
                if (photo != null) {
                    photo.setId(dataSnapshot.getKey());
                    String email = firebaseHelper.getAuthEmail();
                    boolean publishedByMe = !TextUtils.isEmpty(email) && !TextUtils.isEmpty(photo.getEmail()) && email.equals(photo.getEmail());
                    photo.setPublishedByMe(publishedByMe);
                    post(PhotoEvent.READ_EVENT, photo);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Photo photo = dataSnapshot.getValue(Photo.class);
                if (photo != null) {
                    photo.setId(dataSnapshot.getKey());
                    post(PhotoEvent.DELETE_EVENT, photo);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                post(error.getMessage());
            }
        });
    }

    @Override
    public void unsubscribe() {
        firebaseHelper.unsubscribe();
    }

    private void post(int type, Photo photo){
        post(type, photo, null);
    }

    private void post(String error){
        post(0, null, error);
    }

    private void post(int type, Photo photo, String error){
        PhotoEvent event = new PhotoEvent();
        event.setType(type);
        event.setError(error);
        event.setPhoto(photo);
        eventBus.post(event);
    }
}
