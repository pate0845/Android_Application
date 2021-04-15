package com.cst2335.finalproject;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Quizlayout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_layout);

        Bundle bundle = getIntent().getExtras();//instantiates and call the bundle values
        String Qvalue = bundle.getString("Question");//calls the question value
        TextView Questions = findViewById(R.id.txt2);
        Questions.setText(Qvalue);
        String OptAvalue = bundle.getString("OptionA");//calls the OptionA value
        TextView OptA = findViewById(R.id.optBtn1);
        OptA.setText(OptAvalue);
        String OptBvalue = bundle.getString("OptionB");//calls the OptionB value
        TextView OptB = findViewById(R.id.optBtn2);
        OptB.setText(OptBvalue);
        String OptCvalue = bundle.getString("OptionC");//calls the OptionC value
        TextView OptC = findViewById(R.id.optBtn3);
        OptC.setText(OptCvalue);
        String OptDvalue = bundle.getString("OptionD");//calls the OptionD value
        TextView OptD = findViewById(R.id.optBtn4);
        OptD.setText(OptDvalue);


    }
}