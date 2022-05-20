package com.example.comp4521.view.adapter;

import static org.junit.Assert.*;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.comp4521.helper.IndexedLinkedHashMap;
import com.example.comp4521.model.Post;

import org.junit.Test;


public class PostDetailsAdapterTest {

    @Test
    public void getItemCount() {
        IndexedLinkedHashMap<String, Post> postList = new IndexedLinkedHashMap<String, Post>();
        Post post1 = new Post();
        post1.setAnimalType("Dog");
        Post post2 = new Post();
        post1.setAnimalType("Cat");
        postList.add("Post1",post1);
        postList.add("Post2",post2);
        Fragment testFragment = new Fragment();
        PostDetailsAdapter testPostAdapter = new PostDetailsAdapter(testFragment, postList);
        int result = testPostAdapter.getItemCount();
        assertTrue(result == 2);
    }

    @Test
    public void getPostList() {
        IndexedLinkedHashMap<String, Post> postList = new IndexedLinkedHashMap<String, Post>();
        Post post1 = new Post();
        post1.setAnimalType("Dog");
        Post post2 = new Post();
        post1.setAnimalType("Cat");
        postList.add("0",post1);
        postList.add("1",post2);
        Fragment testFragment = new Fragment();
        PostDetailsAdapter testPostAdapter = new PostDetailsAdapter(testFragment, postList);
        String animalType = testPostAdapter.getPostList().getItemByIndex(0).getAnimalType();
        assertTrue(animalType.equals("Cat"));
    }
}