package com.example.comp4521;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;

import java.util.List;


public class FavPostAdapter extends RecyclerView.Adapter<FavPostAdapter.FavPostViewHolder> {
    private List<FavPost> favPostList;
    private ViewPager2 viewPager2;

    public FavPostAdapter(List<FavPost> favPostList, ViewPager2 viewPager2) {
        this.favPostList = favPostList;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public FavPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavPostViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slider_fav_post,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull FavPostViewHolder holder, int position) {
        holder.setPostData(favPostList.get(position));
        if (position == favPostList.size() - 1) {
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return favPostList.size();
    }

    static class FavPostViewHolder extends RecyclerView.ViewHolder {

        private KenBurnsView kbvPost;
        private TextView textLocation;

        public FavPostViewHolder(@NonNull View itemView) {
            super(itemView);
            kbvPost = itemView.findViewById(R.id.kbvPost);
            textLocation = itemView.findViewById(R.id.textLocation);
        }

        void setPostData(FavPost favPost) {
            Picasso.get().load(favPost.imageUrl).into(kbvPost);
            textLocation.setText(favPost.location);
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            favPostList.addAll(favPostList);
            notifyDataSetChanged();
        }
    };
}
