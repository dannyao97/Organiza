package Organiza;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.google.gson.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 *
 * @author Daniel
 */
public class DataGrabber
{
    public DataGrabber()
    {

    }

    private String createRequest(String movie)
    {
        String search = "http://www.omdbapi.com/?";
        String title = "t=" + movie.replaceAll(" ", "+");
        String year = "y=";
        String plot = "plot=full";
        String tomatoes = "tomatoes=true";
        String r = "r=json";

        search += title;
        search += ("&" + year);
        search += ("&" + plot);
        search += ("&" + tomatoes);
        search += ("&" + r);

        //System.out.println("SEARCH: [" + search + "]");
        return search;
    }

    public JsonObject sendRequest(String movie)
    {
        try
        {
            String req = createRequest(movie);

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

            System.out.println(conn.getResponseMessage());
            /*String title = obj.get("Title").getAsString();
            String director = obj.get("Director").getAsString();
            String year = obj.get("Year").getAsString();
            String genre = obj.get("Genre").getAsString();
            String plot = obj.get("Plot").getAsString();
            String tomatoes = obj.get("tomatoRating").getAsString();
            String poster = obj.get("Poster").getAsString();

            System.out.println("TITLE: " + title);
            System.out.println("DIRECTOR: " + director);
            System.out.println("YEAR: " + year);
            System.out.println("GENRE: " + genre);
            System.out.println("PLOT: " + plot);
            System.out.println("TOMATOES: " + tomatoes);
            System.out.println("POSTER: " + poster);*/

            conn.disconnect();
            return obj;

        } catch (MalformedURLException e)
        {

            e.printStackTrace();

        } catch (IOException e)
        {

            e.printStackTrace();

        }
        return null;
    }

    public void getMovies()
    {
        String curDir = System.getProperty("user.dir") + "\\DATA\\Movies";
        File file = new File(curDir);
        File movieDir;
        String movie;
        JsonObject obj;
        String[] names = file.list();

        if (names == null)
        {
            System.out.println("MAKE SURE MOVIES ARE LOCATED IN 'DATA/Movies' spelled exactly.");
        } else
        {
            for (String name : names)
            {
                movieDir = new File(curDir + "\\" + name);
                if (movieDir.isDirectory())
                {
                    movie = name.split("\\(|\\[")[0];
                    if (movie.endsWith("Collection"))
                    {
                        System.out.println("**********************" + movie);
                    } else
                    {
                        System.out.println(movie);
                        obj = sendRequest(movie);
                        if (obj != null)
                        {
                            writeToFile(movieDir.getPath(), obj);
                            readFromFile(movieDir.getPath());
                        }
                    }

                    /*for (String f: movieDir.list())
                    {
                        System.out.println("---" + f);
                    }*/
                }
                break;
            }
        }
    }

    public void writeToFile(String path, JsonObject obj)
    {
        try (FileWriter file = new FileWriter(path + "\\data.json"))
        {
            file.write(obj.toString());
            System.out.println("Successfully Copied JSON Object to File...");
            System.out.println("\nJSON Object: " + obj);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void readFromFile(String path)
    {
        try
        {
            FileReader f = new FileReader(path + "\\data.json");
            BufferedReader br = new BufferedReader(f);         
            JsonObject obj = new JsonParser().parse(br.readLine()).getAsJsonObject();
            
            String title = obj.get("Title").getAsString();
            String director = obj.get("Director").getAsString();
            String year = obj.get("Year").getAsString();
            String genre = obj.get("Genre").getAsString();
            String plot = obj.get("Plot").getAsString();
            String tomatoes = obj.get("tomatoRating").getAsString();
            String poster = obj.get("Poster").getAsString();

            System.out.println("TITLE: " + title);
            System.out.println("DIRECTOR: " + director);
            System.out.println("YEAR: " + year);
            System.out.println("GENRE: " + genre);
            System.out.println("PLOT: " + plot);
            System.out.println("TOMATOES: " + tomatoes);
            System.out.println("POSTER: " + poster);         
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
