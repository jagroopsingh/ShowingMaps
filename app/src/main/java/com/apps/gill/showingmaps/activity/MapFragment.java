package com.apps.gill.showingmaps.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.apps.gill.showingmaps.R;
import com.apps.gill.showingmaps.Retrofit.RestClient;
import com.apps.gill.showingmaps.models.GeoCoding.GeoCodeMain;
import com.apps.gill.showingmaps.utils.AppConstant;
import com.apps.gill.showingmaps.utils.CommonData;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by gill on 23-02-2016.
 */
public class MapFragment extends FragmentActivity implements OnMapReadyCallback {
    Double latitude, longitude;
    ArrayList<LatLng> markerLatLng = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        latitude = getIntent().getDoubleExtra("Latitude", 0.00);
        longitude = getIntent().getDoubleExtra("Longitude", 0.00);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        final Marker markerMoga = googleMap.addMarker(new MarkerOptions().position(new LatLng(30.8191, 75.184)).title("MyHome"));
        final Marker markerLdh = googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("MyLdhHome"));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(markerMoga.getPosition());
        builder.include(markerLdh.getPosition());
        LatLngBounds bounds = builder.build();
        final CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 150);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //googleMap.moveCamera(cameraUpdate);
                googleMap.animateCamera(cameraUpdate);
            }
        }, 3000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //googleMap.moveCamera(cameraUpdate);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerMoga.getPosition(), 14));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //googleMap.moveCamera(cameraUpdate);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerLdh.getPosition(), 14));
                    }
                }, 2000);
            }
        }, 5000);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                String LatLng = latLng.latitude + "," + latLng.longitude;
                final MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng);
                PolylineOptions polylineOptions = new PolylineOptions();
                markerLatLng.add(latLng);
                polylineOptions.addAll(markerLatLng);
                googleMap.addPolyline(polylineOptions);

                RestClient.getApiService().geoCodeReverse(LatLng, AppConstant.serverKey, new Callback<GeoCodeMain>() {
                    @Override
                    public void success(GeoCodeMain geoCodeMain, Response response) {
                        CommonData.setGeoCodeMain(geoCodeMain);
                        markerOptions.title(geoCodeMain.results.get(0).formattedAddress);
                        googleMap.addMarker(markerOptions);
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        });
    }
}
