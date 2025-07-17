package com.example.foodtrucklocationtracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtrucklocationtracker.adapter.TruckAdapter;
import com.example.foodtrucklocationtracker.model.FoodTruck;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private TruckAdapter truckAdapter;
    private ArrayList<FoodTruck> truckList;
    private GoogleMap mMap;

    private BottomSheetBehavior<View> bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Google Map fragment
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // RecyclerView setup
        recyclerView = findViewById(R.id.truckRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // BottomSheet setup
        View bottomSheet = findViewById(R.id.bottomSheetContainer);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(60);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        // Extra lock: auto revert if try hide
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // Optional for animation
            }
        });

        // FAB Add Truck
        FloatingActionButton fab = findViewById(R.id.fabAddTruck);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddTruckActivity.class);
            startActivity(intent);
        });

        // About Us button
        Button btnAbout = findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng defaultLocation = new LatLng(3.1390, 101.6869);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12));

        // Custom Info Window
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View view = getLayoutInflater().inflate(R.layout.custom_info_window, null);
                TextView title = view.findViewById(R.id.tvTitle);
                TextView snippet = view.findViewById(R.id.tvSnippet);
                title.setText(marker.getTitle());
                snippet.setText(marker.getSnippet());
                return view;
            }
        });

        loadFoodTrucks();
    }

    private void loadFoodTrucks() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        truckList = new ArrayList<>();

        db.collection("food_truck")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            FoodTruck truck = document.toObject(FoodTruck.class);
                            truckList.add(truck);

                            LatLng location = new LatLng(truck.getLatitude(), truck.getLongitude());
                            mMap.addMarker(new MarkerOptions()
                                    .position(location)
                                    .title(truck.getType())
                                    .snippet("Type: " + truck.getType()
                                            + "\nReported By: " + truck.getReportedBy()
                                            + "\nDate/Time: " + truck.getLastReported()));

                            builder.include(location);
                        }

                        truckAdapter = new TruckAdapter(truckList);
                        recyclerView.setAdapter(truckAdapter);

                        if (!truckList.isEmpty()) {
                            LatLngBounds bounds = builder.build();
                            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }

                    } else {
                        Log.e(TAG, "Error getting documents: ", task.getException());
                        Toast.makeText(MainActivity.this, "Failed to load trucks", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
