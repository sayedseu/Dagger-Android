package com.example.daggerdemo.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.RequestManager;
import com.example.daggerdemo.R;
import com.example.daggerdemo.model.User;
import com.example.daggerdemo.resource.AuthResource;
import com.example.daggerdemo.viewmodel.AuthViewModel;
import com.example.daggerdemo.viewmodel.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class AuthActivity extends DaggerAppCompatActivity {

    private static final String TAG = "Sayed";
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    RequestManager requestManager;
    @Inject
    Drawable drawable;
    private AuthViewModel authViewModel;
    private Button loginBT;
    private EditText idET;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        loginBT = findViewById(R.id.login_button);
        idET = findViewById(R.id.user_id_input);
        progressBar = findViewById(R.id.progress_bar);

        authViewModel = ViewModelProviders.of(this, providerFactory).get(AuthViewModel.class);

        loginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        setLogo();
        subscribeObserver();
    }

    private void subscribeObserver() {
        authViewModel.observeAuthSate().observe(this, new Observer<AuthResource<User>>() {
            @Override
            public void onChanged(AuthResource<User> userAuthResource) {
                if (userAuthResource != null) {
                    switch (userAuthResource.status) {
                        case ERROR: {
                            showingProgressBar(false);
                            Log.d(TAG, "onChanged: " + userAuthResource.message);
                            break;
                        }
                        case LOADING: {
                            showingProgressBar(true);
                            break;
                        }
                        case AUTHENTICATED: {
                            showingProgressBar(false);
                            Log.d(TAG, "onChanged: " + userAuthResource.data.getEmail());
                            onLoginSuccess();
                            break;
                        }
                        case NOT_AUTHENTICATED: {
                            showingProgressBar(false);
                            break;
                        }
                    }
                }
            }
        });
    }

    private void onLoginSuccess() {
        Log.d(TAG, "onLoginSuccess: login successful!");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showingProgressBar(boolean isShown) {
        if (isShown) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void attemptLogin() {
        String id = idET.getText().toString();
        if (TextUtils.isEmpty(id)) {
            idET.setError("");
            return;
        } else {
            authViewModel.authenticateUser(Integer.parseInt(id));
        }
    }

    private void setLogo() {
        requestManager
                .load(drawable)
                .into((ImageView) findViewById(R.id.login_logo));
    }
}
