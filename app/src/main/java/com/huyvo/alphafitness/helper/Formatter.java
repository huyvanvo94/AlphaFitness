package com.huyvo.alphafitness.helper;

import com.huyvo.alphafitness.model.StopWatch;
import com.huyvo.alphafitness.model.UserProfile;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import static com.huyvo.alphafitness.model.UserProfile.CALORIES_UNIT;
import static com.huyvo.alphafitness.model.UserProfile.COUNT_UNIT;
import static com.huyvo.alphafitness.model.UserProfile.DISTANCE_UNIT;


public class Formatter {

    public final static String SPACE = " ";

    private UserProfile mUserProfile;

    public Formatter(UserProfile userProfile){
        mUserProfile = userProfile;
    }

    // May take a while
    public String[] array(){
        String[] s = new String[10];
        s[0] = "Weekly";
        s[1] = appendString("Distance: ", formatNumber(mUserProfile.getWeeklyDistance()), SPACE, DISTANCE_UNIT);
        s[2] = appendString("Time: ", StopWatch.formatTime(mUserProfile.getWeeklyTime()));
        s[3] = appendString("Workout: ", formatNumber(mUserProfile.getWeeklyWorkoutCount()), SPACE, COUNT_UNIT); // weekly workout
        s[4] = appendString("Calories Burned: ", formatNumber(mUserProfile.getWeeklyCalories()),SPACE, CALORIES_UNIT);
        s[5] = "All Time";
        s[6] = appendString("Distance: ", formatNumber(mUserProfile.getTotalDistance()), SPACE, DISTANCE_UNIT);
        s[7] = appendString("Time: ", StopWatch.formatTime(mUserProfile.getTotalTime()));
        s[8] = appendString("Workouts: ", formatNumber(mUserProfile.getTotalWorkoutCount()), SPACE, COUNT_UNIT);
        s[9] = appendString("Calories Burned: ", formatNumber(mUserProfile.getTotalCalories()), SPACE, CALORIES_UNIT);
        return s;
    }

    public static String formatNumber(int number){
        return String.valueOf(number);
    }

    public static String formatNumber(double number){
        NumberFormat formatter = new DecimalFormat("#0.00");
        return formatter.format(number);
    }
    public static String appendString(String ... args){
        StringBuilder stringBuilder = new StringBuilder();
        for(String arg: args){
            stringBuilder.append(arg);
        }
        return stringBuilder.toString();
    }
}
