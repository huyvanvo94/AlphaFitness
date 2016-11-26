package com.huyvo.alphafitness.helper;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.OrientationEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.SENSOR_SERVICE;


public class StepCountHelper implements SensorEventListener{

   // private final static String TAG = "StepDetector";
    private float   mLimit = 10;
    private float   mLastValues[] = new float[3*2];
    private float   mScale[] = new float[2];
    private float   mYOffset;

    private float   mLastDirections[] = new float[3*2];
    private float   mLastExtremes[][] = { new float[3*2], new float[3*2] };
    private float   mLastDiff[] = new float[3*2];
    private int     mLastMatch = -1;

    private ArrayList<StepDetector.StepListener> mStepListeners = new ArrayList<>();


    private static final float SHAKE_THRESHOLD_GRAVITY = 19.0f;
    private static final int SHAKE_TIME_LAPSE = 500;
    private long mTimeOfLastShake = 0;

    public static boolean DEBUG = true;
    private static final String TAG = StepCountHelper.class.getName();

    private SensorManager mSensorManager;
    private Sensor mStepCounter;
    private Sensor mStepDetector;
    private Sensor mSensorAccelerometer;
    private List<OnStepCountListener> mListeners;

    private OrientationEventListener orientationEventListener;
    private int currentOrientation;
    private Context mContext;


    public StepCountHelper(Context context){
        mContext = context;
        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);


        if(DEBUG){
            mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_UI);

        }
        else if(mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null){

            mStepCounter = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            mStepDetector = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            mSensorManager.registerListener(this, mStepCounter, SensorManager.SENSOR_DELAY_UI);
            mSensorManager.registerListener(this, mStepDetector, SensorManager.SENSOR_DELAY_UI);
        }

        mListeners = new ArrayList<>();
    }



    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        final int value = sensorEvent.sensor.getType();

        if(!DEBUG && value==Sensor.TYPE_STEP_DETECTOR){
            notifyListeners();
        }
        else if(value==Sensor.TYPE_ACCELEROMETER){
            detectShake(sensorEvent);
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private float last_x = 0;
    private float last_y = 0;
    private float last_z = 0;

    private void detectShake(SensorEvent sensorEvent){
        long curTime = System.currentTimeMillis();
        // only allow one update every 100ms.
        if ((curTime - mTimeOfLastShake) > 100) {
            long diffTime = (curTime - mTimeOfLastShake);
            mTimeOfLastShake = curTime;

            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

            if (speed > SHAKE_THRESHOLD_GRAVITY-1) {
                notifyListeners();
            }
            last_x = x;
            last_y = y;
            last_z = z;
        }
    }

    public void destroy(){
        if(DEBUG){
            mSensorManager.unregisterListener(this, mSensorAccelerometer);
            return;
        }
        mSensorManager.unregisterListener(this, mStepDetector);
        mSensorManager.unregisterListener(this, mStepCounter);
    }


    public void resume(){
        if(DEBUG){
            mSensorManager.registerListener(this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_UI);
            return;
        }
        mSensorManager.registerListener(this, mStepCounter, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mStepDetector, SensorManager.SENSOR_DELAY_UI);
    }


    private void notifyListeners(){
        for(OnStepCountListener listener:mListeners)
            listener.onStep();
    }

    public void addListener(OnStepCountListener stepCountListener){
        mListeners.add(stepCountListener);
    }

    public void removeListener(OnStepCountListener stepCountListener){
        mListeners.remove(stepCountListener);
    }
    public interface OnStepCountListener{
        void onStep();
    }
}
