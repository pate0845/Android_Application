package com.cst2335.finalproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/*
         This will create a fragment on a new page
         */
public class EmptyActivity extends AppCompatActivity {

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        Bundle data=getIntent().getExtras();
        BlankFragment fragment=new BlankFragment();
        fragment.setArguments(data);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLocation,fragment)
                .commit();
    }
}