package com.example.indusbustracker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.animation.Interpolator;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class Indus_Bus_Tracker extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;

    private LocationListener locationListener;
    static Location previousLocation;
    static long speed;
    private LocationManager locationManager;

    private final long MIN_TIME = 1000; // 1
    static LatLng latLng3;
    private final long MIN_DIST = 5; // 5 Meters
    static Marker mcl;
    static Bitmap bitmap;
    static float[] positions;
    static LatLng cl;
    static RadialGradient gradient;

    static int[] colors;
    static boolean mark = false;
    static long timeDifference;
    static Location location;
    static Canvas canvas;
    static Paint paint;
    static BitmapDescriptor cmarkerIcon;


    private static LatLng latLng;
    static double latitude;
    static FusedLocationProviderClient fusedLocationProviderClient;
    static long distance;
    static double longitude;
    static Location currentloc;
    static LocationManager locationManager3;
    static LatLng latLng1;
    static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    DrawerLayout drawerLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        drawerLayout = findViewById(R.id.my_drawer_layout1);
        ImageButton toggleButton = findViewById(R.id.toggle_sidebar_button);
        toggleButton.setOnClickListener(v -> {
            // Code that executes when the button is clicked
           drawerLayout.open();
           if(!drawerLayout.isOpen())
           {           drawerLayout.open();


           }
            Log.d("Indus Bus Tracker", "Opened");
            Toast.makeText(this, "Opened drawer", Toast.LENGTH_SHORT).show();

            //     sidebar(); // Call the sidebaropt method
        });
    }

    public static boolean isLocationEnabled(Context context) {
        locationManager3 = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager3 != null) {

            return locationManager3.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager3.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        }


        return false;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    private void sidebar() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        CancellationSignal cancellationSignal = new CancellationSignal();
        Executor executor = new Executor() {
            @Override
            public void execute(Runnable command) {
                new Handler(Looper.getMainLooper()).post(command);
            }
        };

        locationManager3.getCurrentLocation(LocationManager.GPS_PROVIDER, cancellationSignal, executor, new Consumer<Location>() {
            @Override
            public void accept(Location location) {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    latLng3 = new LatLng(latitude, longitude);
                    MarkerAnimation.animateMarkerToGB(mcl, latLng3, 1000, new FastOutSlowInInterpolator());

                    Toast.makeText(Indus_Bus_Tracker.this, "Updated", Toast.LENGTH_SHORT).show();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng3, 15.2F));
                }
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        LatLng iuni = new LatLng(23.0641313678088, 72.4397193291611);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(iuni, 12.98F));
        Marker marker = mMap.addMarker(new MarkerOptions().position(iuni).title("Indus University"));
        assert marker != null;
        marker.showInfoWindow();

        bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

// Create a Canvas object with the bitmap
        canvas = new Canvas(bitmap);

// Create a Paint object for drawing
        paint = new Paint();
        paint.setAntiAlias(true);

// Define the gradient colors
        colors = new int[]{Color.parseColor("#007FFF"), Color.parseColor("#00BFFF")};

// Define the gradient positions
        positions = new float[]{0, 1};

// Create a RadialGradient object
        gradient = new RadialGradient(50, 50, 50, colors, positions, Shader.TileMode.CLAMP);

// Set the shader of the paint object to the gradient
        paint.setShader(gradient);

// Draw a circle with the gradient background
        canvas.drawCircle(50, 50, 25, paint);

// Convert the bitmap to a BitmapDescriptor to use as a marker icon
        cmarkerIcon = BitmapDescriptorFactory.fromBitmap(bitmap);


        CancellationSignal cancellationSignal = new CancellationSignal();
        Executor executor = new Executor() {
            @Override
            public void execute(Runnable command) {
                new Handler(Looper.getMainLooper()).post(command);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager3.getCurrentLocation(LocationManager.GPS_PROVIDER, cancellationSignal, executor, new Consumer<Location>() {
            @Override
            public void accept(Location location) {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    latLng3 = new LatLng(latitude, longitude);
                    mcl = mMap.addMarker(new MarkerOptions()
                            .position(latLng3).icon(cmarkerIcon)
                            .title("You")); // Set marker color
                    assert mcl != null;
                    mcl.showInfoWindow();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng3, 15.2F));

                    mark=true;
                    mcl.showInfoWindow();
                    Toast.makeText(Indus_Bus_Tracker.this, "Updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

// Add a marker in Sydney and move the camera


            locationListener = new LocationListener() {

                @Override
                public void onLocationChanged(Location location) {
                    try {

                        if (ActivityCompat.checkSelfPermission(Indus_Bus_Tracker.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Indus_Bus_Tracker.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(Indus_Bus_Tracker.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                            ActivityCompat.requestPermissions(Indus_Bus_Tracker.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                        } else {
                            if (ActivityCompat.checkSelfPermission(Indus_Bus_Tracker.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Indus_Bus_Tracker.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                                LocationServices.getFusedLocationProviderClient(Indus_Bus_Tracker.this).getLocationAvailability().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        LocationAvailability availability = task.getResult();
                                        if (!Indus_Bus_Tracker.isLocationEnabled(Indus_Bus_Tracker.this)) {
                                            if (!availability.isLocationAvailable()) {
                                                Toast.makeText(Indus_Bus_Tracker.this, "Turn on Location", Toast.LENGTH_LONG).show();

                                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                                startActivity(intent);


                                            }
                                        }
                                    } else {
                                        Toast.makeText(Indus_Bus_Tracker.this, "Location is not available", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                if (isLocationEnabled(Indus_Bus_Tracker.this)) {
                                }
                            }








                         Location Gpslocation = locationManager3.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                                    if (Gpslocation != null) {
                                        latitude = Gpslocation.getLatitude();
                                        longitude = Gpslocation.getLongitude();
                                        latLng3 = new LatLng(latitude, longitude);
                                        if (previousLocation != null) {
                                            // Calculate speed
                                            distance = (long) location.distanceTo(previousLocation);
                                            timeDifference = (location.getTime() - previousLocation.getTime()) / 1000; // in seconds
                                            speed = distance / timeDifference;

                                        }

                                        previousLocation = Gpslocation;
                                     if(mcl!=null) {
                                         MarkerAnimation.animateMarkerToGB(mcl, latLng3, 1000, new FastOutSlowInInterpolator());
                                         Toast.makeText(Indus_Bus_Tracker.this, "Updated", Toast.LENGTH_SHORT).show();
                                     }

                                    }

                            }


                    }
                    catch (SecurityException e){
                        e.printStackTrace();
                    }

                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };
            locationManager3 = (LocationManager) getSystemService(LOCATION_SERVICE);

            try {
                locationManager3.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
            }
            catch (SecurityException e){
                e.printStackTrace();
            }
        }

/*
    private GoogleMap mMap;
    public static boolean isLocationEnabled(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if(locationManager != null){

            return  locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        }


        return false;
    }
private void turnongps(){
       AlertDialog.Builder builder=new AlertDialog.Builder(Indus_Bus_Tracker.this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        final AlertDialog dialog=builder.create();
        dialog.show();
}

    boolean rec;
    static Location currentloc;
    static Location mLastLocation;
    static Marker mcl;
    Marker mk;
    static Bitmap bitmap;
    static float[] positions;
    static LatLng cl;
    static RadialGradient gradient;

    static int[] colors;
static Canvas canvas;
static Paint paint;
static    BitmapDescriptor cmarkerIcon;

    static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    static String lat,lon;

    //api
    private FusedLocationProviderClient fusedLocationClient;
    public LocationRequest locationRequest;
    static LocationManager locationManager3;
    static double latitude;
    static double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        // Initialize Firebase Database




// Add marker to the map with the custom marker icon
        super.onCreate(savedInstanceState);

        com.example.indusbustracker.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
      //  getCurrentLocation();
        ImageButton toggleButton = findViewById(R.id.toggle_sidebar_button);


        toggleButton.setOnClickListener(v -> {
            // Code that executes when the button is clicked
            sidebar(); // Call the sidebaropt method
        }); toggleButton.setOnClickListener(v -> {
            // Code that executes when the button is clicked
            sidebar(); // Call the sidebaropt method
        });
        // Check and request location permissions if not granted
        if (!hasLocationPermission()) {
            requestLocationPermission();
        }




        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

        }
        LocationServices.getFusedLocationProviderClient(this).getLocationAvailability().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                LocationAvailability availability = task.getResult();
                if(!isLocationEnabled(this)) {
                    if (availability.isLocationAvailable()) {
                        rec = false;
                    } else {
                        rec = true;
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);

                    }
                }
            } else {
                Toast.makeText(Indus_Bus_Tracker.this, "", Toast.LENGTH_SHORT).show();
            }

        });


        if(rec){

        Intent intent = new Intent(this, Indus_Bus_Tracker.class);
        startActivity(intent);}
    }


    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
        else{

            Location GpsLocation = locationManager3.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (GpsLocation!=null){
                latitude=GpsLocation.getLatitude();
                longitude = GpsLocation.getLongitude();
               lat = String.valueOf(latitude);
                lon = String.valueOf(longitude);

            }
        }
        Thread getcloc = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(300); // Delay of 500 milliseconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                Indus_Bus_Tracker.this.runOnUiThread(() -> {


                  if (ActivityCompat.checkSelfPermission(Indus_Bus_Tracker.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Indus_Bus_Tracker.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        LocationServices.getFusedLocationProviderClient(this).getLocationAvailability().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                LocationAvailability availability = task.getResult();
                                if(isLocationEnabled(this)) {
                                    if (availability.isLocationAvailable()) {

                                        CancellationToken token=new CancellationToken() {
                                            @NonNull
                                            @Override
                                            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                                                return null;
                                            }

                                            @Override
                                            public boolean isCancellationRequested() {
                                                return false;
                                            }
                                        };
                                        Task<Location> task1 = fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY,token);
                                        task1.addOnSuccessListener(location -> {
                                            if (location != null) {
                                                currentloc = location;

                                            }});

                                    }
                                }
                            }
                        });

                    }

                });
            }
        });
        getcloc.start();
        CancellationToken token=new CancellationToken() {
            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }

            @Override
            public boolean isCancellationRequested() {
                return false;
            }
        };
        Task<Location> task = fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY,token);
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentloc = location;

            }
            if(location!=null){
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                assert mapFragment != null;
                mapFragment.getMapAsync(Indus_Bus_Tracker.this);
            }
        });

    }


    private void sidebar() {


            // Add a marker with your desired color and title
        bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

// Create a Canvas object with the bitmap
        canvas = new Canvas(bitmap);

// Create a Paint object for drawing
        paint = new Paint();
        paint.setAntiAlias(true);

// Define the gradient colors
        colors = new int[]{Color.parseColor("#007FFF"), Color.parseColor("#00BFFF")};

// Define the gradient positions
        positions = new float[]{0, 1};

// Create a RadialGradient object
        gradient = new RadialGradient(50, 50, 50, colors, positions, Shader.TileMode.CLAMP);

// Set the shader of the paint object to the gradient
        paint.setShader(gradient);

// Draw a circle with the gradient background
        canvas.drawCircle(50, 50, 25, paint);

// Convert the bitmap to a BitmapDescriptor to use as a marker icon
        cmarkerIcon = BitmapDescriptorFactory.fromBitmap(bitmap);
        cl = new LatLng(currentloc.getLatitude(), currentloc.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cl, 15.2F));
    }

    private boolean hasLocationPermission() {



        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }
    // Assuming Google Play Services is available and configured


    // Method to request location permissions
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
        LocationServices.getFusedLocationProviderClient(this).getLocationAvailability().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                LocationAvailability availability = task.getResult();
                if(!isLocationEnabled(this)) {
                    if (!availability.isLocationAvailable()) {


                        Toast.makeText(this, "Turn on Location", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);


                    }
                }
            } else {
                Toast.makeText(Indus_Bus_Tracker.this, "", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start location updates
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
               // getCurrentLocation();
            } else {

            }
        }
    }

    private void animateMarkerToNewLocation() {
        if(mLastLocation!=currentloc) {
            LatLng endLatLng = new LatLng(currentloc.getLatitude(), currentloc.getLongitude());
            // Create MarkerAnimation object with animation duration and easing
            MarkerAnimation.animateMarkerToGB(mcl, endLatLng, 1000, new LatLngInterpolator.Linear());
            Log.d("Indus Bus Tracker", "Updated");
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationServices.getFusedLocationProviderClient(this).getLocationAvailability().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    LocationAvailability availability = task.getResult();
                    if (!isLocationEnabled(this)) {
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

        }

            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));

            UiSettings uiSettings = mMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(true);
            uiSettings.setMyLocationButtonEnabled(true);
            uiSettings.setMapToolbarEnabled(true);
            // Add a marker in Sydney and move the camera

         */
/*

            bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

// Create a Canvas object with the bitmap
            canvas = new Canvas(bitmap);

// Create a Paint object for drawing
            paint = new Paint();
            paint.setAntiAlias(true);

// Define the gradient colors
            colors = new int[]{Color.parseColor("#007FFF"), Color.parseColor("#00BFFF")};

// Define the gradient positions
            positions = new float[]{0, 1};

// Create a RadialGradient object
            gradient = new RadialGradient(50, 50, 50, colors, positions, Shader.TileMode.CLAMP);

// Set the shader of the paint object to the gradient
            paint.setShader(gradient);

// Draw a circle with the gradient background
            canvas.drawCircle(50, 50, 25, paint);

// Convert the bitmap to a BitmapDescriptor to use as a marker icon
            cmarkerIcon = BitmapDescriptorFactory.fromBitmap(bitmap);
        if (isLocationEnabled(this)) {
            cl = new LatLng(currentloc.getLatitude(), currentloc.getLongitude());

            mcl = mMap.addMarker(new MarkerOptions()
                    .position(cl)
                    .title("You")
                    .icon(cmarkerIcon)); // Set marker color

            assert mcl != null;
            mcl.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cl, 15.2F));
            EditText editTextMessage = findViewById(R.id.editTextMessage);

            editTextMessage.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Perform your search operation here
                    Log.d("indus","search");
                    return true;
                }
                return false;
            });

            // Delay of 2 seconds
            // Store current location in mLastLocation
            // Fetch the new current location
            // Use the new current location as needed (e.g., update marker, perform actions)
            Thread locationUpdaterThread = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(2500); // Delay of 2 seconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(() -> {
                        // Store current location in mLastLocation

                            mLastLocation = currentloc;

                            // Fetch the new current location
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                            }
                        CancellationToken token=new CancellationToken() {
                            @NonNull
                            @Override
                            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                                return null;
                            }

                            @Override
                            public boolean isCancellationRequested() {
                                return false;
                            }
                        };
                        Task<Location> task = fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY,token);
                            task.addOnSuccessListener(location -> {
                                if (location != null) {
                                    currentloc = location;

                                }
                                animateMarkerToNewLocation();
                            });

                    });
                }
            });

            locationUpdaterThread.start();
            Log.d("indus", "success");
        }
    }
  */  }



    class MarkerAnimation {

        public static void animateMarkerToGB(final Marker marker, final LatLng finalPosition, final long duration, final FastOutSlowInInterpolator latLngInterpolator) {
            final LatLng startPosition = marker.getPosition();
            final Handler handler;
            handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final Interpolator interpolator = new FastOutSlowInInterpolator();

            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);
                    double lng = t * finalPosition.longitude + (1 - t) * startPosition.longitude;
                    double lat = t * finalPosition.latitude + (1 - t) * startPosition.latitude;

                    marker.setPosition(new LatLng(lat, lng));
                    marker.setVisible(true);


                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 100);
                    }
                }
            };

            handler.post(runnable);
        }
    }

    // Define the LatLngInterpolator interface
    interface LatLngInterpolator {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class Linear implements LatLngInterpolator {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lng = (b.longitude - a.longitude) * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
        class FastOutSlowInInterpolator implements LatLngInterpolator {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double t = (Math.pow(fraction, 2) - (2 * fraction) + 1);
                double lat = (b.latitude - a.latitude) * t + a.latitude;
                double lng = (b.longitude - a.longitude) * t + a.longitude;
                return new LatLng(lat, lng);
            }
        }

    }


