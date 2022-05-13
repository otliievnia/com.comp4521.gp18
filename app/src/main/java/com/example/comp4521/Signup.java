package com.example.comp4521;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Signup extends AppCompatActivity {
    private Button backInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        backInButton = findViewById(R.id.backBtn);
        backInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLandingActivity();
            }
        });
    }
    public void goLandingActivity() {
        Intent intent = new Intent(this, Landing.class);
        startActivity(intent);
    }
}