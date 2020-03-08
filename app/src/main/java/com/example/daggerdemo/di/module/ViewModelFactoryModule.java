package com.example.daggerdemo.di.module;

import androidx.lifecycle.ViewModelProvider;

import com.example.daggerdemo.viewmodel.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelProviderFactory(ViewModelProviderFactory viewModelProviderFactory);
}
