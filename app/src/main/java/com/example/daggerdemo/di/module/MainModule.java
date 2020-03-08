package com.example.daggerdemo.di.module;

import com.example.daggerdemo.PostRecyclerAdapter;
import com.example.daggerdemo.di.scope.MainScope;
import com.example.daggerdemo.network.MainApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class MainModule {

    @MainScope
    @Provides
    static PostRecyclerAdapter postRecyclerAdapter() {
        return new PostRecyclerAdapter();
    }

    @MainScope
    @Provides
    static MainApi providesMainApi(Retrofit retrofit) {
        return retrofit.create(MainApi.class);
    }
}
