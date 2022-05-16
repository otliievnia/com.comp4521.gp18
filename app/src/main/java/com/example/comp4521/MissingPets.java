package com.example.comp4521;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.comp4521.R;
import com.example.comp4521.databinding.FragmentMissingPetsBinding;
import com.example.comp4521.viewmodel.PostListViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MissingPets#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MissingPets extends Fragment {
    PostListViewModel postListViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MissingPets() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MissingPets.
     */
    // TODO: Rename and change types and number of parameters
    public static MissingPets newInstance(String param1, String param2) {
        MissingPets fragment = new MissingPets();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        postListViewModel.removeListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentMissingPetsBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_missing_pets, container, false);
        View view = binding.getRoot();

        // TODO: uncomment below code to continue integration, The first "this" is not match between activity and fragment
        //postListViewModel = new PostListViewModel(this, binding);
        postListViewModel.setActionBar();
        postListViewModel.init();
        //return inflater.inflate(R.layout.fragment_missing_pets, container, false);
        return view;
    }
}