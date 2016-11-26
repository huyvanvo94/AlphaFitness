package com.huyvo.alphafitness.model;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UserProfile{

    public final static String DISTANCE_UNIT = "miles";
    public final static String CALORIES_UNIT = "Cal";
    public final static String COUNT_UNIT    = "times";
    public final static String WEIGHT_UNIT   = "lbs";

    private Week mWeek;
    // ArrayList
    private String mId;
    private String mFirstName;
    private String mLastName;
    private float mWeight;
    private String mGender;
    // Average / Weekly

    // All Time
    private double mTotalDistance;
    private int mTotalWorkoutCount;
    private float mTotalCalories;
    private long mTotalTime;
    private int mTotalStepCount;

    //Today
    private int mToday;

    public UserProfile(String firstName, String lastName, float weight){
        this(firstName, lastName, weight, 0, 0, 0);
    }

    public UserProfile(String firstName, String lastName, float weight,
                       float totalDistance, float totalCalories, int totalWorkoutCount){

        mFirstName = firstName;
        mLastName = lastName;
        mWeight = weight;

        mTotalDistance = totalDistance;
        mTotalWorkoutCount = totalWorkoutCount;
        mTotalCalories = totalCalories;
        mTotalTime = 0;

        mWeek = new Week();
        mToday = mWeek.getDayOfTheWeek();
        mGender = "Male";
        mId = mFirstName+mLastName;

    }

    // -----------TEST-------------------------------------------
    public static UserProfile[] test(){
        return new UserProfile[]{
                new UserProfile("Huy", "Vo", 102),
                new UserProfile("Billy", "Jones", 111),
                new UserProfile("John", "Le", 122),
                new UserProfile("Sara", "Jones", 333)
        };
    }

    public static List<UserProfile> test2(){
        return new ArrayList<>(Arrays.asList(test()));
    }
    // --------------------------------------------------------
    public long getTotalTime(){
        return mTotalTime;
    }

    public void setTotalTime(long time){
        mTotalTime = time;
    }
    public String getId(){

        return mId;
    }
    public void setTotalStepCount(int s){
        mTotalStepCount = s;
    }

    public int getTotalStepCount(){
        return mTotalStepCount;
    }


    public void setFirstName(String firstName){
        mFirstName = firstName;
    }

    public void setLastName(String lastName){
        mLastName = lastName;
    }

    public void setWeight(float weight){
        mWeight = weight;
    }

    public void setTotalDistance( float distance ){
        mTotalDistance = distance;
    }

    public void setTotalCalories( float calories ){
        mTotalCalories = calories;
    }

    public void setTotalWorkoutCount( int workoutCount ){
        mTotalWorkoutCount = workoutCount;
    }

    public String getFirstName(){ return mFirstName; }

    public String getLastName(){ return mLastName; }

    public float getWeight(){ return mWeight; }

    public float getTotalWorkoutCount(){
        return mTotalWorkoutCount;
    }

    public double getTotalDistance(){
        return mTotalDistance;
    }

    public float getTotalCalories(){
        return mTotalCalories;
    }
    public void setId(String id){
        mId = id;
    }


    public String getGender(){ return mGender; }
    public void setGender(String gender){
        mGender = gender;
    }

    public void setWeek(Week w){
        mWeek = w;
    }
    public Week getWeek(){
        return mWeek;
    }

    double distance = 0;
    public void setTodayDistance(double d){
        Day today = mWeek.getDay(mToday);
        Log.i(UserProfile.class.getName(), today.getDay());
        today.setDistance(d);

        distance = d;
    }

    private int count = 0;

    public void setTodaySteps(int s){
        Day today = mWeek.getDay(mToday);
        today.setStepCount(s);
    }

    public void setTodayCalories(float cal){
        Day today = mWeek.getDay(mToday);
        today.setCaloriesBurned(today.getCaloriesBurned()+cal);
    }

    public void setTodayTime(long t){
        Day today = mWeek.getDay(mToday);
        today.setTime(today.getTime()+t);

    }
    public void setTodayWorkout(int c){
        Day today = mWeek.getDay(mToday);
        today.setDidWorkout(c);
    }

    public double getWeeklyDistance(){
        return mWeek.getWeeklyDistance();
    }

    public int getWeeklyWorkoutCount(){
        return mWeek.getWeeklyWorkedOut();
    }


    public float getWeeklyCalories(){
        return mWeek.getWeeklyCaloriesBurned();
    }


    public long getWeeklyTime(){
        return mWeek.getWeeklyTime();
    }




    public String getGson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static UserProfile newInstance(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, UserProfile.class);
    }

}