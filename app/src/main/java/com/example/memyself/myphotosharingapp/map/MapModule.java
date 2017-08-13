package com.example.memyself.myphotosharingapp.map;

import com.example.memyself.myphotosharingapp.FirebaseHelper;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MeMyself on 7/30/2017.
 */
@Module
public class MapModule {
    private MapView mapView;

    public MapModule(MapView mapView) {
        this.mapView = mapView;
    }

    @Provides
    @Singleton
    MapView providesMapView() {
        return this.mapView;
    }

    @Provides
    @Singleton
    MapPresenter providesMapPresenter(EventBus eventBus, MapView mapView, MapModel model) {
        return new MapPresenterImpl(eventBus, mapView, model);
    }

    @Provides
    @Singleton
    MapModel providesMapModel(EventBus eventBus, FirebaseHelper firebaseHelper) {
        return new MapModelImpl(eventBus, firebaseHelper);
    }
}
