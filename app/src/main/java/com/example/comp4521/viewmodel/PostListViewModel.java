package com.example.comp4521.viewmodel;


import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp4521.R;
import com.example.comp4521.callback.FirebaseChildCallBack;
import com.example.comp4521.databinding.FragmentMissingPetsBinding;
import com.example.comp4521.firebase.FirebaseRequestModel;
import com.example.comp4521.firebase.FirebaseUtility;
import com.example.comp4521.helper.IndexedLinkedHashMap;
import com.example.comp4521.model.Post;
import com.example.comp4521.repository.PostRepository;
import com.example.comp4521.repository.impl.PostRepositoryImpl;
import com.example.comp4521.utility.Utility;
import com.example.comp4521.view.activity.AddPostActivity;
//import com.example.comp4521.view.activity.PostListActivity;
import com.example.comp4521.view.activity.PostListActivity;
import com.example.comp4521.view.adapter.PostDetailsAdapter;

import static com.example.comp4521.constants.Constant.ADD;
import static com.example.comp4521.constants.Constant.DELETE;
import static com.example.comp4521.constants.Constant.UPDATE;


public class PostListViewModel {
    private static final String TAG = "PostListViewModel";
    private FragmentMissingPetsBinding activityPostListBinding;
    private PostListActivity postListActivity;
    private PostDetailsAdapter postDetailsAdapter;
    private PostRepository postRepository;
    private FirebaseRequestModel firebaseRequestModel;

    public PostListViewModel(PostListActivity postListActivity, FragmentMissingPetsBinding activityPostListBinding) {
        this.activityPostListBinding = activityPostListBinding;
        this.postListActivity = postListActivity;
        postRepository = new PostRepositoryImpl(postListActivity);
    }

    public void setActionBar() {
        //Toolbar tb = activityPostListBinding.toolbar;
       // postListActivity.setSupportActionBar(tb);
        ActionBar actionBar = postListActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(R.string.app_name);
        }
        /*tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postListActivity.onBackPressed();
            }
        });*/
    }

    public void init() {
        Log.w(TAG, "start to do init()");
        getAllPosts();
    }

    private void setAdapter(IndexedLinkedHashMap<String, Post> postIndexedLinkedHashMap) {
        Log.w(TAG, "start to do setAdapter()");
        if (postDetailsAdapter == null) {
            postDetailsAdapter = new PostDetailsAdapter(postListActivity, postIndexedLinkedHashMap);
            activityPostListBinding.rvEmployeeList.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(postListActivity);
            activityPostListBinding.rvEmployeeList.setLayoutManager(mLayoutManager);
            activityPostListBinding.rvEmployeeList.setItemAnimator(new DefaultItemAnimator());
            activityPostListBinding.rvEmployeeList.setAdapter(postDetailsAdapter);
        } else {
            postDetailsAdapter.reloadList(postIndexedLinkedHashMap);
        }
    }

    private void getAllPosts() {
        Log.w(TAG, "start to do getAllPosts()");
        if (firebaseRequestModel != null)
            removeListener();
        Log.w(TAG, "start to do getAllPosts()");
        firebaseRequestModel = postRepository.readAllPostByChildEvent(new FirebaseChildCallBack() {
            @Override
            public void onChildAdded(Object object) {
                if (object != null) {
                    Post post = (Post) object;
                    if (postDetailsAdapter == null)
                        setAdapter(new IndexedLinkedHashMap<String, Post>());
                    postDetailsAdapter.getPostList().add(post.getPostID(), post);
                    postDetailsAdapter.reloadList(postDetailsAdapter.getPostList().size() - 1, ADD);
                }
            }

            @Override
            public void onChildChanged(Object object) {
                if (object != null) {
                    Post post = (Post) object;
                    postDetailsAdapter.getPostList().update(post.getPostID(), post);
                    postDetailsAdapter.reloadList(postDetailsAdapter.getPostList().getIndexByKey(post.getPostID()), UPDATE);
                }
            }

            @Override
            public void onChildRemoved(Object object) {
                if (object != null) {
                    Post post = (Post) object;
                    postDetailsAdapter.getPostList().update(post.getPostID(), post);
                    postDetailsAdapter.reloadList(postDetailsAdapter.getPostList().getIndexByKey(post.getPostID()), DELETE);
                }
            }

            @Override
            public void onChildMoved(Object object) {

            }

            @Override
            public void onCancelled(Object object) {
                Utility.showMessage(postListActivity, postListActivity.getString(R.string.some_thing_went_wrong));
            }
        });
    }

    public void removeListener() {
        Log.w(TAG, "start to do setAdapter()");
        FirebaseUtility.removeFireBaseChildListener(postListActivity, firebaseRequestModel);
    }

}
