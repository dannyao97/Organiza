package Organiza;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.google.gson.*;

/**
 *
 * @author Daniel
 */
public class DataGrabber
{
    public DataGrabber()
    {

    }

    private String createRequest()
    {
        String search = "http://www.omdbapi.com/?";
        String title = "t=how+to+train+your+dragon";
        String year = "y=";
        String plot = "plot=full";
        String tomatoes = "tomatoes=true";
        String r = "r=json";

        search += title;
        search += ("&" + year);
        search += ("&" + plot);
        search += ("&" + tomatoes);
        search += ("&" + r);

        System.out.println("SEARCH: [" + search + "]");
        return search;
    }

    public void getRequest()
    {
        try
        {
            String req = createRequest();

            URL url = new URL(req);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200)
            {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            JsonObject obj = new JsonParser().parse(br.readLine()).getAsJsonObject();

            String title = obj.get("Title").getAsString();
            String director = obj.get("Director").getAsString();
            String year = obj.get("Year").getAsString();
            String plot = obj.get("Plot").getAsString();
            String tomatoes = obj.get("tomatoRating").getAsString();
            String poster = obj.get("Poster").getAsString();

            System.out.println("TITLE: " + title);
            System.out.println("DIRECTOR: " + director);
            System.out.println("YEAR: " + year);
            System.out.println("PLOT: " + plot);
            System.out.println("TOMATOES: " + tomatoes);
            System.out.println("POSTER: " + poster);

            /*
            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null)
            {
                System.out.println(output);
            }
            */
            conn.disconnect();

        } catch (MalformedURLException e)
        {

            e.printStackTrace();

        } catch (IOException e)
        {

            e.printStackTrace();

        }
    }
}
