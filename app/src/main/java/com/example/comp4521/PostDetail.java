package com.example.comp4521;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.comp4521.helper.IndexedLinkedHashMap;
import com.example.comp4521.model.Post;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PostDetail extends AppCompatActivity {

    private Post post;
    private TextView animalType;
    private TextView breed;
    private TextView sex;
    private TextView name;
    private TextView location;
    private TextView date;
    private TextView description;

    private Button backbtn;
    private Button locationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        post = (Post) getIntent().getSerializableExtra("post");
        animalType = findViewById(R.id.type);
        breed = findViewById(R.id.breed);
        sex = findViewById(R.id.sex);
        name = findViewById(R.id.name);
        location = findViewById(R.id.location);
        date = findViewById(R.id.date);
        description = findViewById(R.id.description);

        animalType.setText(post.getAnimalType());
        breed.setText(post.getBreed());
        sex.setText(post.getSex());
        name.setText(post.getName());
        location.setText(post.getLocation());
        date.setText(post.convertTime(post.getCreatedDateTimeLong()));
        description.setText(post.getDescriptions());

        ImageSlider imageSlider = findViewById(R.id.image_slider);
        List<SlideModel> imageList = new ArrayList<>(); // Create image list

        imageList.add(new SlideModel(R.drawable.pet1, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.pet2, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.pet3, ScaleTypes.FIT));

        imageSlider.setImageList(imageList, ScaleTypes.FIT);

        backbtn = findViewById(R.id.backBtn);
        locationBtn = findViewById(R.id.locationBtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMissingPet();
            }
        });
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goViewLocation();
            }
        });
    }

    public void goMissingPet() {
        Intent intent = new Intent(this, Main.class);
        intent.putExtra("fragment", "missing_pets");
        startActivity(intent);
    }
    public void goViewLocation() {
        Intent intent = new Intent(this, ViewLocation.class);
        intent.putExtra("post", (Serializable)post);
        startActivity(intent);
    }
}