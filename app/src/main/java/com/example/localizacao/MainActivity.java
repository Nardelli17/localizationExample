package com.example.localizacao;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private LatLng currentPosition;

    private GoogleMap map;

    private LocationManager locationManager;

    private LatLng lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, 0);

            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 0);

        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();

        String provider = locationManager.getBestProvider(criteria, true);

        map = googleMap;

//        map.setOnMapClickListener();

        map.getUiSettings().setZoomControlsEnabled(true);

        map.setMyLocationEnabled(true);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        if (location != null) {
            double latti = location.getLatitude();
            double longi = location.getLongitude();
            lastLocation = new LatLng(latti,longi);

        } else if (location1 != null) {
            double latti = location1.getLatitude();
            double longi = location1.getLongitude();
            lastLocation = new LatLng(latti,longi);
        } else if (location2 != null) {
            double latti = location2.getLatitude();
            double longi = location2.getLongitude();
            lastLocation = new LatLng(latti,longi);
        }


        map.addMarker(new MarkerOptions().position(lastLocation).title("Sua Posicação!"));

        map.moveCamera(CameraUpdateFactory.newLatLng(lastLocation));

    }

}
