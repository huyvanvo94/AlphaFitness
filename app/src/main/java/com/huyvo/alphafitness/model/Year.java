package com.huyvo.alphafitness.model;

import com.google.gson.Gson;

import java.util.Calendar;

public class Year {

    public final int NUM_WEEK = 52;

    private int mYear;


    public Week[] mWeeks;


    public Year(){
        this(Calendar.getInstance().get(Calendar.YEAR));
    }

    public Year(int year){
        mYear = year;
        mWeeks = new Week[NUM_WEEK];
    }

    public int getYear(){
        return mYear;
    }


    public boolean setWeek(int index, Week week){
        if(index < 0 || index>NUM_WEEK) return false;
        mWeeks[index] = week;
        return true;
    }

    public Week getWeek(int index){
        if (index >= 0 && index < NUM_WEEK){
            return mWeeks[index];
        }
        return null;
    }


    // END

    public String toJson(){
        return new Gson().toJson(this);
    }

    public static Year newInstance(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Year.class);
    }

}
