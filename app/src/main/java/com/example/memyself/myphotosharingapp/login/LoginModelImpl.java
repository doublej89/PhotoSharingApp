package com.example.memyself.myphotosharingapp.login;

import android.support.annotation.NonNull;

import com.example.memyself.myphotosharingapp.FirebaseActionListenerCallback;
import com.example.memyself.myphotosharingapp.FirebaseHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by MeMyself on 7/26/2017.
 */

public class LoginModelImpl implements LoginModel {
    private EventBus eventBus;
    private FirebaseHelper firebase;

    public LoginModelImpl(EventBus eventBus, FirebaseHelper firebase) {
        this.eventBus = eventBus;
        this.firebase = firebase;
    }

    @Override
    public void signUp(final String email, final String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        post(LoginEvent.onSignUpSuccess);
                        signIn(email, password);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                post(LoginEvent.onSignUpError, e.getMessage());
            }
        });
    }

    @Override
    public void signIn(String email, String password) {
        if (email != null && password != null) {
            try {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                String email = firebase.getAuthEmail();
                                post(LoginEvent.onSignInSuccess, null, email);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                post(LoginEvent.onSignInError, e.getMessage());
                            }
                        });
            } catch (Exception e) {
                post(LoginEvent.onSignInError, e.getMessage());
            }
        } else {
            firebase.checkForSession(new FirebaseActionListenerCallback() {
                @Override
                public void onSuccess() {
                    String email = firebase.getAuthEmail();
                    post(LoginEvent.onSignInSuccess, null, email);
                }

                @Override
                public void onError(FirebaseException error) {
                    post(LoginEvent.onFailedToRecoverSession);
                }
            });
        }
    }

    private void post(int type) {
        post(type, null);
    }

    private void post(int type, String errorMessage) {
        post(type, errorMessage, null);
    }

    private void post(int type, String errorMessage, String loggedUserEmail) {
        LoginEvent loginEvent = new LoginEvent();
        loginEvent.setEventType(type);
        if (errorMessage != null) {
            loginEvent.setErrorMesage(errorMessage);
        }
        loginEvent.setLoggedUserEmail(loggedUserEmail);
        eventBus.post(loginEvent);
    }
}
