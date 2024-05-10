//package com.example.lostandfoundapp;
//
//import android.content.Intent;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.RadioButton;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class NewAdvert extends AppCompatActivity {
//    DatabaseHelper db;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_new_advert);
//        db = new DatabaseHelper(this);
//
//        Button save = findViewById(R.id.buttonsave);
//        Button location = findViewById(R.id.buttonloc);
//        EditText name = findViewById(R.id.editTextname);
//        EditText phone = findViewById(R.id.editTextphone);
//        EditText desc = findViewById(R.id.editTextDescription);
//        EditText Date = findViewById(R.id.editTextDate);
//        EditText loc = findViewById(R.id.editTextLocation);
//        RadioButton radiolost= findViewById(R.id.radioButtonLost);
//        RadioButton radiofound= findViewById(R.id.radioButtonFound);
//
//
//
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String Name = name.getText().toString();
//                String Phone = phone.getText().toString();
//                String descr = desc.getText().toString();
//                String date = Date.getText().toString();
//                String Loc = loc.getText().toString();
//
//                if (radiolost.isChecked()) {
//                    Boolean checkinsertdata = db.savedatalost(Name, Phone, descr, date, Loc);
//                    if (checkinsertdata)
//                        Toast.makeText(NewAdvert.this, "New Lost Entry Inserted", Toast.LENGTH_SHORT).show();
//                    else
//                        Toast.makeText(NewAdvert.this, "New Lost Entry Not Inserted", Toast.LENGTH_SHORT).show();
//                } else if (radiofound.isChecked()) {
//                    Boolean checkinsertdata = db.savedatafound(Name, Phone, descr, date, Loc);
//                    if (checkinsertdata)
//                        Toast.makeText(NewAdvert.this, "New Found Entry Inserted", Toast.LENGTH_SHORT).show();
//                    else
//                        Toast.makeText(NewAdvert.this, "New Found Entry Not Inserted", Toast.LENGTH_SHORT).show();
//                }
//            } });
//
//
//
//
//
//    }
//}
//

package com.example.lostandfoundapp;

import static android.content.ContentValues.TAG;
import static androidx.core.location.LocationManagerCompat.getCurrentLocation;

import android.Manifest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

//public class NewAdvert extends AppCompatActivity {
//    DatabaseHelper db;
//    private EditText name, phone, desc, Date, loc;
//    private RadioButton radiolost, radiofound;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_new_advert);
//
//        // Initialize Places API
//        if (!Places.isInitialized()) {
//            Places.initialize(getApplicationContext(), "AIzaSyBCCCTDMEe07srSlh4u_20la9dn8JAidvY");
//        }
//
//        // Create a new Places client instance
//        PlacesClient placesClient = Places.createClient(this);
//
//        name = findViewById(R.id.editTextname);
//        phone = findViewById(R.id.editTextphone);
//        desc = findViewById(R.id.editTextDescription);
//        Date = findViewById(R.id.editTextDate);
//        loc = findViewById(R.id.editTextLocation);
//        radiolost = findViewById(R.id.radioButtonLost);
//        radiofound = findViewById(R.id.radioButtonFound);
//
//        Button save = findViewById(R.id.buttonsave);
//        Button location = findViewById(R.id.buttonloc);
//
//        // Set up Places Autocomplete for location
//        location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Request location permission
//                if (ContextCompat.checkSelfPermission(NewAdvert.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(NewAdvert.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//                } else {
//                    // Permission already granted, get current location
//                    getCurrentLocation();
//                }
//            }
//        });
//
//
//// Set up Places Autocomplete for location
//        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
//                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
//
//        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(@NonNull Place place) {
//                loc.setText(place.getName());
//            }
//
//            @Override
//            public void onError(@NonNull Status status) {
//                Log.e("Places", "An error occurred: " + status);
//            }
//        });
////        autocompleteFragment.getView().setVisibility(View.GONE);
//
//        loc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                autocompleteFragment.getView().setVisibility(View.VISIBLE);
//            }
//        });
//
//
//
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String Name = name.getText().toString();
//                String Phone = phone.getText().toString();
//                String descr = desc.getText().toString();
//                String date = Date.getText().toString();
//                String Loc = loc.getText().toString();
//
//                if (radiolost.isChecked()) {
//                    Boolean checkinsertdata = db.savedatalost(Name, Phone, descr, date, Loc);
//                    if (checkinsertdata) {
//                        Toast.makeText(NewAdvert.this, "New Lost Entry Inserted", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(NewAdvert.this, "New Lost Entry Not Inserted", Toast.LENGTH_SHORT).show();
//                    }
//                } else if (radiofound.isChecked()) {
//                    Boolean checkinsertdata = db.savedatafound(Name, Phone, descr, date, Loc);
//                    if (checkinsertdata) {
//                        Toast.makeText(NewAdvert.this, "New Found Entry Inserted", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(NewAdvert.this, "New Found Entry Not Inserted", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            getCurrentLocation();
//        }
//    }
//
//
//
//    private void getCurrentLocation() {
//        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(NewAdvert.this);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        fusedLocationClient.getLastLocation().addOnSuccessListener(NewAdvert.this, new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if (location != null) {
//                    Geocoder geocoder = new Geocoder(NewAdvert.this, Locale.getDefault());
//                    try {
//                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//                        if (!addresses.isEmpty()) {
//                            Address address = addresses.get(0);
//                            String locationn;
//                            locationn = address.getAddressLine(0);
//                            loc.setText(locationn);
//                        } else {
//                            Toast.makeText(NewAdvert.this, "Location not available", Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    Toast.makeText(NewAdvert.this, "Location not available", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//}
public class NewAdvert extends AppCompatActivity {
    DatabaseHelper db;
    private EditText name, phone, desc, Date,loc;
    private RadioButton radiolost, radiofound;
    private PlacesClient placesClient;

    private AutocompleteSupportFragment autocompleteFragment;
    private boolean isAutocompleteVisible = false;
    private ActivityResultLauncher<Intent> startAutocomplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_advert);

        // Initialize Places API
//        if (!Places.isInitialized()) {
//            Places.initialize(getApplicationContext(), "AIzaSyCGmdptSL1RPN8CeIPYmc1YWMBYUt2a_uQ");
//        }
        Places.initialize(getApplicationContext(), "AIzaSyCGmdptSL1RPN8CeIPYmc1YWMBYUt2a_uQ");

        // Create a new Places client instance
        placesClient = Places.createClient(this);

        db = new DatabaseHelper(this);

        name = findViewById(R.id.editTextname);
        phone = findViewById(R.id.editTextphone);
        desc = findViewById(R.id.editTextDescription);
        Date = findViewById(R.id.editTextDate);
        loc = findViewById(R.id.editTextLocation);
        radiolost = findViewById(R.id.radioButtonLost);
        radiofound = findViewById(R.id.radioButtonFound);

        Button save = findViewById(R.id.buttonsave);
        Button location = findViewById(R.id.buttonloc);

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // Handle the selected place
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                loc.setText(place.getName());
            }

            @Override
            public void onError(@NonNull Status status) {
                // Handle any errors
                Log.i(TAG, "An error occurred: " + status);
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = name.getText().toString();
                String Phone = phone.getText().toString();
                String descr = desc.getText().toString();
                String date = Date.getText().toString();
                String Loc = loc.getText().toString();

                if (radiolost.isChecked()) {
                    Boolean checkinsertdata = db.savedatalost(Name, Phone, descr, date, Loc);
                    if (checkinsertdata) {
                        Toast.makeText(NewAdvert.this, "New Lost Entry Inserted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(NewAdvert.this, "New Lost Entry Not Inserted", Toast.LENGTH_SHORT).show();
                    }
                } else if (radiofound.isChecked()) {
                    Boolean checkinsertdata = db.savedatafound(Name, Phone, descr, date, Loc);
                    if (checkinsertdata) {
                        Toast.makeText(NewAdvert.this, "New Found Entry Inserted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(NewAdvert.this, "New Found Entry Not Inserted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Request location permission
                if (ContextCompat.checkSelfPermission(NewAdvert.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NewAdvert.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                } else {
                    // Permission already granted, get current location
                    getCurrentLocation();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }
    }

    private void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(NewAdvert.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(NewAdvert.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Geocoder geocoder = new Geocoder(NewAdvert.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (!addresses.isEmpty()) {
                            Address address = addresses.get(0);
                            String locationn = address.getAddressLine(0);
                            loc.setText(locationn);
                        } else {
                            Toast.makeText(NewAdvert.this, "Location not available", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(NewAdvert.this, "Location not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

