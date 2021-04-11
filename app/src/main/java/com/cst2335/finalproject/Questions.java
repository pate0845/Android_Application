package com.cst2335.finalproject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Questions {
    public String question;
    public String optA;
    public String optB;
    public String optC;
    public String optD;
public String difficulty;

    public Questions(){

    }
    public Questions(String question, String optA){
        this.question = question;
        this.optA = optA;
       /** this.optB = optB;
        this.optC = optC;
        this.optD = optD;
        this.difficulty = difficulty;
        */
    }
    public String getQuestion(){
        return question;
    }
    public String getoptA(){
        return optA;
    }
    public String getoptB(){
        return optB;
    }
    public String getoptC(){
        return optC;
    }
    public String getoptD(){
        return optD;
    }
}
