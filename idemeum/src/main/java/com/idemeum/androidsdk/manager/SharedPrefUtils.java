package com.idemeum.androidsdk.manager;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.idemeum.androidsdk.R;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Created by Meet Parabiya on 10/17/2020.
 *
 * This is view_play_screen_rating utility class to interact with shared preference data.
 *
 */

public class SharedPrefUtils {

    /**
     * This method set boolean value in shared prefrence.
     *
     * @param context - Current context
     * @param key     - String representation the key
     * @param value   - boolean representing the default value
     */
    static SharedPreferences sharedPreferences = null;

    static SharedPreferences getInstance(Context mContext) {
        try {
            MasterKey masterKey = new MasterKey.Builder(mContext, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            if (sharedPreferences == null)
                sharedPreferences = EncryptedSharedPreferences.create(
                        mContext,
                        mContext.getString(R.string.filename),
                        masterKey,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return sharedPreferences;
    }

    public static void setSharedPrefBoolean(Context context, final String key, final boolean value) {
//        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences settings = getInstance(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }


    /**
     * Returns String value as per prefrence key passed.
     *
     * @param context      - Current context
     * @param key          - String representation the key
     * @param defaultValue - boolean representing the default value
     * @return boolean
     */

    public static boolean getSharedPrefBoolean(Context context, final String key, final boolean defaultValue) {
//        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences settings = getInstance(context);
        return settings.getBoolean(key, defaultValue);
    }


    /**
     * This method set String value in shared prefrence.
     *
     * @param context - Current context
     * @param key     - String representation the key
     * @param value   -String representing the default value
     */
    public static void setSharedPrefString(Context context, final String key, final String value) {
//        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences settings = getInstance(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
        editor.apply();
    }

    /**
     * Returns String value as per prefrence key passed.
     *
     * @param context      - Current context
     * @param key          - String representation the key
     * @param defaultValue -String representing the default value
     * @return String
     */
    public static String getSharedPrefString(Context context, final String key, final String defaultValue) {
//        SharedPreferences token = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences token = getInstance(context);
        if (token == null) {
            return "";
        }
        return token.getString(key, defaultValue);
    }

    /**
     * This method set Integer value in shared prefrence.
     *
     * @param context - Current context
     * @param key     - String representation the key
     * @param value   - Integer representing the default value
     */
    public static void setSharedPrefInteger(Context context, final String key, final int value) {
//        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences settings = getInstance(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * Returns Integer value as per prefrence key passed.
     *
     * @param context      - Current context
     * @param key          - String representation the key
     * @param defaultValue - Integer representing the default value
     * @return int
     */
    public static int getSharedPrefInteger(Context context, final String key, final int defaultValue) {
//        SharedPreferences token = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences token = getInstance(context);
        return token.getInt(key, defaultValue);
    }

    /**
     * This method set Integer value in shared prefrence.
     *
     * @param context - Current context
     * @param key     - String representation the key
     * @param value   - Long representing the default value
     */
    public static void setSharedPrefLong(Context context, final String key, final long value) {
//        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences settings = getInstance(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * Returns Integer value as per prefrence key passed.
     *
     * @param context      - Current context
     * @param key          - String representation the key
     * @param defaultValue - Long representing the default value
     * @return int
     */
    public static long getSharedPrefLong(Context context, final String key, final long defaultValue) {
//        SharedPreferences token = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences token = getInstance(context);
        return token.getLong(key, defaultValue);
    }

    public static void removeCookie(Context context, final String key) {
//        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences settings = getInstance(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(key);
        editor.apply();
    }

    public static void clear(Context context) {
        SharedPreferences preferences = getInstance(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
