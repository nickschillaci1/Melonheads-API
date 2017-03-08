package restful;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright Dr. Ganesh R. Baliga
 * All rights reserved.
 * Modified with permission by Nick Schillaci
 */
public class ApiImplementation extends Api {

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
        try (Connection conn = sql2o.open()) { //TODO: fix this sql connection (note: source -> src)
             songs =
                conn.createQuery("select city.name, city.population from city, country where "
                    + "city.countrycode=country.code and country.name=:countryName "
                    + "order by city.population desc;")
                    .addParameter("countryName", country)
                    .executeAndFetch(Song.class);
            return songs;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return songs;
    }

    @Override
    public boolean createSong (
            String title,
            String artist,
            String album,
            String URL,
            String source
    ) {
        try (Connection conn = sql2o.open()) { //TODO: fix these sql connections (note: source -> src)
            List<String> codes =
                conn.createQuery("select code from country where name=:countryName")
                    .addParameter("countryName", countryName)
                    .executeAndFetch(String.class);
            if (codes.size() != 1) {
                return false;
            }
            else {
                String code = codes.get(0);
                conn.createQuery("insert into city (name, countrycode, district, population) "
                    + "values (:name, :code, :district, :population);")
                    .addParameter("name", name)
                    .addParameter("code", code)
                    .addParameter("district", district)
                    .addParameter("population", population)
                    .executeUpdate();
                return true;

            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
