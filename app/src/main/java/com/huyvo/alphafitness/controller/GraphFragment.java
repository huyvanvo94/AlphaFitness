package com.huyvo.alphafitness.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huyvo.alphafitness.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GraphFragment extends Fragment{


    private GraphView mGraphView;
    public GraphFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static GraphFragment newInstance() {
        GraphFragment fragment = new GraphFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_graph, container, false);
        mGraphView = (GraphView) rootView.findViewById(R.id.graph);


        int[] time = {5, 10, 15, 20};
        int[] value = {10, 20, 30, 40};

        setDataPoints(time, value);




        GridLabelRenderer gridLabel = mGraphView.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Time (Minutes)");


        return rootView;
    }

    // (x, y)
    private void setDataPoints(int[] time, int[] value){

        DataPoint[] dataPoints = new DataPoint[time.length];

        for(int idx=0; idx< time.length; idx++){
            dataPoints[idx] = new DataPoint(time[idx], value[idx]);
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        mGraphView.addSeries(series);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
