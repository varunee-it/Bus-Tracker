package com.example.indusbustracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class register extends AppCompatActivity {
    public static boolean driver;
    public static boolean firstrun;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference usersRef = database.getReference("users"); // Replace "user1" with your desired user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        boolean fr = preferences.getBoolean("firstrun",true);

            if(!fr){
               Intent intent = new Intent(this, Indus_Bus_Tracker.class);
               startActivity(intent);
finish();
                SharedPreferences.Editor editor = preferences.edit();


// Set the 'signal' variable
                firstrun = false; // Set your desired value
                editor.putBoolean("firstrun", firstrun);
                editor.apply();
            }





// Set the 'signal' variable


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_register);





        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Process retrieved data
                    User user = snapshot.getValue(User.class); // Adjust based on your data structure
                    // Do something with the user data, e.g., update UI elements
                } else {
                    // Handle the case where no data is found
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }
    public void registerUser(View view) {
        // Get user information from input fields
      /*  String name = ((EditText) findViewById(R.id.nameEditText)).getText().toString();
        String busNo = ((EditText) findViewById(R.id.busNoEditText)).getText().toString();
        String phoneNo = ((EditText) findViewById(R.id.phoneNoEditText)).getText().toString();

        // Use push() to generate a unique child key for the new user
        String key = usersRef.push().getKey();

        // Create a new User object
        User user = new User(name, busNo, phoneNo, currentloc.getLatitude(), currentloc.getLongitude());

        // Set the automatically generated key for the User object
        user.setKey(key);

        // Write user data to the database with the generated key
        usersRef.child(key).setValue(user);

       */
        Intent intent = new Intent(this, Indus_Bus_Tracker.class);
        startActivity(intent);
        finish();
        driver = true; // Set your desired value
        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("driver", driver);
        editor.apply();
        Log.d("reg","d");
        // Display success message or handle errors (optional)
    }


    public void setreg(View view) {
        setContentView(R.layout.activity_register);

        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

// Set the 'signal' variable

        SharedPreferences.Editor editor = preferences.edit();
// Set the 'signal' variable
        firstrun = false; // Set your desired value
        editor.putBoolean("firstrun", firstrun);
        editor.putString("user","driver");
        editor.apply();

    }

    public void starttrack(View view) {
        Intent intent = new Intent(this, Indus_Bus_Tracker.class);
        startActivity(intent);
        finish();
        Log.d("reg","s");
        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();


// Set the 'signal' variable
        firstrun = false; // Set your desired value
        editor.putBoolean("firstrun", firstrun);
        editor.putString("user","student");
        editor.apply();
// Set the 'signal' variable
        driver = false; // Set your desired value
        editor.putBoolean("driver", driver);
        editor.apply();

    }
}