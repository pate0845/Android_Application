package com.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CarResultActivity extends AppCompatActivity {
    String modelName;
    String carName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_result);
        Bundle carDataToPass = getIntent().getExtras();                                                //get the data that was passed from FragmentExample
        Toolbar tBar = (Toolbar)findViewById(R.id.Cartoolbr);
        setSupportActionBar(tBar);
        carName = carDataToPass.getString("carName");
        modelName = carDataToPass.getString("modelName");
        //This is copied directly from FragmentExample.java
        CarDetailedFragement dFragment = new CarDetailedFragement();
        dFragment.setArguments( carDataToPass ); //pass data to the the fragment
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
                String url = "http://www.google.com/search?q="+carName+modelName;
                Intent in = new Intent(Intent.ACTION_VIEW);
                in.setData(Uri.parse(url));
                startActivity(in);
                finish();
                message = getString(R.string.carview);
                break;
            case R.id.carSave:
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
                Intent nextActivity = new Intent(CarResultActivity.this, Car_Favorites_Activity.class);
                startActivity(nextActivity);


                message = getString(R.string.savedCar);
                break;
            case R.id.carShopping:
                String url1 = "https://www.autotrader.ca/cars/?mdl="+carName+"&make="+modelName+"&loc=K2G1V8";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url1));
                startActivity(i);
                message = getString(R.string.carshopping);
                break;
            case R.id.carhome:
//                String url2 = "https://vpic.nhtsa.dot.gov/api/vehicles/GetModelsForMake/"+manu+"?format=json";
//                Intent i2 = new Intent(Intent.ACTION_VIEW);
//                i2.setData(Uri.parse(url2));
//                startActivity(i2);
                Intent goToSearchPage  = new Intent(CarResultActivity.this, MainActivity.class);
                startActivity(goToSearchPage);
                message = getString(R.string.homePage);
                break;
        }

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        return true;
    }
}