package com.huyvo.alphafitness.helper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.huyvo.alphafitness.FitnessInterface;
import com.huyvo.alphafitness.model.StopWatch;
import com.huyvo.alphafitness.model.UserProfile;

import java.util.Timer;
import java.util.TimerTask;

public class WorkoutService extends Service implements
        StepCountHelper.OnStepCountListener,
        LocationHelper.OnLocationListener{

    private static final String TAG = WorkoutService.class.getName();

    FitnessInterface.Stub mBinder;

    private State mState;
    private Location mLocation;


    public static Intent newIntent(Context context){
        return new Intent(context, WorkoutService.class);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
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

        mBinder = new FitnessInterface.Stub(){

            @Override
            public void start() throws RemoteException {
                Log.i(TAG, "start");
                init();
            }
        };

    }

    private void init(){
        LocationHelper mLocationHelper = new LocationHelper(this);
        StepCountHelper mStepCountHelper = new StepCountHelper(this);
        mLocationHelper.addListener(this);
        mStepCountHelper.addListener(this);
        mState = new State();

        UserManager userManager = new UserManager(this);
        saveUserPeriodically(userManager);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestory()");

        super.onDestroy();
    }


    /**
     * onLocationChanged is literally
     * the same method in MapWorkoutFragment.
     */
    @Override
    public void onLocationChanged(final Location location) {
        Log.i(TAG, "onLocationChanged");

        mLocation = location; // get location

        if (mState.onLocationBusy) return;
        mState.onLocationBusy = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                WorkoutManager.sharedInstance()
                        .getMap()
                        .add(mLocation);
                mState.onLocationBusy = false;
            }
        }).start();
    }

    @Override
    public void onStep() {
        if (mState.onStepBusy) return;
        Log.i(TAG, "onStep()");
        mState.onStepBusy = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                WorkoutManager.sharedInstance().incStep();
                mState.onStepBusy = false;
            }
        }).start();
    }

    private void saveUserPeriodically(final UserManager userManager){
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                Log.i(TAG, "saveUserPeriodically()");
                UserProfile mCurrentUser = WorkoutManager.sharedInstance().getCurrentUser();
                if(mCurrentUser != null){
                    userManager.updateUser(mCurrentUser);
                    UserManager.saveUserPreference(getApplicationContext(), mCurrentUser);
                }
            }
        }, 0, StopWatch.TWO_MINUTE);
    }

    private class State{

        State(){
            onLocationBusy = false;
            onStepBusy = false;
        }

        boolean onLocationBusy;
        boolean onStepBusy;
    }

}
