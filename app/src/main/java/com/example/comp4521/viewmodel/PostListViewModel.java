package com.example.comp4521.viewmodel;


import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp4521.MissingPets;
import com.example.comp4521.PostDetail;
import com.example.comp4521.R;
import com.example.comp4521.callback.FirebaseChildCallBack;
import com.example.comp4521.databinding.FragmentMissingPetsBinding;
import com.example.comp4521.firebase.FirebaseRequestModel;
import com.example.comp4521.firebase.FirebaseUtility;
import com.example.comp4521.helper.IndexedLinkedHashMap;
import com.example.comp4521.model.Post;
import com.example.comp4521.repository.PostRepository;
import com.example.comp4521.repository.impl.PostRepositoryImpl;
import com.example.comp4521.utility.Utility;
//import com.example.comp4521.view.activity.AddPostActivity;
//import com.example.comp4521.view.activity.PostListActivity;
//import com.example.comp4521.view.activity.PostListActivity;
import com.example.comp4521.view.adapter.PostDetailsAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.comp4521.constants.Constant.ADD;
import static com.example.comp4521.constants.Constant.DELETE;
import static com.example.comp4521.constants.Constant.UPDATE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PostListViewModel {
    private static final String TAG = "PostListViewModel";
    private FragmentMissingPetsBinding fragmentMissingPetsBinding;
    private MissingPets missingPetsFragment;
    public PostDetailsAdapter postDetailsAdapter;
    private PostRepository postRepository;
    private FirebaseRequestModel firebaseRequestModel;
    private ArrayList<Marker> mMarkerArray = new ArrayList<Marker>();
    private String missingOrStray;

    public PostListViewModel(MissingPets missingPetsFragment, FragmentMissingPetsBinding fragmentMissingPetsBinding, String missingOrStray) {
        this.fragmentMissingPetsBinding = fragmentMissingPetsBinding;
        this.missingPetsFragment = missingPetsFragment;
        this.missingOrStray = missingOrStray;
        postRepository = new PostRepositoryImpl(missingPetsFragment);
    }


    public void init() {
        Log.w(TAG, "start to do init()");
        getAllPosts();
    }

    private void setAdapter(IndexedLinkedHashMap<String, Post> postIndexedLinkedHashMap) {
        Log.w(TAG, "start to do setAdapter()");
        if (postDetailsAdapter == null) {
            postDetailsAdapter = new PostDetailsAdapter(missingPetsFragment, postIndexedLinkedHashMap);
            fragmentMissingPetsBinding.rvEmployeeList.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(missingPetsFragment.getActivity());
            fragmentMissingPetsBinding.rvEmployeeList.setLayoutManager(mLayoutManager);
            fragmentMissingPetsBinding.rvEmployeeList.setItemAnimator(new DefaultItemAnimator());
            fragmentMissingPetsBinding.rvEmployeeList.setAdapter(postDetailsAdapter);
        } else {
            postDetailsAdapter.reloadList(postIndexedLinkedHashMap);
        }
    }

    private void getAllPosts() {
        Log.w(TAG, "start to do getAllPosts()");
        if (firebaseRequestModel != null)
            removeListener();
        Log.w(TAG, "start to do getAllPosts()");
        firebaseRequestModel = postRepository.readAllPostByChildEvent(new FirebaseChildCallBack() {
            @Override
            public void onChildAdded(Object object) {
                if (object != null) {
                    Post post = (Post) object;
                    Log.w(TAG, "getAllPosts(); post Similarity is:"+post.getSimilarity());
                    if (!post.getMissingOrStray().equals(missingOrStray)){
                        return;
                    }
                    if (postDetailsAdapter == null)
                        setAdapter(new IndexedLinkedHashMap<String, Post>());
                    postDetailsAdapter.getPostList().add(post.getPostID(), post);
                    postDetailsAdapter.reloadList(postDetailsAdapter.getPostList().size() - 1, ADD);
                    SupportMapFragment supportMapFragment = (SupportMapFragment) missingPetsFragment.getChildFragmentManager().findFragmentById(R.id.map_fragment);
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            // load map
                            LatLng latLng = new LatLng(post.getGpsLatitude(), post.getGpsLongitude());
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.missing_marker));
                            Marker marker = googleMap.addMarker(markerOptions);
                            marker.setTag(post.getPostID());
                            mMarkerArray.add(marker);

                            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(@NonNull Marker marker) {
                                    Intent intent = new Intent(missingPetsFragment.getActivity(), PostDetail.class);
                                    IndexedLinkedHashMap<String, Post> postList = postDetailsAdapter.getPostList();
                                    Post selectedPost = new Post();
                                    for (int i = 0; i < postList.size(); i++) {
                                        if (String.valueOf(marker.getTag()) == postList.getItemByIndex(i).getPostID()) {
                                            selectedPost = postList.getItemByIndex(i);
                                        }
                                    }
                                    intent.putExtra("post", (Serializable) selectedPost);
                                    missingPetsFragment.getActivity().startActivity(intent);
                                    return false;
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onChildChanged(Object object) {
                if (object != null) {
                    Post post = (Post) object;
                    postDetailsAdapter.getPostList().update(post.getPostID(), post);
                    postDetailsAdapter.reloadList(postDetailsAdapter.getPostList().getIndexByKey(post.getPostID()), UPDATE);
                    SupportMapFragment supportMapFragment = (SupportMapFragment) missingPetsFragment.getChildFragmentManager().findFragmentById(R.id.map_fragment);
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            // load map
                            for (Marker marker : mMarkerArray) {
                                if (String.valueOf(marker.getTag()).equals(post.getPostID())) {
                                    LatLng latLng = new LatLng(post.getGpsLatitude(), post.getGpsLongitude());
                                    marker.setPosition(latLng);
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onChildRemoved(Object object) {
                if (object != null) {
                    Post post = (Post) object;
                    postDetailsAdapter.getPostList().update(post.getPostID(), post);
                    postDetailsAdapter.reloadList(postDetailsAdapter.getPostList().getIndexByKey(post.getPostID()), DELETE);
                    SupportMapFragment supportMapFragment = (SupportMapFragment) missingPetsFragment.getChildFragmentManager().findFragmentById(R.id.map_fragment);
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            // load map
                            for (Marker marker : mMarkerArray) {
                                if (marker.getTag() == post.getPostID()) {
                                    marker.remove();
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onChildMoved(Object object) {

            }

            @Override
            public void onCancelled(Object object) {
                Utility.showMessage(missingPetsFragment.getActivity(), missingPetsFragment.getString(R.string.some_thing_went_wrong));
            }
        });
    }

    public void removeListener() {
        Log.w(TAG, "start to do setAdapter()");
        FirebaseUtility.removeFireBaseChildListener(missingPetsFragment.getActivity(), firebaseRequestModel);
    }

}
