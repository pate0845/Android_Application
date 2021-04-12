package com.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Quiz_frame extends AppCompatActivity {
    Intent intent;
    String Qnumber,type,level;
    public ProgressBar progressBar;
    ArrayList<Questions> listItems;
    TriviaListAdapter adapter = new TriviaListAdapter();

    @Override
   public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_frame);

        ListView myList = findViewById(R.id.ListView1);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        intent=getIntent();
        Qnumber = intent.getStringExtra("Number");
        type = intent.getStringExtra("type");
        level = intent.getStringExtra("level");

        TriviaQuiz req = new TriviaQuiz();
        req.execute("https://opentdb.com/api.php?amount="+Qnumber+"&difficulty="+level+"&type="+type);

        adapter.notifyDataSetChanged();
        listItems =  new ArrayList<Questions>();
        myList.setAdapter(adapter);
    }
    /**
     * TriviaListAdapter is a clause that inherited from BaseAdapter
     * which is job to get show the vertical lise by using listView.
     */
    private class TriviaListAdapter extends BaseAdapter {

        public int getCount() {
            return listItems.size();
        }

        public Object getItem(int position) {
            return listItems.get(position);
        }

        public long getItemId(int position) {
            return position;
        }


        public View getView(int position, View old, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

            Questions data = listItems.get(position);
            View newView = null ;
            newView = inflater.inflate(R.layout.row_layout, parent, false);
            TextView QView = newView.findViewById(R.id.ques);
            QView.setText(data.question);
            TextView ansView = newView.findViewById(R.id.answer);
            ansView.setText(data.optA);
            return newView;
        }
    }

    class TriviaQuiz extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                publishProgress(25);
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();
                //JSON reading:   Look at slide 26
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, StandardCharsets.UTF_8), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                String result = sb.toString(); //result is the whole string
                // convert string to JSON: Look at slide 27:
                JSONObject jObject = new JSONObject(result);

                //get the double associated with "value"
                JSONArray TriviaArray = jObject.getJSONArray("Results");
                publishProgress(50);

                for(int i=0;i<TriviaArray.length();i++){
                    JSONObject object=TriviaArray.getJSONObject(i);
                    /**
                     * declaring the data from JSON and make them as string object.
                     */
                    Questions data=new Questions(
                            object.getString("question"),
                            object.getString("correct_answer")
                    );
                    // this will add all data from JSON to the listItems
                    listItems.add(data);
                }
                publishProgress(100);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return "done";
        }


        public void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            ProgressBar bar=findViewById(R.id.progress);
            bar.setProgress(values[0]);;
        }
        @Override
        public void onPostExecute(String s) {
            super.onPostExecute(s);
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);

    }
    }
}