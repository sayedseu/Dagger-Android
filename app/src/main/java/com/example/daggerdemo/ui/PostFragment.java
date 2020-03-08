package com.example.daggerdemo.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daggerdemo.PostRecyclerAdapter;
import com.example.daggerdemo.R;
import com.example.daggerdemo.VerticalSpaceItemDecoration;
import com.example.daggerdemo.model.Post;
import com.example.daggerdemo.resource.Resource;
import com.example.daggerdemo.viewmodel.PostViewModel;
import com.example.daggerdemo.viewmodel.ViewModelProviderFactory;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class PostFragment extends DaggerFragment {

    private static final String TAG = "PostFragment";
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    PostRecyclerAdapter adapter;
    private PostViewModel mViewModel;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.post_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rec);
        progressBar = view.findViewById(R.id.progressBar2);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, providerFactory).get(PostViewModel.class);
        subscribeObserver();
        initRecyclerView();
    }

    private void subscribeObserver() {
        mViewModel.getUserPost().removeObservers(getViewLifecycleOwner());
        mViewModel.getUserPost().observe(getViewLifecycleOwner(), new Observer<Resource<List<Post>>>() {
            @Override
            public void onChanged(Resource<List<Post>> listResource) {
                if (listResource != null) {
                    switch (listResource.status) {
                        case LOADING: {
                            showingProgressBar(true);
                            Log.d(TAG, "onChanged: PostsFragment: LOADING...");
                            break;
                        }

                        case SUCCESS: {
                            showingProgressBar(false);
                            Log.d(TAG, "onChanged: PostsFragment: got posts.");
                            adapter.setPosts(listResource.data);
                            break;
                        }

                        case ERROR: {
                            showingProgressBar(false);
                            Log.d(TAG, "onChanged: PostsFragment: ERROR... " + listResource.message);
                            break;
                        }
                    }
                }
            }
        });
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        VerticalSpaceItemDecoration itemDecoration = new VerticalSpaceItemDecoration(15);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(adapter);
    }

    private void showingProgressBar(boolean isShown) {
        if (isShown) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}

