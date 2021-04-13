package com.cst2335.finalproject;
/*saving the searched songs in database using the array
 */
class songData{
    public String song_name;
    public int artist_id,song_id,column_id;

    /*
    This constructor is used to save the data that is retrived from the JSON API
     */
    public songData(String song_name,int artist_id,int song_id,int column_id){
        this.song_name=song_name;
        this.artist_id=artist_id;
        this.song_id=song_id;
        this.column_id=column_id;
    }
    /*
    This method is used to get name of the current song
     */
    public String getSong_name(){return song_name;}
    /*
    This method is used to get the artist id
     */
    public int getArtist_id(){ return artist_id;}
    /*
    This method is used to recieve the song id
     */
    public int getSong_id(){return song_id;}
    /*
    This method is used to get the index
     */
    public int getColumn_id(){return column_id;}

}
