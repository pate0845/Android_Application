package com.cst2335.finalproject;

public class Mydatabase {

        String Question;
        String Opt1;
        String Opt2;
        String Opt3;
        String Opt4;

        public Mydatabase( String Question, String Opt1,  String Opt2,  String Opt3, String Opt4) {
            this.Question = Question;
            this.Opt1 = Opt1;
            this.Opt2 = Opt2;
            this.Opt3 = Opt3;
            this.Opt4 = Opt4;
        }
        public String getQuestion(){
            return Question;
        }
    public String getOpt1(){
        return Opt1;
    }
    public String getOpt2(){
        return Opt2;
    }
    public String getOpt3(){
        return Opt3;
    }
    public String getOpt4(){
        return Opt4;
    }
}
