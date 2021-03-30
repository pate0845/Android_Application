package com.cst2335.finalproject;

import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

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
        //putting the email in edittext

        email.setText(email.getText().toString());
        //initializing progress bar
        ProgressBar progress = findViewById(R.id.progressBar);
        //initializing button
        Button btn = findViewById(R.id.button);
        //setting progress to 50%
        setProgress(25);

        //using onclick listener to login and start other activity
        btn.setOnClickListener(click -> {
            setProgress(100);
            Toast.makeText(getApplicationContext(), "You are taken to search page on Click", Toast.LENGTH_LONG);
            progress.setVisibility(View.INVISIBLE);
            Intent goToProfile = new Intent(this, SongsterAPI_View.class);
           goToProfile.putExtra("email", "" + email.getText().toString());
            startActivity(goToProfile);
        });
    }


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
