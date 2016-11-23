package com.huyvo.alphafitness.data;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
import com.huyvo.alphafitness.model.UserProfile;

import java.util.HashMap;

public class UserProfileContentProvider extends ContentProvider {
    private static final String TAG = UserProfileContentProvider.class.getName();

    private static final String TABLE_NAME =  DatabaseScheme.UserTable.NAME;
    public static final String PROVIDER_NAME = "com.huyvo.userprofilecontentprovider";
    private static final String URL = "content://" + PROVIDER_NAME;
    public static final Uri CONTENT_URI = Uri.parse(URL);
    // The helper
    private UserDBHelper dbHelper;
    // Defines the database name
    private static final String DBNAME = DatabaseScheme.UserTable.NAME;
    // Defines the database name
    private SQLiteDatabase db;
    // URICODE
    private static final int uriCode = 1;

    // private static final UriMatcher sUriMatcher;

    private static HashMap<String, String> values;
    private static UriMatcher uriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "cte", uriCode);
        uriMatcher.addURI(PROVIDER_NAME, "cte/*", uriCode);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new UserDBHelper(context);
        db = dbHelper.getWritableDatabase();
        return db != null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values){
        Log.i(TAG, "insert()");
        long rowID = db.insert(TABLE_NAME, "", values);
        if(rowID>0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);

            return _uri;
        }


        return null;
    }
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);

        Cursor cursor = db.query(TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int count = db.delete(TABLE_NAME, selection, selectionArgs);

        return count;
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

    public static UserProfile getUserProfile(Cursor cursor){
        UserProfileCursorWrapper wrapper = new UserProfileCursorWrapper(cursor);
        UserProfile userProfile = wrapper.getUserProfile();
        wrapper.close();
        return userProfile;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {

        db.update(TABLE_NAME, contentValues,
                DatabaseScheme.UserTable.Cols.ID + " =? ",
                new String[]{ s });
        return 0;
    }
}
