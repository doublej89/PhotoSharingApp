package com.example.memyself.myphotosharingapp.login;

/**
 * Created by MeMyself on 7/25/2017.
 */

public interface LoginView {
    void enableInputs();
    void disableInputs();
    void showProgress();
    void hideProgress();
    void handlesSignUp();
    void handlesSignIn();
    void newUserSuccess();
    void navigateToMainScreen();
    void setUserEmail(String email);
    void loginError(String error);
    void newUserError(String error);
}
