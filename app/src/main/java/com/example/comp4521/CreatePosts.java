package com.example.comp4521;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class CreatePosts extends Fragment {

    private Button missingPostBtn;
    private Button strayPostBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_posts, container, false);
        missingPostBtn = view.findViewById(R.id.missing_pet_btn);
        missingPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateMissingPost();
            }
        });
        strayPostBtn = view.findViewById(R.id.stray_pet_btn);
        strayPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateStrayPost();
            }
        });


        return view;
    }

    private void goCreateStrayPost() {
        Intent intent = new Intent(this.getActivity(), CreatePetPost.class);
        intent.putExtra("type", "stray");
        startActivity(intent);
    }

    private void goCreateMissingPost() {
        Intent intent = new Intent(this.getActivity(), CreatePetPost.class);
        intent.putExtra("type", "missing");
        startActivity(intent);
    }
}