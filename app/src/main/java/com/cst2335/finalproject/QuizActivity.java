package com.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    String Type,Level,Num;
    private ProgressBar progressBar;
    ListView question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Intent intent=getIntent();
        Num = intent.getStringExtra("Number");
        Type=intent.getStringExtra("Type");
        Level=intent.getStringExtra("Level");



        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
       TriviaQuiz req = new TriviaQuiz();
        req.execute("https://opentdb.com/api.php?amount="+Num+"&difficulty="+Level+"&type="+Type);
       question = findViewById(R.id.ListView1);
    }
    class TriviaQuiz extends AsyncTask<String ,String ,String>{
        private String Questions;
        private String[] IncAns;
        private String CrtAns;
        @Override
        protected String doInBackground(String... strings) {

            try {
                String[] args = new String[0];
                URL url = new URL(args[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(response, "UTF-8");

                String iconName = null;

                int eventType = xpp.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {

                    if (eventType == XmlPullParser.START_TAG) {
                        //If you get here, then you are pointing at a start tag
                        if (xpp.getName().equals("category")) {
                            Questions = xpp.getAttributeValue(null, "question");
                            publishProgress(75);
                        }
                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
                }
            } catch (Exception e) {
            publishProgress(100);
            }

            return "Done";
        }

        private void publishProgress(int i) {
        }

        public void onProgressUpdate(Integer... args) {
            progressBar.setProgress(args[0]);
        }
        @Override
        public void onPostExecute(String fromDoInBackground) {
            super.onPostExecute(fromDoInBackground);
            Intent goToProfile  = new Intent(QuizActivity.this, EmptyActivity.class);
            goToProfile .putExtra("Question", Questions);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}