package com.example.root.userlocation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    LocationManager locationManager;
    LocationListener listener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            startListening();
        }

    }

    public void startListening(){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,0,0,listener);

        }

    }
    public void updateLocationInfo(Location location){



        TextView lattext = (TextView)findViewById(R.id.lattext);

        TextView longtext = (TextView)findViewById(R.id.longtext);
        TextView  altitude = (TextView)findViewById(R.id.altitude);
        TextView accuracy = (TextView)findViewById(R.id.accuracy);

        lattext.setText("Latitude: "+ location.getLatitude());
        longtext.setText("Longitude: "+ location.getLongitude());
        accuracy.setText("Accuracy: "+ location.getAccuracy());
        altitude.setText("Altitude: "+ location.getAltitude());


        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

            String address = "Could Not find!";
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            if (addresses != null && addresses.size() > 0) {

                Log.i("Location:",addresses.get(0).toString());

                address = "Address: \n";

                if (addresses.get(0).getSubThoroughfare() != null){

                    address += addresses.get(0).getSubThoroughfare()+ "\n";
                }

                if (addresses.get(0).getThoroughfare() != null){
                    address += addresses.get(0).getThoroughfare()+ "\n";
                }

                if (addresses.get(0).getPostalCode() != null){

                    address += addresses.get(0).getPostalCode()+ "\n";
                }
                if (addresses.get(0).getCountryName() != null){

                    address += addresses.get(0).getCountryName();
                }

            }

           TextView addresstext = (TextView)findViewById(R.id.addresstext);

           addresstext.setText(address);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {


                updateLocationInfo(location);

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


        if (Build.VERSION.SDK_INT <23){


            startListening();

        }else {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else {

            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,0,0,listener);

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null){
                updateLocationInfo(location);
            }


        }

        }

    }
}
