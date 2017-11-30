package com.example.maruta.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private LocationManager locMag;
    private LocationListener locList;

    private List<Address> addresses;
    private Geocoder geoCoder;

    //Ui
    private TextView latView, longView, accView, altView, addView;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){

            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }

                locMag.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2, 2, locList);

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();

        locMag = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locList = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

                geoCoder = new Geocoder(MainActivity.this, Locale.getDefault());

                try {

                    addresses = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                } catch (IOException e) {

                    e.printStackTrace();
                }

                latView.setText("Latitude:" + location.getLatitude());
                longView.setText("Longitude:" + location.getLongitude());

                altView.setText("Altitude:" + location.getAltitude());
                accView.setText("Accuracy:" + location.getAccuracy());

                addView.setText("Address: " + addresses.get(0).getCountryName() + addresses.get(0).getPostalCode());

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {

            locMag.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2, 2, locList);
        }

    }

    private void initUi() {

        latView = findViewById(R.id.latText);
        longView = findViewById(R.id.longText);
        accView = findViewById(R.id.accView);
        altView = findViewById(R.id.altView);
        addView = findViewById(R.id.addView);
    }
}
