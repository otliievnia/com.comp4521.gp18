package com.example.comp4521;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.comp4521.callback.CallBack;
import com.example.comp4521.helper.IndexedLinkedHashMap;
import com.example.comp4521.model.Post;
import com.example.comp4521.model.FavPost;
import com.example.comp4521.repository.FavPostRepository;
import com.example.comp4521.repository.impl.FavPostRepositoryImpl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
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
    private Button shareBtn;
    private Button setFavBtn;
    private FavPostRepository fpRepo;
    private GlobalVariable gv ;
    boolean notYetSetFav = true;

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
        setFavBtn = findViewById(R.id.set_fav_btn);
        animalType.setText(post.getAnimalType());
        breed.setText(post.getBreed());
        sex.setText(post.getSex());
        name.setText(post.getName());
        location.setText(post.getLocation());
        date.setText(post.convertTime(post.getCreatedDateTimeLong()));
        description.setText(post.getDescriptions());
        fpRepo = new FavPostRepositoryImpl(this);

        Context context;
        ImageSlider imageSlider = findViewById(R.id.image_slider);

        List<Object> postImageList = post.getImageUrls();
        List<SlideModel> imageList = new ArrayList<>(); // Create image list

        for (int i = 0; i < postImageList.size(); i++) {
            String imageURL = String.valueOf(postImageList.get(i));
            imageList.add(new SlideModel(imageURL, ScaleTypes.FIT));
        }

        imageSlider.setImageList(imageList, ScaleTypes.FIT);

        backbtn = findViewById(R.id.backBtn);
        locationBtn = findViewById(R.id.locationBtn);
        contactBtn = findViewById(R.id.contactBtn);
        shareBtn = findViewById(R.id.shareBtn);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("error","error when transform image url");

                shareImage();
            }
        });

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
                String message = "I would like to ask about the following post on the PetReuniion app: \n";
                message += "Type: " + post.getAnimalType() + "\n";
                message += "Breed: " + post.getBreed() + "\n";
                message += "Gender: " + post.getSex() + "\n";
                message += "Name: " + post.getName() + "\n";
                message += "Location: " + post.getLocation() + "\n";
                String phone = "+852 " + post.getMobile();
                if (isWhatappInstalled()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://api.whatsapp.com/send?phone=" + phone + "&text=" + message));
                    startActivity(intent);
                } else {
                    Toast.makeText(PostDetail.this, "Whatapp is not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        setFavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gv = (GlobalVariable) getApplicationContext();
                FavPost favPost = new FavPost();
                favPost.setPostID(post.getPostID());
                favPost.setUserID(gv.getUserID());
                fpRepo.readFavPostByUserID(gv.getUserID(),new CallBack() {
                    @Override
                    public void onSuccess(Object object) {
                        ArrayList<FavPost> postArrayList = (ArrayList<FavPost>) object;
                        String found_favpostKey = "";
                        for(FavPost fp:postArrayList) {

                            notYetSetFav = true;
                            if (fp.getPostID().equals(post.getPostID())) {
                                notYetSetFav = false;
                                found_favpostKey = fp.getFavPostID();
                                break;
                            }
                        }
                        if( notYetSetFav){
                            fpRepo.createFavPost(favPost,new CallBack() {
                                @Override
                                public void onSuccess(Object object) {
                                    Toast.makeText(getApplicationContext(), "Set Favourite" , Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(Object object) {

                                }
                            });
                        }else{
                            fpRepo.deleteFavPost(found_favpostKey,new CallBack() {
                                @Override
                                public void onSuccess(Object object) {
                                    Toast.makeText(getApplicationContext(), "Removed Favourite" , Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(Object object) {

                                }
                            });
                        }

                    }

                    @Override
                    public void onError(Object object) {

                    }
                });


            }
        });
    }

    private void shareImage() {

        try {

            URL url = new URL("http://www.helpinghomelesscats.com/images/cat1.jpg");
            InputStream in = url.openConnection().getInputStream();
            BufferedInputStream bis = new BufferedInputStream(in,1024*8);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            int len=0;
            byte[] buffer = new byte[1024];
            while((len = bis.read(buffer)) != -1){
                out.write(buffer, 0, len);
            }
            out.close();
            bis.close();

            byte[] data = out.toByteArray();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean isWhatappInstalled() {
        PackageManager packageManager = getPackageManager();
        boolean whatsappInstalled;
        try {
            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
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