package com.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Soccer_Empty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer__empty);
        Bundle dataToPass = getIntent().getExtras();                                                //get the data that was passed from FragmentExample

        //This is copied directly from FragmentExample.java
        SoccerDetailedFragment dFragment = new SoccerDetailedFragment();
        dFragment.setArguments( dataToPass ); //pass data to the the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.SoccerfragmentLocation, dFragment)
                .commit();
    }
}