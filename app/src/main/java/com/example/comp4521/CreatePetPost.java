package com.example.comp4521;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp4521.model.Post;
import com.example.comp4521.utility.Utility;
import com.example.comp4521.viewmodel.AddPostViewModel;
import com.example.comp4521.viewmodel.PostListViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

public class CreatePetPost extends AppCompatActivity {
    PostListViewModel postListViewModel;
    private FusedLocationProviderClient client;
    ConstraintLayout form;
    Button setLocationBtn;
    Button submitBtn;
    Button backBtn;
    TextView toolbarTitle;
    com.google.android.material.radiobutton.MaterialRadioButton femaleBtn;
    com.google.android.material.radiobutton.MaterialRadioButton maleBtn;
    Boolean female;
    Boolean locationSet = false;

    EditText name;
    EditText type;
    EditText breed;
    EditText description;

    Double Gpslatitude;
    Double Gpslongitude;

    SearchView searchView;

    String postType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_missing_post);
        Bundle extras = getIntent().getExtras();
        postType = extras.getString("type");

        form = findViewById(R.id.form);
        setLocationBtn = findViewById(R.id.locationBtn);
        toolbarTitle = findViewById(R.id.toolbar_title);
        femaleBtn = findViewById(R.id.femaleBtn);
        maleBtn = findViewById(R.id.maleBtn);
        submitBtn = findViewById(R.id.submitBtn);
        backBtn = findViewById(R.id.backBtn);
        name = findViewById(R.id.detail_name);
        type = findViewById(R.id.detail_type);
        breed = findViewById(R.id.detail_breed);
        description = findViewById(R.id.detail_descriptions);
        searchView = findViewById(R.id.search_view);
        femaleBtn.setChecked(true);
        form.setVisibility(View.INVISIBLE);
        // Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);

        // Initialize fused location
        client = LocationServices.getFusedLocationProviderClient(this);

        // Check permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            // When permission denied, request permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        AddPostViewModel addPostViewModel = new AddPostViewModel(this);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationSet) {
                    setLocationBtn.setVisibility(View.VISIBLE);
                    form.setVisibility(View.INVISIBLE);
                    locationSet = false;
                    toolbarTitle.setText("Create Post - 1/2");
                } else {
                    goCreatePost();
                }
            }
        });
        setLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocationBtn.setVisibility(View.INVISIBLE);
                form.setVisibility(View.VISIBLE);
                locationSet = true;
                toolbarTitle.setText("Create Post - 2/2");
            }
        });

        femaleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                female = true;
                maleBtn.setChecked(false);
            }
        });

        maleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                female = false;
                femaleBtn.setChecked(false);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    Post post = new Post();

                    post.setName(name.getText().toString());
                    post.setAnimalType(type.getText().toString());
                    post.setBreed(breed.getText().toString());
                    String sex = female ? "female" : "male";
                    post.setSex(sex);
                    post.setDescriptions(description.getText().toString());
                    post.setGpsLatitude(Gpslatitude);
                    post.setGpsLongitude(Gpslongitude);
                    post.setPostID(Utility.getNewId());
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    post.setCreatedDateTime(timestamp.getTime());
                    Location targetLocation = new Location("");//provider name is unnecessary
                    targetLocation.setLatitude(Gpslatitude);//your coords of course
                    targetLocation.setLongitude(Gpslongitude);
                    String locationText = "";
                    try {
                        Geocoder geocoder = new Geocoder(CreatePetPost.this, Locale.ENGLISH);
                        List<Address> addresses = geocoder.getFromLocation(targetLocation.getLatitude(), targetLocation.getLongitude(), 1);
                        locationText = addresses.get(0).getAddressLine(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    post.setLocation(locationText);
                    post.setMissingOrStray(postType);

                    addPostViewModel.addClickListener(post, "post");
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(CreatePetPost.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                            Gpslatitude = latLng.latitude;
                            Gpslongitude = latLng.longitude;
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            if (postType.equals("missing")) {
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.missing_marker));
                            } else {
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.stray_marker));
                            }
                            googleMap.clear();
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                            googleMap.addMarker(markerOptions);
                        }
                    });

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private boolean validate() {
        boolean isValid = true;

        if (Utility.isEmptyOrNull(name.getText().toString())) {
            isValid = false;
        }
        if (Utility.isEmptyOrNull(type.getText().toString())) {
            isValid = false;
        }
        if (Utility.isEmptyOrNull(breed.getText().toString())) {
            isValid = false;
        }
        if (Utility.isEmptyOrNull(description.getText().toString())) {
            isValid = false;
        }
        Log.v("isValid", String.valueOf(isValid));
        if (!isValid) {
            Toast.makeText(this, "Please input all data",
                    Toast.LENGTH_SHORT).show();
        }
        return isValid;
    }

    private void goCreatePost() {
        Intent intent = new Intent(this, Main.class);
        intent.putExtra("fragment", "create_posts");
        startActivity(intent);
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();

                    if (location != null) {
                        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull GoogleMap googleMap) {
                                // Initialize lat lng
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                Gpslatitude = latLng.latitude;
                                Gpslongitude = latLng.longitude;
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
                                googleMap.moveCamera(cameraUpdate);
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(latLng);
                                if (postType.equals("missing")) {
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.missing_marker));
                                } else {
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.stray_marker));
                                }
                                googleMap.addMarker(markerOptions);
                                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                    @Override
                                    public void onMapClick(@NonNull LatLng latLng) {
                                        Gpslatitude = latLng.latitude;
                                        Gpslongitude = latLng.longitude;
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(latLng);
                                        if (postType.equals("missing")) {
                                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.missing_marker));
                                        } else {
                                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.stray_marker));
                                        }
                                        googleMap.clear();
                                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                                        googleMap.addMarker(markerOptions);
                                    }
                                });
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
                                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                        Gpslatitude = latLng.latitude;
                                        Gpslongitude = latLng.longitude;
                                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
                                        googleMap.moveCamera(cameraUpdate);
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(latLng);
                                        if (postType.equals("missing")) {
                                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.missing_marker));
                                        } else {
                                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.stray_marker));
                                        }
                                        googleMap.addMarker(markerOptions);
                                        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                            @Override
                                            public void onMapClick(@NonNull LatLng latLng) {
                                                Gpslatitude = latLng.latitude;
                                                Gpslongitude = latLng.longitude;
                                                MarkerOptions markerOptions = new MarkerOptions();
                                                markerOptions.position(latLng);
                                                if (postType.equals("missing")) {
                                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.missing_marker));
                                                } else {
                                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.stray_marker));
                                                }
                                                googleMap.clear();
                                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                                                googleMap.addMarker(markerOptions);
                                            }
                                        });
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // When permission granted
                getCurrentLocation();
            }
        }
    }
}