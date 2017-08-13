package com.example.memyself.myphotosharingapp.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.memyself.myphotosharingapp.PhotoSharingApp;
import com.example.memyself.myphotosharingapp.R;
import com.example.memyself.myphotosharingapp.main.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginView{
    @BindView(R.id.btnSignIn)
    Button btnSignIn;
    @BindView(R.id.btnSignUp)
    Button btnSignUp;
    @BindView(R.id.editTxtEmail)
    EditText inputEmail;
    @BindView(R.id.editTxtPassword)
    EditText inputPassword;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.layoutMainContainer)
    RelativeLayout container;

    @Inject
    LoginPresenter presenter;
    @Inject
    SharedPreferences preferences;

    private PhotoSharingApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        app = (PhotoSharingApp) getApplication();
        app.getLoginComponent(this).inject(this);

        presenter.onCreate();
        presenter.login(null, null);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void enableInputs() {
        setInputs(true);
    }

    @Override
    public void disableInputs() {
        setInputs(false);
    }

    @Override
    public void showProgress() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        if (progressBar != null) progressBar.setVisibility(View.GONE);
    }

    @Override
    @OnClick(R.id.btnSignUp)
    public void handlesSignUp() {
        Log.d("LoginActivity", "Sign up click");
        presenter.signup(inputEmail.getText().toString(), inputPassword.getText().toString());
    }

    @Override
    @OnClick(R.id.btnSignIn)
    public void handlesSignIn() {
        presenter.login(inputEmail.getText().toString(), inputPassword.getText().toString());
    }

    @Override
    public void newUserSuccess() {
        Snackbar.make(container, R.string.login_notice_message_useradded, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToMainScreen() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void setUserEmail(String email) {
        if (email != null) {
            String key = PhotoSharingApp.getEmailKey();
            preferences.edit().putString(key, email).apply();
        }
    }

    @Override
    public void loginError(String error) {
        if (inputPassword != null) {
            inputPassword.setText("");
            String msgError = String.format(getString(R.string.login_error_message_signin), error);
            inputPassword.setError(msgError);
        }
    }

    @Override
    public void newUserError(String error) {
        if (inputPassword != null) {
            inputPassword.setText("");
            String msgError = String.format(getString(R.string.login_error_message_signup), error);
            inputPassword.setError(msgError);
        }
    }

    private void setInputs(boolean enabled){
        if (btnSignIn != null && inputEmail!= null && inputPassword != null) {
            btnSignIn.setEnabled(enabled);
            btnSignUp.setEnabled(enabled);
            inputEmail.setEnabled(enabled);
            inputPassword.setEnabled(enabled);
        }
    }
}
