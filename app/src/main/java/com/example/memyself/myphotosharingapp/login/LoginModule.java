package com.example.memyself.myphotosharingapp.login;

import com.example.memyself.myphotosharingapp.FirebaseHelper;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MeMyself on 7/26/2017.
 */
@Module
public class LoginModule {
    private LoginView view;

    public LoginModule(LoginView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    LoginView providesLoginView() {
        return this.view;
    }

    @Provides @Singleton
    LoginPresenter providesLoginPresenter(EventBus eventBus, LoginView loginView, LoginModel repository) {
        return new LoginPresenterImpl(eventBus, loginView, repository);
    }


    @Provides @Singleton
    LoginModel providesLoginRepository(EventBus eventBus, FirebaseHelper firebase) {
        return new LoginModelImpl(eventBus, firebase);
    }
}
