package com.huyvo.alphafitness.controller;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huyvo.alphafitness.R;
import com.huyvo.alphafitness.helper.Formatter;
import com.huyvo.alphafitness.helper.WorkoutManager;
import com.huyvo.alphafitness.model.StopWatch;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GraphFragment extends Fragment{

    private static String TAG = GraphFragment.class.getName();
    private LineGraphSeries<DataPoint> mStepCountSeries;
    private LineGraphSeries<DataPoint> mCaloriesCountSeries;

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

    TextView avgTextView;// = (TextView) rootView.findViewById(R.id.text_view_average);
    TextView minTextView; //= (TextView) rootView.findViewById(R.id.text_view_min);
    TextView maxTextView; //= (TextView) rootView.findViewById(R.id.text_view_max);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_graph, container, false);
        mGraphView = (GraphView) rootView.findViewById(R.id.graph);

        GridLabelRenderer gridLabel = mGraphView.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Time (Minutes)");

        update();

        avgTextView = (TextView) rootView.findViewById(R.id.text_view_average);
        minTextView = (TextView) rootView.findViewById(R.id.text_view_min);
        maxTextView = (TextView) rootView.findViewById(R.id.text_view_max);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                mHandler.sendEmptyMessage(0);
            }
        },0, StopWatch.ONE_MINUTE);


        return rootView;
    }

    private final android.os.Handler mHandler = new android.os.Handler(){
        public void handleMessage(Message msg){
            if(WorkoutManager.sharedInstance().getCurrentUser() == null) return;

            if(avgTextView == null || minTextView == null || maxTextView == null) return;

            double avg = WorkoutManager.sharedInstance().getAverage();
            double min = WorkoutManager.sharedInstance().getMin();
            double max = WorkoutManager.sharedInstance().getMax();

            avgTextView.setText(Formatter.formatNumber(avg));
            minTextView.setText(Formatter.formatNumber(min));
            maxTextView.setText(Formatter.formatNumber(max));
        }
    };

    class Plotter{

        private int mColor;
        private LineGraphSeries<DataPoint> mSeries;

        public Plotter(int color){
            mColor = color;
        }

        LineGraphSeries<DataPoint> makePlot(List<Double> data){

            int len = data.size();
            int[] time = new int[len];
            double[] values = new double[len];

            for(int i=0; i<len; i++){
                time[i] = i+1;
                values[i] = data.get(i);
            }

            DataPoint[] dataPoints = new DataPoint[len];

            for(int i=0; i<len; i++){
                dataPoints[i] = new DataPoint(time[i], values[i]);
            }

            mSeries = new LineGraphSeries<>(dataPoints);

            mSeries.setColor( mColor );

            return mSeries;

        }
    }
    private void setCaloriesDataPoint(int[] time, double[] values){
        DataPoint[] dataPoints = new DataPoint[time.length];

        for(int idx=0; idx< time.length; idx++){
            dataPoints[idx] = new DataPoint(time[idx], values[idx]);
        }
        mCaloriesCountSeries = new LineGraphSeries<>(dataPoints);
        mCaloriesCountSeries.setTitle("Calories");
        mCaloriesCountSeries.setColor(Color.BLUE);
        mGraphView.addSeries(mCaloriesCountSeries);
    }


    // (x, y)
    private void setStepsDataPoints(int[] time, int[] value){

        DataPoint[] dataPoints = new DataPoint[time.length];

        for(int idx=0; idx< time.length; idx++){
            dataPoints[idx] = new DataPoint(time[idx], value[idx]);
        }
        mStepCountSeries =  new LineGraphSeries<>(dataPoints);
        mStepCountSeries.setTitle("Step");
        mStepCountSeries.setColor(Color.RED);
        mGraphView.addSeries(mStepCountSeries);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    private void update(){

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                if(WorkoutManager.sharedInstance().getCurrentUser() == null) return;

                mGraphView.refreshDrawableState();
                mGraphView.removeAllSeries();

                List<Integer> stepCountTrack = WorkoutManager.sharedInstance().getStepCountTrack();

                int[] time = new int[stepCountTrack.size()];
                int[] value = new int[stepCountTrack.size()];


                for(int idx = 0; idx<time.length; idx++){
                    time[idx] = idx+1;
                    value[idx] = stepCountTrack.get(idx);
                }

                setStepsDataPoints(time, value);
                //
                List<Double> caloriesCountTrack = WorkoutManager.sharedInstance().getCaloriesCountTrack();

                time = new int[caloriesCountTrack.size()];
                double[] values = new double[caloriesCountTrack.size()];

                for(int idx = 0; idx<time.length; idx++){
                    time[idx] = idx+1;
                    values[idx] = caloriesCountTrack.get(idx);
                }

                setCaloriesDataPoint(time, values);


            }
        }, 0, StopWatch.FIVE_MINUTE);

    }


}
