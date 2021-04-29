package com.idemeum.androidsdk.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.google.androidbrowserhelper.trusted.TwaLauncher;
import com.google.gson.Gson;
import com.idemeum.androidsdk.models.Error;
import com.idemeum.androidsdk.manager.IdemeumManager;
import com.idemeum.androidsdk.models.IdemeumSigninResponse;
import com.idemeum.androidsdk.manager.SharedPrefManager;

import java.net.URLDecoder;

public class IdemeumActivity extends Activity {
    static String KEY_AUTHORIZATION_STARTED = "KEY_AUTHORIZATION_STARTED";
    public static String HANDLE_RESPONSE = "HANDLE_RESPONSE";
    static String KEY_TWA_OPENED = "KEY_TWA_OPENED";

    private boolean mAuthorizationStarted = false;
    private boolean twaOpened = false;
    private TwaLauncher mTWALauncher;

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);

        // on first open, set authorization started as false
        if (savedInstanceBundle != null) {
            mAuthorizationStarted = savedInstanceBundle.getBoolean(KEY_AUTHORIZATION_STARTED, false);
        }

        Log.i("IDEMEUM_IDENTITY", "onCreate handleIntent");
        handleIntent(getIntent());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i("onSaveInstanceState", outState.toString());
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_AUTHORIZATION_STARTED, mAuthorizationStarted);
        outState.putBoolean(KEY_TWA_OPENED, twaOpened);
    }


    @Override
    protected void onResume() {
        super.onResume();

        Log.i("IDEMEUM_IDENTITY", "onResume handleIntent");
        handleIntent(getIntent());
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    public void handleIntent(Intent intent) {
        boolean openTwa = intent.getBooleanExtra(IdemeumManager.OPEN_TWA, false);
        boolean handleResponse = intent.getBooleanExtra(HANDLE_RESPONSE, false);

        Log.i("IDEMEUM_IDENTITY", "handleIntent, openTwa: " + openTwa);
        Log.i("IDEMEUM_IDENTITY", "handleIntent, handleResponse: " + handleResponse);
        Log.i("IDEMEUM_IDENTITY", "handleIntent, mAuthorizationStarted: " + mAuthorizationStarted);
        Log.i("IDEMEUM_IDENTITY", "handleIntent, twaOpened: " + twaOpened);


        // if open twa is true and authorization hasn't started, launch TWA
        if (openTwa && !mAuthorizationStarted) {
            // launch TWA
            String url = intent.getStringExtra(IdemeumManager.TWA_URL);
            mTWALauncher = new TwaLauncher(this, "com.android.chrome");
            mTWALauncher.launch(Uri.parse(url));
            Log.i("IDEMEUM_IDENTITY", "handleIntent, Launched TWA" + url);
            mAuthorizationStarted = true;
            return;
        }

        // if handleResponse requested, then this must come from redirect page
        if (handleResponse) {
            // handle the response from verification request
            Log.i("IDEMEUM_IDENTITY", "handleIntent, start handleResponse");
            handleResponse(this, intent);
            return;
        }

        // if openTwa is true, mAuthorizationStarted has started, but not twaOpened
        // this must be onResume when the WebView is open
        // so set twaOpened = true
        if (openTwa && mAuthorizationStarted && !twaOpened) {
            twaOpened = true;
            Log.i("IDEMEUM_IDENTITY", "handleIntent, openTwa && mAuthorizationStarted && !twaOpened -> set twaOpened true");
            return;
        }

        // if openTwa is true, mAuthorizationStarted has started, and twaOpened is true
        // this must be when the user canceled the WebView
        // so finish the activity
        if (openTwa && mAuthorizationStarted && twaOpened) {
            Log.i("IDEMEUM_IDENTITY", "handleIntent, openTwa && mAuthorizationStarted && twaOpened -> finish");
            finish();
        }
    }

    public void handleResponse(Context ctx, Intent intent) {


        Uri appLinkData = getIntent().getData();
        try {
            String response = URLDecoder.decode(appLinkData.getQueryParameter("response"), "UTF-8");

            byte[] data = Base64.decode(response, Base64.DEFAULT);
            String text = new String(data, "UTF-8");
            IdemeumSigninResponse mIdemeumSigninResponse = new Gson().fromJson(text, IdemeumSigninResponse.class);

            if (mIdemeumSigninResponse != null) {
//                Toast.makeText(this, URLDecoder.decode(appLinkData.toString(), "UTF-8"), Toast.LENGTH_SHORT).show();
                if (mIdemeumSigninResponse.getStatus()) {
                    //status true
                    if (TextUtils.isEmpty(mIdemeumSigninResponse.getToken().getIdToken()) || TextUtils.isEmpty(mIdemeumSigninResponse.getToken().getAccessToken())) {
                        //token is null
                        IdemeumManager.getInstance(IdemeumManager.clientID).sendCallBack(false, null, Error.TOKEN_ERROR);
                    } else {
                        //token received
                        SharedPrefManager.getInstance().setToken(this, mIdemeumSigninResponse.getToken());
                        IdemeumManager.getInstance(IdemeumManager.clientID).sendCallBack(true, mIdemeumSigninResponse, null);
                    }


                } else {
                    //status false
                    if (!TextUtils.isEmpty(mIdemeumSigninResponse.getMessage()))
                        //error msg received
                        IdemeumManager.getInstance(IdemeumManager.clientID).
                                sendCallBack(false, mIdemeumSigninResponse, Error.UNKNOWN_ERROR);
                    else
                        //error msg not received
                        IdemeumManager.getInstance(IdemeumManager.clientID).
                                sendCallBack(false, null, Error.UNKNOWN_ERROR);
                }

            } else {
                Log.e("App Link data", "null");
                IdemeumManager.getInstance(IdemeumManager.clientID).
                        sendCallBack(false, null, Error.OPERATION_CANCELLED);
            }
            this.finish();
        } catch (Exception e) {
            e.printStackTrace();
            IdemeumManager.getInstance(IdemeumManager.clientID).
                    sendCallBack(false, null, Error.UNKNOWN_ERROR);
            this.finish();

        }
        this.finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTWALauncher != null) {
            mTWALauncher.destroy();
        }
        IdemeumManager.getInstance(IdemeumManager.clientID).sendCallBack(false, null, Error.OPERATION_CANCELLED);
    }
}
