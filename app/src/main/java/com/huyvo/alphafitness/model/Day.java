package com.huyvo.alphafitness.model;

import com.google.gson.Gson;

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
    private int mDidWorkout;
    // Total calories burned
    private float mCaloriesBurned;
    private String mDay;
    private int mStepCount;

    public Day(String day){
        mDay = day;
        mDistance = 0;
        mTime = 0;
        mDidWorkout = 0;
        mCaloriesBurned = 0;
        mStepCount = 0;

    }
    public void setStepCount(int s){
        mStepCount = s;
    }

    public int getStepCount(){
        return mStepCount;
    }

    public double getDistance(){ return mDistance; }
    public long getTime(){ return mTime; }
    public int getDidWorkout(){ return mDidWorkout; }
    public float getCaloriesBurned(){ return mCaloriesBurned; }

    public void setDistance(double d){
        mDistance = d;
    }

    public void setTime(long t){
        mTime = t;
    }

    public boolean setDidWorkout(int w){
        if (w > 1) return false;
        if(w <0) return false;

        mDidWorkout = w;
        return true;
    }

    public void setCaloriesBurned(float c){
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

    public static Day newInstance(String day, double distance, long time, float cal, int w, int s) {
        Day d = new Day(day);
        d.setDistance(distance);
        d.setTime(time);
        d.setCaloriesBurned(cal);
        d.setDidWorkout(w);
        d.setStepCount(s);
        return d;
    }

    public String toJson(){
        return new Gson().toJson(this);
    }

    public static Day newInstance(String json){
        return new Gson().fromJson(json, Day.class);
    }

    @Override
    public String toString() {
        return "Day{" +
                "mDistance=" + mDistance +
                ", mTime=" + mTime +
                ", mDidWorkout=" + mDidWorkout +
                ", mCaloriesBurned=" + mCaloriesBurned +
                ", mDay='" + mDay + '\'' +
                ", mStepCount=" + mStepCount +
                '}';
    }
}
