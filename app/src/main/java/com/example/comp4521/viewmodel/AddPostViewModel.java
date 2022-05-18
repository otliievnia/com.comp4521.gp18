package com.example.comp4521.viewmodel;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.example.comp4521.CreatePetPost;
import com.example.comp4521.R;
import com.example.comp4521.callback.CallBack;
import com.example.comp4521.model.Post;
import com.example.comp4521.repository.PostRepository;
import com.example.comp4521.repository.impl.PostRepositoryImplActivity;
import com.example.comp4521.utility.Utility;

public class AddPostViewModel {
    private Activity addPostActivity;
    private PostRepository postRepository;
    private Post post;
    private Object post_1;

    public AddPostViewModel(CreatePetPost postActivity) {
        this.addPostActivity = postActivity;
        this.postRepository = new PostRepositoryImplActivity(addPostActivity);
    }


    public void addClickListener(Post post, String action) {
        //Post postModel = fillPostDetails();
        if (action.equals("post")) {
            saveData(post);
        } else {
            // updateData(postModel);
        }


    }

    private void saveData(Post post) {
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
//
//    private void updateData(Post post) {
//        Log.w(TAG, "inside updateData()");
//        postRepository.updatePost(this.post.getPostID(), post.getMap(), new CallBack() {
//            @Override
//            public void onSuccess(Object object) {
//                addPostActivity.finish();
//            }
//
//            @Override
//            public void onError(Object object) {
//                Utility.showMessage(addPostActivity, addPostActivity.getString(R.string.some_thing_went_wrong));
//            }
//        });
//    }
//

//
//    private Post fillPostDetails() {
//        Post post = new Post();
//        post.setAnimalType(activityAddPostBinding.detailType.getText().toString());
//        post.setBreed(activityAddPostBinding.detailBreed.getText().toString());
//        post.setSex(activityAddPostBinding.detailSex.getText().toString());
//        post.setName(activityAddPostBinding.detailName.getText().toString());
//        post.setLocation(activityAddPostBinding.detailLocation.getText().toString());
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        post.setCreatedDateTime(timestamp.getTime());
//        post.setDescriptions(activityAddPostBinding.detailDescriptions.getText().toString());
//
//        if (this.post == null)
//            post.setPostID(Utility.getNewId());
//        Image images = new Image();
//        /*branchDetails.setBranchName(activityAddEmployeeBinding.etBranchName.getText().toString());
//        branchDetails.setBranchId(Constant.BR + activityAddEmployeeBinding.etBranchId.getText().toString());
//        branchDetails.setBranchLocation(activityAddEmployeeBinding.etBranchLocation.getText().toString());*/
//        post.setImages(images);
//        return post;
//    }
}
