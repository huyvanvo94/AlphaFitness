package com.huyvo.alphafitness.controller;

import android.Manifest;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.huyvo.alphafitness.R;
import com.huyvo.alphafitness.helper.LocationHelper;
import com.huyvo.alphafitness.helper.StepCountHelper;
import com.huyvo.alphafitness.helper.UserManager;
import com.huyvo.alphafitness.helper.WorkoutManager;
import com.huyvo.alphafitness.helper.WorkoutService;
import com.huyvo.alphafitness.model.UserProfile;
import com.huyvo.alphafitness.model.Utils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.huyvo.alphafitness.model.Utils.LEVEL_ZOOM;
import static com.huyvo.alphafitness.model.Utils.getLatLng;
// The controller class to
// implement map view
public class MapWorkoutFragment extends Fragment
        implements LocationHelper.OnLocationListener,
        StepCountHelper.OnStepCountListener,
        ProfileScreenFragment.OnProfileScreenListener {

    public final static String TAG = MapWorkoutFragment.class.getName();

    private TextView mDistanceTextView;
    private LinearLayout mLinearLayout;
    // Variables to be saved.
    private static boolean mWorkoutStarted = false;
    public final static String MAP_STATE_ID = "MAP_STATE";
    private static boolean mFindLocationIsBusy = false;
    private final static String LOCATION_BUSY_STATE = "LOCATION_BUSY_STATE";
    private static boolean mStepCountBusy = false;
    private final static String STEP_COUNT_BUSY = "STEP_COUNT_BUSY";
    private static boolean mServiceStarted = false;
    // End
    private static GoogleMap mGoogleMap;
    private MapView mMapView;
    // Variables for maps
    private LocationHelper mLocationHelper;
    private StepCountHelper mStepCountHelper;
    private Location mLocation;
    private WorkoutManager mWorkoutManager;

    public static MapWorkoutFragment newInstance() {
        return new MapWorkoutFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mServiceStarted = false;
        mLocationHelper = new LocationHelper(getActivity());
        mStepCountHelper = new StepCountHelper(getActivity());
        mWorkoutManager = WorkoutManager.sharedInstance();
        UserProfile userProfile = UserManager.getUserPreference(getActivity());
        mWorkoutManager.setUserProfile(userProfile);
        mStepCountHelper.addListener(this);
        mLocationHelper.addListener(this);
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
                mGoogleMap = googleMap;
                if (checkPermission()) {
                    return;
                }
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
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
                // Check if the view is resuming or not
                if(mWorkoutStarted) {
                    startMapView();
                }

            }
        });

        Button mCenterScreen = (Button) rootView.findViewById(R.id.recenter_start);
        mCenterScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = mWorkoutManager.getRoute().size();
                if (size == 0)
                    return;

                LatLng ll = mWorkoutManager.getRoute().get(size - 1);
                zoomCamera(ll);
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
        //initTimerView(mTimeTextView);
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
        if (mButtonWorkout != null)
            if (mWorkoutStarted) {
                mButtonWorkout.setText(getString(R.string.stop));
            } else {
                mButtonWorkout.setText(getString(R.string.start));
            }
        mButtonWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mWorkoutStarted) {

                    mButtonWorkout.setText(getString(R.string.stop));
                    startTimer();
                    startMapView();
                } else {
                    mButtonWorkout.setText(getString(R.string.start));
                    mFindLocationIsBusy = true;
                    stopTimer();
                    mWorkoutManager.pauseStopWatch();
                }
                toggle();
            }
        });
    }

    private void toggle() {
        mWorkoutStarted = !mWorkoutStarted;
    }

    private void setProfileScreenFragment() {
        mLinearLayout.setVisibility(LinearLayout.INVISIBLE);
        mProfileScreenFragment.setListener(this);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, ProfileScreenFragment.newInstance())
                .commit();
    }

    private void startMapView() {
        if (mGoogleMap == null || checkPermission()) return;
        mLocation = mLocationHelper.getLastKnowLocation();
        LatLng ll = getLatLng(mLocation);
        mWorkoutManager.addToRoute(ll);
        zoomCamera(ll);
    }

    private void zoomCamera(LatLng ll) {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, LEVEL_ZOOM));
        mGoogleMap.addMarker(new MarkerOptions()
                .title("You are here")
                .position(ll));
    }

    private void drawRoute(List<LatLng> theRoute) {
        if(mGoogleMap == null){
            Log.i(TAG, "GoogleMap NULL");
            return;
        }
        mGoogleMap.clear();
        PolylineOptions polylineOptions = new PolylineOptions();
        for (LatLng ll : theRoute) {
            polylineOptions.add(ll);
        }
        mGoogleMap.addPolyline(polylineOptions);
        LatLng mLat = theRoute.get(theRoute.size() - 1);
        zoomCamera(mLat);
    }

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
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");
        mMapView.onPause();

        if (mWorkoutStarted && !mServiceStarted) {
            mServiceStarted = true;
            getActivity().startService(WorkoutService.newIntent(getActivity()));
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
        if (mWorkoutStarted && !mServiceStarted) {
            mServiceStarted = true;
            getActivity().startService(WorkoutService.newIntent(getActivity()));
        }
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

        if(mServiceStarted){
            mServiceStarted = false;
        }

        mTimeTextView.setText(mWorkoutManager.getFormattedTime());

        if(mWorkoutStarted) {
          //  startMapView();
            startTimer();
            mLocation = mLocationHelper.getLastKnowLocation();
            compute();
           // drawRoute(mWorkoutManager.getRoute());
            //displayDistance(mWorkoutManager.distance());
        }
       //
        mMapView.onResume();
    }

    @Override
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
    public void onUserSetting(UserSettingFragment userSettingFragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, userSettingFragment)
                .commit();
    }

    @Override
    public void onStep() {
        Log.i(TAG, "onStep()");

        if (mStepCountBusy) return;
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


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void compute() {
        if (checkPermission()) {
            return;
        }
        LatLng to = Utils.getLatLng(mLocation);
        if (mWorkoutManager.computeCurrentDistanceTo(to)) {
            mWorkoutManager.updateUserDistance();
            mHandler.sendEmptyMessage(0); // drawRoute
        }
    }

    private void displayDistance(String distance) {
        if (mDistanceTextView != null) {
            mDistanceTextView.setText(distance);
        }
    }
    private final Handler mHandlerTime = new Handler(){
        public void handleMessage(Message msg){
            mTimeTextView.setText(mWorkoutManager.getFormattedTime());
        }
    };

    private final Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            drawRoute(mWorkoutManager.getRoute());
            displayDistance(mWorkoutManager.distance());
        }
    };

    private boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

}
