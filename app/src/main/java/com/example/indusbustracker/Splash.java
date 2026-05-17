package com.example.indusbustracker;

import static com.example.indusbustracker.Indus_Bus_Tracker.LOCATION_PERMISSION_REQUEST_CODE;
import static com.example.indusbustracker.Indus_Bus_Tracker.isLocationEnabled;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;

public class Splash extends AppCompatActivity {
    boolean start= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();

// Set the 'signal' variable


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this,"Allow Permission", Toast.LENGTH_LONG).show();

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);


            }

        Thread starttrack = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(3000); // Delay of 500 milliseconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (start) {
                    break; // Exit the loop if start is true
                }

                Splash.this.runOnUiThread(() -> {
                    if (!start) {
                        // Store current location in mLastLocation
                        if (ActivityCompat.checkSelfPermission(Splash.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Splash.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                            LocationServices.getFusedLocationProviderClient(this).getLocationAvailability().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    LocationAvailability availability = task.getResult();
                                    if(!Indus_Bus_Tracker.isLocationEnabled(this)) {
                                        if (!availability.isLocationAvailable()) {


                                            Toast.makeText(this, "Turn on Location", Toast.LENGTH_LONG).show();

                                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            startActivity(intent);


                                        }
                                    }
                                } else {
                                    Toast.makeText(this, "Unexpected Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                            if(isLocationEnabled(this)) {

                                startRegActivity();

                            Log.d("Indus","thread");
                            start = true;
                            Splash.this.finish();}
                        }
                    }
                });
            }
        });
        starttrack.start();

        if (start) {
            starttrack.interrupt(); // Signal the thread to stop
        }


    }






    private void startRegActivity() {
        Intent intent = new Intent(this, register.class);
        startActivity(intent);
        finish(); // Finish this activity to prevent returning to it when pressing the back button from MainActivity
    }
    private void starttrackActivity() {
        Intent intent = new Intent(this, Indus_Bus_Tracker.class);
        startActivity(intent);
        finish(); // Finish this activity to prevent returning to it when pressing the back button from MainActivity
    }
}


