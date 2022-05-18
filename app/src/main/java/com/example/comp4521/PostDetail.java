package com.example.comp4521;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private Button contactBtn;

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
        contactBtn = findViewById(R.id.contactBtn);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (post.getMissingOrStray().equals("missing")) {
                    goMissingPet();
                } else {
                    goStrayPet();
                }
            }
        });
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goViewLocation();
            }
        });
        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "Hi! I would like to ask about the post of " + post.getName() + " on the PetReuniion app.";
                String phone = "+852 " + "95502882";
                if (isWhatappInstalled()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://api.whatsapp.com/send?phone=" +phone+"&text="+message ));
                    startActivity(intent);
                } else {
                    Toast.makeText(PostDetail.this, "Whatapp is not installed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isWhatappInstalled() {
        PackageManager packageManager = getPackageManager();
        boolean whatsappInstalled;
        try {
            packageManager.getPackageInfo("com.whatsapp",PackageManager.GET_ACTIVITIES);
            whatsappInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            whatsappInstalled = false;
        }
        return whatsappInstalled;
    }

    public void goMissingPet() {
        Intent intent = new Intent(this, Main.class);
        intent.putExtra("fragment", "missing_pets");
        startActivity(intent);
    }

    public void goStrayPet() {
        Intent intent = new Intent(this, Main.class);
        intent.putExtra("fragment", "stray_pets");
        startActivity(intent);
    }

    public void goViewLocation() {
        Intent intent = new Intent(this, ViewLocation.class);
        intent.putExtra("post", (Serializable) post);
        startActivity(intent);
    }
}