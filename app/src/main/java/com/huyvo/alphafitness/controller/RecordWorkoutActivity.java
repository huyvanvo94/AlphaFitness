package com.huyvo.alphafitness.controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.huyvo.alphafitness.FitnessInterface;
import com.huyvo.alphafitness.Login;
import com.huyvo.alphafitness.R;
import com.huyvo.alphafitness.helper.WorkoutService;

public class RecordWorkoutActivity extends AppCompatActivity
        implements MapWorkoutFragment.OnListener,
        ProfileScreenFragment.OnProfileScreenListener,
        UserSettingFragment.OnListener{
    final static String TAG = RecordWorkoutActivity.class.getName();


    MapWorkoutFragment mapWorkoutFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_workout);
        Login login = new Login(getApplicationContext());
        login.requestLogin();
        displayUI();
    }

    private void displayUI(){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mapWorkoutFragment = MapWorkoutFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, mapWorkoutFragment)
                    .commit();
        } else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, GraphFragment.newInstance())
                    .commit();
        }
    }


    @Override
    public void remove(Fragment fragment) {

        if(mapWorkoutFragment!=null){
            mapWorkoutFragment.remove(fragment);
        }
    }

    @Override
    public void onUserSetting(UserSettingFragment fragment) {
        if(mapWorkoutFragment!=null){
            mapWorkoutFragment.replaceFragment(fragment);
        }
    }


    public static Intent newIntent(Context context){
        return new Intent(context, RecordWorkoutActivity.class);
    }

    private FitnessInterface remoteService;
    private RemoteConnection remoteConnection = null;

    @Override
    public void removeUserSetting(UserSettingFragment fragment) {
        if(mapWorkoutFragment != null){
            mapWorkoutFragment.remove(fragment);
        }
    }

    class RemoteConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            Log.i(TAG, "onServiceConnected");
            remoteService = FitnessInterface.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            remoteService = null;
            Log.i(TAG, "onServiceDisconnected");

        }
    }

    @Override
    public void startService(){
        Log.i(TAG, "startService()");
        try {
            remoteConnection = new RemoteConnection();
            remoteService.start();
            Intent mIntentService = WorkoutService.newIntent(getApplicationContext());
            bindService(mIntentService, remoteConnection, BIND_AUTO_CREATE);

        }catch (Exception ignored){}
    }

    @Override
    public void stopService(){
        Log.i(TAG, "stopService");
        if(remoteConnection==null){return; }
        try {
            unbindService(remoteConnection);
            remoteConnection = null;
        }catch (Exception ignored){}
    }
}
