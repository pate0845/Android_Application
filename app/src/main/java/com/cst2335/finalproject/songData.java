package com.cst2335.finalproject;
//saving the searched songs in database using the array
class songData{

    public boolean is_fav;
    public String song_name;
    public long artist_id,song_id,column_id;


    public songData(String song_name,long artist_id,long song_id){
        this.song_name=song_name;
        this.artist_id=artist_id;
        this.song_id=song_id;
    }

    public songData(String song_name,long artist_id,long song_id,long column_id){
        this.song_name=song_name;
        this.artist_id=artist_id;
        this.song_id=song_id;
        this.column_id=column_id;
    }
    public String getSong_name(){return song_name;}
    public long getArtist_id(){ return artist_id;}
    public boolean isIs_fav(){return is_fav;}
    public long getSong_id(){return song_id;}

    public void setColumn_id(long column_id) {
        this.column_id = column_id;
    }

    public long getColumn_id(){return column_id;}

}
