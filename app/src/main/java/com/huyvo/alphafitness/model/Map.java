package com.huyvo.alphafitness.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.huyvo.alphafitness.Utils;

import java.util.ArrayList;
import java.util.List;

public class Map {

    private List<LatLng> mRoute;
    public Map(){
        mRoute = new ArrayList<>();
    }

    public void clear(){
        mRoute.clear();
    }

    public boolean add(Location location){
        return add(Utils.getLatLng(location));
    }

    public boolean add(LatLng latLng){
        if(mRoute.size() == 0){
            mRoute.add(latLng);
            return false;
        }

        LatLng fromLatLng = mRoute.get(size()-1);
        // Utils.calculateDistance(latLng, fromlatLng);
        mRoute.add(latLng);
        return true;
    }

    public int size(){
        return mRoute.size();
    }

    public List<LatLng> getRoute(){
        return mRoute;
    }

    public boolean remove(LatLng latLng){
        return mRoute.remove(latLng);
    }

    public LatLng getLast(){
        return mRoute.get(size()-1);
    }

}
