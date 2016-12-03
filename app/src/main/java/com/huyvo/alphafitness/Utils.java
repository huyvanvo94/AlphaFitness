package com.huyvo.alphafitness;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class Utils {
    // How much the UI will zoom in for the map
    public final static int LEVEL_ZOOM = 17;
    // Convert
    public final static double METERS_TO_MILES = 0.000621371192;

    public static double calculateDistance(LatLng l1, LatLng l2) {
        Location locationA = new Location("point A");
        locationA.setLatitude(l1.latitude);
        locationA.setLongitude(l1.longitude);
        Location locationB = new Location("point B");
        locationB.setLatitude(l2.latitude);
        locationB.setLongitude(l2.longitude);
        return locationA.distanceTo(locationB) * METERS_TO_MILES;
    }


    public static double calculateDistance(List<LatLng> theRoute) {
        double sum = 0;

        for (int j = 0; j < theRoute.size() - 1; j++) {
            sum += Utils.calculateDistance(theRoute.get(j), theRoute.get(j + 1));
        }

        return sum;
    }

    public static LatLng getLocation(String theAddress, Context c) {
        Geocoder coder = new Geocoder(c);
        try {
            List<Address> abbesses = coder.getFromLocationName(theAddress, 50);
            for (Address add : abbesses) {
                double longitude = add.getLongitude();
                double latitude = add.getLatitude();
                return new LatLng(latitude, longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public LatLng getLocationFromAddress(Context context, String strAddress){

        Geocoder coder = new Geocoder(context);
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);

            return new LatLng(location.getLatitude(), location.getLongitude());
        }catch (Exception ex){
            return null;
        }

    }
    public void onMapSearch(String theAddress, GoogleMap mMap, Context c) {

        String location = theAddress;
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(c);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }
    public static LatLng getLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }
}
