package com.example.comp4521.repository.impl;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.example.comp4521.R;
import com.example.comp4521.callback.CallBack;
import com.example.comp4521.callback.FirebaseChildCallBack;
import com.example.comp4521.firebase.FirebaseRepository;
import com.example.comp4521.firebase.FirebaseRequestModel;
import com.example.comp4521.model.Post;
import com.example.comp4521.repository.PostRepository;
import com.example.comp4521.utility.Utility;
import com.example.comp4521.view.dialog.ProgressDialogClass;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.comp4521.constants.Constant.FAIL;
import static com.example.comp4521.constants.Constant.SUCCESS;
import static com.example.comp4521.firebase.FirebaseConstants.POST_TABLE;
import static com.example.comp4521.firebase.FirebaseDatabaseReference.DATABASE;

import androidx.fragment.app.Fragment;

public class PostRepositoryImplActivity extends FirebaseRepository implements PostRepository {
    private ProgressDialogClass progressDialog;
    private Activity activity;
    private DatabaseReference postDatabaseReference;

    public PostRepositoryImplActivity(Activity activity) {
        this.activity = activity;
        progressDialog = new ProgressDialogClass(activity);
        postDatabaseReference = DATABASE.getReference(POST_TABLE);
    }

    @Override
    public void createPost(Post post, final CallBack callBack) {
        String pushKey = postDatabaseReference.push().getKey();
        if (post != null && !Utility.isEmptyOrNull(pushKey)) {
            progressDialog.showDialog(getString(R.string.loading), getString(R.string.please_wait));
            post.setPostID(pushKey);
            DatabaseReference databaseReference = postDatabaseReference.child(pushKey);
            fireBaseCreate(databaseReference, post, new CallBack() {
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
    public void updatePost(String postIdKey, HashMap map, final CallBack callBack) {
        if (!Utility.isEmptyOrNull(postIdKey)) {
            progressDialog.showDialog(getString(R.string.loading), getString(R.string.please_wait));
            DatabaseReference databaseReference = postDatabaseReference.child(postIdKey);
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
    public void deletePost(String postIdKey, final CallBack callBack) {
        if (!Utility.isEmptyOrNull(postIdKey)) {
            progressDialog.showDialog(getString(R.string.loading), getString(R.string.please_wait));
            DatabaseReference databaseReference = postDatabaseReference.child(postIdKey);
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
    public void readPostByKey(String postIdKey, final CallBack callBack) {
        if (!Utility.isEmptyOrNull(postIdKey)) {
            progressDialog.showDialog(getString(R.string.loading), getString(R.string.please_wait));
            Query query = postDatabaseReference.child(postIdKey);
            fireBaseReadData(query, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    if (object != null) {
                        DataSnapshot dataSnapshot = (DataSnapshot) object;
                        if (dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                            Post post = dataSnapshot.getValue(Post.class);
                            callBack.onSuccess(post);
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
    public void readPostByName(String postName, final CallBack callBack) {
        if (!Utility.isEmptyOrNull(postName)) {
            progressDialog.showDialog(getString(R.string.loading), getString(R.string.please_wait));
            Query query = postDatabaseReference.orderByChild("empName").equalTo(postName);
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
                            Post post = child.getValue(Post.class);
                            callBack.onSuccess(post);
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
    public void readAllPostBySingleValueEvent(final CallBack callBack) {
        progressDialog.showDialog(getString(R.string.loading), getString(R.string.please_wait));
        //get all posts order by post's pet name
        Query query = postDatabaseReference.orderByChild("empName");
        fireBaseReadData(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                        ArrayList<Post> postArrayList = new ArrayList<>();
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            Post post = suggestionSnapshot.getValue(Post.class);
                            postArrayList.add(post);
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
    }

    @Override
    public FirebaseRequestModel readAllPostByDataChangeEvent(final CallBack callBack) {
        progressDialog.showDialog(getString(R.string.loading), getString(R.string.please_wait));
        //get all posts order by post pet name
        Query query = postDatabaseReference.orderByChild("empName");
        ValueEventListener valueEventListener = fireBaseDataChangeListener(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                        ArrayList<Post> postArrayList = new ArrayList<>();
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            Post post = suggestionSnapshot.getValue(Post.class);
                            postArrayList.add(post);
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
    public FirebaseRequestModel readAllPostByChildEvent(final FirebaseChildCallBack firebaseChildCallBack) {
        progressDialog.showDialog(getString(R.string.loading), getString(R.string.please_wait));
        //get all posts order by created date time
        Query query = postDatabaseReference.orderByChild("createdDateTime");
        ChildEventListener childEventListener = fireBaseChildEventListener(query, new FirebaseChildCallBack() {
                    @Override
                    public void onChildAdded(Object object) {
                        DataSnapshot dataSnapshot = (DataSnapshot) object;
                        if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                            Post post = dataSnapshot.getValue(Post.class);
                            firebaseChildCallBack.onChildAdded(post);
                        }
                        progressDialog.dismissDialog();
                    }

                    @Override
                    public void onChildChanged(Object object) {
                        DataSnapshot dataSnapshot = (DataSnapshot) object;
                        if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                            Post post = dataSnapshot.getValue(Post.class);
                            firebaseChildCallBack.onChildChanged(post);
                        }
                    }

                    @Override
                    public void onChildRemoved(Object object) {
                        DataSnapshot dataSnapshot = (DataSnapshot) object;
                        if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                            Post post = dataSnapshot.getValue(Post.class);
                            firebaseChildCallBack.onChildRemoved(post);
                        }
                        progressDialog.dismissDialog();
                    }

                    @Override
                    public void onChildMoved(Object object) {
                        DataSnapshot dataSnapshot = (DataSnapshot) object;
                        if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                            Post post = dataSnapshot.getValue(Post.class);
                            firebaseChildCallBack.onChildMoved(post);
                        }
                        progressDialog.dismissDialog();
                    }

                    @Override
                    public void onCancelled(Object object) {
                        DataSnapshot dataSnapshot = (DataSnapshot) object;
                        if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                            Post post = dataSnapshot.getValue(Post.class);
                            firebaseChildCallBack.onCancelled(post);
                        }
                        progressDialog.dismissDialog();
                    }
                }
        );
        query.addChildEventListener(childEventListener);
        return new FirebaseRequestModel(childEventListener, query);

    }

    private String getString(int id) {
        return activity.getString(id);
    }
}
