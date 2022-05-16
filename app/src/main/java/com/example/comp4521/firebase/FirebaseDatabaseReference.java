package com.example.comp4521.firebase;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseReference {
    private FirebaseDatabaseReference() {
    }

    public static final FirebaseDatabase DATABASE = FirebaseDatabase.getInstance();
}
