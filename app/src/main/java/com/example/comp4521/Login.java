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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private Button backInButton;
    private EditText emailEditText ;
    private EditText passwordEditText ;
    private Button loginButton;
    private ProgressBar loadingProgressBar;
    private static final String TAG = "Login";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        backInButton = findViewById(R.id.backBtn);
        loginButton = findViewById(R.id.signInBtn);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loadingProgressBar = findViewById(R.id.progressBarLogin);
        loadingProgressBar.setVisibility(View.GONE);

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
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                String password = passwordEditText.getText().toString();
                if ( emailEditText.getText().toString().isEmpty() || passwordEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Input should not be empty", Toast.LENGTH_SHORT).show();
                }else {
                    signIn( emailEditText.getText().toString(),  passwordEditText.getText().toString());
                }
                loadingProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loadingProgressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(getApplicationContext(), "Authentication success", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            // TODO: Move to next page
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    public void goLandingActivity() {
        Intent intent = new Intent(this, Landing.class);
        startActivity(intent);
    }
    private void reload() { }

    private void updateUI(FirebaseUser user) {

    }
}