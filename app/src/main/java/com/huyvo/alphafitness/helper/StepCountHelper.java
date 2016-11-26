package com.huyvo.alphafitness.helper;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.SENSOR_SERVICE;


public class StepCountHelper implements SensorEventListener {
    private static final float SHAKE_THRESHOLD_GRAVITY = 22.0f;
    private static final int SHAKE_TIME_LAPSE = 500;
    private long mTimeOfLastShake;
    public static boolean DEBUG = false;
    private static final String TAG = StepCountHelper.class.getName();

    private SensorManager mSensorManager;
    private Sensor mStepCounter;
    private Sensor mStepDetector;
    private Sensor mSensorAccelerometer;
    private List<OnStepCountListener> mListeners;

    public StepCountHelper(Context context){
        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        if(DEBUG){
            mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_UI);

        }
        else if(mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null){
            Toast.makeText(context, "OKAY!", Toast.LENGTH_LONG).show();
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

        /**

        Sensor sensor = sensorEvent.sensor;
        float[] values = sensorEvent.values;

        if (values.length > 0) {
            value = (int) values[0];
        }

        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

        } else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            // For test only. Only allowed value is 1.0 i.e. for step taken

        }*/
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void detectShake(SensorEvent sensorEvent){
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        float gForceX = x - SensorManager.GRAVITY_EARTH;
        float gForceY = y - SensorManager.GRAVITY_EARTH;
        float gForceZ = z - SensorManager.GRAVITY_EARTH;

        double value = Math.pow(gForceX,2.0)+Math.pow(gForceY,2.0)+Math.pow(gForceZ, 2.0);
        float gForce = (float) Math.sqrt(value);

        if(gForce>= SHAKE_THRESHOLD_GRAVITY-1){
            notifyListeners();
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
