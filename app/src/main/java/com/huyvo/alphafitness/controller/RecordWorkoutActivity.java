package com.huyvo.alphafitness.controller;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.huyvo.alphafitness.R;

public class RecordWorkoutActivity extends AppCompatActivity {
    private static final String TAG = RecordWorkoutActivity.class.getName();

    private MapWorkoutFragment mMapWorkoutFragment;
    private GraphFragment mGraphFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_workout);

        /*
        UserManager manager = new UserManager(getApplicationContext());

        manager.addUsers(UserProfile.test2());

        List<UserProfile> userProfiles = manager.getUsers();

        for(UserProfile userProfile: userProfiles){
            Log.i(TAG, userProfile.getFirstName());
            Log.i(TAG, userProfile.getId());
            Log.i(TAG, "----------------------------------");
        } */


        //

        displayUI();
    }

    private void displayUI(){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, MapWorkoutFragment.newInstance())
                    .commit();
        } else{


            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, GraphFragment.newInstance())
                    .commit();


        }
    }


}
