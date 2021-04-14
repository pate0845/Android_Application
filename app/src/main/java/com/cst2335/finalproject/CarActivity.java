package com.cst2335.finalproject;
/**
 * Author: Abdulrahman Al Hariri
 * Student number: 040920128
 * Professor: Islam Gomaa, Fedor Ilitchev
 * Course ID: CST 2335
 * Lab Section: 13
 * Porpuse: Project for mobile application car database.
 */

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * CarActivity which is the name of the class that I have used for my part
 * that is inherited from AppCompatActivity
 */
public class CarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {
    String savedCar;
    Boolean IsPhone;
    String carName;
    String modelName;
    /*
     * this array is listed all the array attribute in the listItem
     *  the adapter can give access to items and responsible to create
     * view for each item
     */
    ArrayList<CarData> carlistItems;
    MyListAdapter adapter = new MyListAdapter();

    /**
     * the using of onCreate function and recreate the activity
     * it also used to load the data from savedInstanceState.
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        Toolbar T = findViewById(R.id.toolbar);
        setSupportActionBar(T);

        DrawerLayout Cardrawer = findViewById(R.id.Cardrawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, Cardrawer, T, R.string.open, R.string.close);
        Cardrawer.addDrawerListener(toggle);
        toggle.syncState();
//
        NavigationView CarnavigationView = findViewById(R.id.Carnav_view);
        CarnavigationView.setNavigationItemSelectedListener(this);

        /*
         * sharedPreference is used to save the input and read and write the text.
         */
        SharedPreferences prefs = getSharedPreferences("fileName", Context.MODE_PRIVATE);
        String savedString = prefs.getString("manu", "");
        EditText searchText=findViewById(R.id.inputText);
        searchText.setText(savedString);
        /**
         * ListView, is the layout xml that we used to list all the items.
         */
        ListView myList = (ListView)findViewById(R.id.ListView1);
        /**
         * this button is used to search for the item on the app.
         */
       /* Button helpB = findViewById(R.id.help_item);
        helpB.setOnClickListener(e->{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(getString(R.string.CarData))
                    .setMessage(R.string.instruction)
                    .setNegativeButton((getString(R.string.Ok)), (click, arg) -> { }).create().show();
        });*/



        Button searchButton = findViewById(R.id.Search);
        searchButton.setText(R.string.search);
        searchButton.setOnClickListener(e->{
            String manu=searchText.getText().toString().trim();

            /**
             *  creating a new query object which helps in hold the numbers of rows
             *  that each consist of the columns is specified.
             */
            CarQuery req=new CarQuery();
            req.execute("https://vpic.nhtsa.dot.gov/api/vehicles/GetModelsForMake/"+manu+"?format=json");
            /**
             *
             * ProgressBar does the download and upload files, which we can called it user interface.
             */
            ProgressBar bar=findViewById(R.id.bar1);
            bar.setVisibility(View.VISIBLE);
            /**
             * we used .clear() to clear all the hiding information that we search for.
             */
            carlistItems.clear();
            /**
             *  we use this method to notify the observer that the data is changed, which refresh by itself if the view reflection
             */
            adapter.notifyDataSetChanged();
            /**
             *  here is the write part of SharedPreferences which we used for that putString
             */
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("manu", manu);
            editor.commit();
            editor.apply();
            /**
             * we used Snackbar to show a message in the app when we have an action
             * ex: when we searching here, message will appear saying "You are searching"
             */
            Snackbar snackbar = Snackbar
                    .make(searchButton,getString(R.string.searching)+manu,Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.undo) , (View.OnClickListener) view -> {
                    });
            snackbar.show();
        });
        /**
         * ArrayList can be helpful in terms of implement the list interface, which can add or remove elements.
         */
        carlistItems =  new ArrayList<CarData>();
        myList.setAdapter(adapter);
        /**
         * the setOnItemLongClickListener method which have four argument,d,pos,id
         * the p progressbar, the int pos, and long id.
         * that I we used it to get the transactions and data from listItems
         */
        myList.setOnItemLongClickListener((p,b,pos,id)->{
            // CarData data=listItems.get(pos);
            /**
             * AlertDialog is used to display the box message,
             * also it can be used to ask users to something if the users insert wrong information.
             * the method setMessage jobs to show the listAdapter information
             * in the dialog as a content.
             */
           /* AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(getString(R.string.CarData))
                    .setMessage(
                            (getString(R.string.MakeID))
                            +data.id
                            +(getString(R.string.MakeName))
                            +data.carName
                            +(getString(R.string.ModelID))
                            +data.modelID
                            +(getString(R.string.ModelName))
                            +data.modelName
                    )*/
            /**
             * this is Lambda Expressions if the click is positive then true
             * otherwise will use the .setNegativeButton to create then show
             * the result of click.
             */
                    /*.setPositiveButton((getString(R.string.Yes)), (click, arg) -> {
                    })
                    .setNegativeButton((getString(R.string.No)), (click, arg) -> { }).create().show();*/
            CarData data=carlistItems.get(pos);
            //Create a bundle to pass data to the new fragment
            Bundle dataToPass = new Bundle();
            dataToPass.putString("carName", carlistItems.get(pos).getCarName() );
            dataToPass.putInt("id", carlistItems.get(pos).getId() );
            dataToPass.putInt("modelID", carlistItems.get(pos).getModelID() );
            dataToPass.putString("modelName", carlistItems.get(pos).getModelName() );
            boolean sendSide;
            // if the device is not phone (tablet) load the fragment

            Intent nextActivity = new Intent(CarActivity.this, CarResultActivity.class);
            nextActivity.putExtras(dataToPass);
            startActivityForResult(nextActivity,100);


            return true;
        });

    }
    /**
     *
     */
    @Override
    /**
     * using onPause method as there is another activity has been launched
     */
    protected void onPause() {

        super.onPause();
        SharedPreferences Abdul = getSharedPreferences("fileName", MODE_PRIVATE);
        SharedPreferences.Editor myEdit =Abdul.edit();
        myEdit.putString(getString(R.string.carsearching),savedCar);
        myEdit.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater Carinflater = getMenuInflater();
        Carinflater.inflate(R.menu.car_menu, menu);

        return true;
    }

    @Override
    public boolean onNavigationItemSelected( MenuItem item) {

        String message = null;

        switch (item.getItemId()) {
            case R.id.Carinstructions:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(getString(R.string.CarData))
                        .setMessage(R.string.Carinstruction)
                        .setNegativeButton((getString(R.string.Ok)), (click, arg) -> { }).create().show();
                message = getString(R.string.carview);
                break;
            case R.id.carSave:
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
                Intent nextActivity = new Intent(CarActivity.this, Car_Favorites_Activity.class);
                startActivity(nextActivity);
                break;
            case R.id.CarHome:
                Intent HomeActivity = new Intent(CarActivity.this, SongMainActivity.class);
                startActivity(HomeActivity);
                break;

        }
        DrawerLayout drawerLayout = findViewById(R.id.Cardrawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        Toast.makeText(this, "NavigationDrawer: " + message, Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem Caritem) {
        String message = null;
        //Look at your menu XML file. Put a case for every id in that file:
        switch(Caritem.getItemId())
        {

            //what to do when the menu item is selected:
            case R.id.Carinstructions:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(getString(R.string.CarData))
                        .setMessage(R.string.Carinstruction)
                        .setNegativeButton((getString(R.string.Ok)), (click, arg) -> { }).create().show();
                message = getString(R.string.carview);
                break;
            case R.id.carSave:
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
                // Intent nextActivity = new Intent(CarResultActivity.this, Car_Favorites_Activity.class);
                // startActivity(nextActivity);


            case R.id.carview:
                String url = "http://www.google.com/search?q="+carName+modelName;
                Intent in = new Intent(Intent.ACTION_VIEW);
                in.setData(Uri.parse(url));
                startActivity(in);
                finish();
                message = getString(R.string.carview);
                break;


            case R.id.carShopping:
                // String url1 = "https://www.autotrader.ca/cars/?mdl="+carName+"&make="+modelName+"&loc=K2G1V8";
                //Intent i = new Intent(Intent.ACTION_VIEW);
                // i.setData(Uri.parse(url1));
                //startActivity(i);
                message = getString(R.string.carshopping);
                break;
            case R.id.carhome:
//                String url2 = "https://vpic.nhtsa.dot.gov/api/vehicles/GetModelsForMake/"+manu+"?format=json";
//                Intent i2 = new Intent(Intent.ACTION_VIEW);
//                i2.setData(Uri.parse(url2));
//                startActivity(i2);
                Intent HomeActivity = new Intent(CarActivity.this, MainActivity.class);
                startActivity(HomeActivity);
                break;

        }

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        return true;
    }
    /**
     * MyListAdapter is a clause that inherited from BaseAdapter
     * which is job to get show the vertical lise by using listView.
     */
    private class MyListAdapter extends BaseAdapter {
        /**
         * getCount is returning count total list of the adapter
         * @return ListItem.size
         */
        public int getCount() {
            return carlistItems.size();
        }

        /**
         * getItem is returning the object data from the items in order to access the adapter's data
         * @param position
         * @return listItems.get
         */
        public Object getItem(int position) {
            return carlistItems.get(position);
        }

        /**
         * the getItemId job is to access and unique value has a data of long, which can be attached to item in the list.
         * @param position
         * @return listItems.get
         */
        public long getItemId(int position) {
            return carlistItems.get(position).id;
        }

        /**
         * the getview is calling the method to get view. it's job to create the view behind the scenes.
         * @param position
         * @param convertView
         * @param parent
         * @return listItem with get position
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            /**
             * LayoutInflater: is building the view object and taking the XML file as a input.
             */
            LayoutInflater inflater = getLayoutInflater();
            CarData data = carlistItems.get(position);
            View newView= null;
            newView = inflater.inflate(R.layout.cars_items, parent,false);
            TextView carView = newView.findViewById(R.id.carNames);
            carView.setText(data.carName);
            TextView modelView = newView.findViewById(R.id.carModels);
            modelView.setText(data.modelName);
            return newView;
        }

    }

    /**
     * Query class that inherited from AsyncTask.
     * it's job to mark a method in a annotated class as a Query method, which can retrieve data from an http server,
     * so that will be run when this method is called.
     */
    private class CarQuery extends AsyncTask<String, Integer, String> {
        /**
         * this method consists of the query codes which needed to be executed in the background.
         * @param strings
         * @return String
         */
        @Override
        protected String doInBackground(String... strings) {
            try {
                publishProgress(25);
                /**
                 * Url, this method is used to load the data form array like
                 */
                URL url=new URL(strings[0]);
                HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                /**
                 * inputStream is used to read any input the user put
                 */
                InputStream response=urlConnection.getInputStream();
                /**
                 * the method BufferedReader is used to read the character text form inputStream.
                 */
                BufferedReader reader = new BufferedReader( new InputStreamReader(response, Charset.defaultCharset()));
                /**
                 * the method StringBuilder is used to create many string.
                 */
                StringBuilder sb = new StringBuilder();
                String line = null;
                /**
                 * while loop is used in this method to loop through the query codes
                 * so that it will read each line and append it to print out the result.
                 */
                while ((line = reader.readLine()) != null){
                    sb.append(line + "\n");
                }
                /**
                 * printing the result of append
                 */
                String result = sb.toString();
                /**
                 * JSON object is used to implement the data inside JSON file.
                 */
                JSONObject triviaResult = new JSONObject(result);
                /**
                 * JSONArray will get each array in the JSON file and linked to string value
                 * to get output from it.
                 */
                JSONArray carArray=triviaResult.getJSONArray("Results");
                publishProgress(50);
                /**
                 * For loop: this loop through JSON array to get all data value from there.
                 */
                for(int i=0;i<carArray.length();i++){
                    JSONObject object=carArray.getJSONObject(i);
                    /**
                     * declaring the data from JSON and make them as string object.
                     */
                    CarData data=new CarData(
                            object.getInt("Make_ID"),
                            object.getString("Make_Name"),
                            object.getInt("Model_ID"),
                            object.getString("Model_Name")
                    );
                    // this will add all data from JOSN to the listItems
                    carlistItems.add(data);
                }
                publishProgress(100);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "done";
        }

        /**
         * on Progress Update used to display progress to the users while the task is running
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            ProgressBar bar=findViewById(R.id.bar1);
            bar.setProgress(values[0]);
        }

        /**
         * onPostExecute used to animate the progress bar or show logs in a text field
         * so it's display any form of the progress in the user interface.
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            /**
             * notifyDataSetChanged is looking for the items that display on the screen at the same time that is call running.
             */
            adapter.notifyDataSetChanged();
            ProgressBar bar=findViewById(R.id.bar1);
            bar.setVisibility(View.INVISIBLE);
            /**
             * toast is giving a feedback from the system operation so it's dis*appear by itself when the time is out.
             */
            Toast.makeText(CarActivity.this,(getString(R.string.success)), Toast.LENGTH_SHORT).show();
        }
    }

}