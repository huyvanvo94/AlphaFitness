package com.huyvo.alphafitness.helper;

import android.util.Log;

import com.huyvo.alphafitness.model.Map;
import com.huyvo.alphafitness.model.StopWatch;
import com.huyvo.alphafitness.model.UserProfile;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class WorkoutManager {
    static final String TAG = WorkoutManager.class.getName();

    private static WorkoutManager mWorkoutManager;
    
    private final StopWatch mStopWatch;
    private UserProfile mCurrentUser = null;
    private boolean mTimerStarted;
    private Counter mCounter;
    private DurationTracker mDurationTracker;
    // Model class for route.
    private Map mMap;

    private double mLastCalories;
    private double mLastDistance;
    private WorkoutManager(){
        mDurationTracker = new DurationTracker();
        mStopWatch = new StopWatch();
        mTimerStarted = false;
        mLastCalories = 0;
        mLastDistance = 0;
        mMap = new Map();

    }

    public void startStopWatch(){
        mTimerStarted = true;
        Thread mTimeThread = new Thread(new TimeWorker());
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
        mStopWatch.reset();
        mMap.clear();
        mTimerStarted = false;
        mDurationTracker.reset();
    }

    public void setUserProfile(UserProfile userProfile){
        Log.i(TAG, "setUserProfile()");
        mCurrentUser = userProfile;
        mCurrentUser.getToday().incWorkoutCount();
        mCounter = new Counter(mCurrentUser);
        reset();
        updatePeriodically();
    }

    public UserProfile getCurrentUser(){
        return mCurrentUser;
    }

    public double getAverage(){
        return mDurationTracker.getAverageDistance();
    }

    public double getMin(){
        return mDurationTracker.getMinDistance();
    }

    public double getMax(){
        return mDurationTracker.getMaxDistance();
    }


    public synchronized void incStep(){
        if(mCounter==null){
            Log.i(TAG, "mCounter is null");
            return;
        }
        Log.i(TAG, "incStep()");


        mCounter.onStep();
        // update user profile calories and distance
        double calories = mCurrentUser.getToday().getCaloriesBurned() + mCounter.getCalories()-mLastCalories;
        double distance = mCurrentUser.getToday().getDistance() + mCounter.getDistance() - mLastDistance;
        mCurrentUser.setTodayCalories(calories);
        mCurrentUser.setTodayDistance(distance);

        mLastDistance = mCounter.getDistance();
        mLastCalories = mCounter.getCalories();
        // update data for graph
        mDurationTracker.incStep();
        mDurationTracker.addDistance(mCounter.getDistance());
        mDurationTracker.addCalories(mCounter.getCalories());
    }

    public synchronized double getDistance(){
        return mCounter.getDistance();
    }

    public synchronized int getStepCount(){
        return mCounter.getSteps();
    }

    public Map getMap(){
        return mMap;
    }

    // Deals with service
    public void turnOffService(){
        if(mEndServiceListener == null) return;
        mEndServiceListener.turnOffService();
    }

    void setService(EndServiceListener service){
        mEndServiceListener = service;
    }

    void removeService(){
        mEndServiceListener = null;
    }

    private EndServiceListener mEndServiceListener;

    interface EndServiceListener {
        void turnOffService();
    }

    private class TimeWorker implements Runnable{
        @Override
        public void run() {
            mStopWatch.start();
        }
    }

    public List<Integer> getStepCountTrack(){
        return mDurationTracker.getStepsTrack();
    }

    public List<Double> getCaloriesCountTrack(){
        return mDurationTracker.getCaloriesTrack();
    }

    private void updatePeriodically(){

        Timer mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if(mTimerStarted){
                        mCurrentUser.setTodayTime(mStopWatch.getElapsedTime());
                    }
                }
        }, 0, 1000);


        // periodically update data
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(mTimerStarted)
                    mDurationTracker.stampSoFar();

            }
        }, 0, StopWatch.ONE_MINUTE);

    }
}
