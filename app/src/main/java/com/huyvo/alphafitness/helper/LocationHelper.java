package com.huyvo.alphafitness.helper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class LocationHelper implements LocationListener {

    private List<OnLocationListener> mListeners;
    private LocationManager mLocationManager;
    private Context mContext;
    private Location mLocation;

    public LocationHelper(Context context) {
        mContext = context;
        mListeners = new ArrayList<>();
        if (checkPermission()) {
            Toast.makeText(mContext, "Cannot connect map", Toast.LENGTH_SHORT).show();
            return;
        }
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates( mLocationManager.getBestProvider(getCriteria(), true), 5000, 2.0f, this);
        mLocation = mLocationManager.getLastKnownLocation(getProvider());


    }

    private String getProvider(){
        return mLocationManager.getBestProvider(getCriteria(), true);
    }

    private Criteria getCriteria(){
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    private boolean checkPermission(){
        return ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onLocationChanged(Location location) {
        for (OnLocationListener listener:mListeners)
            listener.onLocationChanged(location);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void addListener(OnLocationListener locationListener) {
        if(mListeners == null)
            mListeners = new ArrayList<>();
        mListeners.add(locationListener);
    }

    public void removeListener(OnLocationListener locationListener) {
        if(mListeners == null) return;
        mListeners.remove(locationListener);
    }

    public interface OnLocationListener {
        void onLocationChanged(Location location);
    }

    public Location getLastKnowLocation() {
        if(checkPermission())
            return mLocation;
        return mLocationManager.getLastKnownLocation(getProvider());
    }
}
