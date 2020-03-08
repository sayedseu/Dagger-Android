package com.example.daggerdemo.di.module;

import androidx.lifecycle.ViewModel;

import com.example.daggerdemo.di.key.ViewModelKey;
import com.example.daggerdemo.viewmodel.AuthViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AuthViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel.class)
    public abstract ViewModel provideAuthViewModel(AuthViewModel authViewModel);

}
