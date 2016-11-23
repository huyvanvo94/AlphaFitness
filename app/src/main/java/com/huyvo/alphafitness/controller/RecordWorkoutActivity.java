package com.huyvo.alphafitness.controller;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.huyvo.testermap.R;

public class RecordWorkoutActivity extends AppCompatActivity {
    private static final String TAG = RecordWorkoutActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**

        UserManager manager = new UserManager(getApplicationContext());
        UserProfile me = UserProfile.test()[0];

        List<UserProfile> list = manager.getUsers();

        Log.i(TAG, list.get(0).getWeek().toString());
         */


        displayUI();

    }

    private void displayUI(){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, LandscapeFragment.newInstance())
                    .commit();
        } else{

        }
    }


}
