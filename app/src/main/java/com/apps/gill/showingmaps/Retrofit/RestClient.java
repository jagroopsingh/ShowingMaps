package com.apps.gill.showingmaps.Retrofit;

import com.apps.gill.showingmaps.config.Config;

import retrofit.RestAdapter;

/**
 * Created by gill on 22-02-2016.
 */
public class RestClient {
    private static ApiService apiService = null;

    public static ApiService getApiService() {
        if (apiService == null) {

            // For object response which is default
            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.getBaseURL()).setLogLevel(RestAdapter.LogLevel.FULL).build();
            apiService = restAdapter.create(ApiService.class);
        }
        return apiService;
    }
}
