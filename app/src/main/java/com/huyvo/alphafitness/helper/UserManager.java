package com.huyvo.alphafitness.helper;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;

import com.huyvo.alphafitness.data.DatabaseScheme;
import com.huyvo.alphafitness.data.UserProfileContentProvider;
import com.huyvo.alphafitness.model.UserProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * A class use to manage SQLite
 */
public class UserManager {

    private static String TAG = UserManager.class.getName();
    public final static String PREFERENCE_NAME = "com.huyvo.perfence_user";

    private Context mContext;

    public UserManager(Context context){
        mContext = context;
    }

    public void deleteUser(String id){
        mContext.getContentResolver().delete(UserProfileContentProvider.CONTENT_URI, null, null);
    }

    public void addUser(UserProfile profile){
        ContentValues values = UserProfileContentProvider.getContentValues(profile);
        mContext.getContentResolver().insert(UserProfileContentProvider.CONTENT_URI, values);
    }

    public void addUsers(List<UserProfile> list){
        for(UserProfile userProfile: list){
            addUser(userProfile);
        }
    }

    public void safelyAddUser(UserProfile userProfile){
        List<UserProfile> list = getUsers();
        for(UserProfile users: list){
            if(userProfile.getId().equals(users.getId())){
                updateUser(userProfile);
                return;
            }
        }

        addUser(userProfile);
    }

    public void deleteUsers(List<UserProfile> userProfiles){
        for(UserProfile userProfile: userProfiles){
            deleteUser(userProfile.getId());
        }
    }

    public void updateUsers(List<UserProfile> userProfiles){
        for(UserProfile userProfile: userProfiles){
            updateUser(userProfile);
        }
    }

    public void updateUser(UserProfile userProfile){
        String id = userProfile.getId();
        mContext.getContentResolver().update(UserProfileContentProvider.CONTENT_URI,
                UserProfileContentProvider.getContentValues(userProfile), id, null);
    }

    public List<UserProfile> getUsers(){
        List<UserProfile> userProfiles = new ArrayList<>();

        Uri uri = UserProfileContentProvider.CONTENT_URI;
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null, null);

        try{
            assert cursor != null;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                UserProfile userProfile = makeUserProfile(cursor);
                userProfiles.add(userProfile);
                cursor.moveToNext();
            }

        }finally {
            assert cursor != null;
            cursor.close();
        }

        return userProfiles;

    }

    /**
     * TODO:
     * Look through database
     * find user from getPreference().
     */
    public static UserProfile getUser(){
        return null;
    }

    public static UserProfile makeUserProfile(Cursor cursor){

        String uuid = cursor.getString(cursor.getColumnIndex(DatabaseScheme.UserTable.Cols.ID));

        String gson = cursor.getString(cursor.getColumnIndex(DatabaseScheme.UserTable.Cols.GSON));

        return makeUserProfile(
                uuid,
                gson);
    }

    public static UserProfile makeUserProfile(String uuid, String gson) {
        UserProfile userProfile = UserProfile.newInstance(gson);
        userProfile.setId(uuid);
        return userProfile;
    }

    private static final String USER_PREF = "com.huyvo.alphafitness.userpref";

    public static void saveUserPreference(Context context, UserProfile userProfile){
        SharedPreferences mPrefs = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        String json = userProfile.getGson();
        prefsEditor.putString(USER_PREF, json);
        prefsEditor.apply();
    }

    public static UserProfile getUserPreference(Context context){

        SharedPreferences mPrefs = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        String json = mPrefs.getString(USER_PREF, "");
        return UserProfile.newInstance(json);

    }
}


