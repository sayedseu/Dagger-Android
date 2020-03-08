package com.example.daggerdemo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.daggerdemo.SessionManager;
import com.example.daggerdemo.model.User;
import com.example.daggerdemo.network.AuthApi;
import com.example.daggerdemo.resource.AuthResource;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class AuthViewModel extends ViewModel {

    private static final String TAG = "AuthViewModel";
    private SessionManager sessionManager;
    private AuthApi authApi;
    private MediatorLiveData<AuthResource<User>> authUser = new MediatorLiveData<>();

    @Inject
    public AuthViewModel(SessionManager sessionManager, AuthApi authApi) {
        this.sessionManager = sessionManager;
        this.authApi = authApi;
    }

    public void authenticateUser(int id) {
        sessionManager.authenticateUserWithId(queryUserById(id));
    }

    private LiveData<AuthResource<User>> queryUserById(int id) {
        return LiveDataReactiveStreams.fromPublisher(
                authApi.getUser(id)
                        .onErrorReturn(new Function<Throwable, User>() {
                            @Override
                            public User apply(Throwable throwable) throws Exception {
                                User errorUser = new User();
                                errorUser.setId(-1);
                                return errorUser;
                            }
                        })
                        .map(new Function<User, AuthResource<User>>() {
                            @Override
                            public AuthResource<User> apply(User user) throws Exception {
                                if (user.getId() == -1) {
                                    return AuthResource.error("Authentication Failed", null);
                                }
                                return AuthResource.authenticated(user);
                            }
                        })
                        .subscribeOn(Schedulers.io())
        );
    }

    public LiveData<AuthResource<User>> observeAuthSate() {
        return sessionManager.getAuthUser();
    }
}
