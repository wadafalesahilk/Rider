package com.example.rider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RideRequestActivity extends AppCompatActivity {

    private EditText pickupLocation, destinationLocation;
    private Button btnRequestRide;
    private ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_request);

        // Initialize UI elements
        pickupLocation = findViewById(R.id.pickupLocation);
        destinationLocation = findViewById(R.id.destinationLocation);
        btnRequestRide = findViewById(R.id.btnRequestRide);
        loadingIndicator = findViewById(R.id.loadingIndicator);

        // Set click listener for the Request Ride button
        btnRequestRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRideRequest();
            }
        });
    }

    /**
     * Handle the ride request by validating inputs and simulating a request.
     */
    private void handleRideRequest() {
        String pickup = pickupLocation.getText().toString().trim();
        String destination = destinationLocation.getText().toString().trim();

        // Validate input fields
        if (pickup.isEmpty()) {
            Toast.makeText(this, "Please enter a pickup location.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (destination.isEmpty()) {
            Toast.makeText(this, "Please enter a destination.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading indicator
        loadingIndicator.setVisibility(View.VISIBLE);
        btnRequestRide.setEnabled(false);

        // Simulate a network request (e.g., Firebase or API integration)
        simulateRideRequest(pickup, destination);
    }

    /**
     * Simulates a ride request with a delay to mimic a network operation.
     *
     * @param pickup      The pickup location entered by the user.
     * @param destination The destination entered by the user.
     */
    private void simulateRideRequest(String pickup, String destination) {
        // Simulate a delay using a background thread
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Simulate network delay
                runOnUiThread(() -> {
                    // Hide loading indicator
                    loadingIndicator.setVisibility(View.GONE);
                    btnRequestRide.setEnabled(true);

                    // Show success message
                    Toast.makeText(RideRequestActivity.this,
                            "Ride requested from " + pickup + " to " + destination + ".",
                            Toast.LENGTH_LONG).show();

                    // Clear input fields
                    pickupLocation.setText("");
                    destinationLocation.setText("");
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    // Hide loading indicator and enable button
                    loadingIndicator.setVisibility(View.GONE);
                    btnRequestRide.setEnabled(true);

                    // Show error message
                    Toast.makeText(RideRequestActivity.this,
                            "Failed to request a ride. Please try again.", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
}
