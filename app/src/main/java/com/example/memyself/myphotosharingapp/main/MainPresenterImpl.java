package com.example.memyself.myphotosharingapp.main;

import android.location.Location;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by MeMyself on 8/1/2017.
 */

public class MainPresenterImpl implements MainPresenter {
    private MainView view;
    private EventBus eventBus;
    private MainModel model;

    public MainPresenterImpl(MainView view, EventBus eventBus, MainModel model) {
        this.view = view;
        this.eventBus = eventBus;
        this.model = model;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        view = null;
        eventBus.unregister(this);
    }

    @Override
    public void logout() {
        model.logout();
    }

    @Override
    public void uploadPhoto(Location location, String path) {
        model.uploadPhoto(location, path);
    }

    @Override
    @Subscribe
    public void onEventMainThread(MainEvent event) {
        String error = event.getError();
        if (this.view != null) {
            switch (event.getType()) {
                case MainEvent.UPLOAD_INIT:
                    view.onUploadInit();
                    break;
                case MainEvent.UPLOAD_COMPLETE:
                    view.onUploadComplete();
                    break;
                case MainEvent.UPLOAD_ERROR:
                    view.onUploadError(error);
                    break;
            }
        }
    }
}
