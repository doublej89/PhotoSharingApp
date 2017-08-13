package com.example.memyself.myphotosharingapp.photoList;

import android.content.Context;
import android.location.Geocoder;

import com.example.memyself.myphotosharingapp.FirebaseHelper;
import com.example.memyself.myphotosharingapp.ImageLoader;
import com.example.memyself.myphotosharingapp.photo.Photo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MeMyself on 8/5/2017.
 */
@Module
public class PhotoListModule {
    private PhotoListView view;
    private OnItemClickListener listener;

    public PhotoListModule(PhotoListView view, OnItemClickListener listener) {
        this.view = view;
        this.listener = listener;
    }

    @Provides
    @Singleton
    PhotoListView providesListView() {
        return this.view;
    }

    @Provides
    @Singleton
    PhotoListPresenter providesPhotoListPresenter(EventBus eventBus, PhotoListView view, PhotoListModel model) {
        return new PhotoListPresenterImpl(eventBus, view, model);
    }

    @Provides
    @Singleton
    PhotoListModel providesPhotoListModel(EventBus eventBus, FirebaseHelper firebase) {
        return new PhotoListModelImpl(eventBus, firebase);
    }

    @Provides
    @Singleton
    PhotoListAdapter providesPhotoListAdapter(List<Photo> photoList, ImageLoader imageLoader, OnItemClickListener onItemClickListener, Geocoder geocoder) {
        return new PhotoListAdapter(photoList, imageLoader, onItemClickListener, geocoder);
    }

    @Provides
    @Singleton
    List<Photo> providesPhotoList() {
        return new ArrayList<Photo>();
    }

    @Provides
    @Singleton
    OnItemClickListener providesItemClickListener() {
        return this.listener;
    }

    @Provides
    @Singleton
    Geocoder providesGoecoder(Context context) {
        return new Geocoder(context);
    }

}
