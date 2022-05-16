package com.example.comp4521.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp4521.R;
import com.example.comp4521.databinding.PostInfoRowLayoutBinding;
import com.example.comp4521.helper.IndexedLinkedHashMap;
import com.example.comp4521.model.Post;
import com.example.comp4521.utility.Utility;
import com.example.comp4521.view.activity.AddPostActivity;

import static com.example.comp4521.constants.Constant.ADD;
import static com.example.comp4521.constants.Constant.DELETE;
import static com.example.comp4521.constants.Constant.EMPLOYEE_MODEL;
import static com.example.comp4521.constants.Constant.UPDATE;


public class PostDetailsAdapter extends RecyclerView.Adapter<PostDetailsAdapter.PostViewHolder> {
    private IndexedLinkedHashMap<String, Post> postList;
    private LayoutInflater layoutInflater;
    private Activity activity;

    public PostDetailsAdapter(Activity activity, IndexedLinkedHashMap<String, Post> postList) {
        this.activity = activity;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        PostInfoRowLayoutBinding postInfoRowLayoutBinding = DataBindingUtil.inflate(layoutInflater, R.layout.post_info_row_layout, parent, false);
        return new PostViewHolder(postInfoRowLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, final int position) {
        final Post post = postList.getItemByIndex(getReversePosition(position));
        if (!Utility.isEmptyOrNull(post.getAnimalType()))
            holder.postInfoRowLayoutBinding.animalType.setText(post.getAnimalType());
        else
            holder.postInfoRowLayoutBinding.animalType.setText(R.string.na);
        if (!Utility.isEmptyOrNull(post.getLocation()))
            holder.postInfoRowLayoutBinding.location.setText(post.getLocation());
        else
            holder.postInfoRowLayoutBinding.location.setText(R.string.na);
        if (!Utility.isEmptyOrNull(post.convertTime(post.getCreatedDateTimeLong()))) {
            holder.postInfoRowLayoutBinding.postCreateTime.setText(post.convertTime(post.getCreatedDateTimeLong()));
        }else
            holder.postInfoRowLayoutBinding.postCreateTime.setText(R.string.na);

        //Toast.makeText(this.activity, "sim is: "+post.getSimilarity(),  Toast.LENGTH_SHORT).show();
        if (!Utility.isEmptyOrNull(post.getSimilarity()) ) {

            if(post.getSimilarity().equals("high"))
                holder.postInfoRowLayoutBinding.similarity.setText("High");
            else
                holder.postInfoRowLayoutBinding.similarity.setText("Low");
        } else
            holder.postInfoRowLayoutBinding.similarity.setText(R.string.na);
    }


    @Override
    public int getItemCount() {
        if (postList != null)
            return postList.size();
        else return 0;
    }

    private int getReversePosition(int index) {
        if (postList != null && !postList.isEmpty())
            return postList.size() - 1 - index;
        else return 0;
    }

    public void reloadList(IndexedLinkedHashMap<String, Post> list) {
        postList = list;
        notifyDataSetChanged();
    }

    public void reloadList(int index, String operation) {
        switch (operation) {
            case ADD:
                notifyItemInserted(getReversePosition(index));
                break;
            case UPDATE:
                notifyItemChanged(getReversePosition(index));
                break;
            case DELETE:
                notifyItemRemoved(getReversePosition(index));
                break;
            default:
                notifyDataSetChanged();
                break;
        }
    }


    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private PostInfoRowLayoutBinding postInfoRowLayoutBinding;

        private PostViewHolder(PostInfoRowLayoutBinding postInfoRowLayoutBinding) {
            super(postInfoRowLayoutBinding.getRoot());
            this.postInfoRowLayoutBinding = postInfoRowLayoutBinding;
            postInfoRowLayoutBinding.cardViewRowClick.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity, AddPostActivity.class);
            intent.putExtra(EMPLOYEE_MODEL, postList.getItemByIndex(getReversePosition(getAdapterPosition())));
            activity.startActivity(intent);
        }
    }//end of view holder

    public IndexedLinkedHashMap<String, Post> getPostList() {
        return postList;
    }
}
