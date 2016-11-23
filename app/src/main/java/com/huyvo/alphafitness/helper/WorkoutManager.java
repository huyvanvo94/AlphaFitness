package com.huyvo.alphafitness.helper;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import com.huyvo.alphafitness.model.Data;
import com.huyvo.alphafitness.model.StopWatch;
import com.huyvo.alphafitness.model.UserProfile;
import com.huyvo.alphafitness.model.Utils;

import java.util.List;

public class WorkoutManager {

    private static final String TAG = WorkoutManager.class.getName();

    private StopWatch mStopWatch;
    private double mDistance;
    private int mSteps;
    private List<LatLng> mLatLngList;
    private UserProfile mCurrentUser;


    public WorkoutManager(UserProfile userProfile){
        mCurrentUser = userProfile;
        mLatLngList = Data.mLatLng;
        mStopWatch = new StopWatch();
    }

    public WorkoutManager(){

    }

    public void start(){
       // mTimerThread.start();
    }

    public void setUserProfile( UserProfile up){
        mCurrentUser = up;
    }

    public UserProfile getCurrentUser(){
        return mCurrentUser;
    }

    public double getAverage(){
        double avg = 0;
        return avg;
    }

    public double getMin(){
        double min = 0;
        return min;
    }

    public double getMax(){
        double max = 0;

        return max;
    }

    public double getDistance(){
        return mDistance;
    }

    public void incStep(){
        mSteps++;
    }
    public void dcrStep(){
        mSteps--;
    }

    public boolean computeCurrentDistanceTo(LatLng to){
        if(mLatLngList.size() == 0) {
            mLatLngList.add(to);
            return false;
        }

        LatLng from = mLatLngList.get(mLatLngList.size()-1);
        mDistance += Utils.calculateDistance(from, to);
        Log.i(TAG, String.valueOf(mDistance));
        mLatLngList.add(to);

        return true;

    }

    public void updateUserDistance(){
        mCurrentUser.setTodayDistance(mDistance);
    }
    public void updateUserStep(){
        mCurrentUser.setTodaySteps(mSteps);
    }

    public List<LatLng> getRoute(){
        return mLatLngList;
    }

    public void addToRoute(LatLng latLng){
        mLatLngList.add(latLng);
    }


    public String distance(){
        return String.valueOf(Math.round(mDistance*100.0)/100.0) + " miles";
    }


    public void updateProfileToDatabase(Context context){

    }


}
