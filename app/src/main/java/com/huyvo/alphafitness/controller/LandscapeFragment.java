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
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.huyvo.alphafitness.helper.WorkoutManager;
import com.huyvo.alphafitness.helper.WorkoutService;
import com.huyvo.alphafitness.helper.LocationHelper;
import com.huyvo.alphafitness.helper.StepCountHelper;
import com.huyvo.alphafitness.model.UserProfile;
import com.huyvo.alphafitness.model.Utils;
import com.huyvo.alphafitness.R;

import java.util.List;

import static com.huyvo.alphafitness.model.Utils.getLatLng;

// The controller class to
// implement map view
public class LandscapeFragment extends Fragment
        implements LocationHelper.OnLocationListener,
        StepCountHelper.OnStepCountListener,
        ProfileScreenFragment.OnProfileScreenListener {

    private static boolean serviceStarted = false;
    private final static String TAG = LandscapeFragment.class.getName();

    private ProfileScreenFragment mProfileScreenFragment;
    private TextView mDistanceTextView;
    private LinearLayout mLinearLayout;
    //***********************************************
    private boolean mStarted;
    private boolean mLocationBusy;
    private boolean mOnStepBusy;
    private GoogleMap mMap;

    // Variables for maps
    private LocationHelper locationHelper;
    private StepCountHelper stepCountHelper;
    private Location mLocation;
    private WorkoutManager mWorkoutManager;

    public static LandscapeFragment newInstance() {
        return new LandscapeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationHelper = new LocationHelper(getActivity());
        stepCountHelper = new StepCountHelper(getActivity());

        stepCountHelper.addListener(this);
        locationHelper.addListener(this);

        // Used to stop or start thread
        mLocationBusy = false;
        mOnStepBusy = false;
        // Running started?
        mStarted = false;

        mWorkoutManager = new WorkoutManager(UserProfile.test()[0]);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_landscape, container, false);

        final MapView mMapView = (MapView) rootView.findViewById(R.id.mapView);

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception ignored) {
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        Button button = (Button) rootView.findViewById(R.id.recenter_start);
                        if(button.getVisibility() == View.INVISIBLE) {
                            button.setVisibility(View.VISIBLE);
                        }
                        else{
                            button.setVisibility(View.INVISIBLE);
                        }
                    }
                });

            }
        });

        Button mCenterScreen = (Button) rootView.findViewById(R.id.recenter_start);
        mCenterScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = mWorkoutManager.getRoute().size();
                if(size == 0)
                    return;

                LatLng ll = mWorkoutManager.getRoute().get(size-1);
                zoomCamera(ll);
            }
        });

        Button mButtonStart = (Button) rootView.findViewById(R.id.button_start);
        initWorkoutButton(mButtonStart);

        Button mButtonUserProfile = (Button) rootView.findViewById(R.id.user_profile_button);
        if(mButtonUserProfile !=null) {
            mButtonUserProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setProfileScreenFragment(rootView);
                }
            });
        }

        mLinearLayout = (LinearLayout) rootView.findViewById(R.id.container_2);
        TextView mStopWatchTextView = (TextView) rootView.findViewById(R.id.time_text_view);
        mDistanceTextView = (TextView) rootView.findViewById(R.id.distance_text_view);
        return rootView;
    }

    private void initWorkoutButton(final Button mButtonStart){
        if (mButtonStart != null)
            mButtonStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!mStarted) {
                        mButtonStart.setText(getString(R.string.stop));
                        mStarted = !mStarted;
                        startUI();
                        mWorkoutManager.start();
                        //   mStopWatch.start();

                    } else {
                        mButtonStart.setText(getString(R.string.start));
                        mStarted = !mStarted;
                        mLocationBusy = true;
                        // mStopWatch.reset();
                    }
                }});
    }
    private void setProfileScreenFragment(View view){
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.container_2);
        ll.setVisibility(LinearLayout.INVISIBLE);

        mProfileScreenFragment = ProfileScreenFragment.newInstance();
        mProfileScreenFragment.setUserProfile(mWorkoutManager.getCurrentUser());
        mProfileScreenFragment.setListener(this);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, mProfileScreenFragment)
                .commit();
    }

    private void startUI() {
        if(mMap == null) return;
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLocation = locationHelper.getLastKnowLocation();
        LatLng ll = getLatLng(mLocation);
        mWorkoutManager.addToRoute(ll);
        zoomCamera(ll);
    }


    private void zoomCamera(LatLng ll){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, Utils.LEVEL_ZOOM));
        mMap.addMarker(new MarkerOptions()
                .title("You are here")
                .position(ll));
    }

    private void drawRoute(List<LatLng> theRoute) {
        mMap.clear();
        PolylineOptions polylineOptions = new PolylineOptions();
        for (LatLng ll : theRoute)
            polylineOptions.add(ll);
        mMap.addPolyline(polylineOptions);
        LatLng mLat = theRoute.get(theRoute.size() - 1);
        zoomCamera(mLat);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged()");
        if(!mStarted) return;
        mLocation = location; // get location
        if(mLocationBusy) return;
        mLocationBusy = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                compute();
                mLocationBusy = false;
            }
        }).start();
    }


    @Override
    public void onPause(){

        super.onPause();
    }

    @Override
    public void onDestroy(){
        Log.d(TAG, "onDestroy()");
        if(mStarted && !serviceStarted ){
            serviceStarted = true;
            getActivity().startService(WorkoutService.newIntent(getActivity()));
        }
        locationHelper.removeListener(this);
        stepCountHelper.removeListener(this);
        super.onDestroy();
    }
    @Override
    public void onResume(){
        super.onResume();
        Log.i(TAG, "onResume()");
        locationHelper.addListener(this);
        stepCountHelper.addListener(this);
    }

    @Override
    public void backPressed() {
        Log.i(TAG, "backPressed()");
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .remove(mProfileScreenFragment)
                .commit();

        mLinearLayout.setVisibility(LinearLayout.VISIBLE);
    }

    @Override
    public void onStep() {
        Log.i(TAG, "onStep()");

        if (mOnStepBusy) return;
        mOnStepBusy = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                mWorkoutManager.incStep();
                mWorkoutManager.updateUserStep();
                mOnStepBusy = false;
            }
        }).start();
    }

    private void compute() {

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LatLng to = Utils.getLatLng(mLocation);

        if(mWorkoutManager.computeCurrentDistanceTo(to)){
            mWorkoutManager.updateUserDistance();
            mHandler.sendEmptyMessage(0); // drawRoute
        }
    }

    private void displayDistance(String distance){
        if(mDistanceTextView!=null){
            mDistanceTextView.setText(distance);
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message message){
            drawRoute(mWorkoutManager.getRoute());
            displayDistance(mWorkoutManager.distance());
        }
    };




    public void saveUserSetting(String first, String last){

    }
}
