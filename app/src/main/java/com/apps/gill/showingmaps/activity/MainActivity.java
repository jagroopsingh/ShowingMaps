package com.apps.gill.showingmaps.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.apps.gill.showingmaps.R;
import com.apps.gill.showingmaps.Retrofit.RestClient;
import com.apps.gill.showingmaps.models.GeoCoding.GeoCodeMain;
import com.apps.gill.showingmaps.models.PlaceAutocomplete.MyLocation;
import com.apps.gill.showingmaps.utils.AppConstant;
import com.apps.gill.showingmaps.utils.CommonData;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements AppConstant {
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> descriptionList = new ArrayList<>();
    TextWatcher textWatcher;
    TextView tvPlaceLocation;
    Intent intent;
    Button btClickSeeMap, btGetLocation;
    private double latForGPS, lngForGPS;

    void init() {
        initTextWatcher();
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.tv_location);
        arrayAdapter = new ArrayAdapter(MainActivity.this, R.layout.text_layout, descriptionList);
        autoCompleteTextView.setAdapter(arrayAdapter);
        tvPlaceLocation = (TextView) findViewById(R.id.tv_address);
        btClickSeeMap = (Button) findViewById(R.id.bt_map);
        btGetLocation = (Button) findViewById(R.id.bt_gps);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        autoCompleteTextView.addTextChangedListener(textWatcher);
        intent = new Intent(MainActivity.this, Tracking.class);
        startService(intent);
        MyReciever myReceiver = new MyReciever();
        registerReceiver(myReceiver, new IntentFilter(LOCATION_SERVICE));

        btClickSeeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackGeocoding();
            }
        });
        btGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GpsLocation();
            }
        });
    }

    private void GpsLocation() {
        intent = new Intent(MainActivity.this, MapFragment.class);
        intent.putExtra("Latitude", latForGPS);
        intent.putExtra("Longitude", lngForGPS);
        startActivity(intent);
    }

    private void callBackGeocoding() {
        String getAddress = autoCompleteTextView.getText().toString();
        Log.v("TAG",getAddress);
        RestClient.getApiService().geoCode(getAddress, serverKey, new Callback<GeoCodeMain>() {
            @Override
            public void success(GeoCodeMain geoCodeMain, Response response) {
                CommonData.setGeoCodeMain(geoCodeMain);
                Log.v("TAG", String.valueOf(geoCodeMain.results.get(0).geometry.location.lat));
                double latitude = geoCodeMain.results.get(0).geometry.location.lat;
                double longitude = geoCodeMain.results.get(0).geometry.location.lng;
                intent = new Intent(MainActivity.this, MapFragment.class);
                intent.putExtra("Latitude", latitude);
                intent.putExtra("Longitude", longitude);
                startActivity(intent);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.v("TAG",error.getLocalizedMessage());
                Log.v("TAG","Failure");
            }
        });
    }

    private void initTextWatcher() {
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                callBackForAutoComplete();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    private void callBackForAutoComplete() {

        RestClient.getApiService().autoComplete(autoCompleteTextView.getText().toString(), serverKey, new Callback<MyLocation>() {
            @Override
            public void success(MyLocation location, Response response) {
                CommonData.setMyLocation(location);

                descriptionList.clear();
                int len = location.predictions.size();
                for (int loop = 0; loop < len; loop++) {
                    descriptionList.add(loop, location.predictions.get(loop).description);
                }
                arrayAdapter.clear();
                arrayAdapter.addAll(descriptionList);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.v("failed", error.getLocalizedMessage());
            }
        });
    }

    private class MyReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v("TAG", "inRecieve");
            Bundle extras = intent.getExtras();
            latForGPS = extras.getDouble("lat");
            lngForGPS = extras.getDouble("lng");
            Log.v("TAGGPS", latForGPS + lngForGPS + "");
        }
    }
}
