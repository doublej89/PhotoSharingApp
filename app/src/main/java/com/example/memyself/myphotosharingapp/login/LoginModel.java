package com.example.memyself.myphotosharingapp.login;

/**
 * Created by MeMyself on 7/26/2017.
 */

public interface LoginModel {
    void signUp(final String email, final String password);
    void signIn(String email, String password);
}
