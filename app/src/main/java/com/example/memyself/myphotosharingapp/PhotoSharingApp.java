package com.example.memyself.myphotosharingapp;

import android.app.Application;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.memyself.myphotosharingapp.login.DaggerLoginComponent;
import com.example.memyself.myphotosharingapp.login.LoginComponent;
import com.example.memyself.myphotosharingapp.login.LoginModule;
import com.example.memyself.myphotosharingapp.login.LoginView;
import com.example.memyself.myphotosharingapp.main.DaggerMainComponent;
import com.example.memyself.myphotosharingapp.main.MainComponent;
import com.example.memyself.myphotosharingapp.main.MainModule;
import com.example.memyself.myphotosharingapp.main.MainView;
import com.example.memyself.myphotosharingapp.map.DaggerMapComponent;
import com.example.memyself.myphotosharingapp.map.MapComponent;
import com.example.memyself.myphotosharingapp.map.MapModule;
import com.example.memyself.myphotosharingapp.map.MapView;
import com.example.memyself.myphotosharingapp.photoList.DaggerPhotoListComponent;
import com.example.memyself.myphotosharingapp.photoList.OnItemClickListener;
import com.example.memyself.myphotosharingapp.photoList.PhotoListComponent;
import com.example.memyself.myphotosharingapp.photoList.PhotoListModule;
import com.example.memyself.myphotosharingapp.photoList.PhotoListView;


/**
 * Created by MeMyself on 7/26/2017.
 */

public class PhotoSharingApp extends Application {
    private final static String EMAIL_KEY = "email";
    private PhotoSharingAppModule photoSharingAppModule;

    @Override
    public void onCreate() {
        super.onCreate();
        photoSharingAppModule = new PhotoSharingAppModule(this);
    }

    public static String getEmailKey() {
        return EMAIL_KEY;
    }

    public LoginComponent getLoginComponent(LoginView view) {
        return DaggerLoginComponent
                .builder()
                .photoSharingAppModule(photoSharingAppModule)
                .loginModule(new LoginModule(view))
                .build();
    }

    public MapComponent getMapComponent(MapView view, Fragment fragment) {
        photoSharingAppModule.setFragment(fragment);
        return DaggerMapComponent
                .builder()
                .photoSharingAppModule(photoSharingAppModule)
                .mapModule(new MapModule(view))
                .build();
    }

    public MainComponent getMainComponent(MainView view, Fragment[] fragments, String[] titles, FragmentManager fragmentManager) {
        return DaggerMainComponent
                .builder()
                .mainModule(new MainModule(view, fragments, titles, fragmentManager))
                .photoSharingAppModule(photoSharingAppModule)
                .build();
    }

    public PhotoListComponent getPhotoListComponent(PhotoListView view, OnItemClickListener listener, Fragment fragment) {
        photoSharingAppModule.setFragment(fragment);
        return DaggerPhotoListComponent
                .builder()
                .photoSharingAppModule(photoSharingAppModule)
                .photoListModule(new PhotoListModule(view, listener))
                .build();
    }
}
