package com.idemeum.androidsdk.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.idemeum.androidsdk.manager.IdemeumManager;

public class RedirectUriReceiverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Handling the redirect this way ensures that the WebView closes after redirect
        startActivity(IdemeumManager.getInstance(IdemeumManager.clientID).createResponseHandlingIntent(this, getIntent().getData()));
        finish();
    }


}