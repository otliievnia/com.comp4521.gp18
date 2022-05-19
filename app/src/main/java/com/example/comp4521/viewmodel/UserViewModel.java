package com.example.comp4521.viewmodel;

import static com.example.comp4521.constants.Constant.ADD;
import static com.example.comp4521.constants.Constant.DELETE;
import static com.example.comp4521.constants.Constant.UPDATE;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp4521.MissingPets;
import com.example.comp4521.PostDetail;
import com.example.comp4521.R;
import com.example.comp4521.Login;
import com.example.comp4521.Signup;
import com.example.comp4521.callback.CallBack;
import com.example.comp4521.callback.FirebaseChildCallBack;
import com.example.comp4521.databinding.FragmentMissingPetsBinding;
import com.example.comp4521.databinding.FragmentStrayPetsBinding;
import com.example.comp4521.firebase.FirebaseRequestModel;
import com.example.comp4521.firebase.FirebaseUtility;
import com.example.comp4521.helper.IndexedLinkedHashMap;
import com.example.comp4521.model.User;
import com.example.comp4521.repository.UserRepository;
import com.example.comp4521.repository.impl.UserRepositoryImpl;
import com.example.comp4521.utility.Utility;
import com.example.comp4521.view.adapter.PostDetailsAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;

public class UserViewModel {
    private static final String TAG = "UserViewModel";
    private FragmentMissingPetsBinding fragmentMissingPetsBinding;
    private FragmentStrayPetsBinding fragmentStrayPetsBinding;
    private Fragment fragment;
    private Activity activity;

    private UserRepository userRepository;
    private FirebaseRequestModel firebaseRequestModel;
    private ArrayList<Marker> mMarkerArray = new ArrayList<Marker>();
    private String missingOrStray;

    public UserViewModel(Fragment fragment) {
        this.fragment = (MissingPets) fragment;
        userRepository = new UserRepositoryImpl(this.fragment);
    }
    public UserViewModel(Activity activity) {
        this.activity = (Signup) activity;
        userRepository = new UserRepositoryImpl(this.activity);
    }

    public void createNewUser(User user) {
        Log.w(TAG, "start to do createNewUser()");
        setupNewUser(user);
    }
    private void setupNewUser(User user) {
        userRepository.createUser(user, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                activity.finish();
            }

            @Override
            public void onError(Object object) {
                Utility.showMessage(activity, activity.getString(R.string.some_thing_went_wrong));
            }
        });
    }


    public void init() {
        Log.w(TAG, "start to do init()");
        //getAllUsers();
    }
    /*
    private void setAdapter(IndexedLinkedHashMap<String, User> userIndexedLinkedHashMap) {
        Log.w(TAG, "start to do setAdapter()");
        if (userDetailsAdapter == null) {
            userDetailsAdapter = new UserDetailsAdapter(fragment, userIndexedLinkedHashMap);
            if (missingOrStray.equals("missing")) {

                fragmentMissingPetsBinding.rvEmployeeList.setHasFixedSize(true);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(fragment.getActivity());
                fragmentMissingPetsBinding.rvEmployeeList.setLayoutManager(mLayoutManager);
                fragmentMissingPetsBinding.rvEmployeeList.setItemAnimator(new DefaultItemAnimator());
                fragmentMissingPetsBinding.rvEmployeeList.setAdapter(userDetailsAdapter);
            } else {
                fragmentStrayPetsBinding.rvEmployeeList.setHasFixedSize(true);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(fragment.getActivity());
                fragmentStrayPetsBinding.rvEmployeeList.setLayoutManager(mLayoutManager);
                fragmentStrayPetsBinding.rvEmployeeList.setItemAnimator(new DefaultItemAnimator());
                fragmentStrayPetsBinding.rvEmployeeList.setAdapter(userDetailsAdapter);
            }
        } else {
            userDetailsAdapter.reloadList(userIndexedLinkedHashMap);
        }
    }
*/
    /*
    private void getAllUsers() {
        Log.w(TAG, "start to do getAllUsers()");
        if (firebaseRequestModel != null)
            removeListener();
        Log.w(TAG, "start to do getAllUsers()");
        firebaseRequestModel = userRepository.readAllUserByChildEvent(new FirebaseChildCallBack() {
            @Override
            public void onChildAdded(Object object) {
                if (object != null) {
                    User user = (User) object;
                    //Log.w(TAG, "getAllUsers(); User Similarity is:" + post.getSimilarity());

                    if (userDetailsAdapter == null)
                        setAdapter(new IndexedLinkedHashMap<String, User>());
                    userDetailsAdapter.getUserList().add(user.getUserId(), user);
                    userDetailsAdapter.reloadList(userDetailsAdapter.getUserList().size() - 1, ADD);

                }
            }

            @Override
            public void onChildChanged(Object object) {
                if (object != null) {
                    User user = (User) object;
                    userDetailsAdapter.getUserList().update(user.getUserId(), user);
                    userDetailsAdapter.reloadList(userDetailsAdapter.getUserList().getIndexByKey(user.getUserID()), UPDATE);

                }
            }

            @Override
            public void onChildRemoved(Object object) {
                if (object != null) {
                    User user = (User) object;
                    userDetailsAdapter.getUserList().update(user.getUserId(), user);
                    userDetailsAdapter.reloadList(userDetailsAdapter.getUserList().getIndexByKey(user.getUserID()), DELETE);

                }
            }

            @Override
            public void onChildMoved(Object object) {

            }

            @Override
            public void onCancelled(Object object) {
                Utility.showMessage(fragment.getActivity(), fragment.getString(R.string.some_thing_went_wrong));
            }
        });
    }
    */
    public void removeListener() {
        Log.w(TAG, "start to do setAdapter()");
        FirebaseUtility.removeFireBaseChildListener(fragment.getActivity(), firebaseRequestModel);
    }
}
