package com.idemeum.androidsdk.manager;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.idemeum.androidsdk.listeners.IdemeumSigninListener;
import com.idemeum.androidsdk.listeners.IsLoggedInListener;
import com.idemeum.androidsdk.listeners.TokenValidationListener;
import com.idemeum.androidsdk.models.Error;
import com.idemeum.androidsdk.models.IdemeumSigninResponse;
import com.idemeum.androidsdk.models.OIDCToken;
import com.idemeum.androidsdk.ui.IdemeumActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class IdemeumManager {

    public static String OPEN_TWA = "OPEN_TWA";
    public static String TWA_URL = "TWA_URL";

    public static String clientID;
    public static String JS_URL = "https://ciam.idemeum.com/api/consumer/authorize?clientId=%s";

    private static IdemeumManager sInstance;

    IdemeumSigninListener mIdemeumSigninListener;
    boolean mCallbackSent = false;

    public static synchronized IdemeumManager getInstance(String clientID) {
        if (sInstance == null) {
            sInstance = new IdemeumManager(clientID);
        }
        IdemeumManager.clientID = clientID;
        return sInstance;
    }

    public IdemeumManager(String clientID) {
        this.clientID = clientID;
    }


    public void sendCallBack(boolean isSuccess, IdemeumSigninResponse mIdemeumSigninResponse, Error errorMsg) {
        if (this.mIdemeumSigninListener != null) {
            if (!mCallbackSent) {
                mCallbackSent = true;
                if (isSuccess)
                    mIdemeumSigninListener.onSuccess(mIdemeumSigninResponse);
                else {
                    if (mIdemeumSigninResponse != null
                            && mIdemeumSigninResponse.getStatusCode() != 0
                            && !mIdemeumSigninResponse.getMessage().isEmpty())
                        mIdemeumSigninListener.onError(mIdemeumSigninResponse.getStatusCode(),
                                mIdemeumSigninResponse.getMessage());
                    else
                        mIdemeumSigninListener.onError(errorMsg.getCode(), errorMsg.getDescription());

                }
            }
        }
    }

    public Intent createResponseHandlingIntent(Context context, Uri responseUri) {
        Log.i("createResponseIntent", responseUri.toString());

        // Creating an intent of the IdentityActivity to handle the uri from redirect
        Intent intent = new Intent(context, IdemeumActivity.class);
        intent.setData(responseUri);
        intent.putExtra(IdemeumManager.OPEN_TWA, false);
        intent.putExtra(IdemeumActivity.HANDLE_RESPONSE, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }


    public void login(Context context, IdemeumSigninListener mIdemeumSigninListener) {
        this.mIdemeumSigninListener = mIdemeumSigninListener;
        mCallbackSent = false;
        Intent idemeumActivity = new Intent(context, IdemeumActivity.class);
        idemeumActivity.putExtra(OPEN_TWA, true);
        idemeumActivity.putExtra(TWA_URL, String.format(JS_URL, clientID));
        context.startActivity(idemeumActivity);
    }

    public void userClaims(Context context, TokenValidationListener mOnTokenValidationListener) {

        OIDCToken oidcToken = SharedPrefManager.getInstance().getToken(context);
        if (oidcToken == null) {
            mOnTokenValidationListener.onError(Error.OIDC_TOKEN_EMPTY.getCode(),
                    Error.OIDC_TOKEN_EMPTY.getDescription());
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://ciam.idemeum.com/api/consumer/token/validation";

        if (!isInternetOn(context)) {
            mOnTokenValidationListener.onError(Error.NO_INTERNET_CONNECTION.getCode(),
                    Error.NO_INTERNET_CONNECTION.getDescription());
        }


        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST, url, oidcToken.toJSON(),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            mOnTokenValidationListener.onSuccess(response, "Success");
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            mOnTokenValidationListener.onError(Error.UNKNOWN_ERROR.getCode(),
                                    Error.UNKNOWN_ERROR.getDescription());
                        }
                    });
            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void logout(Context context) {
        SharedPrefUtils.clear(context);
    }

    public void isLoggedIn(Context context, IsLoggedInListener mIsLoggedInListener) {
        OIDCToken oidcToken = SharedPrefManager.getInstance().getToken(context);
        if (oidcToken == null) {
            mIsLoggedInListener.isLoggedIn(false);
        } else {

            RequestQueue queue = Volley.newRequestQueue(context);
            String url = "https://ciam.idemeum.com/api/consumer/token/validation/accessToken";

            if (!isInternetOn(context)) {
                mIsLoggedInListener.isLoggedIn(false);
            }

            try {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST, url, oidcToken.toJSON(),
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                mIsLoggedInListener.isLoggedIn(true);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                mIsLoggedInListener.isLoggedIn(false);
                            }
                        });
                queue.add(jsonObjectRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public static boolean isInternetOn(Context application) {
        ConnectivityManager connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network nw = connectivityManager.getActiveNetwork();
        if (nw == null) return false;
        NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
        return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
    }
}
