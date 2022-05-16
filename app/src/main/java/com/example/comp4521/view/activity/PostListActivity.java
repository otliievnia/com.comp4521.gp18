package com.example.comp4521.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.comp4521.R;
import com.example.comp4521.databinding.FragmentMissingPetsBinding;
import com.example.comp4521.viewmodel.PostListViewModel;

public class PostListActivity extends AppCompatActivity {
    PostListViewModel postListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentMissingPetsBinding activityPostListBinding = DataBindingUtil.setContentView(this, R.layout.fragment_missing_pets);
        postListViewModel = new PostListViewModel(this, activityPostListBinding);
        postListViewModel.setActionBar();
        postListViewModel.init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        postListViewModel.removeListener();
    }
}
