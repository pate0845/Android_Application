package com.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SoccerActivity extends AppCompatActivity {

    ArrayList<News> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer);
    }
    private class SoccerQuery extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {
            try{
                URL url= new URL(strings[0]);
                HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                InputStream response=urlConnection.getInputStream();
                XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp=factory.newPullParser();
                xpp.setInput(response,"UTF-8");

                News news=new News("","",""
//                        object.getString("Title"),
//                        object.getString("Date"),
//                        object.getInt("Image")
                );
                int status=0;
                int eventType=xpp.getEventType();
                while(eventType!=XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if(xpp.getName().equals("title")){
                            status=1;
                        }else if(xpp.getName().equals("pubDate")){
                            status=2;
                        }else{
                            status=0;
                        }

                        //if(xpp)
                    }else if(eventType ==  XmlPullParser.TEXT){
                        if(status==1){
                            news.title=xpp.getText();
                        }else if(status==2){
                            news.date=xpp.getText();
                        }

                    }else if(eventType ==  XmlPullParser.END_TAG){
                        listItems.add(news);
                        news=new News();
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}