package com.example.comp4521;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.denzcoskun.imageslider.constants.ScaleTypes; // important
import com.example.comp4521.callback.CallBack;
import com.example.comp4521.model.FavPost;
import com.example.comp4521.FavPostClass;
import com.example.comp4521.model.Post;
import com.example.comp4521.repository.FavPostRepository;
import com.example.comp4521.repository.PostRepository;
import com.example.comp4521.repository.impl.FavPostRepositoryImpl;
import com.example.comp4521.repository.impl.PostRepositoryImpl;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;


public class Profile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RelativeLayout relativeLayout;
    private androidx.appcompat.widget.Toolbar toolbar;

    private ViewPager2 yourPostViewPager;
    private ViewPager2 favPostViewPager;
    private Handler sliderHandler = new Handler();
    private GlobalVariable gv ;
    private FavPostRepository fpRepo;
    private ArrayList<FavPost> favPost_ArrayList;
    private PostRepository postRepo ;
    private ArrayList<Post> all_post_ArrayList ;
    private ArrayList<String> favPostIdList;
    private Fragment fragment = this;

    private Runnable yourPostSliderRunnable = new Runnable() {
        @Override
        public void run() {
            yourPostViewPager.setCurrentItem(yourPostViewPager.getCurrentItem() + 1);
        }
    };

    private Runnable favPostSliderRunnable = new Runnable() {
        @Override
        public void run() {
            favPostViewPager.setCurrentItem(favPostViewPager.getCurrentItem() + 1);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        gv = (GlobalVariable) getActivity().getApplication().getApplicationContext();
        Button localPathwayBtn = v.findViewById(R.id.localPathwayBtn);
        TextView profileUserName = v.findViewById(R.id.profile_user_name);
        //GlobalVariable.getInstance().getUserID();
        profileUserName.setText(gv.getUserName());
        localPathwayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLocalPathwayActivity();
            }
        });

        appBarLayout = (AppBarLayout) v.findViewById(R.id.appBarLayout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) v.findViewById(R.id.collapsingToolbarLayout);
        relativeLayout = (RelativeLayout) v.findViewById(R.id.topbar);
        toolbar = v.findViewById(R.id.toolbar);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }

                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    relativeLayout.setVisibility(View.VISIBLE);
                } else if (isShow) {
                    relativeLayout.setVisibility(View.INVISIBLE);
                    isShow = false;
                }
            }
        });
        favPostViewPager = v.findViewById(R.id.favPostSlider);
        List<FavPostClass> favPostList = new ArrayList<>();
        yourPostViewPager = v.findViewById(R.id.yourPostSlider);
        List<YourPost> yourPostList = new ArrayList<>();
        // [Get the list of FavPost ]
        fpRepo = new FavPostRepositoryImpl(this);
        postRepo = new PostRepositoryImpl(this);
        all_post_ArrayList = new ArrayList<>(); // * ArrayList Contain all post data
        fpRepo.readFavPostByUserID(gv.getUserID(),new CallBack() {
            @Override
            public void onSuccess(Object object) {
                favPost_ArrayList = (ArrayList<FavPost>) object;
                favPostIdList = new ArrayList<String>();
                // Call PostRepositoryImpl.readPostByKey() to get the detail of a post.
                for (int i = 0; i < favPost_ArrayList.size(); i++) {
                    favPostIdList.add(favPost_ArrayList.get(i).getPostID());
                }
                Toast.makeText(getActivity().getApplication().getApplicationContext(), "Got the FavPost list", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Object object) {

            }
        });
        postRepo.readAllPostBySingleValueEvent(new CallBack() {
            @Override
            public void onSuccess(Object object) {
                all_post_ArrayList = (ArrayList<Post>) object;
                for (int i = 0; i < all_post_ArrayList.size(); i++) {
                   Post post = all_post_ArrayList.get(i);
                   if (favPostIdList.contains(post.getPostID())) {
                       FavPostClass pet = new FavPostClass();
                       pet.location = post.getLocation();
                       pet.imageUrl = String.valueOf(post.getImageUrls().get(0));
                       pet.post = post;
                       favPostList.add(pet);
                   }
                    if (post.getAccountID().equals(gv.getUserID())) {
                        YourPost pet1 = new YourPost();
                        pet1.location = post.getLocation();
                        pet1.imageUrl = String.valueOf(post.getImageUrls().get(0));
                        pet1.post = post;
                        yourPostList.add(pet1);
                    }
                }
                yourPostViewPager.setAdapter(new YourPostAdapter(fragment, yourPostList, yourPostViewPager));
                yourPostViewPager.setClipToPadding(false);
                yourPostViewPager.setClipChildren(false);
                yourPostViewPager.setOffscreenPageLimit(3);
                yourPostViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);


                CompositePageTransformer compositePageTransformer_your_post = new CompositePageTransformer();
                compositePageTransformer_your_post.addTransformer(new MarginPageTransformer(40));
                compositePageTransformer_your_post.addTransformer(new ViewPager2.PageTransformer() {
                    @Override
                    public void transformPage(@NonNull View page, float position) {
                        float r = 1 - Math.abs(position);
                        page.setScaleY(0.95f + r * 0.05f);
                    }
                });

                yourPostViewPager.setPageTransformer(compositePageTransformer_your_post);
                yourPostViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        sliderHandler.removeCallbacks(yourPostSliderRunnable);
                        sliderHandler.postDelayed(yourPostSliderRunnable, 3000);
                    }
                });

                favPostViewPager.setAdapter(new FavPostAdapter(fragment, favPostList, favPostViewPager));
                favPostViewPager.setClipToPadding(false);
                favPostViewPager.setClipChildren(false);
                favPostViewPager.setOffscreenPageLimit(3);
                favPostViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                CompositePageTransformer compositePageTransformer_fav_post = new CompositePageTransformer();
                compositePageTransformer_fav_post.addTransformer(new MarginPageTransformer(40));
                compositePageTransformer_fav_post.addTransformer(new ViewPager2.PageTransformer() {
                    @Override
                    public void transformPage(@NonNull View page, float position) {
                        float r = 1 - Math.abs(position);
                        page.setScaleY(0.95f + r * 0.05f);
                    }
                });

                favPostViewPager.setPageTransformer(compositePageTransformer_fav_post);
                favPostViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        sliderHandler.removeCallbacks(favPostSliderRunnable);
                        sliderHandler.postDelayed(favPostSliderRunnable, 3000);
                    }
                });
                return;
            }

            @Override
            public void onError(Object object) {
            }
        });
        return v;
    }

    public void goLocalPathwayActivity() {
        Intent intent = new Intent(getActivity(), LocalPathway.class);
        startActivity(intent);
    }
}
