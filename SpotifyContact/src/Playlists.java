public class Playlists {
    private Playlist[] items;
    private int total;


    public int getTotal(){

        return total;
    }
    public void setTotal(int total){
        this.total = total;
    }
    public Playlist[] getItems(){
        return items;
    }

    public void setPlaylists(Playlist[] playlists){
        this.items = playlists;
    }
}
