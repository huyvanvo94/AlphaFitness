package com.huyvo.alphafitness.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.huyvo.alphafitness.R;
import com.huyvo.alphafitness.helper.UserManager;
import com.huyvo.alphafitness.helper.WorkoutManager;
import com.huyvo.alphafitness.model.UserProfile;

import java.util.List;


public class UserSettingFragment extends Fragment {

    private OnListener mListener;

    public UserSettingFragment() {
        // Required empty public constructor
    }


    public static UserSettingFragment newInstance() {
        UserSettingFragment fragment = new UserSettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_user_setting, container, false);

        final EditText firstName = (EditText) rootView.findViewById(R.id.edit_text_first_name);
        final EditText lastName = (EditText) rootView.findViewById(R.id.edit_text_last_name);
        final EditText weightText = (EditText) rootView.findViewById(R.id.edit_text_weight);
        Button buttonBack = (Button) rootView.findViewById(R.id.button_back);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.onUserSettingBackPressed();
                }

                Toast.makeText(getContext(), "Back", Toast.LENGTH_SHORT).show();


            }
        });



        Button buttonDone = (Button) rootView.findViewById(R.id.button_done);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String first = firstName.getText().toString();

                if(first.length() == 0){
                    Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
                    return;
                }

                String last = lastName.getText().toString();

                if(last.length() == 0){
                    Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(weightText.getText().toString().length()==0){
                    Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
                    return;
                }

                float weight = Float.valueOf(weightText.getText().toString());
                setUser(first, last, weight);
            }
        });

        return rootView;
    }

    private void setUser(String firstName, String lastName, float weight){
        firstName = firstName.trim();
        lastName = lastName.trim();

        firstName = firstName.toLowerCase();
        lastName = lastName.toLowerCase();

        firstName = firstName.substring(0,1).toUpperCase()+firstName.substring(1);
        lastName = lastName.substring(0,1).toUpperCase()+lastName.substring(1);

        UserManager userManager = new UserManager(getContext());

        List<UserProfile> userProfiles = userManager.getUsers();
        String id = firstName+lastName;

        UserProfile userProfile = null;

        for(UserProfile profile: userProfiles){
            if(id.equals(profile.getId())){
                userProfile = profile;
                break;
            }
        }

        if(userProfile==null){
            userProfile = new UserProfile(firstName, lastName, weight);
        }

        WorkoutManager.sharedInstance().setUserProfile(userProfile);
        WorkoutManager.sharedInstance().reset();

        UserManager.saveUserPreference(getContext(), userProfile);

        if(mListener!= null) {
            mListener.onUserSettingBackPressed();
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof AppCompatActivity){
//            mListener = (OnListener) context;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();


    }

    public interface OnListener{
        void onUserSettingBackPressed();
    }

    public void setListener(OnListener listener){
        mListener = listener;
    }

}
