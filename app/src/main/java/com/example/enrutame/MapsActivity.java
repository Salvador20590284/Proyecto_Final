package com.example.enrutame;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.enrutame.databinding.ActivityMapsBinding;

import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    Boolean actualPosition = true;
    JSONObject jso;
    Double LongitudOrigen, LatitudOrigen;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(20.375269, -99.983749);
        LatLng sydney1 = new LatLng(20.385477, -99.968995);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Tecnologico"));
        mMap.addMarker(new MarkerOptions().position(sydney1).title("Soriana"));
        LatLng sydney2 = new LatLng(20.38734396168668, -99.9929022810612);
        mMap.addMarker(new MarkerOptions().position(sydney2).title("Casa Gabriel"));
        LatLng sydney3 = new LatLng(20.385091228649575, -100.01264333818503);
        mMap.addMarker(new MarkerOptions().position(sydney3).title("Casa Del Naranjo"));
        LatLng sydney4 = new LatLng(20.389836273424493, -99.98431922265782);
        mMap.addMarker(new MarkerOptions().position(sydney4).title("Gasolinera Servicio Victoria"));
        LatLng sydney5 = new LatLng(20.38484811522978, -99.98509169880614);
        mMap.addMarker(new MarkerOptions().position(sydney5).title("Pemex"));
        LatLng sydney6 = new LatLng(20.409061561131846, -99.98243095754002);
        mMap.addMarker(new MarkerOptions().position(sydney6).title("Excellence Express & Suites"));
        LatLng sydney7 = new LatLng(20.387823191552542, -99.996421358893);
        mMap.addMarker(new MarkerOptions().position(sydney7).title("Hotel Layseca"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney6));
        //para poner un zoom
        mMap.setMinZoomPreference(1.0f);
        mMap.setMaxZoomPreference(50.0f);
        mMap.setMinZoomPreference(1.0f);
        mMap.setMaxZoomPreference(50.0f);
        //para mover la camara
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
        googleMap.setOnMyLocationButtonClickListener(this);
        //habilitar el zoom
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //Brujula
        mMap.getUiSettings().setCompassEnabled(true);

        //Mover el marcador
        final LatLng perthLocation = new LatLng(20.397300, -99.983358);
        mMap.addMarker(new MarkerOptions().position(perthLocation).title("Marcador Movil"));
        Marker perth = mMap.addMarker(
                new MarkerOptions()
                        .position(perthLocation)
                        .draggable(true));

        //habilitar mis ubicacion
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //Pedimos permisos
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)){

            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){

            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(@NonNull Location location) {

                if(actualPosition){
                    LatitudOrigen = location.getLatitude();
                    LongitudOrigen = location.getLongitude();
                    actualPosition = false;

                    LatLng miposicion = new LatLng(LatitudOrigen,LongitudOrigen);
                    mMap.addMarker(new MarkerOptions().position(miposicion).title("Tu Estas Aqui"));

                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return;
            }
            // Other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
}