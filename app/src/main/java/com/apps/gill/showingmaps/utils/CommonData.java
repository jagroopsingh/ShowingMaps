package com.apps.gill.showingmaps.utils;

import com.apps.gill.showingmaps.models.GeoCoding.GeoCodeMain;
import com.apps.gill.showingmaps.models.PlaceAutocomplete.MyLocation;

/**
 * Created by gill on 22-02-2016.
 */
public class CommonData {

    private static MyLocation myLocation=null;

    private static GeoCodeMain geoCodeMain=null;

    public static MyLocation getMyLocation() {
        return myLocation;
    }

    public static void setMyLocation(MyLocation myLocation) {
        CommonData.myLocation = myLocation;
    }


    public static GeoCodeMain getGeoCodeMain() {
        return geoCodeMain;
    }

    public static void setGeoCodeMain(GeoCodeMain geoCodeMain) {
        CommonData.geoCodeMain = geoCodeMain;
    }

}
