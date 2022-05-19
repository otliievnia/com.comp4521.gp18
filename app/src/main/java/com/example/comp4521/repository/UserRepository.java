package com.example.comp4521.repository;

import com.example.comp4521.callback.CallBack;
import com.example.comp4521.callback.FirebaseChildCallBack;
import com.example.comp4521.firebase.FirebaseRequestModel;
import com.example.comp4521.model.User;

import java.util.HashMap;

public interface UserRepository {
    void createUser(User user, CallBack callBack);

    void updateUser(String userIdKey, HashMap map, CallBack callBack);

    void deleteUser(String userIdKey, CallBack callBack);

    void readUserByKey(String userIdKey, CallBack callBack);

    void readUserByUserEmail(String userEmail, CallBack callBack);

    void readAllUserBySingleValueEvent(CallBack callBack);

    FirebaseRequestModel readAllUserByDataChangeEvent(CallBack callBack);

    FirebaseRequestModel readAllUserByChildEvent(FirebaseChildCallBack firebaseChildCallBack);
}
