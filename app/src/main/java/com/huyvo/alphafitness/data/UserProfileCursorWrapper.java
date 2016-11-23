package com.huyvo.alphafitness.data;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;
import com.huyvo.alphafitness.model.UserProfile;
import com.huyvo.alphafitness.model.Week;

public class UserProfileCursorWrapper extends CursorWrapper {

    public UserProfileCursorWrapper( Cursor cursor ){
        super( cursor );
    }

    public UserProfile getUserProfile(){
        //--------------------------------------------------------------------------
        String uuid = getString(getColumnIndex(DatabaseScheme.UserTable.Cols.ID));
        Log.i("UUID", uuid);

        float totalDistance = getFloat(getColumnIndex(DatabaseScheme.UserTable.Cols.TOTAL_DISTANCE));
        float totalCalories = getFloat(getColumnIndex(DatabaseScheme.UserTable.Cols.TOTAL_CALORIES_BURNED));
        int totalWorkoutCount = getInt(getColumnIndex(DatabaseScheme.UserTable.Cols.TOTAL_CALORIES_BURNED));

        String firstName = getString(getColumnIndex(DatabaseScheme.UserTable.Cols.FIRST_NAME));
        String lastName = getString(getColumnIndex(DatabaseScheme.UserTable.Cols.LAST_NAME));
        float weight = getFloat(getColumnIndex(DatabaseScheme.UserTable.Cols.WEIGHT));

        // Test
        Week week = Week.newInstance( getString(getColumnIndex(DatabaseScheme.UserTable.Cols.WEEK)));
        //--------------------------------------------------------------------------

        UserProfile userProfile = new UserProfile(uuid);

        userProfile.setTotalCalories(totalCalories);
        userProfile.setTotalDistance(totalDistance);
        userProfile.setTotalWorkoutCount(totalWorkoutCount);

        userProfile.setWeight(weight);
        userProfile.setFirstName(firstName);
        userProfile.setLastName(lastName);

        // TO DO: Test
        userProfile.setWeek(week);

        return userProfile;
    }
}