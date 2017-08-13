package com.example.memyself.myphotosharingapp.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.memyself.myphotosharingapp.FirebaseHelper;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MeMyself on 8/4/2017.
 */
@Module
public class MainModule {
    private MainView view;
    private Fragment[] fragments;
    private String[] titles;
    private FragmentManager fragmentManager;

    public MainModule(MainView view, Fragment[] fragments, String[] titles, FragmentManager fragmentManager) {
        this.view = view;
        this.fragments = fragments;
        this.titles = titles;
        this.fragmentManager = fragmentManager;
    }

    @Provides
    @Singleton
    MainView providesMainView() {
        return this.view;
    }

    @Provides
    @Singleton
    MainPresenter providesMainPresenter(MainView view, EventBus eventBus, MainModel model) {
        return new MainPresenterImpl(view, eventBus, model);
    }

    @Provides
    @Singleton
    MainModel providesMainModel(EventBus eventBus, FirebaseHelper firebaseHelper, ImageStorage storage) {
        return new MainModelImpl(eventBus, firebaseHelper, storage);
    }

    @Provides
    @Singleton
    MainPagerAdapter providesMainPagerAdapter(FragmentManager fm, Fragment[] fragments, String[] titles) {
        return new MainPagerAdapter(fm, fragments, titles);
    }

    @Provides
    @Singleton
    FragmentManager providesFragmentManager() {
        return this.fragmentManager;
    }

    @Provides
    @Singleton
    Fragment[] providesPagerAdapterFragments() {
        return this.fragments;
    }

    @Provides
    @Singleton
    String[] providesTabTitles() {
        return this.titles;
    }


}
