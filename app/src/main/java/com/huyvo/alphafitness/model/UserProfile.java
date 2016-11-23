package com.huyvo.alphafitness.model;

import android.util.Log;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class UserProfile{

    public final static String DISTANCE_UNIT = "miles";
    public final static String CALORIES_UNIT = "Cal";
    public final static String COUNT_UNIT    = "times";
    public final static String WEIGHT_UNIT   = "lbs";

    private Week mWeek;
    // ArrayList
    private UUID mId;
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
    public UserProfile(String id){
        mId = UUID.fromString(id);
    }

    public UserProfile(String firstName, String lastName, float weight){

        mId = UUID.randomUUID();
        mFirstName = firstName;
        mLastName = lastName;
        mWeight = weight;
        mGender = "Male";

        mWeek = new Week();

        // All Time
        mTotalDistance = 0;
        mTotalWorkoutCount = 0;
        mTotalCalories = 0;
        mTotalTime = 0;

        mToday = mWeek.getDayOfTheWeek();
    }

    public UserProfile(String firstName, String lastName, float weight,
                       float totalDistance, float totalCalories, int totalWorkoutCount){

        mFirstName = firstName;
        mLastName = lastName;
        mWeight = weight;

        mTotalDistance = totalDistance;
        mTotalWorkoutCount = totalWorkoutCount;
        mTotalCalories = totalCalories;

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
    // --------------------------------------------------------
    public long getTotalTime(){
        return mTotalTime;
    }

    public void setTotalTime(long time){
        mTotalTime = time;
    }
    public UUID getId(){

        return mId;
    }
    public void setTotalStepCount(int s){
        mTotalStepCount = s;
    }

    public int getTotalStepCount(){
        return mTotalStepCount;
    }
    public void setId(UUID id){
        mId = id;
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
    public void setId(String uuid){
        mId = UUID.fromString(uuid);
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


    public void setTodayDistance(double d){
        Day today = mWeek.getDay(mToday);
        Log.i(UserProfile.class.getName(), today.getDay());
        today.setDistance(d);
    }

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



    public String[] getDisplayToListView(){
        String[] s = new String[13];
        s[0] = getFirstName()+ " " + getLastName(); // Name
        s[1] = "Gender:" + getGender();  // gender
        s[2] = "Weight:" + String.valueOf(getWeight()) + WEIGHT_UNIT; // weight
        s[3] = "Average/Weekly";
        s[4] = "Distance:" + String.valueOf(getWeeklyDistance()) + DISTANCE_UNIT;
        s[5] = "Time:" + getFormattedTime(getWeeklyTime());
        s[6] = "Workout:" + String.valueOf(getWeeklyWorkoutCount()) + COUNT_UNIT; // weekly workout
        s[7] = "Calories Burned:" + String.valueOf(getWeeklyCalories()) + CALORIES_UNIT;
        s[8] = "All Time"; // Label
        s[9] = "Distance:" + String.valueOf(getTotalDistance())+DISTANCE_UNIT;
        s[10] = "Time:" + getFormattedTime(getTotalTime());
        s[11] = "Workouts:" + String.valueOf(getTotalWorkoutCount()) + COUNT_UNIT;
        s[12] = "Calories Burned:" + String.valueOf(getTotalCalories());
        return s;
    }

    public String getWeekStr(){
        return mWeek.toJson();
    }


    public static String getFormattedTime(long millis){
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }
}