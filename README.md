## Idemeum Android SDK


## Installation

1. Add idemeum SDK dependency to gradle

  In your app's build.gradle dependencies section, add the following:

  ```
  implementation 'com.idemeum:identity-android-sdk:1.0.0'
  ```

2. Create URL scheme

  Add URL scheme code in Android Manifest XML file.

  ```
  <activity android:name="com.idemeum.androidsdk.ui.RedirectUriReceiverActivity"   
            android:exported="true">
  <intent-filter>
  <action android:name="android.intent.action.VIEW" />
  <category android:name="android.intent.category.DEFAULT" />
  <category android:name="android.intent.category.BROWSABLE" />
      <data android:host="auth"
          android:scheme="idemeum" />
  </intent-filter>
  </activity>
  ```

## Usage

## Initialize idemeum SDK

Initialize idemeum SDK instance. 
Use your clientId that you obtained from [idemeum developer portal]().

``` 
IdemeumManager mIdemeumManager = IdemeumManager.getInstance("<-- ClientId -->");
```

## Manage user authentication state

```
mIdemeumManager.isLoggedIn(this, isLoggedIn - > {
    // Process the user logged-in state.           
    if (isLoggedIn) {
        // user is logged in
    } else {
        // user is NOT logged in
    }
});
```

## Login 

```
mIdemeumManager.login(this, new IdemeumSigninListener() {
    @Override
     public void onSuccess(OIDCToken oidcToken) {
        /* receive ID and Access tokens from idemeum
        {
         "accessToken": "string",
         "expires_in": 0,
         "idToken": "string"
        } */
    }
    @Override
    public void onError(int statusCode, String error) {
        // Login fail
    }
});
```

## Get and validate user claims

```
mIdemeumManager.userClaims(this, new TokenValidationListener() {

    @Override
    public void onSuccess(JSONObject claims) {
        //fetch user approved claims from JSON response
    }

    @Override
    public void onError(int errorCode, String errorMsg) {
        // If there is an error you can process it here
    }

});
```

## Logout

```
mIdemeumManager.logout()
```


## Complete documentation guide

You can checkout the complete documentation guide [here](https://docs.idemeum.com/reference/android-guide/)


## Contact

You can reach us at <support@idemeum.com>

## Licence

This project uses the following license: [MIT License](https://github.com/idemeum/idemeum-android-sdk/blob/main/LICENSE)
