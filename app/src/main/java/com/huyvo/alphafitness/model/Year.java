package com.huyvo.alphafitness.model;

import java.util.Calendar;

public class Year {

    public static final String TAG = "Year";

    public final int NUM_WEEK = 52;

    private int mYear;

    private Week[] mWeeks;
    private Week mWeek;

    public static Year getThisYear(){
        return new Year(Calendar.getInstance().get(Calendar.YEAR));
    }

    public Year(int year){
        mYear = year;

      //  Log.i(TAG, String.valueOf(year));
       // Log.i(TAG, String.valueOf(numberOfWeeks()));

        mWeeks = new Week[numberOfWeeks()];
        mWeeks[getCurrentWeekIndex()] = new Week();


    }

    public Week[] getWeeks(){
        return mWeeks;
    }

    public Week getCurrentWeek(){
        return mWeeks[getCurrentWeekIndex()];
    }
    public int getCurrentWeekIndex(){
        return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
    }

    public int numberOfWeeks(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2015);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 31);

        int ordinalDay = cal.get(Calendar.DAY_OF_YEAR);
        int weekDay = cal.get(Calendar.DAY_OF_WEEK) - 1; // Sunday = 0
        int numberOfWeeks = (ordinalDay - weekDay + 10) / 7;
        return numberOfWeeks;
    }
    public int getYear(){
        return mYear;
    }

    public Week getWeek(int index) {
        if (index >= 0 && index < NUM_WEEK) {
            return mWeeks[index];
        }
        return null;
    }

    public double getTotalDistance(){
        double total = 0;

        for(Week week: getWeeks()){
            if(week != null){
                total += week.getWeeklyDistance();
            }
        }

        return total;
    }

    public int getTotalWorkoutCount(){
        int total = 0;

        for(Week week: getWeeks()){
            if(week != null){
                total += week.getWeeklyWorkedOut();
            }
        }
        return total;
    }

    public double getTotalCalories(){
        double total = 0;

        for(Week week: getWeeks()){
            if(week != null){
                total += week.getWeeklyCaloriesBurned();
            }
        }

        return total;
    }

    public long getTotalTime(){
        long total = 0;

        for(Week week: getWeeks()){
            if(week != null){
                total += week.getWeeklyTime();
            }
        }
        return total;
    }

    public int getTotalStepCount(){
        int total = 0;

        for(Week week: getWeeks()){
            if(week != null){

            }
        }

        return total;
    }

}
