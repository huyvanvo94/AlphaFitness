package com.huyvo.alphafitness.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.huyvo.alphafitness.model.UserProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDBHelper extends SQLiteOpenHelper {

    // The version
    private static final int VERSION = 1;
    // Name of the database
    private static final String DATABASE_NAME = "users.db";
    // Name of the table
    private static final String TABLE_NAME = "user";
    // Create the table
    private static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME  +
            "("+
            DatabaseScheme.UserTable.Cols.ID + " TEXT,"+
            DatabaseScheme.UserTable.Cols.FIRST_NAME + " TEXT," +
            DatabaseScheme.UserTable.Cols.LAST_NAME + " TEXT," +
            DatabaseScheme.UserTable.Cols.WEIGHT + " REAL," +
            DatabaseScheme.UserTable.Cols.TOTAL_DISTANCE + " REAL,"+
            DatabaseScheme.UserTable.Cols.TOTAL_CALORIES_BURNED + " REAL,"+
            DatabaseScheme.UserTable.Cols.TOTAL_WORKOUT_COUNT + " INT,"+
            DatabaseScheme.UserTable.Cols.WEEK + " TEXT" +
            ")";

    // Delete the table
    private static final String SQL_DROP = "DROP TABLE IF EXISTS " +
            DatabaseScheme.UserTable.NAME;



    public UserDBHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DROP);
        onCreate(db);
    }

    public int delete(UUID id){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME,
                DatabaseScheme.UserTable.Cols.ID + " =? ",
                new String[]{id.toString()});
    }

    public static ContentValues getContentValues(UserProfile userProfile){
        ContentValues values = new ContentValues();

        values.put(DatabaseScheme.UserTable.Cols.ID, userProfile.getId().toString());
        values.put(DatabaseScheme.UserTable.Cols.FIRST_NAME, userProfile.getFirstName());
        values.put(DatabaseScheme.UserTable.Cols.LAST_NAME, userProfile.getLastName());
        values.put(DatabaseScheme.UserTable.Cols.WEIGHT, userProfile.getWeight());
        values.put(DatabaseScheme.UserTable.Cols.TOTAL_DISTANCE, userProfile.getTotalDistance());
        values.put(DatabaseScheme.UserTable.Cols.TOTAL_CALORIES_BURNED, userProfile.getTotalCalories());
        values.put(DatabaseScheme.UserTable.Cols.TOTAL_WORKOUT_COUNT, userProfile.getTotalWorkoutCount());

        values.put(DatabaseScheme.UserTable.Cols.WEEK, userProfile.getWeekStr());
        return values;
    }

    // Get users
    public List<UserProfile> getUsers(){
        List<UserProfile> up = new ArrayList<>();
        UserProfileCursorWrapper cursor = queryUserProfiles(null, null);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                up.add(cursor.getUserProfile());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return up;
    }

    public void insertUserProfile( UserProfile userprofile){
        ContentValues values = getContentValues(userprofile);
        getWritableDatabase().insert(TABLE_NAME, null, values);

    }


    public void updateUserProfile(UserProfile userProfile){
        SQLiteDatabase db = getWritableDatabase();

        String id = userProfile.getId().toString();

        ContentValues contentValues = getContentValues(userProfile);

        db.update(TABLE_NAME, contentValues,
                DatabaseScheme.UserTable.Cols.ID + " =? ",
                new String[]{ id });
    }

    public UserProfile getUserProfile(UUID id){
        SQLiteDatabase db = getReadableDatabase();

        Cursor res = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE "
                        + DatabaseScheme.UserTable.Cols.ID +"=?",
                new String[]{id.toString()});

        res.close();

        return new UserProfileCursorWrapper(res).getUserProfile();
    }

    private UserProfileCursorWrapper queryUserProfiles(String whereClause, String[] whereArgs){
        Cursor cursor = getWritableDatabase().query(
                DatabaseScheme.UserTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new UserProfileCursorWrapper(cursor);
    }

}
