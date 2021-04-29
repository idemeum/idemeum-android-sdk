package com.idemeum.sampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class HomeScreenActivity extends AppCompatActivity {
    private TextView mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        mData = findViewById(R.id.text_data);
        if (getIntent().hasExtra("response")) {
            mData.setText(getIntent().getStringExtra("response"));
        }
    }

}