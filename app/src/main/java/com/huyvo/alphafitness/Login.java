package com.huyvo.alphafitness;

import android.content.Context;
import com.huyvo.alphafitness.helper.UserManager;
import com.huyvo.alphafitness.helper.WorkoutManager;
import com.huyvo.alphafitness.model.UserProfile;

public class Login {
    private static boolean currentLogin = false;
    private Context mContext;
    private UserManager mUserManager;
    public Login(Context context){
        mContext = context;
        mUserManager = new UserManager(mContext);
    }

    /**
     *
     * @return UserProfile from SharedPreference
     */

    public boolean requestLogin(){

        return requestLogin(UserManager.getUserPreference(mContext));
    }

    public boolean requestLogin(UserProfile userProfile){
        if(currentLogin){
            return false;
        }
        WorkoutManager.sharedInstance(userProfile);
        currentLogin = true;
        return true;

    }

    public boolean requestLogout(){
        if(!currentLogin){
            return false;
        }

        currentLogin = false;
        return true;
    }
}
