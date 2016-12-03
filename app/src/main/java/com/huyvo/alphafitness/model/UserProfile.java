package com.huyvo.alphafitness.model;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UserProfile{

    static final String TAG = UserProfile.class.getName();

    public final static String DISTANCE_UNIT = "miles";
    public final static String CALORIES_UNIT = "cal";
    public final static String COUNT_UNIT    = "times";
    public final static String WEIGHT_UNIT   = "lbs";

    private String mId;
    private String mFirstName;
    private String mLastName;
    private float mWeight;
    private String mGender;

    private Year mYear;
    //Today
    private int mToday;

    public UserProfile(String firstName, String lastName, float weight, String gender){
        this(firstName, lastName,weight, gender, Year.getThisYear());
    }

    public UserProfile(String firstName, String lastName, float weight, String gender,
                       Year year){

        mFirstName = firstName;
        mLastName = lastName;
        mWeight = weight;
        mYear = year;
        mToday = mYear.getCurrentWeek().getDayOfTheWeek();
        mGender = gender;
        mId = mFirstName+mLastName;
    }

    public Day getToday(){
        return mYear.getCurrentWeek().getDay(mToday);
    }

    public long getTotalTime(){
        return mYear.getTotalTime();
    }

    public String getId(){
        return mId;
    }

    public int getTotalStepCount(){
        return mYear.getTotalStepCount();
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

    public String getFirstName(){ return mFirstName; }

    public String getLastName(){ return mLastName; }

    public float getWeight(){ return mWeight; }

    public int getTotalWorkoutCount(){
        return mYear.getTotalWorkoutCount();
    }

    public double getTotalDistance(){
        return mYear.getTotalDistance();
    }

    public double getTotalCalories(){
        return mYear.getTotalCalories();
    }
    public void setId(String id){
        mId = id;
    }


    public String getGender(){ return mGender; }
    public void setGender(String gender){
        mGender = gender;
    }
    public Year getYear(){
        return mYear;
    }

    public void setTodayDistance(double d){
        mYear.getCurrentWeek().getDay(mToday).setDistance(d);

    }

    public double getTodayDistance(){
        return mYear.getCurrentWeek().getDay(mToday).getDistance();
    }


    public void setTodaySteps(int s){
        mYear.getCurrentWeek().getDay(mToday).setStepCount(s);
    }

    public void setTodayCalories(double cal){
        mYear.getCurrentWeek().getDay(mToday).setCaloriesBurned(cal);
    }
    public void setTodayTime(long t){
        mYear.getCurrentWeek().getDay(mToday).setTime(t);
    }
    public void setTodayWorkout(int c){
        mYear.getCurrentWeek().getDay(mToday).setWorkoutCount(c);
    }
    public double getWeeklyDistance(){
        return mYear.getCurrentWeek().getWeeklyDistance();
    }
    public int getWeeklyWorkoutCount(){
        return mYear.getCurrentWeek().getWeeklyWorkedOut();
    }
    public double getWeeklyCalories(){
        return mYear.getCurrentWeek().getWeeklyCaloriesBurned();
    }
    public long getWeeklyTime(){
        return mYear.getCurrentWeek().getWeeklyTime();
    }
    public String getGson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static UserProfile newInstance(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, UserProfile.class);
    }


    // -----------TEST-------------------------------------------
    public static UserProfile[] test(){
        return new UserProfile[]{
                new UserProfile("Huy", "Vo", 102, "Male"),
                new UserProfile("Billy", "Jones", 111, "Female"),
                new UserProfile("John", "Le", 122, "Female"),
                new UserProfile("Sara", "Jones", 333, "Male")
        };
    }

    public static List<UserProfile> test2(){
        return new ArrayList<>(Arrays.asList(test()));
    }
    // --------------------------------------------------------

}