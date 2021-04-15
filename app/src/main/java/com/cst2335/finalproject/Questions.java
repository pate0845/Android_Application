package com.cst2335.finalproject;

import android.util.Log;

import java.util.Arrays;

public class Questions {
    public String question;
    public String correct;
    public String wrongones;
    public String type;
    public String difficulty;
    public String optB;
    public String optC;
    public String optD;

    public Questions(String question, String correct,String wrongones,String optB,String optC,String optD,String type,String difficulty){
        this.question = question;
        this.correct = correct;
        this.wrongones = wrongones;
        this.type = type;
        this.difficulty = difficulty;
        this.optB = optB;
        this.optC = optC;
        this.optD = optD;
    }
    public Questions(String question, String correct,String wrongones,String optB,String type,String difficulty){
        this.question = question;
        this.correct = correct;
        this.wrongones = wrongones;
        this.type = type;
        this.difficulty = difficulty;
        this.optB = optB;
    }
    public String getQuestion(){ return question; }
    public String getCorrect(){
        return correct;
    }
    public String getType(){
        return type;
    }
    public String getDifficulty(){
        return difficulty;
    }
    public String getWrongones(){
        return wrongones;
    }

    public String getOptB(){ return optB; }
    public String getOptC(){ return optC; }
    public String getOptD(){ return optD;}
}
