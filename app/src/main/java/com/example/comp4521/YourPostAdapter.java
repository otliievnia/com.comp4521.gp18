package com.example.comp4521;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.comp4521.callback.CallBack;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class YourPostAdapter extends RecyclerView.Adapter<YourPostAdapter.YourPostViewHolder> {
    private List<YourPost> yourPostList;
    private ViewPager2 viewPager2;
    private Fragment fragment;

    public YourPostAdapter(Fragment fragment, List<YourPost> yourPostList, ViewPager2 viewPager2) {
        this.yourPostList = yourPostList;
        this.viewPager2 = viewPager2;
        this.fragment = fragment;
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

    public class YourPostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private KenBurnsView kbvPost;
        private TextView textLocation;

        public YourPostViewHolder(@NonNull View itemView) {
            super(itemView);
            kbvPost = itemView.findViewById(R.id.kbvPost);
            textLocation = itemView.findViewById(R.id.textLocation);
            itemView.setOnClickListener(this);

        }

        void setPostData(YourPost yourPost) {
            Picasso.get().load(yourPost.imageUrl).into(kbvPost);
            textLocation.setText(yourPost.location);
        }

        @Override
        public void onClick(View v) {
            Log.v("halo","halo");
            Intent intent = new Intent(fragment.getActivity(), PostDetail.class);
            intent.putExtra("post", (Serializable) yourPostList.get(getAdapterPosition()).post);
            fragment.getActivity().startActivity(intent);
        }
    }

    private int getReversePosition(int index) {
        if (yourPostList != null && !yourPostList.isEmpty())
            return yourPostList.size() - 1 - index;
        else return 0;
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            yourPostList.addAll(yourPostList);
            notifyDataSetChanged();
        }
    };
}
