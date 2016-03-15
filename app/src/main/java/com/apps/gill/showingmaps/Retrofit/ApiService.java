package com.apps.gill.showingmaps.Retrofit;

import com.apps.gill.showingmaps.models.GeoCoding.GeoCodeMain;
import com.apps.gill.showingmaps.models.PlaceAutocomplete.MyLocation;

import retrofit.Callback;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by gill on 22-02-2016.
 */
public interface ApiService {


    @POST("/place/autocomplete/json")
    public void autoComplete(@Query("input") String location,@Query("key") String key ,Callback<MyLocation> callback);
    @POST("/geocode/json")
    public void geoCode(@Query("address") String placeAddress,@Query("key") String key ,Callback<GeoCodeMain> callback);
    @POST("/geocode/json")
    public void geoCodeReverse(@Query("latlng") String placeAddress,@Query("key") String key ,Callback<GeoCodeMain> callback);
}
