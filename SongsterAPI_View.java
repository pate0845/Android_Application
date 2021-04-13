package com.cst2335.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/*This class allows user to search for their artist or band
and save the data into the array which is then stored into the database and
it also allows user to add songs to their favourite list
 */
public class SongsterAPI_View extends AppCompatActivity {
    //Declaring variables
    EditText search;
    int artist_id, song_id;
    String song_title, display_toast;
    Button search_txt, fav_btn2;
    String URL_query;
    ListView list;
    ImageButton fav_btn;

    private ArrayList<songData> songlist = new ArrayList<songData>();
    private ArrayList<songData> fav_songlist = new ArrayList<>();
    private ContentValues searched_songs = new ContentValues();
    public static final String SONG_COL_ID = "ID";
    public static final String ARTIST_ID = "ARTIST";
    public static final String SONG_NAME = "SONG_NAME";
    public static final String SONG_ID = "SONG_ID";

    SongAdapter songAdapter;
    SQLiteDatabase db;
    ProgressBar progressBar;
    BlankFragment blankFragment = new BlankFragment();
    Bundle data2;
    boolean favourite = false;
    SharedPreferences sp;
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
        //using sharedpreferences to save and load the last search text
        sp=getSharedPreferences("Search",Context.MODE_PRIVATE);
        String artist_search=sp.getString("Search","");
        search.setText(artist_search);
        //declaring the navigation menu
        NavigationView nav=findViewById(R.id.nav_vie);
        nav.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        //initializing progress bar
        progressBar = findViewById(R.id.progressBar2);
        //initializing the favourite button
        fav_btn = findViewById(R.id.fav_btn);
        getSupportActionBar().setTitle("Final Project");
        //  fav_btn2=findViewById(R.id.fav_btn);
        FrameLayout frameLayout = findViewById(R.id.fragmentLocation);
        boolean result = frameLayout != null;

        //onClicklistener
        search_txt.setOnClickListener(click -> {
            favourite = false;
            fav_songlist.clear();
            URL_query = "https://www.songsterr.com/a/ra/songs.json?pattern=" + search.getText().toString();
            SongData exeq = new SongData();
            exeq.execute();
            list.setAdapter(songAdapter = new SongAdapter(this, songlist));
            songAdapter.notifyDataSetChanged();
            setProgress(25);
            songlist.clear();
            display_toast = search.getText().toString();
        });

        /*This onclick feature will load the list with
        array that has data of favourite song
         */
        fav_btn.setOnClickListener(onclick -> {
            songlist.clear();
            fav_songlist.clear();
            favourite = true;
            loadDatabase();
            if (fav_songlist.isEmpty()) {
                Toast.makeText(getApplicationContext(), getString(R.string.toast_favlist_song_add), Toast.LENGTH_SHORT).show();
                setProgress(100);
            }
            list.setAdapter(songAdapter = new SongAdapter(this, fav_songlist));
            songAdapter.notifyDataSetChanged();
            setProgress(100);
        });

        /*This long click listener will allow the user to either delete the song or add them to
        the favourite list or to remove them from the favourite list
         */
        list.setOnItemLongClickListener(((parent, view, position, id) -> {
            if (favourite == true) {
                songData loadfavourite = fav_songlist.get(position);
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("" + getString(R.string.delete))
                        .setMessage("" + getString(R.string.row) + position + "\n" +
                                "" + getString(R.string.data) + id)
                        .setNegativeButton(getString(R.string.delete_song), (click, arg) -> {
                            fav_songlist.remove(loadfavourite);
                            deletedatabase(loadfavourite);
                            songAdapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), fav_songlist.get(position).song_name + getString(R.string.toast_favlist_song_remove), Toast.LENGTH_SHORT).show();
                        })
                        .setNeutralButton(getString(R.string.dismiss), (click, b) -> {
                        })
                        .create().show();
            } else {
                songData loadMessage = songlist.get(position);
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("" + getString(R.string.delete))
                        .setMessage("" + getString(R.string.row) + position + "\n" +
                                "" + getString(R.string.data) + id)
                        .setNegativeButton(getString(R.string.delete_song), (click, arg) -> {
                            Toast.makeText(getApplicationContext(), songlist.get(position).song_name + getString(R.string.toast_simple_remove), Toast.LENGTH_SHORT).show();
                            songlist.remove(loadMessage);
                            songAdapter.notifyDataSetChanged();
                        })
                        .setNeutralButton(getString(R.string.dismiss), (click, b) -> {
                        })
                        .setPositiveButton(getString(R.string.addtofav), (click, areg) -> {
                            Song_Database database = new Song_Database(this);
                            db = database.getWritableDatabase();
                            searched_songs.put(Song_Database.COL_SONG_ID, songlist.get(position).song_id);
                            searched_songs.put(Song_Database.COL_ARTIST_ID, songlist.get(position).artist_id);
                            searched_songs.put(Song_Database.COL_SONG_NAME, songlist.get(position).song_name);
                            db.insert(Song_Database.TABLE_NAME, null, searched_songs);
                            Toast.makeText(getApplicationContext(), songlist.get(position).song_name + getString(R.string.addtofav), Toast.LENGTH_SHORT).show();
                            songlist.remove(loadMessage);
                            songAdapter.notifyDataSetChanged();
                        })

                        .create().show();
            }
            return true;

        }));


        /*This feature will load the fragment which displays the data about
        the song that userr selects from the list view
         */
        list.setOnItemClickListener((parent, view, position, id) -> {
            if (favourite == true) {
                Bundle favouritedata = new Bundle();
                favouritedata.putInt(SONG_COL_ID, (int) id);
                favouritedata.putString(SONG_NAME, fav_songlist.get(position).song_name);
                favouritedata.putInt(SONG_ID, fav_songlist.get(position).song_id);
                favouritedata.putInt(ARTIST_ID, fav_songlist.get(position).artist_id);
                if (result) {
                    BlankFragment fragment = new BlankFragment();
                    fragment.setArguments(favouritedata);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentLocation, fragment, Long.toString(id))
                            .commit();
                } else {
                    Intent newActivity = new Intent(SongsterAPI_View.this, EmptyActivity.class);
                    newActivity.putExtras(favouritedata);
                    startActivity(newActivity);
                }
            } else {
                Bundle data = new Bundle();
                data.putInt(SONG_COL_ID, (int) id);
                data.putString(SONG_NAME, songlist.get(position).song_name);
                data.putInt(SONG_ID, songlist.get(position).song_id);
                data.putInt(ARTIST_ID, songlist.get(position).artist_id);
                if (result) {
                    BlankFragment fragment = new BlankFragment();
                    fragment.setArguments(data);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentLocation, fragment, Long.toString(id))
                            .commit();
                } else {
                    Intent newActivity = new Intent(SongsterAPI_View.this, EmptyActivity.class);
                    newActivity.putExtras(data);
                    startActivity(newActivity);
                }
            }
        });

    }

    /*This method goes through the database and retrives all
    the rows from database and sae it to array
     */
    private void loadDatabase() {
        Song_Database database = new Song_Database(this);
        db = database.getWritableDatabase();
        String[] columns = {Song_Database.COL_ID, Song_Database.COL_SONG_ID, Song_Database.COL_ARTIST_ID,
                Song_Database.COL_SONG_NAME};

        Cursor results = db.query(false, Song_Database.TABLE_NAME, columns, null, null,
                null, null, null, null);
        int songid = results.getColumnIndex(Song_Database.COL_SONG_ID);
        int artistid = results.getColumnIndex(Song_Database.COL_ARTIST_ID);
        int indexid = results.getColumnIndex(Song_Database.COL_ID);
        int songname = results.getColumnIndex(Song_Database.COL_SONG_NAME);
        while (results.moveToNext()) {
            String song_name = results.getString(songname);
            int song_id = results.getInt(songid);
            int artist_id = results.getInt(artistid);
            int columnid = results.getInt(indexid);
            fav_songlist.add(new songData(song_name, artist_id, song_id, columnid));
        }
    }

    /*This method is used to delete the selected column
     */
    protected void deletedatabase(songData d) {
        db.delete(Song_Database.TABLE_NAME, Song_Database.COL_ID + "=?", new String[]{Long.toString(d.getColumn_id())});
    }


    /*getting the url and searching for songs or artist when the button is clicked
    the url is passed along with the
     */
    class SongData extends AsyncTask<String, Integer, String> {

        /*This method uses the url and reads the JSON data
        and put the data into the array
         */
        @Override
        public String doInBackground(String... args) {
            try {
                URL url = new URL(URL_query);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();
                BufferedReader read = new BufferedReader(new InputStreamReader(response, "UTF-8"),20);
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
                    songData songdata = new songData(song_title, artist_id, song_id, i + 1);
                    songlist.add(songdata);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }



        /*This will load all the data when all the back ground process is done
         */
        public void onPostExecute(String fromDoInBackground) {
            Log.i("HTTP", fromDoInBackground);
            songAdapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "Searching for " + display_toast, Toast.LENGTH_SHORT).show();
            setProgress(75);

        }

    }

    /*loading list view on click for each searched item
     */
    public class SongAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<songData> songlist;

        /*
        this constructor will be used to pass data
         */
        public SongAdapter(Context context, ArrayList<songData> songlist) {
            this.context = context;
            this.songlist = songlist;
        }

        /*
        this method will return the size of array
         */
        @Override
        public int getCount() {
            return songlist.size();
        }

        /*
        this method will get item position
        from songlist
         */
        @Override
        public songData getItem(int position) {
            return songlist.get(position);
        }

        /*
        this method will the the item id
         */
        @Override
        public long getItemId(int position) {
            return songlist.get(position).getColumn_id();
        }

        /*
        This method will load the list view layout
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.list_layout, parent, false);
            songData currentsong = (songData) getItem(position);
            TextView sngnam = view.findViewById(R.id.textView);
            sngnam.setText("Song Name:" + currentsong.getSong_name() +
                    "\nSong id:" + currentsong.getSong_id() +
                    "\nArtist id:" + currentsong.getArtist_id());
            setProgress(100);
            progressBar.setVisibility(View.INVISIBLE);
            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*
         Inflate the menu items for use in the action bar
         */
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    /*
    This method will look for user click and display alert on how to use the app
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message=null;
        switch (item.getItemId()) {
            case R.id.item1:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle(R.string.help6)
                        .setMessage(R.string.help).setNeutralButton(R.string.help5, (click, b) -> {
                }).create().show();
                message=getString(R.string.tool_menu);
                break;
            case R.id.item2:
                Intent gotosongster = new Intent(this, MainActivity.class);
                startActivity(gotosongster);
                message=getString(R.string.tool_menu2);
                break;
        }
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        return true;
    }

    /*
    Needed for the onNavigationItemSelected interface:
     */
    public boolean onNavigationItemSelected( MenuItem item) {
        Intent gotosongster = null;
        switch(item.getItemId())
        {
            case R.id.item2:
                gotosongster=new Intent(this,MainActivity.class);
                startActivity(gotosongster);
                break;
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layou);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
    /*
    This method will save the searched text so that when the user
     login again it will be used to load the searched text
     */
    @Override
    protected void onPause() {
        super.onPause();
        EditText search;
        search=findViewById(R.id.editText2);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("Search",search.getText().toString());
        editor.apply();
    }
}


