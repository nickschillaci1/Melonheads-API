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
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;

public class Main {
    static Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {

        port(80);

        get("/hello", (req, res) -> {
                logger.debug("Get request: /hello");
                return "Hello World";
        });

        //TODO: check to make sure this endpoint works
        get("/songs/:filterType/:filter", (req, res) -> {

            String filterType = req.params("filterType"); // 0 for title, 1 for artist, 2 for album
            String filter = req.params("filter");
            logger.info("Get request: /songs"
                + " whose " + filterType
                + " matches \"" + filter + "\"");
            Api myapi = Api.getApi();
            List<Song> songs = myapi.getSongs(filterType, filter);

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
        //TODO: check to make sure this endpoint works
        put("songs/update", (request, response)-> {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try {
                JsonObject obj = new JsonParser().parse(request.body()).getAsJsonObject();

                int id = obj.get("id").getAsInt();
                String title = obj.get("title").getAsString();
                String artist = obj.get("artist").getAsString();
                String album = obj.get("album").getAsString();
                String url = obj.get("url").getAsString();
                String src = obj.get("src").getAsString();
                logger.info("Put request updating song object with id: " + id); //

                Api myapi = Api.getApi();


                if (myapi.updateSong(id, title, artist, album, url, src)) {
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
