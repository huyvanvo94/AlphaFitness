package com.huyvo.alphafitness.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Data {

    // Data for total distance
    public final static List<LatLng> mLatLng = Collections.synchronizedList(new ArrayList<LatLng>());
    public final static DataStepModel mStepModel = DataStepModel.getDataStepsModel();
    public static UserProfile userProfile = UserProfile.test()[0];


}
