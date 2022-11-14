package com.gsrtc.mobileweb.utils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.List;

import com.gsrtc.mobileweb.GSRTC;
import com.gsrtc.mobileweb.models.Location;

/**
 * Created by Ajith on 4/6/2017.
 */

public class RestClientUtil {
    public static void fetchALlLocations(final FetchAllLocationsCallback callback){
        String url = URLUtils.FETCH_ALL_LOCATIONS_URL;
        final StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {
                        try {
                            List<Location> siteList = null;
                            try {
                                siteList = JacksonUtil.getMapper().readValue(responseString, new TypeReference<List<Location>>() {});
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if(callback != null)
                                callback.onSuccess(siteList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

//                        if(callback != null)
//                            callback.onSuccess(MockUtil.getSiteList());
                        callback.onError(error);
                    }
                }
        )
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {   //Add Authentication header
//                String tokenString = LoginUtil.getLoggedInUser().getAuthToken();
//                Map<String, String> requestHeader = new HashMap<>();
//                requestHeader.put("Authorization", "Token " + tokenString);
//                requestHeader.put("Content-Type", "application/json");
//                return requestHeader;
//            }
//
//        }
        ;
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(GSRTC.getAppContext()).addToRequestQueue(jsonObjectRequest);
    }

    public interface FetchAllLocationsCallback{
        void onSuccess(List<Location> locationList);
        void onError(VolleyError error);
    }
}
