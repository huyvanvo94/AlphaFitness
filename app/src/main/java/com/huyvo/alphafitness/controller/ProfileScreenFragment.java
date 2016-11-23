package com.huyvo.alphafitness.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.huyvo.alphafitness.model.UserProfile;
import com.huyvo.alphafitness.R;


public class ProfileScreenFragment extends Fragment {
    public static String ID = ProfileScreenFragment.class.getName();
    private OnProfileScreenListener mListener;
    private UserProfile mUserProfile;

    public void setListener(OnProfileScreenListener listener){
        mListener = listener;
    }

    public static ProfileScreenFragment newInstance() {
        return new ProfileScreenFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile_screen, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        // Test
        testListView(listView);

        Button backButton = (Button) rootView.findViewById(R.id.back_button);
        if(backButton!= null){
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if(mListener!=null)
                       mListener.backPressed();

                }
            });
        }

        return rootView;
    }

    private void testListView(ListView listView){

        ArrayAdapter<String> myarrayAdapter =
                new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_expandable_list_item_1,
                        mUserProfile.getDisplayToListView());

        listView.setAdapter(myarrayAdapter);


    }

    public void setUserProfile(UserProfile u){
        mUserProfile = u;
    }

    public interface OnProfileScreenListener{
        void backPressed();
    }

}
