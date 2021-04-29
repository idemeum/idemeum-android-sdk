package com.idemeum.androidsdk.manager;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.idemeum.androidsdk.models.OIDCToken;

/**
 * Created by Meet Parabiya on 10/17/2020.
 * <p>
 * Utility class for SharedPreference
 */
public class SharedPrefManager {


    private final String Pref_Key_Token = "Token";


    private static SharedPrefManager instance;

    private SharedPrefManager() {
        // hidden constructor
    }

    public static SharedPrefManager getInstance() {
        return instance = new SharedPrefManager();
    }

    public OIDCToken getToken(Context mContext) {
        String mToken = SharedPrefUtils.getSharedPrefString(mContext, Pref_Key_Token, "");
        if (TextUtils.isEmpty(mToken)) {
            return null;

        } else {
            return new Gson().fromJson(mToken, OIDCToken.class);
        }
    }

    public void setToken(Context mContext, OIDCToken token) {
        SharedPrefUtils.setSharedPrefString(mContext, Pref_Key_Token, new Gson().toJson(token));
    }


}
