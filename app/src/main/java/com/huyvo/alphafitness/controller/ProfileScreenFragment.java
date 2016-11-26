package com.huyvo.alphafitness.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.huyvo.alphafitness.R;
import com.huyvo.alphafitness.helper.WorkoutManager;
import com.huyvo.alphafitness.model.UserProfile;


public class ProfileScreenFragment extends Fragment
    implements UserSettingFragment.OnListener{

    public static String ARG_PROFILE_NAME = "None";

    public static String ID = ProfileScreenFragment.class.getName();
    private OnProfileScreenListener mListener;
    private UserProfile mUserProfile;
    private UserSettingFragment mUserSettingFragment;
    private ListView mListView;

    public void setListener(OnProfileScreenListener listener) {
        mListener = listener;
    }

    public static ProfileScreenFragment newInstance() {
        ProfileScreenFragment profileScreenFragment = new ProfileScreenFragment();
        profileScreenFragment.setUserProfile(WorkoutManager.sharedInstance().getCurrentUser());

        return profileScreenFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile_screen, container, false);
        mListView = (ListView) rootView.findViewById(R.id.listView);
        setDisplayUser(mListView);

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
                            mUserSettingFragment = UserSettingFragment.newInstance();
                            mUserSettingFragment.setListener(ProfileScreenFragment.this);
                            mListener.onUserSetting(mUserSettingFragment);
                        }catch (Exception ex){
                            Toast.makeText(getContext(), "ERROR", Toast.LENGTH_LONG).show();
                        }
                    }

                }
            });
        }
        return rootView;
    }



    private void setDisplayUser(ListView listView) {

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_expandable_list_item_1,
                        mUserProfile.getDisplayToListView());

        listView.setAdapter(adapter);

    }

    private void setUserProfile(UserProfile u) {
        mUserProfile = u;
    }

    @Override
    public void onUserSettingBackPressed() {
        if(mListener != null){
            mListener.remove(mUserSettingFragment);
        }
    }

    public interface OnProfileScreenListener {
        void remove(Fragment fragment);
        void onUserSetting(UserSettingFragment fragment);
    }

    private void removeMyself() {
        if(mListener == null) return;
        mListener.remove(this);
    }

}
