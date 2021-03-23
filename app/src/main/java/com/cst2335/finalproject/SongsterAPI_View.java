package com.cst2335.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class SongsterAPI_View extends AppCompatActivity {
    EditText search;
    int artist_id, song_id;
    String song_title;
    Button search_txt;
    String URL_query;
    ListView list;
    private ArrayList<SongData> songlist = new ArrayList<>();
    SongAdapter songAdapter;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songster_api);
        search = findViewById(R.id.editText2);
        search_txt = findViewById(R.id.button2);
        list.findViewById(R.id.list);
        search_txt.setOnClickListener(click -> {
            URL_query = "https://www.songsterr.com/a/ra/songs.json?pattern=" + search.getText().toString();
            SongData exeq = new SongData();
            exeq.execute();
            list.setAdapter(songAdapter=new SongAdapter());

        });
    }


    class SongData extends AsyncTask<String, Integer, String> {

        @Override
        public String doInBackground(String... args) {

            try {
                URL url = new URL(URL_query);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();
                BufferedReader read = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder build = new StringBuilder();
                String line = null;
                while ((line = read.readLine()) != null) {
                    build.append(line + "\n");
                }
                String string = build.toString();
                JSONArray bracket = new JSONArray(string);
                for (int i = 0; i <= bracket.length(); i++) {
                    //getting data for matched searches
                    JSONObject obj = bracket.getJSONObject(i);
                    song_id = obj.getInt("id");
                    song_title = obj.getString("title");
                    JSONObject artist_data = obj.getJSONObject("artist");
                    artist_id = artist_data.getInt("id");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        //Type 2
        public void onProgressUpdate(Integer... progress) {

        }
        //Type3

        public void onPostExecute(String fromDoInBackground) {
            Log.i("HTTP", fromDoInBackground);

        }

    }

   public  class SongAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<songData> songData;



        @Override
        public int getCount() {
            return songlist.size();
        }

        @Override
        public Object getItem(int position) {
            return songlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            View view=inflater.inflate(R.layout.list_layout,parent,false);
            songData currentsong= (com.cst2335.finalproject.songData) getItem(position);
             TextView txt=view.findViewById(R.id.textView);
             txt.setText(artist_id);
            return null;
        }

    }

}

    class songData{
        public boolean isSent;
        public String song_name;
        public long artist_id,song_id;

        public songData(String song_name,long artist_id,long song_id){
            this.song_name=song_name;
            this.artist_id=artist_id;
            this.song_id=song_id;
        }

        public String getSong_name(){return song_name;}
        public long getArtist_id(){ return artist_id;}
        public boolean isSent(){return isSent;}
        public long getSong_id(){return song_id;}

    }






