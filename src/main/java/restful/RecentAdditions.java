package restful;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class holds recent song and playlist additions and handles the management
 * of the lists as new items are created. Some of this must be done externally
 * (file IO) so the ability to restart the server without losing data is preserved.
 */
public class RecentAdditions {

    private static final String DIRECTORY_NAME = "data/";
    private static final File RECENT_SONGS_FILE = new File(DIRECTORY_NAME + "recentSongIds.cfg");
    private static final File RECENT_PLAYLISTS_FILE = new File(DIRECTORY_NAME + "recentPlaylistIds.cfg");
    private static final int MAX_RECENT_SONGS = 15;
    private static final int MAX_RECENT_PLAYLISTS = 15;

    // values containing the id of an item, and its age
    private static HashMap<Integer, Integer> recentSongIds;
    private static HashMap<Integer, Integer> recentPlaylistIds;

    /**
     * Initialize the lists of recent additions so they can be retrieved by the running client.
     * These will need to be saved so we can utilize them through server reboots.
     */
    public static void createRecentAdditionLists() {
        try {
            // init directory and files
            new File(DIRECTORY_NAME).mkdir();

            recentSongIds = new HashMap<>();
            recentPlaylistIds = new HashMap<>();


            // read from or create song file
            if(RECENT_SONGS_FILE.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(RECENT_SONGS_FILE));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    recentSongIds.put(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
                }
            }
            else {
                RECENT_SONGS_FILE.createNewFile();
            }

            //read from or create playlist file
            if(RECENT_PLAYLISTS_FILE.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(RECENT_PLAYLISTS_FILE));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    recentSongIds.put(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
                }
            }
            else {
                RECENT_PLAYLISTS_FILE.createNewFile();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Loop through a HashMap and increment all the ages of its items.
     */
    private static void incrementItemAges(HashMap<Integer, Integer> map) {
        for(int id : map.keySet()) {
            map.put(id, map.get(id) + 1);
        }
    }

    /**
     * Clear the file containing recent additions and rewrite it
     * according to updated data.
     */
    private static void updateFile(File file, HashMap<Integer, Integer> map) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
            for (int id : map.keySet()) {
                bw.write(id + "," + map.get(id));
                bw.newLine();
            }
            bw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * If there are 10 recent songs, boot out the oldest song and add the new one.
     * If there are less than 10, just add the new one.
     */
    public static boolean onSongAdded(int songId) {
        incrementItemAges(recentSongIds);
        if(recentSongIds.size() < MAX_RECENT_SONGS) {
            recentSongIds.put(songId, 0);
        }
        else {
            int oldestId = 0;
            for (int id : recentSongIds.keySet()) {
                int age = recentSongIds.get(id);
                if (age > oldestId) {
                    oldestId = id;
                }
            }
            recentSongIds.remove(oldestId);
            recentSongIds.put(songId, 0);
        }
        updateFile(RECENT_SONGS_FILE, recentSongIds);
        return true;
    }

    /**
     * If there are 10 recent playlists, boot out the oldest playlist and
     * add the new one. If there are less than 10, just add the new one.
     */
    public static boolean onPlaylistAdded(int playlistId){
        incrementItemAges(recentPlaylistIds);
        if(recentPlaylistIds.size() < MAX_RECENT_PLAYLISTS) {
            recentPlaylistIds.put(playlistId, 0);
        }
        else {
            int oldestId = 0;
            for (int id : recentPlaylistIds.keySet()) {
                int age = recentPlaylistIds.get(id);
                if (age > oldestId) {
                    oldestId = id;
                }
            }
            recentPlaylistIds.remove(oldestId);
            recentPlaylistIds.put(playlistId, 0);
        }
        updateFile(RECENT_PLAYLISTS_FILE, recentPlaylistIds);
        return true;
    }

    /**
     * Used for returning values to the client;
     */
    public static ArrayList<Integer> getRecentSongIds() {
        ArrayList<Integer> ids = new ArrayList<>();
        for(int id : recentSongIds.keySet()) {
            ids.add(id);
        }
        return ids;
    }

    /**
     * Used for returning values to the client.
     */
    public static ArrayList<Integer> getRecentPlaylistIds() {
        ArrayList<Integer> ids = new ArrayList<>();
        for(int id : recentPlaylistIds.keySet()) {
            ids.add(id);
        }
        return ids;
    }

}
