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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.denzcoskun.imageslider.constants.ScaleTypes; // important
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
    //private GlobalVariable gv = (GlobalVariable) getActivity().getApplication();

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

        Button localPathwayBtn = v.findViewById(R.id.localPathwayBtn);
        TextView profileUserName = v.findViewById(R.id.profile_user_name);

        //profileUserName.setText(gv.getUserName());
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

        yourPostViewPager = v.findViewById(R.id.yourPostSlider);
        List<YourPost> yourPostList = new ArrayList<>();

        YourPost pet1 = new YourPost();
        pet1.location = "location1";
        pet1.imageUrl = "https://seenthemagazine.com/wp-content/uploads/IMG_1982-1.jpg";
        yourPostList.add(pet1);

        YourPost pet2 = new YourPost();
        pet2.location = "location1";
        pet2.imageUrl = "https://besthqwallpapers.com/Uploads/18-6-2018/56130/thumb2-small-brown-puppy-cute-pets-small-animals-dogs-puppies.jpg";
        yourPostList.add(pet2);

        YourPost pet3 = new YourPost();
        pet3.location = "location1";
        pet3.imageUrl = "https://imageio.forbes.com/specials-images/dam/imageserve/1068867780/960x0.jpg?fit=bounds&format=jpg&width=960";
        yourPostList.add(pet3);

        YourPost pet4 = new YourPost();
        pet4.location = "location1";
        pet4.imageUrl = "https://ichef.bbci.co.uk/news/800/cpsprodpb/16B90/production/_107427039_gettyimages-636475496.jpg";
        yourPostList.add(pet4);

        yourPostViewPager.setAdapter(new YourPostAdapter(yourPostList, yourPostViewPager));
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


        favPostViewPager = v.findViewById(R.id.favPostSlider);
        List<FavPost> favPostList = new ArrayList<>();

        FavPost pet5 = new FavPost();
        pet5.location = "location1";
        pet5.imageUrl = "https://seenthemagazine.com/wp-content/uploads/IMG_1982-1.jpg";
        favPostList.add(pet5);

        FavPost pet6 = new FavPost();
        pet6.location = "location1";
        pet6.imageUrl = "https://besthqwallpapers.com/Uploads/18-6-2018/56130/thumb2-small-brown-puppy-cute-pets-small-animals-dogs-puppies.jpg";
        favPostList.add(pet6);

        FavPost pet7 = new FavPost();
        pet7.location = "location1";
        pet7.imageUrl = "https://imageio.forbes.com/specials-images/dam/imageserve/1068867780/960x0.jpg?fit=bounds&format=jpg&width=960";
        favPostList.add(pet7);

        FavPost pet8 = new FavPost();
        pet8.location = "location1";
        pet8.imageUrl = "https://ichef.bbci.co.uk/news/800/cpsprodpb/16B90/production/_107427039_gettyimages-636475496.jpg";
        favPostList.add(pet8);

        favPostViewPager.setAdapter(new FavPostAdapter(favPostList, favPostViewPager));
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
        return v;
    }

    public void goLocalPathwayActivity() {
        Intent intent = new Intent(getActivity(), LocalPathway.class);
        startActivity(intent);
    }
}
