package com.example.comp4521.repository;

import com.example.comp4521.callback.CallBack;
import com.example.comp4521.callback.FirebaseChildCallBack;
import com.example.comp4521.firebase.FirebaseRequestModel;
import com.example.comp4521.model.FavPost;

import java.util.HashMap;

public interface FavPostRepository {
    void createFavPost(FavPost post, CallBack callBack);

    void deleteFavPost(String postKey, CallBack callBack);

    void readFavPostByKey(String postKey, CallBack callBack);

    void readFavPostByUserID(String userID, CallBack callBack);

    FirebaseRequestModel readAllPostByDataChangeEvent(CallBack callBack);

    FirebaseRequestModel readAllPostByChildEvent(FirebaseChildCallBack firebaseChildCallBack);
}
