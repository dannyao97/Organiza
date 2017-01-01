package Organiza;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.google.gson.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.*;

/**
 *
 * @author Daniel
 */
public class DataGrabber
{
    protected ArrayList<Movie> movieList;

    public DataGrabber()
    {
        movieList = new ArrayList<>();
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
            System.out.print("Getting Movie Information: " + movie + "..........");
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

            if (obj.get("Response").getAsBoolean())
            {
                System.out.println(conn.getResponseMessage());
                conn.disconnect();
                return obj;
            }
            else
            {
                System.out.print("ERROR: ");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void populateMovies()
    {
        String curDir = System.getProperty("user.dir") + "\\DATA\\Movies";
        File file = new File(curDir);
        File movieDir;
        String movieName;
        Movie movie;
        JsonObject obj;
        String[] names = file.list();

        if (names == null)
        {
            System.out.println("MAKE SURE MOVIES ARE LOCATED IN 'DATA/Movies' spelled exactly.");
            System.out.println("Current Directory: " + System.getProperty("user.dir"));
        }
        else
        {
            for (String name : names)
            {
                movieDir = new File(curDir + "\\" + name);
                if (movieDir.isDirectory())
                {
                    movieName = name.split("\\(|\\[")[0];
                    movieName.replaceAll("~", "/");
                    if (movieName.endsWith("Collection"))
                    {
                        System.out.println("**********************" + movieName);
                    }
                    else
                    {
                        //System.out.println(movieName);
                        if (!Arrays.asList(movieDir.list()).contains("data.json"))
                        {
                            obj = sendRequest(movieName);
                            if (obj != null)
                            {
                                writeToFile(movieDir.getPath(), obj);
                            }
                            else
                            {
                                System.out.println("Could not find movie!");
                            }
                        }

                        if (Arrays.asList(movieDir.list()).contains("data.json"))
                        {
                            try
                            {
                                obj = readFromFile(movieDir.getPath());
                                if (obj != null)
                                {
                                    movie = new Movie(obj);
                                    movie.setPosterUrl(movieDir.getPath() + "/poster.jpg");
                                    movieList.add(movie);
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                                System.out.println("Movie Directory: " + movieDir.getPath());
                            }
                        }
                    }
                }
                //break;
            }
        }
    }

    public void writeToFile(String path, JsonObject obj)
    {
        try (FileWriter file = new FileWriter(path + "/data.json"))
        {
            //Save json file
            file.write(obj.toString());

            //Save poster image
            URL url = new URL(obj.get("Poster").getAsString());
            InputStream in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != (n = in.read(buf)))
            {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();

            FileOutputStream fos = new FileOutputStream(path + "/poster.jpg");
            fos.write(response);
            fos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public JsonObject readFromFile(String path)
    {
        try
        {
            FileReader f = new FileReader(path + "/data.json");
            BufferedReader br = new BufferedReader(f);
            JsonObject obj = new JsonParser().parse(br.readLine()).getAsJsonObject();

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
            System.out.println("POSTER: " + poster);
            System.out.println("");*/

 /*for (Map.Entry<String, JsonElement> e : obj.entrySet())
            {
                System.out.println(e.getKey() + " = obj.get(\"" + e.getKey() + "\").getAsString();");
            }*/
            return obj;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public Movie getMovie(String imdb)
    {
        for (Movie mov : movieList)
        {
            if (mov.imdbID.equals(imdb))
            {
                return mov;
            }
        }
        return null;
    }
}
