package com.example.daggerdemo.di.module;

import com.example.daggerdemo.di.scope.AuthScope;
import com.example.daggerdemo.network.AuthApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class AuthModule {

    @AuthScope
    @Provides
    static AuthApi providesAuthApi(Retrofit retrofit) {
        return retrofit.create(AuthApi.class);
    }

}
