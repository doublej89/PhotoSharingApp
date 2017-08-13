package com.example.memyself.myphotosharingapp.main;

import com.example.memyself.myphotosharingapp.PhotoSharingAppModule;
import com.example.memyself.myphotosharingapp.map.MapModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by MeMyself on 8/4/2017.
 */
@Singleton
@Component(modules = {MainModule.class, PhotoSharingAppModule.class})
public interface MainComponent {
    void inject(MainActivity activity);
}
