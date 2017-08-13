package com.example.memyself.myphotosharingapp.map;

import com.example.memyself.myphotosharingapp.PhotoSharingAppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by MeMyself on 7/30/2017.
 */
@Singleton
@Component(modules = {MapModule.class, PhotoSharingAppModule.class})
public interface MapComponent {
    void inject(MapFragment fragment);
}
