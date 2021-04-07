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

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class Soccer_Favorites_Activity extends AppCompatActivity {
    SQLiteDatabase db;

    //public static SoccerListAdapter myAdapter;
    public static ArrayList<News> FavList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
               LoadDataFromDataBase();
        setContentView(R.layout.activity_soccer__favorites_);
        ListView listView = findViewById(R.id.SoccerFavoritlistView1);

        SoccerListAdapter myAdapter;
        listView.setAdapter(myAdapter = new SoccerListAdapter());

        listView.setOnItemLongClickListener((parent, view, position, id)->{
            News SoccerNew = FavList.get(position);
            AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(this);
            alertdialogbuilder.setTitle(getString(R.string.sDelete))
                    .setMessage(getString(R.string.soccerDelete)+ position )
                    .setPositiveButton(getString(R.string.yes), (click, arg)->{

                        deletenew(SoccerNew);
                        FavList.remove(position);
                        Snackbar snackbar = Snackbar
                                .make(listView,getString(R.string.instruction),Snackbar.LENGTH_LONG);
                                //.setAction(getString(R.string.undo) , (View.OnClickListener) view -> {
                               // });
                        snackbar.show();

                        Button finishButton = findViewById(R.id.finishbtn);
                        finishButton.setOnClickListener(c->finishButton.callOnClick());

                        myAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton(getString(R.string.no), (click, arg)->{
                        myAdapter.notifyDataSetChanged();
                    })
                    .create().show();

            return  true;
        });
    }

    private void LoadDataFromDataBase(){
        SoccerDataBase dbOpener = new SoccerDataBase(this);
        db = dbOpener.getWritableDatabase();
        String [] columns = {SoccerDataBase.Col_id, SoccerDataBase.Col_title, SoccerDataBase.Col_date, SoccerDataBase.Col_image};
        Cursor results = db.query(false, SoccerDataBase.Table_Name, columns, null, null, null, null, null, null);
        FavList.clear();
        int idColIndex = results.getColumnIndex(SoccerDataBase.Col_id);
        int titleColIndex = results.getColumnIndex(SoccerDataBase.Col_title);
        int dateColIndex = results.getColumnIndex(SoccerDataBase.Col_date);
        int imageColIndex = results.getColumnIndex(SoccerDataBase.Col_image);
        // iterate over the results
        while (results.moveToNext()){
            News Fav = new News( results.getInt(idColIndex),results.getString(titleColIndex),results.getString(dateColIndex),results.getString(imageColIndex));
            FavList.add(Fav);
        }
    }

    protected void deletenew(News news)    {
        String sq ="DELETE FROM " + SoccerDataBase.Table_Name + " WHERE "+ SoccerDataBase.Col_id +"='"+ news.getID() +"'";
        db.execSQL(sq);
    }

    class SoccerListAdapter extends BaseAdapter {


        //returns the number of items to display in the list
        @Override
        public int getCount() {
            return FavList.size();
        }

        // row position in the list
        @Override
        public News getItem(int position){
        //public Object  getItem(int position) {
           return FavList.get(position);
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
            LayoutInflater inflater = getLayoutInflater();

            newView = inflater.inflate(R.layout.activity_soccer__fav__layout, parent, false);

            TextView FavTiltle = newView.findViewById(R.id.Fav_title);
            TextView FavDate = newView.findViewById(R.id.Fav_date);


            FavTiltle.setText(FavList.get(position).getTitle());
            FavDate.setText(FavList.get(position).getDate());
            return newView;
        }
    }

}