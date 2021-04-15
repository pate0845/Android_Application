package com.cst2335.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class IndexActivity extends AppCompatActivity {
    RadioButton trueORfalse,Multiple,Both,easy,medium,hard;
    RadioGroup radioGroup1,radioGroup2;
    String type,level;
    EditText Number;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        Button submitButton = findViewById(R.id.submitBtn);
        Number = findViewById(R.id.editTxt1);
        radioGroup1 = (RadioGroup)findViewById(R.id.gp1);
        trueORfalse = (RadioButton)findViewById(R.id.radioTF);
        Multiple = (RadioButton)findViewById(R.id.radioMc);
        Both = (RadioButton)findViewById(R.id.radioBoth);

        radioGroup2 = (RadioGroup)findViewById(R.id.gp2);
        easy = (RadioButton)findViewById(R.id.radioEasy);
        medium = (RadioButton)findViewById(R.id.radioMedium);
        hard = (RadioButton)findViewById(R.id.radioHard);
        String Num = Number.getText().toString();

        EditText NameField = (EditText) findViewById(R.id.editTxt10);
        //  EditText passField=(EditText) findViewById(R.id.editView3);
        SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String savedString = preferences.getString("Name", "");
        NameField.setText(savedString);

        radioGroup1.setOnCheckedChangeListener((group, checkedId) -> {
            if(trueORfalse.isChecked())
            {
                type = "boolean";
                Toast.makeText(IndexActivity.this, "trueORfalse", Toast.LENGTH_SHORT).show();
            }
            else if(Multiple.isChecked()) {
                type = "multiple";
                Toast.makeText(IndexActivity.this, "Multiple", Toast.LENGTH_SHORT).show();
            }
            else  {
                type = "multiple";
                Toast.makeText(IndexActivity.this, "Both", Toast.LENGTH_SHORT).show();
            }
        });
        radioGroup2.setOnCheckedChangeListener((group, checkedId) -> {
            if(easy.isChecked()) {
                level = "easy";
                Toast.makeText(IndexActivity.this, "easy", Toast.LENGTH_SHORT).show();
            }
            else if(medium.isChecked()) {
                level = "medium";
                Toast.makeText(IndexActivity.this, "medium", Toast.LENGTH_SHORT).show();
            }
            else  {
                level = "hard";
                Toast.makeText(IndexActivity.this, "hard", Toast.LENGTH_SHORT).show();
            }
        });
        submitButton.setOnClickListener( c ->{
            Intent goToProfile  = new Intent(IndexActivity.this,Quiz_frame.class);
            goToProfile .putExtra("Number", Number.getText().toString());
            goToProfile.putExtra("NAME",NameField.getText().toString().trim());
            goToProfile.putExtra("Type",type);
            goToProfile.putExtra("Level",level);
            startActivityForResult( goToProfile,345);
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        EditText NameField = (EditText) findViewById(R.id.editTxt10);
        SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String textEmailAddress=NameField.getText().toString().trim();
        editor.putString("NAME", textEmailAddress);
        editor.commit();
        editor.apply();

    }
}
