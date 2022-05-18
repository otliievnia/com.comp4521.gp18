package com.example.comp4521;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.comp4521.model.Post;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;

public class ViewLocation extends FragmentActivity {
    private Post post;
    private Button backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        post = (Post) getIntent().getSerializableExtra("post");
        setContentView(R.layout.activity_view_location);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                // Initialize lat lng
                LatLng latLng = new LatLng(post.getGpsLatitude(), post.getGpsLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                Log.v("missing", post.getMissingOrStray());
                if (post.getMissingOrStray().equals("missing")) {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.missing_marker));
                } else {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.stray_marker));
                }
                googleMap.addMarker(markerOptions);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            }
        });

        backbtn = findViewById(R.id.backBtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPostDetail();
            }
        });
    }

    public void goPostDetail() {
        Intent intent = new Intent(this, PostDetail.class);
        intent.putExtra("post", (Serializable) post);
        startActivity(intent);
    }
}