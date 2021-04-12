package com.cst2335.finalproject;

 abdul_car
import android.content.Context;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
 master
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
 abdul_car

import android.widget.Toast;
 master

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SoccerDetailedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SoccerDetailedFragment extends Fragment {
    Bitmap currPic;
    ImageView imageview;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SoccerDetailedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SoccerDetailedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SoccerDetailedFragment newInstance(String param1, String param2) {
        SoccerDetailedFragment fragment = new SoccerDetailedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
 abdul_car


        Activity context= getActivity();

 master
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_soccer_detailed, container, false);
        Bundle dataFromActivity;
        dataFromActivity = getArguments();
        View result =  inflater.inflate(R.layout.fragment_soccer_detailed, container, false);    // Inflate the layout for this fragment



        TextView titletxt = result.findViewById(R.id.Newslbl);                                  //Receive the news
        TextView datetxt = result.findViewById(R.id.NewsDate);


        titletxt.setText(dataFromActivity.getString("Title"));
        datetxt.setText(dataFromActivity.getString("Date"));

        ImageView imageview = result.findViewById(R.id.soccerimageview);

        new SoccerImageLoadTask(dataFromActivity.getString("Image"), imageview).execute();

 abdul_car



        Button Favoritetbtn = result.findViewById(R.id.favbtn);
        Favoritetbtn.setOnClickListener(c ->
        {
            SQLiteDatabase db;
            SoccerDataBase dbOpener = new SoccerDataBase(context);
            db = dbOpener.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(SoccerDataBase.Col_title, dataFromActivity.getString("Title"));
            cv.put(SoccerDataBase.Col_date,dataFromActivity.getString("Date"));
            cv.put(SoccerDataBase.Col_image, dataFromActivity.getString("Image"));

            long id = db.insert(SoccerDataBase.Table_Name,null , cv);
            Toast.makeText(context,getString(R.string.dataSave),Toast.LENGTH_SHORT).show();
        });

 master
        return  result;
    }
    class SoccerImageLoadTask  extends AsyncTask<Void, Void, Bitmap> {
        private String url;
        private ImageView imageView;

        public SoccerImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }
        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            imageView.setImageBitmap(result);
        }
    }
}