package com.example.comp4521.repository;

import com.example.comp4521.callback.CallBack;
import com.example.comp4521.callback.FirebaseChildCallBack;
import com.example.comp4521.firebase.FirebaseRequestModel;
import com.example.comp4521.model.Post;

import java.util.HashMap;

public interface PostRepository {
    void createPost(Post post, CallBack callBack);

    void updatePost(String postKey, HashMap map, CallBack callBack);

    void deletePost(String employeeKey, CallBack callBack);

    void readPostByKey(String postKey, CallBack callBack);

    void readPostByName(String postName, CallBack callBack);

    void readAllPostBySingleValueEvent(CallBack callBack);

    FirebaseRequestModel readAllPostByDataChangeEvent(CallBack callBack);

    FirebaseRequestModel readAllPostByChildEvent(FirebaseChildCallBack firebaseChildCallBack);
}
