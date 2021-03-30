package com.cst2335.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;


import androidx.appcompat.app.AlertDialog;
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
    long artist_id, song_id;
    String song_title, display_toast;
    Button search_txt;
    String URL_query;
    ListView list;
    ImageView fav_btn;

    private ArrayList<songData> songlist = new ArrayList<songData>();
    private ContentValues searched_songs = new ContentValues();
    public static final String SONG_COL_ID="ID";
    public static final String ARTIST_ID="ARTIST";
    public static final String SONG_NAME="SONG_NAME";
    public static final String SONG_ID="SONG_ID";
    SongAdapter songAdapter;
    SQLiteDatabase db;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.songster_api);
        //search bar
        search = findViewById(R.id.editText2);
        //search button
        search_txt = findViewById(R.id.button2);
        //initializing list
        list = findViewById(R.id.list);
        //initializing progress bar
        progressBar=findViewById(R.id.progressBar2);
        //initializing the favourite button
        fav_btn=findViewById(R.id.fav_btn);
        FrameLayout frameLayout=findViewById(R.id.fragmentLocation);
        boolean result=frameLayout != null;

        //onClicklistener
        search_txt.setOnClickListener(click -> {
            URL_query = "https://www.songsterr.com/a/ra/songs.json?pattern=" + search.getText().toString();
            SongData exeq = new SongData();
            exeq.execute();
            setProgress(25);
            list.setAdapter(songAdapter = new SongAdapter(this, songlist));
            songAdapter.notifyDataSetChanged();
            display_toast = search.getText().toString();
            search.setText("");
        });



        list.setOnItemLongClickListener(((parent, view, position, id) -> {
            songData loadMessage = songlist.get(position);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("" + getString(R.string.delete))
                    .setMessage("" + getString(R.string.row) + position + "\n" +
                            "" + getString(R.string.data) + id)
                    .setNeutralButton("Dismiss", (click, b) -> {
                    })
                    .create().show();
            return true;
        }));


        list.setOnItemClickListener((parent, view, position, id) -> {
            Bundle data = new Bundle();
            data.putLong(SONG_COL_ID,id);
            data.putString(SONG_NAME, songlist.get(position).song_name);
            data.putLong(SONG_ID, songlist.get(position).song_id);
            data.putLong(ARTIST_ID, songlist.get(position).artist_id);
            if(result) {
                BlankFragment fragment = new BlankFragment();
                fragment.setArguments(data);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, fragment, Long.toString(id))
                        .commit();
            }else{
                Intent newActivity=new Intent(SongsterAPI_View.this,EmptyActivity.class);
                newActivity.putExtras(data);
                startActivity(newActivity);
            }
        });

    }

    //loading the database
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

    //deleting the database
    protected void deletedatabase(songData d) {
        db.delete(Song_Database.TABLE_NAME, Song_Database.COL_ID + "=?", new String[]{Long.toString(d.getColumn_id())});
    }


    //getting the url and searching for songs
    class SongData extends AsyncTask<String, Integer, String> {

        @Override
        public String doInBackground(String... args) {

            try {
                progressBar.setVisibility(View.INVISIBLE);
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
                    searched_songs.put(Song_Database.COL_SONG_NAME, song_title);
                    searched_songs.put(Song_Database.COL_SONG_ID, song_id);
                    searched_songs.put(Song_Database.COL_ARTIST_ID, artist_id);
//                    long newId = db.insert(Song_Database.TABLE_NAME, null, searched_songs);
                    songData songdata = new songData(song_title, artist_id, song_id,i+1);
                    songlist.add(songdata);
                    setProgress(50);
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
            setProgress(75);

        }

    }

    //loading list view on click for each searched item
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
        public songData getItem(int position) {
            return songlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getColumn_id();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.list_layout, parent, false);
            songData currentsong = (songData) getItem(position);
            TextView sngnam = view.findViewById(R.id.textView);
        //    TextView sngid = view.findViewById(R.id.textView5);
          //  TextView artid = view.findViewById(R.id.textView6);
            sngnam.setText("Song Name:" + currentsong.getSong_name()+
                    "\nSong id:"+currentsong.getSong_id()+
                    "\nArtist id:"+currentsong.getArtist_id());
            //sngid.setText("Song id:" + currentsong.getSong_id());
            //artid.setText("Artist id:" + currentsong.getArtist_id());

//            sngid.setOnClickListener(onclick -> {
//                String d = "https://www.songsterr.com/a/wa/song?id=" + currentsong.getSong_id();
//                Intent songster_browser = new Intent(Intent.ACTION_VIEW);
//                songster_browser.setData(Uri.parse(d));
//                startActivity(songster_browser);
//            });
//            artid.setOnClickListener(onclick -> {
//                String d = "https://www.songsterr.com/a/wa/artist?id=" + currentsong.getArtist_id();
//                Intent songster_browser = new Intent(Intent.ACTION_VIEW);
//                songster_browser.setData(Uri.parse(d));
//                startActivity(songster_browser);
//
//            });
            setProgress(100);

            return view;

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        if(R.id.item1==item.getItemId()){
            AlertDialog.Builder alert=new AlertDialog.Builder(this);
            alert.setTitle("Welcome To Songsterr")
                    .setMessage("To search for your favourite artist or band songs"
                    +"type the name in the search bar and it will load the songs related to band or artist."
                    +"You can also see the information by clicking on the and also add them to favourite list by pressing favourite button"
                    +".By hitting the star next to search button it will load all your favourite songs.You can also remove the songs from your favourite list by long clicking them.")
                    .setNeutralButton("Dismiss",(click,b)->{
                    }).create().show();
        }
        return true;
    }




}


