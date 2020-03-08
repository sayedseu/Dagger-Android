package com.example.daggerdemo.di.module;

import com.example.daggerdemo.di.scope.AuthScope;
import com.example.daggerdemo.di.scope.MainScope;
import com.example.daggerdemo.ui.AuthActivity;
import com.example.daggerdemo.ui.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
            modules = {
                    AuthViewModelModule.class,
                    AuthModule.class
            }
    )
    abstract AuthActivity authActivity();

    @MainScope
    @ContributesAndroidInjector(
            modules = {
                    MainFragmentBuilders.class,
                    MainViewModelModules.class,
                    MainModule.class
            }
    )
    abstract MainActivity mainActivity();

}
