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

public class YourPostAdapter extends RecyclerView.Adapter<YourPostAdapter.YourPostViewHolder> {
    private List<YourPost> yourPostList;
    private ViewPager2 viewPager2;

    public YourPostAdapter(List<YourPost> yourPostList, ViewPager2 viewPager2) {
        this.yourPostList = yourPostList;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public YourPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new YourPostViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slider_your_post,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull YourPostViewHolder holder, int position) {
        holder.setPostData(yourPostList.get(position));
        if (position == yourPostList.size() - 1) {
            viewPager2.post(runnable);
        }

    }

    @Override
    public int getItemCount() {
        return yourPostList.size();
    }

    static class YourPostViewHolder extends RecyclerView.ViewHolder {

        private KenBurnsView kbvPost;
        private TextView textLocation;

        public YourPostViewHolder(@NonNull View itemView) {
            super(itemView);
            kbvPost = itemView.findViewById(R.id.kbvPost);
            textLocation = itemView.findViewById(R.id.textLocation);
        }

        void setPostData(YourPost yourPost) {
            Picasso.get().load(yourPost.imageUrl).into(kbvPost);
            textLocation.setText(yourPost.location);
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            yourPostList.addAll(yourPostList);
            notifyDataSetChanged();
        }
    };
}
