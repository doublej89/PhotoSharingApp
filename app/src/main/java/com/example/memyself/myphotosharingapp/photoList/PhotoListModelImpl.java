package com.example.memyself.myphotosharingapp.photoList;

import android.text.TextUtils;

import com.example.memyself.myphotosharingapp.FirebaseActionListenerCallback;
import com.example.memyself.myphotosharingapp.FirebaseHelper;
import com.example.memyself.myphotosharingapp.PhotoEvent;
import com.example.memyself.myphotosharingapp.photo.FirebaseEventListenerCallback;
import com.example.memyself.myphotosharingapp.photo.Photo;
import com.google.firebase.FirebaseException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by MeMyself on 7/31/2017.
 */

public class PhotoListModelImpl implements PhotoListModel{
    private EventBus eventBus;
    private FirebaseHelper firebase;

    public PhotoListModelImpl(EventBus eventBus, FirebaseHelper firebase) {
        this.eventBus = eventBus;
        this.firebase = firebase;
    }

    @Override
    public void subscribe() {
        firebase.checkForData(new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(FirebaseException error) {
                if (error != null) {
                    post(PhotoEvent.READ_EVENT, error.getMessage());
                } else {
                    post(PhotoEvent.READ_EVENT, "");
                }

            }
        });
        firebase.subscribe(new FirebaseEventListenerCallback() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot) {
                Photo photo = dataSnapshot.getValue(Photo.class);
                photo.setId(dataSnapshot.getKey());

                String email = firebase.getAuthEmail();

                boolean publishedByMy = !TextUtils.isEmpty(email) && !TextUtils.isEmpty(photo.getEmail()) && photo.getEmail().equals(email);
                photo.setPublishedByMe(publishedByMy);
                post(PhotoEvent.READ_EVENT, photo);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Photo photo = dataSnapshot.getValue(Photo.class);
                photo.setId(dataSnapshot.getKey());

                post(PhotoEvent.DELETE_EVENT, photo);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                post(PhotoEvent.READ_EVENT, error.getMessage());
            }
        });
    }

    @Override
    public void unSubscribe() {
        firebase.unsubscribe();
    }

    @Override
    public void remove(final Photo photo) {
        firebase.remove(photo, new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
                post(PhotoEvent.DELETE_EVENT, photo);
            }

            @Override
            public void onError(FirebaseException error) {

            }
        });
    }

    private void post(int type, Photo photo){
        post(type, photo, null);
    }

    private void post(int type, String error){
        post(type, null, error);
    }

    private void post(int type, Photo photo, String error){
        PhotoEvent event = new PhotoEvent();
        event.setType(type);
        event.setError(error);
        event.setPhoto(photo);
        eventBus.post(event);
    }
}
