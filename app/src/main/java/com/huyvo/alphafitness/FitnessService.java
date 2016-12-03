package com.huyvo.alphafitness;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.RemoteException;

import com.huyvo.alphafitness.helper.LocationHelper;
import com.huyvo.alphafitness.helper.StepCountHelper;
import com.huyvo.alphafitness.helper.UserManager;
import com.huyvo.alphafitness.helper.WorkoutManager;

public class FitnessService extends Service {

    static final String TAG = FitnessService.class.getName();

    FitnessInterface.Stub mBinder;

   // private State mState;
    private WorkoutManager mWorkoutManager;
    private Location mLocation;
    private StepCountHelper mStepCountHelper;
    private LocationHelper mLocationHelper;

    private UserManager mUserManger;
    public FitnessService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
        mBinder = new FitnessInterface.Stub(){
            @Override
            public void start() throws RemoteException {


            }
        };


    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
