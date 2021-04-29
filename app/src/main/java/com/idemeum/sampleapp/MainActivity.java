package com.idemeum.sampleapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.idemeum.androidsdk.listeners.IdemeumSigninListener;
import com.idemeum.androidsdk.manager.IdemeumManager;
import com.idemeum.androidsdk.models.IdemeumSigninResponse;
import com.idemeum.androidsdk.listeners.TokenValidationListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    // Declare an object for Idemeum Request class
    private IdemeumManager mIdemeumManager;
    //BIOMETRIC
    private final String apiKeyBiometric = "5166e6ac-9442-11eb-a8b3-0242ac130003";
    //ONE CLICK
    private final String apiKeyOneClick = "00000000-0000-0000-0000-000000000000";
    //DVMI
    private final String apiKeyDVMI = "c1d84ad4-9442-11eb-a8b3-0242ac130003";


    private ConstraintLayout mLayoutLogin, mLayoutUserClaims;
    private TextView mTxtUserClaims;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button mBtnLoginWithIdemeumBiometric = findViewById(R.id.btn_login_with_idemeum_biometric);
        Button mBtnLoginWithIdemeumOneClick = findViewById(R.id.btn_login_with_idemeum_one_click);
        Button mBtnLoginWithIdemeumDVMI = findViewById(R.id.btn_login_with_idemeum_dvmi);
        Button mBtnLogout = findViewById(R.id.logout);
        mLayoutLogin = findViewById(R.id.layout_login);
        mLayoutUserClaims = findViewById(R.id.layout_user_claims);
        mTxtUserClaims = findViewById(R.id.userClaims);

        // initialize an idemeum manager object
        mIdemeumManager = IdemeumManager.getInstance(apiKeyOneClick);

        mIdemeumManager.isLoggedIn(this, isLoggedIn -> {
            if (isLoggedIn) {
                validateToken();
            } else {
                showLoginButton();
            }

        });

        mBtnLoginWithIdemeumBiometric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on button click do login with idemeum
                mIdemeumManager = IdemeumManager.getInstance(apiKeyBiometric);
                mIdemeumManager.login(MainActivity.this
                        , new IdemeumSigninListener() {


                            @Override
                            public void onSuccess(IdemeumSigninResponse mIdemeumSigninResponse) {
                                validateToken();
                            }

                            @Override
                            public void onError(int statusCode, String error) {
                                Toast.makeText(MainActivity.this,
                                        error, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        mBtnLoginWithIdemeumOneClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on button click do login with idemeum
                mIdemeumManager = IdemeumManager.getInstance(apiKeyOneClick);
                mIdemeumManager.login(MainActivity.this
                        , new IdemeumSigninListener() {
                            @Override
                            public void onSuccess(IdemeumSigninResponse mIdemeumSigninResponse) {
                                validateToken();
                            }

                            @Override
                            public void onError(int statusCode, String errorMsg) {
                                Toast.makeText(MainActivity.this,
                                        errorMsg, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        mBtnLoginWithIdemeumDVMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on button click do login with idemeum
                mIdemeumManager = IdemeumManager.getInstance(apiKeyDVMI);
                mIdemeumManager.login(MainActivity.this
                        , new IdemeumSigninListener() {
                            @Override
                            public void onSuccess(IdemeumSigninResponse mIdemeumSigninResponse) {
                                validateToken();
                            }

                            @Override
                            public void onError(int statusCode, String errorMsg) {
                                Toast.makeText(MainActivity.this,
                                        errorMsg, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIdemeumManager != null) {
                    mIdemeumManager.logout(MainActivity.this);
                    showLoginButton();
                }
            }
        });
    }

    private void showLoginButton() {

        mLayoutUserClaims.setVisibility(View.GONE);
        mLayoutLogin.setVisibility(View.VISIBLE);

    }

    private void showUserClaims() {
        mLayoutUserClaims.setVisibility(View.VISIBLE);
        mLayoutLogin.setVisibility(View.GONE);

    }

    private void validateToken() {
        mIdemeumManager.userClaims(this, new TokenValidationListener() {

            @Override
            public void onSuccess(JSONObject mClaims, String message) {
                //user claims will be received as JSONObject here
                mTxtUserClaims.setText(fetchDataFromClaims(mClaims));
                showUserClaims();
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }

        });
    }

    private String fetchDataFromClaims(JSONObject mClaims) {

        StringBuilder mStringBuilder = new StringBuilder();
        mStringBuilder.append("You are currently logged in.")
                .append("\n\n")
                .append("User profile:")
                .append("\n\n");

        try {
            if (mClaims.has("given_name")) {
                mStringBuilder.append("First Name: ").append(mClaims.getString("given_name"));
                mStringBuilder.append("\n\n");
            }
            if (mClaims.has("family_name")) {
                mStringBuilder.append("Last Name: ").append(mClaims.getString("family_name"));
                mStringBuilder.append("\n\n");
            }
            if (mClaims.has("email")) {
                mStringBuilder.append("Email Address: ").append(mClaims.getString("email"));
                mStringBuilder.append("\n\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return mStringBuilder.toString();
        }

        return mStringBuilder.toString();
    }


}