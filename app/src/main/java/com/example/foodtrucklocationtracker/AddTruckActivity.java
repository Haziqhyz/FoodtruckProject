package com.example.foodtrucklocationtracker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodtrucklocationtracker.model.FoodTruck;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddTruckActivity extends AppCompatActivity {

    private EditText edtType, edtLatitude, edtLongitude, edtReporter, edtLastReported;
    private Button btnSave, btnCancel;  // ✅ Add btnCancel

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_truck);

        edtType = findViewById(R.id.edtType);
        edtLatitude = findViewById(R.id.edtLatitude);
        edtLongitude = findViewById(R.id.edtLongitude);
        edtReporter = findViewById(R.id.edtReporter);
        edtLastReported = findViewById(R.id.edtLastReported);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);  // ✅ Bind Cancel button

        btnSave.setOnClickListener(v -> saveTruck());

        // ✅ Cancel logic: finish activity
        btnCancel.setOnClickListener(v -> finish());
    }

    private void saveTruck() {
        String type = edtType.getText().toString();
        String reporter = edtReporter.getText().toString();
        String lastReported = edtLastReported.getText().toString();

        double latitude;
        double longitude;

        try {
            latitude = Double.parseDouble(edtLatitude.getText().toString());
            longitude = Double.parseDouble(edtLongitude.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid latitude and longitude", Toast.LENGTH_SHORT).show();
            return;
        }

        FoodTruck truck = new FoodTruck();
        truck.setType(type);
        truck.setReportedBy(reporter);
        truck.setLastReported(lastReported);
        truck.setLatitude(latitude);
        truck.setLongitude(longitude);

        FirebaseFirestore.getInstance().collection("food_truck")
                .add(truck)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Truck added", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
