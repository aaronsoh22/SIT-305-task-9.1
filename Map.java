package com.example.lostandfoundapp;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;



public class Map extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        db = new DatabaseHelper(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mMap != null) {
            addMarkersForLostItems();
            addMarkersForFoundItems();
        }
    }


    private void addMarkersForLostItems() {
        Cursor cursor = db.getdatalost();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("Name"));
                @SuppressLint("Range") String location = cursor.getString(cursor.getColumnIndex("Location"));
                LatLng latLng = getLocationFromAddress(location);
                if (latLng != null) {
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Lost").snippet("Item: " + name + "\nLocation: " + location));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void addMarkersForFoundItems() {
        Cursor cursor = db.getdatafound();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("Name"));
                @SuppressLint("Range") String location = cursor.getString(cursor.getColumnIndex("Location"));
                LatLng latLng = getLocationFromAddress(location);
                if (latLng != null) {
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Found").snippet("Item: " + name + "\nLocation: " + location));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }



//    private void addMarkersForLostItems() {
//        Cursor cursor = db.getdatalost();
//        if (cursor.moveToFirst()) {
//            do {
//                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("Name"));
//                @SuppressLint("Range") String location = cursor.getString(cursor.getColumnIndex("Location"));
//                LatLng latLng = getLocationFromAddress(location);
//                if (latLng != null) {
//                    mMap.addMarker(new MarkerOptions().position(latLng).title("Lost: " + name));
//                }
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//    }
//
//    private void addMarkersForFoundItems() {
//        Cursor cursor = db.getdatafound();
//        if (cursor.moveToFirst()) {
//            do {
//                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("Name"));
//                @SuppressLint("Range") String location = cursor.getString(cursor.getColumnIndex("Location"));
//                LatLng latLng = getLocationFromAddress(location);
//                if (latLng != null) {
//                    mMap.addMarker(new MarkerOptions().position(latLng).title("Found: " + name));
//                }
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//    }

    private LatLng getLocationFromAddress(String address) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address addr = addresses.get(0);
                return new LatLng(addr.getLatitude(), addr.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

