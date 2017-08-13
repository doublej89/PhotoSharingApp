package com.example.memyself.myphotosharingapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;

import com.bumptech.glide.request.RequestOptions;
import com.example.memyself.myphotosharingapp.main.ImageStorage;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MeMyself on 7/26/2017.
 */
@Module
public class PhotoSharingAppModule {
    private Application application;
    private final static String SHARED_PREFERENCES_NAME = "UserPrefs";
    private Fragment fragment;

    public PhotoSharingAppModule(Application application) {
        this.application = application;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return application.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    ImageLoader providesImageLoader(Fragment fragment, RequestOptions options) {
        ImageLoader imageLoader = new ImageLoader();
        if (fragment != null) {
            imageLoader.setLoaderContext(fragment, options);
        }
        return imageLoader;
    }

    @Provides
    @Singleton
    RequestOptions getRequestOptions() {
        return new RequestOptions();
    }

    @Provides
    @Singleton
    Context providesContext() {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return application;
    }

    @Provides
    @Singleton
    ImageStorage providesImageStorage(Context context) {
        return new ImageStorage(context);
    }

    @Provides
    @Singleton
    EventBus providesEventBus() {
        return new EventBus();
    }

    @Provides
    @Singleton
    FirebaseHelper providesFirebaseHelper() {
        return new FirebaseHelper();
    }

    @Provides
    @Singleton
    Fragment providesFragment(){
        return this.fragment;
    }

}
