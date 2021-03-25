package com.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
/*
soccerActivity class inherts from AppcompactActivity
 */
public class SoccerActivity extends AppCompatActivity {
    String savedString;
    ArrayList<News> listItems=new ArrayList<>();
    MyListAdapter adapter = new MyListAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer);

       SharedPreferences Moh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
         savedString = Moh.getString("ratting", "");

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SoccerActivity.this);
        alertDialog.setTitle("Rating");
        final EditText input = new EditText(SoccerActivity.this);
        input.setText(savedString);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setPositiveButton("YES",(dialog,which)->{

            savedString = input.getText().toString();
            Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();
            dialog.cancel();
        });
        alertDialog.show();

        ListView myList = (ListView)findViewById(R.id.ListView1);
        myList.setAdapter(adapter);

        SoccerQuery soccerQuery=new SoccerQuery();
        soccerQuery.execute("https://www.goal.com/en/feeds/news?fmt=rss");
    }
    @Override
    protected void onPause() {

        super.onPause();
        SharedPreferences Moh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit =Moh.edit();
        myEdit.putString("ratting",savedString);
        myEdit.commit();
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

                News news=null;
                int status=0;
                int eventType=xpp.getEventType();
                while(eventType!=XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if(xpp.getName().equals("item")){
                            news=new News();
                        }
                        else if(xpp.getName().equals("title")){
                            if(news!=null) {
                                status = 1;
                            }
                        }else if(xpp.getName().equals("pubDate")){
                            if(news!=null) {
                                status = 2;
                            }
                        }
                        else if(xpp.getName().equals("media:thumbnail")){
                            news.image=xpp.getAttributeValue(null,"url");
                        }
                        //if(xpp)
                    }else if(eventType ==  XmlPullParser.TEXT){
                        if(status==1){
                            news.title=xpp.getText();
                        }else if(status==2){
                            news.date=xpp.getText();
                        }
                        status=0;

                    }else if(eventType ==  XmlPullParser.END_TAG){
                        if(xpp.getName().equals("item")){
                            listItems.add(news);
                        }

                    }
                    eventType=xpp.next();
                }

                publishProgress(100);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            adapter.notifyDataSetChanged();
        }
    }

    private class MyListAdapter extends BaseAdapter {

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
            News news = listItems.get(position);
            View newView = null;

            newView = inflater.inflate(R.layout.news_layout, parent, false);
            TextView tView = newView.findViewById(R.id.newTxt);
            tView.setText(news.title);

            return newView;
        }
    }
}