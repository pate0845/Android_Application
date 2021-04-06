package com.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class CarResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_result);
        Bundle dataToPass = getIntent().getExtras();                                                //get the data that was passed from FragmentExample
        Toolbar tBar = (Toolbar)findViewById(R.id.Cartoolbr);
        setSupportActionBar(tBar);

        //This is copied directly from FragmentExample.java
        CarDetailedFragement dFragment = new CarDetailedFragement();
        dFragment.setArguments( dataToPass ); //pass data to the the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.CarfragmentLocation, dFragment)
                .commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater Carinflater = getMenuInflater();
        Carinflater.inflate(R.menu.car_menu, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem Caritem) {
        String message = null;
        //Look at your menu XML file. Put a case for every id in that file:
        switch(Caritem.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.carview:
                finish();
                message = "Go to View Cars";
                break;
            case R.id.carSave:
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
                builder.setTitle("Save Cars")
                        .setMessage(" ")
                        .setCancelable(false)

                        .setPositiveButton("OK", (click, arg) -> {

                        })
                        .create().show();
                message = "You clicked to save the a car";
                break;
            case R.id.carShopping:
                message = "You clicked To shop a car";
                break;
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        return true;
    }
}