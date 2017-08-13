package com.example.memyself.myphotosharingapp;

import com.google.firebase.FirebaseException;

/**
 * Created by MeMyself on 7/26/2017.
 */

public interface FirebaseActionListenerCallback {
    void onSuccess();
    void onError(FirebaseException error);
}
