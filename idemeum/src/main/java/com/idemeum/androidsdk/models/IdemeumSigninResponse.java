package com.idemeum.androidsdk.models;

import com.google.gson.annotations.SerializedName;

public class IdemeumSigninResponse {
    private boolean status;
    @SerializedName("oidc")
    private OIDCToken token;
    private String message;
    private int statusCode;

    public boolean getStatus() {
        return status;
    }

    public OIDCToken getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status='" + status + '\'' +
                ", token='" + token + '\'' +
                ", message='" + message + '\'' +
                ", statusCode='" + statusCode + '\'' +
                '}';
    }
}
