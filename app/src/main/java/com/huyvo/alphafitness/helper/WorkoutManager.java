package com.huyvo.alphafitness.helper;

import com.google.android.gms.maps.model.LatLng;
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

    public void reset(){
        mDistance = 0;
        mSteps = 0;
        mList.clear();
        mTimerStarted = false;
    }

    public void setUserProfile(UserProfile userProfile){
        mCurrentUser = userProfile;
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
    }
    public synchronized void dcrStep(){
        mSteps--;
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
}
