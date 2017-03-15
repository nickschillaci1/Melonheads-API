/**
 * Created by Dr. Baliga on 2/11/17.
 *
 * Modified by Rex Cummings, Nick Schillaci
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restful.Api;
import restful.Song;

import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class Main {
    static Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {

        get("/hello", (req, res) -> {
                logger.debug("Get request: /hello");
                return "Hello World";
        });

        //TODO: check to make sure this endpoint works
        get("/songs/:title/:artist/:album", (req, res) -> {

            String title = req.params("title");
            String artist = req.params("artist");
            String album = req.params("album");

            logger.info("Get request: /songs"
                + " for title " + title
                + " and for artist " + artist
                + " and for album " + album);
            Api myapi = Api.getApi();
            List<Song> songs = myapi.getSongs(title, artist, album);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(songs);
        });

        //TODO: check to make sure this endpoint works
        post("/songs", (request, response) -> {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            response.type("application/json");

            try {
                JsonObject obj = new JsonParser().parse(request.body()).getAsJsonObject();

                String title = obj.get("title").getAsString();
                String artist = obj.get("artist").getAsString();
                String album = obj.get("album").getAsString();
                String url = obj.get("url").getAsString();
                String src = obj.get("src").getAsString();
                logger.info("Post request: /songs"); //TODO: expand on the info written by the logger

                Api myapi = Api.getApi();


                if (myapi.createSong(title, artist, album, url, src)) {
                    response.status(200);
                    return gson.toJson(obj);
                }
                else {
                    response.status(400);
                    return(null);
                }

            } catch (Exception e) {
                response.status(404);
                return (gson.toJson(e));
            }

        });
        
    }
}
