package com.huyvo.alphafitness.model;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Week {
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
        mDayOfTheWeek = mCalendar.get(Calendar.DAY_OF_WEEK);

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

    public void setWeekOfTheYear(int w){
        mWeekOfTheYear = w;
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
            total = d.getTime();
        }
        return total;
    }

    public int getWeeklyWorkedOut(){
        int total = 0;
        for(Day d: mDays)
            total += d.getDidWorkout();
        return total;
    }

    public float getWeeklyCaloriesBurned(){
        float total = 0;
        for(Day d:mDays)
            total = d.getCaloriesBurned();
        return total;
    }

    public float getWeeklyDistance(){
        float total = 0;
        for(Day d: mDays)
            total += d.getDistance();
        return total;
    }
    // ----------------------------END--------------------------------------

    public static int idxWeekOfTheYear(){
        return new GregorianCalendar().get(Calendar.WEEK_OF_YEAR)-1;
    }
    public String toJson(){
        return new Gson().toJson(this);
    }

    public static Week newInstance(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Week.class);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        for (Day d: mDays){
            sb.append(d.toString());
            sb.append('\n');
        }

        return sb.toString();
    }


}
