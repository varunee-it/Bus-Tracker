package com.example.indusbustracker;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper extends Application {

    private static final String TAG = "FirebaseHelper";
    private static DatabaseReference databaseReference;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeFirebase();
    }

    public void initializeFirebase() {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("") // Replace with your app ID
                .setApiKey("") // Replace with your API key
                // ... other required options
                .build();
        FirebaseApp.initializeApp(this, options, "my-app"); // Name doesn't matter much
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }


    public static DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

}
