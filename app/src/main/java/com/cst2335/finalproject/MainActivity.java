package com.cst2335.finalproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        EditText email=findViewById(R.id.edittext);
        email.setText(email.getText().toString());
        ProgressBar progress=findViewById(R.id.progressBar);
        Button btn=findViewById(R.id.button);
        setProgress(50);
        btn.setOnClickListener(click->{
            setProgress(100);
            email.setText("");
            Toast.makeText(getApplicationContext(),"You are taken to search page",Toast.LENGTH_SHORT);
            progress.setVisibility(View.INVISIBLE);
        });



    }
}
