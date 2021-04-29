package com.idemeum.androidsdk.listeners;


import com.idemeum.androidsdk.models.IdemeumSigninResponse;

public interface IdemeumSigninListener {
    void onSuccess(IdemeumSigninResponse mIdemeumSigninResponse);

    void onError(int statusCode, String errorMsg);

//
}
