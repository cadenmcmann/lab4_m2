package com.example.lab4_m2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            startListening();
        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    updateLocationInfo(location);
                }
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length> 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }

    private void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public void updateLocationInfo(Location location) {
        Log.i("LocationInfo", location.toString());
        TextView lat = (TextView) findViewById(R.id.Latitude);
        TextView lon = (TextView) findViewById(R.id.Longitude);
        TextView alt = (TextView) findViewById(R.id.Altitude);
        TextView acc = (TextView) findViewById(R.id.Accuracy);
        lat.setText("Latitude: " + location.getLatitude());
        lon.setText("Longitude: " + location.getLongitude());
        alt.setText("Altitude: " + location.getAltitude());
        acc.setText("Accuracy: " + location.getAccuracy());

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            String address = "Could not find address.";
            List<Address> listAddrs = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (listAddrs != null && listAddrs.size() > 0) {
                Log.i("PlaceInfo", listAddrs.get(0).toString());
                address = "Address: \n";
                if (listAddrs.get(0).getSubThoroughfare() != null) {
                    address += listAddrs.get(0).getSubThoroughfare() + " ";
                }
                if (listAddrs.get(0).getThoroughfare() != null) {
                    address += listAddrs.get(0).getThoroughfare() + " ";
                }
                if (listAddrs.get(0).getLocality() != null) {
                    address += listAddrs.get(0).getLocality() + " ";
                }
                if (listAddrs.get(0).getPostalCode() != null) {
                    address += listAddrs.get(0).getPostalCode() + " ";
                }
                if (listAddrs.get(0).getCountryName() != null) {
                    address += listAddrs.get(0).getCountryName() + " ";
                }
            }

            TextView addrTextView = (TextView) findViewById(R.id.Address);
            addrTextView.setText(address);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}