package com.cst2335.finalproject;

import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        //initializing edit text
        EditText email = findViewById(R.id.edittext);
        //using shared preferences to load email-id again when user comes
        sharedPreferences = getSharedPreferences("email", Context.MODE_PRIVATE);
        //saving the value to pass to other page
        String save_email = sharedPreferences.getString("email", "");
         email.setText(save_email);
        //putting the email in edittext
        //initializing progress bar
        ProgressBar progress = findViewById(R.id.progressBar);
        //initializing button
        Button btn = findViewById(R.id.button);
        //setting progress to 50%
        setProgress(25);
        //declaring nav bar
        NavigationView nav=findViewById(R.id.nav_vie);
        nav.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        /*using onclick listener to login and start other activity
         */
        btn.setOnClickListener(click -> {
            setProgress(100);
            Toast.makeText(getApplicationContext(), getString(R.string.toast_favlist_song_add), Toast.LENGTH_LONG);
            progress.setVisibility(View.INVISIBLE);
            Intent goToProfile = new Intent(this, SongsterAPI_View.class);
           goToProfile.putExtra("email", "" + email.getText().toString());
            startActivity(goToProfile);
        });
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
/*when the page is not open it will save the
data of the user from the edit text and loads when it will open
 */
    protected void onPause(){
        super.onPause();
        EditText login;
        setProgress(50);
        login=findViewById(R.id.edittext);
        //saving the email id
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("email",login.getText().toString());
        editor.apply();
    }
}
