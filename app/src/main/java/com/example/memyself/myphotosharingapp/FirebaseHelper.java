package com.example.memyself.myphotosharingapp;

import android.util.Log;

import com.example.memyself.myphotosharingapp.photo.FirebaseEventListenerCallback;
import com.example.memyself.myphotosharingapp.photo.Photo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by MeMyself on 7/26/2017.
 */

public class FirebaseHelper {
    private DatabaseReference mPhotosDatabaseReference;
    private ChildEventListener photosEventListener;

    public FirebaseHelper() {
        mPhotosDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public String getAuthEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getEmail();
        }
        return null;
    }

    public void checkForSession(FirebaseActionListenerCallback listener) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            listener.onSuccess();
        } else {
            listener.onError(null);
        }
    }

    public void checkForData(final FirebaseActionListenerCallback listener){
        ValueEventListener postListener = new ValueEventListener() {
            @Override public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    listener.onSuccess();
                } else {
                    listener.onError(null);
                }
            }

            @Override public void onCancelled(DatabaseError databaseError) {
                Log.d("FIREBASE API", databaseError.getMessage());
            }
        };
        mPhotosDatabaseReference.addValueEventListener(postListener);
    }

    public void subscribe(final FirebaseEventListenerCallback listenerCallback) {
        if (photosEventListener == null) {
            photosEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    listenerCallback.onChildAdded(dataSnapshot);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    listenerCallback.onChildRemoved(dataSnapshot);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    listenerCallback.onCancelled(databaseError);
                }
            };
            mPhotosDatabaseReference.addChildEventListener(photosEventListener);
        }
    }

    public void unsubscribe() {
        mPhotosDatabaseReference.removeEventListener(photosEventListener);
    }

    public void remove(Photo photo, FirebaseActionListenerCallback listener) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(photo.getId()).removeValue();

        listener.onSuccess();
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    public String create() {
        if (mPhotosDatabaseReference != null) {
            return mPhotosDatabaseReference.push().getKey();
        }
        return null;
    }

    public void update(Photo photo) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(photo.getId()).setValue(photo);
    }


}
