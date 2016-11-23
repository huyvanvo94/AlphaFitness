package com.huyvo.alphafitness.model;

public class CaloriesCounter {

    private static double AVERAGE_STEP_WOMEN = 2.2; // feet
    private static double AVERAGE_STEP_MEN = 2.5; // feet

    private UserProfile mUserProfile;


    public CaloriesCounter( UserProfile userProfile ){
        mUserProfile = userProfile;
    }

    public double computeCaloriesBurned(){
        double total = 0;

        return total;
    }
}
