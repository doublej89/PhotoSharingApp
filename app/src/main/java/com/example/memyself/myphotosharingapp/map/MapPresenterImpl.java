package com.example.memyself.myphotosharingapp.map;

import com.example.memyself.myphotosharingapp.PhotoEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by MeMyself on 7/29/2017.
 */

public class MapPresenterImpl implements MapPresenter {
    private EventBus eventBus;
    private MapView mapView;
    private MapModel model;

    public MapPresenterImpl(EventBus eventBus, MapView mapView, MapModel model) {
        this.eventBus = eventBus;
        this.mapView = mapView;
        this.model = model;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        this.mapView = null;
        eventBus.unregister(this);
    }

    @Override
    public void subscribe() {
        model.subscribe();
    }

    @Override
    public void unsubscribe() {
        model.unsubscribe();
    }

    @Override
    @Subscribe
    public void onEventMainThread(PhotoEvent event) {
        if (this.mapView != null) {
            String error = event.getError();
            if (error != null) {
                mapView.onPhotosError(error);
            } else {
                if (event.getType() == PhotoEvent.READ_EVENT) {
                    mapView.addPhoto(event.getPhoto());
                } else if (event.getType() == PhotoEvent.DELETE_EVENT) {
                    mapView.removePhoto(event.getPhoto());
                }
            }
        }
    }
}
