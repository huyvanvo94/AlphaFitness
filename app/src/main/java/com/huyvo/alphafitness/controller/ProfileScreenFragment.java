package com.huyvo.alphafitness.controller;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.huyvo.alphafitness.R;
import com.huyvo.alphafitness.helper.Formatter;
import com.huyvo.alphafitness.helper.WorkoutManager;
import com.huyvo.alphafitness.model.UserProfile;

import static com.huyvo.alphafitness.R.id.listView;
import static com.huyvo.alphafitness.helper.WorkoutManager.sharedInstance;


public class ProfileScreenFragment extends Fragment{

    static final String TAG = ProfileScreenFragment.class.getName();

    private OnProfileScreenListener mListener;
    private UserProfile mUserProfile;

    public static ProfileScreenFragment newInstance() {
        ProfileScreenFragment profileScreenFragment = new ProfileScreenFragment();
        profileScreenFragment.setUserProfile(sharedInstance().getCurrentUser());
        return profileScreenFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile_screen, container, false);
        mListView = (ListView) rootView.findViewById(listView);

        final EditText firstName = (EditText) rootView.findViewById(R.id.change_firstname_edit_view);
        final UserProfile userProfile = WorkoutManager.sharedInstance().getCurrentUser();

        if(firstName!=null) {
            firstName.setText(userProfile.getFirstName());
            firstName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text = firstName.getText().toString();

                    if(text.matches(".*\\d+.*")){
                        firstName.setText(userProfile.getFirstName());
                        return;
                    }
                    if(text.length() != 0){
                        userProfile.setFirstName(text);
                    }
                }
            });

        }

        final EditText weight = (EditText) rootView.findViewById(R.id.change_weight_edit_view);

        if(weight!=null){
            weight.setText(Formatter.formatNumber(userProfile.getWeight()));
            weight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text = weight.getText().toString();

                    if(text.length()!=0){
                        float theWeight = Float.valueOf(text);
                        userProfile.setWeight(theWeight);
                    }
                }
            });
        }

        final EditText lastName = (EditText) rootView.findViewById(R.id.change_lastname_edit_view);

        if(lastName!=null) {
            lastName.setText(userProfile.getLastName());
            lastName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text = lastName.getText().toString();

                    if(text.matches(".*\\d+.*")){
                        lastName.setText(userProfile.getLastName());
                        return;
                    }
                    if(text.length() != 0){
                        userProfile.setLastName(text);
                    }
                }
            });

        }

        setDisplayUser();


        Button backButton = (Button) rootView.findViewById(R.id.back_button);
        if (backButton != null) {
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeMyself();
                }
            });
        }

        Button setUserButton = (Button) rootView.findViewById(R.id.button_set_user);
        if (setUserButton != null) {
            setUserButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "Setting User Profile", Toast.LENGTH_SHORT).show();
                    if(mListener != null) {
                        try {
                            mListener.onUserSetting(UserSettingFragment.newInstance());
                        }catch (Exception ignored){

                        }
                    }

                }
            });
        }
        return rootView;
    }

    private String[] arr;
    private ListView mListView;

    private void setDisplayUser() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Formatter formatter = new Formatter(mUserProfile);
                arr = formatter.array();
                mHandler.sendEmptyMessage(0);
            }
        }).start();


    }

    private Handler mHandler = new android.os.Handler() {
        public void handleMessage(Message msg){

            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_expandable_list_item_1,
                            arr);

            mListView.setAdapter(adapter);
        }
    };

    private void setUserProfile(UserProfile u) {
        mUserProfile = u;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            Activity a = getActivity();
            if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    public interface OnProfileScreenListener {
        void remove(Fragment fragment);
        void onUserSetting(UserSettingFragment fragment);
    }

    public void removeMyself() {
        if(mListener == null) return;
        mListener.remove(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a;

        if (context instanceof Activity){
            Log.i(TAG, "onAttach()");
            a = (Activity) context;
            mListener = (OnProfileScreenListener) a;
        }

    }

    @Override
    public void onDetach(){
        super.onDetach();
        mListener = null;
    }
}