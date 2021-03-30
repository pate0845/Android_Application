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

public class BlankFragment extends Fragment {
   private Bundle data;
   private long id;
   private AppCompatActivity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        data=getArguments();
        id=data.getLong(SongsterAPI_View.SONG_ID);
        View result=inflater.inflate(R.layout.fragment_blank,container,false);
        TextView sng_name,art_id,sng_id;
        Button fav_btn,play_btn;

        sng_name=result.findViewById(R.id.textView7);
        sng_id=result.findViewById(R.id.textView8);
        art_id=result.findViewById(R.id.textView9);
        fav_btn=result.findViewById(R.id.fav_button);
        play_btn=result.findViewById(R.id.play_button);


        sng_name.setText("Name:"+data.getString(SongsterAPI_View.SONG_NAME));
        sng_id.setText("Song id:"+id);
        art_id.setText("Artist id:"+data.getLong(SongsterAPI_View.ARTIST_ID));


        play_btn.setOnClickListener(onclick->{
            String d = "https://www.songsterr.com/a/wa/song?id=" + data.getLong(SongsterAPI_View.SONG_ID);
            Intent songster_browser = new Intent(Intent.ACTION_VIEW);
            songster_browser.setData(Uri.parse(d));
            startActivity(songster_browser);
        });

        art_id.setOnClickListener(onclick->{
            String d = "https://www.songsterr.com/a/wa/artist?id=" + data.getLong(SongsterAPI_View.ARTIST_ID);
            Intent songster_browser = new Intent(Intent.ACTION_VIEW);
            songster_browser.setData(Uri.parse(d));
            startActivity(songster_browser);
        });


        sng_id.setOnClickListener(onclick->{
            String d = "https://www.songsterr.com/a/wa/song?id=" + data.getLong(SongsterAPI_View.SONG_ID);
            Intent songster_browser = new Intent(Intent.ACTION_VIEW);
            songster_browser.setData(Uri.parse(d));
            startActivity(songster_browser);
        });

     return  result;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity= (AppCompatActivity) context;
    }
}