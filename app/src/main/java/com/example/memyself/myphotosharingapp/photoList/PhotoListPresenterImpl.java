package com.example.memyself.myphotosharingapp.photoList;

import com.example.memyself.myphotosharingapp.PhotoEvent;
import com.example.memyself.myphotosharingapp.photo.Photo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by MeMyself on 7/31/2017.
 */

public class PhotoListPresenterImpl implements PhotoListPresenter {
    private EventBus eventBus;
    private PhotoListView view;
    private PhotoListModel model;
    private final static String EMPTY_LIST = "Empty list";

    public PhotoListPresenterImpl(EventBus eventBus, PhotoListView view, PhotoListModel model) {
        this.eventBus = eventBus;
        this.view = view;
        this.model = model;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        this.view = null;
        eventBus.unregister(this);
    }

    @Override
    public void subscribe() {
        if (view != null){
            view.hideList();
            view.showProgress();
        }
        model.subscribe();
    }

    @Override
    public void unsubscribe() {
        model.unSubscribe();
    }

    @Override
    public void removePhoto(Photo photo) {
        model.remove(photo);
    }

    @Override
    @Subscribe
    public void onEventMainThread(PhotoEvent event) {
        if (view != null){
            view.hideProgress();
            view.showList();
        }
        String error = event.getError();
        if (error != null) {
            if (error.isEmpty()) {
                view.onPhotosError(EMPTY_LIST);
            } else {
                view.onPhotosError(error);
            }
        } else {
            if (event.getType() == PhotoEvent.READ_EVENT) {
                view.addPhoto(event.getPhoto());
            } else if (event.getType() == PhotoEvent.DELETE_EVENT) {
                view.removePhoto(event.getPhoto());
            }
        }
    }
}
