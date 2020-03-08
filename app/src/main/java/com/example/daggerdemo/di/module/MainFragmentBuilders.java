package com.example.daggerdemo.di.module;

import com.example.daggerdemo.ui.PostFragment;
import com.example.daggerdemo.ui.ProfileFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuilders {

    @ContributesAndroidInjector
    abstract ProfileFragment profileFragment();

    @ContributesAndroidInjector
    abstract PostFragment postFragment();
}
