package com.example.comp4521.repository.impl;

import static com.example.comp4521.constants.Constant.FAIL;
import static com.example.comp4521.constants.Constant.SUCCESS;
import static com.example.comp4521.firebase.FirebaseConstants.USER_TABLE;
import static com.example.comp4521.firebase.FirebaseDatabaseReference.DATABASE;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import com.example.comp4521.R;
import com.example.comp4521.callback.CallBack;
import com.example.comp4521.callback.FirebaseChildCallBack;
import com.example.comp4521.firebase.FirebaseRepository;
import com.example.comp4521.firebase.FirebaseRequestModel;
import com.example.comp4521.model.User;
import com.example.comp4521.repository.UserRepository;
import com.example.comp4521.utility.Utility;
import com.example.comp4521.view.dialog.ProgressDialogClass;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class UserRepositoryImpl extends FirebaseRepository implements UserRepository {
    private ProgressDialogClass progressDialog;
    private Fragment fragment;
    private DatabaseReference dbReference;
    private Activity activity;

    public UserRepositoryImpl(Fragment fragment) {
        this.fragment = fragment;
        progressDialog = new ProgressDialogClass(fragment.getActivity());
        dbReference = DATABASE.getReference(USER_TABLE);
    }

    public UserRepositoryImpl(Activity activity) {
        this.activity = activity;
        progressDialog = new ProgressDialogClass(activity);
        dbReference = DATABASE.getReference(USER_TABLE);
    }

    @Override
    public void createUser(User user, final CallBack callBack) {
        String pushKey = dbReference.push().getKey();
        if (user != null && !Utility.isEmptyOrNull(pushKey)) {
            progressDialog.showDialog(getString(R.string.loading), getString(R.string.please_wait));
            user.setUserId(pushKey);
            DatabaseReference databaseReference = dbReference.child(pushKey);
            fireBaseCreate(databaseReference, user, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    progressDialog.dismissDialog();
                    callBack.onSuccess(SUCCESS);
                }

                @Override
                public void onError(Object object) {
                    progressDialog.dismissDialog();
                    callBack.onError(object);
                }
            });
        } else {
            callBack.onError(FAIL);
        }
    }

    @Override
    public void updateUser(String userIdKey, HashMap map, final CallBack callBack) {
        if (!Utility.isEmptyOrNull(userIdKey)) {
            progressDialog.showDialog(getString(R.string.loading), getString(R.string.please_wait));
            DatabaseReference databaseReference = dbReference.child(userIdKey);
            fireBaseUpdateChildren(databaseReference, map, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    progressDialog.dismissDialog();
                    callBack.onSuccess(SUCCESS);
                }

                @Override
                public void onError(Object object) {
                    progressDialog.dismissDialog();
                    callBack.onError(object);
                }
            });
        } else {
            callBack.onError(FAIL);
        }
    }

    @Override
    public void deleteUser(String userIdKey, final CallBack callBack) {
        if (!Utility.isEmptyOrNull(userIdKey)) {
            progressDialog.showDialog(getString(R.string.loading), getString(R.string.please_wait));
            DatabaseReference databaseReference = dbReference.child(userIdKey);
            fireBaseDelete(databaseReference, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    progressDialog.dismissDialog();
                    callBack.onSuccess(SUCCESS);
                }

                @Override
                public void onError(Object object) {
                    progressDialog.dismissDialog();
                    callBack.onError(object);
                }
            });
        } else {
            callBack.onError(FAIL);
        }
    }

    @Override
    public void readUserByKey(String userIdKey, final CallBack callBack) {
        if (!Utility.isEmptyOrNull(userIdKey)) {
            progressDialog.showDialog(getString(R.string.loading), getString(R.string.please_wait));
            Query query = dbReference.child(userIdKey);
            fireBaseReadData(query, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    if (object != null) {
                        DataSnapshot dataSnapshot = (DataSnapshot) object;
                        if (dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                            User user = dataSnapshot.getValue(User.class);
                            callBack.onSuccess(user);
                        } else
                            callBack.onSuccess(null);
                    } else
                        callBack.onSuccess(null);
                    progressDialog.dismissDialog();
                }

                @Override
                public void onError(Object object) {
                    progressDialog.dismissDialog();
                    callBack.onError(object);
                }
            });
        } else {
            callBack.onError(FAIL);
        }
    }

    @Override
    public void readUserByUserEmail(String userEmail, final CallBack callBack) {
        if (!Utility.isEmptyOrNull(userEmail)) {
            progressDialog.showDialog(getString(R.string.loading), getString(R.string.please_wait));
            Query query = dbReference.orderByChild("userEmail").equalTo(userEmail);
            fireBaseReadData(query, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    if (object != null) {
                        DataSnapshot dataSnapshot = (DataSnapshot) object;
                        /*
                         *Here we assume that empName is unique
                         * else the parsing technique will be different
                         */
                        if (dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                            DataSnapshot child = dataSnapshot.getChildren().iterator().next();
                            User user = child.getValue(User.class);
                            callBack.onSuccess(user);
                        } else
                            callBack.onSuccess(null);
                    } else
                        callBack.onSuccess(null);
                    progressDialog.dismissDialog();
                }

                @Override
                public void onError(Object object) {
                    progressDialog.dismissDialog();
                    callBack.onError(object);
                }
            });
        } else {
            callBack.onError(FAIL);
        }
    }

    @Override
    public void readAllUserBySingleValueEvent(final CallBack callBack) {
        progressDialog.showDialog(getString(R.string.loading), getString(R.string.please_wait));
        Query query = dbReference.orderByChild("userEmail");
        fireBaseReadData(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                        ArrayList<User> userArrayList = new ArrayList<>();
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            User user = suggestionSnapshot.getValue(User.class);
                            userArrayList.add(user);
                        }
                        callBack.onSuccess(userArrayList);
                    } else
                        callBack.onSuccess(null);
                } else
                    callBack.onSuccess(null);
                progressDialog.dismissDialog();
            }

            @Override
            public void onError(Object object) {
                progressDialog.dismissDialog();
                callBack.onError(object);
            }
        });
    }

    @Override
    public FirebaseRequestModel readAllUserByDataChangeEvent(final CallBack callBack) {
        progressDialog.showDialog(getString(R.string.loading), getString(R.string.please_wait));
        Query query = dbReference.orderByChild("userEmail");
        ValueEventListener valueEventListener = fireBaseDataChangeListener(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                        ArrayList<User> postArrayList = new ArrayList<>();
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            User user = suggestionSnapshot.getValue(User.class);
                            postArrayList.add(user);
                        }
                        callBack.onSuccess(postArrayList);
                    } else
                        callBack.onSuccess(null);
                } else
                    callBack.onSuccess(null);
                progressDialog.dismissDialog();
            }

            @Override
            public void onError(Object object) {
                progressDialog.dismissDialog();
                callBack.onError(object);
            }
        });
        return new FirebaseRequestModel(valueEventListener, query);
    }

    @Override
    public FirebaseRequestModel readAllUserByChildEvent(final FirebaseChildCallBack firebaseChildCallBack) {
        progressDialog.showDialog(getString(R.string.loading), getString(R.string.please_wait));
        //get all posts order by created date time
        Query query = dbReference.orderByChild("createdDateTime");
        ChildEventListener childEventListener = fireBaseChildEventListener(query, new FirebaseChildCallBack() {
                    @Override
                    public void onChildAdded(Object object) {
                        DataSnapshot dataSnapshot = (DataSnapshot) object;
                        if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                            User user = dataSnapshot.getValue(User.class);
                            firebaseChildCallBack.onChildAdded(user);
                        }
                        progressDialog.dismissDialog();
                    }

                    @Override
                    public void onChildChanged(Object object) {
                        DataSnapshot dataSnapshot = (DataSnapshot) object;
                        if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                            User user = dataSnapshot.getValue(User.class);
                            firebaseChildCallBack.onChildChanged(user);
                        }
                    }

                    @Override
                    public void onChildRemoved(Object object) {
                        DataSnapshot dataSnapshot = (DataSnapshot) object;
                        if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                            User user = dataSnapshot.getValue(User.class);
                            firebaseChildCallBack.onChildRemoved(user);
                        }
                        progressDialog.dismissDialog();
                    }

                    @Override
                    public void onChildMoved(Object object) {
                        DataSnapshot dataSnapshot = (DataSnapshot) object;
                        if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                            User user = dataSnapshot.getValue(User.class);
                            firebaseChildCallBack.onChildMoved(user);
                        }
                        progressDialog.dismissDialog();
                    }

                    @Override
                    public void onCancelled(Object object) {
                        DataSnapshot dataSnapshot = (DataSnapshot) object;
                        if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                            User user = dataSnapshot.getValue(User.class);
                            firebaseChildCallBack.onCancelled(user);
                        }
                        progressDialog.dismissDialog();
                    }
                }
        );
        query.addChildEventListener(childEventListener);
        return new FirebaseRequestModel(childEventListener, query);

    }

    private String getString(int id) {
        if (fragment != null) {
            return fragment.getString(id);
        } else {
            return activity.getString(id);
        }

    }
}