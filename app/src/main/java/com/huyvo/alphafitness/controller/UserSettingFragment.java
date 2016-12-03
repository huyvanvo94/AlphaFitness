package com.huyvo.alphafitness.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.huyvo.alphafitness.Login;
import com.huyvo.alphafitness.R;
import com.huyvo.alphafitness.helper.UserManager;
import com.huyvo.alphafitness.model.UserProfile;

import java.util.List;


public class UserSettingFragment extends Fragment implements AdapterView.OnItemSelectedListener{

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

    private Spinner spinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_user_setting, container, false);

        spinner = (Spinner) rootView.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.gender_array, android.R.layout.simple_selectable_list_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        final EditText firstName = (EditText) rootView.findViewById(R.id.edit_text_first_name);
        final EditText lastName = (EditText) rootView.findViewById(R.id.edit_text_last_name);
        final EditText weightText = (EditText) rootView.findViewById(R.id.edit_text_weight);
        Button buttonBack = (Button) rootView.findViewById(R.id.button_back);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.removeUserSetting(UserSettingFragment.this);
                }

                Toast.makeText(getContext(), "Back", Toast.LENGTH_SHORT).show();
            }
        });




        Button buttonDone = (Button) rootView.findViewById(R.id.button_done);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(MapWorkoutFragment.State.workoutStarted){
                    Toast.makeText(getContext(), "Workout in progress", Toast.LENGTH_SHORT).show();
                }

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

        if(gender==null) {
            Toast.makeText(getContext(), "Please select gender", Toast.LENGTH_SHORT).show();
            return;
        }
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

        // Will loop to determine if First Name and Last Name matches.
        for(UserProfile profile: userProfiles){
            if(id.equals(profile.getId())){
                userProfile = profile;
                break;
            }
        }

        if(userProfile==null){
            userProfile = new UserProfile(firstName, lastName, weight, gender);
        }

        Login login = new Login(getContext());
        login.requestLogout();
        login.requestLogin(userProfile);

        Intent i = RecordWorkoutActivity.newIntent(getContext());
        startActivity(i);
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
    public void onDetach(){
        super.onDetach();
        mListener = null;
    }
    String TAG = UserSettingFragment.class.getName();
    String gender = null;
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        gender = (String) adapterView.getItemAtPosition(i);
        spinner.setSelection(i);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        gender = (String) adapterView.getItemAtPosition(0);
        spinner.setSelection(0);
    }

    public interface OnListener{
        void removeUserSetting(UserSettingFragment fragment);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}