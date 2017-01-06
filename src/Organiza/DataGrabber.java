package Organiza;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
    protected Movie selectedMovie;

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
        File collectDir;
        String movieName;
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
                        for (String coll : movieDir.list())
                        {
                            collectDir = new File(movieDir + "\\" + coll);
                            movieName = coll.split("\\(|\\[")[0];
                            movieName.replaceAll("~", "/");
                            checkData(collectDir, movieName);
                        }
                    }
                    else
                    {
                        checkData(movieDir, movieName);
                    }
                }
                //break;
            }
        }
    }

    public void checkData(File movieDir, String movieName)
    {
        Movie movie;
        JsonObject obj;
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
                    if (!Arrays.asList(movieDir.list()).contains("poster.jpg"))
                    {
                        savePoster(movieDir.getPath(), obj);
                    }
                    movie.setPosterUrl(movieDir.getPath() + "/poster.jpg");

                    //Check for actual file
                    for (String playFile : movieDir.list())
                    {
                        if (playFile.endsWith(".mp4") || playFile.endsWith(".mkv"))
                        {
                            movie.playFile = movieDir.getPath() + "\\" + playFile;
                            break;
                        }
                    }
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

    public void writeToFile(String path, JsonObject obj)
    {
        try (FileWriter file = new FileWriter(path + "/data.json"))
        {
            //Save json file
            file.write(obj.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void savePoster(String path, JsonObject obj)
    {
        try
        {
            //Save poster image
            String posterURL = obj.get("Poster").getAsString();
            //High quality image url
            //posterURL = posterURL.substring(0, posterURL.lastIndexOf("_V1") + 3);

            URL url = new URL(posterURL);
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

    public void sortMovies(String sort)
    {
        switch (sort)
        {
            case "Title":
                Collections.sort(movieList, new Comparator<Movie>()
                {
                    public int compare(Movie m1, Movie m2)
                    {
                        String movie1 = m1.Title, movie2 = m2.Title;
                        if (m1.Title.startsWith("The"))
                        {
                            movie1 = movie1.replaceFirst("The ", "");
                        }
                        if (m2.Title.startsWith("The "))
                        {
                            movie2 = movie2.replaceFirst("The", "");
                        }
                        return movie1.compareTo(movie2);
                    }
                });
                break;
            case "Length":
                Collections.sort(movieList, new Comparator<Movie>()
                {
                    public int compare(Movie m1, Movie m2)
                    {
                        return m1.getLength() > m2.getLength() ? 1 : -1;
                    }
                });
                break;
            case "Year":
                Collections.sort(movieList, new Comparator<Movie>()
                {
                    public int compare(Movie m1, Movie m2)
                    {
                        return m1.getYear() > m2.getYear() ? 1 : -1;
                    }
                });
                break;
            default:
                break;
        }
    }

    public ArrayList<Movie> sortGenres(String genre)
    {
        ArrayList<Movie> genreMovies;
        
        if (genre.equals("All"))
        {
            genreMovies = movieList;
        }
        else
        {
            genreMovies = new ArrayList<>();
            for (Movie movie : movieList)
            {
                if (movie.Genre.contains(genre))
                {
                    genreMovies.add(movie);
                }
            }
        }
        return genreMovies;
    }
}
