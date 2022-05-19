package com.example.comp4521;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.comp4521.model.User;
import com.example.comp4521.viewmodel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Timestamp;

public class Signup extends AppCompatActivity {
    private Button backInButton;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private EditText confirmPasswordEditText;
    private Button signUpButton;
    private ProgressBar loadingProgressBar;
    private UserViewModel userViewModel;
    private static final String TAG = "Signup";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        backInButton = findViewById(R.id.backBtn);
        signUpButton = findViewById(R.id.signUpBtn);
        usernameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirm_password);
        loadingProgressBar = findViewById(R.id.progressBar);
        loadingProgressBar.setVisibility(View.GONE);

        userViewModel = new UserViewModel(this);
        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        backInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLandingActivity();
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                String password = passwordEditText.getText().toString();
                String conf_password = confirmPasswordEditText.getText().toString();
                if (confirmPasswordEditText.getText().toString().isEmpty() || usernameEditText.getText().toString().isEmpty() || emailEditText.getText().toString().isEmpty() || passwordEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Input should not be empty", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(conf_password)) {
                    Toast.makeText(getApplicationContext(), "Your passwords are not the same", Toast.LENGTH_SHORT).show();
                } else {
                    createAccount(emailEditText.getText().toString(), passwordEditText.getText().toString(), usernameEditText.getText().toString());
                }
                loadingProgressBar.setVisibility(View.GONE);
            }
        });
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            reload();
        }
    }

    private void reload() {
    }

    private void checkInputs(String password, String confirm_password) {

    }

    // [END on_start_check_user]
    private void createAccount(String newUserEmail, String newUserPassword, String newUserName) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(newUserEmail, newUserPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loadingProgressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "Success to Create User");
                            Toast.makeText(getApplicationContext(), "Success to Create User with Email", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            User rtDbUser = new User();
                            rtDbUser.setName(newUserName);
                            rtDbUser.setUserEmail(newUserEmail);
                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                            rtDbUser.setCreatedDateTime(timestamp.getTime());
                            userViewModel.createNewUser(rtDbUser);

                            Toast.makeText(getApplicationContext(), "Please login", Toast.LENGTH_LONG).show();
                            goLandingActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "Failure to Create User", task.getException());
                            Toast.makeText(getApplicationContext(), "Failure to Create User", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void updateUI(FirebaseUser user) {

    }

    public void goLandingActivity() {
        Intent intent = new Intent(this, Landing.class);
        startActivity(intent);
    }
}