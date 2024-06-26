package com.example.parks;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.parks.adapter.CustomInfoWindow;
import com.example.parks.data.AsyncResponse;
import com.example.parks.data.Repository;
import com.example.parks.model.Park;
import com.example.parks.model.ParkViewModel;
import com.example.parks.util.Util;
import com.example.todo_application.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.todo_application.databinding.ActivityMapsBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private ParkViewModel parkViewModel;
    private List<Park> parkList;
    private CardView cardView;
    private EditText stateCodeEt;
    private ImageButton searchButton;
    private String code = "wa";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        parkViewModel = new ViewModelProvider(this)
                .get(ParkViewModel.class);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        cardView = findViewById(R.id.cardView);
        stateCodeEt = findViewById(R.id.floating_statecode_edittext);
        searchButton = findViewById(R.id.floating_search_button);
        // Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();
            if(id == R.id.maps_nav_button){
                if(cardView.getVisibility() == View.INVISIBLE || cardView.getVisibility() == View.GONE){
                    cardView.setVisibility(View.VISIBLE);
                }
                parkList.clear();
                mMap.clear();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.map,mapFragment)      //mapFragment
                .commit();
                mapFragment.getMapAsync(MapsActivity.this);
                return true;

                //show the map view
            }else if(id==R.id.parks_nav_btn){
                //show the parks view
                selectedFragment = ParksFragment.newInstance();
                cardView.setVisibility(View.GONE);
            }

            // Replacing fragment with each other on click
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.map,selectedFragment)
                    .commit();

            return true;
        });

        searchButton.setOnClickListener(v -> {
            String stateCode = stateCodeEt.getText().toString().trim();
            Util.hideofSoftKeyboard(v);
            parkList.clear();
            if(!TextUtils.isEmpty(stateCode)){
                code = stateCode;
                parkViewModel.selectCode(code);
                onMapReady(mMap); //refresh map
                stateCodeEt.setText("");
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new CustomInfoWindow(getApplicationContext()));
        mMap.setOnInfoWindowClickListener(this);
        parkList = new ArrayList<>();
        parkList.clear();

        // Add a marker in Sydney and move the camera


        populateMap();
    }

    private void populateMap() {
        mMap.clear(); // important! Clear the map!
        Repository.getParks(new AsyncResponse() {
            @Override
            public void processPark(List<Park> parks) {
                parkList = parks;
                for(Park park: parks){
                    Log.d("Parks: ", "processPark: "+park.getFullName());

                    LatLng location = new LatLng(Double.parseDouble(park.getLatitude()), Double.parseDouble(park.getLongitude()));
                    MarkerOptions markerOptions= new MarkerOptions()
                            .position(location)
                            .title(park.getName())
                            .snippet(park.getStates());
                    Marker marker = mMap.addMarker(markerOptions);
                    marker.setTag(park);
//                    mMap.addMarker(markerOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,5));
                }
                parkViewModel.setSelectedParks(parkList);
            }
        },code);
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        cardView.setVisibility(View.GONE);
        // goto Details Freagment
        goToDetails(marker);
    }

    private void goToDetails(@NonNull Marker marker) {
        parkViewModel.setSelectedPark((Park) marker.getTag());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.map,DetailsFragment.newInstance())
                .commit();
    }
}