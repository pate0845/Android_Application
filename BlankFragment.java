package com.cst2335.finalproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
/*
@author Rutvik Patel
@version 3
This class deals with the fragment to show the information of the
song which user selects k
 */

public class BlankFragment extends Fragment {
    /*Declaring Bundle variable so that the data can be retrived
      and id to get the selected song id
     */
   private Bundle data;
   private long id;
   private AppCompatActivity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        data=getArguments();
        id=data.getInt(SongsterAPI_View.SONG_ID);
        View result=inflater.inflate(R.layout.fragment_blank,container,false);
        TextView sng_name,art_id,sng_id;
        Button play_btn;

        sng_name=result.findViewById(R.id.textView7);
        sng_id=result.findViewById(R.id.textView8);
        art_id=result.findViewById(R.id.textView9);
        play_btn=result.findViewById(R.id.play_button);



        sng_name.setText("Name:"+data.getString(SongsterAPI_View.SONG_NAME));
        sng_id.setText("Song id:"+id);
        art_id.setText("Artist id:"+data.getInt(SongsterAPI_View.ARTIST_ID));

        /*
          when the user selects the play button the activity will
          open the browser to play the song
         */
        play_btn.setOnClickListener(onclick->{
            String d = "https://www.songsterr.com/a/wa/song?id=" + data.getInt(SongsterAPI_View.SONG_ID);
            Intent songster_browser = new Intent(Intent.ACTION_VIEW);
            songster_browser.setData(Uri.parse(d));
            startActivity(songster_browser);
        });

        /*
        when the user selects the id this will start an intent to open
        browser to display the songs but artist
         */
        art_id.setOnClickListener(onclick->{
            String d = "https://www.songsterr.com/a/wa/artist?id=" + data.getInt(SongsterAPI_View.ARTIST_ID);
            Intent songster_browser = new Intent(Intent.ACTION_VIEW);
            songster_browser.setData(Uri.parse(d));
            startActivity(songster_browser);
        });

        /*
          when the user selects the song id the activity will
          open the browser to play the song
         */
        sng_id.setOnClickListener(onclick->{
            String d = "https://www.songsterr.com/a/wa/song?id=" + data.getInt(SongsterAPI_View.SONG_ID);
            Intent songster_browser = new Intent(Intent.ACTION_VIEW);
            songster_browser.setData(Uri.parse(d));
            startActivity(songster_browser);
        });

     return  result;

    }

    /*
    this method is used to know that our fragment has been attached to an
    activity
     */
    @Override
    public void onAttach( Context context) {
        super.onAttach(context);
        activity= (AppCompatActivity) context;
    }
}