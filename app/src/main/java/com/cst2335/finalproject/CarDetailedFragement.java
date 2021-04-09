package com.cst2335.finalproject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CarDetailedFragement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarDetailedFragement extends Fragment {
    Bitmap currePic;
    ImageView carimageview;
    Activity context;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CarDetailedFragement() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CarDetailedFragement.
     */
    // TODO: Rename and change types and number of parameters
    public static CarDetailedFragement newInstance(String param1, String param2) {
        CarDetailedFragement Carfragment = new CarDetailedFragement();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        Carfragment.setArguments(args);
        return Carfragment;
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
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_car_detailed_fragement, container, false);
        Bundle dataFromActivity;
        dataFromActivity = getArguments();
        View result =  inflater.inflate(R.layout.fragment_car_detailed, container, false);    // Inflate the layout for this fragment
        context = getActivity();
        TextView CarName = result.findViewById(R.id.CarId);                                  //Receive the news
        TextView Carname = result.findViewById(R.id.CarName);
        TextView ModelID = result.findViewById(R.id.ModelID);
        TextView Modelname = result.findViewById(R.id.ModelNametxt);

        CarName.setText(String.valueOf( dataFromActivity.getInt("id")));
        Carname.setText(dataFromActivity.getString("carName"));
        ModelID.setText(String.valueOf( dataFromActivity.getInt("modelID")));
        Modelname.setText(dataFromActivity.getString("modelName"));
        Button Favoritetbtn = result.findViewById(R.id.addV);

        Favoritetbtn.setOnClickListener(c ->
        {
            SQLiteDatabase db;
            CarDataBase dbOpener = new CarDataBase(context);
            db = dbOpener.getWritableDatabase();

            ContentValues Ccv = new ContentValues();
            Ccv.put(CarDataBase.Col_id, dataFromActivity.getString("id"));
            Ccv.put(CarDataBase.Col_MakeName, dataFromActivity.getString("carName"));
            Ccv.put(CarDataBase.Col_modelName,dataFromActivity.getString("modelName"));
            Ccv.put(CarDataBase.Col_modelId, dataFromActivity.getString("modelID"));

            long id = db.insert(CarDataBase.Table_Name,null , Ccv);
            Toast.makeText(context,getString(R.string.carsaved), Toast.LENGTH_SHORT).show();
        });

        return  result;


//            ImageView carimageview = result.findViewById(R.id.carimageview);
//            new CarImageLoadTask(dataFromActivity.getString("CarImage"), carimageview).execute();
//        return  result;
   }



//    class CarImageLoadTask  extends AsyncTask<Void, Void, Bitmap> {
//        private String CarUrl;
//        private ImageView carimageView;
//
//        public CarImageLoadTask(String Carurl, ImageView carimageView) {
//            this.CarUrl = Carurl;
//            this.carimageView = carimageView;
//        }
//
//        @Override
//        protected Bitmap doInBackground(Void... params) {
//            try {
//                URL urlConnection = new URL(CarUrl);
//                HttpURLConnection connection = (HttpURLConnection) urlConnection
//                        .openConnection();
//                connection.setDoInput(true);
//                connection.connect();
//                InputStream input = connection.getInputStream();
//                Bitmap carBitmap = BitmapFactory.decodeStream(input);
//                return carBitmap;
//            } catch (Exception e) {
//                e.printStackTrace();
//
//            }
//            return null;
//        }

//        @Override
//        protected void onPostExecute(Bitmap result) {
//            super.onPostExecute(result);
//
//            carimageView.setImageBitmap(result);
//
//        }
 //   }
}