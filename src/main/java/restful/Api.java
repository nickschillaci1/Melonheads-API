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
     * @param filterType The type of filter to apply (id, title, artist etc.) null indicates return all songs
     * @param filter The value to filter by. null indicates return all songs
     * @return list of songs that fall within the filter
     */
    public abstract List<Song> getSongs(String filterType, String filter);


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

    public abstract boolean updateSong (
            int id,
            String title,
            String artist,
            String album,
            String url,
            String src
    );


}
