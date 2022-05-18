package com.example.comp4521;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import com.example.comp4521.helper.IndexedLinkedHashMap;
import com.example.comp4521.model.Post;
import com.example.comp4521.view.adapter.PostDetailsAdapter;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.comp4521.R;
import com.example.comp4521.databinding.FragmentMissingPetsBinding;
import com.example.comp4521.viewmodel.PostListViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MissingPets extends Fragment {
    PostListViewModel postListViewModel;
    private FusedLocationProviderClient client;


    @Override
    public void onDestroy() {
        super.onDestroy();
        postListViewModel.removeListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentMissingPetsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_missing_pets, container, false);
        View view = binding.getRoot();

        // Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);

        // Initialize fused location
        client = LocationServices.getFusedLocationProviderClient(this.getActivity());

        // Check permission
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();

        } else {
            // When permission denied, request permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        // TODO: uncomment below code to continue integration, The first "this" is not match between activity and fragment
        postListViewModel = new PostListViewModel(this, binding, "missing");
        postListViewModel.init();

        FrameLayout mScrollContainer = view.findViewById(R.id.bottom_sheet);
        mScrollContainer.bringToFront();
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(mScrollContainer);
        bottomSheetBehavior.setPeekHeight(200);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING || newState == BottomSheetBehavior.STATE_EXPANDED) {
//                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
//                        @Override
//                        public void onMapReady(@NonNull GoogleMap googleMap) {
//                            // load map
//                            googleMap.getUiSettings().setScrollGesturesEnabled(false);
//                        }
//                    });
                } else {
//                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
//                        @Override
//                        public void onMapReady(@NonNull GoogleMap googleMap) {
//                            // load map
//                            googleMap.getUiSettings().setScrollGesturesEnabled(true);
//                        }
//                    });
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });

        return view;
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();

                    if (location != null) {
                        // Async map
                        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull GoogleMap googleMap) {
                                // Initialize lat lng
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
                                googleMap.moveCamera(cameraUpdate);
                                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            }
                        });
                    } else {

                        LocationRequest locationRequest = LocationRequest.create() //if you want access of variable
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setNumUpdates(1);
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(@NonNull GoogleMap googleMap) {
                                        // Initialize lat lng
                                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
                                        googleMap.moveCamera(cameraUpdate);
                                        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                    }
                                });
                            }
                        };
                        client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                }
            });
        }
    }
}