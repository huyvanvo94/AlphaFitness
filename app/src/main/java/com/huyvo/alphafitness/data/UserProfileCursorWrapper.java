package com.huyvo.alphafitness.data;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.google.gson.Gson;
import com.huyvo.alphafitness.model.UserProfile;

public class UserProfileCursorWrapper extends CursorWrapper {

    public UserProfileCursorWrapper( Cursor cursor ){
        super( cursor );
    }
    public UserProfile getUserProfile(){
        String uuid = getString(getColumnIndex(DatabaseScheme.UserTable.Cols.ID));
        Log.i("UUID", uuid);
        String gsonUserProfile = getString(getColumnIndex(DatabaseScheme.UserTable.Cols.GSON));
        return new Gson().fromJson(gsonUserProfile, UserProfile.class);
    }
}