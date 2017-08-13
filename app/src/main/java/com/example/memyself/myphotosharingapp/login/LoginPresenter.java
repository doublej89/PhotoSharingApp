package com.example.memyself.myphotosharingapp.login;

/**
 * Created by MeMyself on 7/25/2017.
 */

public interface LoginPresenter {
    void onCreate();
    void onDestroy();
    void onEventMainThread(LoginEvent event);
    void login(String email, String password);
    void signup(String email, String password);
}
