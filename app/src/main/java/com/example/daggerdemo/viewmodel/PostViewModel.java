package com.example.daggerdemo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.daggerdemo.SessionManager;
import com.example.daggerdemo.model.Post;
import com.example.daggerdemo.model.User;
import com.example.daggerdemo.network.MainApi;
import com.example.daggerdemo.resource.AuthResource;
import com.example.daggerdemo.resource.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class PostViewModel extends ViewModel {

    private final SessionManager sessionManager;
    private MainApi mainApi;
    private MediatorLiveData<Resource<List<Post>>> posts;

    @Inject
    public PostViewModel(SessionManager sessionManager, MainApi mainApi) {
        this.sessionManager = sessionManager;
        this.mainApi = mainApi;
    }

    public LiveData<AuthResource<User>> getAuthenticatedUser() {
        return sessionManager.getAuthUser();
    }

    public LiveData<Resource<List<Post>>> getUserPost() {
        if (posts == null) {
            posts = new MediatorLiveData<>();
            posts.setValue(Resource.loading(null));
            LiveData<Resource<List<Post>>> source = queryPostByUser(sessionManager.getAuthUser().getValue().data.getId());
            posts.addSource(source, new Observer<Resource<List<Post>>>() {
                @Override
                public void onChanged(Resource<List<Post>> listResource) {
                    posts.setValue(listResource);
                    posts.removeSource(source);
                }
            });
        }
        return posts;
    }

    private LiveData<Resource<List<Post>>> queryPostByUser(int id) {
        return LiveDataReactiveStreams.fromPublisher(
                mainApi.getPostsFromUser(id)
                        .onErrorReturn(new Function<Throwable, List<Post>>() {
                            @Override
                            public List<Post> apply(Throwable throwable) throws Exception {
                                Post post = new Post();
                                post.setId(-1);
                                List<Post> posts = new ArrayList<>();
                                posts.add(post);
                                return posts;
                            }
                        })
                        .map(new Function<List<Post>, Resource<List<Post>>>() {
                            @Override
                            public Resource<List<Post>> apply(List<Post> posts) throws Exception {
                                if (posts.size() > 0) {
                                    if (posts.get(0).getId() == -1) {
                                        return Resource.error("Something went wrong", null);
                                    }
                                }
                                return Resource.success(posts);
                            }
                        })
                        .subscribeOn(Schedulers.io())
        );
    }
}
