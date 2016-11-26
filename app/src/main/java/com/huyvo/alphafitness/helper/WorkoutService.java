package com.huyvo.alphafitness.helper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import com.huyvo.alphafitness.model.Utils;

/**
 * This class is for counting the steps and location.
 * It is the MapWorkoutFragment but without the UI.
 */
public class WorkoutService extends Service implements
        StepCountHelper.OnStepCountListener,
        LocationHelper.OnLocationListener,
        WorkoutManager.EndServiceListener {

    private static final String TAG = WorkoutService.class.getName();
    private WorkoutManager mWorkoutManager;
    private Location mLocation;
    public Thread t1;
    private static boolean mWorkoutStarted = false;
    public final static String MAP_STATE_ID = "MAP_STATE";
    private static boolean mFindLocationIsBusy = false;
    private final static String LOCATION_BUSY_STATE = "LOCATION_BUSY_STATE";
    private static boolean mStepCountBusy = false;
    private final static String STEP_COUNT_BUSY = "STEP_COUNT_BUSY";
    private static boolean mServiceStarted = false;
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
        mWorkoutManager.setService(this);

        mLocationHelper = new LocationHelper(this);
        mStepCountHelper = new StepCountHelper(this);

        mLocationHelper.addListener(this);
        mStepCountHelper.addListener(this);

    }

    @Override
    public void onDestroy() {
        //Thread.currentThread().interrupt();
        Log.i(TAG, "onDestory()");
        mLocationHelper.removeListener(this);
        mStepCountHelper.removeListener(this);
        mWorkoutManager.removeService();
        super.onDestroy();
    }


    /**
     * onLocationChanged is literally
     * the same method in MapWorkoutFragment.
     */
    @Override
    public void onLocationChanged(Location location) {
        if (!mWorkoutStarted) return;
        mLocation = location; // get location
        if (mFindLocationIsBusy) return;
        mFindLocationIsBusy = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                compute();
                mFindLocationIsBusy = false;
            }
        }).start();
    }

    @Override
    public void onStep() {
        if (mStepCountBusy) return;
        Log.i(TAG, "onStep()");
        mStepCountBusy = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                mWorkoutManager.incStep();
                Log.i(TAG, String.valueOf(mWorkoutManager.getStepCount()));
                mWorkoutManager.updateUserStep();
                mStepCountBusy = false;
            }
        }).start();
    }

    private void compute(){
        LatLng to = Utils.getLatLng(mLocation);
        if(mWorkoutManager.computeCurrentDistanceTo(to)){
            mWorkoutManager.updateUserDistance();
        }
    }

    /**
     * My intention is if the MapWorkoutFragment resume,
     * that is, the UI is back up, all the work
     * will be back onto the MapWorkoutFragment.
     *
     * WorkoutManager has the interface.
     * WorkoutService implements the interface.
     *
     * Every time when there is a new instance of MapWorkoutFragment,
     * it will call this method.
     */
    @Override
    public void turnOffService() {
        Log.i(TAG, "Off");
        stopSelf();
    }
}
