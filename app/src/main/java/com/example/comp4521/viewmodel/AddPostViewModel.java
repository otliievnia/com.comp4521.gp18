package com.example.comp4521.viewmodel;

import android.util.Log;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.example.comp4521.R;
import com.example.comp4521.callback.CallBack;
import com.example.comp4521.constants.Constant;
import com.example.comp4521.databinding.ActivityAddPostBinding;
import com.example.comp4521.model.Image;
import com.example.comp4521.model.Post;
import com.example.comp4521.repository.PostRepository;
import com.example.comp4521.repository.impl.PostRepositoryImpl;
import com.example.comp4521.utility.Utility;
import com.example.comp4521.view.activity.AddPostActivity;

import java.sql.Timestamp;

public class AddPostViewModel {
    private static final String TAG = "AddPostViewModel";
    private AddPostActivity addPostActivity;
    private ActivityAddPostBinding activityAddPostBinding;
    private PostRepository postRepository;
    private Post post;
    private Object post_1;

    public AddPostViewModel(AddPostActivity addPostActivity, ActivityAddPostBinding activityAddPostBinding) {
        this.addPostActivity = addPostActivity;
        this.activityAddPostBinding = activityAddPostBinding;
        postRepository = new PostRepositoryImpl(addPostActivity);
    }

    public void setActionBar() {
        Log.w(TAG, "start to do setActionBar()");
        Toolbar tb = activityAddPostBinding.toolbar;
        addPostActivity.setSupportActionBar(tb);
        ActionBar actionBar = addPostActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(R.string.app_name);
        }
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPostActivity.onBackPressed();
            }
        });
    }

    public void initView() {
        Log.w(TAG, "start to do initView()");
        post = (Post) addPostActivity.getIntent().getSerializableExtra(Constant.EMPLOYEE_MODEL);
        if (post != null) {


            activityAddPostBinding.btSave.setText(addPostActivity.getText(R.string.update));
            activityAddPostBinding.detailSimilarity.setText(post.getSimilarity());
            activityAddPostBinding.detailType.setText(post.getAnimalType());
            activityAddPostBinding.detailBreed.setText(post.getBreed());
            activityAddPostBinding.detailSex.setText(post.getSex());
            activityAddPostBinding.detailName.setText(post.getName());
            activityAddPostBinding.detailLocation.setText(post.getLocation());
            activityAddPostBinding.detailPostDate.setText(post.convertTime(post.getCreatedDateTimeLong()));
            activityAddPostBinding.detailDescriptions.setText(post.getDescriptions());

            activityAddPostBinding.detailSimilarity.setKeyListener(null);
            activityAddPostBinding.detailType.setKeyListener(null);
            activityAddPostBinding.detailBreed.setKeyListener(null);
            activityAddPostBinding.detailSex.setKeyListener(null);
            activityAddPostBinding.detailName.setKeyListener(null);
            activityAddPostBinding.detailLocation.setKeyListener(null);
            activityAddPostBinding.detailPostDate.setKeyListener(null);
            activityAddPostBinding.detailDescriptions.setKeyListener(null);
            activityAddPostBinding.btSave.setVisibility(View.GONE); // TODO: enable for user manual update
            if (post.getImages() != null) {
                /*activityAddEmployeeBinding.etBranchId.setText(post.getBranchDetails().getBranchId().replace(Constant.BR,""));
                activityAddEmployeeBinding.etBranchName.setText(post.getBranchDetails().getBranchName());
                activityAddEmployeeBinding.etBranchLocation.setText(post.getBranchDetails().getBranchLocation());*/
            }
        }else{
            activityAddPostBinding.detailSimilarity.setVisibility(View.GONE);
            activityAddPostBinding.detailPostDate.setVisibility(View.GONE);
        }
    }

    public void addClickListener() {

        activityAddPostBinding.btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    Post postModel = fillPostDetails();
                    if (post == null) {
                        Log.w(TAG, "start to do saveData()");
                        saveData(postModel);
                    }else {
                        Log.w(TAG, "start to do updateData()");
                        updateData(postModel);
                    }
                }
            }
        });
    }

    private void saveData(Post post) {
        Log.w(TAG, "inside saveData()");
        postRepository.createPost(post, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                addPostActivity.finish();
            }

            @Override
            public void onError(Object object) {
                Utility.showMessage(addPostActivity, addPostActivity.getString(R.string.some_thing_went_wrong));
            }
        });
    }

    private void updateData(Post post) {
        Log.w(TAG, "inside updateData()");
        postRepository.updatePost(this.post.getPostID(), post.getMap(), new CallBack() {
            @Override
            public void onSuccess(Object object) {
                addPostActivity.finish();
            }

            @Override
            public void onError(Object object) {
                Utility.showMessage(addPostActivity, addPostActivity.getString(R.string.some_thing_went_wrong));
            }
        });
    }

    private boolean validate() {
        boolean isValid = true;
        if (Utility.isEmptyOrNull(activityAddPostBinding.detailDescriptions.getText().toString())) {
            activityAddPostBinding.detailDescriptions.setError(addPostActivity.getString(R.string.descriptions_error));
            isValid = false;
        }
        if (Utility.isEmptyOrNull(activityAddPostBinding.detailType.getText().toString())){
            activityAddPostBinding.detailType.setError(addPostActivity.getString(R.string.type_error));
            isValid = false;
        }
        if (Utility.isEmptyOrNull(activityAddPostBinding.detailBreed.getText().toString())){
            activityAddPostBinding.detailBreed.setError(addPostActivity.getString(R.string.breed_error));
            isValid = false;
        }
        if (Utility.isEmptyOrNull(activityAddPostBinding.detailSex.getText().toString())){
            activityAddPostBinding.detailSex.setError(addPostActivity.getString(R.string.sex_error));
            isValid = false;
        }
        if (Utility.isEmptyOrNull(activityAddPostBinding.detailName.getText().toString())){
            activityAddPostBinding.detailName.setError(addPostActivity.getString(R.string.name_error));
            isValid = false;
        }
        if (Utility.isEmptyOrNull(activityAddPostBinding.detailLocation.getText().toString())){
            activityAddPostBinding.detailLocation.setError(addPostActivity.getString(R.string.location_error));
            isValid = false;
        }
        return isValid;
    }

    private Post fillPostDetails() {
        Post post = new Post();
        post.setAnimalType(activityAddPostBinding.detailType.getText().toString());
        post.setBreed(activityAddPostBinding.detailBreed.getText().toString());
        post.setSex(activityAddPostBinding.detailSex.getText().toString());
        post.setName(activityAddPostBinding.detailName.getText().toString());
        post.setLocation(activityAddPostBinding.detailLocation.getText().toString());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        post.setCreatedDateTime(timestamp.getTime());
        post.setDescriptions(activityAddPostBinding.detailDescriptions.getText().toString());

        if (this.post == null)
            post.setPostID(Utility.getNewId());
        Image images = new Image();
        /*branchDetails.setBranchName(activityAddEmployeeBinding.etBranchName.getText().toString());
        branchDetails.setBranchId(Constant.BR + activityAddEmployeeBinding.etBranchId.getText().toString());
        branchDetails.setBranchLocation(activityAddEmployeeBinding.etBranchLocation.getText().toString());*/
        post.setImages(images);
        return post;
    }
}
