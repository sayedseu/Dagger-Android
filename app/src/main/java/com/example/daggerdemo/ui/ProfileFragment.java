package com.example.daggerdemo.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.daggerdemo.R;
import com.example.daggerdemo.model.User;
import com.example.daggerdemo.resource.AuthResource;
import com.example.daggerdemo.viewmodel.ProfileViewModel;
import com.example.daggerdemo.viewmodel.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class ProfileFragment extends DaggerFragment {

    private static final String TAG = "sayed";
    @Inject
    ViewModelProviderFactory providerFactory;
    private TextView name, email, website;
    private ProfileViewModel viewModel;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated: profile fragment create successfully");
        name = view.findViewById(R.id.username);
        email = view.findViewById(R.id.email);
        website = view.findViewById(R.id.website);
        progressBar = view.findViewById(R.id.progressBar);
        viewModel = ViewModelProviders.of(this, providerFactory).get(ProfileViewModel.class);
        subscribeObserver();
    }

    private void subscribeObserver() {
        viewModel.getAuthenticatedUser().observe(getViewLifecycleOwner(), new Observer<AuthResource<User>>() {
            @Override
            public void onChanged(AuthResource<User> userAuthResource) {
                if (userAuthResource != null) {
                    switch (userAuthResource.status) {
                        case AUTHENTICATED: {
                            showingProgressBar(false);
                            setUserDetails(userAuthResource);
                            break;
                        }
                        case ERROR: {
                            showingProgressBar(false);
                            setErrorMsg();
                            break;
                        }
                        case LOADING: {
                            showingProgressBar(true);
                            break;
                        }
                    }
                }
            }
        });
    }

    private void setErrorMsg() {
        name.setText("Error");
        email.setText("Error");
        website.setText("Error");
    }

    private void setUserDetails(AuthResource<User> userAuthResource) {
        name.setText(userAuthResource.data.getUsername());
        email.setText(userAuthResource.data.getEmail());
        website.setText(userAuthResource.data.getWebsite());
    }

    private void showingProgressBar(boolean isShown) {
        if (isShown) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
