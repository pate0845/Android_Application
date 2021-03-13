package com.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CarActivity extends AppCompatActivity {
    ArrayList<CarData> listItems;
    MyListAdapter adapter = new MyListAdapter();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        ListView myList = (ListView)findViewById(R.id.ListView1);
        Button searchButton = findViewById(R.id.Search);
        searchButton.setOnClickListener(e->{
            EditText searchText=findViewById(R.id.inputText);
            String manu=searchText.getText().toString();

        });
        listItems =  new ArrayList<CarData>();
        myList.setAdapter(adapter);

    }
    private class MyListAdapter extends BaseAdapter {

        public int getCount() {
            return listItems.size();
        }

        public Object getItem(int position) {
            return listItems.get(position);
        }

        public long getItemId(int position) {
            return listItems.get(position).id;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            CarData data = listItems.get(position);
            View newView= null;
            newView = inflater.inflate(R.layout.cars_items, parent,false);
            TextView carView = newView.findViewById(R.id.carNames);
            carView.setText(data.carName);
            TextView modelView = newView.findViewById(R.id.carModels);
            modelView.setText(data.modelName);
            return newView;
        }
    }
}