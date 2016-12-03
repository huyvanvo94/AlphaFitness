package com.huyvo.alphafitness.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huyvo on 11/27/16.
 */

public class DurationTracker {

    public static final String TAG = DurationTracker.class.getName();
    private List<Integer> mStepCountTrack;
    private List<Double> mCaloriesCountTrack;
    private List<Double> mDistanceTrack;
    
    private int mSteps;
    private double mDistance;
    private double mCalories;
    
    public DurationTracker(){
        reset();

    }

    public void stampSoFar(){
        stampCaloriesSoFar();
        stampDistancesSoFar();
        stampStepsSoFar();

        //clear();
    }

    private void stampDistancesSoFar(){
       mDistanceTrack.add(mDistance);
    }

    private void stampCaloriesSoFar(){
        mCaloriesCountTrack.add(mCalories);
    }

    private void stampStepsSoFar(){
        mStepCountTrack.add(mSteps);
    }

    public void addDistance(double distance) {
        mDistance += distance;
    }

    public void addCalories(double calories) {
        mCalories += calories;
    }

    public void addStep(int steps) {
        mSteps += steps;
    }

    private void clear() {
        mDistance = 0;
        mCalories = 0;
        mSteps = 0;

    }

    public void reset(){
        mStepCountTrack = new ArrayList<>();
        mCaloriesCountTrack = new ArrayList<>();
        mDistanceTrack = new ArrayList<>();

        mCalories = 0;
        mDistance = 0;
        mCalories = 0;
    }

    public List<Double> getDistancesTrack() {
        return mDistanceTrack;
    }

    public List<Integer> getStepsTrack() {
        return mStepCountTrack;
    }

    public List<Double> getCaloriesTrack() {
        return mCaloriesCountTrack;
    }

    public double getAverageDistance() {
        if(mDistanceTrack.size()==0){
            return 0;
        }

        double sum = 0;
        for(double distance:mDistanceTrack){
            sum += distance;
        }
        return sum/mDistanceTrack.size();
    }

    public double getMinDistance() {

        if(mDistanceTrack.size() == 0){
            return 0;
        }

        double min = mDistanceTrack.get(0);

        if(mDistanceTrack.size()==1){
            return min;
        }

        for(int i = 1; i < mDistanceTrack.size(); i++){

            if(min == 0){
                min = mDistanceTrack.get(i);
                continue;
            }

            if(min > mDistanceTrack.get(i) && mDistanceTrack.get(i) != 0){
                min = mDistanceTrack.get(i);
            }
        }
        return min;
    }

    public double getMaxDistance() {

        //Log.i(TAG, String.valueOf(mDistanceTrack.size()));

        if(mDistanceTrack.size() == 0){
            return 0;
        }
        
        double max = mDistanceTrack.get(0);

        if(mDistanceTrack.size() == 1){
            return max;
        }
        for(int i = 1; i < mDistanceTrack.size(); i++){
            if(max < mDistanceTrack.get(i)){
                max = mDistanceTrack.get(i);
            }
        }
        return max;
    }

    public void incStep(){
        mSteps ++;
    }

    public double getDistance() {
        return mDistance;
    }

    public int getSteps() {
        return mSteps;
    }

    public double getCalories() {
        return mCalories;
    }

}
