package com.cst2335.finalproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class Car_Favorites_Activity extends AppCompatActivity {
    SQLiteDatabase db;

    //public static MyListAdapter myAdapter;
    public static ArrayList<CarData> CarFavList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_car__favorites_);
        ListView listView = findViewById(R.id.CarFavoritlistView1);
        LoadDataFromDataBase();
        CarListAdapter myCarAdapter;
        listView.setAdapter(myCarAdapter = new CarListAdapter());

        listView.setOnItemLongClickListener((parent, view, position, id)->{
            CarData CarDataDetaile = CarFavList.get(position);
            AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(this);
            alertdialogbuilder.setTitle(getString(R.string.cardelete))
                    .setMessage(getString(R.string.deletemessage)+ position )
                    .setPositiveButton(getString(R.string.yes), (click, arg)->{

                        deletdetailes(CarDataDetaile);
                        CarFavList.remove(position);

                        Button carfinishButton = findViewById(R.id.Carfinishbtn);
                        carfinishButton.setOnClickListener(c->carfinishButton.callOnClick());

                        myCarAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton(getString(R.string.no), (click, arg)->{
                        myCarAdapter.notifyDataSetChanged();
                    })
                    .create().show();

            return  true;
        });
        Button Favoritetbtn = findViewById(R.id.Carfinishbtn);
        Favoritetbtn.setOnClickListener(cc -> finish());
    }

    private void LoadDataFromDataBase(){
        CarDataBase dbOpener = new CarDataBase(this);
        db = dbOpener.getWritableDatabase();
        String [] columns = {CarDataBase.Col_id, CarDataBase.Col_MakeName, CarDataBase.Col_modelId, CarDataBase.Col_modelName};
        Cursor Carresults = db.query(false, CarDataBase.Table_Name, columns, null, null, null, null, null, null);
        CarFavList.clear();
        int MakeIDColIndex = Carresults.getColumnIndex(CarDataBase.Col_id);
        int MakeNameColIndex = Carresults.getColumnIndex(CarDataBase.Col_MakeName);
        int Model_IdColIndex = Carresults.getColumnIndex(CarDataBase.Col_modelId);
        int ModelIdColIndex = Carresults.getColumnIndex(CarDataBase.Col_modelName);
        // iterate over the Carresults
        while (Carresults.moveToNext()){
            CarData CarFav = new CarData( Carresults.getInt(MakeIDColIndex),Carresults.getString(MakeNameColIndex),Carresults.getInt(Model_IdColIndex),Carresults.getString(ModelIdColIndex));
            CarFavList.add(CarFav);
        }
    }

    protected void deletdetailes(CarData CarDat)    {
        String sq ="DELETE FROM " + CarDataBase.Table_Name + " WHERE "+ CarDataBase.Col_id +"='"+ CarDat.getId() +"'";
        db.execSQL(sq);
    }

    class CarListAdapter extends BaseAdapter {


        //returns the number of items to display in the list
        @Override
        public int getCount() {
            return CarFavList.size();
        }

        // row position in the list
        @Override
        public CarData getItem(int position){
            //public Object  getItem(int position) {
            return CarFavList.get(position);
        }

        //return the database ID
        @Override
        public long getItemId(int position) {
            return  position;
        }

        // how each row looks; BUTTON, TEXTVIEW, CHECKBOX
        @Override
        public View getView(int position, View old, ViewGroup parent) {
            View newView; //= old ;
            LayoutInflater Carinflater = getLayoutInflater();

            newView = Carinflater.inflate(R.layout.activity_car_fav__layout, parent, false);

            TextView FavName = newView.findViewById(R.id.CarFav_Name);
            TextView FavModel = newView.findViewById(R.id.CarFav_Model);


            FavName.setText(CarFavList.get(position).getCarName());
            FavModel.setText(CarFavList.get(position).getModelName());
            return newView;
        }
    }

}