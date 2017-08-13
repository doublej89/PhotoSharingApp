package com.example.memyself.myphotosharingapp.login;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by MeMyself on 7/26/2017.
 */

public class LoginPresenterImpl implements LoginPresenter {
    private EventBus eventBus;
    private LoginView loginView;
    private LoginModel repository;

    public LoginPresenterImpl(EventBus eventBus, LoginView loginView, LoginModel repository) {
        this.eventBus = eventBus;
        this.loginView = loginView;
        this.repository = repository;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        loginView = null;
        eventBus.unregister(this);
    }

    @Override
    @Subscribe
    public void onEventMainThread(LoginEvent event) {
        switch (event.getEventType()) {
            case LoginEvent.onSignInError:
                if (loginView != null) {
                    loginView.hideProgress();
                    loginView.enableInputs();
                    loginView.loginError(event.getErrorMesage());
                }
                break;
            case LoginEvent.onSignInSuccess:
                if (loginView != null) {
                    loginView.setUserEmail(event.getLoggedUserEmail());
                    loginView.navigateToMainScreen();
                }
                break;
            case LoginEvent.onSignUpError:
                if (loginView != null) {
                    loginView.hideProgress();
                    loginView.enableInputs();
                    loginView.newUserError(event.getErrorMesage());
                }
                break;
            case LoginEvent.onSignUpSuccess:
                if (loginView != null) {
                    loginView.newUserSuccess();
                }
                break;
            case LoginEvent.onFailedToRecoverSession:
                if (loginView != null) {
                    loginView.hideProgress();
                    loginView.enableInputs();
                }
                break;
        }
    }

    @Override
    public void login(String email, String password) {
        if (loginView != null) {
            loginView.disableInputs();
            loginView.showProgress();
        }

        repository.signIn(email, password);
    }

    @Override
    public void signup(String email, String password) {
        if (loginView != null) {
            loginView.disableInputs();
            loginView.showProgress();
        }

        repository.signUp(email, password);
    }
}
