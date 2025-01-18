package com.example.rider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class ActiveRideActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView textRideStatus, textDriverName, textCarDetails, textEstimatedTime;
    private Button btnCallDriver, btnCancelRide;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_ride);

        // Initialize UI elements
        textRideStatus = findViewById(R.id.textRideStatus);
        textDriverName = findViewById(R.id.textDriverName);
        textCarDetails = findViewById(R.id.textCarDetails);
        textEstimatedTime = findViewById(R.id.textEstimatedTime);
        btnCallDriver = findViewById(R.id.btnCallDriver);
        btnCancelRide = findViewById(R.id.btnCancelRide);

        // Set up the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapPlaceholder);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Set up button listeners
        btnCallDriver.setOnClickListener(view -> {
            String driverPhone = "+1234567890"; // Replace with actual driver phone number
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + driverPhone));
            startActivity(callIntent);
        });

        btnCancelRide.setOnClickListener(view -> {
            Toast.makeText(ActiveRideActivity.this, "Ride canceled successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
        });

        // Simulate setting dynamic ride details
        setRideDetails("John Doe", "Toyota Prius - ABC 1234", "10 mins");
    }

    private void setRideDetails(String driverName, String carDetails, String eta) {
        textDriverName.setText("Driver: " + driverName);
        textCarDetails.setText("Car: " + carDetails);
        textEstimatedTime.setText("ETA: " + eta);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Customize map settings
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // TODO: Add markers, routes, or other map features as needed
    }
}
