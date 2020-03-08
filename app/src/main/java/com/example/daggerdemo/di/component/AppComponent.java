package com.example.daggerdemo.di.component;

import android.app.Application;

import com.example.daggerdemo.SessionManager;
import com.example.daggerdemo.app.BaseApplication;
import com.example.daggerdemo.di.module.ActivityBuildersModule;
import com.example.daggerdemo.di.module.AppModule;
import com.example.daggerdemo.di.module.ViewModelFactoryModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                ActivityBuildersModule.class,
                AppModule.class,
                ViewModelFactoryModule.class
        }
)
public interface AppComponent extends AndroidInjector<BaseApplication> {
    SessionManager sessionManager();

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
