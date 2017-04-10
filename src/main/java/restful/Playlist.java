package restful;

/**
 * Created by Rex Cummings.
 */
public class Playlist {

    private int id;
    private String title, songidlist;

    public Playlist() {

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSongIdList() {
        return songidlist;
    }

    public void setSongIdList(String songidlist) {
        this.songidlist = songidlist;
    }

}
