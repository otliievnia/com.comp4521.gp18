package com.example.comp4521;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.comp4521.databinding.ActivityMainBinding;

public class Main extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle fragment = getIntent().getExtras();
        if (fragment != null) {
            String pass_fragment = fragment.getString("fragment");
            switch (pass_fragment) {
                case "missing_pets":
                    binding.bottomNavigationView.setSelectedItemId(R.id.navigation_missing_pets);
                    replaceFragment(new MissingPets());
                    break;
                case "stray_pets":
                    binding.bottomNavigationView.setSelectedItemId(R.id.navigation_stray_pets);
                    replaceFragment(new StrayPets());
                    break;
                case "create_posts":
                    binding.bottomNavigationView.setSelectedItemId(R.id.navigation_create_posts);
                    replaceFragment(new CreatePosts());
                    break;
                case "profile":
                    binding.bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
                    replaceFragment(new Profile());
                    break;
            }
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_missing_pets:
                    replaceFragment(new MissingPets());
                    break;
                case R.id.navigation_stray_pets:
                    replaceFragment(new StrayPets());
                    break;
                case R.id.navigation_create_posts:
                    replaceFragment(new CreatePosts());
                    break;
                case R.id.navigation_profile:
                    replaceFragment(new Profile());
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // When permission granted
                replaceFragment(new MissingPets());
            }
        }
    }
}