package com.huyvo.alphafitness.controller;

import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.huyvo.alphafitness.Utils;

import java.util.List;



public class MapController {

    private final static int ZOOM_17   = 17;
    private final static String MARKER = "You are here";

    private GoogleMap mGoogleMap;

    public MapController(GoogleMap googleMap){
        mGoogleMap = googleMap;
    }

    public void clear(){
        mGoogleMap.clear();
    }

    boolean startView(LatLng latLng){
        if(mGoogleMap == null){
            return false;
        }
        zoomCamera(latLng);
        return true;
    }

    boolean startView(Location location){
        return startView(Utils.getLatLng(location));
    }

    void zoomCamera(Location location){
        zoomCamera(Utils.getLatLng(location));
    }

    boolean drawRoute(List<LatLng> route){
        if(mGoogleMap==null || route.size() < 2){
            return false;
        }
        mGoogleMap.clear();
        PolylineOptions options = new PolylineOptions();
        options.color(Color.BLUE);
        options.width(5);
        options.visible(true);
        for(LatLng point: route){
            options.add(point);
        }
        mGoogleMap.addPolyline(options);
        return true;
    }
    void zoomCamera(LatLng latLng){
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_17));
        mGoogleMap.addMarker(new MarkerOptions().title(MARKER).position(latLng));
    }
}
