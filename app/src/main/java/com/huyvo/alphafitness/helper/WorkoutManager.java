package com.huyvo.alphafitness.helper;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.huyvo.alphafitness.bagi.CaloriesCounter;
import com.huyvo.alphafitness.bagi.DistanceCounter;
import com.huyvo.alphafitness.model.StopWatch;
import com.huyvo.alphafitness.model.UserProfile;
import com.huyvo.alphafitness.model.Utils;

import java.util.ArrayList;
import java.util.List;

public class WorkoutManager {
    private static final String TAG = WorkoutManager.class.getName();

    private static WorkoutManager mWorkoutManager;
    private final StopWatch mStopWatch;
    private double mDistance;
    private int mSteps;
    private List<LatLng> mList;
    private UserProfile mCurrentUser;
    private Thread mTimeThread;
    private boolean mTimerStarted;
    //private List<OnWorkoutManageListener> mListeners;
    private CaloriesCounter mCaloriesCounter;
    private DistanceCounter mDistanceCounter;
    private WorkoutManager(){
        mDistance = 0;
        mSteps = 0;

        mStopWatch = new StopWatch();
        mTimerStarted = false;
        mList = new ArrayList<>();


  //      mListeners = new ArrayList<>();
    }

    public void startStopWatch(){
        mTimeThread = new Thread(new TimeWorker());
        mTimeThread.start();
    }

    public boolean timerStarted(){
        return mTimerStarted;
    }
    public void pauseStopWatch(){
        mStopWatch.pause();
        mTimerStarted = false;
    }

    public String getFormattedTime(){
        return mStopWatch.getFormattedTime();
    }

    public static WorkoutManager sharedInstance(){
        if(mWorkoutManager==null){
            mWorkoutManager = new WorkoutManager();
        }
        return mWorkoutManager;
    }

    public static WorkoutManager sharedInstance(UserProfile userProfile){
        WorkoutManager workoutManager = sharedInstance();
        workoutManager.setUserProfile(userProfile);
        return workoutManager;
    }

    public void reset(){
        mDistance = 0;
        mSteps = 0;
        mList.clear();
        mTimerStarted = false;
    }

    public void setUserProfile(UserProfile userProfile){
        mCurrentUser = userProfile;
        mCaloriesCounter = new CaloriesCounter(mCurrentUser);
        mDistanceCounter = new DistanceCounter();
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

    public synchronized void incStep(){
        mSteps++;
        if(mCaloriesCounter != null){
            mCaloriesCounter.onStep();
            mDistanceCounter.onStep();
            mCurrentUser.setTodayCalories((float) mCaloriesCounter.getCalories());
            mCurrentUser.setTodayDistance(mDistanceCounter.getDistance());

            Log.i(TAG, String.valueOf(mDistanceCounter.getDistance()));
            Log.i(TAG, String.valueOf(mCaloriesCounter.getCalories()));
        }
    }

    public synchronized int getStepCount(){
        return mSteps;
    }

    public boolean computeCurrentDistanceTo(LatLng to){
        if(mList.size() == 0) {
            mList.add(to);
            return false;
        }
        LatLng from = mList.get(mList.size()-1);
        mDistance += Utils.calculateDistance(from, to);
        mList.add(to);

        return true;
    }
    public void updateUserDistance(){
        mCurrentUser.setTodayDistance(mDistance);
    }
    public void updateUserStep(){
        mCurrentUser.setTodaySteps(mSteps);
    }
    public List<LatLng> getRoute(){
        return mList;
    }
    public void addToRoute(LatLng latLng){
        mList.add(latLng);
    }

    public void clearRoute(){
        mList.clear();
    }

    public void remote(LatLng ll){
        mList.remove(ll);
    }

    public String distance(){
        return String.valueOf(Math.round(mDistance*100.0)/100.0) + " miles";
    }


    private class TimeWorker implements Runnable{
        @Override
        public void run() {
            mStopWatch.start();
        }
    }

    public void turnOffService(){
        if(mEndServiceListener == null) return;
        mEndServiceListener.turnOffService();
    }

    public void setService(EndServiceListener service){
        mEndServiceListener = service;
    }

    public void removeService(){
        mEndServiceListener = null;
    }


    private EndServiceListener mEndServiceListener;

    public interface EndServiceListener {
        public void turnOffService();
    }
}
