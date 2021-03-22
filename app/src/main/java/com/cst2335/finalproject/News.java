package com.cst2335.finalproject;

public class News {
    String title;
    String date;
    String image;

    public News(){

    }
   public News (String title, String date, String image){
        this.title= title;
        this.date= date;
        this.image=image;
    }


    public String getTitle(){
       return title;
    }
    public String getDate(){
       return date;
    }
    public String getImage(){
       return image;
    }
}
