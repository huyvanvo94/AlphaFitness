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
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class LocationHelper implements LocationListener {

    private List<OnLocationListener> mListeners;
    private LocationManager mLocationManager;
    private String mLocationProvider;
    private Context mContext;

    public LocationHelper(Context context) {
        mContext = context;

        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        mLocationProvider =
                mLocationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
       // Location mLocation = mLocationManager.getLastKnownLocation(mLocationProvider);
        mLocationManager.requestLocationUpdates(mLocationProvider, 5000, 2.0f, this);
        mListeners = new ArrayList<>();


    }


    @Override
    public void onLocationChanged(Location location) {
        Log.i(LocationHelper.class.getName(), "onLocationChanged()");

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
        mListeners.add(locationListener);
    }

    public void removeListener(OnLocationListener locationListener) {
        mListeners.remove(locationListener);
    }

    public interface OnLocationListener {
        void onLocationChanged(Location location);
    }

    public Location getLastKnowLocation() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return null;
        }
        return mLocationManager.getLastKnownLocation(mLocationProvider);
    }
}
