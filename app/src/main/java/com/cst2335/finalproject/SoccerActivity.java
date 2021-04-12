package com.cst2335.finalproject;
/**
 * * Author: Mohamed Hassan
 *  * Student number: 040988584
 *  * Professor: Islam Gomaa, Fedor Ilitchev
 *  * Course ID: CST 2335
 *  * Lab Section: 13
 *  * Porpuse: Mobile application for soccer news
 */

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * soccerActivity class inherits from AppcompatActivity
 *
 */
public class SoccerActivity extends AppCompatActivity {
    /**
     * this array is listed all the array attribute in the listItem
     * the adapter can give access to items and responsible to create
     * view for each item
     */
    String savedString;
    ArrayList<News> listItems=new ArrayList<>();
    MyListAdapter adapter = new MyListAdapter();
    Boolean IsPhone;
    String curr;
    Bitmap currPic;
    String CurrPicName;

    /**
     *the using of onCreate function and recreate the activity
     *it also used to load the data from savedInstanceState.
     * @param savedInstanceState
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer);
        FrameLayout frameLayout = findViewById(R.id.SoccerfragmentLocation);

        Toolbar tBar = findViewById(R.id.Soccertoolbr);
        setSupportActionBar(tBar);

        /**
         * sharedPreference is used to save the ratting input and read and write the text
         */

       SharedPreferences Moh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
         savedString = Moh.getString("ratting", "");
        if (frameLayout == null) { IsPhone = true; }
        else{            IsPhone = false;        }
        /**
         *AlertDialog is used to display the box message to ask user for application ratting at the beginning of the application
         */

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SoccerActivity.this);
        alertDialog.setTitle(getString(R.string.Rating));
        /**
         *Edit text for input user's rating.
         *rating is saved as savedString in SharedPreferences to show the next time when start the application
         */
        final EditText input = new EditText(SoccerActivity.this);
        input.setText(savedString);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setPositiveButton(getString(R.string.Yes),(dialog,which)->{

            savedString = input.getText().toString();
            dialog.cancel();
        });
        alertDialog.show();

        ListView myList = (ListView)findViewById(R.id.ListView1);
        myList.setAdapter(adapter);
        myList.setOnItemClickListener((parent, view, position, id)->{

            News news = listItems.get(position);
            //Create a bundle to pass data to the new fragment
            Bundle dataToPass = new Bundle();
            //dataToPass.putInt("id", listItems.get(position).getID() );
            dataToPass.putString("Title", listItems.get(position).getTitle() );
            dataToPass.putString("Date", listItems.get(position).getDate() );
            dataToPass.putString("Image", listItems.get(position).getImage() );

            boolean sendSide;
            // if the device is not phone (tablet) load the fragment
            if(!IsPhone)
            {
                SoccerDetailedFragment dFragment = new SoccerDetailedFragment();
                dFragment.setArguments( dataToPass );
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.SoccerfragmentLocation, dFragment)
                        .commit();
            }
            else
            {
                Intent nextActivity = new Intent(SoccerActivity.this, Soccer_Empty.class);
                nextActivity.putExtras(dataToPass);
                startActivityForResult(nextActivity,100);
            }

        });



        SoccerQuery soccerQuery=new SoccerQuery();
        soccerQuery.execute("https://www.goal.com/en/feeds/news?fmt=rss");
    }

    /**
     *
     */
    @Override
    /**
     * using onPause method as there is another activity has been launched
     */
    protected void onPause() {

        super.onPause();
        SharedPreferences Moh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit =Moh.edit();
        myEdit.putString("ratting",savedString);
        myEdit.commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.soccer_menu, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        //Look at your menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.item1:
                finish();
                message = getString(R.string.sccoerhome);
                break;
            case R.id.item2:
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.Instr))
                        .setMessage(getString(R.string.Instructions))
                        .setCancelable(false)

                        .setPositiveButton(getString(R.string.OK), (click, arg) -> {
                        })
                        .create().show();
                message = getString(R.string.shelp);
                break;
            case R.id.item3:
                Intent goTofav = new Intent(SoccerActivity.this, Soccer_Favorites_Activity.class);
                startActivity ( goTofav );
                message = getString(R.string.sFavo);
                break;
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        return true;
    }
    /**
     * SoccerQuery class inherited from AsyncTask.
     * it's job to mark a method in a annotated class as a Query method,
     * so that will be run when this method is called
     */
    private class SoccerQuery extends AsyncTask<String, Integer, String>{
        /**
         * doInBackground method consists of the query codes which needed to be executed in the background
         * @param strings
         * @return String
         */

        @Override
        protected String doInBackground(String... strings) {
            try{
                /**
                 * Url method is used to load the data form array like
                 */
                URL url= new URL(strings[0]);
                HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                InputStream response=urlConnection.getInputStream();
                XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp=factory.newPullParser();
                xpp.setInput(response,"UTF-8");

                News news=null;
                int status=0;
                int eventType=xpp.getEventType();
                while(eventType!=XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if(xpp.getName().equals("item")){
                            news=new News();
                        }
                        else if(xpp.getName().equals("title")){
                            if(news!=null) {
                                status = 1;
                            }
                        }else if(xpp.getName().equals("pubDate")){
                            if(news!=null) {
                                status = 2;
                            }
                        }
                        else if(xpp.getName().equals("media:thumbnail")){
                            news.image=xpp.getAttributeValue(null,"url");
                        }

                    }else if(eventType ==  XmlPullParser.TEXT){
                        if(status==1){
                            news.title=xpp.getText();
                        }else if(status==2){
                            news.date=xpp.getText();
                        }
                        status=0;

                    }else if(eventType ==  XmlPullParser.END_TAG){
                        if(xpp.getName().equals("item")){
                            listItems.add(news);
                        }

                    }
                    eventType=xpp.next();
                }

                publishProgress(100);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        /**
         * onProgressUpdate is used to display progress to the users while the task is running
         */
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        /**
         * onPostExecute used to animate the progress bar or show logs in a text field
         * it display any progress in the user interface.
         */
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     *MyListAdapter is a clause that inherited from BaseAdapter
     *which is job to get show the vertical list by using listView.
     */

    private class MyListAdapter extends BaseAdapter {
        /**
         * getCount is returning count total list of the adapter
         * @return ListItem.size
         */

        public int getCount() {
            return listItems.size();
        }

        /**
         * getItem is returning the object data from the items in order to access the adapter's data
         * @param position
         * @return listItems.get
         */

        public Object getItem(int position) {
            return listItems.get(position);
        }

        /**
         * the getItemId method is used  to access unique value has a data of long, which can be attached to item in the list
         * @param position
         * @return
         */

        public long getItemId(int position) {
            return position;
        }

        /**
         * the getView is calling the method to get view. it's job to create new view
         * @param position
         * @param old
         * @param parent
         * @return newView
         */

        public View getView(int position, View old, ViewGroup parent) {
            /**
             * LayoutInflater building the view object and taking the news_layout as a input
             */
            LayoutInflater inflater = getLayoutInflater();
            News news = listItems.get(position);
            View newView = null;

            newView = inflater.inflate(R.layout.news_layout, parent, false);
            TextView tView = newView.findViewById(R.id.newTxt);
            tView.setText(news.title);

            return newView;
        }
    }
}