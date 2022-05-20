package com.example.comp4521;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PostDetail extends AppCompatActivity {
    private static final String TAG = "error";

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
    private Context context;

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
                /*fpRepo.createFavPost(favPost,new CallBack() {
                    @Override
                    public void onSuccess(Object object) {
                        Toast.makeText(getApplicationContext(), "Set Favourite" , Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Object object) {

                    }
                });*/

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
        StrictMode.VmPolicy.Builder builder= new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Target image;
        image = new Target() {
            @Override
            public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/jpeg");

                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "title");
                        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                values);


                        OutputStream outstream;
                        try {
                            outstream = getContentResolver().openOutputStream(uri);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                            outstream.close();
                        } catch (Exception e) {
                            System.err.println(e.toString());
                        }

                        share.putExtra(Intent.EXTRA_STREAM, uri);
                        startActivity(Intent.createChooser(share, "Share Image"));
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.get().load(String.valueOf(post.getImageUrls().get(0))).into(image);





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