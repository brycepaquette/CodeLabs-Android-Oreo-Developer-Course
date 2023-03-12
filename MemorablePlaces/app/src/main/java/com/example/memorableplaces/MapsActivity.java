package com.example.memorableplaces;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.memorableplaces.databinding.ActivityMapsBinding;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    LocationManager locationManager;
    LocationListener locationListener;

    public void moveMapsToLocation(Location location, String title) {
        if (location != null) {
            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
//            mMap.clear();
            mMap.addMarker(new MarkerOptions().title(title).position(currentLocation));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //Grant location permissions required for gMaps
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = getIntent();
        int int1 = intent.getIntExtra("placeNumber", 0);
        if (intent.getIntExtra("placeNumber", 0) == 0) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
//                    moveMapsToLocation(location, "Your Location");
                }
            };

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            } else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            moveMapsToLocation(location, "Your Location");
        }
        else {
            Location placeLocation = new Location(LocationManager.GPS_PROVIDER);
            placeLocation.setLatitude(MainActivity.latitudes.get(intent.getIntExtra("placeNumber", 0)));
            placeLocation.setLongitude(MainActivity.longitudes.get(intent.getIntExtra("placeNumber", 0)));
            moveMapsToLocation(placeLocation, MainActivity.locArray.get(intent.getIntExtra("placeNumber", 0)));
        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                List<Address> addrList = null;
                String address;
                SharedPreferences sharedPreferences = MapsActivity.this.getSharedPreferences("com.example.sharedpreferences", Context.MODE_PRIVATE);
                Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                try {
                     addrList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (addrList.size() == 0) {
                    address = LocalDate.now().toString();
                    mMap.addMarker(new MarkerOptions().position(latLng).title(address));
                }
                else if (addrList.get(0).getThoroughfare() == null || addrList.get(0).getThoroughfare() == "Unnamed Road" || addrList.get(0).getFeatureName().equals(addrList.get(0).getThoroughfare())) {
                    address = addrList.get(0).getFeatureName();
                    mMap.addMarker(new MarkerOptions().position(latLng).title(address));
                }
                else {

                    address = String.format("%s %s", addrList.get(0).getFeatureName(), addrList.get(0).getThoroughfare());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(address));
                }
                MainActivity.locArray.add(address);
                MainActivity.latitudes.add(latLng.latitude);
                MainActivity.longitudes.add(latLng.longitude);
                try {
                    String serializedLocArray = ObjectSerializer.serialize(MainActivity.locArray);
                    sharedPreferences.edit().putString("locArray", serializedLocArray).apply();
                    String serializedLatitudes = ObjectSerializer.serialize(MainActivity.latitudes);
                    sharedPreferences.edit().putString("latitudes", serializedLatitudes).apply();
                    String serializedLongitudes = ObjectSerializer.serialize(MainActivity.longitudes);
                    sharedPreferences.edit().putString("longitudes", serializedLongitudes).apply();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }


                Toast.makeText(MapsActivity.this, "Location Saved!", Toast.LENGTH_SHORT).show();
                MainActivity.arrayAdapter.notifyDataSetChanged();
            }
        });
    }
}