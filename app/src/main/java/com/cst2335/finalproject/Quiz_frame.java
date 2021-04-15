package com.cst2335.finalproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Quiz_frame extends AppCompatActivity {
    Intent intent;
    String Qnumber,type,level;
    public ProgressBar progressBar;
    ArrayList<Questions> listItems;
    TriviaListAdapter adapter = new TriviaListAdapter();
    public static final String Question = "Question";
    public static final String OptionA = "OptionA";
    public static final String OptionB = "OptionB";
    public static final String OptionC = "OptionC";
    public static final String OptionD = "OptionD";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_frame);

        ListView myList = findViewById(R.id.ListView1);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        intent=getIntent();

        EditText emailEditText=(EditText)findViewById(R.id.NameTxt) ;
        String recievedEmail= intent.getStringExtra("NAME");
        emailEditText.setText(recievedEmail);

        Qnumber = intent.getStringExtra("Number");
        type = intent.getStringExtra("Type");
        level = intent.getStringExtra("Level");
        Log.d("amal",""+Qnumber+level+type);
        TriviaQuiz req = new TriviaQuiz();
        req.execute("https://opentdb.com/api.php?amount="+Qnumber+"&difficulty="+level+"&type="+type);

        adapter.notifyDataSetChanged();
        listItems = new ArrayList<Questions>();
        myList.setAdapter(adapter);


        myList.setOnItemClickListener((parent, view, position, id)->{
            Questions Msg = listItems.get(position);
            //Create a bundle to pass data to the new fragment
            Bundle dataToPass = new Bundle();
            dataToPass.putString(Question,listItems.get(position).getQuestion());
            dataToPass.putString(OptionA,listItems.get(position).getCorrect());
            dataToPass.putString(OptionB,listItems.get(position).getOptB());
            dataToPass.putString(OptionC,listItems.get(position).getOptC());
            dataToPass.putString(OptionD,listItems.get(position).getOptD());
            Intent nextActivity = new Intent(Quiz_frame.this, Quizlayout.class);
            nextActivity.putExtras(dataToPass); //send data to next activity
            startActivity(nextActivity); //make the transition
        });

    }
    /**
     * TriviaListAdapter is a clause that inherited from BaseAdapter
     * which is job to get show the vertical lise by using listView.
     */
    private class TriviaListAdapter extends BaseAdapter {

        public int getCount() {
            return listItems.size();
        }

        public Object getItem(int position) {
            return listItems.get(position);
        }

        public long getItemId(int position) {
            return position;
        }


        public View getView(int position, View old, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

            Questions data = listItems.get(position);
            View newView = null ;
            newView = inflater.inflate(R.layout.row_layout, parent, false);
            TextView QView = newView.findViewById(R.id.ques);
            QView.setText(data.question);
            //TextView ansView = newView.findViewById(R.id.answer);
            //ansView.setText(data.wrongones);
            return newView;
        }
    }

    class TriviaQuiz extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                publishProgress(25);
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();
                //JSON reading:   Look at slide 26
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, StandardCharsets.UTF_8), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                String result = sb.toString(); //result is the whole string
                // convert string to JSON: Look at slide 27:
                JSONObject jObject = new JSONObject(result);

                //get the double associated with "value"
                JSONArray TriviaArray = jObject.getJSONArray("results");

                Log.d("amalraj",""+TriviaArray.length());
                publishProgress(50);

                for(int i=0;i<TriviaArray.length();i++){
                    JSONObject object=TriviaArray.getJSONObject(i);
                    /**
                     * declaring the data from JSON and make them as string object.
                     */
                    String choiceString = object.getString("incorrect_answers");
                    choiceString = choiceString.replace("\",\"",",");
                    String[] choiceArray = choiceString.split(",");

                    //Questions optData = new Questions(choiceArray[0],choiceArray[1],choiceArray[2]);

                    Log.d("amal",""+object.getString("correct_answer"));
                    Log.d("amal",""+object.getString("type"));
                    if(object.getString("type").equals("multiple")) {//checks if the type is a multiple or boolean
                        publishProgress(25);
                        Questions data = new Questions(
                                object.getString("question"),
                                object.getString("correct_answer"),
                                object.getString("incorrect_answers"),
                                choiceArray[0],
                                choiceArray[1],
                                choiceArray[2],
                                object.getString("type"),
                                object.getString("difficulty")
                        );
                        publishProgress(75);
                        listItems.add(data);
                    }else{
                        publishProgress(25);
                        Questions data = new Questions(
                                object.getString("question"),
                                object.getString("correct_answer"),
                                object.getString("incorrect_answers"),
                                choiceArray[0],
                                object.getString("type"),
                                object.getString("difficulty")
                        );
                        publishProgress(75);
                        listItems.add(data);
                    }
                    // this will add all data from JSON to the listItems
                    // listItems.add(optData)
                }
                publishProgress(100);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return "done";
        }

        private void publishProgress(int i) {
        }


        public void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }
        @Override
        public void onPostExecute(String s) {
            super.onPostExecute(s);
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);

        }
    }
}