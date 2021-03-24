package com.cst2335.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SongsterAPI_View extends AppCompatActivity {
    EditText search;
    int artist_id, song_id;
    String song_title, display_toast;
    Button search_txt;
    String URL_query;
    ListView list;
    private ArrayList<songData> songlist = new ArrayList<songData>();
    private ContentValues searched_songs = new ContentValues();
    SongAdapter songAdapter;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songster_api);
        search = findViewById(R.id.editText2);
        search_txt = findViewById(R.id.button2);
        list = findViewById(R.id.list);
        search_txt.setOnClickListener(click -> {
            URL_query = "https://www.songsterr.com/a/ra/songs.json?pattern=" + search.getText().toString();
            SongData exeq = new SongData();
            exeq.execute();
            list.setAdapter(songAdapter = new SongAdapter(this, songlist));
            //     String [] columns={Song_Database.COL_ID,Song_Database.COL_SONG_ID,Song_Database.COL_ARTIST_ID,Song_Database.COL_SONG_NAME};
            //      Cursor results = db.query(false, Song_Database.TABLE_NAME, columns, null, null,
            //              null, null, null, null);
            songAdapter.notifyDataSetChanged();
            display_toast = search.getText().toString();
            search.setText("");
        });


    }

    private void loadDatabase() {
        Song_Database database = new Song_Database(this);
        db = database.getWritableDatabase();
        String[] columns = {Song_Database.COL_ID, Song_Database.COL_SONG_ID, Song_Database.COL_ARTIST_ID, Song_Database.COL_SONG_NAME};
        Cursor results = db.query(false, Song_Database.TABLE_NAME, columns, null, null,
                null, null, null, null);
        int songid = results.getColumnIndex(Song_Database.COL_SONG_ID);
        int artistid = results.getColumnIndex(Song_Database.COL_ARTIST_ID);
        int indexid = results.getColumnIndex(Song_Database.COL_ID);
        int songname = results.getColumnIndex(Song_Database.COL_SONG_NAME);

        while (results.moveToNext()) {
            String song_name = results.getString(songname);
            long song_id = results.getInt(songid);
            long artist_id = results.getInt(artistid);
            long id = results.getLong(indexid);
            songlist.add(new songData(song_name, artist_id, song_id, id));
        }

    }

    protected void deletedatabase(songData d) {
        db.delete(Song_Database.TABLE_NAME, Song_Database.COL_ID + "=?", new String[]{Long.toString(d.getColumn_id())});
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
                for (int i = 0; i < bracket.length(); i++) {
                    //getting data for matched searches
                    JSONObject obj = bracket.getJSONObject(i);
                    song_id = obj.getInt("id");
                    song_title = obj.getString("title");
                    JSONObject artist_data = obj.getJSONObject("artist");
                    artist_id = artist_data.getInt("id");
                    songData songdata = new songData(song_title, artist_id, song_id);
                    searched_songs.put(Song_Database.COL_SONG_NAME, song_title);
                    searched_songs.put(Song_Database.COL_SONG_ID, song_id);
                    searched_songs.put(Song_Database.COL_ARTIST_ID, artist_id);
                    songlist.add(songdata);
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
            Toast.makeText(getApplicationContext(), "Searching for " + display_toast, Toast.LENGTH_SHORT).show();

        }

    }

    public class SongAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<songData> songlist;

        public SongAdapter(Context context, ArrayList<songData> songlist) {
            this.context = context;
            this.songlist = songlist;
        }

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
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.list_layout, parent, false);
            songData currentsong = (songData) getItem(position);
            TextView sngnam = view.findViewById(R.id.textView);
            TextView sngid = view.findViewById(R.id.textView5);
            TextView artid = view.findViewById(R.id.textView6);
            sngnam.setText("Song Name:" + currentsong.getSong_name());
            sngid.setText("Song id:" + currentsong.getSong_id());
            artid.setText("Artist id:" + currentsong.getArtist_id());
            sngid.setOnClickListener(onclick -> {
                String d = "https://www.songsterr.com/a/wa/song?id=" + currentsong.getSong_id();
                Intent songster_browser = new Intent(Intent.ACTION_VIEW);
                songster_browser.setData(Uri.parse(d));
                startActivity(songster_browser);
            });
            artid.setOnClickListener(onclick -> {
                String d = "https://www.songsterr.com/a/wa/artist?id=" + currentsong.getArtist_id();
                Intent songster_browser = new Intent(Intent.ACTION_VIEW);
                songster_browser.setData(Uri.parse(d));
                startActivity(songster_browser);
            });

            return view;
        }

    }

}
    class songData{
        public boolean isSent;
        public String song_name;
        public long artist_id,song_id,column_id;

        public songData(String song_name,long artist_id,long song_id){
            this.song_name=song_name;
            this.artist_id=artist_id;
            this.song_id=song_id;
        }

        public songData(String song_name,long artist_id,long song_id,long column_id){
            this.song_name=song_name;
            this.artist_id=artist_id;
            this.song_id=song_id;
            this.column_id=column_id;
        }
        public String getSong_name(){return song_name;}
        public long getArtist_id(){ return artist_id;}
        public boolean isSent(){return isSent;}
        public long getSong_id(){return song_id;}
        public long getColumn_id(){return column_id;}

    }




