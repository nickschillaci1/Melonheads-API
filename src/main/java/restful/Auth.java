package restful;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Nick Schillaci
 */
public class Auth {

    static String dbFileName = "src/main/resources/dbi.cfg";

    public static ArrayList<String> getDBAuthentication() {
        ArrayList<String> lines = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(dbFileName))) {
            String line;
            while((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace(); //TODO: temporary solution to debug relevant exceptions
        }
        return lines;
    }

}
