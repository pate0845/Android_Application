package com.cst2335.finalproject;
/*saving the searched songs in database using the array
 */
class songData{
    public String song_name;
    public int artist_id,song_id,column_id;

    public songData(String song_name,int artist_id,int song_id,int column_id){
        this.song_name=song_name;
        this.artist_id=artist_id;
        this.song_id=song_id;
        this.column_id=column_id;
    }
    public String getSong_name(){return song_name;}
    public int getArtist_id(){ return artist_id;}

    public int getSong_id(){return song_id;}

    public int getColumn_id(){return column_id;}

}
