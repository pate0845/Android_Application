package com.cst2335.finalproject;
/**
 * Author: Abdulrahman Al Hariri
 * Student number: 040920128
 * Professor: Eric Torunski
 * Course ID: CST 2335
 * Lab Section: 13
 * Porpuse: Project
 */

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
 *
 */
public class CarActivity extends AppCompatActivity {
    ArrayList<CarData> listItems;
    MyListAdapter adapter = new MyListAdapter();

    /**
     *
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        SharedPreferences prefs = getSharedPreferences("fileName", Context.MODE_PRIVATE);
        String savedString = prefs.getString("manu", "");
        EditText searchText=findViewById(R.id.inputText);
        searchText.setText(savedString);

        ListView myList = (ListView)findViewById(R.id.ListView1);
        Button searchButton = findViewById(R.id.Search);
        searchButton.setOnClickListener(e->{
            String manu=searchText.getText().toString().trim();
            CarQuery req=new CarQuery();
            req.execute("https://vpic.nhtsa.dot.gov/api/vehicles/GetModelsForMake/"+manu+"?format=json");
            ProgressBar bar=findViewById(R.id.bar1);
            bar.setVisibility(View.VISIBLE);
            listItems.clear();
            adapter.notifyDataSetChanged();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("manu", manu);
            editor.commit();
            editor.apply();

            Snackbar snackbar = Snackbar
                    .make(searchButton,getString(R.string.searching)+manu,Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.undo) , (View.OnClickListener) view -> {
                    });
            snackbar.show();
        });
        listItems =  new ArrayList<CarData>();
        myList.setAdapter(adapter);

        myList.setOnItemLongClickListener((p,b,pos,id)->{
            CarData data=listItems.get(pos);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(getString(R.string.CarData))

                    //What is the message:
                    .setMessage(
                            (getString(R.string.MakeID))
                            +data.id
                            +(getString(R.string.MakeName))
                            +data.carName
                            +(getString(R.string.ModelID))
                            +data.modelID
                            +(getString(R.string.ModelName))
                            +data.modelName
                    )

                    .setPositiveButton((getString(R.string.Yes)), (click, arg) -> {

                    })
                    .setNegativeButton((getString(R.string.No)), (click, arg) -> { }).create().show();
            return true;
        });

    }

    /**
     *
     */
    private class MyListAdapter extends BaseAdapter {
        /**
         *
         * @return
         */
        public int getCount() {
            return listItems.size();
        }

        /**
         *
         * @param position
         * @return
         */
        public Object getItem(int position) {
            return listItems.get(position);
        }

        /**
         *
         * @param position
         * @return
         */
        public long getItemId(int position) {
            return listItems.get(position).id;
        }

        /**
         *
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            CarData data = listItems.get(position);
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
     *
     */
    private class CarQuery extends AsyncTask<String, Integer, String> {
        /**
         *
         * @param strings
         * @return
         */
        @Override
        protected String doInBackground(String... strings) {
            try {
                publishProgress(25);
                URL url=new URL(strings[0]);
                HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                InputStream response=urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader( new InputStreamReader(response, Charset.defaultCharset()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null){
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                JSONObject triviaResult = new JSONObject(result);
                JSONArray carArray=triviaResult.getJSONArray("Results");
                publishProgress(50);
                for(int i=0;i<carArray.length();i++){
                    JSONObject object=carArray.getJSONObject(i);
                    CarData data=new CarData(
                            object.getInt("Make_ID"),
                            object.getString("Make_Name"),
                            object.getInt("Model_ID"),
                            object.getString("Model_Name")
                    );
                    listItems.add(data);
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
         *
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            ProgressBar bar=findViewById(R.id.bar1);
            bar.setProgress(values[0]);
        }

        /**
         *
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            adapter.notifyDataSetChanged();
            ProgressBar bar=findViewById(R.id.bar1);
            bar.setVisibility(View.INVISIBLE);

            Toast.makeText(CarActivity.this,(getString(R.string.success)), Toast.LENGTH_SHORT).show();
        }
    }
}