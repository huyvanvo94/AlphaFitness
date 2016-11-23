package com.huyvo.alphafitness.helper;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import com.huyvo.alphafitness.data.DatabaseScheme;
import com.huyvo.alphafitness.data.UserProfileContentProvider;
import com.huyvo.alphafitness.model.UserProfile;
import com.huyvo.alphafitness.model.Week;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Wrapper class to update
//
//
//
public class UserManager {

    public final static String PREFERENCE_NAME = "com.huyvo.perfence_user";


    private static String TAG = UserManager.class.getName();
    private Context mContext;

    public UserManager(Context context){
        mContext = context;
    }

    public void deleteUser(UUID uuid){
        mContext.getContentResolver().delete(UserProfileContentProvider.CONTENT_URI, null, null);
    }

    public void addUser(UserProfile profile){
        ContentValues values = UserProfileContentProvider.getContentValues(profile);
        mContext.getContentResolver().insert(UserProfileContentProvider.CONTENT_URI, values);
    }


    public void updateUser(UserProfile userProfile){
        String id = userProfile.getId().toString();
        mContext.getContentResolver().update(UserProfileContentProvider.CONTENT_URI,
                UserProfileContentProvider.getContentValues(userProfile), id, null);
    }

    public List<UserProfile> getUsers(){
        List<UserProfile> userProfiles = new ArrayList<>();

        Uri uri = UserProfileContentProvider.CONTENT_URI;
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null, null);

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                UserProfile userProfile = makeUserProfile(cursor);
                userProfiles.add(userProfile);
                cursor.moveToNext();
            }

        }finally {
            cursor.close();
        }

        // UserProfile userProfile = UserProfileProvider.getUserProfile(cursor);
        //Log.i(TAG, userProfile.toString());

        return userProfiles;

    }



    public void savePreference(String first, String last){
        SharedPreferences.Editor editor =
                mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
                        .edit();

        editor.putString(DatabaseScheme.UserTable.Cols.FIRST_NAME, first);
        editor.putString(DatabaseScheme.UserTable.Cols.LAST_NAME, last);
        editor.commit();
    }

    public String[] getPreference(){
        SharedPreferences sharedPreferences =
                mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        String firstname = sharedPreferences.getString(DatabaseScheme.UserTable.Cols.FIRST_NAME, null);
        String lastname = sharedPreferences.getString(DatabaseScheme.UserTable.Cols.LAST_NAME, null);

        return new String[]{firstname, lastname};
    }

    /**
     * TODO:
     * Look through database
     * find user from getPreference().
     */
    public static UserProfile getUser(){
        return null;
    }

    static UserProfile makeUserProfile(Cursor cursor){

        String uuid = cursor.getString(cursor.getColumnIndex(DatabaseScheme.UserTable.Cols.ID));

        float totalDistance = cursor.getFloat(cursor.getColumnIndex(DatabaseScheme.UserTable.Cols.TOTAL_DISTANCE));
        float totalCalories = cursor.getFloat(cursor.getColumnIndex(DatabaseScheme.UserTable.Cols.TOTAL_CALORIES_BURNED));
        int totalWorkoutCount = cursor.getInt(cursor.getColumnIndex(DatabaseScheme.UserTable.Cols.TOTAL_CALORIES_BURNED));

        String firstName = cursor.getString(cursor.getColumnIndex(DatabaseScheme.UserTable.Cols.FIRST_NAME));
        String lastName = cursor.getString(cursor.getColumnIndex(DatabaseScheme.UserTable.Cols.LAST_NAME));
        float weight = cursor.getFloat(cursor.getColumnIndex(DatabaseScheme.UserTable.Cols.WEIGHT));

        Week week = Week.newInstance(cursor.getString(cursor.getColumnIndex(DatabaseScheme.UserTable.Cols.WEEK)));

        return makeUserProfile(
                uuid,
                totalDistance,
                totalCalories,
                totalWorkoutCount,
                firstName,
                lastName,
                weight,
                week);
    }

    static UserProfile makeUserProfile(String uuid, float totalDistance,
                                       float totalCalories, int totalWorkoutCount,
                                       String firstName, String lastName,
                                       float weight, Week week) {

        UserProfile userProfile = new UserProfile(firstName,
                lastName, weight,
                totalDistance, totalCalories,
                totalWorkoutCount);

        userProfile.setWeek(week);
        userProfile.setId(uuid);
        return userProfile;
    }
}
