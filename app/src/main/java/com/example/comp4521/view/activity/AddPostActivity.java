//package com.example.comp4521.view.activity;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.databinding.DataBindingUtil;
//
//import android.os.Bundle;
//
//import com.example.comp4521.R;
//import com.example.comp4521.databinding.ActivityAddPostBinding;
//import com.example.comp4521.viewmodel.AddPostViewModel;
//
//public class AddPostActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        ActivityAddPostBinding activityAddPostBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_post);
//        AddPostViewModel addPostViewModel = new AddPostViewModel(this, activityAddPostBinding);
//        addPostViewModel.setActionBar();
//        addPostViewModel.initView();
//        addPostViewModel.addClickListener();
//    }
//}
