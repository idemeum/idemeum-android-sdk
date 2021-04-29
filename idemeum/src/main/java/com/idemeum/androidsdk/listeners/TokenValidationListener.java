package com.idemeum.androidsdk.listeners;

import org.json.JSONObject;

public interface TokenValidationListener {
    void onSuccess(JSONObject mClaims, String message);

    void onError(int errorCode, String errorMsg);

}
