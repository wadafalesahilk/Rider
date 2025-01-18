package com.example.rider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class UserHomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    ImageView iconMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        // Initialize Views
        ImageView iconLogout = findViewById(R.id.iconLogout);
        ImageView iconNotification = findViewById(R.id.iconNotification);
        iconMenu = findViewById(R.id.iconMenu);
        FloatingActionButton fabCurrentLocation = findViewById(R.id.fabCurrentLocation);
        EditText editTextSearch = findViewById(R.id.editTextSearch);

        // Initialize Google Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapContainer);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Toast.makeText(this, "Error: Map Fragment is null!", Toast.LENGTH_SHORT).show();
        }

        // Initialize FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Logout Button Click
        iconLogout.setOnClickListener(view -> {
            Toast.makeText(UserHomeActivity.this, "Logging out...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserHomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Menu Button Click
        iconMenu.setOnClickListener(this::showMenu);

        // Notification Button Click
        iconNotification.setOnClickListener(view -> Toast.makeText(UserHomeActivity.this, "Opening Notifications", Toast.LENGTH_SHORT).show());

        // Floating Action Button for Current Location
        fabCurrentLocation.setOnClickListener(view -> fetchCurrentLocation());

        // Search Location
        findViewById(R.id.iconSearch).setOnClickListener(view -> searchLocation(editTextSearch.getText().toString()));
    }
    @SuppressLint("NonConstantResourceId")
    private void showMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);

        // Inflate the menu
        popupMenu.getMenuInflater().inflate(R.menu.menu_user_home, popupMenu.getMenu());

        // Handle menu item clicks
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_settings) {
                Toast.makeText(UserHomeActivity.this, "Settings clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.action_help) {
                Toast.makeText(UserHomeActivity.this, "Help clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.action_about) {
                Toast.makeText(UserHomeActivity.this, "About clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.action_driver_dashboard) {
                // Navigate to Driver Dashboard Activity
                Intent intent = new Intent(UserHomeActivity.this, DriverDashboardActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.action_ride_request) {
                // Navigate to Ride Request Activity
                Intent intent = new Intent(UserHomeActivity.this, RideRequestActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.action_active_ride) {
                // Navigate to Active Ride Activity
                Intent intent = new Intent(UserHomeActivity.this, ActiveRideActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }




    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Enable My Location Layer if permission granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            requestLocationPermission();
        }
    }

    private void fetchCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.clear(); // Clear existing markers
                mMap.addMarker(new MarkerOptions().position(currentLatLng).title("You are here!"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
            } else {
                Toast.makeText(UserHomeActivity.this, "Unable to fetch location. Make sure location is enabled.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(UserHomeActivity.this, "Failed to fetch location: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void searchLocation(String locationName) {
        if (locationName == null || locationName.isEmpty()) {
            Toast.makeText(this, "Please enter a location name.", Toast.LENGTH_SHORT).show();
            return;
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocationName(locationName, 1);
            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.clear(); // Clear existing markers
                mMap.addMarker(new MarkerOptions().position(latLng).title(locationName));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            } else {
                Toast.makeText(this, "Location not found. Try again.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error fetching location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
