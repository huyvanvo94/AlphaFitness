package com.huyvo.alphafitness.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Week {

    private static final String TAG = Week.class.getName();
    // Contains each day
    private Day[] mDays;
    private int mWeekOfTheYear; // int
    private int mMonthOfTheYear;
    private int mDayOfTheWeek;
    private Calendar mCalendar;
    private Date mToday;

    public Week(){
        mCalendar = new GregorianCalendar();
        mWeekOfTheYear = mCalendar.get(Calendar.WEEK_OF_YEAR);
        mMonthOfTheYear = mCalendar.get(Calendar.MONTH);
        mDayOfTheWeek = mCalendar.get(Calendar.DAY_OF_WEEK)-1;

        mDays = new Day[]{
                new Day(Day.SUN),
                new Day(Day.MON),
                new Day(Day.TUES),
                new Day(Day.WED),
                new Day(Day.THURS),
                new Day(Day.FRI),
                new Day(Day.SAT)
        };
    }

    public int getDayOfTheWeek(){
        return mDayOfTheWeek;
    }


    public int getWeekOfTheYear(){
        return mWeekOfTheYear;
    }

    public Day getDay(String theDay){
        return mDays[Day.getNumDay(theDay)];

    }

    public Day getDay(int theDay){
        return mDays[theDay];
    }

    public void setDay(Day d){
        int idx = Day.getNumDay(d.getDay())-1;
        mDays[idx] = d;
    }

    // ----------------------------Compute------------------------------
    public long getWeeklyTime(){
        long total = 0;
        for(Day d: mDays){
            total += d.getTime();
        }
        return total;
    }

    public int getWeeklyWorkedOut(){
        int total = 0;
        for(Day d: mDays)
            total += d.getWorkoutCount();
        return total;
    }

    public double getWeeklyCaloriesBurned(){
        double total = 0;
        for(Day d:mDays)
            total += d.getCaloriesBurned();

        return total;
    }

    public float getWeeklyDistance(){
        float total = 0;
        for(Day d: mDays) {
            total += d.getDistance();
        }
        return total;
    }



}
