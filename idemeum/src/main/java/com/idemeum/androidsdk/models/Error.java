package com.idemeum.androidsdk.models;

public enum Error {

    OPERATION_CANCELLED(11, "Operation cancelled!!"),
    TOKEN_ERROR(12, "Token not received from backend!!"),
    UNKNOWN_ERROR(13, "Unknown error!!"),
    OIDC_TOKEN_EMPTY(14, "User is logout or token not available"),
    NO_INTERNET_CONNECTION(15, "Internet Connection not available");

    private final int code;
    private final String description;

    Error(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }

}
