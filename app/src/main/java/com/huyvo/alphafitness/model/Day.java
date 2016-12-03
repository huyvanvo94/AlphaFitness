package com.huyvo.alphafitness.model;

public class Day{

    public static final String SUN   = "sunday";
    public static final String MON   = "monday";
    public static final String TUES  = "tuesday";
    public static final String WED   = "wednesday";
    public static final String THURS = "thursday";
    public static final String FRI   = "friday";
    public static final String SAT   = "saturday";

    // The distance
    private double mDistance;
    private long mTime;
    // Total time
    private int mWorkoutCount;
    // Total calories burned
    private double mCaloriesBurned;
    private String mDay;
    private int mStepCount;

    public Day(String day, double distance, long time, int didWorkout, float caloriesBurned,
                int stepCount){

        mDay = day;
        mDistance = distance;
        mTime = time;
        mWorkoutCount = didWorkout;
        mCaloriesBurned = caloriesBurned;
        mStepCount = stepCount;
    }

    public Day(String day){
        mDay = day;
        mDistance = 0;
        mTime = 0;
        mWorkoutCount = 0;
        mCaloriesBurned = 0;
        mStepCount = 0;

    }

    public void setStepCount(int s){
        mStepCount = s;
    }

    public int getStepCount(){
        return mStepCount;
    }


    public long getTime(){ return mTime; }
    public int getWorkoutCount(){ return mWorkoutCount; }
    public double getCaloriesBurned(){ return mCaloriesBurned; }

    public void setDistance(double d){
        mDistance = d;
    }
    public double getDistance(){ return mDistance; }
    public void setTime(long t){
        mTime = t;
    }

    public boolean setWorkoutCount(int w){

        mWorkoutCount = w;
        return true;
    }

    public void incWorkoutCount(){
        mWorkoutCount++;
    }

    public void setCaloriesBurned(double c){
        mCaloriesBurned = c;
    }


    public void setDay(String d){
        mDay = d;
    }

    public String getDay(){
        return mDay;
    }

    public static int getNumDay(String theDay){
        if(theDay.equals(Day.SUN))
            return 1;
        if(theDay.equals( Day.MON))
            return 2;
        if(theDay.equals(Day.TUES))
            return 3;
        if(theDay.equals(Day.WED))
            return 4;
        if(theDay.equals(Day.THURS))
            return 5;
        if(theDay.equals( Day.FRI))
            return 6;
        return 7;
    }

}
