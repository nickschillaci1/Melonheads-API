/**
 * Created by Dr. Baliga on 2/11/17.
 */

import static spark.Spark.get;
import static spark.Spark.post;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restful.Api;
import restful.Song;

import java.util.List;

public class Main {
    static Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {

        get("/hello", (req, res) -> {
                logger.debug("Get request: /hello");
                return "Hello World";
        });

        //TODO: check to make sure this endpoint works
        get("/songs/:filterType:filter", (req, res) -> {
            String filterType = req.params("filterType");
            String filter = req.params("filter");
            logger.info("Get request: /songs"
                + " filtered by " + filterType
                + " matching \""
                + filter + "\"");
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
                String URL = obj.get("URL").getAsString();
                String source = obj.get("source").getAsString();

                Api myapi = Api.getApi();


                if (myapi.createSong(title, artist, album, URL, source)) {
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