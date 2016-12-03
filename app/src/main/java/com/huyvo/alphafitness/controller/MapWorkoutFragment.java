package com.huyvo.alphafitness.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.huyvo.alphafitness.R;
import com.huyvo.alphafitness.helper.Formatter;
import com.huyvo.alphafitness.helper.LocationHelper;
import com.huyvo.alphafitness.helper.StepCountHelper;
import com.huyvo.alphafitness.helper.UserManager;
import com.huyvo.alphafitness.helper.WorkoutManager;
import com.huyvo.alphafitness.model.StopWatch;
import com.huyvo.alphafitness.model.UserProfile;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

// The controller class to
// implement map view
public class MapWorkoutFragment extends Fragment
        implements LocationHelper.OnLocationListener,
        StepCountHelper.OnStepCountListener{

    public final static String TAG = MapWorkoutFragment.class.getName();
    private OnListener mListener;
    public static final int DRAW_ROUTE       = 1;
    public static final int DISPLAY_DISTANCE = 2;

    private TextView mDistanceTextView;
    private LinearLayout mLinearLayout;
    private MapView mMapView;
    private LocationHelper mLocationHelper;
    private StepCountHelper mStepCountHelper;
    private Location mLocation;
    private WorkoutManager mWorkoutManager;
    private static MapController mMapController;

    public static MapWorkoutFragment newInstance() {
        return new MapWorkoutFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "onCreate");

        State.serviceStarted = false;

        mLocationHelper = new LocationHelper(getActivity());
        mStepCountHelper = new StepCountHelper(getActivity());
        //
        mWorkoutManager = WorkoutManager.sharedInstance();
        //
        mStepCountHelper.addListener(this);
        mLocationHelper.addListener(this);

        UserManager userManager = new UserManager(getContext());

        saveUserPeriodically(userManager);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_map_workout, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception ignored) {}

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                if (checkPermission()) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        Button button = (Button) rootView.findViewById(R.id.recenter_start);
                        if (button.getVisibility() == View.INVISIBLE) {
                            button.setVisibility(View.VISIBLE);
                        } else {
                            button.setVisibility(View.INVISIBLE);
                        }
                    }
                });

                mMapController = new MapController(googleMap);
                // Check if the view is resuming or not
                if(State.workoutStarted) {
                    Log.i(TAG, "Starting MapView");
                    startMapView();
                }

            }
        });

        Button mCenterScreen = (Button) rootView.findViewById(R.id.recenter_start);
        mCenterScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWorkoutManager.getMap().size() == 0)
                    return;
                LatLng ll = mWorkoutManager.getMap().getLast();
                mMapController.zoomCamera(ll);
            }
        });
        mLinearLayout = (LinearLayout) rootView.findViewById(R.id.container_2);
        Button mButtonStart = (Button) rootView.findViewById(R.id.button_start);
        initWorkoutButton(mButtonStart);

        Button mButtonUserProfile = (Button) rootView.findViewById(R.id.user_profile_button);
        if (mButtonUserProfile != null) {
            mButtonUserProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setProfileScreenFragment();
                }
            });
        }

        mTimeTextView = (TextView) rootView.findViewById(R.id.time_text_view);

        mDistanceTextView = (TextView) rootView.findViewById(R.id.distance_text_view);


        return rootView;
    }

    private TextView mTimeTextView;
    // Update time for every one second
    private Timer mTimer;
    private void startTimer(){

            mWorkoutManager.startStopWatch();
            mTimer = new Timer();
            mTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {

                    mHandlerTime.obtainMessage(1).sendToTarget();

                }
            }, 0, 1000);

    }

    private void stopTimer(){
        mTimer.cancel();
        mTimer.purge();
    }

    private void initWorkoutButton(final Button mButtonWorkout) {
        if (mButtonWorkout != null) {
            if (State.workoutStarted) {
                mButtonWorkout.setText(getString(R.string.stop));
            } else {
                mButtonWorkout.setText(getString(R.string.start));
            }
            mButtonWorkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!State.workoutStarted) {
                        mButtonWorkout.setText(getString(R.string.stop));
                        startTimer();
                        startMapView();
                    } else {
                        mButtonWorkout.setText(getString(R.string.start));
                        State.onLocationBusy = true;
                        stopTimer();
                        mWorkoutManager.pauseStopWatch();
                    }
                    toggle();
                }
            });
        }
    }

    private void toggle()
    {
        State.workoutStarted = !State.workoutStarted;
    }

    private ProfileScreenFragment profileScreenFragment;

    private void setProfileScreenFragment() {
        profileScreenFragment = ProfileScreenFragment.newInstance();
        mLinearLayout.setVisibility(LinearLayout.INVISIBLE);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container,  profileScreenFragment)
                .commit();
    }

    public void replaceFragment(Fragment fragment){
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private void startMapView() {
        if (checkPermission()) return;

        mMapController.clear();
        mLocation = mLocationHelper.getLastKnowLocation();
        if(mLocation == null){
            Log.i(TAG, "mLocation is null");
        }

        if(!mMapController.startView(mLocation)) {
            return;
        }

        mWorkoutManager
                .getMap()
                .add(mLocation);
    }

    private void drawRoute(List<LatLng> route) {
        if(!mMapController.drawRoute(route)){
            return;
        }
        LatLng latLng = route.get(route.size() - 1);
        mMapController.zoomCamera(latLng);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged");
        if (!State.workoutStarted) return;
        mLocation = location; // get location
        if (State.onLocationBusy) return;
        State.onLocationBusy = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(addToMap())
                    mHandler.sendEmptyMessage(DRAW_ROUTE);

                State.onLocationBusy = false;
            }
        }).start();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");
        mMapView.onPause();

        if (State.workoutStarted && !State.serviceStarted) {
            State.serviceStarted = true;

            mListener.startService();
        }



        if(profileScreenFragment != null){
            getActivity().
                    getSupportFragmentManager()
                    .beginTransaction()
                    .remove(profileScreenFragment)
                    .commit();
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
        mLocationHelper.removeListener(this);
        mStepCountHelper.removeListener(this);
        mMapView.onDestroy();
        super.onDestroy();
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
        mLocationHelper.addListener(this);
        mStepCountHelper.addListener(this);

        if(State.serviceStarted){
            State.serviceStarted = false;
            mListener.stopService();
        }

        mTimeTextView.setText(mWorkoutManager.getFormattedTime());
        mMapView.onResume();
        if(State.workoutStarted) {
            startTimer();
            startMapView();
            mLocation = mLocationHelper.getLastKnowLocation();
            if(addToMap()) {
                mHandler.sendEmptyMessage(DRAW_ROUTE);
            }
           mHandler.sendEmptyMessage(DISPLAY_DISTANCE);
        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            Activity a = getActivity();
            if(a != null && profileScreenFragment != null)
                a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    public void remove(Fragment fragment) {
        getActivity().
                getSupportFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .commit();
        if(fragment instanceof UserSettingFragment) {
            setProfileScreenFragment();
        }else{
            mLinearLayout.setVisibility(LinearLayout.VISIBLE);
        }
    }

    @Override
    public void onStep() {
        if(!State.workoutStarted) return;
        if (State.onStepBusy) return;
        Log.i(TAG, "onStep()");
        State.onStepBusy = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                mWorkoutManager.incStep();
                mHandler.sendEmptyMessage(DISPLAY_DISTANCE);
                State.onStepBusy = false;
            }
        }).start();
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private boolean addToMap() {
        if(mLocation == null) {
            Log.i(TAG, "mLocation is null");
            return false;
        }
        return mWorkoutManager.getMap().add(mLocation);
    }

    private boolean addToMap(LatLng latLng){
        return mWorkoutManager.getMap().add(latLng);
    }

    private void displayDistance() {
        Log.i(TAG, mWorkoutManager.getCurrentUser().getFirstName());
        Log.i(TAG, mWorkoutManager.getCurrentUser().getToday().getDay());
        double distance = mWorkoutManager.getDistance();

        if (mDistanceTextView != null) {
            Log.i(TAG, "displayDistance()");
            Log.i(TAG, "distance="+String.valueOf(distance));
            mDistanceTextView.setText(
                    Formatter.appendString(Formatter.formatNumber(distance),
                    Formatter.SPACE,
                    UserProfile.DISTANCE_UNIT)
            );
        }
    }
    private final Handler mHandlerTime = new Handler(){
        public void handleMessage(Message msg){
            mTimeTextView.setText(mWorkoutManager.getFormattedTime());
        }
    };

    private final Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            Log.i(TAG, "mHandler");
            if(msg.what == DRAW_ROUTE) {
                drawRoute(mWorkoutManager
                        .getMap()
                        .getRoute());
            }

            else if(msg.what == DISPLAY_DISTANCE){
                displayDistance();
            }
        }
    };

    private boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Save current user every one minute
     *
     * @param userManager The Content Provider
     */
    private void saveUserPeriodically(final UserManager userManager){
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.i(TAG, "saveUserPeriodically()");
                UserProfile mCurrentUser = WorkoutManager.sharedInstance().getCurrentUser();
                if(mCurrentUser != null){
                    userManager.updateUser(mCurrentUser);
                    UserManager.saveUserPreference(getContext(), mCurrentUser);
                }
            }
        }, 0, StopWatch.TWO_MINUTE);
    }

    public static class State{
        static boolean workoutStarted = false;
        static boolean onLocationBusy = false;
        static boolean onStepBusy = false;
        static boolean serviceStarted = false;
    }


    interface OnListener{
        void stopService();
        void startService();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;
        if (context instanceof Activity){
            Log.i(TAG, "onAttach()");
            a = (Activity) context;
            mListener = (OnListener) a;
        }

    }
    @Override
    public void onDetach() {
        Log.i(TAG, "onDetach()");
        super.onDetach();
        mListener = null;
    }
}
