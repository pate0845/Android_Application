package com.cst2335.finalproject;

/**
 * public class News
 * with three variables title, date and image
 */
public class News {
    String title;
    String date;
    String image;

    /**
     * default constructor
     */

    public News(){

    }

    /**
     * argument constructor
     * @param title
     * @param date
     * @param image
     */
   public News (String title, String date, String image){
        this.title= title;
        this.date= date;
        this.image=image;
    }

    /**
     * getter method
     * @return title
     */
    public String getTitle(){
       return title;
    }

    /**
     * getter method
     * @return date
     */
    public String getDate(){
       return date;
    }

    /**
     * getter method
     * @return image
     */
    public String getImage(){
       return image;
    }
}
