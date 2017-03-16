package restful;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright Dr. Ganesh R. Baliga
 * All rights reserved.
 * Modified with permission by Nick Schillaci, and Rex Cummings
 */
public class ApiImplementation extends Api {

    Logger logger = LoggerFactory.getLogger(ApiImplementation.class);

    Sql2o sql2o;
    static ArrayList<String> auth = Auth.getDBAuthentication();
    static final String DB_URL = auth.get(0);
    static final String DB_NAME = auth.get(1);
    static final String DB_PASSWORD = auth.get(2);

    public ApiImplementation() {
        sql2o = new Sql2o(DB_URL, DB_NAME, DB_PASSWORD);
    }


    @Override
    public List<Song> getSongs(String filterType, String filter) {
        List<Song> songs = null;
        try (Connection conn = sql2o.open()) {
            switch(filterType) {
                case "title": // filter by title
                    songs =
                            conn.createQuery("" +
                                    "SELECT id, title, artist, album, url, src, upvotes, downvotes, plays " +
                                    "FROM songs WHERE title = :filter")
                                    .addParameter("filter", filter)
                                    .executeAndFetch(Song.class);
                    return songs;
                case "artist": // filter by artist
                    songs =
                            conn.createQuery("" +
                                    "SELECT id, title, artist, album, url, src, upvotes, downvotes, plays " +
                                    "FROM songs WHERE artist = :filter")
                                    .addParameter("filter", filter)
                                    .executeAndFetch(Song.class);
                    return songs;
                case "album": // filter by album
                    songs =
                            conn.createQuery("" +
                                    "SELECT id, title, artist, album, url, src, upvotes, downvotes, plays " +
                                    "FROM songs WHERE album = :filter")
                                    .addParameter("filter", filter)
                                    .executeAndFetch(Song.class);
                    return songs;
                case "all": // search all columns
                    songs =
                            conn.createQuery("" +
                                    "SELECT id, title, artist, album, url, src, upvotes, downvotes, plays " +
                                    "FROM songs WHERE :filter = title OR :filter = artist OR :filter = album")
                                    .addParameter("filter", filter)
                                    .executeAndFetch(Song.class);
                    return songs;
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }
        return songs;
    }

    @Override
    public boolean createSong (
            String title,
            String artist,
            String album,
            String url,
            String src
    ) {
        try (Connection conn = sql2o.open()) {
                conn.createQuery("insert into songs (title, artist, album, url, src) "
                    + "values (:title, :artist, :album, :url, :src);")
                    .addParameter("title", title)
                    .addParameter("artist", artist)
                    .addParameter("album", album)
                    .addParameter("url", url)
                        .addParameter("src", src)
                    .executeUpdate();
                return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
