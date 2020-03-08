package com.example.daggerdemo.di.module;

import androidx.lifecycle.ViewModel;

import com.example.daggerdemo.di.key.ViewModelKey;
import com.example.daggerdemo.viewmodel.PostViewModel;
import com.example.daggerdemo.viewmodel.ProfileViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelModules {

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel.class)
    abstract ViewModel bindProfileViewModel(ProfileViewModel profileViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PostViewModel.class)
    abstract ViewModel bindPostViewModel(PostViewModel postViewModel);
}
