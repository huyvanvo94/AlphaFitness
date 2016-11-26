package com.huyvo.alphafitness.helper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import com.huyvo.alphafitness.model.Utils;

public class WorkoutService extends Service implements StepCountHelper.OnStepCountListener,
        LocationHelper.OnLocationListener{

    private static final String TAG = WorkoutService.class.getName();
    private WorkoutManager mWorkoutManager;
    private Location mLocation;
    public Thread t1;

    private boolean mLocationChanged = false;
    private boolean mOnStepDetected = false;

    private StepCountHelper mStepCountHelper;
    private LocationHelper mLocationHelper;


    public static Intent newIntent(Context context){
        return new Intent(context, WorkoutService.class);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.i(TAG, "onStartCommand()");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate()");

        mWorkoutManager = WorkoutManager.sharedInstance();

        mLocationHelper = new LocationHelper(this);
        mStepCountHelper = new StepCountHelper(this);

        mLocationHelper.addListener(this);
        mStepCountHelper.addListener(this);

        /**
        t1 = new Thread(){
            public void run(){

                while (true){

                    if(mLocationChanged){
                        compute();
                        mLocationChanged = false;
                    }

                    if(mOnStepDetected){
                        mWorkoutManager.incStep();
                        mOnStepDetected = false;
                    }
                }
            }
        };

        t1.start();


        */

    }

    @Override
    public void onDestroy() {
        //Thread.currentThread().interrupt();



        super.onDestroy();
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged");
        mLocation = location;
        mLocationChanged = true;
    }

    @Override
    public void onStep() {
        Log.i(TAG, "onStep");
        mOnStepDetected = true;
    }

    private void compute(){
        LatLng to = Utils.getLatLng(mLocation);
        if(mWorkoutManager.computeCurrentDistanceTo(to)){
            mWorkoutManager.updateUserDistance();
        }
    }
}
