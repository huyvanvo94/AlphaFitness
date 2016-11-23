package com.huyvo.alphafitness.model;

public class DataStepModel {
    private int mCount;
    private static DataStepModel model;

    private DataStepModel(){
        mCount = 0;
    }

    public static DataStepModel getDataStepsModel(){
        if( model == null)
            model = new DataStepModel();
        return model;
    }

    public void incr(){
        mCount++;
    }

    public void decr(){
        if(mCount == 0) return;

        mCount--;
    }

    public int getCount(){
        return mCount;
    }

    public void reset(){
        mCount = 0;
    }

}