package com.example.memyself.myphotosharingapp.photoList;

import com.example.memyself.myphotosharingapp.PhotoSharingAppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by MeMyself on 8/5/2017.
 */

@Singleton
@Component(modules = {PhotoListModule.class, PhotoSharingAppModule.class})
public interface PhotoListComponent {
    void inject(PhotoListFragment fragment);
}