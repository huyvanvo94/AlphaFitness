package com.huyvo.alphafitness.helper;

import android.util.Log;

import com.huyvo.alphafitness.model.UserProfile;

/**
 * Created by huyvo on 11/28/16.
 * Keep track of Calories and Distance.
 */

public class Counter {

    public static String TAG = Counter.class.getName();

    private static double METRIC_RUNNING_FACTOR = 1.02784823;
    private static double IMPERIAL_RUNNING_FACTOR = 0.75031498;

    private static double METRIC_WALKING_FACTOR = 0.708;
    private static double IMPERIAL_WALKING_FACTOR = 0.517;


    public static double STEP_LENGTH = 30;

    private int mSteps;
    private double mDistance;
    private double mCalories;
    private UserProfile mUserProfile;

    private double mBodyWeight;
    private boolean mIsRunning;

    public Counter(UserProfile profile){
        mUserProfile = profile;
        mBodyWeight = mUserProfile.getWeight();
        mIsRunning = true;
        mSteps = 0;
        mDistance = 0;
        mCalories = 0;
    }

    public double getDistance(){
        return mDistance;
    }

    public double getCalories(){
        return mCalories;
    }

    public void onStep(){
        mSteps++;
        computeCalories();
        computeDistance();

        talk();

    }

    private void talk(){
        Log.i(TAG, "mDistance="+String.valueOf(mDistance));
        Log.i(TAG, "mCalories="+String.valueOf(mCalories));
        Log.i(TAG, "mSteps="+String.valueOf(mSteps));
    }

    public int getSteps(){
        return mSteps;
    }

    public void computeDistance(){
        mDistance += 0.000473485;
    }

    public void computeCalories(){
        mCalories +=
                (mBodyWeight * (mIsRunning ? IMPERIAL_RUNNING_FACTOR : IMPERIAL_WALKING_FACTOR))
                        // Distance:
                        * STEP_LENGTH // inches
                        / 63360.0; // inches/mile
    }
}
