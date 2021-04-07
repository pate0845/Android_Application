package com.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class IndexActivity extends AppCompatActivity {
    RadioButton trueORfalse,Multiple,Both,easy,medium,hard;
    RadioGroup radioGroup1,radioGroup2;
    String type,level;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        EditText editText = findViewById(R.id.editTxt1);
        radioGroup1 = (RadioGroup)findViewById(R.id.gp1);
        trueORfalse = (RadioButton)findViewById(R.id.radioTF);
        Multiple = (RadioButton)findViewById(R.id.radioMc);
        Both = (RadioButton)findViewById(R.id.radioBoth);

        radioGroup2 = (RadioGroup)findViewById(R.id.gp2);
        easy = (RadioButton)findViewById(R.id.radioEasy);
        medium = (RadioButton)findViewById(R.id.radioMedium);
        hard = (RadioButton)findViewById(R.id.radioHard);

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


        Button submitButton = findViewById(R.id.submitBtn);
        submitButton.setOnClickListener( c ->{
            Intent goToProfile  = new Intent(IndexActivity.this, QuizActivity.class);
            goToProfile .putExtra("Number", editText.getText().toString());
            goToProfile.putExtra("Type",type);
            goToProfile.putExtra("Level",level);
            startActivityForResult( goToProfile,345);
        });
    }

}