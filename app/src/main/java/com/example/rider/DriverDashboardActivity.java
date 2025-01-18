package com.example.rider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rider.R;

public class DriverDashboardActivity extends AppCompatActivity {

    private Switch switchDriverStatus;
    private TextView textDriverStatus;
    private TextView textEarnings;
    private TextView textActiveRideDetails;
    private Button btnStartRide;
    private Button btnEndRide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dashboard);

        // Initialize UI elements
        switchDriverStatus = findViewById(R.id.switchDriverStatus);
        textDriverStatus = findViewById(R.id.textDriverStatus);
        textEarnings = findViewById(R.id.textEarnings);
        textActiveRideDetails = findViewById(R.id.textActiveRideDetails);
        btnStartRide = findViewById(R.id.btnStartRide);
        btnEndRide = findViewById(R.id.btnEndRide);

        // Set initial states
        textDriverStatus.setText(switchDriverStatus.isChecked() ? "Online" : "Offline");
        textEarnings.setText("$0.00");
        textActiveRideDetails.setText("No active ride.");

        // Switch listener to toggle driver status
        switchDriverStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String status = isChecked ? "Online" : "Offline";
            textDriverStatus.setText(status);
            Toast.makeText(DriverDashboardActivity.this, "Status: " + status, Toast.LENGTH_SHORT).show();
        });

        // Button to start a ride
        btnStartRide.setOnClickListener(v -> {
            textActiveRideDetails.setText("Ride in progress...");
            Toast.makeText(DriverDashboardActivity.this, "Ride started!", Toast.LENGTH_SHORT).show();
        });

        // Button to end a ride
        btnEndRide.setOnClickListener(v -> {
            textActiveRideDetails.setText("No active ride.");
            Toast.makeText(DriverDashboardActivity.this, "Ride ended!", Toast.LENGTH_SHORT).show();
        });
    }
}
