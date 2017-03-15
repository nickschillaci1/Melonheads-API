package restful;


import java.util.List;

/**
 * Copyright Dr. Ganesh R. Baliga
 * All rights reserved.
 * Modified with permission by Nick Schillaci, and Rex Cummings
 */

public abstract class Api {

    static Api theApi = null;

    public static Api getApi() {
        if (theApi == null)
            theApi = new ApiImplementation();
        return theApi;
    }

    /**
     * Returns all songs within the given filter
     * @param title The title of the song.
     * @param artist The artist of the desired song.
     * @param album The album of the song, if provided.
     * @return list of songs that fall within the filter
     */
    public abstract List<Song> getSongs(String title, String artist, String album);


    /**
     * Add a song to the database
     * @param title title of song
     * @param artist artist of the song
     * @param album album that the song appears in
     * @param url URL that the song is located at
     * @param src source website or service that the song is located at (used for embedding)
     * @return true if the song was added, false otherwise
     */

    public abstract boolean createSong (
        String title,
        String artist,
        String album,
        String url,
        String src
    );


}
