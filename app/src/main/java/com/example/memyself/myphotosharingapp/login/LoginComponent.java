package com.example.memyself.myphotosharingapp.login;

import com.example.memyself.myphotosharingapp.PhotoSharingAppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by MeMyself on 7/26/2017.
 */

@Singleton
@Component(modules = {LoginModule.class, PhotoSharingAppModule.class})
public interface LoginComponent {
    void inject(LoginActivity activity);
}
